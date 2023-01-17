package frc.robot.pilot;

import frc.lib.gamepads.Gamepad;
import frc.lib.gamepads.mapping.ExpCurve;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.swerveDrive.commands.SwerveDriveCmds;

/** Used to add buttons to the pilot gamepad and configure the joysticks */
public class PilotGamepad extends Gamepad {
    //public final PilotGamepadTelemetry telemetry;
    public static ExpCurve forwardSpeedCurve =
            new ExpCurve(
                    PilotGamepadConfig.forwardSpeedExp,
                    PilotGamepadConfig.forwardSpeedOffset,
                    PilotGamepadConfig.forwardSpeedScaler,
                    PilotGamepadConfig.forwardSpeedDeadband);
    public static ExpCurve sidewaysSpeedCurve =
            new ExpCurve(
                    PilotGamepadConfig.sidewaysSpeedExp,
                    PilotGamepadConfig.sidewaysSpeedOffset,
                    PilotGamepadConfig.sidewaysSpeedScaler,
                    PilotGamepadConfig.sidewaysSpeedDeadband);
    public static ExpCurve rotationCurve =
            new ExpCurve(
                    PilotGamepadConfig.rotationSpeedExp,
                    PilotGamepadConfig.rotationSpeedOffset,
                    PilotGamepadConfig.rotationSpeedScaler,
                    PilotGamepadConfig.rotationSpeedDeadband);

    public PilotGamepad() {
        super("Pilot", PilotGamepadConfig.port);
        //telemetry = new PilotGamepadTelemetry(this);
    }

    public void setupTeleopButtons() {
        //gamepad.aButton.whileTrue(ElevatorCmds.goToPIDPosCmd(30.0));
        // Setup Elevator button Assignments

        //final JoystickButton xboxButton1 = new JoystickButton(gamepad, XboxController.Button.kA.value);        
        //xboxButton1.whileTrue(SliderCmds.sliderLeftCmd().withInterruptBehavior(InterruptionBehavior.kCancelSelf));
       
        gamepad.Dpad.Down.whileTrue(ElevatorCmds.lowerCmd());
        gamepad.Dpad.Up.whileTrue(ElevatorCmds.raiseCmd());
        // gamepad.Dpad.Left.onTrue(null);  // same as the old "when pressed"

        // Reset Gyro/Encoders/Pose Data
        //gamepad.selectButton.whenPressed(DrivetrainCmds.resetPoseCmd());
    }

    public void setupDisabledButtons() {
    }

    public void setupTestButtons() {
        gamepad.aButton.whileTrue(SwerveDriveCmds.testWheelFwdCmd());
        gamepad.bButton.whileTrue(SwerveDriveCmds.testWheelFwdLeftCmd());
        gamepad.xButton.whileTrue(SwerveDriveCmds.testWheelFwdRightCmd());
        gamepad.yButton.whileTrue(SwerveDriveCmds.lockSwerveCmd());
        gamepad.startButton.whileTrue(SwerveDriveCmds.resetFalconAnglesCmd());
    }

    // forward/backward down the field
    public double getDriveFwdPositive() {
        return forwardSpeedCurve.calculateMappedVal(this.gamepad.leftStick.getY());
    }

    // side-to-side across the field
    public double getDriveLeftPositive() {
        return sidewaysSpeedCurve.calculateMappedVal(this.gamepad.rightStick.getX());
    }

    //Positive is counter-clockwise, left Trigger is positive
    public double getDriveRotationCCWPositive() {
		double value = this.gamepad.triggers.getTwist();
		value = rotationCurve.calculateMappedVal(value);
		return value;        
    }

    // Return the angle created by the left stick in radians, 0 is up, pi/2 is left
    public Double getDriveAngle() {
        return Math.atan2(
            getDriveFwdPositive(),
            -getDriveLeftPositive());
    }

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
     }
}
