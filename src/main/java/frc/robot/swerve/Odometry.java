package frc.robot.swerve;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;

public class Odometry {

    public SwerveDriveOdometry swerveOdometry;
    private Swerve swerve;

    // ----------------- Constructor --------------
    public Odometry(Swerve s) {
        swerve = s;
        swerveOdometry = new SwerveDriveOdometry(
                                SwerveConfig.swerveKinematics,
                                swerve.gyro.getGyroYawRotation2d(),
                                swerve.getPositions());
    }

    // --------------------- Update Odometry ----------------
    public void update() {
        // Called in swerv priodic - every 20ms
        swerveOdometry.update(swerve.gyro.getGyroYawRotation2d(), swerve.getPositions());
    }

    // --------------------- Reset/Init Odometry ----------------
    public void resetOdometry(Pose2d pose) { 
        // Resets Odometry Pose to current Encodernand Gyro Angle
        // swerveOdometry.resetPosition(swerve.gyro.getGyroHeading(), swerve.getPositions(), pose);
        swerveOdometry.resetPosition(  swerve.gyro.getGyroYawRotation2d(),     swerve.getPositions(), pose);
    }

    public void resetHeading(Rotation2d newHeading) {
        resetOdometry(new Pose2d(getTranslationMeters(), newHeading));
    }

    // ------------- Odometry Getters -----------------
    public SwerveDriveOdometry getSwerveDriveOdometry() {
        return swerveOdometry;
    }

    public Pose2d getPoseMeters() {
        return swerveOdometry.getPoseMeters();
    }

    public Translation2d getTranslationMeters() {
        return swerveOdometry.getPoseMeters().getTranslation();
    }

    public Rotation2d getHeading() {
        return getPoseMeters().getRotation();
    }

}
