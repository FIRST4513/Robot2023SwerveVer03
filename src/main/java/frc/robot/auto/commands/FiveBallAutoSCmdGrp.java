package frc.robot.auto.commands;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.auto.AutoConfig;
import frc.robot.trajectories.commands.FollowTrajectoryCmd;
import frc.robot.trajectories.commands.TrajectoriesCmds;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html

public class FiveBallAutoSCmdGrp extends SequentialCommandGroup {
  /** Creates a new FourBallAuto. */
  
  PathPlannerTrajectory GetFirstBalls = PathPlanner.loadPath("Five_FirstBalls", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
  PathPlannerTrajectory Prep2ndBall =   PathPlanner.loadPath("Five_2ndBall", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
  PathPlannerTrajectory Get2ndBall =    PathPlanner.loadPath("Five_Intake2ndBall", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
  PathPlannerTrajectory GoToTerminal =  PathPlanner.loadPath("Five_GoToTerminal", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
  PathPlannerTrajectory HumanBall =     PathPlanner.loadPath("Five_HumanBall", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
  PathPlannerTrajectory GoTo2ndShots =  PathPlanner.loadPath("Five_2ndShots", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);

  public FiveBallAutoSCmdGrp() {
      // Add your commands in the addCommands() call, e.g.
      // addCommands(new FooCommand(), new BarCommand());
      addCommands(
        TrajectoriesCmds.IntializePathFollowingCmd(GetFirstBalls),
          AutoCmds.llShotwithTimeoutCmd(15).alongWith(
              AutoCmds.followPathAndIntakeCmd(GetFirstBalls, 2).andThen(
                  AutoCmds.intakeCmd(0.05),
                  AutoCmds.followPathAndIntakeCmd(Prep2ndBall, 2),
                  new WaitCommand(0.1),
                  AutoCmds.followPathAndIntakeCmd(Get2ndBall, 2),
                  AutoCmds.intakeCmd(0.05),
                  AutoCmds.feedCmd(1), //Feed first three balls
                  AutoCmds.followPathAndIntakeCmd(GoToTerminal, 5),
                  new WaitCommand(0.1),
                  AutoCmds.followPathAndIntakeCmd(HumanBall,2),
                  AutoCmds.intakeCmd(0.75),
                  new FollowTrajectoryCmd(GoTo2ndShots).withTimeout(4),
                  AutoCmds.autoLLAimCmd().withTimeout(0.4),
                  AutoCmds.feedCmd(6)
              )
          )
      );
  }
  
}


//AutoCmds.intake(2).alongWith(AutoCmds.autonLLAim().withTimeout(2)),
