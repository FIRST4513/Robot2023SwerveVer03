package frc.robot.slider;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

public class SliderCmds {

    public static void setupDefaultCommand() {
        // not really needed
        //Robot.slider.setDefaultCommand(sliderStopCmd());
    }

    // -------- Slider to Preset Positions --------
    public static Command sliderFullLeftCmd() {
        return new RunCommand(() -> Robot.slider.setSliderServoToLeftPosition(), Robot.slider);
    }
    public static Command sliderFullRightCmd() {
        return new RunCommand(() -> Robot.slider.setSliderServoToRightPosition(), Robot.slider);
    }
    public static Command sliderCtrCmd() {
        return new RunCommand(() -> Robot.slider.setSliderServoToCtrPosition(), Robot.slider);
    }

    // ---------- Slider by joystick ------------
    public static Command sliderLeftCmd() {
        return new RunCommand(() -> Robot.slider.sliderLeft(), Robot.slider);
    }
    public static Command sliderRightCmd() {
        return new RunCommand(() -> Robot.slider.sliderRight(), Robot.slider);
    }

    // ---------- Slider to a given position ------------
    public static Command sliderToPositionCmd(double pos) {
        return new RunCommand(() -> Robot.slider.setSliderServoToPosition(pos), Robot.slider);
    }

    // ----------- Disable Servo -----------------
    public static Command sliderStopCmd() {
        return new RunCommand(() -> Robot.slider.stop(), Robot.grabber);
    }
}

