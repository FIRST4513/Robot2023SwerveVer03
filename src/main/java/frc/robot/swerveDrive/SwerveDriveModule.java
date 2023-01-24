// Created by 4513 - Circuit Breakers
// Based on Code from Team3847 - 2023 Base
// Based on Code from Team364 - BaseFalconSwerve

package frc.robot.swerveDrive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.CANCoderStatusFrame;
import com.ctre.phoenix.sensors.WPI_CANCoder;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.swerve.SwerveModuleConfig;
import frc.lib.util.Conversions;
import frc.robot.Robot;

public class SwerveDriveModule extends SubsystemBase {
    public String moduleName;
    public int moduleNumber;
    private double angleOffset;
    private WPI_TalonFX mAngleMotor;
    private WPI_TalonFX mDriveMotor;
    private WPI_CANCoder angleEncoder;
    private double lastAngle;
    private SwerveDriveConfig swerveConfig;
    private SwerveModuleState mSwerveModState = new SwerveModuleState();
    public SwerveModulePosition mSwerveModPosition = new SwerveModulePosition();

    private Rotation2d mCANcoderAngle360CCW = new Rotation2d();
    private Rotation2d mCANcoderAngle180CCW = new Rotation2d();
    private Rotation2d desiredAngle = new Rotation2d();    
    private double desiredSpeed, optimizedSpeed = 0;
    private double delta, currentAngle, outputAngle, optimizedAngle = 0;
    private double percentOutput, feedforward_value = 0;
    private double rawFalconSensorData = 0;

    private boolean m_LogFlag = false;
    private String line;

    SimpleMotorFeedforward feedforward =
            new SimpleMotorFeedforward(
                    SwerveDriveConfig.driveKS, SwerveDriveConfig.driveKV, SwerveDriveConfig.driveKA);

    // ------------- Constructor ------------
    public SwerveDriveModule( int moduleNumber,
                         SwerveDriveConfig swerveConfig,
                         SwerveModuleConfig moduleConfig) {
        setLoggingOn();
        this.moduleNumber = moduleNumber;
        this.swerveConfig = swerveConfig;
        moduleName = moduleConfig.moduleName;
        angleOffset = moduleConfig.angleOffset;

        /* Angle Encoder Config */
        angleEncoder = new WPI_CANCoder(moduleConfig.cancoderID);
        configAngleEncoder();

        /* Angle Motor Config */
        mAngleMotor = new WPI_TalonFX(moduleConfig.angleMotorID);
        configAngleMotor();

        /* Drive Motor Config */
        mDriveMotor = new WPI_TalonFX(moduleConfig.driveMotorID);
        configDriveMotor();

        resetFalconToCANcoderAngle();
        lastAngle = getFalconAngle().getDegrees();
    }

    // ------------ Periodic Update of Wheel State Info. -----------
    @Override
    public void periodic() {
        updateSwerveModuleState();              // Drive Mtr Vel (mps) & Angle Motor Angle
        updateSwerveModulePosition();           // Drive Mtr Dist & Angle Motor Angle
        updateCANcoderAngles();                 // CanCoder Angles (360 and 180 values)
        logSwerveModuleData();
    }


