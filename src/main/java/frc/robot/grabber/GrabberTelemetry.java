package frc.robot.grabber;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Robot;

public class GrabberTelemetry {
    // ----------------//
    // Tab & Layouts //
    private static ShuffleboardTab m_tab;
    // --------------//
    // Constructor //
    public GrabberTelemetry() {
        m_tab = Shuffleboard.getTab("Grabber");
    }
    //---------------------//
    // initializeViewable  //
    // Create all View Widgets, ones you can't edit, created after subsystem instances are made
    public void initialize() {
        m_tab.addNumber("Grabber Servo Setting", 
                        ()-> Robot.grabber.getGrabberServoPosition()).withPosition(0,0);
    }    

    // --------//
    // Update //
    public void update() { // This will be called in the robotPeriodic
        //GrabberSubSys.update();
    }
}
