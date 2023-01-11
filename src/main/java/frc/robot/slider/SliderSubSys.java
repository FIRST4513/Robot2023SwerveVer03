package frc.robot.slider;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SliderSubSys extends SubsystemBase {
    public SliderConfig config;

    // Standard classes for controlling the slider
    public final Servo m_motor;
    public double sliderServoValue = config.kSliderServoCtr;
    private boolean enabled;

    public SliderSubSys() {
        config = new SliderConfig();
        m_motor = new Servo(config.kMotorPort);
        enabled = true;
    }

    // --------------------------------------------
    // ---------    Slider Methods    -------------
    // --------------------------------------------

    public void setSliderServoToLeftPosition()      { setSliderServoToPosition(config.kSliderServoLeft); }    
    public void setSliderServoToCtrPosition()       { setSliderServoToPosition(config.kSliderServoCtr); }
    public void setSliderServoToRightPosition()     { setSliderServoToPosition(config.kSliderServoRight); }
    public double getSliderServoPosition()          { return sliderServoValue; } 

    public void sliderLeft(){
        sliderServoValue = sliderServoValue + config.kSliderLeftRate;
        setSliderServoToPosition(sliderServoValue);
    }

    public void sliderRight(){
        sliderServoValue = sliderServoValue + config.kSliderRightRate;
        setSliderServoToPosition(sliderServoValue);
    }

    public void setSliderServoToPosition(double value){
        // Valid range 0.0 to 1.0 Extreme Left/Right
        enabled = true;
        if (value > config.kSliderServoMax) {value = config.kSliderServoMax;}
        if (value < config.kSliderServoMin) {value = config.kSliderServoMin;}
        sliderServoValue = value;
        //m_motor.set(sliderServoValue);
    }

    public void stop(){
        enabled = false;
    }

    @Override
    public void periodic() {
        // Update servos to desired position
        if (enabled) {
            m_motor.setPosition(sliderServoValue);
        }
    }
}