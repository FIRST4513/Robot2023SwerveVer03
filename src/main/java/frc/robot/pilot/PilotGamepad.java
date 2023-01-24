package frc.robot.pilot;

import frc.lib.gamepads.Gamepad;
import frc.lib.gamepads.mapping.ExpCurve;
import frc.robot.logger.commands.LoggerCmds;
import frc.robot.pilot.commands.PilotGamepadCmds;
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

        // Rest Gyro to 0
        gamepad.startButton.onTrue(SwerveDriveCmds.zeroGyroHeadingCmd());

        // Teleop Drive with R0bot Perspective
        gamepad.aButton.whileTrue(PilotGamepadCmds.rpvPilotSwerveCmd());
        gamepad.yButton.onTrue(LoggerCmds.WriteOutLogFileCmd());

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
        // right will be priority in code, but not primarily used in driving practice
        if (Math.abs(this.gamepad.rightStick.getX()) > Math.abs(this.gamepad.leftStick.getX())) {
            return sidewaysSpeedCurve.calculateMappedVal(this.gamepad.rightStick.getX()) * 1;  // change later to scale
        } else {
            return sidewaysSpeedCurve.calculateMappedVal(this.gamepad.leftStick.getX());
        }
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
