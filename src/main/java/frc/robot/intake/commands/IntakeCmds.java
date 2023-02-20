package frc.robot.intake.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

public class IntakeCmds {
    public static void setupDefaultCommand() {
        Robot.intake.setDefaultCommand(IntakeStopCmd());
    }

    public static Command IntakeStopCmd() {
        return new InstantCommand( () -> Robot.intake.stopMotors(), Robot.intake)
            .withName("IntakeStopCmd");
    }

    public static Command IntakeCubeRetractCmd() {
        return new RunCommand(() -> Robot.intake.setMotorsCubeRetract(), Robot.intake)
            .withName("IntakeCubeRetractCmd")
            .until(() -> Robot.intake.isCubeRetractDetected());
    }

    // public static Command IntakeConeRetractCmd() {
    //     return new RunCommand(() -> Robot.intake.setMotorsConeRetract(), Robot.intake)
    //         .withName("IntakeConeRetractCmd")
    //         .alongWith(new PrintCommand("cone retract initiated"))
    //         .until(() -> Robot.intake.isConeDetected());
    // }

    public static Command IntakeEjectCmd() {
        return new ConditionalCommand(
            // cube
            new RunCommand(() -> Robot.intake.setMotorsCubeEject(), Robot.intake).withTimeout(1.0),
            // cone
            new RunCommand(() -> Robot.intake.setMotorsConeEject(), Robot.intake).withTimeout(1.0),
            // condition
            () -> Robot.intake.isCubeEjectDetected()
        );
    }
    
}
