package frc.robot.intake;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;

public class IntakeConfig {
    // IR Prox distance value for detection of a gamepiece
    public static double gamepieceDetectDistance = 1.0;

    // retract/eject speeds
    public static double retractSpeed = -1.0;
    public static double holdSpeed    = -0.25;
    public static double ejectSpeed   =  1.0;

    /* Neutral Modes */
    public static final NeutralMode intakeNeutralMode = NeutralMode.Coast;

    /* Inverts */
    public static final boolean intakeMotorInvert = false;

    // increase to reduce jitter
    public static final int intakeAllowableError = 0;

    public static TalonSRXConfiguration intakeSRXConfig;     // TalonSRX Config object

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
        intakeSRXConfig.openloopRamp                   = openLoopRamp;
        intakeSRXConfig.closedloopRamp                 = closedLoopRamp;
        intakeSRXConfig.continuousCurrentLimit         = intakeContinuousCurrentLimit;
        intakeSRXConfig.peakCurrentLimit               = intakePeakCurrentLimit;         
        intakeSRXConfig.peakCurrentDuration            = intakePeakCurrentDuration;
    }
}
