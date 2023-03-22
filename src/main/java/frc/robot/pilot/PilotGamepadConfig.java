package frc.robot.pilot;

import edu.wpi.first.math.geometry.Translation2d;

/** Constants used by the Pilot Gamepad */
public class PilotGamepadConfig {
    public static final int port = 0;

    public static enum MaxSpeeds {FAST, MEDFAST, MEDSLOW, SLOW}

    // Selectable speeds
    //----Fast-----//
    public static final double FastfowardVelocity       = -4.8;
    public static final double FastsidewaysVelocity     = -4.8;
    public static final double FastrotationVelocity     = 0.25;
    public static final double FastForwardExp           = 30;
    public static final double FastSidewaysExp          = 30;
    public static final double FastRotationExp          = 20;

    //---Medium Fast---///
    public static final double MedFastforwardVelocity   = -4.9;
    public static final double MedFastsidewaysVelocity  = -4.9;
    public static final double MedFastrotationVelocity  = 0.25;
    public static final double MedFastForwardExp        = 30;
    public static final double MedFastSidewaysExp       = 30;
    public static final double MedFastRotationExp       = 20;

    //---Medium Slow--//
    public static final double MedSlowforwardVelocity   = -1.5;
    public static final double MedSlowsidewaysVelocity  = -1.5;
    public static final double MedSlowrotationVelocity  = 0.25;
    public static final double MedSlowForwardExp        = 30;
    public static final double MedSlowSidewaysExp       = 30;
    public static final double MedSlowRotationExp       = 20;

    //---Slow--//
    public static final double SlowforwardVelocity      = -0.75;
    public static final double SlowsidewaysVelocity     = -0.75;
    public static final double SlowrotationVelocity     = 0.25;
    public static final double SlowForwardExp           = 30;
    public static final double SlowSidewaysExp          = 30;
    public static final double SlowRotationExp          = 20;

    // forward speed//
    public static final double forwardSpeedExp =       35.0;
    public static final double forwardSpeedScaler =    -1.5;
    public static final double forwardSpeedDeadband =  0.15;
    public static final double forwardSpeedOffset =    0.0;
    // sideways speed
    public static final double sidewaysSpeedExp =       forwardSpeedExp;
    public static final double sidewaysSpeedScaler =    -1.5; //-4.8;
    public static final double sidewaysSpeedDeadband =  forwardSpeedDeadband;
    public static final double sidewaysSpeedOffset =    0.0;
    // rotation speed
    public static final double rotationSpeedExp =       forwardSpeedExp;
    public static final double rotationSpeedScaler =    -(Math.PI);  // -pi to +pi? for radians
    public static final double rotationSpeedDeadband =  forwardSpeedDeadband;
    public static final double rotationSpeedOffset =    0.0;

    public static final Translation2d intakeCoRmeters = new Translation2d(0, 0);
}
