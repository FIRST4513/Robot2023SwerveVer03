package frc.robot.operator;

public class OperatorGamepadConfig {
    public static final int port = 1;

    public static final boolean armYInvert = true;
    public static final boolean elevYInvert = true;

    public static final double intakeSpeedExp = 75;
    public static final double intakeSpeedScaler = -0.75; // -4.8;  // max velocity? // ~4.9 robot max
    public static final double intakeSpeedDeadband = 0.05;
    public static final double intakeSpeedOffset = 0;
}
