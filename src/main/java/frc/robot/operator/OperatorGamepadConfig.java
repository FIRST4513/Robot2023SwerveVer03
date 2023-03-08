package frc.robot.operator;

public class OperatorGamepadConfig {
    public static final int port = 1;

    public static final boolean armYInvert = true;
    public static final boolean elevYInvert = true;

    public static final double intakeSpeedExp = 25;
    public static final double intakeSpeedScaler = -0.75; // -4.8;  // max velocity? // ~4.9 robot max
    public static final double intakeSpeedDeadband = 0.05;
    public static final double intakeSpeedOffset = 0;

    public static final double elevSpeedExp = 15;
    public static final double elevSpeedScaler = -0.5; // -4.8;  // max velocity? // ~4.9 robot max
    public static final double elevSpeedDeadband = 0.07;
    public static final double elevSpeedOffset = 0;

    public static final double armSpeedExp = 15;
    public static final double armSpeedScaler = -0.75; // -4.8;  // max velocity? // ~4.9 robot max
    public static final double armSpeedDeadband = 0.175;
    public static final double armSpeedOffset = 0;


}
