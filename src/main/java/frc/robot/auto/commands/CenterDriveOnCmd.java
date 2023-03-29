package frc.robot.auto.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class CenterDriveOnCmd extends CommandBase {
    private double balanceEffort; // The effort the robot should use to balance
    private double currentIncline;
    private Timer cmdTimer;
    private double accelerationTime = 1.0;
    private double settleTime = 1.0;  // 0.5;

    private double lastAngle1      = 0;
    private double lastAngle2      = 0;
    private double lastAngle3      = 0;
    private double lastAngle4      = 0;
    private double avgRunningAngle = 0;
    private double maxAvgAngle     = 0;

    private double timesAngleAbove12 = 0;

    private double APPROACH_SPEED  = -1.4;
    private double CLIMB_SPEED     = -1.4;

    private static enum DRIVE_STAGE { TIME_DRIVE, ACCELERATION, APPROACH, SETTLE, CLIMB, CENTERED }
    private DRIVE_STAGE driveStage;

    public double initRobotX;

    public CenterDriveOnCmd() {
        addRequirements(Robot.swerve);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        System.out.println("CTR DRIVE - Initiated");
        Robot.swerve.setBrakeMode(true);
        // driveStage = DRIVE_STAGE.TIME_DRIVE;
        driveStage = DRIVE_STAGE.APPROACH;
        cmdTimer = new Timer();
        cmdTimer.reset();
        cmdTimer.start();
        initRobotX = Robot.swerve.odometry.getPoseMeters().getX();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // update gryo angle variables
        // updateAverages();
        System.out.println(Robot.swerve.gyro.getGyroInclineAngle());

        if (driveStage == DRIVE_STAGE.TIME_DRIVE) {
            updateAverages();

            if (cmdTimer.get() > 2.0) {
                driveStage = DRIVE_STAGE.CENTERED;
                System.out.println("CTR DRIVE - was time driving, now centered");
                return;
            }
            Robot.swerve.drive(APPROACH_SPEED, 0, 0, false, false);
            System.out.println("CTR DRIVE - time driving, avg angle: " + avgRunningAngle + ", ta12: " + timesAngleAbove12);
            return;
        }

        if (driveStage == DRIVE_STAGE.ACCELERATION) {
            if (cmdTimer.get() > accelerationTime) {
                driveStage = DRIVE_STAGE.APPROACH;
                System.out.println("CTR DRIVE - was accelerating, now approaching");
                return;
            }
            System.out.println("CTR DRIVE - accelerating, timer: " + cmdTimer.get());
            return;
        }

        if (driveStage == DRIVE_STAGE.APPROACH) {
            updateAverages();
            
            // check if angle is consistently above 12
            if (timesAngleAbove12 > 10) {
                driveStage = DRIVE_STAGE.SETTLE;
                cmdTimer.reset();
                cmdTimer.start();
                System.out.println("CTR DRIVE - was approaching, now settling");
                return;
            }
            // approaching
            Robot.swerve.drive(APPROACH_SPEED, 0, 0, false, false);
            System.out.println("CTR DRIVE - approaching, avg angle: " + avgRunningAngle + ", ta12: " + timesAngleAbove12);
            return;
        }

        if (driveStage == DRIVE_STAGE.SETTLE) {
            updateAverages();

            if (cmdTimer.get() > settleTime) {
                driveStage = DRIVE_STAGE.CLIMB;
                System.out.println("CTR DRIVE - was settling, now climbing");
                return;
            }
            System.out.println("CTR DRIVE - settling, timer: " + cmdTimer.get());
            Robot.swerve.drive(0.1, 0, 0, false, false);
            return;
        }

        if (driveStage == DRIVE_STAGE.CLIMB) {
            updateAverages();

            // check if angle is probably less than 10
            if (avgRunningAngle < 10) {
                driveStage = DRIVE_STAGE.CENTERED;
                System.out.println("CTR DRIVE - was climbing, now centered");
                return;
            }
            // climbing
            Robot.swerve.drive(CLIMB_SPEED, 0, 0, false, false);
            System.out.println("CTR DRIVE - climbing, avg angle: " + avgRunningAngle);
            return;
        }
        
        if (driveStage == DRIVE_STAGE.CENTERED) {
            // nothing
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
        if (avgRunningAngle < -10) {
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

        if (avgRunningAngle > 12) {
            timesAngleAbove12++;
        } else {
            timesAngleAbove12 = 0;
        }
    }
}
