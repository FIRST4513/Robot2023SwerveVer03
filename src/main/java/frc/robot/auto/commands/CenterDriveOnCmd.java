package frc.robot.auto.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class CenterDriveOnCmd extends CommandBase {
    private double currentIncline;
    private Timer cmdTimer;
    private double ACCEL_TIME = 1.5;
    private double CLIMB_TIME = 0.75;

    private double lastAngle1;
    private double lastAngle2;
    private double lastAngle3;
    private double lastAngle4;
    private double lastAngle5;
    private double lastAngle6;
    private double lastAngle7;
    private double lastAngle8;
    private double lastAngle9;
    private double lastAngle10;
    private double avgRunningAngle;

    private double timesAngleAbove;
    private double timesAngleBelow;

    private double ABOVE_ANGLE = -12;
    private double BELOW_ANGLE = -10;

    private double ABOVE_TOLERANCE = 5;
    private double BELOW_TOLERANCE = 5;

    private double APPROACH_SPEED  = 1.0;
    private double CLIMB_SPEED     = 0.5;

    private static enum DRIVE_STAGE { ACCELERATION, CLIMB, LOCK_WHEELS, CENTERED }
    private DRIVE_STAGE driveStage;

    public CenterDriveOnCmd() {
        addRequirements(Robot.swerve);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        zeroAverages();

        System.out.println("CTR DRIVE - Initialize called");
        Robot.swerve.setBrakeMode(true);

        driveStage = DRIVE_STAGE.ACCELERATION;
        cmdTimer = new Timer();
        cmdTimer.reset();
        cmdTimer.start();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // update gryo angle variables
        updateAverages();
        System.out.println("avg angle: " + avgRunningAngle);

        // accelerating
        if (driveStage == DRIVE_STAGE.ACCELERATION) {
            if (cmdTimer.get() > ACCEL_TIME) {
                driveStage = DRIVE_STAGE.CLIMB;
                setAverages(ABOVE_ANGLE);
                System.out.println("CTR DRIVE - was accelerating, now CLIMBING");
                return;
            }
            // accelerating, go forward
            Robot.swerve.drive(APPROACH_SPEED, 0, 0, false, false);
            System.out.println("CTR DRIVE - ACCELERATING, avg angle: " + avgRunningAngle  + ", timer: " + cmdTimer.get());
            return;
        }

        // climbing
        if (driveStage == DRIVE_STAGE.CLIMB) {
            // check if angle is probably less than 10
            // if (hasBeenBelow()) {
            //     driveStage = DRIVE_STAGE.LOCK_WHEELS;
            //     System.out.println("CTR DRIVE - was climbing, now CENTERED");
            //     return;
            // }
            if (cmdTimer.get() > (ACCEL_TIME + CLIMB_TIME)) {
                driveStage = DRIVE_STAGE.LOCK_WHEELS;
                System.out.println("CTR DRIVE - was climbing, now CENTERED");
                return;
            }
            // climbing
            Robot.swerve.drive(CLIMB_SPEED, 0, 0, false, false);
            System.out.println("CTR DRIVE - CLIMBING, avg angle: " + avgRunningAngle + " timer: " + cmdTimer.get());
            return;
        }

        if (driveStage == DRIVE_STAGE.LOCK_WHEELS) {
            Robot.swerve.drive(0, 0, 1, false, false);
            System.out.println("CTR DRIVE - LOCKING WHEELS");
            driveStage = DRIVE_STAGE.CENTERED;
            System.out.println("CTR DRIVE - was locking wheels, now CENTERED");
        }
        
        // centered, stopped
        if (driveStage == DRIVE_STAGE.CENTERED) {
            // nothing
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.swerve.stop();
        lockWheels();
        System.out.println("CTR DRIVE - EXITED");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (driveStage == DRIVE_STAGE.CENTERED) {
            System.out.println("CTR DRIVE - CENTERED, EXITING");
            return true;
        }
        return false;
    }

    private void updateAverages() {
        // Get incline and Average last 5 samples to account for gyro bouncing/variations
        currentIncline = Robot.swerve.gyro.getGyroInclineAngle();

        // cap incline angle to compensate for strays
        if (currentIncline > 20.0) { currentIncline = 20.0; }
        if (currentIncline < -20.0) { currentIncline = -20.0; }

        // calculate average angles
        avgRunningAngle = (currentIncline + lastAngle1 + lastAngle2 + lastAngle3 + lastAngle4 +
                lastAngle5 + lastAngle6 + lastAngle7 + lastAngle8 + lastAngle9 + lastAngle10) / 11;
        lastAngle10 = lastAngle9;       
        lastAngle9 = lastAngle8;
        lastAngle8 = lastAngle7;
        lastAngle7 = lastAngle6;
        lastAngle6 = lastAngle5;
        lastAngle5 = lastAngle4;
        lastAngle4 = lastAngle3;
        lastAngle3 = lastAngle2;
        lastAngle2 = lastAngle1;
        lastAngle1 = avgRunningAngle;

        // increment above or below counters if applicable
        if (avgRunningAngle < ABOVE_ANGLE) { timesAngleAbove++; } else { timesAngleAbove = 0; }
        if (avgRunningAngle > BELOW_ANGLE) { timesAngleBelow++; } else { timesAngleBelow = 0; }
    }

    private void zeroAverages() {
        lastAngle1       = 0;
        lastAngle2       = 0;
        lastAngle3       = 0;
        lastAngle4       = 0;
        lastAngle5       = 0;
        lastAngle6       = 0;
        lastAngle7       = 0;
        lastAngle8       = 0;
        lastAngle9       = 0;
        lastAngle10      = 0;
        avgRunningAngle  = 0;
        timesAngleAbove  = 0;
        timesAngleBelow  = 0;
    }

    public void setAverages(double newAngle) {
        lastAngle1        = newAngle;
        lastAngle2        = newAngle;
        lastAngle3        = newAngle;
        lastAngle4        = newAngle;
        lastAngle5        = newAngle;
        lastAngle6        = newAngle;
        lastAngle7        = newAngle;
        lastAngle8        = newAngle;
        lastAngle9        = newAngle;
        lastAngle10        = newAngle;
        avgRunningAngle   = newAngle;
        timesAngleAbove   = 0;
        timesAngleBelow   = 0;
    }

    private boolean hasBeenBelow() {
        if (timesAngleBelow > BELOW_TOLERANCE) { return true; }
        return false;
    }

    private boolean hasBeenAbove() {;;;;;;;
        if (timesAngleAbove > ABOVE_TOLERANCE) { return true; }
        return false;
    }

    private void lockWheels() {
        SwerveModuleState[] stopModuleStates =
            new SwerveModuleState[] {
                new SwerveModuleState(0, Rotation2d.fromDegrees(225)),
                new SwerveModuleState(0, Rotation2d.fromDegrees(135)),
                new SwerveModuleState(0, Rotation2d.fromDegrees(315)),
                new SwerveModuleState(0, Rotation2d.fromDegrees(45))
            };
        
            Robot.swerve.setModuleStates(stopModuleStates);
    }
}
