package frc.robot.autoBalance;

public class AutoBalanceConfig {
    
    // Drive Values
    public static final double balancedAngle = 0;           // The angle the robot should be at when balanced
    public static final double balancedAngleTolerence = 3;  // Angle tollerence to determine if close enough
    public static final double kP = 0.015;                   // The proportional constant for the PID controller
    public static final double kPsin = 1.0;                // The proportional constant for the PID controller

    // Rotation Values
    public static final double angleSetPoint =
            0; // The angle the PID controller should try to reach
    public static final double kTurn = 0.007; // The constant for the turn PID controller
}
