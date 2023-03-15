// Created by 4513 - Circuit Breakers
// Based on Code from Team3847 - 2023 Base
// Based on Code from Team364 - BaseFalconSwerve

package frc.robot.swerve;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Swerve extends SubsystemBase {
    public      SwerveConfig config;
    public      Gyro gyro;
    public      Odometry odometry;
    public      SwerveModule[] mSwerveMods;
    private     SwerveModuleState[] mSwerveModStates;

    public      boolean pilotRobotPOV;

    // --------------------- Constructor ------------------
    public Swerve() {
        setName("Swerve");
        config = new SwerveConfig();
        gyro = new Gyro();
        mSwerveMods =
                new SwerveModule[] {
                    new SwerveModule(0, config, SwerveConfig.FLMod0.config),
                    new SwerveModule(1, config, SwerveConfig.FRMod1.config),
                    new SwerveModule(2, config, SwerveConfig.BLMod2.config),
                    new SwerveModule(3, config, SwerveConfig.BRMod3.config)
                };
        //mSwerveMods[0].logDescription();    // Add description to log file
        odometry = new Odometry(this);
        Timer.delay(1.0);
        resetFalconAngles();

        pilotRobotPOV = false;
    }

    @Override
    public void periodic() {
        odometry.update();
        mSwerveModStates = getStatesCAN();
        SmartDashboard.putNumber("Gyro Yaw", gyro.getGyroYawAngle());
        //telemetry.logModuleAbsolutePositions();
    }


    // -------------------------------------------------------------------
    // ----------------------  Swerve Drive Methods  ---------------------
    // -------------------------------------------------------------------

    // This drive method (overloaded) does'nt need the translation component
    public void drive(  double fwdPositive,
                        double leftPositive,
                        double omegaRadiansPerSecond,
                        boolean fieldRelative,
                        boolean isOpenLoop) {
        // call real drive method to get job done
        drive ( fwdPositive,
                leftPositive,
                omegaRadiansPerSecond,
                fieldRelative,
                isOpenLoop,
                new Translation2d ());      // Zeros out Translation by default
    }

    /**
     * Used to drive the swerve robot, should be called from commands that require swerve.
     *
     * @param fwdPositive Velocity of the robot fwd/rev, Forward Positive meters per second
     * @param leftPositive Velocity of the robot left/right, Left Positive meters per secound
     * @param omegaRadiansPerSecond Rotation Radians per second
     * @param fieldRelative If the robot should drive in field relative
     * @param isOpenLoop If the robot should drive in open loop
     * @param centerOfRotationMeters The center of rotation in meters
     */

     public void drive( double fwdPositive,
                        double leftPositive,
                        double omegaRadiansPerSecond,
                        boolean fieldRelative,
                        boolean isOpenLoop,
                        Translation2d centerOfRotationMeters) {
    
        // ------------------- Step 1 Set Chassis Speeds ----------------------
        // mps (Meters Per Second) and rps (Radians Per Second)
        ChassisSpeeds speeds;
        if (fieldRelative) {
            speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
                        fwdPositive, leftPositive, omegaRadiansPerSecond, getGyroYaw());
        } else {
            speeds = new ChassisSpeeds(fwdPositive, leftPositive, omegaRadiansPerSecond);
        }

        // --------------- Step 2 Create Swerve Modules Desired States Array ---------------
        // Wheel Velocity (mps) and Wheel Angle (radians) for each of the 4 swerve modules
        SwerveModuleState[] swerveModuleDesiredStates =
                SwerveConfig.swerveKinematics.toSwerveModuleStates(speeds, centerOfRotationMeters);

        // -------------------------- Step 3 Desaturate Wheel speeds -----------------------
        // LOOK INTO THE OTHER CONSTRUCTOR FOR desaturateWheelSpeeds to see if it is better
        SwerveDriveKinematics.desaturateWheelSpeeds(
            swerveModuleDesiredStates, SwerveConfig.maxVelocity);

        // ------------------ Step 4 Send Desrired Module states to wheel modules ----------------
        for (SwerveModule mod : mSwerveMods) {
            mod.setDesiredState(swerveModuleDesiredStates[mod.moduleNumber], isOpenLoop);
        }
    }


     /**
     * Used to drive wheel speeds manually
     *
     * @param leftVolts +- 12 volts
     * @param rightVolts +- 12 volts
     */    
    public void tankDriveVolts(double leftVolts, double rightVolts) {
        mSwerveMods[0].setDriveMotorVoltage(leftVolts);
        mSwerveMods[2].setDriveMotorVoltage(leftVolts);
        mSwerveMods[1].setDriveMotorVoltage(rightVolts);
        mSwerveMods[3].setDriveMotorVoltage(rightVolts);
    }

    public void stop() {
        for (SwerveModule mod : mSwerveMods) { 
            mod.mDriveMotor.stopMotor();
            mod.mAngleMotor.stopMotor();
        }
    }

    public void setWheelsLock(){
        // Set wheels to angle that locks robot motion
        Rotation2d wheelAngleLeft  = new Rotation2d().fromDegrees(45);
        Rotation2d wheelAngleRight = new Rotation2d().fromDegrees(-45);
        mSwerveMods[0].setAngle(new SwerveModuleState( 0, wheelAngleLeft));
        mSwerveMods[1].setAngle(new SwerveModuleState( 0, wheelAngleRight));
        mSwerveMods[2].setAngle(new SwerveModuleState( 0, wheelAngleLeft));
        mSwerveMods[3].setAngle(new SwerveModuleState( 0, wheelAngleRight));
    }

    // Set brakes On/Off for swerve module motors
    public void setBrakeMode(boolean enabled) {
        for (SwerveModule mod : mSwerveMods) {
            if (enabled) {
                mod.mDriveMotor.setNeutralMode(NeutralMode.Brake);
                mod.mAngleMotor.setNeutralMode(NeutralMode.Brake);
            } else {
                mod.mDriveMotor.setNeutralMode(NeutralMode.Coast);
                mod.mAngleMotor.setNeutralMode(NeutralMode.Coast);
            }
        }
    }

    // ---------- Initialize Falcon Angle Motor To CanCoder Absolute -------
    public void resetFalconAngles() {
        for (SwerveModule mod : mSwerveMods) {
            mod.resetFalconToAbsolute();
        }
    }

    public void setLastAngleToCurrentAngle() {
        for (SwerveModule mod : mSwerveMods) {
            mod.setLastAngleToCurrentAngle();
        }
    }

    // -------------------------------------------------------------
    // -------------------  Heading / Gyro Calls  ------------------
    // -------------------------------------------------------------

     /**
     * Get the heading of the robot
     *
     * @return current heading using the offset from Odometry class
     */


    /**
     * Reset the Heading to any angle
     *
     * @param heading Rotation2d representing the current heading of the robot
     */

    public void resetGyro(){
        gyro.resetGyro();
    }

    public void resetGyro( double newYaw ) {
        gyro.resetGyro( newYaw ); // +-180 Degrees ??
    }

    public Rotation2d getGyroYaw() {
        return gyro.getGyroYawRotation2d();
    }

    public double getGyroYawDegrees() {
        return gyro.getGyroYawAngle();
    }

    public double getSnap90Angle() {
        double currAngle = getGyroYawDegrees();
        double tgt;
        if      ((currAngle >= 45) && (currAngle <= 135))  { tgt = 90;  }
        else if ((currAngle >= 135) || (currAngle < -135)) { tgt = 180; }
        else if ((currAngle <= -45) && (currAngle >=-135)) { tgt = -90; }
        else    { tgt = 0; }
        return tgt;
    }

    // --------------------------------------------------------------------
    // ----------------------  Odometry/Pose Methods  ---------------------
    // --------------------------------------------------------------------

    public Pose2d getPoseMeters() {
        return odometry.getPoseMeters();
    }

    public void resetOdometry(Pose2d pose) {
        odometry.resetOdometry(pose);
    }

    public Translation2d getPosition(){
        return odometry.getTranslationMeters();
    }

    public Rotation2d getHeading() {
        return odometry.getHeading();
    }

    // -------------------------------------------------------------
    // --------------- Set/Get Swerve Module States ----------------
    // -------------------------------------------------------------

     /**
     * Used by SwerveFollowCommand in Auto, assumes closed loop control
     *
     * @param desiredStates Meters per second and radians per second
     */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, SwerveConfig.maxVelocity);
        for (SwerveModule mod : mSwerveMods) {
            mod.setDesiredState(desiredStates[mod.moduleNumber], false);
        }
    }

    /**
     * Gets the states of the modules from the modules directly, called once a loop
     *
     * @return the current module states
     */
    public SwerveModuleState[] getStatesCAN() {
        SwerveModuleState[] states = new SwerveModuleState[4];
        for (SwerveModule mod : mSwerveMods) {
            states[mod.moduleNumber] = mod.getState();
        }
        return states;
    }

    public SwerveModuleState[] getStates() {
        return mSwerveModStates;
    }

    /**
     * Get the module positions from the modules directly
     *
     * @return the current module positions
     */
    public SwerveModulePosition[] getPositions() {
        SwerveModulePosition[] positions = new SwerveModulePosition[4];
        for (SwerveModule mod : mSwerveMods) {
            positions[mod.moduleNumber] = mod.getPosition();
        }
        return positions;
    }

    // other misc methods
    public void togglePilotPOV() {
        pilotRobotPOV = !pilotRobotPOV;
        System.out.println("Swerve - Toggled Pilot POV to " + pilotRobotPOV);
    }

    public boolean getPilotPOV() {
        return pilotRobotPOV;
    }

}
