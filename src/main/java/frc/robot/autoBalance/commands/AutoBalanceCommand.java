package frc.robot.autoBalance.commands;

import java.time.OffsetDateTime;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import edu.wpi.first.wpilibj.Timer;

// Based on code from Spectrum Team

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.Rmath;
import frc.robot.Robot;
import frc.robot.autoBalance.AutoBalanceConfig;

public class AutoBalanceCommand extends CommandBase {
    private double balanceEffort; // The effort the robot should use to balance
    private double turningEffort; // The effort the robot should use to turn

    private double countTimer;
    private double gyroIncline;

    private double angle1 = 0;
    private double angle2 = 0;
    private double angle3 = 0;
    private double avgRunningAngle = 0;

    /*Creates a new GeneratePath.
     * * @param firstPoints*/
    public AutoBalanceCommand() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.swerve);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        countTimer = 0;
        gyroIncline = Robot.swerve.gyro.getGyroInclineAngle();
        Robot.swerve.setBrakeMode(true);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        countTimer += 1;
        double countTimerMod = countTimer % 4;

        // if (countTimerMod == 0.0) {
        //     gyroIncline = Robot.swerve.gyro.getGyroInclineAngle();
        // }
        gyroIncline = Robot.swerve.gyro.getGyroInclineAngle();

        avgRunningAngle = (gyroIncline + angle1 + angle2 + angle3) / 4;
        angle3 = angle2;
        angle2 = angle1;
        angle1 = avgRunningAngle;

        // Average samples to account for gyro bouncing/variations
        
        // Rotate based on heading error
        // turningEffort =
        //         Robot.trajectories
        //                 .calculateThetaSupplier(() -> AutoBalanceConfig.angleSetPoint)
        //                 .getAsDouble();

        // Drive Forward based on Angle
        double angleDifference = (AutoBalanceConfig.balancedAngle - avgRunningAngle);  // Robot.swerve.gyro.getGyroInclineAngle());
        angleDifference = Math.round(angleDifference);
        double angleDiffAsRad = Math.toRadians(angleDifference);
        double sinValMultiplier = Math.sin(angleDiffAsRad);

        // balanceEffort = sinValMultiplier * AutoBalanceConfig.kPsin;         // Sin
        balanceEffort = angleDifference * AutoBalanceConfig.kP;          // Linear

        if ( Math.abs(Robot.swerve.gyro.getGyroInclineAngle()) < AutoBalanceConfig.balancedAngleTolerence ) {
            balanceEffort = 0.0;
        }

        System.out.println("curr: " + countTimer + ", mod: " + countTimerMod + " b.e.: " + balanceEffort);
        Robot.swerve.drive(    balanceEffort, 0, 0 , false, false);
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
