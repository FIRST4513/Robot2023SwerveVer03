package frc.robot.intake;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import frc.robot.RobotConfig.AnalogPorts;
import frc.robot.RobotConfig.Motors;

public class IntakeConfig {

    public static int intakeUpperMotorCANID = Motors.intakeUpperMotorID;
    public static int intakeLowerMotorCANID = Motors.intakeLowerMotorID;

    public static int coneDetectPortID = AnalogPorts.intakeConeDetectPort;
    public static int cubeDetectPortID = AnalogPorts.intakeCubeDetectPort;

    public static double cubeDetectTrue = 2.25; // IR Prox Volt greator than 2.25 means we see it.
    public static double coneDetectTrue = 2.25; // IR Prox Volt greator than 2.25 means we see it.

    public static double cubeRetractSpeed = -0.25;
    public static double coneRetractSpeed = -0.25;
    public static double coneRetractSlowSpeed = -0.125;
    public static double cubeEjectSpeed = -0.25;
    public static double coneEjectSpeed = -0.25;

    /* Neutral Modes */
    public static final NeutralMode intakeNeutralMode = NeutralMode.Coast;

    /* Inverts */
    public static final boolean upperIntakeMotorInvert = false;
    public static final boolean lowerIntakeMotorInvert = false;

    // increase to reduce jitter
    public static final int intakeAllowableError = 0;

    public static TalonSRXConfiguration intakeSRXConfig;     // TalonSRX Config objetct

    /* Intake Motor Current Limiting */
    public static final int     intakeContinuousCurrentLimit = 25;
    public static final int     intakePeakCurrentLimit       = 40;
    public static final int     intakePeakCurrentDuration    = 100;
    public static final boolean intakeEnableCurrentLimit     = true;


    // --------------- Constuctor Setting Up Motor Config values -------------
    public IntakeConfig() {
        /* Intake Motor Configurations */
        intakeSRXConfig = new TalonSRXConfiguration();

        intakeSRXConfig.slot0.kP = 0;
        intakeSRXConfig.slot0.kI = 0;
        intakeSRXConfig.slot0.kD = 0;
        intakeSRXConfig.slot0.kF = 0;
        intakeSRXConfig.slot0.allowableClosedloopError = intakeAllowableError;
        intakeSRXConfig.continuousCurrentLimit         = intakeContinuousCurrentLimit;
        intakeSRXConfig.peakCurrentLimit               = intakePeakCurrentLimit;         
        intakeSRXConfig.peakCurrentDuration            = intakePeakCurrentDuration;
    }

}
