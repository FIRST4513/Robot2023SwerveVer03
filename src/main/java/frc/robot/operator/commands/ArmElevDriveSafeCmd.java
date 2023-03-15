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

        System.out.println("ArmElevDriveSafeCmd - Constructor called");
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
        System.out.println("ArmElevDriveSafeCmd - initialized, cmdState = " + cmdState);
    }

    @Override
    public void execute() {
        // if raising elevator to safe pos
        if (cmdState == CmdState.ELEV_TO_SAFE) {
            Robot.arm.holdArmMM();
            Robot.elevator.setMMheight(elevSafe);
            // check if target reached
            if (Robot.elevator.isMMtargetReached()) { cmdState = CmdState.ARM_TO_ZERO; }
        }
        // if arm to zero
        else if (cmdState == CmdState.ARM_TO_ZERO) {
            Robot.arm.setMMTargetAngle(0);
            Robot.elevator.elevHoldMtr();
            // check if arm at target
            if (Robot.arm.isMMtargetReached()) { cmdState = CmdState.ELEV_TO_FINAL; }
        }
        // if elev to final
        else if (cmdState == CmdState.ELEV_TO_FINAL) {
            Robot.arm.holdArmMM();
            Robot.elevator.setMMheight(elevFinal);
            // if elevator at target
            if (Robot.elevator.isMMtargetReached()) { cmdState = CmdState.ARM_TO_TARGET; }
        } else if (cmdState == CmdState.ARM_TO_TARGET) {
            Robot.arm.setMMTargetAngle(armTgt);
            Robot.elevator.elevHoldMtr();
            // if arm at target
            if (Robot.arm.isMMtargetReached()) { cmdState = CmdState.DONE; }
        } else {
            Robot.arm.holdArmMM();
            Robot.elevator.elevHoldMtr();
        }
        System.out.println("ArmElevDriveSafeCmd - Execute, cmdState = " + cmdState);
    }

    @Override
    public void end(boolean interrupted) {
        Robot.arm.holdArmMM();
        Robot.elevator.elevHoldMtr();
        System.out.println("ArmElevDriveSafeCmd - End called");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (cmdState == CmdState.DONE) { System.out.println("cmd state done"); return true; }
        if (runTimer.get() > timeout) { System.out.println("cmd state done"); return true; }
        return false;
    }
}
