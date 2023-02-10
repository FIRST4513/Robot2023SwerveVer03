package frc.robot.swerveDrive;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SPI;
import frc.Rmath;

public class Gyro {
        private final AHRS gyro = new AHRS(SPI.Port.kMXP);
        private double gyroOffset;      // Used to set gyro to different heading

    public Gyro() {
        resetGyro();
    }

    public void resetGyro() {
         gyro.reset();
         gyroOffset = 0;
    }

    public double getGyroHeadingDegrees(){
        // Get Gyro angle from 0 to 360 degrees CCW
        double gyroAngle = (-getGyroAngle()) % 360.0;
        if ( gyroAngle < 0.0 ) { gyroAngle += 360.0; }
        // Now add any Heading Offset
        double angle = gyroAngle + gyroOffset;
        if ( angle < 0.0 )   { angle += 360.0; }
        if ( angle > 360.0 ) { angle -= 360.0; }
        return Rmath.mRound(angle, 2);
    }

    public void setGyroHeadingDegrees( double hdg) {
        gyro.reset();
        gyroOffset = hdg;
    }

    public Rotation2d getGyroYaw() {
        // This returns a Gyro Yaw angle +-180 degrees in Rotation2d format
        return Rotation2d.fromDegrees(getGyroYawAngle()); 
    }  

    public double getGyroYawAngle() {
        // This returns a Gyro Yaw angle +-180 degrees 
        double yaw = getGyroHeadingDegrees();
        if ( yaw > 180.0 ) { yaw -= 360.0; }
        if ( yaw < 180.0 ) { yaw += 360.0; }
        return yaw; 
    }  
    
    public void setGyroYawAngle( double yaw) {
        // This accepte a Yaw angle +-180 degrees
        gyro.reset();
        gyroOffset = yaw;
        if ( gyroOffset < 0.0 ) { gyroOffset += 360.0; }
        return; 
    }
    

    public Rotation2d getGyroHeading() {
        // this will return a Rotation2d object of the yaw value 0 to +360 degree
        return Rotation2d.fromDegrees(getGyroHeadingDegrees());
    }

    public double getGyroAngle() {
        // This returns a continous rotation angle - x times +360 through x times -360 
        return (gyro.getAngle()); 
    }

    public double getGyroOffset(){
        return gyroOffset;
    }

}
