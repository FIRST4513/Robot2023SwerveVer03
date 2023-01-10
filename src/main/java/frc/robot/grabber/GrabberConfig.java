package frc.robot.grabber;

import frc.robot.Robot;

public class GrabberConfig {

    public final int kMotorPort = Robot.config.motors.grabberMotor;
    public final double kGrabberMin = 0.3;
    public final double kGrabberMax = 0.8;

    public final double kGrabberOpenPosition = 0.3;
    public final double kGrabberCenterPosition = 0.55;  // Fully OPen
    public final double kGrabberClosedPosition = 0.8;   // Fully Closed

    public final double kGrabberOpenRate = -0.01;

    public final double kGrabberCloseRate = +0.01;
}
