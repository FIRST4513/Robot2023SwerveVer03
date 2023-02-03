// Created by 4513 - Circuit Breakers
// Based on Code from Team3847 - 2023 Base
// Based on Code from Team364 - BaseFalconSwerve

package frc.robot.swerveDrive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotTelemetry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SwerveDrive extends SubsystemBase {
    public      SwerveDriveConfig config;
    public      Odometry odometry;
    public      SwerveDriveTelemetry telemetry;
    protected   SwerveDriveModule[] mSwerveMods;
    private     SwerveModuleState[] SwerveModDesiredStates;
    public      Gyro gyro;

    public SwerveDrive() {
        setName("Swerve");
        config = new SwerveDriveConfig();

        mSwerveMods =
                new SwerveDriveModule[] {
                    new SwerveDriveModule(0, config, SwerveDriveConfig.FLMod0.config),
                    new SwerveDriveModule(1, config, SwerveDriveConfig.FRMod1.config),
                    new SwerveDriveModule(2, config, SwerveDriveConfig.BLMod2.config),
                    new SwerveDriveModule(3, config, SwerveDriveConfig.BRMod3.config)
                };
        mSwerveMods[0].logDescription();    // Add description to log file

        gyro = new Gyro();
        odometry = new Odometry(this);
        telemetry = new SwerveDriveTelemetry(this);

        Timer.delay(1.0);
        resetFalconAngles();
        
        RobotTelemetry.print("Gyro initilized and Swerve angles");
        //drive(0, 0, 0, true, false, new Translation2d());     // Set the initial module states to zero
    }

    @Override
    public void periodic() {
        odometry.update();
        SmartDashboard.putNumber("gyroheading",gyro.getHeadingDegrees());

        //telemetry.logModuleAbsolutePositions();
    }

    // -------------------------------------------------------------------
    // ----------------------  Swerve Drive Methods  ---------------------
    // -------------------------------------------------------------------
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

     // This drive method used with DriveByJoystick command and Autonomous drive commands
    public void drive(
            double fwdPositive,
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
                            fwdPositive, leftPositive, omegaRadiansPerSecond, getHeading());
        } else {
            speeds = new ChassisSpeeds(fwdPositive, leftPositive, omegaRadiansPerSecond);
        }

        // --------------- Step 2 Create Swerve Modules Desired States Array ---------------
        // Wheel Velocity (mps) and Wheel Angle (radians) for each of the 4 swerve modules
        SwerveModDesiredStates =
                SwerveDriveConfig.swerveKinematics.toSwerveModuleStates(speeds, centerOfRotationMeters);

        // -------------------------- Step 3 Desaturate Wheel speeds -----------------------
        // LOOK INTO THE OTHER CONSTRUCTOR FOR desaturateWheelSpeeds to see if it is better
        SwerveDriveKinematics.desaturateWheelSpeeds(
                SwerveModDesiredStates, SwerveDriveConfig.maxVelocity);

        // ------------------ Step 4 Send Desrired Module states to wheel modules ----------------
        for (SwerveDriveModule mod : mSwerveMods) {
            mod.setDesiredState(SwerveModDesiredStates[mod.moduleNumber], isOpenLoop);
        }
    }

     /**
     * Used by SwerveFollowCommand in Auto, assumes closed loop control
     *
     * @param desiredStates Meters per second and radians per second
     */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, SwerveDriveConfig.maxVelocity);

        SwerveModDesiredStates = desiredStates;
        for (SwerveDriveModule mod : mSwerveMods) {
            mod.setDesiredState(desiredStates[mod.moduleNumber], false);
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

    // Used in turnCmd
    // public void useOutput(double output) {
    //     pidTurn = output * SwerveConstants.maxAngularVelocity;
    // }

    // //Used for control loops that give a rotational velocity directly
    // public void setRotationalVelocity(double rotationalVelocity){
    //     pidTurn = rotationalVelocity;
    // }

   /**
     * Stop the drive and angle motor of each module And set desired states to 0 meters per second
     * and current module angles
     */
    public void stop() {
        SwerveModuleState[] states = new SwerveModuleState[4];
        for (SwerveDriveModule mod : mSwerveMods) {
            mod.stop();
            states[mod.moduleNumber] =
                    new SwerveModuleState(0, mod.getTargetAngle());
        }
        SwerveModDesiredStates = states;
    }


    // -------------------------------------------------------------
    // ---------  Brake Mode & Reset Angle Motor Angle  ------------
    // -------------------------------------------------------------

    /**
     * Set both the drive and angle motor on each module to brake mode if enabled = true
     *
     * @param enabled true = brake mode, false = coast mode
     */
    public void setBrakeMode(boolean enabled) {
        for (SwerveDriveModule mod : mSwerveMods) {
            mod.setBrakeMode(enabled);
        }
    }


    /** Reset AngleMotors to Absolute This is used to reset the angle motors to absolute position */
    public void resetSteeringToAbsolute() {
        for (SwerveDriveModule mod : mSwerveMods) {
            mod.resetFalconToAbsolute();
        }
    }


    // -------------------------------------------------------------
    // ----------------------  Odometry Calls  ---------------------
    // -------------------------------------------------------------

    /**
     * Reset the Heading to any angle
     *
     * @param heading Rotation2d representing the current heading of the robot
     */
    public void resetHeading(Rotation2d heading) {
        odometry.resetHeading(heading);
    }

    /**
     * Reset the pose2d of the robot
     *
     * @param pose
     */
    public void resetOdometry(Pose2d pose) {
        odometry.resetOdometry(pose);
    }

    /**
     * Get the heading of the robot
     *
     * @return current heading using the offset from Odometry class
     */
    public Rotation2d getHeading() {
        return odometry.getHeading();
    }

    // Used in turn to angle
    public double getDegrees(){
        return gyro.getHeadingDegrees();
    }

    public Rotation2d getHeadingRotation2d() {
        return Rotation2d.fromDegrees(getDegrees() * -1.0f);
    }

    public void resetGyro(){
        gyro.resetGyro();
    }

    public void setGyroDegrees( double newHdg ) {
        gyro.setHeadingDegrees( newHdg);
    }

    /**
     * Ge the Pose of the odemotry class
     *
     * @return
     */
    public Pose2d getPoseMeters() {
        return odometry.getPoseMeters();
    }

    public Translation2d getPosition(){
        return odometry.getTranslationMeters();
    }

    // -------------------------------------------------------------
    // ----------------- Get Swerve Module States ------------------
    // -------------------------------------------------------------

    /**
     * Gets the states of the modules from the modules directly, called once a loop
     *
     * @return the current module states
     */
    public SwerveModuleState[] getStates() {
        SwerveModuleState[] states = new SwerveModuleState[4];
        for (SwerveDriveModule mod : mSwerveMods) {
            states[mod.moduleNumber] = mod.getState();
        }
        return states;
    }

    public SwerveModuleState[] getDesiredStates() {
        return SwerveModDesiredStates;
    }

    /**
     * Get the module positions from the modules directly
     *
     * @return the current module positions
     */
    public SwerveModulePosition[] getPositions() {
        SwerveModulePosition[] positions = new SwerveModulePosition[4];
        for (SwerveDriveModule mod : mSwerveMods) {
            positions[mod.moduleNumber] = mod.mSwerveModPosition;
        }
        return positions;
    }


    /**
     * Used by SwerveFollowCommand in Auto, assumes closed loop control
     *
     * @param desiredStates Meters per second and radians per second
     */
    public void resetFalconAngles() {
        for (SwerveDriveModule mod : mSwerveMods) {
            mod.resetFalconToAbsolute();
        }
    }

    public double getCanCoderAngleTest(int modID) {
        return mSwerveMods[modID].getCanCoder().getDegrees();
    }

    public double getFalconAngleTest(int modID) {
        return mSwerveMods[modID].getFalconAngle().getDegrees();
    }
}
