package frc.robot.intake;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConfig.AnalogPorts;
import frc.robot.RobotConfig.Motors;

public class IntakeSubSys extends SubsystemBase {
    public static IntakeConfig config;
    
    //Devices
    public WPI_TalonSRX intakeUpperMotor  = new WPI_TalonSRX(Motors.intakeUpperMotorID);
    public WPI_TalonSRX intakeLowerMotor  = new WPI_TalonSRX(Motors.intakeLowerMotorID);

    public AnalogInput coneDetectSensor = new AnalogInput(AnalogPorts.intakeConeDetectPort);
    public AnalogInput cubeDetectSensor = new AnalogInput(AnalogPorts.intakeCubeDetectPort);

    //contructor
    public IntakeSubSys() { 
        config = new IntakeConfig();
        configureTalonSRXControllers();
        stopMotors();
    } 

    // --------------------------------------------------------
    // ---------------- Intake Motor Methods ------------------
    // --------------------------------------------------------
    public void setMotors(double speed) {
        intakeUpperMotor.set(speed);
        intakeLowerMotor.set(speed); 
    }

    public void setUpperMotor(double speed) {
        intakeUpperMotor.set(speed);
    }

    public void setLowerMotor(double speed) {
        intakeLowerMotor.set(speed);
    }

    public void stopMotors() {
        intakeUpperMotor.stopMotor();
        intakeLowerMotor.stopMotor();
    }

    public void setMotorsConeRetract(){
        intakeUpperMotor.set(IntakeConfig.coneRetractUpperSpeed);
        intakeLowerMotor.set(IntakeConfig.coneRetractLowerSpeed);
    }

    public void setMotorsConeRetractSlow(){
        intakeUpperMotor.set(IntakeConfig.coneRetractUpperSlowSpeed);
        intakeLowerMotor.set(IntakeConfig.coneRetractLowerSlowSpeed);
    }

    public void setMotorsCubeRetract(){
        intakeUpperMotor.set(IntakeConfig.cubeRetractUpperSpeed);
        intakeLowerMotor.set(IntakeConfig.cubeRetractLowerSpeed);
    }

    // public void setMotorsEject(){
    //     if (isCubeDetected()) {
    //         intakeUpperMotor.set(IntakeConfig.cubeEjectSpeed);
    //         intakeLowerMotor.set(IntakeConfig.cubeEjectSpeed);
    //     } else {
    //         intakeUpperMotor.set(IntakeConfig.coneEjectSpeed);  // top deals with cone
    //         intakeLowerMotor.set(IntakeConfig.coneEjectSpeed);  // bottom deals with cube
    //     }
    // }

    public void setMotorsCubeEject() {
        intakeUpperMotor.set(IntakeConfig.cubeEjectSpeed);
        intakeLowerMotor.set(IntakeConfig.cubeEjectSpeed);
    }

    public void setMotorsConeEject() {
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
    // ---------------- Intake Detect Methods -------------------------
    // ----------------------------------------------------------------
    public boolean isConeDetected(){
        if(coneDetectSensor.getAverageVoltage() > IntakeConfig.coneDetectTrue) { return true; } 
        return false;
    }

    public String coneDetectStatus(){
        if(isConeDetected())    { return "Detected"; } 
        return "Not Detected";
    }

    public boolean isCubeEjectDetected() {
        if(cubeDetectSensor.getAverageVoltage() > IntakeConfig.cubeEjectDetectTrue) { return true; } 
        return false;
    }

    public boolean isCubeRetractDetected() {
        if(cubeDetectSensor.getAverageVoltage() > IntakeConfig.cubeRetractDetectTrue) { return true; } 
        return false;
    }

    public boolean isCubeRetractNotDetected() {
        return !isCubeRetractDetected();
    }

    public boolean isCubeEjectNotDetected() {
        return !isCubeRetractDetected();
    }

    public String cubeEjectDetectStatus() {
        if(isCubeEjectDetected()) { 
            return "Detected"; } 
        return "Not Detected";
    }

    public String cubeRetractDetectStatus() {
        if(isCubeEjectDetected()) { 
            return "Detected"; } 
        return "Not Detected";
    }

    // ----------------------------------------------------------
    // ---------------- Configure Intake Motor ------------------
    // ----------------------------------------------------------
    public void configureTalonSRXControllers(){
        // This config is for the Talon SRX Controllers

        // Upper Motor
        intakeUpperMotor.configFactoryDefault();
        intakeUpperMotor.configAllSettings(IntakeConfig.intakeSRXConfig);
        intakeUpperMotor.setInverted(IntakeConfig.upperIntakeMotorInvert);
        intakeUpperMotor.setNeutralMode(IntakeConfig.intakeNeutralMode);
        intakeUpperMotor.setSelectedSensorPosition(0);

        // Lower Motor
        intakeLowerMotor.configFactoryDefault();
        intakeLowerMotor.configAllSettings(IntakeConfig.intakeSRXConfig);
        intakeLowerMotor.setInverted(IntakeConfig.lowerIntakeMotorInvert);
        intakeLowerMotor.setNeutralMode(IntakeConfig.intakeNeutralMode);
        intakeLowerMotor.setSelectedSensorPosition(0);

    }
}
