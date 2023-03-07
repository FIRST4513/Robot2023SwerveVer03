package frc.robot.operator;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.lib.gamepads.Gamepad;
import frc.lib.gamepads.mapping.ExpCurve;
import frc.robot.Robot;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.elevator.ElevFXMotorConfig;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.operator.commands.OperatorGamepadCmds;

public class OperatorGamepad extends Gamepad {
    public static ExpCurve intakeThrottleCurve = new ExpCurve(
        OperatorGamepadConfig.intakeSpeedExp,
        OperatorGamepadConfig.intakeSpeedOffset,
        OperatorGamepadConfig.intakeSpeedScaler,
        OperatorGamepadConfig.intakeSpeedDeadband);

public static ExpCurve elevThrottleCurve = new ExpCurve(
    OperatorGamepadConfig.elevSpeedExp,
    OperatorGamepadConfig.elevSpeedOffset,
    OperatorGamepadConfig.elevSpeedScaler,
    OperatorGamepadConfig.elevSpeedDeadband);

public static ExpCurve armThrottleCurve = new ExpCurve(
    OperatorGamepadConfig.armSpeedExp,
    OperatorGamepadConfig.armSpeedOffset,
    OperatorGamepadConfig.armSpeedScaler,
    OperatorGamepadConfig.armSpeedDeadband);

    public OperatorGamepad() {
        super("Operator", OperatorGamepadConfig.port);
    }
    
    public void setupTeleopButtons() {

        // gamepad.bButton.onTrue(ElevatorCmds.InitialArmReleaseCmd()); // Test Button

        gamepad.bButton     .onTrue(IntakeCmds.IntakeCubeCmd());
        gamepad.yButton     .onTrue(IntakeCmds.IntakeConeCmd());
        gamepad.xButton     .onTrue(IntakeCmds.IntakeStopCmd());
        gamepad.aButton     .onTrue(IntakeCmds.IntakeEjectCmd());

        //gamepad.selectButton.onTrue(OperatorGamepadCmds.SetArmElevToFullRetractPosCmd());
        //gamepad.selectButton.onTrue(new AutoBalanceCommand());
        
        gamepad.Dpad.Up     .onTrue(OperatorGamepadCmds.SetArmElevToEjectHighPosCmd());
        gamepad.Dpad.Down   .onTrue(OperatorGamepadCmds.SetArmElevToEjectLowPosCmd());
        gamepad.Dpad.Left   .onTrue(OperatorGamepadCmds.SetArmElevToEjectMidPosCmd());
        gamepad.Dpad.Right  .onTrue(OperatorGamepadCmds.SetArmElevToStorePosCmd());

        gamepad.rightBumper .onTrue(OperatorGamepadCmds.ControlArmElevByJoysticksCmd());
        gamepad.leftBumper  .whileTrue(IntakeCmds.IntakeByJoystickCmd());

        gamepad.startButton .onTrue(ArmCmds.ResetArmEncoderCmd());
    }

    public void setupTestButtons() {
        gamepad.bButton     .onTrue(ElevatorCmds.InitialArmReleaseCmd());
        gamepad.aButton     .onTrue(new PrintCommand("Test Button A"));
    }

    public void setupDisabledButtons() {
    }


    public double getElevInput() {
        return elevThrottleCurve.calculateMappedVal(gamepad.rightStick.getY()) ;

        // double yValue = gamepad.rightStick.getY();
        // if (Math.abs(yValue) < 0.05) {
        //     yValue = 0.0;
        // }
        // if (OperatorGamepadConfig.elevYInvert) {
        //     return yValue * -0.5;
        // } else {
        //     return yValue * 0.5;
        // }
    }

    public double getElevInputWFF() {
        // calculate value with feed forward for elevator
        return getElevInput() + ElevFXMotorConfig.arbitraryFeedForward;
    }

    public double getArmInput() {
        return armThrottleCurve.calculateMappedVal(gamepad.leftStick.getY());

        // double yValue = gamepad.leftStick.getY();
        // if (Math.abs(yValue) < 0.175) {
        //     yValue = 0.0;
        // }
        // if (OperatorGamepadConfig.armYInvert) {
        //     return yValue * -0.5;
        // } else {
        //     return yValue * 0.5;
        // }
    }

    public double getArmInputWFF() {
        // calculate value with feed forward for arm
        return getArmInput() + Robot.arm.getHoldPwr();
    }

    public double getTriggerTwist() {
        // return -gamepad.triggers.getTwist()/3;
        return intakeThrottleCurve.calculateMappedVal(gamepad.triggers.getTwist());
    }
    
    public double getTriggerTwistInvert() {
        return -getTriggerTwist();
    }

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
    }
    public boolean isArmAndElevAtPos() {
        if ((Robot.arm.isMMtargetReached()) && (Robot.elevator.isMMtargetReached())) {
            return true;
        }
        return false;
    }

    
}
