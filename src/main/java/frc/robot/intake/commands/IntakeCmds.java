package frc.robot.intake.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

public class IntakeCmds {
    public static void setupDefaultCommand() {
        Robot.elevator.setDefaultCommand(IntakeStopCmd());
    }

    public static Command IntakeStopCmd() {
        return new InstantCommand( () -> Robot.intake.stopMotors(), Robot.intake)
            .withName("IntakeStopCmd");
    }

    public static Command IntakeCubeRetractCmd() {
        return new RunCommand(() -> Robot.intake.setMotorsCubeRetract(), Robot.intake)
            .withName("IntakeCubeRetractCmd")
            .until(() -> Robot.intake.isCubeDetected());
    }

    public static Command IntakeConeRetractCmd() {
        return new RunCommand(() -> Robot.intake.setMotorsConeRetract(), Robot.intake)
            .withName("IntakeConeRetractCmd")
            .until(() -> Robot.intake.isConeDetected());
    }

    public static Command IntakeEjectCmd() {
        return new RunCommand(() -> Robot.intake.setMotorsEject(), Robot.intake)
            .withName("IntakeEjectCmd")
            .withTimeout(0.5);
    }
    
}
