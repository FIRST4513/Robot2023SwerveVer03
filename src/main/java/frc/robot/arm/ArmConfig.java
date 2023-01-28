package frc.robot.arm;

import frc.robot.RobotConfig.Encoders;
import frc.robot.RobotConfig.LimitSwitches;
import frc.robot.RobotConfig.Motors;

public class ArmConfig {
    // ------ Device Port Constants ------
    public final static int kMotorPort =            Motors.armMotorID;
    public final static int kLowerLimitSwitchPort = LimitSwitches.armLowerLimitSw;
    public final static int kUpperLimitSwitchPort = LimitSwitches.armUpperLimitSw;

    // ------ PID Constants ------
    public final double kElevatorKp = 0.4;      // orig example 5.0;
    public final double kElevatorKf = 0.05;
    public final double kMaxPwr = 1.00;

    // ------ Speed Constants ------
    public final static double kRaiseSpeed = 0.5;
    public final static double kLowerSpeed = -0.35;
    public final static double kHoldSpeed = 0.1;

    // ------ Limit Switch True States ------
    public final static boolean lowerLimitedSwitchTrue = true;
    public final static boolean upperlimitSwitchTrue = true;
}