    // -------------------------------------------------------------------------------
    // --------------------------- Drive Motors Methods ------------------------------
    // -------------------------------------------------------------------------------

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {
        desiredAngle = desiredState.angle;
        desiredSpeed = desiredState.speedMetersPerSecond;
        currentAngle = convertAngleTo180(getFalconAngle().getDegrees());   //mSwerveModState.angle;

        // ---------------------- Step 1   Optimize Wheel Angle ----------------------
        // Wheel only needs to rotate a max of 90 degrees to achieve any direction
        desiredState = SwerveModuleState.optimize(desiredState, Rotation2d.fromDegrees(currentAngle));
        optimizedSpeed = desiredState.speedMetersPerSecond;
        optimizedAngle = desiredState.angle.getDegrees();

        // ---------------------- Step 2  Calculate New Steer Angle ----------------------
        // Calculate the correct angle to steer the wheel and correct for CTRE controller
        // being continuous +360 -> -360.   
        
        delta = optimizedAngle - currentAngle;

        outputAngle = getFalconAngle().getDegrees() + delta;

        // ------------------------- Step 3 Reduce wheel angle Jitter  --------------------------
        //
        if ((Math.abs(optimizedSpeed) < (SwerveDriveConfig.maxVelocity * 0.05))) {
            outputAngle = lastAngle;
        }

        // --------------------------- Step 4 Set Angle Motor  ----------------------------
        // Uses internal PID to set/hold angle motor at correct position
        mAngleMotor.set(
                ControlMode.Position,
                Conversions.degreesToFalcon(outputAngle, SwerveDriveConfig.angleGearRatio));
        lastAngle = outputAngle;

        // --------------------------- Step 5 Set Drive Motor  ----------------------------
        // Send velocity data to the modules Drive motor
        if (isOpenLoop) {
            percentOutput = optimizedSpeed / SwerveDriveConfig.maxVelocity; // MPS to -1 to +1 value
            setDriveMotorPercent( percentOutput );
        } else {
            // Send Optimized speed MPS along with feedforward value to Drive motor
            feedforward_value = feedforward.calculate(optimizedSpeed);
            setDriveMotorVelocityMPS( optimizedSpeed, feedforward_value );
        }
        if (moduleNumber == 0)      { smartDashboardUpdate(); } // Only for testing and Tuning
    }

    public void setDriveMotorVelocityMPS( double vel, double feedforward){
        // This is a closed loop, using internal PID and Feedforward, to maintain accurate velocity
        // possible PID parameter not tuned or feedforward values causing jitter and creep errors?
        double falcon_desired_Velocity = Conversions.MPSToFalcon(
                                            vel,
                                            SwerveDriveConfig.wheelCircumference,
                                            SwerveDriveConfig.driveGearRatio);

        mDriveMotor.set( ControlMode.Velocity,
                         falcon_desired_Velocity,
                         DemandType.ArbitraryFeedForward,
                         0); // feedforward);
    }

    public void setDriveMotorPercent( double percentOut){
        // This is an openloop value -1 to +1
        mDriveMotor.set(ControlMode.PercentOutput, percentOut); 
    }

    public void stop() {
        mDriveMotor.stopMotor();
        mAngleMotor.stopMotor();
    }

    public void setDriveMotorVoltage(double voltage) {
        mDriveMotor.setVoltage(voltage);
    }

    // -------------------------------------------------------------------------------
    // ---------------------------- Module State Methods -----------------------------
    // -------------------------------------------------------------------------------

    private void updateSwerveModuleState() {
        double velocity = Conversions.falconToMPS(
                                mDriveMotor.getSelectedSensorVelocity(),
                                SwerveDriveConfig.wheelCircumference,
                                SwerveDriveConfig.driveGearRatio
                                );
     
        // Conversion = Enc-Pos(cnts) * (360.0 / (gearRatio * 2048.0)) continous > 360 to < -360 degrees
        double deg = getFalconAngle().getDegrees();
        Rotation2d angle = Rotation2d.fromDegrees(deg);

        mSwerveModState = new SwerveModuleState(velocity, angle);
    }

    public SwerveModuleState getState() {
        // used by swerve drive odometry
        return mSwerveModState;
    }


    // -------------------------------------------------------------------------
    // -------------------------  CANcoder Methods -----------------------------
    // -------------------------------------------------------------------------
        
    private void updateCANcoderAngles() {
        mCANcoderAngle360CCW = Rotation2d.fromDegrees( getCANcoderAngle360() );
        mCANcoderAngle180CCW = Rotation2d.fromDegrees( getCANcoderAngle180() );
    }

    public double getCANcoderAngleAbsolute(){
        // Returns CANcoder Raw angle with NO offset applied, continous 0 to 360 degrees (CCW).
        double angle = angleEncoder.getAbsolutePosition();
        return angle;
    }

