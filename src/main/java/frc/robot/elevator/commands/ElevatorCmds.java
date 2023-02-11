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

    // relative to floor, considering robot height
    public static Command ElevGoToPIDheightCmd(double height) {
        return new InstantCommand( () -> Robot.elevator.setPIDheight(height), Robot.elevator)
            .withName("ElevGoToPIDHeightCmd");
    }

    // pure elevator height
    public static Command ElevGoToPIDPosCmd(double inches) {
        return new RunCommand( () -> Robot.elevator.setPIDposition(inches), Robot.elevator)
            .withName("ElevGoToPIDPosCmd");
    }

    public static Command ElevGoToBottomCmd() {
        return new RunCommand( () -> Robot.elevator.elevLower(), Robot.elevator)
            .until(() ->Robot.elevator.isLowerLimitSwitchPressed())
            .withName("ElevGoToBottomCmd");
    }
    
    public static Command ElevByJoystickCmd() {
        return new RunCommand(
        () -> Robot.elevator.elevSetSpeed(() -> Robot.operatorGamepad.getElevInput()), Robot.arm);
    }
}
