package frc.robot.intake.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

public class IntakeCmds {
    public static void setupDefaultCommand() {
        Robot.intake.setDefaultCommand(IntakeStopCmd());
    }

    // ------------ Intake Stop ------------------
    public static Command IntakeStopCmd() {
        return new InstantCommand( () -> Robot.intake.stopMotors(), Robot.intake)
            .withName("IntakeStopCmd");
    }

    // ---------- Intake Retract Commands -----------
    public static Command IntakeRetractUntilGamepieceCmd() {
        return new RunCommand(() -> Robot.intake.setMotorRetract(), Robot.intake)
            .withName("Intake Retract Until Gamepiece Cmd")
            .until(() -> Robot.intake.isGamepieceDetected());
    }

    public static Command IntakeRetractRunCmd() {
        return new RunCommand(() -> Robot.intake.setMotorRetract(), Robot.intake)
            .withName("Intake Retract Run Cmd");
    }

    // ---------- Intake Hold Commands  -----------
    public static Command IntakeHoldUntilNoGamepieceCmd() {
        return new RunCommand(() -> Robot.intake.setMotorHold(), Robot.intake)
            .withName("Intake Hold Until No Gamepiece Cmd")
            .until(() -> Robot.intake.isGamepieceNotDetected());
    }

    public static Command IntakeHoldRunCmd() {
        return new RunCommand(() -> Robot.intake.setMotorHold(), Robot.intake)
            .withName("Intake Hold Run Cmd");
    }

    // ---------- Intake Eject Commands -----------
    public static Command IntakeEjectUntilNoGamepieceCmd() {
        return new RunCommand(() -> Robot.intake.setMotorEject(), Robot.intake)
            .withName("Intake Eject Until No Gamepiece Cmd")
            .until(() -> Robot.intake.isGamepieceNotDetected());
    }

    public static Command IntakeEjectRunCmd() {
        return new RunCommand(() -> Robot.intake.setMotorEject(), Robot.intake)
            .withName("Intake Eject Run Cmd");
    }

    // ------------ Intake Manual control ------------
    public static Command IntakeByJoystickCmd() {
        return new RunCommand( () -> Robot.intake.setMotor(() -> Robot.operatorGamepad.getTriggerTwist()), Robot.intake);
    }

    // // Here's an old command I'm gonna leave here, for sentimental value D':
    // // ---------- Intake Eject --------
    // public static Command IntakeEjectCmd() {
    //     return new SequentialCommandGroup(
    //         new ConditionalCommand(
    //             // True - Cube Detected
    //             new RunCommand(() -> Robot.intake.setMotorsCubeEject(), Robot.intake).withTimeout(2.0),
    //             // False - Must be Cone
    //             new RunCommand(() -> Robot.intake.setMotorsConeEject(), Robot.intake).withTimeout(2.0),
    //             // condition
    //             () -> Robot.intake.isCubeEjectDetected()
    //             ),
    //         new InstantCommand(() -> Robot.intake.setBrakeMode(true),Robot.intake)
    //     );
    // }
    // // lol
}
