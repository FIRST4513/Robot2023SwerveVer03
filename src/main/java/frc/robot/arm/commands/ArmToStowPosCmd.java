package frc.robot.arm.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;
import frc.robot.elevator.ElevatorConfig;

/* 
 * Command to Run Arm MM for an amount of time
 * Step 1. Set MM target
 * Step 2. Let arm run mm on motor until time runs out
 * Step 3. End - set target angle to current angle (arm will continue to hold
 *               that position even after command ends)
 */

public class ArmToStowPosCmd extends CommandBase {
    boolean readyToLower = false;

    public ArmToStowPosCmd(double timeout) {
        addRequirements(Robot.arm);
        addRequirements(Robot.elevator);
        System.out.println("ArmDriveForSecondsCmd Called");
    }

    @Override
    public void initialize() {
        System.out.println("ArmDriveForSecondsCmd Init");
    }

    @Override
    public void execute() {
        if (!readyToLower) {
            Robot.arm.setMMTargetAngle(ArmConfig.ArmAngleStowPos);
            Robot.elevator.setMMheight(ElevatorConfig.ElevBumperClearHt);
            if (Robot.elevator.isMMtargetReached()) {
                readyToLower = true;
            }
        } else {
            if (Robot.arm.mCurrArmAngle < -22.5) {
                Robot.elevator.setMMheight(ElevatorConfig.ElevStoreHt);
            } else {
                Robot.elevator.setMMheight(ElevatorConfig.ElevBumperClearHt);
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        Robot.elevator.elevStop();
    }

    @Override
    public boolean isFinished() {
        if ((readyToLower) && Robot.elevator.mCurrElevHt < 2) {
            return true;
        }
        return false;
    }
}