    public double getCANcoderAngle360(){
        // Returns CANcoder angle Continous 0 (Fwd) to 360 degrees CCW, with offset applied
        // 0Degrees is straight forward
        double angle = getCANcoderAngleAbsolute() - angleOffset;
        if (angle < 0)      { angle = angle + 360; }
        return angle;             
    }

    public double getCANcoderAngle180(){
        // Returns CANcoder angle Continous +180 degrees CCW to -180 degrees CW, with offset applied
        // 0 Degrees is wheel staight forward
        double angle = getCANcoderAngleAbsolute() - angleOffset;
        if (angle < -180)   { angle = angle + 360; }    
        if (angle > 180)    { angle = 360 - angle; }    // Assures +180 to -180 degrees
        return (angle); 
    }

    // -------------------------------------------------------------------------------
    // --------------------------- Angle Motor Methods -------------------------------
    // -------------------------------------------------------------------------------

    public double getTargetAngle() {
        return lastAngle;
    }

    public Rotation2d getFalconAngle() {
        // returns encoder.counts * (360.0 / (gearRatio * 2048.0)) each cnt = 0.00823125 degrees
        double falconDegrees = Conversions.falconToDegrees(
                                    mAngleMotor.getSelectedSensorPosition(),
                                    SwerveDriveConfig.angleGearRatio); 
        return Rotation2d.fromDegrees(falconDegrees);
    }

    // Set Angle Motor Encoder based on absolute CanCoder Value
    public void resetFalconToCANcoderAngle() {
        double absolutePosition = Conversions.degreesToFalcon(
                            // getCANcoderAngle360(),
                            getCANcoderAngle180(),
                            SwerveDriveConfig.angleGearRatio);
        mAngleMotor.setSelectedSensorPosition(absolutePosition);
        lastAngle = getFalconAngle().getDegrees();
    }
    // -------------------------------------------------------------------------------
    // ---------------------------- Drive Motor Methods ------------------------------
    // -------------------------------------------------------------------------------
        
    private void updateSwerveModulePosition() {
        mSwerveModPosition = new SwerveModulePosition(
                                        getDriveDistanceMeters(),
                                        mSwerveModState.angle);
    }

    public double getDriveDistanceMeters() {
        double rawFalconSensorPos = mDriveMotor.getSelectedSensorPosition();
        double positionMeters = Conversions.FalconToMeters(
                                rawFalconSensorPos,
                                SwerveDriveConfig.wheelCircumference,
                                SwerveDriveConfig.driveGearRatio);
        return positionMeters;
    }

    public double getDriveVelocityMPS(){
        double rawFalconSensorVel = mDriveMotor.getSelectedSensorVelocity();
        double velMPS = Conversions.falconToMPS(
            rawFalconSensorVel,
            SwerveDriveConfig.wheelCircumference,
            SwerveDriveConfig.driveGearRatio);
        return velMPS;
    }


    // -------------------------------------------------------------------------------
    // -------------------------- Configuration Methods ------------------------------
    // -------------------------------------------------------------------------------
    
