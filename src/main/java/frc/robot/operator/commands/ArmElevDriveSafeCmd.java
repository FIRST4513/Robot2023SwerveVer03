package frc.robot.operator.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.Rmath;
import frc.robot.Robot;

public class ArmElevDriveSafeCmd extends CommandBase {
    double armTgt, elevSafe, elevFinal, timeout;
    Timer runTimer = new Timer();

    static enum CmdState { ELEV_TO_SAFE, ARM_TO_ZERO, ELEV_TO_FINAL, ARM_TO_TARGET, DONE};
    CmdState cmdState;

    public ArmElevDriveSafeCmd(double armTgtHt, double elevSafeHt, double elevFinalHt, double timeout) {
        armTgt = armTgtHt;
        elevSafe = elevSafeHt;
        elevFinal = elevFinalHt;
        this.timeout = timeout;

        addRequirements(Robot.arm);
        addRequirements(Robot.elevator);
    }

    @Override
    public void initialize() {
        runTimer.reset();
        runTimer.start();

        // decide whether elevator needs to raise or not to safe pos,
        // and set state as needed.
        // if arm is inside and target is outside
        if (Robot.arm.isArmInside() && (armTgt > 0)) {
            cmdState = CmdState.ELEV_TO_SAFE;
        }
        // if arm is outside and target is inside
        else if (Robot.arm.isArmOutside() && (armTgt < 0)) {
            cmdState = CmdState.ELEV_TO_SAFE;
        }
        // otherwise raise to elev final pos
        else {
            cmdState = CmdState.ELEV_TO_FINAL;
        }
    }

    @Override
    public void execute() {
        // if raising elevator to safe pos
        if (cmdState == CmdState.ELEV_TO_SAFE) {
            // check if target reached
            if (Robot.elevator.isMMtargetReached()) { cmdState = CmdState.ARM_TO_ZERO; }
            else {
                Robot.arm.holdArmMM();
                Robot.elevator.setMMheight(elevSafe);
            }
        }
        // if arm to zero
        else if (cmdState == CmdState.ARM_TO_ZERO) {
            // check if arm at target
            if (Robot.arm.isMMtargetReached()) { cmdState = CmdState.ELEV_TO_FINAL; }
            else {
                Robot.arm.setMMTargetAngle(0);
                Robot.elevator.elevHoldMtr();
            }
        }
        // if elev to final
        else if (cmdState == CmdState.ELEV_TO_FINAL) {
            // if elevator at target
            if (Robot.elevator.isMMtargetReached()) { cmdState = CmdState.ARM_TO_TARGET; }
            else {
                Robot.arm.holdArmMM();
                Robot.elevator.setMMheight(elevFinal);
            }
        } else if (cmdState == CmdState.ARM_TO_TARGET) {
            // if arm at target
            if (Robot.arm.isMMtargetReached()) { cmdState = CmdState.DONE; }
            else {
                Robot.arm.setMMTargetAngle(armTgt);
                Robot.elevator.elevHoldMtr();
            }
        } else {
            Robot.arm.holdArmMM();
            Robot.elevator.elevHoldMtr();
        }
    }

    @Override
    public void end(boolean interrupted) {
        Robot.arm.holdArmMM();
        Robot.elevator.elevHoldMtr();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (cmdState == CmdState.DONE) { return true; }
        if (runTimer.get() > timeout) { return true; }
        return false;
    }
}
