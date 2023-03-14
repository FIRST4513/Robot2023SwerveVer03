package frc.robot.arm.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

/* 
 * Command to Run Arm MM for an amount of time
 * Step 1. Set MM target
 * Step 2. Let arm run mm on motor until time runs out
 * Step 3. End - set target angle to current angle (arm will continue to hold
 *               that position even after command ends)
 */

public class ArmDriveForSecondsCmd extends CommandBase {
    double targetAngle = 0;
    double timeout = 0;
    Timer runTimer = new Timer();

    // simple time out overload- let arm use current target
    public ArmDriveForSecondsCmd(double timeout) {
        addRequirements(Robot.arm);
        targetAngle = Robot.arm.mTargetArmAngle;
        this.timeout = timeout;
        System.out.println("ArmDriveForSecondsCmd Called");
    }

    // override/change current arm target to a parameter passed in
    // plus timeout
    public ArmDriveForSecondsCmd(double newTargetAngle, double timeout) {
        addRequirements(Robot.arm);
        targetAngle = newTargetAngle;
        this.timeout = timeout;
        System.out.println("ArmDriveForSecondsCmd Called");
    }

    @Override
    public void initialize() {
        Robot.arm.setMMTargetAngle(targetAngle);
        runTimer.reset();
        runTimer.start();
        System.out.println("ArmDriveForSecondsCmd Init");
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
        Robot.arm.setMMTargetAngle(Robot.arm.mCurrArmAngle);
    }

    @Override
    public boolean isFinished() {
        if (runTimer.get() > timeout) { return true; }
        return false;
    }
}
