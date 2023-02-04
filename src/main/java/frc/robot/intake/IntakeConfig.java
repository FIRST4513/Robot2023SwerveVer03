package frc.robot.intake;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

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
    public static double cubeEjectSpeed = -0.25;
    public static double coneEjectSpeed = -0.25;

    /* Neutral Modes */
    public static final NeutralMode intakeNeutralMode = NeutralMode.Coast;

    /* Inverts */
    public static final boolean upperIntakeMotorInvert = false;
    public static final boolean lowerIntakeMotorInvert = false;

    /* Intake Motor PID Values */
    public static final double intakeKP = 0.1;
    public static final double intakeKI = 0.0;
    public static final double intakeKD = 0.0;
    public static final double intakeKF = 0.0;

    /* Intake Motor Characterization Values */
    public static final double intakeKS = ( 0.32 / 12.0 );  // /12 to convert from volts to %output
    public static final double intakeKV = ( 3.00 / 12.0 );
    public static final double intakeKA = ( 0.27 / 12.0 );

    // increase to reduce jitter
    public static final int intakeAllowableError = 0;

    public static TalonFXConfiguration intakeFXConfig;     // TalonFX Config objetct

    /* Intake Motor Current Limiting */
    public static final int intakeContinuousCurrentLimit = 25;
    public static final int intakePeakCurrentLimit = 40;
    public static final double intakePeakCurrentDuration = 0.1;
    public static final boolean intakeEnableCurrentLimit = true;


    // --------------- Constuctor Setting Up Motor Config values -------------
    public IntakeConfig() {
        /* Intake Motor Configurations */
        intakeFXConfig = new TalonFXConfiguration();
        SupplyCurrentLimitConfiguration intakeSupplyLimit =
                new SupplyCurrentLimitConfiguration(
                    IntakeConfig.intakeEnableCurrentLimit,
                    IntakeConfig.intakeContinuousCurrentLimit,
                    IntakeConfig.intakePeakCurrentLimit,
                    IntakeConfig.intakePeakCurrentDuration);

        intakeFXConfig.slot0.kP = IntakeConfig.intakeKP;
        intakeFXConfig.slot0.kI = IntakeConfig.intakeKI;
        intakeFXConfig.slot0.kD = IntakeConfig.intakeKD;
        intakeFXConfig.slot0.kF = IntakeConfig.intakeKF;
        intakeFXConfig.slot0.allowableClosedloopError = IntakeConfig.intakeAllowableError;
        intakeFXConfig.supplyCurrLimit = intakeSupplyLimit;
        intakeFXConfig.initializationStrategy = SensorInitializationStrategy.BootToZero;
    }

}
