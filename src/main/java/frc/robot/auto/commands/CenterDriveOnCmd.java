package frc.robot.auto.commands;

import com.ctre.phoenixpro.configs.SoftwareLimitSwitchConfigs;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.Rmath;
import frc.robot.Robot;
import frc.robot.autoBalance.AutoBalanceConfig;

public class CenterDriveOnCmd extends CommandBase {
    private double balanceEffort; // The effort the robot should use to balance
    private double currentIncline;

    private double lastAngle1 = 0;
    private double lastAngle2 = 0;
    private double lastAngle3 = 0;
    private double lastAngle4 = 0;
    private double avgRunningAngle = 0;
    private double maxAvgAngle = 0;

    private static enum DRIVE_STAGE { FLAT, RAISE, LOWER, CENTER }
    private DRIVE_STAGE driveStage;

    public CenterDriveOnCmd() {
        addRequirements(Robot.swerve);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Robot.swerve.setBrakeMode(true);
        driveStage = DRIVE_STAGE.FLAT;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // update gryo angle variables
        updateAverages();
        System.out.println("CTR DRIVE - drive state: " + driveStage + " ; gyro angle: " + avgRunningAngle);

        // first, check for FLAT stage
        if (driveStage == DRIVE_STAGE.FLAT) {
            // check for gyro angle greater than a certain amount of degrees
            if (avgRunningAngle > 3) {
                driveStage = DRIVE_STAGE.RAISE;
                return;
            }
            // if flat, drive forward at slow-ish speed
            Robot.swerve.drive(-2.0, 0, 0, false, false);
            return;
        }
        if (driveStage == DRIVE_STAGE.RAISE) {
            // check if gyro angle is less than max degrees - 2
            if (avgRunningAngle < (maxAvgAngle - 3)) {
                driveStage = DRIVE_STAGE.LOWER;
                return;
            }
            // else, continue to drive slow-ish
            Robot.swerve.drive(-2.0, 0, 0, false, false);
            return;
        }
        if (driveStage == DRIVE_STAGE.LOWER) {
            // check if drive angle is "starting" to fall down
            if (avgRunningAngle < 10) {
                driveStage = DRIVE_STAGE.CENTER;
                return;
            }
            // else, drive a little faster
            Robot.swerve.drive(-2.0, 0, 0, false, false);
        }
        if (driveStage == DRIVE_STAGE.CENTER) {
            // do nothing, is finished will call find it an exit the command
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.swerve.stop();
        System.out.println("hey we finished lol");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (driveStage == DRIVE_STAGE.CENTER) {
            return true;
        }
        return false;
    }

    public void updateAverages() {
        // Get incline and Average last 5 samples to account for gyro bouncing/variations
        currentIncline = Robot.swerve.gyro.getGyroInclineAngle();
        avgRunningAngle = (currentIncline + lastAngle1 + lastAngle2 + lastAngle3 + lastAngle4) / 5;
        lastAngle4 = lastAngle3;        
        lastAngle3 = lastAngle2;
        lastAngle2 = lastAngle1;
        lastAngle1 = avgRunningAngle;

        // update max angle
        if (driveStage != DRIVE_STAGE.LOWER) {
            if (avgRunningAngle > maxAvgAngle) {
                maxAvgAngle = avgRunningAngle;
            }
        }
    }
}
