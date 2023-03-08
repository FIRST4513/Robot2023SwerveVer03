package frc.robot.pilot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.gamepads.Gamepad;
import frc.lib.gamepads.mapping.ExpCurve;
import frc.robot.auto.AutoConfig;
import frc.robot.elevator.commands.ElevatorCmds;
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
        // "A" Button - Teleop Drive with Robot Perspective
        gamepad.aButton.onTrue(IntakeCmds.IntakeEjectCmd());
        gamepad.bButton.onTrue(OperatorGamepadCmds.IntakeCubeCmd());
        gamepad.yButton.onTrue(OperatorGamepadCmds.IntakeConeCmd());
        gamepad.xButton.whileTrue(PilotGamepadCmds.RpvPilotSwerveCmd());

        gamepad.leftBumper.onTrue(IntakeCmds.IntakeStopCmd());
        // gamepad.rightBumper.onTrue(PilotGamepadCmds.BasicSnapCmd());  // basic snap (turn-in-place)
         
        // "Start" Button - Rest Gyro to 0
        gamepad.startButton.onTrue(SwerveCmds.ZeroGyroHeadingCmd());
        // gamepad.startButton.onTrue(SwerveCmds.ResetOdometryCmd());
        // gamepad.selectButton.onTrue(PilotGamepadCmds.FpvDriveAndAutoRotateCmd());  // snap to angle while driving

        gamepad.Dpad.Down.onTrue(OperatorGamepadCmds.SetArmElevToStorePosCmd());
        gamepad.Dpad.Up.onTrue(OperatorGamepadCmds.SetArmElevToIntakeConePosCmd());
        gamepad.Dpad.Left.onTrue(IntakeCmds.IntakeCubeHoldCmd());
    }

    public void setupDisabledButtons() {
    }

    public void setupTestButtons() {
        gamepad.aButton.whileTrue(SwerveCmds.TestWheelFwdCmd());
        gamepad.bButton.whileTrue(SwerveCmds.TestWheelFwdLeftCmd());
        gamepad.xButton.whileTrue(SwerveCmds.TestWheelFwdRightCmd());
        gamepad.bButton.onTrue(ElevatorCmds.InitialArmReleaseCmd());
        gamepad.startButton.whileTrue(SwerveCmds.ResetFalconAnglesCmd());
    }

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
        return getDriveRotationCCWPositive()/2;
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
        if ( speed == "MedFast") return MaxSpeeds.MEDFAST;
        if ( speed == "MedSlow") return MaxSpeeds.MEDSLOW;
        return MaxSpeeds.SLOW;
    }

    public void setupSpeedMenu(){
            // Setup Speed Selector
            speedChooser.addOption          ("1. SLOW",         "Slow");
            speedChooser.addOption          ("2. MED. Slow",	"MedSlow");
            speedChooser.setDefaultOption   ("3. MED. Fast", 	"MedFast");
            speedChooser.addOption          ("4. Fast", 	    "Fast");
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
            case MEDFAST:
                System.out.println("Driver Speeds set to MEDFAST !!!");
                forwardSpeedCurve.setScalar(PilotGamepadConfig.MedFastforwardVelocity);
                sidewaysSpeedCurve.setScalar(PilotGamepadConfig.MedFastsidewaysVelocity);
                rotationCurve.setScalar(PilotGamepadConfig.MedFastsidewaysVelocity);
                // forwardSpeedCurve.setExpVal(PilotGamepadConfig.MedFastForwardExp);
                // sidewaysSpeedCurve.setExpVal(PilotGamepadConfig.MedFastSidewaysExp);
                // rotationCurve.setExpVal(PilotGamepadConfig.MedFastRotationExp);
                break;
            case MEDSLOW:
                System.out.println("Driver Speeds set to MEDSLOW !!!");
                forwardSpeedCurve.setScalar(PilotGamepadConfig.MedSlowforwardVelocity);
                sidewaysSpeedCurve.setScalar(PilotGamepadConfig.MedSlowsidewaysVelocity);
                rotationCurve.setScalar(PilotGamepadConfig.MedSlowsidewaysVelocity);
                // forwardSpeedCurve.setExpVal(PilotGamepadConfig.MedSlowForwardExp);
                // sidewaysSpeedCurve.setExpVal(PilotGamepadConfig.MedSlowSidewaysExp);
                // rotationCurve.setExpVal(PilotGamepadConfig.FastRotationExp);
                break;
            default:
                System.out.println("Driver Speeds set to Slow !!!");
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
