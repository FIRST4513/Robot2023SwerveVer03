package frc.robot.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ArmHoldPositionCmd extends CommandBase {
    double angle = 0;

    public ArmHoldPositionCmd() {
        addRequirements(Robot.arm);
    }

    @Override
    public void initialize() {
        // Robot.arm.resetEncoderToAbsolute();     // Initialize Arm motor encoder to absolute
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
