package frc.robot.elevator;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.math.util.Units;

public class ElevatorConfig {
    // Declare and Initialize needed class objects

    public final boolean lowerLimitTrue = true;
    public final boolean upperLimitTrue = true;

    public final double kMaxPwr = 1.00;

    //public final double kElevatorGearing = 10.0;
    public final double kElevatorDrumRadius = Units.inchesToMeters(2.0);

    public final double kElevatorEncoderDistPerPulse = 2.0 * Math.PI * kElevatorDrumRadius / 4096;

    // ------ Orig Elevator Constants -----
    public final static double zeroSpeed = -0.1;
    
    public final double KRaiseSpeedDefault = +0.80;
    public final double KRaiseSlowSpeed =    +0.45;
    public final double KHoldSpeedDefault =  +0.15;
    public final double KLowerSlowSpeed =    -0.20;
    public final double KLowerSpeedDefault = -0.65;

    public final double lowerMaxSpeed  = -0.70;
    public final double raiseMaxSpeed  = +1.00;

    // ------ Max Speeds (testing/controllability) ------
    //public final double KMaxSpeed = 0.6;

    public final double KheightDeadBand =    +0.15;

    public final static double ElevClearPos = 10.0;
    public final static double ElevStorePos = 7.0;
    public final static double ElevMidPos = 20.0;
    public final static double ElevHighPos = 35.0;

    public final double KElevMaxTopPos =             38.0;      // This is the top GO NO FURTHER!
    public final double KLimitElevTopSlowPos =       35.5;		// Start slowing the raise at this position   
    public final double KLimitElevBottomSlowPos =    3.0;       // Start slowing the lower at this position

    public final double ELEV_INCH_ABOVE_GROUND =     2.875;     // Inches claw is above ground when fully lowered

    public final double ELEV_ENCODER_CONV = 0.03461;            // Inches the elevator rises for each encoder count

    public final boolean KLIMIT_SWITCH_PRESSED = false;         // The state of the sensor at the bottom


    
    // All these are made up and need to be changed
    public final static double cubeIntake = 5000;
    public final static double cubeMid = 60000;
    public final static double cubeTop = 100000;

    public final static double coneIntake = 0;
    public final static double coneStandingIntake = 0;
    public final static double coneShelf = 130000;

    public final static double coneMid = 130000;
    public final static double coneTop = 150000;

    public final static double diameterInches = 2.0; // changed from int, 4
    public final static double gearRatio = 62 / 8;
    public final static double maxUpFalconPos = 162116;

    public final static double safePositionForFourBar = 0; // TODO: find safe position for four bar
    public final static double startingHeight = 0; // TODO: find starting height
    public final static double startingHorizontalExtension = 0; // TODO: find starting horizontal extension
    public final static double maxExtension = 80000; // TODO: find max relative extension
    public final static double angle = 60;

}
