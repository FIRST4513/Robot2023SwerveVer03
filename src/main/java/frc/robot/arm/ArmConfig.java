package frc.robot.arm;

public class ArmConfig {

    // ------- Encoder Conversion Factor --------------
    // 1797.41176 cnts per degree tested 2/20/23
    public final static double kCntsPerDeg              = 2532;       // Convert cnt to degrees of angle (2/21/23)
    public final static double kDegsPerCnt              = 0.00039488;   // Convert cnt to degrees of angle (2/21/23)
    public final static double KAngleDeadBand           = +1.0;         // Degrees close enough to Tgt

    // ------ Speed Constants ------
    public final static double kDefaultExtendPwr        = +0.25;
    public final static double kExtendMaxPwr            = +0.5;

    public final static double kDefaultRetractPwr       = -0.4;
    public final static double kRetractMaxPwr           = -0.5;

    // Arm Position Constants for single value for Cone or Cube
    public final static double ArmAngleIntakeCubePos    = +0.0;         // Pickup position
    public final static double ArmAngleIntakeConePos    = +0.0;         // Pickup position
    public final static double ArmAngleEjectLowPos      = +25.0;        // Eject position
    public final static double ArmAngleEjectMidPos      = +40.0;        // Eject position
    public final static double ArmAngleEjectHighPos     = +40.0;        // Eject position
    public final static double ArmAngleStorePos         = -20.0;        // Store for Travel
    public final static double ArmAngleFullRetractPos   = -30.0;        // Full retract

    // These would be for separate values for Cone or Cube (May not be needed)
    public final static double ArmAngleConeLowPos       = +15.0;        // Angle to Eject Cone for Low Score 
    public final static double ArmAngleConeMidPos       = +20.0;
    public final static double ArmAngleConeHighPos      = +30.0;
    public final static double ArmAngleCubeLowPos       = +15.0;        // Angle to Eject Cube for Low Score
    public final static double ArmAngleCubeMidPos       = +20.0;
    public final static double ArmAngleCubeHighPos      = +25.0;

    // ------ Limit Switches ------
    public final static boolean RetractLimitSwitchTrue      = false;
    public final static double  RetractLimitSwitchAngle     = -51.8;    // Max angle retract
    public final static boolean RetractSoftLimitSwitchEnable = false;
    public final static int     RetractSoftLimitSwitchAngle = -42;      // Max angle retract

    public final static boolean ExtendLimitSwitchTrue       = false;
    public final static double  ExtendLimitSwitchAngle      = +47.0;    // Max angle extended
    public final static boolean ExtendSoftLimitSwitchEnable = false;
    public final static int     ExtendSoftLimitSwitchAngle  = +44;      // Max angle extended
}
