package frc.robot.logger.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.auto.commands.DelayCmd;

public class WriteOutTest2SCmdGrp extends SequentialCommandGroup{

    public WriteOutTest2SCmdGrp() {
        addCommands(
            new DelayCmd( 2.0 ));
            // new InstantCommand(() -> Robot.logger.init()),
            // new InstantCommand(() -> Robot.logger.writeData(2.5, 3.6)),
            // new InstantCommand(() -> Robot.logger.writeData(4.5, 5.6)),
            // new InstantCommand(() -> Robot.logger.close()));
    }
    
}
