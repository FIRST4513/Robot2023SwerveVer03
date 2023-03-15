package frc.robot.intake;

import java.util.function.DoubleSupplier;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConfig.AnalogPorts;
import frc.robot.RobotConfig.Motors;

public class IntakeSubSys extends SubsystemBase {
    public static IntakeConfig config;
    public static String intakeBrakeStatus = "";
    
    // Devices
    public WPI_TalonSRX intakeMotor = new WPI_TalonSRX(Motors.intakeUpperMotorID);

    public AnalogInput gamepieceDetectSensor = new AnalogInput(AnalogPorts.intakeGamepieceSensor);

    // Constructor
    public IntakeSubSys() { 
        config = new IntakeConfig();
        configureTalonSRXControllers();
        stopMotors();
        setBrakeMode(true);
    } 

    // --------------------------------------------------------
    // ---------------- Intake Motor Methods ------------------
    // --------------------------------------------------------

    public void setMotor(double speed)         { intakeMotor.set(speed); }
    public void setMotor(DoubleSupplier speed) { setMotor(speed.getAsDouble()); }
    public double getMotorSpeed()              { return intakeMotor.get(); }

    public void stopMotors() {
        setBrakeMode(true);
        intakeMotor.stopMotor();
    }

    // ---------- Set Motor Methods ----------
    public void setMotorRetract(){
        intakeMotor.set(IntakeConfig.retractSpeed);
    }

    public void setMotorEject() {
        intakeMotor.set(IntakeConfig.ejectSpeed);
    }

    public void setMotorHold() {
        intakeMotor.set(IntakeConfig.holdSpeed);
    }

    // ------ Set Brake Modes ---------
    public void setBrakeMode(Boolean enabled) {
        if (enabled) {
            intakeMotor.setNeutralMode(NeutralMode.Brake);
            intakeBrakeStatus = "Intake Brake On";
        } else {
            intakeMotor.setNeutralMode(NeutralMode.Coast);
            intakeBrakeStatus = "Intake Brake Off";
        }
    }

    public String getBrakeStatus(){
        return intakeBrakeStatus;
    }

    // ----------------------------------------------------------------
    // ---------------- Intake Detect Methods -------------------------
    // ----------------------------------------------------------------

    // ---------- General Gamepiece Detects ----------
    public boolean isGamepieceDetected() {
        if (getSensorVal() > IntakeConfig.gamepieceDetectDistance) { return true; }
        return false;
    }

    public boolean isGamepieceNotDetected() {
        return !isGamepieceDetected();
    }

    public double getSensorVal() {
        return gamepieceDetectSensor.getAverageVoltage();
    }

    public String gamepieceDetectStatus() {
        if(isGamepieceDetected()) { 
            return "Detected"; } 
        return "Not Detected";
    }

    // ----------------------------------------------------------
    // ---------------- Configure Intake Motor ------------------
    // ----------------------------------------------------------
    public void configureTalonSRXControllers(){
        // This config is for the Talon SRX Controller(s)

        // The only motor
        intakeMotor.configFactoryDefault();
        intakeMotor.configAllSettings(IntakeConfig.intakeSRXConfig);
        intakeMotor.setInverted(IntakeConfig.intakeMotorInvert);
        intakeMotor.setNeutralMode(IntakeConfig.intakeNeutralMode);
        intakeMotor.setSelectedSensorPosition(0);
    }
}
