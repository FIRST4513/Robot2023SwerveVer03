package frc.robot.pilot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.gamepads.Gamepad;
import frc.lib.gamepads.mapping.ExpCurve;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.operator.commands.OperatorGamepadCmds;
import frc.robot.pilot.PilotGamepadConfig.MaxSpeeds;
import frc.robot.pilot.commands.PilotGamepadCmds;
import frc.robot.swerve.commands.SwerveCmds;

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
    public SendableChooser<String> speedChooser = new SendableChooser<String>();

    public PilotGamepad() {
        super("Pilot", PilotGamepadConfig.port);
        setupSpeedMenu();
    }

    public void setupTeleopButtons() {
        // "X" Button - Robot POV
        gamepad.xButton.whileTrue(PilotGamepadCmds.RpvPilotSwerveCmd());
        
        // "Start" Button - Reset Gyro to 0
        gamepad.startButton.onTrue(SwerveCmds.ZeroGyroHeadingCmd());  // possible reset robo odo as well?
        
        // Disabled and non working buttons -> commands, will test in future:
        // gamepad.rightBumper.onTrue(PilotGamepadCmds.BasicSnapCmd());  // basic snap (turn-in-place)
        // gamepad.selectButton.onTrue(PilotGamepadCmds.FpvDriveAndAutoRotateCmd());  // snap to angle while driving
    }

    public void setupDisabledButtons() {}

    public void setupTestButtons() {}

    // forward/backward down the field
    public double getDriveFwdPositive() {
        return forwardSpeedCurve.calculateMappedVal(this.gamepad.leftStick.getY());
    }

    public double getDriveFwdPositiveSlow() {
        return getDriveFwdPositive()/2;
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

    public double getDriveLeftPositiveSlow() {
        return getDriveLeftPositive()/2;
    }

    //Positive is counter-clockwise, left Trigger is positive
    public double getDriveRotationCCWPositive() {
		double value = this.gamepad.triggers.getTwist();
		value = rotationCurve.calculateMappedVal(value);
		return value;        
    }

    public double getDriveRotationCCWPositiveSlow() {
        return getDriveRotationCCWPositive()/4;
    }

    // Return the angle created by the left stick in radians, 0 is up, pi/2 is left
    public Double getDriveAngle() {
        return Math.atan2(
            +getDriveFwdPositive(),
            -getDriveLeftPositive());
    }

    public MaxSpeeds getSelectedSpeed(){
        String speed = speedChooser.getSelected();;
        if ( speed == "Fast")    return MaxSpeeds.FAST;
        if ( speed == "Medium") return MaxSpeeds.MEDIUM;
        return MaxSpeeds.SLOW;
    }

    public void setupSpeedMenu(){
            // Setup Speed Selector
            speedChooser.addOption        ("1. Slow",      "Slow");
            speedChooser.addOption        ("2. Medium", "Medium");
            speedChooser.addOption        ("4. Fast", 	   "Fast");
            SmartDashboard.putData(speedChooser);
    }
    
    public void setMaxSpeeds(MaxSpeeds speed){
        switch (speed) { 
            case FAST:
                System.out.println("Driver Speeds set to FAST !!!");
                forwardSpeedCurve.setScalar(PilotGamepadConfig.FastfowardVelocity);
                sidewaysSpeedCurve.setScalar(PilotGamepadConfig.FastsidewaysVelocity);
                rotationCurve.setScalar(PilotGamepadConfig.FastsidewaysVelocity);
                // forwardSpeedCurve.setExpVal(PilotGamepadConfig.FastForwardExp);
                // sidewaysSpeedCurve.setExpVal(PilotGamepadConfig.FastSidewaysExp);
                // rotationCurve.setExpVal(PilotGamepadConfig.FastRotationExp);
                break;
            case MEDIUM:
                System.out.println("Driver Speeds set to MEDIUM !!!");
                forwardSpeedCurve.setScalar(PilotGamepadConfig.MediumForwardVelocity);
                sidewaysSpeedCurve.setScalar(PilotGamepadConfig.MediumSidewaysVelocity);
                rotationCurve.setScalar(PilotGamepadConfig.MediumSidewaysVelocity);
                // forwardSpeedCurve.setExpVal(PilotGamepadConfig.MedSlowForwardExp);
                // sidewaysSpeedCurve.setExpVal(PilotGamepadConfig.MedSlowSidewaysExp);
                // rotationCurve.setExpVal(PilotGamepadConfig.FastRotationExp);
                break;
            default:
                System.out.println("Driver Speeds set to SLOW !!!");
                forwardSpeedCurve.setScalar(PilotGamepadConfig.SlowforwardVelocity);
                sidewaysSpeedCurve.setScalar(PilotGamepadConfig.SlowsidewaysVelocity);
                rotationCurve.setScalar(PilotGamepadConfig.SlowsidewaysVelocity);
                // forwardSpeedCurve.setExpVal(PilotGamepadConfig.SlowForwardExp);
                // sidewaysSpeedCurve.setExpVal(PilotGamepadConfig.SlowSidewaysExp);
                // rotationCurve.setExpVal(PilotGamepadConfig.SlowRotationExp);
                break;
        }
    }

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
    }
}
