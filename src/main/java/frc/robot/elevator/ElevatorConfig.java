package frc.robot.elevator;

public class ElevatorConfig {

    // ------- Conversion Factor --------
    // Measured 2/18/23 25.375 inches = 84,139 sensor units
    public final static double kCntsPerInch         = 3315.822;     // Convert cnt to inches of height
    public final static double kInchesPerCnt        = 0.000301584;  // Convert inches of height to cnt
    public final static double KheightDeadBand      = +0.10;        // How close to target is close enough (inches)      

    // ------ Position Constatnts ------
    public final static double KElevMaxTopHt        = 31.0;         // This is the top GO NO FURTHER!
    public final static double KLimitElevTopSlowHt  = 28.0;		    // Start slowing the raise at this position   
    public final static double KLimitElevBottomSlowHt = 3.0;        // Start slowing the lower at this position

    public final static double ElevIntakeConeHt     = 0.0;          // Position to intake Cone
    public final static double ElevIntakeCubeHt     = 0.0;          // Position to intake Cube

    public final static double ElevBumperClearHt    = 10.0;         // Position to raise to clear bumper with cube
    public final static double ElevStoreHt          = 0.0;          // Position to Store Cube/Cone retracted for motion
    public final static double ElevRetractHt        = 0.0;          // Position to fully retract
    public final static double ElevEjectLowHt       = 0.0;          // Position to Score a Cube/Cone in LOW Position    
    public final static double ElevEjectMidHt       = 20.0;         // Position to Score a Cube/Cone in MID Position
    public final static double ElevEjectHighHt      = 30.0;         // Position to Score a Cube/Cone in Hi Position
    public final static double ElevArmReleaseHt     = 24.0;         // Position needed to release arm on startup

    // ------ Elevator Speed Constants -----
    public final static double zeroPwr              = -0.10;
    public final static double kMaxPwr              = +0.25;
    public final static double lowerMaxPwr          = -0.15;
    public final static double raiseMaxPwr          = +0.30;

    public final static double KRaiseSpeedDefault   = +0.25;
    public final static double KRaiseSlowSpeed      = +0.15;
    public final static double KHoldSpeedDefault    = +0.08;
    public final static double KLowerSlowSpeed      = -0.05;
    public final static double KLowerSpeedDefault   = -0.15;
    
    // ------ Limit Switches ------------
    public final static boolean LowerLimitSwitchTrue        = false;
    public final static boolean LowerSoftLimitSwitchEnable  = false;
    public final static double  LowerSoftLimitSwitchHt      = 0;        // Min Height Lowered

    public final static boolean UpperLimitSwitchTrue        = false;
    public final static boolean UpperSoftLimitSwitchEnable  = true;
    public final static double  UpperSoftLimitSwitchHt      = 30;        // Min Height Lowered
    
}
