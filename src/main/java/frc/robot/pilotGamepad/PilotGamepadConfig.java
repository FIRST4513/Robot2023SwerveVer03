package frc.robot.pilotGamepad;

import edu.wpi.first.math.geometry.Translation2d;

/** Constants used by the Pilot Gamepad */
public class PilotGamepadConfig {
    public static final int port = 0;


    // forwardSpeed, sidewaysSpeed, rotation
    public static final double forwardSpeedExp = 1.2;
    public static final double forwardSpeedScaler = -1.0;
    public static final double forwardSpeedDeadband = 0.15;
    public static final double sidewaysSpeedExp = forwardSpeedExp;
    public static final double sidewaysSpeedScaler = -1.0;
    public static final double sidewaysSpeedDeadband = forwardSpeedDeadband;
    public static final double rotationSpeedExp = forwardSpeedExp;
    public static final double rotationSpeedScaler = -1.0;
    public static final double rotationSpeedDeadband = forwardSpeedDeadband;

    public static final Translation2d intakeCoRmeters = new Translation2d(0, 0);
}
