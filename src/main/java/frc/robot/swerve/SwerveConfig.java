// Based on code from Spectrum3847
package frc.robot.swerve;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.ctre.phoenix.sensors.SensorTimeBase;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;
import frc.lib.swerve.SwerveModuleConfig;
import frc.robot.RobotConfig.Encoders;
import frc.robot.RobotConfig.Motors;;

public final class SwerveConfig {

    // -------- Kinematics --------

    public static final double trackWidth         = Units.inchesToMeters(23.75); // Between Right/Left Wheel Ctrs
    public static final double wheelBase          = Units.inchesToMeters(23.75); // Between Front/Back Wheel Ctrs
    public static final double wheelDiameter      = Units.inchesToMeters(3.82);  // Prev 3.8195;
    public static final double wheelCircumference = wheelDiameter * Math.PI;
  
    public static final Translation2d frontLeftLocation =
            new Translation2d(wheelBase / 2.0, trackWidth / 2.0);
    public static final Translation2d frontRightLocation =
            new Translation2d(wheelBase / 2.0, -trackWidth / 2.0);
    public static final Translation2d backLeftLocation =
            new Translation2d(-wheelBase / 2.0, trackWidth / 2.0);
    public static final Translation2d backRightLocation =
            new Translation2d(-wheelBase / 2.0, -trackWidth / 2.0);

    public static final SwerveDriveKinematics swerveKinematics =
            new SwerveDriveKinematics(
                    frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation);

    public static final double MK4_L2_driveGearRatio = (6.75 / 1.0);
    public static final double driveGearRatio = MK4_L2_driveGearRatio;

    public static final double MK4i_L2_angleGearRatio = (150.0 / 7.0);  //(50.0 / 14.0) * (60.0 / 10.0);
    public static final double angleGearRatio = MK4i_L2_angleGearRatio;


    /* Angle Motor PID Values */
    public static final double angleKP = 0.3;                   // 0.2 prev. Spectrum = 0.6
    public static final double angleKI = 0.0;
    public static final double angleKD = 0.0;                   // 0.1 prev 1-24-23 Spectrum = 12
    public static final double angleKF = 0.0; 
    // increase to reduce jitter, (2048 * angleGearRatio) / 360.0) = 1 degree = 122 cnts
    public static final int angleAllowableError = 122;          // 1 degree is close enough ????

    /* Drive Motor PID Values */
    public static final double driveKP = 0.025;                 // 0.1 prev. Spectrum = 0.1
    public static final double driveKI = 0.0;
    public static final double driveKD = 0.01;
    public static final double driveKF = 0.0;

    /* Drive Motor Characterization Values */   
    // KS - Volts Stiction -     How many volts are needed to simply start moving/overcoming friction
    // KV - Volts Velocity -     How many volts it takes to achieve a constant, specified velocity
    // KA - Volts Acceleration - How many volts for a given acceleration (mps^2)

    // Scaled to (0 to 1) from (0 to 12) Volts as required for Arbitrary Feedforward
    public static final double driveKS =  (0.305 / 12.0) ;      //( 0.305 / 12.0 );      // Spectrum = (0.605 / 12) = 0.050416
    public static final double driveKV =  (2.4 / 12.0) ;        // ( 5.0   / 12.0 );      // Spectrum = (1.72 / 12) = 0.14333
    public static final double driveKA =  (0.193 / 12.0);      // Spectrum = (0.193 / 12) = 0.0160833
    
    // Swerve Drive Profiling Values
    public static final double maxVelocity = 
        ((6380 / 60) / driveGearRatio) * wheelDiameter * Math.PI * 0.95; // MPS 4.56
    public final static double maxAccel = maxVelocity * 1.5;    //Get max in 0.5 seconds
    //public final static double maxVelocity = 1.5; // 2.8176;

    public static final double maxAngularVelocity = 
                maxVelocity / Math.hypot(trackWidth / 2.0, wheelBase / 2.0); // 10.55 RPS = 1.6 rev per sec.
    public static final double maxAngularAcceleration = Math.pow(maxAngularVelocity, 2);
    //public static final double maxAngularVelocity = 0.08; //0.16182;    // RPS 3.14 * 2 = 360 Degrees per second

    
    /* Neutral Modes */
    public static final NeutralMode angleNeutralMode = NeutralMode.Coast;
    public static final NeutralMode driveNeutralMode = NeutralMode.Coast;

    /* Inverts */
    public static final boolean driveMotorInvert = false; // True = MK4i
    public static final boolean angleMotorInvert = true; // True = MK4i
    public static final boolean canCoderInvert =   false;

    /* Swerve Current Limiting */
    public static final int angleContinuousCurrentLimit = 20;
    public static final int anglePeakCurrentLimit = 30;
    public static final double anglePeakCurrentDuration = 0.1;
    public static final boolean angleEnableCurrentLimit = true;

    public static final int driveContinuousCurrentLimit = 40;
    public static final int drivePeakCurrentLimit = 40;
    public static final double drivePeakCurrentDuration = 0.0;
    public static final boolean driveEnableCurrentLimit = true;

    /* Module Specific Constants */
    /* When calibrating, angle bevel gear is to the right of the robot */

    /* Front Left Module - Module 0 */
    public static final class FLMod0 {
        public static final String moduleName = "FL Mod 0";
        public static final int driveMotorID = Motors.FLdriveMotorID;
        public static final int angleMotorID = Motors.FLangleMotorID;
        public static final int canCoderID = Encoders.FLcanCoderID;
        public static final double angleOffsetC = 53.6;  // 141.86;  // 2.54 + 180;
        public static final double angleOffsetP = 184.39;
        public static double angleOffset = angleOffsetC;
        public static final SwerveModuleConfig config =
                new SwerveModuleConfig(
                        moduleName, driveMotorID, angleMotorID, canCoderID, angleOffset, angleOffsetP);
    }

