package frc.robot.slider;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Robot;

public class SliderTelemetry {
    // ---------------------//
    // NetworkTableEntries //

    // ----------------//
    // Tab & Layouts //
    private static ShuffleboardTab m_tab;

    // ---------//
    // Widgets //

    
    // --------------//
    // Constructor //
    public SliderTelemetry() {
        m_tab = Shuffleboard.getTab("Slider");
    }
    //---------------------//
    // initializeViewable  //
    // Create all View Widgets, ones you can't edit, created after subsystem instances are made
    public void initialize() {
        m_tab.addNumber("Slider Servo Position", 
                        ()-> Robot.slider.getSliderServoPosition()).withPosition(0,0);
    }

    // --------//
    // Update //
    public void update() { // This will be called in the robotPeriodic
        //ClimberLeader.update();
    }
}
