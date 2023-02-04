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
        return new InstantCommand( () -> Robot.elevator.stop(), Robot.elevator)
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

    public static Command goToPIDheightCmd(double height) {
        return new InstantCommand( () -> Robot.elevator.setPIDheight(height), Robot.elevator)
            .withName("ToPIDHeightCmd");
    }

    public static Command goToPIDPosCmd(double inches) {
        return new InstantCommand( () -> Robot.elevator.setPIDposition(inches), Robot.elevator)
            .withName("ToPIDPosCmd");
    }

    public static Command goToBottomCmd() {
        return new RunCommand( () -> Robot.elevator.elevLower(), Robot.elevator)
            .until(() ->Robot.elevator.isLowerLmtReached())
            .withName("ToPIDPosCmd");
    }
    
    public static Command elevByJoystick() {
        return new RunCommand(
        () -> Robot.elevator.elevSetSpeed(() -> Robot.operatorGamepad.getElevInput()), Robot.arm);
    }
}
