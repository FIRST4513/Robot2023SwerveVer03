package frc.robot.operator;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.lib.gamepads.Gamepad;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.intake.commands.IntakeConeCmd;
import frc.robot.operator.commands.OperatorGamepadCmds;

public class OperatorGamepad extends Gamepad {
    public OperatorGamepad() {
        super("Operator", OperatorGamepadConfig.port);
    }
    
    public void setupTeleopButtons() {
        gamepad.aButton.onTrue(IntakeCmds.IntakeEjectCmd());
        gamepad.bButton.onTrue(IntakeCmds.IntakeCubeRetractCmd());
        gamepad.xButton.onTrue(IntakeCmds.IntakeStopCmd());
        gamepad.yButton.onTrue(new IntakeConeCmd());
        // gamepad.yButton.onTrue(new PrintCommand("Y Pressed"));

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
        if (Math.abs(yValue) < 0.05) {
            yValue = 0.0;
        }
        if (OperatorGamepadConfig.yInvert) {
            return yValue * -0.33;
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
