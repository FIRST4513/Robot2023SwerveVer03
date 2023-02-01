package frc.robot.intake;

import frc.robot.RobotConfig.LimitSwitches;
import frc.robot.RobotConfig.Motors;

public class IntakeConfig {

    public static int intakeUpperMotorCANID = Motors.intakeUpperMotorID;
    public static int intakeLowerMotorCANID = Motors.intakeLowerMotorID;

    public static int coneDetectSwitchID = LimitSwitches.intakeConeDetectSwitch;
    public static int cubeDetectSwitchID = LimitSwitches.intakeCubeDetectSwitch;

    public static boolean cubeDetectTrue = true;
    public static boolean coneDetectTrue = true;

    public static double cubeRetractSpeed = -0.25;
    public static double coneRetractSpeed = -0.25;

    public static double cubeEjectSpeed = -0.25;
    public static double coneEjectSpeed = -0.25;




}
