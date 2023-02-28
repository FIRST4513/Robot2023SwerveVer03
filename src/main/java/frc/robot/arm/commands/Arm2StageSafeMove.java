package frc.robot.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;

public class Arm2StageSafeMove extends CommandBase{
    public static enum CmdState { START, SAFE, FINAL, DONE };
    public static CmdState cmdState;
    private double safeAngle, finalAngle, elevHeight;

    // Command Constructor
    public Arm2StageSafeMove(double armSafeAngle, double armFinalAngle, double elevHeight) {
        addRequirements(Robot.elevator);
        safeAngle = armSafeAngle;
        finalAngle = armFinalAngle;
        this.elevHeight = elevHeight;
    }

    @Override
    public void initialize() {
        cmdState = CmdState.START;
    }

    @Override
    public void execute() {
        if (cmdState == CmdState.START) {
            // elev not at safe pos need to wait
            if (!Robot.elevator.isMMtargetReached()) {
                // move elev to safe pos
                //Robot.elevator.setMMheight(safePos);
                return;
            } else {
                // elev at safe pos, change to safe state
                cmdState = CmdState.SAFE;
            }
        }
        // if (cmdState == CmdState.SAFE) {
        //     // if arm not at final pos
        //     if (Math.abs(armAngle-Robot.arm.mCurrArmAngle) <= ArmConfig.KAngleDeadBand) {
        //         // hold elev
        //         Robot.elevator.elevHoldMtr();
        //         return;
        //     } else {
        //         // arm at final pos, change to final state
        //         cmdState = CmdState.FINAL;
        //         Robot.elevator.setMMheight(finalPos);
        //     }
        // }
        // if (cmdState == CmdState.FINAL) {
        //     // is the elevator at its target height (and command finished)?
        //     if (Robot.elevator.isMMtargetReached()) {
        //         // set state to done, to be quizzed from outside
        //         cmdState = CmdState.DONE;
        //     } else {
        //         // motion magic to final
        //         Robot.elevator.setMMheight(finalPos);
        //         return;
        //     }
        // }
        // if (cmdState == CmdState.DONE) {
        //     // motion magic to final, continues running
        //     Robot.elevator.setMMheight(finalPos);
        // }
    }

    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
