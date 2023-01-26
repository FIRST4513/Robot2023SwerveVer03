package frc.robot.pilot;

import edu.wpi.first.math.geometry.Translation2d;

/** Constants used by the Pilot Gamepad */
public class PilotGamepadConfig {
    public static final int port = 0;

    // forward speed
    public static final double forwardSpeedExp = 35;
    public static final double forwardSpeedScaler = -4.8;  // max velocity? // ~4.9 robot max
    public static final double forwardSpeedDeadband = 0.15;
    public static final double forwardSpeedOffset = 0;
    // sideways speed
    public static final double sidewaysSpeedExp = forwardSpeedExp;
    public static final double sidewaysSpeedScaler = -4.8;
    public static final double sidewaysSpeedDeadband = forwardSpeedDeadband;
    public static final double sidewaysSpeedOffset = 0;
    // rotation speed
    public static final double rotationSpeedExp = forwardSpeedExp;
    public static final double rotationSpeedScaler = -(Math.PI);  // -pi to +pi? for radians
    public static final double rotationSpeedDeadband = forwardSpeedDeadband;
    public static final double rotationSpeedOffset = 0;

    public static final Translation2d intakeCoRmeters = new Translation2d(0, 0);
}
