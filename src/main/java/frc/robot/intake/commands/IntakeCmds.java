package frc.robot.intake.commands;

import javax.swing.GroupLayout.SequentialGroup;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.intake.IntakeConfig;

public class IntakeCmds {
    public static void setupDefaultCommand() {
        Robot.intake.setDefaultCommand(IntakeStopCmd());
    }

    // --------- Intake On Commands -------------
    public static Command IntakeConeCmd() {
        return new IntakeConeSecondaryCmd();
        // return new IntakeConeMainCmd();
    }

    public static Command IntakeCubeCmd() {
        return new RunCommand(() -> Robot.intake.setMotorsCubeRetract(), Robot.intake)
            .withName("IntakeCubeRetractCmd")
            .until(() -> Robot.intake.isCubeRetractDetected());
    }

    // ---------- Intake Eject --------
    public static Command IntakeEjectCmd() {
        return new SequentialCommandGroup(
            new ConditionalCommand(
                // True - Cube Detected
                new RunCommand(() -> Robot.intake.setMotorsCubeEject(), Robot.intake).withTimeout(2.0),
                // False - Must be Cone
                new RunCommand(() -> Robot.intake.setMotorsConeEject(), Robot.intake).withTimeout(2.0),
                // condition
                () -> Robot.intake.isCubeEjectDetected()
                ),
            new InstantCommand(() -> Robot.intake.setBrakeMode(true),Robot.intake)
        );
    }

    public static Command IntakeCubeHoldCmd() {
        return new RunCommand(() -> Robot.intake.setMotorsCubeHold(), Robot.intake);
    }

    public static Command IntakeEjectCubeCmd() {
        return new RunCommand(() -> Robot.intake.setMotorsCubeEject(), Robot.intake).withTimeout(0.5);        
    }

    public static Command holdArmAndElevCmd() { 
        return new ParallelCommandGroup(
            ElevatorCmds.ElevHoldCmd(),
            ArmCmds.HoldArmCmd()
        );
    }

    public static Command IntakeEjectConeCmd() {
        return new RunCommand(() -> Robot.intake.setMotorsConeEject(), Robot.intake).withTimeout(1.0);
    }

    // ------------ Intake Stop ------------------
    public static Command IntakeStopCmd() {
        return new InstantCommand( () -> Robot.intake.stopMotors(), Robot.intake)
            .withName("IntakeStopCmd");
    }

    // ------------ Intake Manual control ------------
    public static Command IntakeByJoystickCmd() {
        return new RunCommand( () -> Robot.intake.setUpperMotor(() -> Robot.operatorGamepad.getTriggerTwist()), Robot.intake );
    }
}
