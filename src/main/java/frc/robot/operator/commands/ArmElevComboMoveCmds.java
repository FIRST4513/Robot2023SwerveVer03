package frc.robot.operator.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.auto.commands.DelayCmd;
import frc.robot.elevator.ElevatorConfig;

public class ArmElevComboMoveCmds{
    public static Command SetArmMoveElevStowPosCmd() {
        return new SequentialCommandGroup(
            new InstantCommand(() -> Robot.arm.setMMTargetAngle(ArmConfig.ArmAngleStowPos), Robot.arm),
            new RunCommand(() -> Robot.elevator.setMMheight(ElevatorConfig.ElevBumperClearHt), Robot.elevator)
                .until(() -> Robot.elevator.isMMtargetReached()).withTimeout(5.0),
            new DelayCmd(2).until(() -> Robot.arm.isArmInside()),
            new RunCommand(() -> Robot.elevator.setMMheight(ElevatorConfig.ElevStoreHt), Robot.elevator)
                .until(() -> Robot.elevator.isMMtargetReached()).withTimeout(5.0)
        );
    }

    public static Command SetArmMoveElevIntakePosCmd() {
        return new SequentialCommandGroup(
            new InstantCommand(() -> Robot.arm.setMMTargetAngle(ArmConfig.ArmAngleIntakePos), Robot.arm),
            new RunCommand(() -> Robot.elevator.setMMheight(ElevatorConfig.ElevBumperClearHt), Robot.elevator)
                .until(() -> Robot.elevator.isMMtargetReached()).withTimeout(5.0),
            new DelayCmd(2).until(() -> Robot.arm.isMMtargetReached()),
            new RunCommand(() -> Robot.elevator.setMMheight(ElevatorConfig.ElevIntakeHt), Robot.elevator)
                .until(() -> Robot.elevator.isMMtargetReached()).withTimeout(5.0)
        );
    }

    public static Command SetArmRunElevMidPosCmd() {
        return new SequentialCommandGroup(
            new SequentialCommandGroup(
                new InstantCommand(() -> Robot.arm.setMMTargetAngle(0)),
                new RunCommand(() -> Robot.elevator.setMMheight(ElevatorConfig.ElevEjectMidHt), Robot.elevator)
                    .until(() -> Robot.elevator.isMMtargetReached()).withTimeout(5.0)
            ),
            new InstantCommand(() -> Robot.arm.setMMTargetAngle(ArmConfig.ArmAngleEjectMidPos), Robot.arm)
        );
    }

    public static Command SetArmRunElevHighPosCmd() {
        return new SequentialCommandGroup(
            new SequentialCommandGroup(
                new InstantCommand(() -> Robot.arm.setMMTargetAngle(0)),
                new RunCommand(() -> Robot.elevator.setMMheight(ElevatorConfig.ElevEjectHighHt), Robot.elevator)
                    .until(() -> Robot.elevator.isMMtargetReached()).withTimeout(5.0)
            ),
            new InstantCommand(() -> Robot.arm.setMMTargetAngle(ArmConfig.ArmAngleEjectHighPos), Robot.arm)
        );
    }
}
