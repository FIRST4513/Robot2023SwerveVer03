package frc.robot.swerve;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SPI;

public class Gyro {
        // Gyro
        // private final AHRS gyro = new AHRS(SPI.Port.kMXP);

    /**
     * Creates a new Gyro, which is a wrapper for the Pigeon IMU and stores an offset so we don't
     * have to directly zero the gyro
     */
    public Gyro() {
        // gyro.reset();
    }

    /**
     * Get the raw yaw of the robot in Rotation2d without using the yawOffset
     *
     * @return the raw yaw of the robot in Rotation2d
     */
    public Rotation2d getRawYaw() {
        // this will return a Rotation2d object of the yaw value -180 to +180 degree ???
        // TODO this may return in +-radians .... need to verify !!!!!!!
        return Rotation2d.fromDegrees(getHeadingDegrees());
    } 


    //public Rotation2d getGyroHeadingRotation2d() {
    //    // this will return a Rotation2d object of the yaw value -180 to +180 degree
    //    return Rotation2d.fromDegrees(getHeadingDegrees());
    //}

    public void zeroHeading() {
        // gyro.reset();
    }

    public double getHeadingDegrees() {
        // This will return values from -180CW to +180CCW degrees of yaw
        // return -Math.IEEEremainder(gyro.getAngle(), 360);
        return 0;
    }



}
