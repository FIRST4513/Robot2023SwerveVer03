package frc.robot.operator;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.lib.gamepads.Gamepad;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.operator.commands.OperatorGamepadCmds;

public class OperatorGamepad extends Gamepad {
    public OperatorGamepad() {
        super("Operator", OperatorGamepadConfig.port);
    }
    
    public void setupTeleopButtons() {

        // gamepad.bButton.onTrue(ElevatorCmds.InitialArmReleaseCmd()); // Test Button

        gamepad.bButton     .onTrue(IntakeCmds.IntakeCubeCmd());
        gamepad.yButton     .onTrue(IntakeCmds.IntakeConeCmd());
        gamepad.xButton     .onTrue(IntakeCmds.IntakeStopCmd());
        gamepad.aButton     .onTrue(IntakeCmds.IntakeEjectCmd());

        gamepad.Dpad.Right  .onTrue(OperatorGamepadCmds.SetArmElevToStorePosCmd());
        gamepad.selectButton.onTrue(OperatorGamepadCmds.SetArmElevToFullRetractPosCmd());

        gamepad.Dpad.Down   .onTrue(OperatorGamepadCmds.SetArmElevToEjectLowPosCmd());
        gamepad.Dpad.Left   .onTrue(OperatorGamepadCmds.SetArmElevToEjectMidPosCmd());
        gamepad.Dpad.Up     .onTrue(OperatorGamepadCmds.SetArmElevToEjectHighPosCmd());

        gamepad.rightBumper .whileTrue(OperatorGamepadCmds.ControlElevByJoysticksCmd());
        gamepad.leftBumper  .whileTrue(OperatorGamepadCmds.ControlArmByJoysticksCmd());

        gamepad.startButton .onTrue(ArmCmds.ResetArmEncoderCmd());
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
        if (OperatorGamepadConfig.elevYInvert) {
            return yValue * -0.33;
        } else {
            return yValue * 0.33;
        }
    }

    public double getArmInput() {
        double yValue = gamepad.leftStick.getY();
        if (Math.abs(yValue) < 0.175) {
            yValue = 0.0;
        }
        if (OperatorGamepadConfig.armYInvert) {
            return yValue * -0.5;
        } else {
            return yValue * 0.5;
        }
    }

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
    }
}
