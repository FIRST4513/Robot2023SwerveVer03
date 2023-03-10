package frc.robot.autoBalance.commands;


// Based on code from Spectrum Team

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.Rmath;
import frc.robot.Robot;
import frc.robot.autoBalance.AutoBalanceConfig;

public class AutoBalanceCommand extends CommandBase {
    private double balanceEffort; // The effort the robot should use to balance
    private double turningEffort; // The effort the robot should use to turn

    private double countTimer;
    private double currentIncline;

    private double lastAngle1 = 0;
    private double lastAngle2 = 0;
    private double lastAngle3 = 0;
    private double lastAngle4 = 0;
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
        // countTimer = 0;
        // currentIncline = Robot.swerve.gyro.getGyroInclineAngle();
        Robot.swerve.setBrakeMode(true);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        // countTimer += 1;
        // double countTimerMod = countTimer % 4;
        // if (countTimerMod == 0.0) {
        //     gyroIncline = Robot.swerve.gyro.getGyroInclineAngle();
        // }

        // Get incline and Average last 5 samples to account for gyro bouncing/variations
        currentIncline = Robot.swerve.gyro.getGyroInclineAngle();
        avgRunningAngle = (currentIncline + lastAngle1 + lastAngle2 + lastAngle3 + lastAngle4) / 5;
        lastAngle4 = lastAngle3;        
        lastAngle3 = lastAngle2;
        lastAngle2 = lastAngle1;
        lastAngle1 = avgRunningAngle;
        
        // Rotate based on heading error
        // turningEffort =
        //         Robot.trajectories
        //                 .calculateThetaSupplier(() -> AutoBalanceConfig.angleSetPoint)
        //                 .getAsDouble();

        // Drive Forward based on Angle
        double angleDifference = (AutoBalanceConfig.balancedAngle - avgRunningAngle);  // Robot.swerve.gyro.getGyroInclineAngle());
        angleDifference = Math.round(angleDifference);
        double angleDiffAsRad = Math.toRadians(angleDifference);

        // double sinValMultiplier = Math.sin(angleDiffAsRad);
        // balanceEffort = sinValMultiplier * AutoBalanceConfig.kPsin;   // Sin

        balanceEffort = angleDifference * AutoBalanceConfig.kP;          // Linear


        if ( Math.abs(Robot.swerve.gyro.getGyroInclineAngle()) < AutoBalanceConfig.balancedAngleTolerence ) {
            balanceEffort = 0.0;
        }

        //System.out.println("curr: " + countTimer + ", mod: " + countTimerMod + " b.e.: " + balanceEffort);
        Robot.swerve.drive(   balanceEffort, 0, 0 , false, false);
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
