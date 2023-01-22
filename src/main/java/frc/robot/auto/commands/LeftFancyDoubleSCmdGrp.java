package frc.robot.auto.commands;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html

public class LeftFancyDoubleSCmdGrp extends SequentialCommandGroup {
  /** Creates a new FourBallAuto. */

  
  PathPlannerTrajectory GetFirstBalls = PathPlanner.loadPath("LFD_FirstBall", 3, 3);
  PathPlannerTrajectory PrepOpponentBall = PathPlanner.loadPath("LFD_PrepOpponentBall", 3, 3);
  PathPlannerTrajectory GetOpponentBall = PathPlanner.loadPath("LFD_GetOpponentBall", 3, 3);

  public LeftFancyDoubleSCmdGrp() {
      // Add your commands in the addCommands() call, e.g.
      // addCommands(new FooCommand(), new BarCommand());
      addCommands(
          AutoCmds.IntializePathFollowingCmd(GetFirstBalls),
          AutoCmds.llShotwithTimeoutCmd(15).alongWith(
              AutoCmds.followPathAndIntakeCmd(GetFirstBalls, 2).andThen(
                  AutoCmds.intakeCmd(0.5),
                  AutoCmds.feedCmd(1.5), //Feed two balls
                  AutoCmds.followPathAndIntakeCmd(PrepOpponentBall, 2),
                  AutoCmds.followPathAndIntakeCmd(GetOpponentBall, 2),
                  AutoCmds.intakeCmd(2),
                  AutoCmds.feedCmd(1)
              )
          )
      );
  }
  
}
