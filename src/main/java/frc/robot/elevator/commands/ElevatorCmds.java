package frc.robot.elevator.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

/** Add your docs here. */
public class ElevatorCmds {

    public static void setupDefaultCommand() {
        Robot.elevator.setDefaultCommand(stop());
    }

    public static Command goToHeight(int height) {
        return new RunCommand(() -> Robot.elevator.goToTarget(height), Robot.elevator);
    }

    public static Command stop() {
        return new RunCommand(() -> Robot.elevator.stop(), Robot.elevator);
    }
}
