package frc.robot.arm.commands;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;

public class ArmCmds {
    // Default Command
    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
        // Robot.arm.setDefaultCommand(new ArmHoldPositionCmd());
        Robot.arm.setDefaultCommand(ArmByJoystickCmd());
    }

    // ---------------- Arm Motion Stop Commands --------------
    public static Command HoldArmCmd() {
        return new RunCommand(() -> Robot.arm.holdArmMM(), Robot.arm )
            .withName("HoldArmCmd");
    }

    public static Command StopArmCmd() {
        return new InstantCommand( () -> Robot.arm.stopArm(), Robot.arm)
            .withName("StopArmCmd");
    }

    // ------------- Arm Motion Commands -------------
    public static Command ArmByJoystickCmd() {
        return new RunCommand(
        () -> Robot.arm.adjustMMTarget(() -> Robot.operatorGamepad.getArmInput()), Robot.arm);
    }

    public static Command ArmSetMMTargetCmd(double angle) {
        return new RunCommand(() -> Robot.arm.setMMTargetAngle(angle), Robot.arm);
    }

    // public static Command RaiseArmCmd() {
    //     return new RunCommand(() -> Robot.arm.raiseArm(), Robot.arm )
    //         .withName("RaiseArmCmd");
    // }

    // public static Command LowerArmCmd() {
    //     return new RunCommand(() -> Robot.arm.lowerArm(), Robot.arm )
    //         .withName("LowerArmCmd");
    // }
   
    // public static Command setManualOutput(double speed) {
    //     return setManualOutput(speed);
    // }

    // public static Command setManualOutput(DoubleSupplier speed) {
    //     return new RunCommand(
    //             () -> Robot.arm.setArmMotor(speed.getAsDouble()), Robot.arm);
    // }

    // ---------- Set Arm Brake Commands -----------
    public static Command SetArmBrakeCmd() {
        return new InstantCommand( () -> Robot.arm.setBrakeMode(true), Robot.arm);
    }

    public static Command SetArmCoastCmd() {
        return new InstantCommand( () -> Robot.arm.setBrakeMode(false), Robot.arm);
    }

    // ------------- Reset Arm Encoder Command -------------
    public static Command ResetArmEncoderCmd() {
        // return new InstantCommand( () -> Robot.arm.resetEncoderToAbsolute(), Robot.arm);
        return new InstantCommand( () -> Robot.arm.resetEncoder(), Robot.arm);
    }

    // --------------- Arm Set MM Target To Predef Angles (instant) ---------------
    public static Command ArmSetTargetStowPosCmd() {
        return new InstantCommand( () -> Robot.arm.setMMTargetAngle(ArmConfig.ArmAngleStowPos));
    }

    public static Command ArmSetTargetIntakePosCmd() {
        return new InstantCommand( () -> Robot.arm.setMMTargetAngle(ArmConfig.ArmAngleIntakePos));
    }

    public static Command ArmSetTargetLowPosCmd() {
        return new InstantCommand( () -> Robot.arm.setMMTargetAngle(ArmConfig.ArmAngleEjectLowPos));
    }

    public static Command ArmSetTargetMidPosCmd() {
        return new InstantCommand( () -> Robot.arm.setMMTargetAngle(ArmConfig.ArmAngleEjectMidPos));
    }

    public static Command ArmSetTargetHighPosCmd() {
        return new InstantCommand( () -> Robot.arm.setMMTargetAngle(ArmConfig.ArmAngleEjectHighPos));
    }

    // --------------- Arm Run To Predef Angles Until Timeout (runcmd) ---------------
    public static Command ArmRunToStowPosCmd() {
        return new ArmDriveForSecondsCmd(ArmConfig.ArmAngleStowPos, 5.0);
    }
    public static Command ArmRunToIntakePosCmd() {
        return new ArmDriveForSecondsCmd(ArmConfig.ArmAngleIntakePos, 5.0);
    }
    public static Command ArmRunToLowPosCmd() {
        return new ArmDriveForSecondsCmd(ArmConfig.ArmAngleEjectLowPos, 5.0);
    }
    public static Command ArmRunToMedPosCmd() {
        return new ArmDriveForSecondsCmd(ArmConfig.ArmAngleEjectMidPos, 5.0);
    }
    public static Command ArmRunToHighPosCmd() {
        return new ArmDriveForSecondsCmd(ArmConfig.ArmAngleEjectHighPos, 5.0);
    }

    // -------- Arm to Eject Positions Commands -------
    // public static Command ArmToEjectLowPosCmd() {
    //     return ArmCmds.ArmSetMMangleCmd(ArmConfig.ArmAngleEjectLowPos);
    //         // .withTimeout(4.0).until(() -> Robot.arm.isMMtargetReached());
    // }

    // public static Command ArmToEjectMidPosCmd() {
    //     return ArmCmds.ArmSetMMangleCmd(ArmConfig.ArmAngleEjectMidPos);
    //         //.withTimeout(4.0).until(() -> Robot.arm.isMMtargetReached());
    // }

    // public static Command ArmToEjectHighPosCmd() {
    //     return ArmCmds.ArmSetMMangleCmd(ArmConfig.ArmAngleEjectHighPos);
    //         //.withTimeout(4.0).until(() -> Robot.arm.isMMtargetReached());
    // }

    // -------- Arm to Intake Positions Commands -------
    // public static Command ArmToIntakeCubePosCmd() {
    //     return ArmCmds.ArmSetMMangleCmd(ArmConfig.ArmAngleIntakeCubePos);
    //         //.withTimeout(4.0).until(() -> Robot.arm.isMMtargetReached());
    // }
    // public static Command ArmToIntakeConePosCmd() {
    //     return ArmCmds.ArmSetMMangleCmd(ArmConfig.ArmAngleIntakeConePos);
    //         //.withTimeout(4.0).until(() -> Robot.arm.isMMtargetReached());
    // }

    // -------- Arm Retracted Positions Commands -------
    // public static Command ArmToStorePosCmd() {
    //     return ArmCmds.ArmSetMMangleCmd(ArmConfig.ArmAngleStorePos);
    //         //.withTimeout(4.0).until(() -> Robot.arm.isMMtargetReached());
    // }

    // public static Command ArmToFullRetractCmd() {
    //     return new RunCommand( () -> Robot.arm.lowerArm(), Robot.arm);
    //         //.withTimeout(4.0).until(() ->Robot.arm.isRetractLimitSwitchPressed())
    //         // .withName("armToBottomCmd");
    // }

    // // --------- ArmRelease (Free Fall) Command ---------
    // public static Command ArmReleaseCmd() {
    //     return new ArmReleaseCmd().withTimeout(4.0);
    // }
}
