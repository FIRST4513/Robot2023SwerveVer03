package frc.robot.elevator.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

// -----------------------------------------------------
// --------------   Elevator Commands    ---------------
// -----------------------------------------------------

public class ElevatorCmds {

    // Default Command
    public static void setupDefaultCommand() {
        Robot.elevator.setDefaultCommand(holdCmd());
    }

    public static Command holdCmd() {
        return new RunCommand(() -> Robot.elevator.elevHoldMtr(), Robot.elevator )
            .withName("HoldCmd");
    }

    public static Command stop() {
        return new InstantCommand( () -> Robot.elevator.elevStop(), Robot.elevator)
            .withName("StopCmd");
    }

    public static Command lowerCmd() {
        return new RunCommand( () -> Robot.elevator.elevLower(), Robot.elevator)
            .withName("LowerCmd");
    }

    public static Command raiseCmd() {
        return new RunCommand( () -> Robot.elevator.elevRaise(), Robot.elevator)
            .withName("RaiseCmd");
    }

    // relative to floor, considering robot height
    public static Command ElevGoToPIDheightCmd(double height) {
        return new InstantCommand( () -> Robot.elevator.setPIDheight(height), Robot.elevator)
            .withName("ToPIDHeightCmd");
    }

    // pure elevator height
    public static Command ElevGoToPIDPosCmd(double inches) {
        return new RunCommand( () -> Robot.elevator.setPIDposition(inches), Robot.elevator)
            .withName("ToPIDPosCmd");
    }

    public static Command ElevGoToBottomCmd() {
        return new RunCommand( () -> Robot.elevator.elevLower(), Robot.elevator)
            .until(() ->Robot.elevator.isLowerLimitSwitchPressed())
            .withName("ToPIDPosCmd");
    }
    
    public static Command elevByJoystick() {
        return new RunCommand(
        () -> Robot.elevator.elevSetSpeed(() -> Robot.operatorGamepad.getElevInput()), Robot.arm);
    }
}
