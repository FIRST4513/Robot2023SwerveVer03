package frc.robot.elevator.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.auto.commands.DelayCmd;
import frc.robot.elevator.ElevatorConfig;

// -----------------------------------------------------
// --------------   Elevator Commands    ---------------
// -----------------------------------------------------

public class ElevatorCmds {

    // Default Command
    public static void setupDefaultCommand() {
        Robot.elevator.setDefaultCommand(new ElevatorHoldPosCmd());
    }

    public static Command ElevHoldCmd() {
        return new RunCommand(() -> Robot.elevator.elevHoldMtr(), Robot.elevator )
            .withName("ElevHoldCmd");
    }

    public static Command ElevStopCmd() {
        return new InstantCommand( () -> Robot.elevator.elevStop(), Robot.elevator)
            .withName("ElevStopCmd");
    }

    public static Command ElevLowerCmd() {
        return new RunCommand( () -> Robot.elevator.elevLower(), Robot.elevator)
            .withName("ElevLowerCmd");
    }

    public static Command ElevRaiseCmd() {
        return new RunCommand( () -> Robot.elevator.elevRaise(), Robot.elevator)
            .withName("ElevRaiseCmd");
    }


    public static Command ElevGoToBottomCmd() {
        return new SequentialCommandGroup(
            // turn off soft limits here (InstantCommand) ??
            new RunCommand( () -> Robot.elevator.elevLower(), Robot.elevator)
                .until(() ->Robot.elevator.isLowerLimitSwitchPressed()),
            new DelayCmd(0.25),
            new InstantCommand( () -> Robot.elevator.resetEncoder())
            // turn on soft limits here ??
        ).withName("ElevGoToBottomCmd");
    }
    
    public static Command ElevByJoystickCmd() {
        return new RunCommand(
        () -> Robot.elevator.elevSetSpeed(() -> Robot.operatorGamepad.getElevInput()), Robot.elevator);
    }


    public static Command setMMPosition(double position) {
        return new RunCommand(() -> Robot.elevator.setMMheight(position), Robot.elevator);
    }

    public static Command resetSensorPosition() {
        return new InstantCommand( () -> Robot.elevator.resetEncoder());
    }


    public static Command setEncoder(double position) {
        return new InstantCommand( () -> Robot.elevator.resetEncoder( position ));
    }

    public static Command ElevSetBrakeOnCmd() {
        return new InstantCommand( () -> Robot.elevator.setBrakeMode( true ));
    }

    public static Command ElevSetBrakeOffCmd() {
        return new InstantCommand( () -> Robot.elevator.setBrakeMode( false ));
    }

    
    // -------- Intake Position Commands -------
    public static Command ElevToIntakeConePosCmd()  { return ElevatorCmds.ElevGoToBottomCmd(); }

    public static Command ElevToIntakeCubePosCmd()  { return ElevatorCmds.ElevGoToBottomCmd(); }


    // -------- Eject Position Commands -------
    public static Command ElevToEjectLowPosCmd()    { return ElevatorCmds.ElevGoToBottomCmd(); }

    public static Command ElevToEjectMidPosCmd() {
        return ElevatorCmds.setMMPosition(ElevatorConfig.ElevEjectMidHt)
                .until(() -> Robot.elevator.isMMtargetReached());
    }

    public static Command ElevToEjectHighPosCmd() {
        return ElevatorCmds.setMMPosition(ElevatorConfig.ElevEjectMidHt)
                .until(() -> Robot.elevator.isMMtargetReached());
    }

    
    // -------- Misc Position Commands -------
    public static Command ElevToBumperClearPosCmd() {
        return ElevatorCmds.setMMPosition(ElevatorConfig.ElevBumperClearHt)
                .until(() -> Robot.elevator.isMMtargetReached());
    }

    public static Command ElevToStorePosCmd()  { return ElevatorCmds.ElevGoToBottomCmd(); }

    public static Command ElevToRetractPosCmd()     { return ElevatorCmds.ElevGoToBottomCmd(); }

    public static Command ElevToArmReleasePosCmd() {
        return ElevatorCmds.setMMPosition(ElevatorConfig.ElevArmReleaseHt)
                .until(() -> Robot.elevator.isMMtargetReached());
    }

    public static Command InitialArmReleaseCmd() {
        return new ParallelCommandGroup(
            ArmCmds.ArmFreeFallCmd(),
            new ElevReleaseArmCmd()
        );
    }
}

