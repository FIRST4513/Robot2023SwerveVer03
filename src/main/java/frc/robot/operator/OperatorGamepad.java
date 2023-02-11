package frc.robot.operator;

import frc.lib.gamepads.Gamepad;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.operator.commands.OperatorGamepadCmds;

public class OperatorGamepad extends Gamepad {
    public OperatorGamepad() {
        super("Operator", OperatorGamepadConfig.port);
    }
    
    public void setupTeleopButtons() {
        gamepad.aButton.onTrue(IntakeCmds.IntakeEjectCmd());
        gamepad.bButton.onTrue(IntakeCmds.IntakeCubeRetractCmd());
        gamepad.xButton.onTrue(IntakeCmds.IntakeStopCmd());
        gamepad.yButton.onTrue(IntakeCmds.IntakeConeRetractCmd());

        gamepad.Dpad.Down.onTrue(OperatorGamepadCmds.SetArmElevToLowPosCmd());
        gamepad.Dpad.Right.onTrue(OperatorGamepadCmds.SetArmElevToStorePosCmd());
        gamepad.Dpad.Left.onTrue(OperatorGamepadCmds.SetArmElevToMidPosCmd());
        gamepad.Dpad.Up.onTrue(OperatorGamepadCmds.SetArmElevToHighPosCmd());

        gamepad.selectButton.onTrue(OperatorGamepadCmds.SetArmElevToFullBackPosCmd());

        gamepad.rightBumper.whileTrue(OperatorGamepadCmds.ControlArmElevByJoysticksCmd());
    }

    public void setupDisabledButtons() {
    }

    public void setupTestButtons() {
    }

    public double getElevInput() {
        double yValue = gamepad.rightStick.getY();
        if (OperatorGamepadConfig.yInvert) {
            return yValue * -1;
        }
        return yValue;
    }

    public double getArmInput() {
        double yValue = gamepad.leftStick.getY();
        if (OperatorGamepadConfig.yInvert) {
            return yValue * -1;
        }
        return yValue;
    }

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
    }
}
