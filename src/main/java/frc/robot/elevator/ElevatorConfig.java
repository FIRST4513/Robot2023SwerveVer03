package frc.robot.elevator;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;

import edu.wpi.first.math.util.Units;

/** Add your docs here. */
public class ElevatorConfig {
    // Declare and Initialize needed class objects

    public final boolean lowerLimitTrue = true;
    public final boolean upperLimitTrue = true;

    // Reduce jitter
    public static final int elevAllowableError = 10; // Encoder counts close enough
    
    // Talon SRX Configuration    
    public static TalonSRXConfiguration elevSRXConfig;       // TalonSRX Config object
    
    // ------ PID Constants ------
    public final double elevKP = 0.4;      // orig example 5.0;
    public final double elevKI = 0.0;
    public final double elevKD = 0.0;
    public final double elevKF = 0.05;


    /* Elev Motor Current Limiting */
    public static final int     elevContinuousCurrentLimit   = 25;       // Amps
    public static final int     elevPeakCurrentLimit         = 40;       // Amps
    public static final int     elevPeakCurrentDuration      = 100;      // Time in milliseconds
    public static final boolean elevEnableCurrentLimit = true;

    /* Neutral Modes */
    public static final NeutralMode elevNeutralMode = NeutralMode.Coast;

    /* Inverts */
    public static final boolean elevMotorInvert = false;

    public final double kMaxPwr = 1.00;

    //public final double kElevatorGearing = 10.0;
    public final double kElevatorDrumRadius = Units.inchesToMeters(2.0);

    public final double kElevatorEncoderDistPerPulse = 2.0 * Math.PI * kElevatorDrumRadius / 4096;

    // ------ Orig Elevator Constants -----
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


    // --------------- Constuctor Setting Up Motor Config values -------------
    public ElevatorConfig() {
        /* Arm Motor Configurations */
        elevSRXConfig = new TalonSRXConfiguration();

        elevSRXConfig.slot0.kP = elevKP;
        elevSRXConfig.slot0.kI = elevKI;
        elevSRXConfig.slot0.kD = elevKD;
        elevSRXConfig.slot0.kF = elevKF;
        elevSRXConfig.slot0.allowableClosedloopError = elevAllowableError;
        elevSRXConfig.continuousCurrentLimit         = elevContinuousCurrentLimit;
        elevSRXConfig.peakCurrentLimit               = elevPeakCurrentLimit;         
        elevSRXConfig.peakCurrentDuration            = elevPeakCurrentDuration;
    }

}
