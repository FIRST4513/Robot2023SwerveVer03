package frc.robot.arm;

public class ArmConfig {

    // ------- Encoder Conversion Factor --------------
    // 76815 encoder Cnts = 42.5 degrees (1807.41176 cnts per degree)
    public final static double kEncoderConversion = 1807.41176;      // Convert cnt to degrees of angle

    // ------ Speed Constants ------
    /* Note Arm is asymytrical raise and lower rates need to change base on angle
     * fully retract will take more power than just lowering. These may need to be 
     * proportianal values to be scaled 
     */
    public final static double zeroSpeed    = -0.01;
    public final static double kRaiseSpeed  = +0.25;
    public final static double kLowerSpeed  = -0.15;
    public final static double kHoldSpeed   = +0.01;        // Not Applicable , Hold speed varies by angle
    public final static double kHoldkP      = +0.01;        // proportional with cosine of angle

    public final static double kArmMotorRaiseMaxPwr =  +0.5;
    public final static double kArmMotorLowerMaxPwr = -0.35;

    // ------ Limit Switch True States ------
    public final static boolean lowerLimitSwitchTrue = false;
    public final static boolean upperLimitSwitchTrue = false;
    public final static double lowerLimitPos = -45.0 * kEncoderConversion;      // Encoder Cnts at this angle
    public final static double upperLimitPos = +45 * kEncoderConversion;

    // ------ Arm Angle Constants (In Degrees) ------
    public final static double minArmAngle = -20.0;
    public final static double maxArmAngle = +30.0;

    public final static double ArmInsidePos           =  0.0;  // less than this number = inside robot

    public final static double ArmAngleFullRetractPos = -90.0;
    public final static double ArmAngleStorePos       = -15.0;
    // These would be for single value for Cone or Cube
    public final static double ArmAngleLowPos         = +0.0;  // Pickup position
    public final static double ArmAngleMidPos         = +15.0;  // Pickup position
    public final static double ArmAngleHighPos        = +25.0;  // Pickup position
    // These would be for separate values for Cone or Cube
    public final static double ArmAngleConeLowPos         = +15.0;  // Angle to Eject Cone for Low Score 
    public final static double ArmAngleConeMidPos         = +20.0;
    public final static double ArmAngleConeHighPos        = +30.0;
    public final static double ArmAngleCubeLowPos         = +15.0;  // Angle to Eject Cube for Low Score
    public final static double ArmAngleCubeMidPos         = +20.0;
    public final static double ArmAngleCubeHighPos        = +25.0;

    // Manual PID Constants
    public final static double kP = 0.5;   // not accurate value, just testing
    public final static double kI = 0;     // could be 0
    public final static double kD = 0;     // could be 0
    public final static double kF = 0.3;  

    // ----------------------------------------
    // ----------  Spectrum Example   --------- 
    // Positions set as percentage of fourbar
    public final static int cubeIntake = 95;
    public final static int cubeMid = 60;
    public final static int cubeTop = 100;

    public final static int coneIntake = 100;
    public final static int coneStandingIntake = 90;
    public final static int coneShelf = 0;

    public final static int coneMid = 24; // converted from 1800 angle
    public final static int coneTop = 73; // converted from 54900 angle




}
