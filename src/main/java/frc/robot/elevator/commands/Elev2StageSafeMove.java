package frc.robot.elevator.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;

public class Elev2StageSafeMove extends CommandBase{
    public static enum CmdState { START, SAFE, FINAL, DONE };
    public static CmdState cmdState;
    private double safePos, finalPos, armAngle;

    // Command Constructor
    public Elev2StageSafeMove(double elevSafePos, double elevFinalPos, double armAngle) {
        addRequirements(Robot.elevator);
        safePos = elevSafePos;
        finalPos = elevFinalPos;
        this.armAngle = armAngle;
    }

    @Override
    public void initialize() {
        cmdState = CmdState.START;
        Robot.elevator.setMMheight(safePos);  // if stuff doesn't work, possible issue?
    }

    @Override
    public void execute() {
        if (cmdState == CmdState.START) {
            // elev not at safe pos
            if (!Robot.elevator.isMMtargetReached()) {
                // move elev to safe pos
                Robot.elevator.setMMheight(safePos);
                return;
            } else {
                // elev at safe pos, change to safe state
                cmdState = CmdState.SAFE;
            }
        } 
        if (cmdState == CmdState.SAFE) {
            // if arm not at final pos
            if (Math.abs(armAngle-Robot.arm.mCurrArmAngle) <= ArmConfig.KAngleDeadBand) {
                // hold elev
                Robot.elevator.elevHoldMtr();
                return;
            } else {
                // arm at final pos, change to final state
                cmdState = CmdState.FINAL;
                Robot.elevator.setMMheight(finalPos);
            }
        }
        if (cmdState == CmdState.FINAL) {
            // is the elevator at its target height (and command finished)?
            if (Robot.elevator.isMMtargetReached()) {
                // set state to done, to be quizzed from outside
                cmdState = CmdState.DONE;
            } else {
                // motion magic to final
                Robot.elevator.setMMheight(finalPos);
                return;
            }
        }
        if (cmdState == CmdState.DONE) {
            // motion magic to final, continues running
            Robot.elevator.setMMheight(finalPos);
        }
    }

    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
