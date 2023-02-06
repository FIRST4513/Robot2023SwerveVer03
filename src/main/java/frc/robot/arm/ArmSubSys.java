package frc.robot.arm;

import java.util.function.DoubleSupplier;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.Rmath;
import frc.robot.RobotConfig.LimitSwitches;

public class ArmSubSys extends SubsystemBase {
    public  WPI_TalonSRX mArmMotor;
    private DigitalInput upperlimitSwitch;
    private DigitalInput lowerlimitSwitch;

    public double mCurrEncoderCnt   = 0; 
    public double mCurrArmAngle     = 0;
    public double mTargetArmAngle   = 0;       // PID Target Angle

    public final PIDController mArmPIDController;
    public double mPIDSetpoint      = 0;
    public double mPIDOutput        = 0;

    // ------------- Constructor ----------
    public ArmSubSys() {
        mArmMotor        = new WPI_TalonSRX(ArmConfig.kMotorPort);
        upperlimitSwitch = new DigitalInput(LimitSwitches.armUpperLimitSw);
        lowerlimitSwitch = new DigitalInput(LimitSwitches.armLowerLimitSw);
        mArmPIDController = new PIDController(ArmConfig.armKP, ArmConfig.armKI, ArmConfig.armKD);
        armMotorConfig();
        stopArm();
    }

    // ------------- Periodic -------------
    public void periodic() {
        if (isLowerLimitSwitchPressed() == true) {
            resetEncoder();
        }
        updateCurrentArmPosition();
    }

    // -----------------------------------------------------
    // ---------------- Arm Motor Methods ------------------
    // -----------------------------------------------------
    public void raiseArm() {
        if (isUpperLimitSwitchPressed() == true) {
            holdArm();
        }else {
            setArmMotor(ArmConfig.kRaiseSpeed);
        }
    }

    public void lowerArm() {
        if (isLowerLimitSwitchPressed() == true) {
            stopArm();
        }else {
            setArmMotor(ArmConfig.kLowerSpeed);
        }
    }

    public void holdArm()   {
        setArmMotor(ArmConfig.kHoldSpeed);
    }

    public void stopArm()   { 
        mArmMotor.stopMotor();
    }

    public void setArmMotor( double pwr ) {
        // Limit power if needed
        if ( pwr > ArmConfig.kHoldSpeed) {
            // Were raising Arm
            if ( pwr > ArmConfig.kArmMotorRaiseMaxPwr )  {
                 pwr = ArmConfig.kArmMotorRaiseMaxPwr;
            }    
        } else {
            // Were Lowering Arm
            if ( pwr < ArmConfig.kArmMotorLowerMaxPwr )  {
                 pwr = ArmConfig.kArmMotorLowerMaxPwr; }
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
        mArmMotor.configAllSettings(ArmConfig.armSRXConfig);
        mArmMotor.setInverted(ArmConfig.armMotorInvert);
        mArmMotor.setNeutralMode(ArmConfig.armNeutralMode);
        mArmMotor.setSelectedSensorPosition(0);                     // Reset Encoder to zero
    }
    
}
