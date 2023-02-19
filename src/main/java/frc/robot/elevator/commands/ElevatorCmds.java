package frc.robot.elevator.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
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

    // pure elevator height
    public static Command ElevGoToPIDheightCmd(double inches) {
        return new RunCommand( () -> Robot.elevator.setPIDheight(inches), Robot.elevator)
            .withName("ElevGoToPIDPosCmd");
    }

    public static Command ElevGoToBottomCmd() {
        return new RunCommand( () -> Robot.elevator.elevLower(), Robot.elevator)
            .until(() ->Robot.elevator.isLowerLimitSwitchPressed())
            .withName("ElevGoToBottomCmd");
    }
    
    public static Command ElevByJoystickCmd() {
        return new RunCommand(
        () -> Robot.elevator.elevSetSpeed(() -> Robot.operatorGamepad.getElevInput()), Robot.elevator);
    }

    // ------------------------------------------------------
    // ---------- Commands from Spectrum 2023 Code ----------
    // ------------------------------------------------------

    public static Command setOutput(double value) {
        return new RunCommand(() -> Robot.elevator.elevSetSpeed(value), Robot.elevator);
    }

    public static Command setOutput(DoubleSupplier value) {
        return new RunCommand(
                () -> Robot.elevator.elevSetSpeed(value.getAsDouble()), Robot.elevator);
    }

    public static Command setMMPosition(double position) {
        return new RunCommand(() -> Robot.elevator.setMMheight(position), Robot.elevator);
    }


    // public static Command coneIntake() {
    //     return setMMPosition(ElevatorConfig.coneIntake);
    // }

    // public static Command coneStandingIntake() {
    //     return setMMPosition(ElevatorConfig.coneStandingIntake);
    // }

    // public static Command coneMid() {
    //     return setMMPosition(ElevatorConfig.coneMid);
    // }

    // public static Command coneTop() {
    //     return setMMPosition(ElevatorConfig.coneTop);
    // }

    // public static Command coneShelf() {
    //     return setMMPosition(ElevatorConfig.coneShelf);
    // }

    // public static Command cubeIntake() {
    //     return setMMPosition(ElevatorConfig.cubeIntake);
    // }

    // public static Command cubeMid() {
    //     return setMMPosition(ElevatorConfig.cubeMid);
    // }

    // public static Command cubeTop() {
    //     return setMMPosition(ElevatorConfig.cubeTop);
    // }

    public static Command home() {
        return setMMPosition(0);
    }

    public static Command zeroElevator() {
        return new RunCommand(() -> Robot.elevator.zeroElevator(), Robot.elevator);
    }

    public static Command resetSensorPosition() {
        return new RunCommand(() -> Robot.elevator.resetSensorPosition(0), Robot.elevator);
    }

    // below function is not used
    public static Command runDownAndZero() {
        return new StartEndCommand(
                () -> Robot.elevator.elevSetSpeed(-0.1),
                () -> Robot.elevator.zeroElevator(),
                // () -> Robot.elevator.resetSensorPosition(),
                Robot.elevator);
    }

    public static Command setEncoder(double position) {
        return new RunCommand(() -> Robot.elevator.resetEncoder(position), Robot.elevator);
    }

    public static Command resetEncoder() {
        return setEncoder(0);
    } 
}
