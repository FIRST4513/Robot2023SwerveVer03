package frc.robot.elevator.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.auto.commands.DelayCmd;

// -----------------------------------------------------
// --------------   Elevator Commands    ---------------
// -----------------------------------------------------

public class ElevatorCmds {

    // Default Command
    public static void setupDefaultCommand() {
        Robot.elevator.setDefaultCommand(ElevHoldCmd());
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
            new RunCommand( () -> Robot.elevator.elevLower(), Robot.elevator)
                .until(() ->Robot.elevator.isLowerLimitSwitchPressed()),
            new DelayCmd(0.25),
            new InstantCommand( () -> Robot.elevator.resetEncoder())
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

}

