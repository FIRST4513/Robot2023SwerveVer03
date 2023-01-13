package frc.robot.trajectories.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Robot;

public class TrajectoriesCmds {

    public static Command resetThetaControllerCmd() {
        return new InstantCommand(() -> Robot.trajectories.resetTheta(), Robot.trajectories);
    }
}
