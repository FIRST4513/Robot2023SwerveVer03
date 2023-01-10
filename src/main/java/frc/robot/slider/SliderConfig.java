package frc.robot.slider;

import frc.robot.Robot;

public class SliderConfig {
    
    public final int kMotorPort = Robot.config.motors.sliderMotor;
    // Slider Servo Variables
    public double kSliderServoValue = 0;
    public double kSliderLeftRate = -0.005;
    public double kSliderRightRate = 0.005;

    public final double kSliderServoMin = 0.0;      // Fully Left
    public final double kSliderServoMax = 1.0;      // Fully Right

    public final double kSliderServoLeft = 0.0;
    public final double kSliderServoCtr = 0.5;      // Centered
    public final double kSliderServoRight = 1.0;
}
