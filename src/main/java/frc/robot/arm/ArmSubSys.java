package frc.robot.arm;

import java.util.function.DoubleSupplier;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.Rmath;
import frc.robot.RobotConfig.LimitSwitches;
import frc.robot.RobotConfig.Motors;

public class ArmSubSys extends SubsystemBase {
    public  static ArmSRXMotorConfig    motorConfig;
    public  WPI_TalonSRX                mArmMotor;
    private DigitalInput                extendLimitSwitch;
    private DigitalInput                retractLimitSwitch;

    // Arm Variables
    public double mCurrArmPwr       = 0;
    public double mTargetArmAngle   = 0;       // PID Target Angle

    public double mCurrEncoderCnt   = 0; 
    public double mCurrArmAngle     = 0;

    public double hold_pwr = 0;

    // ------------- Constructor ----------
    public ArmSubSys() {
        motorConfig         = new ArmSRXMotorConfig();
        mArmMotor           = new WPI_TalonSRX(Motors.armMotorID);
        extendLimitSwitch  = new DigitalInput(LimitSwitches.armExtendLimitSw);
        retractLimitSwitch  = new DigitalInput(LimitSwitches.armRetractLimitSw);
        armMotorConfig();
        stopArm();
        mArmMotor.configForwardSoftLimitThreshold(mCurrArmAngle);
    }

    // ------------- Periodic -------------
    public void periodic() {
        if (isRetractLimitSwitchPressed() == true) {
            resetEncoderAngle(ArmConfig.RetractLimitSwitchAngle);  // recalibrate encoder at retracted position
        }
        if (isExtendLimitSwitchPressed() == true) {
            resetEncoderAngle(ArmConfig.ExtendLimitSwitchAngle);   // recalibrate encoder at extended position
        }
        updateCurrentArmPosition();
    }

    // -----------------------------------------------------
    // ---------------- Arm Motor Methods ------------------
    // -----------------------------------------------------
    public void raiseArm() { setArmMotor(ArmConfig.kDefaultExtendPwr); }
    public void lowerArm() { setArmMotor(ArmConfig.kDefaultRetractPwr); }

    public void holdArm() {
        if ( Math.abs(mCurrArmAngle) < 8.0) {
            stopArm();
            return;
        }
        mCurrArmPwr = getHoldPwr();
        mArmMotor.set(mCurrArmPwr);       
    }

    public void stopArm()   { 
        mArmMotor.stopMotor();
        mCurrArmPwr = 0.0;
    }

    // ------------  Set Arm to Angle by Motion Magic  ----------
    public void setMMangle(double angle) {
        angle = limitArmAngle( angle );     // Limit range to max allowed
        mTargetArmAngle = angle;
        mArmMotor.set(  ControlMode.MotionMagic,            convertAngleToCnt(angle),
                        DemandType.ArbitraryFeedForward,    getHoldPwr());
        }

    // ------------  Set Arm Manually during TeleOP  ----------
    public void setArmMotor( double pwr ) {
        // Limit power if needed
        if ( pwr > ArmConfig.kExtendMaxPwr)  { pwr = ArmConfig.kExtendMaxPwr; }
        if ( pwr < ArmConfig.kRetractMaxPwr) { pwr = ArmConfig.kRetractMaxPwr; }

        if ( pwr > 0) {
            // Were extending so check if Extend Limit Switch has been hit
            if (isExtendLimitSwitchPressed()) {
                holdArm();
                return;
            }
        } else {
            // Were retracting check if Retract Lower Limit Switch has been hit
            if (isRetractLimitSwitchPressed()) {
                holdArm();
                return;
            }
        }
        mArmMotor.set(pwr);
        mCurrArmPwr = pwr;
    }

    public void setArmMotor(DoubleSupplier pwr) {
        setArmMotor(pwr.getAsDouble());
    }
 
    // -------- Set Brake Mode ----------
    public void setBrakeMode(Boolean enabled) {
        if (enabled) {
            mArmMotor.setNeutralMode(NeutralMode.Brake);
        } else {
            mArmMotor.setNeutralMode(NeutralMode.Coast);
        }
    }


    // ------------------------------------------------------------
    // ---------------- Arm Limit Switch Methods ------------------
    // ------------------------------------------------------------
    
