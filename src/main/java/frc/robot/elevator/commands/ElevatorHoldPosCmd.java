package frc.robot.elevator.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ElevatorHoldPosCmd extends CommandBase {
    private double height = 0;

    // Command Constructor
    public ElevatorHoldPosCmd() {
        addRequirements(Robot.elevator);
    }

    @Override
    public void initialize() {
        height = Robot.elevator.getElevHeightInches();
    }

    @Override
    public void execute() {
        if (Robot.elevator.isLowerLimitSwitchPressed()) {
            Robot.elevator.elevStop();
        } else {
            Robot.elevator.setMMheight(height);
        }
    }

    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