    /* Front Right Module - Module 1 */
    public static final class FRMod1 {
        public static final String moduleName = "FR Mod 1";
        public static final int driveMotorID = Motors.FRdriveMotorID;
        public static final int angleMotorID = Motors.FRangleMotorID;
        public static final int canCoderID = Encoders.FRcanCoderID;
        public static final double angleOffsetC = 299.0;  // -91.31 + 180;
        public static final double angleOffsetP = 99;
        public static double angleOffset = angleOffsetC;
        public static final SwerveModuleConfig config =
                new SwerveModuleConfig(
                        moduleName, driveMotorID, angleMotorID, canCoderID, angleOffset, angleOffsetP);
    }

    /* Back Left Module - Module 2 */
    public static final class BLMod2 {
        public static final String moduleName = "BL Mod 2";
        public static final int driveMotorID = Motors.BLdriveMotorID;
        public static final int angleMotorID = Motors.BLangleMotorID;
        public static final int canCoderID = Encoders.BLcanCoderID;
        public static final double angleOffsetC = 288.4;  // 172.4 + 180;
        public static final double angleOffsetP = 355;
        public static double angleOffset = angleOffsetC;
        public static final SwerveModuleConfig config =
                new SwerveModuleConfig(
                        moduleName, driveMotorID, angleMotorID, canCoderID, angleOffset, angleOffsetP);
    }

    /* Back Right Module - Module 3 */
    public static final class BRMod3 {
        public static final String moduleName = "BR Mod 3";
        public static final int driveMotorID = Motors.BRdriveMotorID;
        public static final int angleMotorID = Motors.BRangleMotorID;
        public static final int canCoderID = Encoders.BRcanCoderID;
        public static final double angleOffsetC = 324.4;  // 170.59 - 180;
        public static final double angleOffsetP = 342;
        public static double angleOffset = angleOffsetC;
        public static final SwerveModuleConfig config =
                new SwerveModuleConfig(
                        moduleName, driveMotorID, angleMotorID, canCoderID, angleOffset, angleOffsetP);;
    }

    public TalonFXConfiguration swerveAngleFXConfig;
    public TalonFXConfiguration swerveDriveFXConfig;
    public CANCoderConfiguration swerveCanCoderConfig;

    // ----------------------------
    public SwerveConfig() {
        swerveAngleFXConfig = new TalonFXConfiguration();
        swerveDriveFXConfig = new TalonFXConfiguration();
        swerveCanCoderConfig = new CANCoderConfiguration();

        /* Swerve Angle Motor Configurations */
        SupplyCurrentLimitConfiguration angleSupplyLimit =
                new SupplyCurrentLimitConfiguration(
                        SwerveConfig.angleEnableCurrentLimit,
                        SwerveConfig.angleContinuousCurrentLimit,
                        SwerveConfig.anglePeakCurrentLimit,
                        SwerveConfig.anglePeakCurrentDuration);

        swerveAngleFXConfig.slot0.kP = SwerveConfig.angleKP;
        swerveAngleFXConfig.slot0.kI = SwerveConfig.angleKI;
        swerveAngleFXConfig.slot0.kD = SwerveConfig.angleKD;
        swerveAngleFXConfig.slot0.kF = SwerveConfig.angleKF;
        swerveAngleFXConfig.slot0.allowableClosedloopError = SwerveConfig.angleAllowableError;
        swerveAngleFXConfig.supplyCurrLimit = angleSupplyLimit;
        swerveAngleFXConfig.initializationStrategy = SensorInitializationStrategy.BootToZero;

        /* Swerve Drive Motor Configuration */
        SupplyCurrentLimitConfiguration driveSupplyLimit =
                new SupplyCurrentLimitConfiguration(
                        SwerveConfig.driveEnableCurrentLimit,
                        SwerveConfig.driveContinuousCurrentLimit,
                        SwerveConfig.drivePeakCurrentLimit,
                        SwerveConfig.drivePeakCurrentDuration);

        swerveDriveFXConfig.slot0.kP = SwerveConfig.driveKP;
        swerveDriveFXConfig.slot0.kI = SwerveConfig.driveKI;
        swerveDriveFXConfig.slot0.kD = SwerveConfig.driveKD;
        swerveDriveFXConfig.slot0.kF = SwerveConfig.driveKF;
        swerveDriveFXConfig.supplyCurrLimit = driveSupplyLimit;
        swerveDriveFXConfig.initializationStrategy = SensorInitializationStrategy.BootToZero;

        /* Swerve CANCoder Configuration */
        swerveCanCoderConfig.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
        swerveCanCoderConfig.sensorDirection = SwerveConfig.canCoderInvert;
        swerveCanCoderConfig.initializationStrategy =
                SensorInitializationStrategy.BootToAbsolutePosition;
        swerveCanCoderConfig.sensorTimeBase = SensorTimeBase.PerSecond;
    }
    // --------------

    public static Translation2d[] moduleOffsets(double meters) {
        return moduleOffsets(new Translation2d(meters, meters));
    }

    public static Translation2d[] moduleOffsets(Translation2d frontLeft) {
        // ++ +- -+ --
        Translation2d fl = frontLeftLocation.plus(frontLeft);
        Translation2d fr =
                frontRightLocation.plus(new Translation2d(frontLeft.getX(), -frontLeft.getY()));
        Translation2d bl =
                frontRightLocation.plus(new Translation2d(-frontLeft.getX(), frontLeft.getY()));
        Translation2d br =
                frontRightLocation.plus(new Translation2d(-frontLeft.getX(), -frontLeft.getY()));
        Translation2d a[] = {fl, fr, bl, br};
        return a;
    }
}
