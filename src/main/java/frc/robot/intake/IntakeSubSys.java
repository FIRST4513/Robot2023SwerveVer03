package frc.robot.intake;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubSys extends SubsystemBase {
    
    //Devices
    private WPI_TalonFX intakeUpperMotor = new WPI_TalonFX(IntakeConfig.intakeUpperMotorCANID);
    private WPI_TalonFX intakeLowerMotor = new WPI_TalonFX(IntakeConfig.intakeLowerMotorCANID);
    private DigitalInput coneDetectSwitch = new DigitalInput(IntakeConfig.coneDetectSwitchID);
    private DigitalInput cubeDetectSwitch = new DigitalInput(IntakeConfig.cubeDetectSwitchID);

    //contructor
    public IntakeSubSys() { 
        configureTalonMotors();
        stopMotors();
    } 

    // --------------------------------------------------------
    // ---------------- Intake Motor Methods ------------------
    // --------------------------------------------------------
    public void setMotors(double speed) {
        intakeUpperMotor.set(speed);
        intakeLowerMotor.set(speed); 
    }

    public void stopMotors() {
        intakeUpperMotor.stopMotor();
        intakeLowerMotor.stopMotor();
    }

    public void setMotorsConeRetract(){
        intakeUpperMotor.set(IntakeConfig.coneRetractSpeed);
        intakeLowerMotor.set(IntakeConfig.coneRetractSpeed);
    }

    public void setMotorsCubeRetract(){
        intakeUpperMotor.set(IntakeConfig.cubeRetractSpeed);
        intakeLowerMotor.set(IntakeConfig.cubeRetractSpeed);
    }

    public void setMotorsCubeEject(){
        intakeUpperMotor.set(IntakeConfig.cubeEjectSpeed);
        intakeLowerMotor.set(IntakeConfig.cubeEjectSpeed);
    }

    public void setMotorsConeEject(){
        intakeUpperMotor.set(IntakeConfig.coneEjectSpeed);
        intakeLowerMotor.set(IntakeConfig.coneEjectSpeed);
    }

    public void setBrakeMode(Boolean enabled) {
        if (enabled) {
            intakeUpperMotor.setNeutralMode(NeutralMode.Brake);
            intakeLowerMotor.setNeutralMode(NeutralMode.Brake);
        } else {
            intakeUpperMotor.setNeutralMode(NeutralMode.Coast);
            intakeLowerMotor.setNeutralMode(NeutralMode.Coast);
        }
    }

    // ----------------------------------------------------------------
    // ---------------- Intake Detect Switch Methods ------------------
    // ----------------------------------------------------------------
    public boolean isConeDetectSwitchPressed(){
        if(coneDetectSwitch.get() == IntakeConfig.coneDetectTrue) { return true; } 
        return false;
    }

    public String coneDetectSwitchStatus(){
        if(coneDetectSwitch.get() == IntakeConfig.coneDetectTrue) { 
            return "Pressed"; } 
        return "Not Pressed";
    }

    public boolean isCubeDetectSwitchPressed() {
        if(cubeDetectSwitch.get() == IntakeConfig.cubeDetectTrue) { return true; } 
        return false;
    }

    public String cubeDetectSwitchStatus(){
        if(cubeDetectSwitch.get() == IntakeConfig.cubeDetectTrue) { 
            return "Pressed"; } 
        return "Not Pressed";
    }

    // -------------------------------------------------------
    // ---------------- Configure Arm Motor ------------------
    // -------------------------------------------------------
    public void configureTalonMotors(){
        // Upper Motor
        intakeUpperMotor.configFactoryDefault();
        intakeUpperMotor.configAllSettings(IntakeConfig.intakeFXConfig);
        intakeUpperMotor.setInverted(IntakeConfig.upperIntakeMotorInvert);
        intakeUpperMotor.setNeutralMode(IntakeConfig.intakeNeutralMode);
        intakeUpperMotor.setSelectedSensorPosition(0);

        // Lower Motor
        intakeLowerMotor.configFactoryDefault();
        intakeLowerMotor.configAllSettings(IntakeConfig.intakeFXConfig);
        intakeLowerMotor.setInverted(IntakeConfig.lowerIntakeMotorInvert);
        intakeLowerMotor.setNeutralMode(IntakeConfig.intakeNeutralMode);
        intakeLowerMotor.setSelectedSensorPosition(0);

    }
}
