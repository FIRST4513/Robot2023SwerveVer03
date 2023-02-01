package frc.robot.pilot;

import edu.wpi.first.math.geometry.Translation2d;

/** Constants used by the Pilot Gamepad */
public class PilotGamepadConfig {
    public static final int port = 0;

    public static enum MaxSpeeds {FAST, MEDFAST, MEDSLOW, SLOW}

    //selectuble speeds
    //----Fast-----//
    public static final double FastfowardVelocity       = -4.8;
    public static final double FastsidewaysVelocity     = -4.8; // -4.8;  // max velocity? // ~4.9 robot max
    public static final double FastrotationVelocity     = 0.25;
    
    //---Medium Fast---///
    public static final double MedFastfowardVelocity    = -2.5;
    public static final double MedFastsidewaysVelocity  = -2.5; // -4.8;  // max velocity? // ~4.9 robot max
    public static final double MedFastrotationVelocity  = 0.25;

    //---Medium Slow--//
    public static final double MedSlowfowardVelocity    = -1.5;
    public static final double MedSlowsidewaysVelocity  = -1.5; // -4.8;  // max velocity? // ~4.9 robot max
    public static final double MedSlowrotationVelocity  = 0.25;

    //---Slow--//
    public static final double SlowfowardVelocity       = -0.75;
    public static final double SlowsidewaysVelocity     = -0.75; // -4.8;  // max velocity? // ~4.9 robot max
    public static final double SlowrotationVelocity     = 0.25;

    // forward speed//
    public static final double forwardSpeedExp = 35;
    public static final double forwardSpeedScaler = -1.5; // -4.8;  // max velocity? // ~4.9 robot max
    public static final double forwardSpeedDeadband = 0.15;
    public static final double forwardSpeedOffset = 0;
    // sideways speed
    public static final double sidewaysSpeedExp = forwardSpeedExp;
    public static final double sidewaysSpeedScaler = -1.5; //-4.8;
    public static final double sidewaysSpeedDeadband = forwardSpeedDeadband;
    public static final double sidewaysSpeedOffset = 0;
    // rotation speed
    public static final double rotationSpeedExp = forwardSpeedExp;
    public static final double rotationSpeedScaler = -(Math.PI);  // -pi to +pi? for radians
    public static final double rotationSpeedDeadband = forwardSpeedDeadband;
    public static final double rotationSpeedOffset = 0;

    public static final Translation2d intakeCoRmeters = new Translation2d(0, 0);
}
