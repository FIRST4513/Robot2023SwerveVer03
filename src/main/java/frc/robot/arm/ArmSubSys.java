package frc.robot.arm;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConfig.LimitSwitches;

public class ArmSubSys extends SubsystemBase {
    private WPI_TalonFX mArmMotor = new WPI_TalonFX(ArmConfig.kMotorPort);
    private DigitalInput upperlimitSwitch = new DigitalInput(LimitSwitches.armUpperLimitSw);
    private DigitalInput lowerlimitSwitch = new DigitalInput(LimitSwitches.armLowerLimitSw);

    // ------------- Constructor ----------
    public ArmSubSys() {
        armMotorConfig();
        stopArm();
    }

    // ------------- Periodic -------------
    public void periodic() {
        if (isLowerLimitSwitchPressed() == true) {
            resetEncoder();
        }
    }

    // -----------------------------------------------------
    // ---------------- Arm Motor Methods ------------------
    // -----------------------------------------------------
    public void raiseArm() {
        if (isUpperLimitSwitchPressed() == false) {
            mArmMotor.set(ArmConfig.kRaiseSpeed);
        }else {
            holdArm();
        }
    }

    public void lowerArm() {
        if (isLowerLimitSwitchPressed() == false) {
            mArmMotor.set(ArmConfig.kLowerSpeed);
        }else {
            stopArm();
        }
    }

    public void holdArm() {
        mArmMotor.set(ArmConfig.kHoldSpeed);
    }

    public void stopArm() {
        mArmMotor.stopMotor();
    }
 
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

    public double getEncoder() {
        return mArmMotor.getSelectedSensorPosition();
    }

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
}

