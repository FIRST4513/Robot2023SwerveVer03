package frc.robot.autoBalance.commands;

// Based on code from Spectrum Team

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.autoBalance.AutoBalanceConfig;

public class AutoBalanceCommand extends CommandBase {
    private double balanaceEffort; // The effort the robot should use to balance
    private double turningEffort; // The effort the robot should use to turn



    /*Creates a new GeneratePath.
     * * @param firstPoints*/
    public AutoBalanceCommand() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.swerve);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        // Rotate based on heading error
        turningEffort =
                Robot.trajectories
                        .calculateThetaSupplier(() -> AutoBalanceConfig.angleSetPoint)
                        .getAsDouble();

        // Drive Forward based on Angle
        balanaceEffort =
                (AutoBalanceConfig.balancedAngle - Robot.swerve.gyro.getGyroInclineAngle())
                        * AutoBalanceConfig.kP;
        //Robot.swerve.drive(balanaceEffort, 0, turningEffort, false, true);
        Robot.swerve.drive(balanaceEffort, 0, 0 , false, true);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.swerve.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if ( Math.abs(Robot.swerve.gyro.getGyroInclineAngle()) < AutoBalanceConfig.balancedAngleTolerence ) {
            return true;
        };
        return false;
    }
}
