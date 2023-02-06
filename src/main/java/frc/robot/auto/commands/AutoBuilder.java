package frc.robot.auto.commands;

import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;
import frc.robot.Robot;
import frc.robot.auto.AutoSetup;
import frc.robot.auto.AutoConfig;
import frc.robot.swerveDrive.SwerveDriveConfig;

public
class AutoBuilder { // Create the AutoBuilder. This only needs to be created once when robot code
    // starts, not every
    // time you want to create an auto command. A good place to put this is in RobotContainer along
    // with your subsystems.
    public static final SwerveAutoBuilder autoBuilder =
            new SwerveAutoBuilder(
                    Robot.swerve.odometry::getPoseMeters, // Pose2d supplier
                    Robot.swerve.odometry
                            ::resetOdometry, // Pose2d consumer, used to reset odometry at the
                    // beginning of auto
                    SwerveDriveConfig.swerveKinematics, // SwerveDriveKinematics
                    new PIDConstants(
                            AutoConfig.kPTranslationController,
                            AutoConfig.kITranslationController,
                            AutoConfig.kDTranslationController), // PID constants to correct for
                    // translation error (used to create
                    // the X and Y PID controllers)
                    new PIDConstants(
                            AutoConfig.kPRotationController,
                            AutoConfig.kIRotationController,
                            AutoConfig
                                    .kDRotationController), // PID constants to correct for rotation
                    // error (used to create the
                    // rotation controller)
                    Robot.swerve
                            ::setModuleStates, // Module states consumer used to output to the drive
                    // subsystem
                    AutoSetup.eventMap,
                    true, // Should the path be automatically mirrored depending on
                    // alliance color
                    // Alliance.
                    Robot.swerve // The drive subsystem. Used to properly set the requirements of
                    // path following commands
                    );
}
