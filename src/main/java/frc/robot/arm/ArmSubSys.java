package frc.robot.arm;

//the funny code
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.Rmath;
import frc.robot.RobotConfig.LimitSwitches;

public class ArmSubSys extends SubsystemBase {
    public  WPI_TalonSRX mArmMotor = new WPI_TalonSRX(ArmConfig.kMotorPort);
    private DigitalInput upperlimitSwitch = new DigitalInput(LimitSwitches.armUpperLimitSw);
    private DigitalInput lowerlimitSwitch = new DigitalInput(LimitSwitches.armLowerLimitSw);

    private ArmTelemetry telemetry;

    public double mCurrEncoderCnt = 0; 
    public double mCurrArmAngle = 0;
    public double mTargetArmAngle = 0;       // PID Target Angle

    public final PIDController mArmPIDController;
    public double mPIDSetpoint = 0;
    public double mPIDOutput = 0;

    // ------------- Constructor ----------
    public ArmSubSys() {
        telemetry = new ArmTelemetry();
        //armMotorConfig();
        stopArm();
        mArmPIDController = new PIDController(ArmConfig.armKP, ArmConfig.armKI, ArmConfig.armKD);
    }

    // ------------- Periodic -------------
    public void periodic() {
        if (isLowerLimitSwitchPressed() == true) {
            resetEncoder();
        updateCurrentArmPosition();
        }
    }

    // -----------------------------------------------------
    // ---------------- Arm Motor Methods ------------------
    // -----------------------------------------------------
    public void raiseArm() {
        if (isUpperLimitSwitchPressed() == false) {
            setArmMotor(ArmConfig.kRaiseSpeed);
        }else {
            holdArm();
        }
    }

    public void lowerArm() {
        if (isLowerLimitSwitchPressed() == false) {
            setArmMotor(ArmConfig.kLowerSpeed);
        }else {
            stopArm();
        }
    }

    public void holdArm()   {
        setArmMotor(ArmConfig.kHoldSpeed);
    }

    public void stopArm()   { 
        mArmMotor.stopMotor();
    }

    public void setArmMotor( double pwr ) {
        if ( pwr > +ArmConfig.kArmMotorMaxPwr )  { pwr = +ArmConfig.kArmMotorMaxPwr; }
        if ( pwr < -ArmConfig.kArmMotorMaxPwr )  { pwr = -ArmConfig.kArmMotorMaxPwr; }
        mArmMotor.set(pwr);
    }
 
/*
    public void setBrakeMode(Boolean enabled) {
        if (enabled) {
            mArmMotor.setNeutralMode(NeutralMode.Brake);
        } else {
            mArmMotor.setNeutralMode(NeutralMode.Coast);
        }
    }
 */
    // ------------  Set Arm to Angle by PID  ----------
    public void setPIDArmToAngle( double angle ) {
        // Angle 0 = fully retracted  90 = fully extended
        angle = limitArmAngle( angle );                             // Dont allow an angle greater than permitted
        mPIDSetpoint = convertAngleToCnt(angle);                    // Convert Angle to Encoder Cnts
        mPIDOutput = mArmPIDController.calculate( mPIDSetpoint );   // Calculate PID Out to send to motor
        mPIDOutput = mPIDOutput + ArmConfig.armKF;                  // Add feedforward component
        setArmMotor( mPIDOutput );                                  // Send Power to motor  Pwr -1 to +1
        //m_motor.setVoltage(mPIDOutput);                           // Voltage -12 to +12 ???????        
    }

    // ------------------------------------------------------------
    // ---------------- Arm Limit Switch Methods ------------------
    // ------------------------------------------------------------
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

    //
    // The funny code
    public String UpperLimitSwitchStatus() {
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



    public double convertAngleToCnt( double angle)   { return angle * ArmConfig.kEncoderConversion; }
    public double convertCntToAngle( double cnt)     { return cnt / ArmConfig.kEncoderConversion; }
/* 
    // -------------------------------------------------------
    // ---------------- Configure Arm Motor ------------------
    // -------------------------------------------------------
    public void armMotorConfig(){
        //code blah blah blah blah aaaaaaaaa
        mArmMotor.configFactoryDefault();
        mArmMotor.configAllSettings(ArmConfig.armFXConfig);
        mArmMotor.setInverted(ArmConfig.armMotorInvert);
        mArmMotor.setNeutralMode(ArmConfig.armNeutralMode);
        mArmMotor.setSelectedSensorPosition(0);
    }
    */
}
//the funny :)