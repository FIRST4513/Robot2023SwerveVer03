package frc.robot.operator;

import frc.lib.gamepads.Gamepad;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.operator.commands.OperatorGamepadCmds;

public class OperatorGamepad extends Gamepad {
    public OperatorGamepad() {
        super("Operator", OperatorGamepadConfig.port);
    }
    
    public void setupTeleopButtons() {
        gamepad.aButton.onTrue(IntakeCmds.intakeEjectCmd());
        gamepad.bButton.onTrue(IntakeCmds.intakeCubeRetractCmd());
        gamepad.xButton.onTrue(IntakeCmds.intakeStopCmd());
        gamepad.yButton.onTrue(IntakeCmds.intakeConeRetractCmd());

        gamepad.Dpad.Down.onTrue(OperatorGamepadCmds.setLowPosCmd());
        gamepad.Dpad.Right.onTrue(OperatorGamepadCmds.setStorePosCmd());
        gamepad.Dpad.Left.onTrue(OperatorGamepadCmds.setMidPosCmd());
        gamepad.Dpad.Up.onTrue(OperatorGamepadCmds.setHighPosCmd());

        gamepad.selectButton.onTrue(OperatorGamepadCmds.setFullBackPosCmd());

        gamepad.rightBumper.whileTrue(OperatorGamepadCmds.controlByJoysticks());
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
