package frc.robot.auto.commands;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.trajectories.commands.FollowTrajectoryCmd;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html

public class FourBallAutoSCmdGrp extends SequentialCommandGroup {
  /** Creates a new FourBallAuto. */
  
  PathPlannerTrajectory GetFirstBalls = PathPlanner.loadPath("Four_FirstBalls", 3, 3);
  PathPlannerTrajectory GoToTerminal = PathPlanner.loadPath("Four_GoToTerminal", 3, 3);
  PathPlannerTrajectory GoTo2ndShots = PathPlanner.loadPath("Four_2ndShots", 3, 3);

  public FourBallAutoSCmdGrp() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
        AutoCmds.IntializePathFollowingCmd(GetFirstBalls),
        AutoCmds.llShotwithTimeoutCmd(15).alongWith(
            AutoCmds.followPathAndIntakeCmd(GetFirstBalls, 2).andThen(
                AutoCmds.intakeCmd(0.5),
                AutoCmds.feedCmd(1), //Feed first two balls
                AutoCmds.followPathAndIntakeCmd(GoToTerminal, 4),
                AutoCmds.intakeCmd(2.5),
                new FollowTrajectoryCmd(GoTo2ndShots).withTimeout(4),
                AutoCmds.autoLLAimCmd().withTimeout(1),
                AutoCmds.feedCmd(1)
                )
            )
        );
  }

}
