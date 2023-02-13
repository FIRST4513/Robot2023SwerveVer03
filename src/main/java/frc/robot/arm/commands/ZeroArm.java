package frc.robot.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;

public class ZeroArm extends CommandBase {
    /** Creates a new ZerofourBar. */
    public ZeroArm() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.arm);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // Turn off soft limits
        Robot.arm.softLimitsFalse();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Set fourBar to slowly lower
        Robot.arm.setArmMotor(ArmConfig.zeroSpeed);
        Robot.arm.resetEncoder();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        // Set fourBar position to zero
        // enable soft limits
        
        Robot.arm.resetEncoder(-500);
        Robot.arm.softLimitsTrue();
        Robot.arm.setMMPosition(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
