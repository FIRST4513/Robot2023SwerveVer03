package frc.robot.arm;

public class ArmConfig {

    // ------ Speed Constants ------
    /* Note Arm is asymytrical raise and lower rates need to change base on angle
     * fully retract will take more power than just lowering.
     */
    public final static double zeroSpeed = -0.01;
    public final static double kRaiseSpeed = 0.025;
    public final static double kLowerSpeed = -0.015;
    public final static double kHoldSpeed = 0.01;

    public final static double kArmMotorRaiseMaxPwr =  +0.5;
    public final static double kArmMotorLowerMaxPwr = -0.35;

    // ------ Limit Switch True States ------
    public final static boolean lowerLimitSwitchTrue = false;
    public final static boolean upperLimitSwitchTrue = false;

    // ------ Arm Pos Constants (In Degrees) ------
    public final static double ArmAngleFullRetractPos = 0.0;
    public final static double ArmAngleStorePos       = 25.0;
    public final static double ArmAngleLowPos         = 90.0;
    public final static double ArmAngleMidPos         = 110.0;
    public final static double ArmAngleHighPos        = 180.0;
    public final static double ArmInsidePos           = 90.0;  // less than this number = inside robot
    
    // ------- Encoder Conversion Factor --------------
    public final static double kEncoderConversion = 0.17578;      // Convert cnt to degrees of angle    //the funny code was here

    // ---------- Arm Angle Limits ------------
    public final static double minArmAngle = 0.0;
    public final static double maxArmAngle = 180.0;

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

    // Physical Constants
    public final static double gearRatio = 1; 

}