    private void configAngleEncoder() {
        angleEncoder.configFactoryDefault();
        angleEncoder.configAllSettings(swerveConfig.swerveCanCoderConfig);
        updateCANcoderAngles();
        angleEncoder.setStatusFramePeriod(CANCoderStatusFrame.VbatAndFaults, 249);
        angleEncoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData, 20);
    }

    private void configAngleMotor() {
        mAngleMotor.configFactoryDefault();
        mAngleMotor.configAllSettings(swerveConfig.swerveAngleFXConfig);
        mAngleMotor.setInverted(SwerveDriveConfig.angleMotorInvert);
        mAngleMotor.setNeutralMode(SwerveDriveConfig.angleNeutralMode);
        resetFalconToCANcoderAngle();
    }

    private void configDriveMotor() {
        mDriveMotor.configFactoryDefault();
        mDriveMotor.configAllSettings(swerveConfig.swerveDriveFXConfig);
        mDriveMotor.setInverted(SwerveDriveConfig.driveMotorInvert);
        mDriveMotor.setNeutralMode(SwerveDriveConfig.driveNeutralMode);
        mDriveMotor.setSelectedSensorPosition(0);
    }

    // -------------------------------------------------------------------------------
    // ------------------------------- Misc. Methods ---------------------------------
    // -------------------------------------------------------------------------------

    public void setBrakeMode(Boolean enabled) {
        if (enabled) {
            mDriveMotor.setNeutralMode(NeutralMode.Brake);
            mAngleMotor.setNeutralMode(NeutralMode.Brake);
        } else {
            mDriveMotor.setNeutralMode(NeutralMode.Coast);
            mAngleMotor.setNeutralMode(NeutralMode.Coast);
        }
    }

    public double convertAngleTo180( double angle) {
        if ( angle > 360 )      { angle = angle % +360; }       // Convert to 0 to +360
        if ( angle < -360 )     { angle = angle % -360; }       // Convert to 0 to -360
        if ( angle > 180 )      { angle = angle - 360; }        // Convert to 0 to -180
        if ( angle < -180 )     { angle = angle + 360; }        // Convert to 0 to +180
        return angle;
    }

    public String getName() {
        return moduleName;
    }

    private void smartDashboardUpdate(){
        // Only needed for testing and tuning
        if (moduleNumber == 0) {
            SmartDashboard.putNumber("Desired Angle", desiredAngle.getDegrees());
            SmartDashboard.putNumber("Desired Speed", desiredSpeed);

            SmartDashboard.putNumber("Orig Cancoder Angle", mCANcoderAngle180CCW.getDegrees());
            SmartDashboard.putNumber("Orig Falcon Angle", currentAngle);

            SmartDashboard.putNumber("Optimized Angle", optimizedAngle);
            SmartDashboard.putNumber("Optimized Speed", optimizedSpeed);

            SmartDashboard.putNumber("Delta Angle", delta);
            SmartDashboard.putNumber("Output Angle", outputAngle);

            SmartDashboard.putNumber("Actual Vel", getDriveVelocityMPS());
            SmartDashboard.putNumber("FeedForward", feedforward_value);

            SmartDashboard.putNumber("Percent Output", percentOutput);
            SmartDashboard.putNumber("Falcon Sensor", rawFalconSensorData);
        }
    }

    public void setLoggingOn()	{ m_LogFlag = true; }
    public void setLoggingOff() { m_LogFlag = false; }
    
    public void logDescription(){
      	// Check to see if we should just exit and not log
        if (m_LogFlag == false)  { return; }
        line = "Timestamp,";
        line = "Mod #,Mod Nm";
        line += "Desired Angle,Desired Speed,";
        line += "Orig Cancoder Angle,Orig Falcon Angle,";
        line += "Optimized Angle,Optimized Speed,";
        line += "Delta Angle,Output Angle,";
        line += "Actual Vel, FeedForward";
        Robot.logger.appendLog(line);
    }

    // ----------- Log Drivetrain data --------------------
    public void logSwerveModuleData() {
    	// Check to see if we should just exit and not log
        if (m_LogFlag == false) { return; }
        //if (moduleNumber != 0)  { return; }

        // ---------- Build Print String -----------------
        line = "";
        // ---------- Show Module Num and Name ------------
        line += moduleNumber + "," +moduleName;

        // ------------ Show Desired State ------------
        line += "," + desiredAngle.getDegrees() + "," + desiredSpeed;
        line += "," + mCANcoderAngle180CCW.getDegrees() + "," + currentAngle;
        line += "," + optimizedAngle + "," + optimizedSpeed;
        line += "," + delta + "," +  outputAngle;
        line += "," + getDriveVelocityMPS() + "," + feedforward_value;
        Robot.logger.appendLog(line);
    }
    
}
