package frc.robot.arm;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.Rmath;
import frc.robot.RobotConfig.LimitSwitches;
import frc.robot.RobotConfig.Motors;

public class ArmSubSys extends SubsystemBase {
    public  static ArmSRXMotorConfig   motorConfig;
    public  WPI_TalonSRX        mArmMotor;
    private DigitalInput        upperlimitSwitch;
    private DigitalInput        lowerlimitSwitch;
    private PIDController       mArmPIDController;

    public double mCurrEncoderCnt   = 0; 
    public double mCurrArmAngle     = 0;
    public double mTargetArmAngle   = 0;       // PID Target Angle

    public double mPIDSetpoint      = 0;
    public double mPIDOutput        = 0;

    // ------------- Constructor ----------
    public ArmSubSys() {
        motorConfig       = new ArmSRXMotorConfig();
        mArmMotor         = new WPI_TalonSRX(Motors.armMotorID);
        upperlimitSwitch  = new DigitalInput(LimitSwitches.armUpperLimitSw);
        lowerlimitSwitch  = new DigitalInput(LimitSwitches.armLowerLimitSw);
        mArmPIDController = new PIDController(ArmConfig.kP, ArmConfig.kI, ArmConfig.kD);
        armMotorConfig();
        stopArm();
        mArmMotor.configForwardSoftLimitThreshold(mCurrArmAngle);
    }

    // ------------- Periodic -------------
    public void periodic() {
        if (isLowerLimitSwitchPressed() == true) {
            resetEncoder(ArmConfig.lowerLimitPos);      // recalibrate encoder at retracted position
        }
        if (isUpperLimitSwitchPressed() == true) {
            resetEncoder(ArmConfig.upperLimitPos);      // recalibrate encoder at extended position
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
        if ( Math.abs(mCurrArmAngle) < 5.0) {
            stopArm();
            return;
        }
        double hold_pwr = Math.cos(Math.toRadians(mCurrArmAngle)) * ArmConfig.kHoldkP;
        setArmMotor(hold_pwr);
    }

    public void stopArm()   { 
        mArmMotor.stopMotor();
    }

    public void setArmMotor( double pwr ) {
        // Limit power if needed
        if ( pwr > ArmConfig.kArmMotorRaiseMaxPwr) { pwr = ArmConfig.kArmMotorRaiseMaxPwr; }
        if ( pwr < ArmConfig.kArmMotorLowerMaxPwr) { pwr = ArmConfig.kArmMotorLowerMaxPwr; }

        // We're extending the arm
        if ( pwr > 0) {
            if (isUpperLimitSwitchPressed() == true) {
                stopArm();  // This is temporary until hold is tested
                //holdArm();
                return;
            }  
        }
        // We're retracting the arm
        else {
            if (isLowerLimitSwitchPressed() == true) {
                stopArm();  // This is temporary until hold is tested
                //holdArm();
                return;
            }
        }
        mArmMotor.set(pwr);
    }

    public void setArmMotor(DoubleSupplier pwr) {
        setArmMotor(pwr.getAsDouble());
    }
 
    public void setBrakeMode(Boolean enabled) {
        if (enabled) {
            mArmMotor.setNeutralMode(NeutralMode.Brake);
        } else {
            mArmMotor.setNeutralMode(NeutralMode.Coast);
        }
    }
 
    // ------------  Set Arm to Angle by PID  ----------
    public void setPIDArmToAngle( double angle ) {
        // Angle 0 = Straight down 90 = fully extended -90 = fully retracted
        angle = limitArmAngle( angle );    // Dont allow an angle greater than permitted

        double hold_pwr = Math.cos(Math.toRadians(angle)) * ArmConfig.kHoldkP;
                         
        mPIDSetpoint = convertAngleToCnt(angle);                    // Convert Angle to Encoder Cnts ??????
        mPIDOutput = mArmPIDController.calculate( mPIDSetpoint );   // Calculate PID Out to send to motor
        mPIDOutput = mPIDOutput + hold_pwr;                  // Add feedforward component

        setArmMotor( mPIDOutput );                                  // Send Power to motor  Pwr -1 to +1
        //m_motor.setVoltage(mPIDOutput);                           // Voltage -12 to +12 ???????        
    }

    // ------------  Set Arm to Angle by Motion Magic  ----------
    public double percentToFalcon(double percent) {
        return motorConfig.armMaxFalcon * (percent / 100);
    }

    public void setMMPercent(double percent) {
        setMMPosition( percentToFalcon(percent) );
    }

    public void setMMPosition(double angle) {
        //mArmMotor.set(ControlMode.MotionMagic, angle);
        double aff = Math.cos(Math.toRadians(angle)) * motorConfig.arbitraryFeedForwardScaler;
        mArmMotor.set(  ControlMode.MotionMagic, angle,
                        DemandType.ArbitraryFeedForward,
                        aff);
    }

    // ------------------------------------------------------------
    // ---------------- Arm Limit Switch Methods ------------------
    // ------------------------------------------------------------
    
    // --------------- Set Soft Limits -----------------
    public void softLimitsTrue() {
        mArmMotor.configReverseSoftLimitEnable(true);   // ????????????
    }

    public void softLimitsFalse() {
        mArmMotor.configReverseSoftLimitEnable(false);  // ?????????????
    }
    
    public boolean isLowerLimitSwitchPressed() {
        if (lowerlimitSwitch.get() == ArmConfig.lowerLimitSwitchTrue) {
            return true;
        }
        return false;
    }

    public boolean isUpperLimitSwitchPressed() {
        if (upperlimitSwitch.get() == ArmConfig.upperLimitSwitchTrue) {
            return true;
        }
        return false;
    }

    public String lowerLimitSwitchStatus(){
        if (lowerlimitSwitch.get() == ArmConfig.lowerLimitSwitchTrue) {
            return "Pressed";
        }
        return "Not Pressed";
    }

    public String upperLimitSwitchStatus() {
        if (upperlimitSwitch.get() == ArmConfig.upperLimitSwitchTrue) {
            return "Pressed";
        }
        return "Not Pressed";
    }
    
    // -------------------------------------------------------
    // ---------------- Arm Encoder Methods ------------------
    // -------------------------------------------------------

    public void resetEncoder() {
        // Sets encoder to 0
        mArmMotor.setSelectedSensorPosition(0);
    }

    public void resetEncoder( double position ){
        mArmMotor.setSelectedSensorPosition(position);
    }

    public double getEncoderCnt() {
        return mArmMotor.getSelectedSensorPosition();
    }

    public double getArmAngle() {
        return convertCntToAngle(getEncoderCnt());
    }

    public void updateCurrentArmPosition() {
        mCurrEncoderCnt = getEncoderCnt(); 
        mCurrArmAngle = Rmath.mRound((mCurrEncoderCnt * ArmConfig.kEncoderConversion) , 2);
     }

     public double limitArmAngle( double angle ){
        if (angle > ArmConfig.maxArmAngle)      { angle = ArmConfig.maxArmAngle; }
        if (angle < ArmConfig.minArmAngle)      { angle = ArmConfig.minArmAngle; }
        return angle;
    }

    public boolean isArmInside() {
        if (getArmAngle() < ArmConfig.ArmInsidePos) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isArmOutside() {
        return !isArmInside();
    }

    public double convertAngleToCnt( double angle)   { return angle * ArmConfig.kEncoderConversion; }
    public double convertCntToAngle( double cnt)     { return cnt / ArmConfig.kEncoderConversion; }

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