    public boolean isRetractLimitSwitchPressed() {
        if (retractLimitSwitch.get() == ArmConfig.RetractLimitSwitchTrue) { return true; }
        return false;
    }

    public boolean isExtendLimitSwitchPressed() {
        if (extendLimitSwitch.get() == ArmConfig.ExtendLimitSwitchTrue)   { return true; }
        return false;
    }

    public String retractLimitSwitchStatus(){
        if (retractLimitSwitch.get() == ArmConfig.RetractLimitSwitchTrue) { return "Pressed"; }
        return "Not Pressed";
    }

    public String extendLimitSwitchStatus() {
        if (extendLimitSwitch.get() == ArmConfig.ExtendLimitSwitchTrue)   { return "Pressed"; }
        return "Not Pressed";
    }

    // --------------- Set Soft Limits -----------------
    public void softLimitsTrue() {
        mArmMotor.configReverseSoftLimitEnable(true);   // ????????????
        mArmMotor.configForwardSoftLimitEnable(true);   // ????????????
    }

    public void softLimitsFalse() {
        mArmMotor.configReverseSoftLimitEnable(false);  // ?????????????
        mArmMotor.configForwardSoftLimitEnable(false);  // ?????????????
    }

    // -------------------------------------------------------
    // ---------------- Arm Encoder Methods ------------------
    // -------------------------------------------------------
    
    public void updateCurrentArmPosition() {
        // Called from Periodic so only 1 CAN call is needed per command loop 20ms
        mCurrEncoderCnt = mArmMotor.getSelectedSensorPosition();
        mCurrArmAngle = Rmath.mRound((convertCntToAngle(mCurrEncoderCnt)) , 2);
     }

    public double getEncoderCnt()   { return mCurrEncoderCnt; }
    public double getArmAngle()     { return mCurrArmAngle;   }

    public double convertAngleToCnt( double angle )   { return angle * ArmConfig.kCntsPerDeg; }
    public double convertCntToAngle( double cnt )     { return cnt * ArmConfig.kDegsPerCnt; }


    public void resetEncoder()                    { mArmMotor.setSelectedSensorPosition(0); }
    public void resetEncoder( double position )   { mArmMotor.setSelectedSensorPosition(position); }
    public void resetEncoderAngle( double angle ) { mArmMotor.setSelectedSensorPosition(convertAngleToCnt(angle)); }    

    // ---- Misc Methods ----
    public double getTargetAngle()         { return mTargetArmAngle; }
    public double getArmMotorPwr()         { return mCurrArmPwr; }

     public double limitArmAngle( double angle ){
        if (angle > ArmConfig.ExtendLimitSwitchAngle)       { angle = ArmConfig.ExtendLimitSwitchAngle; }
        if (angle < ArmConfig.RetractLimitSwitchAngle)      { angle = ArmConfig.RetractLimitSwitchAngle; }
        return angle;
    }

    public boolean isMMtargetReached(){
        // If we are within the deadband of our target we can stop
        if (Math.abs(mTargetArmAngle-mCurrArmAngle) <= ArmConfig.KAngleDeadBand) { return true; }
        return false;
    }

    public boolean isArmInside() {
        if (getArmAngle() < 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isArmOutside() { return !isArmInside(); }

    public double getHoldPwr(){
        double pwr = Math.sin(Math.toRadians(mCurrArmAngle)) * motorConfig.arbitraryFeedForwardScaler;
        if (mCurrArmAngle > 0) {
            pwr *= 0.75;
        }
        return pwr;
    }

    // -------------------------------------------------------
    // ---------------- Configure Arm Motor ------------------
    // -------------------------------------------------------
    public void armMotorConfig(){
        // This config is for the Talon SRX Controller
        mArmMotor.configFactoryDefault();
        mArmMotor.configAllSettings(ArmSRXMotorConfig.config);
        mArmMotor.setInverted(ArmSRXMotorConfig.armMotorInvert);
        mArmMotor.setSensorPhase(ArmSRXMotorConfig.armEncoderInvert);
        mArmMotor.setNeutralMode(ArmSRXMotorConfig.armDefaultNeutralMode);
        mArmMotor.setSelectedSensorPosition(0); // Zero Encoder
        mArmMotor.enableCurrentLimit(isArmInside());
    }
    
}
