package frc.robot.elevator;

import edu.wpi.first.math.util.Units;
import frc.robot.Robot;

/** Add your docs here. */
public class ElevatorConfig {
    // Declare and Initialize needed class objects

    // ------ Device Port Constants ------
    public final int kMotorPort = Robot.config.motors.elevatorMotor;
    public final int kEncoderAChannel = Robot.config.encoders.elevMotorEncoderA;
    public final int kEncoderBChannel = Robot.config.encoders.elevMotorEncoderB;
    public final int kLowerLimitSwitchPort = Robot.config.limitSwitches.elevatorLowerLimitSw;
    
    // ------ PID Constants ------
    public final double kElevatorKp = 0.4;      // orig example 5.0;
    public final double kElevatorKf = 0.05;
    public final double kMaxPwr = 1.00;

    //public final double kElevatorGearing = 10.0;
    public final double kElevatorDrumRadius = Units.inchesToMeters(2.0);

    public final double kElevatorEncoderDistPerPulse = 2.0 * Math.PI * kElevatorDrumRadius / 4096;

    // ------ Orig Elevator Constants -----
    public final double KRaiseSpeedDefault = +1.0;
    public final double KRaiseSlowSpeed =    +0.45;
    public final double KHoldSpeedDefault =  +0.15;
    public final double KLowerSlowSpeed =    -0.20;
    public final double KLowerSpeedDefault = -0.75;

    public final double KheightDeadBand =    +0.15;

    public final double KElevMaxTopPos =             38.0;      // This is the top GO NO FURTHER!
    public final double KLimitElevTopSlowPos =       36.5;		// Start slowing the raise at this position   
    public final double KLimitElevBottomSlowPos =    1.5;       // Start slowing the lower at this position
    
    public final double ELEV_INCH_ABOVE_GROUND =     2.875;     // Inches claw is above ground when fully lowered

    public final double ELEV_ENCODER_CONV = 0.03461;            // Inches the elevator rises for each encoder count

    public final boolean KLIMIT_SWITCH_PRESSED = false;         // The state of the sensor at the bottom
}
