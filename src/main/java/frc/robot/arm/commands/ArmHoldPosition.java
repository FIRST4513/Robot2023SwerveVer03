package frc.robot.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ArmHoldPosition extends CommandBase {
    double position = 0;

    /** Creates a new FourBarHoldPosition. */
    public ArmHoldPosition() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.arm);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        position = Robot.arm.getEncoderCnt();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Robot.arm.setMMPosition(position);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
