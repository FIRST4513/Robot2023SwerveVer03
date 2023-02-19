package frc.robot.elevator;

import edu.wpi.first.math.util.Units;

public class ElevatorConfig {
    // Declare and Initialize needed class objects

    public final boolean lowerLimitTrue = false;
    public final boolean upperLimitTrue = false;


    //public final double kElevatorGearing = 10.0;
    public final double kElevatorDrumRadius = Units.inchesToMeters(2.0);

    public final double kElevatorEncoderDistPerPulse = 2.0 * Math.PI * kElevatorDrumRadius / 4096;

    // ------ Orig Elevator Speed Constants -----
    public final double kMaxPwr             = 0.25;

    public final static double zeroSpeed    = +0.01;
    public final double lowerMaxSpeed       = -0.05;
    public final double raiseMaxSpeed       = +0.30;

    public final double KRaiseSpeedDefault  = +0.25;
    public final double KRaiseSlowSpeed     = +0.15;
    public final double KHoldSpeedDefault   = +0.08;
    public final double KLowerSlowSpeed     = +0.03;
    public final double KLowerSpeedDefault  = -0.05;

    // ------ Position Constatnts ------
    public final double KheightDeadBand     = +0.15;             // How close to target is close enough
    public final double ELEV_ENCODER_CONV   = 0.000301584;       // Inches the elevator rises for each encoder count
    // Measured 2/18/23 25.375 inches = 84,139 sensor units

    public final double KElevMaxTopHt       = 25.0;             // This is the top GO NO FURTHER!
    public final double KLimitElevTopSlowHt = 22.0;		        // Start slowing the raise at this position   
    public final double KLimitElevBottomSlowHt = 5.0;           // Start slowing the lower at this position
    
    public final static double ElevClearHt  = 10.0;             // Position to raise to clear bumper with cube
    public final static double ElevStoreHt  = 7.0;              // Position to Store Cube/Cone retracted for motion
    public final static double ElevMidHt    = 20.0;             // Position to Score a Cube/Cone in MID Position
    public final static double ElevHighHt   = 30.0;             // Position to Score a Cube/Cone in Hi Position
    public final static double ElevInitReleaseHt = 24.0;        // Position needed to release arm on startup

    // ---------- PID Constants ------------------
    public final double  kP  = 0.02;
    public final double  kI  = 0.0;
    public final double  kD   = 0.0;
    public final double  kF   = 0.08;       // same as hold speed

}
