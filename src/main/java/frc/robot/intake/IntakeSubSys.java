package frc.robot.intake;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubSys extends SubsystemBase {
    
    //Devices
    public WPI_TalonSRX intakeUpperMotor  = new WPI_TalonSRX(IntakeConfig.intakeUpperMotorCANID);
    public WPI_TalonSRX intakeLowerMotor  = new WPI_TalonSRX(IntakeConfig.intakeLowerMotorCANID);

    private AnalogInput coneDetectSensor = new AnalogInput(IntakeConfig.coneDetectPortID);
    private AnalogInput cubeDetectSensor = new AnalogInput(IntakeConfig.cubeDetectPortID);

    //contructor
    public IntakeSubSys() { 
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

    public void stopMotors() {
        intakeUpperMotor.stopMotor();
        intakeLowerMotor.stopMotor();
    }

    public void setMotorsConeRetract(){
        intakeUpperMotor.set(IntakeConfig.coneRetractSpeed);
        intakeLowerMotor.set(IntakeConfig.coneRetractSpeed);
    }

    public void setMotorsConeRetractSlow(){
        intakeUpperMotor.set(IntakeConfig.coneRetractSlowSpeed);
        intakeLowerMotor.set(IntakeConfig.coneRetractSlowSpeed);
    }

    public void setMotorsCubeRetract(){
        intakeUpperMotor.set(IntakeConfig.cubeRetractSpeed);
        intakeLowerMotor.set(IntakeConfig.cubeRetractSpeed);
    }

    public void setMotorsEject(){
        if (isCubeDetected()) {
            intakeUpperMotor.set(IntakeConfig.cubeEjectSpeed);
            intakeLowerMotor.set(IntakeConfig.cubeEjectSpeed);
        } else {
            intakeUpperMotor.set(IntakeConfig.coneEjectSpeed);  // top deals with cone
            intakeLowerMotor.set(IntakeConfig.coneEjectSpeed);  // bottom deals with cube
        }
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
        if(isConeDetected()) { 
            return "Detected"; } 
        return "Not Detected";
    }

    public boolean isCubeDetected() {
        if(cubeDetectSensor.getAverageVoltage() == IntakeConfig.cubeDetectTrue) { return true; } 
        return false;
    }

    public boolean isCubeNotDetected() {
        return !isCubeDetected();
    }

    public String cubeDetectStatus(){
        if(isCubeDetected()) { 
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
