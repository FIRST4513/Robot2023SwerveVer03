package frc.robot.auto;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.swerve.SwerveConfig;

/** Add your docs here. */
public class AutoConfig {

    // Auto menu selectors
    public static final String kLeftSelect          = "Left";
    public static final String kRightSelect         = "Right";
    public static final String kCenterLeftSelect    = "Center Left";
    public static final String kCenterRightSelect   = "Center Right";
    public static final String kCenterSelect        = "Center";
    public static final String kYesSelect           = "Yes";
    public static final String kNoSelect            = "No";
    public static final String kLowSelect           = "Low";
    public static final String kMidSelect           = "Mid";
    public static final String kHighSelect          = "High";
    public static final String kAlianceAutoSelect   = "Auto";
    public static final String kAlianceRedSelect   = "Red";
    public static final String kAlianceBlueSelect   = "Blue";



    // Auto Start Position Poses
    public static final Rotation2d kLeftYaw    = Rotation2d.fromDegrees(-180.0);
    public static final Rotation2d kRightYaw   = Rotation2d.fromDegrees(-180.0);
    public static final Rotation2d kCenterYaw  = Rotation2d.fromDegrees(-180.0);

    public static final Pose2d kLeftPose       = new Pose2d (new Translation2d( 2.0, 3.0), kLeftYaw);
    public static final Pose2d kRightPose      = new Pose2d (new Translation2d( 2.0, 3.0), kRightYaw);
    public static final Pose2d kCenterPose     = new Pose2d (new Translation2d( 2.0, 3.0), kCenterYaw);

    // Elevator positions
    public static final double kElevTop  = 22.0;

    // Max Speeds for Auto commands
    public static final double kMaxSpeed = 2.7;
    public static final double kMaxAccel = 2.4; // 2 worked but took too long
    public static final double kMaxAngularSpeedRadiansPerSecond = SwerveConfig.maxAngularVelocity;
    public static final double kMaxAngularSpeedRadiansPerSecondSquared =
                                            SwerveConfig.maxAngularVelocity;

    // PID Values for 2023
    public static final double kPTranslationController = 0.6;
    public static final double kITranslationController = 0;
    public static final double kDTranslationController = 0;
    public static final double kPRotationController = 5;
    public static final double kIRotationController = 0;
    public static final double kDRotationController = 0.01;

    // Constraint for the motion profilied robot angle controller
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints =
            new TrapezoidProfile.Constraints(
                    kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);

    public static enum AutoPosition {
        ORIGIN;

        public Pose2d getPose() {
            switch (this) {
                case ORIGIN:
                    return new Pose2d();
                default:
                    return new Pose2d();
            }
        }
    }
}
