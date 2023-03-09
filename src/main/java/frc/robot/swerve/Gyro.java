package frc.robot.swerve;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SPI;

/* NavX gyro returns angles 0 to +360 (CW) Continous and 0 to -360 (CCW)
 * This wraps over to > +360 or < -360 as it spins more than 1 rotation.
 * This is a reveresed direction from the way WPILib works CW vs CCW Positive  
 *  
 * WPILib likes Yaw +180 CCW to -180 CW
 * 
 * We also Need a way to initialize the gyro to a given heading. The robot may not be
 * placed on the field pointing down court. It will then startup with a 0 heading, while
 * pointing a different direction. This wont work for Field Point of View driving.
 * We must provide an offset that can be initialized and compensate for this.
 * 
 */
public class Gyro {
        private final AHRS gyro = new AHRS(SPI.Port.kMXP);
        private double gyroOffset;      // Used to set gyro to different heading

    // ----------------- Constructor ----------------
    public Gyro() {
        resetGyro();
    }

    // ----------------- Reset Gyro Heading -----------
    public void resetGyro() {
        // reset gyro to 0 (Field Fwd)
         gyro.reset();
         gyroOffset = 0;
    }
    
    public void resetGyro(double yaw) {
        // Set gyro to a given Yaw angle +180 (CCW) to -180 (CW) degrees
        // Since the offset is calculated above in 0 to 360 format
        // this needs to be converted to 0 to +360 format
        gyro.reset();
        gyroOffset = yaw;
    }

    // ------------------------- Get Gyro Yaw ----------------------------
    public double getGyroYawAngle() {
        // This returns a Gyro Yaw angle 0 to +180 CCW and 0 to -180 CW
        double yaw = gyro.getYaw() * -1;
        yaw = yaw + gyroOffset;
        if ( yaw > 180 ) yaw -= 360;
        if ( yaw < -180 ) yaw += 360;
        return yaw; 
    }  

    public Rotation2d getGyroYawRotation2d() {
        // This returns a Gyro Yaw angle in Rotation2d format
        return Rotation2d.fromDegrees(getGyroYawAngle()); 
    }

    // ------------------------- Get Gyro Inclination ----------------------------
    public double getGyroInclineRaw() {
        double incline = gyro.getRawAccelX();
        return incline;
    }

    public double getGyroInclineAngle() {
        double angle = getGyroInclineRaw() / -0.01738;
        return angle;
    }
}
