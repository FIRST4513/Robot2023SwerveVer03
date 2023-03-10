package frc.robot.elevator;

public class ElevatorConfig {

    // ------- Conversion Factor --------
    // Measured 2/18/23 25.375 inches = 84,139 sensor units
    public final static double kCntsPerInch         = 3315.822;     // Convert cnt to inches of height
    public final static double kInchesPerCnt        = 0.000301584;  // Convert inches of height to cnt
    public final static double KheightDeadBand      = +0.50;        // How close to target is close enough (inches)      

    // ------ Position Constatnts ------
    public final static double KElevMaxTopHt        = 31.0;         // This is the top GO NO FURTHER!
    public final static double KLimitElevTopSlowHt  = 28.0;		    // Start slowing the raise at this position   
    public final static double KLimitElevBottomSlowHt = 2.0;        // Start slowing the lower at this position

    public final static double ElevIntakeHt     = 0.0;          // Position to intake Cone
    // public final static double ElevIntakeConeHt     = 0.0;          // Position to intake Cone
    // public final static double ElevIntakeCubeHt     = 0.0;          // Position to intake Cube

    public final static double ElevBumperClearHt    = 13;         // Position to raise to clear bumper with intake
    public final static double ElevStoreHt          = 0.0;          // Position to Store Cube/Cone retracted for motion
    // public final static double ElevRetractHt        = 0.0;          // Position to fully retract
    public final static double ElevEjectLowHt       = 10;          // Position to Score a Cube/Cone in LOW Position  
    public final static double ElevEjectCubeLowSafeHt = 26.5;          // Position to Score a Cube/Cone in LOW Position 
    public final static double ElevEjectCubeMidSafeHt = KElevMaxTopHt;          // Position to Score a Cube/Cone in LOW Position 
    public final static double ElevEjectCubeHighSafeHt = KElevMaxTopHt;          // Position to Score a Cube/Cone in LOW Position 
    
    public final static double ElevEjectMidHt       = 27;     // Position to Score a Cube/Cone in MID Position
    public final static double ElevEjectHighHt      = 28.0;         // Position to Score a Cube/Cone in Hi Position
    public final static double ElevArmReleaseHt     = ElevBumperClearHt;         // Position needed to release arm on startup

    // ------ Elevator Speed Constants -----
    public final static double zeroPwr              = -0.10;
    public final static double kMaxPwr              = +1.0;
    public final static double lowerMaxPwr          = -0.20;
    public final static double raiseMaxPwr          = +0.60;

    // These are outside of Motion Magic control
    public final static double KRaiseSpeedDefault   = +0.45;    
    public final static double KRaiseSlowSpeed      = +0.15;
    public final static double KHoldSpeedDefault    = +0.08;
    
    public final static double KLowerSlowSpeed      = -0.03;
    public final static double KLowerSpeedDefault   = -0.20;
    
    // ------ Limit Switches ------------
    public final static boolean LowerLimitSwitchTrue        = false;
    public final static boolean LowerSoftLimitSwitchEnable  = false;
    public final static double  LowerSoftLimitSwitchHt      = 0;        // Min Height Lowered

    public final static boolean UpperLimitSwitchTrue        = false;
    public final static boolean UpperSoftLimitSwitchEnable  = false;
    public final static double  UpperSoftLimitSwitchHt      = 30;        // Min Height Lowered
    
}

// ground intake: elev 0 / arm 0 or 5
// human player is stowed
// low: stowed or ?
// mid and high optimize for cone and see if it works for cube
// mid: elev 27 arm 40
// high: elev 28 arm full

// bumper up, elev 28, arm forward