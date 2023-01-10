package frc.robot.pilotGamepad;

import frc.lib.gamepads.Gamepad;
import frc.lib.gamepads.mapping.ExpCurve;
import frc.robot.elevator.commands.ElevatorCmds;

/** Used to add buttons to the pilot gamepad and configure the joysticks */
public class PilotGamepad extends Gamepad {
    public static ExpCurve forwardSpeedCurve =
            new ExpCurve(
                    PilotGamepadConfig.forwardSpeedExp,
                    0,
                    PilotGamepadConfig.forwardSpeedScaler,
                    PilotGamepadConfig.forwardSpeedDeadband);
    public static ExpCurve sidewaysSpeedCurve =
            new ExpCurve(
                    PilotGamepadConfig.sidewaysSpeedExp,
                    0,
                    PilotGamepadConfig.sidewaysSpeedScaler,
                    PilotGamepadConfig.sidewaysSpeedDeadband);
    public static ExpCurve rotationCurve =
            new ExpCurve(
                    PilotGamepadConfig.rotationSpeedExp,
                    0,
                    PilotGamepadConfig.rotationSpeedScaler,
                    PilotGamepadConfig.rotationSpeedDeadband);

    public PilotGamepad() {
        super("Pilot", PilotGamepadConfig.port);
    }

    public void setupTeleopButtons() {
        gamepad.aButton.whileTrue(ElevatorCmds.goToHeight(30));
    }

    public void setupDisabledButtons() {
        //gamepad.aButton.whileTrue(LEDCommands.solidColor(Color.kGreen, "Green", 5, 5));
        //gamepad.bButton.whileTrue(LEDCommands.blink(Color.kBlue, "Blink Blue", 10, 5));
        //gamepad.xButton.whileTrue(LEDCommands.rainbow("Rainbow", 15, 5));
        //gamepad.yButton.whileTrue(LEDCommands.snowfall("Snowfall", 20, 5));
    }

    public void setupTestButtons() {}

    // forward/backward across the field
    public double getDriveFwdPositive() {
        return forwardSpeedCurve.calculateMappedVal(this.gamepad.leftStick.getY());
    }

    // 
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


    // Return the angle created by the right stick in radians, 0 is up, pi/2 is left
    // public double getRightStickAngle() {
    //     return gamepad.rightStick.getDirectionRadians(
    //             gamepad.rightStick.getY(),
    //             gamepad.rightStick.getX());
    // }

    // // Return the angle created by the right stick in radians, 0 is up, pi/2 is left
    // public double getRightStickAngle() {
    //     return gamepad.rightStick.getDirectionRadians(
    //             gamepad.rightStick.getY(), gamepad.rightStick.getX());
    // }
    // 	public static double getDriveY(){
    // 		return throttleCurve.calculateMappedVal(driver.leftStick.getY());
    // 	}

    // 	public static double getDriveX(){
    // 		return throttleCurve.calculateMappedVal(driver.leftStick.getX());
    // 	}


    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
     }
}
