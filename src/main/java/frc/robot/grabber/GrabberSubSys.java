package frc.robot.grabber;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class GrabberSubSys extends SubsystemBase {
    public GrabberConfig config;

    // Standard classes for controlling the grabber
    public final Servo m_motor;
    public double grabberServoValue = config.kGrabberCenterPosition;
    private boolean enabled;

    public GrabberSubSys() {
        config = new GrabberConfig();
        m_motor = new Servo(config.kMotorPort);
        enabled = true;
    }

    // ------------------------------------------------
    // ------------   Grabber Methods  ----------------
    // ------------------------------------------------
    public void setGrabberServoToCtrPosition()     { setGrabberServoToPosition(config.kGrabberCenterPosition); }
    public void setGrabberServoToOpenPosition()    { setGrabberServoToPosition(config.kGrabberOpenPosition); }
    public void setGrabberServoToClosedPosition()  { setGrabberServoToPosition(config.kGrabberClosedPosition); }
    
    public double getGrabberServoPosition()        { return grabberServoValue; } 

    public void grabberOpen(){
        grabberServoValue = grabberServoValue + config.kGrabberOpenRate;
        setGrabberServoToPosition(grabberServoValue);
    }

    public void grabberClose(){
        grabberServoValue += config.kGrabberCloseRate;
        setGrabberServoToPosition(grabberServoValue);   
    }

    public void setGrabberServoToPosition(double value){
        // Valid range 0.0 to 1.0 Extreme Left/Right
        enabled = true;
        if (value > config.kGrabberMax) {value = config.kGrabberMax;}
        if (value < config.kGrabberMin) {value = config.kGrabberMin;}
        grabberServoValue = value;
        m_motor.set(grabberServoValue);
    }

    public void stop() {
        enabled = false;
        m_motor.setDisabled();
    }

    @Override
    public void periodic() {
        // Update servos to desired position
        if (enabled) {
            m_motor.setPosition(grabberServoValue);
        }
    }

}
