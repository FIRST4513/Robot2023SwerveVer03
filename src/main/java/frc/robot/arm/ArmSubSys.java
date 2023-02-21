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
import frc.robot.arm.commands.ArmHoldPosition;

public class ArmSubSys extends SubsystemBase {
    public  static ArmSRXMotorConfig   motorConfig;
    public  WPI_TalonSRX        mArmMotor;
    private DigitalInput        upperlimitSwitch;
    private DigitalInput        lowerlimitSwitch;

    // Arm Variables
    public double mCurrArmPwr       = 0;
    public double mTargetArmAngle   = 0;       // PID Target Angle

    public double mCurrEncoderCnt   = 0; 
    public double mCurrArmAngle     = 0;

    public double hold_pwr = 0;

    // ------------- Constructor ----------
    public ArmSubSys() {
        motorConfig       = new ArmSRXMotorConfig();
        mArmMotor         = new WPI_TalonSRX(Motors.armMotorID);
        upperlimitSwitch  = new DigitalInput(LimitSwitches.armUpperLimitSw);
        lowerlimitSwitch  = new DigitalInput(LimitSwitches.armLowerLimitSw);
        armMotorConfig();
        stopArm();
        mArmMotor.configForwardSoftLimitThreshold(mCurrArmAngle);
    }

    // ------------- Periodic -------------
    public void periodic() {
        // DO NOT RESET ENCODERS at Ends of travel Init at begining and leave
        if (isLowerLimitSwitchPressed() == true) {
            resetEncoderAngle(ArmConfig.lowerLimitSwitchAngle);      // recalibrate encoder at retracted position
        }
        if (isUpperLimitSwitchPressed() == true) {
            resetEncoderAngle(ArmConfig.upperLimitSwitchAngle);      // recalibrate encoder at extended position
        }
        updateCurrentArmPosition();
    }

    // -----------------------------------------------------
    // ---------------- Arm Motor Methods ------------------
    // -----------------------------------------------------
    public void raiseArm() {
        setArmMotor(ArmConfig.kRaiseSpeed);
    }

    public void lowerArm() {
        setArmMotor(ArmConfig.kLowerSpeed);
    }

    public void holdArm()   {
        // set hold power as needed based on current angle ( 0 = full down)
        if ( Math.abs(mCurrArmAngle) < 10.0) {
            stopArm();
            return;
        }
        setArmMotor(getArbFeedFwd());
        mCurrArmPwr = hold_pwr;
    }

    public void stopArm()   { 
        mArmMotor.stopMotor();
        mCurrArmPwr = 0.0;
    }

    // ------------  Set Arm to Angle by Motion Magic  ----------
    public void setMMangle(double angle) {
        angle = limitArmAngle( angle );     // Limit range to max allowed
        mTargetArmAngle = angle;     // Store to be used in test for there yet
        double aff = getArbFeedFwd();
        if ( aff < 0.2 ) aff = 0.21;
        hold_pwr = Math.cos(Math.toRadians(angle)) * motorConfig.arbitraryFeedForwardScaler;
        mArmMotor.set(  ControlMode.MotionMagic, angle,
                        DemandType.ArbitraryFeedForward,
                        aff);
        }

    // ------------  Set Arm Manually during TeleOP  ----------
    public void setArmMotor( double pwr ) {
        // Limit power if needed
        if ( pwr > ArmConfig.kArmMotorRaiseMaxPwr) { pwr = ArmConfig.kArmMotorRaiseMaxPwr; }
        if ( pwr < ArmConfig.kArmMotorLowerMaxPwr) { pwr = ArmConfig.kArmMotorLowerMaxPwr; }

        if ( pwr > 0) {
            // check Upper Limit Switch hit
            if (isUpperLimitSwitchPressed()) {
                pwr = getArbFeedFwd();      // Upper Limit switch hit while raising so HOLD
            }
        } else {
            // check Lower Limit Switch hit
            if (isLowerLimitSwitchPressed()) {
                pwr = getArbFeedFwd();      // Lower Limit switch hit while lowering so HOLD
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
    
    public boolean isLowerLimitSwitchPressed() {
        if (lowerlimitSwitch.get() == ArmConfig.lowerLimitSwitchTrue) { return true; }
        return false;
    }

    public boolean isUpperLimitSwitchPressed() {
        if (upperlimitSwitch.get() == ArmConfig.upperLimitSwitchTrue) { return true; }
        return false;
    }

    public String lowerLimitSwitchStatus(){
        if (lowerlimitSwitch.get() == ArmConfig.lowerLimitSwitchTrue) { return "Pressed"; }
        return "Not Pressed";
    }

    public String upperLimitSwitchStatus() {
        if (upperlimitSwitch.get() == ArmConfig.upperLimitSwitchTrue) { return "Pressed"; }
        return "Not Pressed";
    }

    // --------------- Set Soft Limits -----------------
    public void softLimitsTrue() {
        mArmMotor.configReverseSoftLimitEnable(true);   // ????????????
    }

    public void softLimitsFalse() {
        mArmMotor.configReverseSoftLimitEnable(false);  // ?????????????
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
        if (angle > ArmConfig.maxArmAngle)      { angle = ArmConfig.maxArmAngle; }
        if (angle < ArmConfig.minArmAngle)      { angle = ArmConfig.minArmAngle; }
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

    public double getArbFeedFwd(){
        hold_pwr = -Math.sin(Math.toRadians(-mCurrArmAngle)) * motorConfig.arbitraryFeedForwardScaler;
        if (mCurrArmAngle > 0) {
            hold_pwr *= 0.75;
        }
        return hold_pwr;
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
        mArmMotor.setNeutralMode(ArmSRXMotorConfig.armNeutralMode);
        mArmMotor.setSelectedSensorPosition(0); // Zero Encoder
        mArmMotor.enableCurrentLimit(isArmInside());
    }
    
}
