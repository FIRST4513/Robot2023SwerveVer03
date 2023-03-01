package frc.robot.intake;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;

public class IntakeConfig {

    public static double coneDetectTrue = 2.25;         // 2.5; // IR Prox Volt greater than 2.25 means we see it.
    public static double cubeEjectDetectTrue = 1.25;   // IR Prox Volt greater than 2.25 means we see it.
    public static double cubeRetractDetectTrue = 2.25; // IR Prox Volt greater than 2.25 means we see it.

    public static double cubeRetractUpperSpeed = -0.625;
    public static double cubeRetractLowerSpeed = -0.625;
    public static double coneRetractUpperSpeed = -1.0;
    public static double coneRetractLowerSpeed = 1.0;
    public static double coneRetractUpperSlowSpeed = -0.35;
    public static double coneRetractLowerSlowSpeed = 0.35;
    public static double cubeEjectSpeed = 0.75;
    public static double coneEjectSpeed = -0.75;

    /* Neutral Modes */
    public static final NeutralMode intakeNeutralMode = NeutralMode.Coast;

    /* Inverts */
    public static final boolean upperIntakeMotorInvert = false;
    public static final boolean lowerIntakeMotorInvert = true;

    // increase to reduce jitter
    public static final int intakeAllowableError = 0;

    public static TalonSRXConfiguration intakeSRXConfig;     // TalonSRX Config objetct

    /* Intake Motor Current Limiting */
    public static final int     intakeContinuousCurrentLimit = 25;
    public static final int     intakePeakCurrentLimit       = 40;
    public static final int     intakePeakCurrentDuration    = 100;
    public static final boolean intakeEnableCurrentLimit     = true;

    /* Ramp Rate */
    public final double openLoopRamp = 0;
    public final double closedLoopRamp = 0;

    // --------------- Constuctor Setting Up Motor Config values -------------
    public IntakeConfig() {
        /* Intake Motor Configurations */
        intakeSRXConfig = new TalonSRXConfiguration();

        intakeSRXConfig.slot0.kP = 0;
        intakeSRXConfig.slot0.kI = 0;
        intakeSRXConfig.slot0.kD = 0;
        intakeSRXConfig.slot0.kF = 0;
        intakeSRXConfig.slot0.allowableClosedloopError = intakeAllowableError;
        intakeSRXConfig.openloopRamp                            = openLoopRamp;
        intakeSRXConfig.closedloopRamp                          = closedLoopRamp;
        intakeSRXConfig.continuousCurrentLimit         = intakeContinuousCurrentLimit;
        intakeSRXConfig.peakCurrentLimit               = intakePeakCurrentLimit;         
        intakeSRXConfig.peakCurrentDuration            = intakePeakCurrentDuration;
    }

}
