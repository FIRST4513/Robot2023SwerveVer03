package frc.robot.intake.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

public class IntakeCmds {
    public static void setupDefaultCommand() {
        Robot.elevator.setDefaultCommand(intakeStopCmd());
    }

    public static Command intakeStopCmd() {
        return new InstantCommand( () -> Robot.intake.stopMotors(), Robot.intake)
            .withName("IntakeStopCmd");
    }

    public static Command intakeCubeRetractCmd() {
        return new RunCommand(() -> Robot.intake.setMotorsCubeRetract(), Robot.intake)
            .withName("IntakeCubeRetract")
            .until(() -> Robot.intake.isCubeDetectSwitchPressed());
    }

    public static Command intakeConeRetractCmd() {
        return new RunCommand(() -> Robot.intake.setMotorsConeRetract(), Robot.intake)
            .withName("IntakeConeRetract")
            .until(() -> Robot.intake.isConeDetectSwitchPressed());
    }

    public static Command intakeCubeEjectCmd() {
        return new RunCommand(() -> Robot.intake.setMotorsCubeEject(), Robot.intake)
            .withName("IntakeCubeEject")
            .withTimeout(0.5);
    }

    public static Command intakeConeEjectCmd() {
        return new RunCommand(() -> Robot.intake.setMotorsConeEject(), Robot.intake)
            .withName("IntakeConeEject")
            .withTimeout(0.5);
    }
}
