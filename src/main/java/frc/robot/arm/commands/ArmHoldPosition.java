package frc.robot.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ArmHoldPosition extends CommandBase {
    double angle = 0;

    public ArmHoldPosition() {
        addRequirements(Robot.arm);
    }

    @Override
    public void initialize() {
        angle = Robot.arm.getArmAngle();
    }

    @Override
    public void execute() {
        Robot.arm.setMMangle(angle);
    }

    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
