package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.auto.AutoConfig;
import frc.robot.swerveDrive.commands.TurnToAngleCmd;

//Need to work on setting an intial position for the field2D map to work properly.
public class TripleBallSimpleSCmdGrp extends SequentialCommandGroup {
  /** Creates a new TestPathFollowing. */
  public TripleBallSimpleSCmdGrp() {

    addCommands(
      new DoubleBallSimplePCmdGrp(),
      thirdBallCmdGrp()
    );
  }

  private Command thirdBallCmdGrp(){
      return new WaitCommand(0.25).andThen(
          //BallPathCommands.llShotRPM().withTimeout(8).alongWith(
          new DelayCmd( 0.25 ).withTimeout(8).alongWith(
              new TurnToAngleCmd( 45.0 ).withTimeout(1.5).andThen(  //turn towards third ball
                  //Drive towards third ball and end when Intake a Ball
                  AutoCmds.DriveForTimeCmd(2.6, 0.25).deadlineWith(AutoCmds.intakeCmd()),
                  new TurnToAngleCmd(AutoConfig.thirdBallTurnToGoal).withTimeout(1).andThen(
                      AutoCmds.autoLLAimCmd().alongWith(        //Turn to goal
                          new WaitCommand(4).deadlineWith(AutoCmds.intakeCmd())) , 
                          //new WaitCommand(4).deadlineWith(BallPathCommands.feed())))).andThen(
                          new WaitCommand(4).deadlineWith(new DelayCmd(0.25))
                      )
                  )
              ).andThen(
          new PrintCommand("Done with 3 ball")
            )
      );
  }
}
