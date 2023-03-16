package frc.robot.arm;

public class ArmConfig {

    // arm plantery order
    // motor -> 5:1 -> encoder -> 7:1 -> 7:1 -> 2:1 -> arm

    // ------- Encoder Conversion Factor --------------
    // 1797.41176 cnts per degree tested 2/20/23
    public final static double kCntsPerDeg    = 809;              // How many counts it take for the physical arm to rotate 1º
    public final static double kDegsPerCnt    = 1 / kCntsPerDeg;  // How many degrees a single count represents
    public final static double KAngleDeadBand = +3.0;             // Allowable error when evaulating "are we there yet??"

    // -------- Absolute Analog Angle Sensor ------------
    // 3.3 volts / 360 = 0.0091666     volts per degree

    // public final static double kAnalogVoltsToDegree      = -0.0091666;  //Invert Direction 
    // public final static double kabsoluteAngleOffset      = -20.0;       // Degrees encoder is offset

    // ------ Speed Constants ------
    // public final static double kDefaultExtendPwr   = +0.25;
    public final static double kExtendMaxPwr          = +0.75;

    // public final static double kDefaultRetractPwr  = -0.4;
    public final static double kRetractMaxPwr         = -0.75;

    // Arm Position Constants, eject positions work for either cone or cube
    public final static double ArmAngleStowPos      = -25.0;
    public final static double ArmAngleIntakePos    =   1.0;  // 18.5;
    public final static double ArmAngleEjectLowPos  =   0.0;  // stow mainly used for low eject though
    public final static double ArmAngleEjectMidPos  =  33.0;
    public final static double ArmAngleEjectHighPos =  70.0;

    // ------ Limit Switches ------
    public final static boolean RetractLimitSwitchTrue       = false;
    public final static double  RetractLimitSwitchAngle      = -25;     // Max angle retract
    public final static boolean RetractSoftLimitSwitchEnable = false;
    public final static int     RetractSoftLimitSwitchAngle  = -25;     // Max angle retract

    public final static boolean ExtendLimitSwitchTrue       = false;
    public final static double  ExtendLimitSwitchAngle      = +84.0;   // Max angle extended
    public final static boolean ExtendSoftLimitSwitchEnable = false;
    public final static int     ExtendSoftLimitSwitchAngle  = +84;     // Max angle extended
}

// ground intake: elev 0 / arm 0 or 5
// human player is stowed
// low: stowed or ?
// mid and high optimize for cone and see if it works for cube
// mid: elev full arm 24
// high: elev full arm 70

// score order: bumper up, elev 28, arm forward