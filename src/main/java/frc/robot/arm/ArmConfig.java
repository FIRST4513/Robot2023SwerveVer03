package frc.robot.arm;

public class ArmConfig {

    // arm plantery order
    // motor -> 5:1 -> encoder -> 7:1 -> 7:1 -> 2:1 -> arm

    // ------- Encoder Conversion Factor --------------
    // 1797.41176 cnts per degree tested 2/20/23
    public final static double kCntsPerDeg              = 809; //569;  // 1843;        // Convert cnt to degrees of angle (2/21/23)
    public final static double kDegsPerCnt              = 1 / kCntsPerDeg;   // Convert cnt to degrees of angle (2/21/23)
    public final static double KAngleDeadBand           = +3.0;        // Degrees close enough to Tgt

    // -------- Absolute Analog Angle Sensor ------------
    // 3.3 volts / 360 = 0.0091666     volts per degree

    public final static double kAnalogVoltsToDegree      = -0.0091666;  //Invert Direction 
    public final static double kabsoluteAngleOffset      = -20.0;       // Degrees encoder is offset

    // ------ Speed Constants ------
    public final static double kDefaultExtendPwr        = +0.25;
    public final static double kExtendMaxPwr            = +0.75;

    public final static double kDefaultRetractPwr       = -0.4;
    public final static double kRetractMaxPwr           = -0.75;

    // Arm Position Constants for single value for Cone or Cube
    public final static double ArmAngleIntakeCubePos    = -10.0;        // Pickup Cube position
    public final static double ArmAngleIntakeConePos    = -10.0;        // Pickup Cone position
    public final static double ArmAngleEjectLowPos      =   0.0;        // Eject Low position
    public final static double ArmAngleEjectMidPos      = +25.0;        // Eject Mid position
    public final static double ArmAngleEjectHighPos     = +46.0;        // Eject High position

    public final static double ArmAngleStorePos         = -45.0;        // Store for Travel
    public final static double ArmAngleFullRetractPos   = -89.0;        // Full retract

    // ------ Limit Switches ------
    public final static boolean RetractLimitSwitchTrue      = false;
    public final static double  RetractLimitSwitchAngle     = -25;     // Max angle retract
    public final static boolean RetractSoftLimitSwitchEnable = false;
    public final static int     RetractSoftLimitSwitchAngle = -25;     // Max angle retract

    public final static boolean ExtendLimitSwitchTrue       = false;
    public final static double  ExtendLimitSwitchAngle      = +84.0;   // Max angle extended
    public final static boolean ExtendSoftLimitSwitchEnable = false;
    public final static int     ExtendSoftLimitSwitchAngle  = +84;     // Max angle extended
}

// ground intake: elev 0 / arm 0 or 5
// human player is stowed
// low: stowed or ?
// mid and high optimize for cone and see if it works for cube
// mid: elev 27 arm 40
// high: elev 28 arm full

// bumper up, elev 28, arm forward