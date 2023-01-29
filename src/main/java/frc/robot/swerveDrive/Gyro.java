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

    public void setHeadingDegrees( double hdg) {
        gyro.reset();
        gyroOffset = hdg;
    }

    public double getHeadingDegrees(){
        // Get Gyro angle from 0 to 360 degrees CCW
        double gyroAngle = (-getAngle()) % 360.0;
        if ( gyroAngle < 0.0 ) { gyroAngle += 360.0; }
        // Now add any Heading Offset
        double angle = gyroAngle + gyroOffset;
        if ( angle < 0.0 )   { angle += 360.0; }
        if ( angle > 360.0 ) { angle -= 360.0; }
        return Rmath.mRound(angle, 2);
    }
   
    public Rotation2d getHeading() {
        // this will return a Rotation2d object of the yaw value 0 to +360 degree
        return Rotation2d.fromDegrees(getHeadingDegrees());
    }

    public double getAngle() {
        // This returns a continous rotation angle - x times +360 through x times -360 
        return (gyro.getAngle()); 
    }

    public double getGyroOffset(){
        return gyroOffset;
    }

}
