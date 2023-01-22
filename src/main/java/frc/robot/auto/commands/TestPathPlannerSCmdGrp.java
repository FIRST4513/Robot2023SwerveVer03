//Created by Spectrum3847
package frc.robot.auto.commands;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.auto.AutoConfig;
import frc.robot.trajectories.commands.FollowTrajectoryCmd;

//Need to work on setting an intial position for the field2D map to work properly.
public class TestPathPlannerSCmdGrp extends SequentialCommandGroup {
  /** Creates a new TestPathFollowing. */
  public TestPathPlannerSCmdGrp() {

    // An example trajectory to follow. All units in meters.
    PathPlannerTrajectory DriveOneMeterFwd = PathPlanner.loadPath("Test_1meter", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
    PathPlannerTrajectory DriveTerminal = PathPlanner.loadPath("Test_DriveTerminal", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
    PathPlannerTrajectory Test_SlideRight = PathPlanner.loadPath("Test_SlideRight", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
    
    addCommands(
        new DelayCmd(0.25),
        AutoCmds.IntializePathFollowingCmd(DriveOneMeterFwd),
        new FollowTrajectoryCmd(DriveOneMeterFwd),
        new DelayCmd(0.5),
        new FollowTrajectoryCmd(DriveTerminal)

    );
  }
}
