package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class CenterDriveOnCmd extends CommandBase {
    private double balanceEffort; // The effort the robot should use to balance
    private double currentIncline;

    private double lastAngle1      = 0;
    private double lastAngle2      = 0;
    private double lastAngle3      = 0;
    private double lastAngle4      = 0;
    private double avgRunningAngle = 0;
    private double maxAvgAngle     = 0;

    private double APPROACH_SPEED  = -0.5;
    private double CLIMB_SPEED     = -0.5;

    private static enum DRIVE_STAGE { APPROACH, RAISE, CLIMB, CENTERED }
    private DRIVE_STAGE driveStage;

    public CenterDriveOnCmd() {
        addRequirements(Robot.swerve);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        System.out.println("CTR DRIVE - Initiated");
        Robot.swerve.setBrakeMode(true);
        driveStage = DRIVE_STAGE.APPROACH;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // update gryo angle variables
        updateAverages();

        // first, check for FLAT stage
        if (driveStage == DRIVE_STAGE.APPROACH) {
            // check for gyro angle greater than a certain amount of degrees
            if (avgRunningAngle > 3) {
                driveStage = DRIVE_STAGE.RAISE;
                System.out.println("CTR DRIVE - Was approaching, now raising");
                return;
            }
            // if flat, drive forward at slow-ish speed
            Robot.swerve.drive(APPROACH_SPEED, 0, 0, false, false);
            System.out.println("CTR DRIVE - Approaching, average angle: " + avgRunningAngle);
            return;
        }
        else if (driveStage == DRIVE_STAGE.RAISE) {
            // check if gyro angle is less than max degrees - 2
            if (avgRunningAngle < (maxAvgAngle - 3)) {
                driveStage = DRIVE_STAGE.CLIMB;
                System.out.println("CTR DRIVE - Was raising, now climbing");
                return;
            }
            // else, continue to drive slow-ish
            Robot.swerve.drive(APPROACH_SPEED, 0, 0, false, false);
            System.out.println("CTR DRIVE - Raising, average angle: " + avgRunningAngle);
            return;
        }
        else if (driveStage == DRIVE_STAGE.CLIMB) {
            // check if drive angle is "starting" to fall down
            if (avgRunningAngle < 10) {
                driveStage = DRIVE_STAGE.CENTERED;
                System.out.println("CTR DRIVE - Was climbing, now centered");
                return;
            }
            // else, drive a little faster
            Robot.swerve.drive(CLIMB_SPEED, 0, 0, false, false);
            System.out.println("CTR DRIVE - Climbing, average angle: " + avgRunningAngle);
            return;
        }
        else if (driveStage == DRIVE_STAGE.CENTERED) {
            // do nothing, is finished will call find it an exit the command
            System.out.println("CTR DRIVE - Centered, average angle: " + avgRunningAngle);
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
        if (driveStage == DRIVE_STAGE.CENTERED) {
            System.out.println("CTR DRIVE - Centered, exiting");
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
        if (driveStage != DRIVE_STAGE.APPROACH) {
            if (avgRunningAngle > maxAvgAngle) {
                maxAvgAngle = avgRunningAngle;
            }
        }
    }
}
