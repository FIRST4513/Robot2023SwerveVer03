package frc.robot.logger.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.Robot;
import edu.wpi.first.wpilibj2.command.Command;

public class LoggerCmds {

    public static Command WriteOutLogFileCmd(){
        return new InstantCommand(() -> Robot.logger.saveLogFile());
    }
    
}
