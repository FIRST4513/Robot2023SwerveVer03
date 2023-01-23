package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.swerveDrive.commands.SwerveDriveCmds;

//Need to work on setting an intial position for the field2D map to work properly.
public class DoubleBallSimplePCmdGrp extends ParallelCommandGroup {
    
  /** Creates a new TestPathFollowing. */
  public DoubleBallSimplePCmdGrp() {
    addCommands(
        AutoCmds.llShotwithTimeoutCmd(4),
        new WaitCommand(0.25).andThen(
            SwerveDriveCmds.DriveForTimeCmd(1, 0.2).deadlineWith(
                AutoCmds.intakeCmd()).andThen(
                    new WaitCommand(1).deadlineWith(
                        AutoCmds.intakeCmd()).andThen(
                            // new WaitCommand(1.75).deadlineWith(BallPathCommands.feed(),
                            new WaitCommand(1.75).deadlineWith(
                                SwerveDriveCmds.DriveForTimeCmd( 2.5, 1.5),
                                new PrintCommand("Double Ball Done")
                            )
                        ) 
                    )
            ).withTimeout(4)
        );
    }
}
