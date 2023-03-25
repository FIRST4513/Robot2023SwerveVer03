package frc.robot.elevator;

public class ElevatorConfig {

    // ------- Conversion Factor --------
    // Measured 2/18/23 25.375 inches = 84,139 sensor units
    public final static double kCntsPerInch           = 3315.822;     // Convert cnt to inches of height
    public final static double kInchesPerCnt          = 0.000301584;  // Convert inches of height to cnt
    public final static double KheightDeadBand        = +0.75;        // How close to target is close enough (inches)      

    // ------ Position Constatnts ------
    public final static double KElevMaxTopHt          = 31.0;          // This is the top GO NO FURTHER!
    public final static double KLimitElevTopSlowHt    = 28.0;		   // Start slowing the raise at this position
    public final static double KLimitElevBottomSlowHt1 = 3.0;           // Start slowing the lower at this position
    public final static double KLimitElevBottomSlowHt2 = 2.0;           // Start slowing the lower at this position
    public final static double KLimitElevBottomSlowHt3 = 1.0;           // Start slowing the lower at this position

    public final static double ElevBumperClearHt      = 13;             // Position to raise to clear bumper with intake
    public final static double ElevIntakeHt           = 1.5;            // Position to intake from ground
    public final static double ElevStowHt             = 1.0;            // Position to Store Cube/Cone retracted for motion and low 
    public final static double ElevEjectLowHt         = ElevStowHt;     // Position to Score a Cube/Cone in LOW Position (same as stow)
    public final static double ElevEjectMidHt         = KElevMaxTopHt;  // Position to Score a Cube/Cone in MID Position
    public final static double ElevEjectHighHt        = KElevMaxTopHt;  // Position to Score a Cube/Cone in HIGH Position

    // ------ Elevator Speed Constants -----
    public final static double zeroPwr              = -0.20;
    public final static double kMaxPwr              = +1.0;
    public final static double lowerMaxPwr          = -0.30;
    public final static double raiseMaxPwr          = +0.60;

    // These are outside of Motion Magic control
    public final static double KRaiseSpeedDefault   = +0.45;    
    public final static double KRaiseSlowSpeed      = +0.15;
    public final static double KHoldSpeedDefault    = +0.08;
    
    public final static double KLowerSlowSpeed1     = -0.175;
    public final static double KLowerSlowSpeed2     = -0.125;
    public final static double KLowerSlowSpeed3     = -0.0625;
    public final static double KLowerSpeedDefault   = -0.25;
    
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

// standing cone
// height 10 -> 5 slow