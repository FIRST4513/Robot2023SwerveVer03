package frc.robot.autoBalance.commands;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

// Based on code from Spectrum Team

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.autoBalance.AutoBalanceConfig;

public class AutoBalanceCommand extends CommandBase {
    private double balanceEffort; // The effort the robot should use to balance
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
        double angleDifference = (AutoBalanceConfig.balancedAngle - Robot.swerve.gyro.getGyroInclineAngle());
        double angleDiffAsRad = Math.toRadians(angleDifference);
        double sinValMultiplier = Math.sin(angleDiffAsRad);

        // balanceEffort = sinValMultiplier * AutoBalanceConfig.kPsin;         // Sin
        balanceEffort = angleDifference * AutoBalanceConfig.kP;          // Linear

        if ( Math.abs(Robot.swerve.gyro.getGyroInclineAngle()) < AutoBalanceConfig.balancedAngleTolerence ) {
            balanceEffort = 0.0;
        }

        Robot.swerve.drive(    balanceEffort, 0, 0 , false, false);
        // System.out.println("Balance Effort: " + balanceEffort);
        //Robot.swerve.drive(  balanceEffort, 0, turningEffort, false, true);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.swerve.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // if ( Math.abs(Robot.swerve.gyro.getGyroInclineAngle()) < AutoBalanceConfig.balancedAngleTolerence ) {
        //     return true;
        // };
        return false;
    }
}
