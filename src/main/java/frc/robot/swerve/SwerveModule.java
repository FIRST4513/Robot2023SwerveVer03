// Created by 4513 - Circuit Breakers
// Based on Code from Team3847 - 2023 Base
// Based on Code from Team364 - BaseFalconSwerve

package frc.robot.swerve;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.CANCoderStatusFrame;
import com.ctre.phoenix.sensors.WPI_CANCoder;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.swerve.CTREModuleState;
import frc.lib.swerve.SwerveModuleConfig;
import frc.lib.util.Conversions;

public class SwerveModule extends SubsystemBase {
    public  String          moduleName;
    public  int             moduleNumber;
    private double          angleOffset;
    public  WPI_TalonFX     mAngleMotor;
    public  WPI_TalonFX     mDriveMotor;
    private WPI_CANCoder    angleEncoder;
    private Rotation2d      lastAngle;
    private SwerveConfig    swerveConfig;

    private boolean m_LogFlag = false;
    private String line;

    SimpleMotorFeedforward feedforward =
            new SimpleMotorFeedforward( SwerveConfig.driveKS,
                                        SwerveConfig.driveKV,
                                        SwerveConfig.driveKA);

    public  SwerveModulePosition mSwerveModPosition = new SwerveModulePosition();

    // ------------- Constructor ------------
    public SwerveModule( int moduleNumber,
                              SwerveConfig swerveConfig,
                              SwerveModuleConfig moduleConfig) {
        setLoggingOff();
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

        lastAngle = getFalconAngle();  // .getDegrees();
    }

    // ------------ Periodic Update of Wheel State Info. -----------
    @Override
    public void periodic() {
        //logSwerveModuleData();
    }

    // -----------------------------------------------------
    // ------------- Set Drive and Angle Motors  -----------
    // -----------------------------------------------------

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {
        desiredState = CTREModuleState.optimize(desiredState, getState().angle);
        setAngle(desiredState);
        setSpeedMPS(desiredState, isOpenLoop);
    }

    // ----- Set Angle Motor to needed angle --------
    public void setAngle(SwerveModuleState desiredState) {
        // Prevent rotating module if speed is less than 1% of max speed. (Jitter prevention)
        Rotation2d angle = desiredState.angle;
        if (( Math.abs(desiredState.speedMetersPerSecond) < (SwerveConfig.maxVelocity * 0.01))) {
            angle = lastAngle;
        }
        mAngleMotor.set(ControlMode.Position,
                        Conversions.degreesToFalcon(angle.getDegrees(),
                        SwerveConfig.angleGearRatio));
        lastAngle = angle;
    }

    // ----- Set Drive Motor to needed speed --------
    public void setSpeedMPS(SwerveModuleState desiredState, boolean isOpenLoop) {
        if (isOpenLoop) {
             // MPS to -1 to +1 value
            double percentOutput = desiredState.speedMetersPerSecond / SwerveConfig.maxVelocity;
            mDriveMotor.set(ControlMode.PercentOutput, percentOutput);
        } else {
            // Send Optimized speed MPS along with feedforward value to Drive motor
            double velocity = Conversions.MPSToFalcon(desiredState.speedMetersPerSecond,
                                                      SwerveConfig.wheelCircumference,
                                                      SwerveConfig.driveGearRatio);
            // their new way of doing it:
            mDriveMotor.set(ControlMode.Velocity,
                            velocity,
                            DemandType.ArbitraryFeedForward,
                            feedforward.calculate(desiredState.speedMetersPerSecond));
        }
    }

    public void setDriveMotorVoltage(double voltage) {
        mDriveMotor.setVoltage(voltage);
    }


    // ------------------------------------------------------------
    // ---------- Getting Angle, State, and Name Methods ----------
    // ------------------------------------------------------------

    // theirs: getAngle()
    public Rotation2d getFalconAngle() {
        // returns Angle from [encoder.counts * (360.0 / (gearRatio * 2048.0)) ]
        // Each cnt = 0.00823125 degrees
        double falconDegrees = Conversions.falconToDegrees(
                                    mAngleMotor.getSelectedSensorPosition(),
                                    SwerveConfig.angleGearRatio); 
        return Rotation2d.fromDegrees(falconDegrees);
    }

    public Rotation2d getCanCoder() {
        return Rotation2d.fromDegrees(angleEncoder.getAbsolutePosition());
    }

    public double getCanCoderDegreesWithOffset() {
        return getCanCoder().getDegrees() - angleOffset;
    }

    public Rotation2d getTargetAngle() {
        return lastAngle;
    }

    public SwerveModuleState getState() {
        double velocity = Conversions.falconToMPS( mDriveMotor.getSelectedSensorVelocity(),
                                                   SwerveConfig.wheelCircumference,
                                                   SwerveConfig.driveGearRatio);
        Rotation2d angle = getFalconAngle();
        return new SwerveModuleState(velocity, angle);
    }

    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(getDriveDistanceMeters(), getFalconAngle());
    }

    public String getName() {
        return moduleName;
    }

    public double getDriveDistanceMeters() {
        double rawFalconSensorPos = mDriveMotor.getSelectedSensorPosition();
        double positionMeters = Conversions.FalconToMeters(
                                        rawFalconSensorPos,
                                        SwerveConfig.wheelCircumference,
                                        SwerveConfig.driveGearRatio);
        return positionMeters;
    }

    public double getDriveVelocityMPS(){
        double rawFalconSensorVel = mDriveMotor.getSelectedSensorVelocity();
        double velMPS = Conversions.falconToMPS(
            rawFalconSensorVel,
            SwerveConfig.wheelCircumference,
            SwerveConfig.driveGearRatio);
        return velMPS;
    }

    // -----------------------------------------------------------
    // -------- Configure Angle and Drive Motor Methods ----------
    // -----------------------------------------------------------

    // Set Angle Motor Encoder based on absolute CanCoder Value
    public void resetFalconToAbsolute() {
        double canCoderAngle = getCanCoderDegreesWithOffset();
        double absolutePosition = Conversions.degreesToFalcon(
                                                canCoderAngle,
                                                SwerveConfig.angleGearRatio);
        mAngleMotor.setSelectedSensorPosition(absolutePosition);
        lastAngle = Rotation2d.fromDegrees( canCoderAngle );
    }

    public void setLastAngleToCurrentAngle(){
        lastAngle = getFalconAngle();
    }

    private void configAngleEncoder() {
        angleEncoder.configFactoryDefault();
        angleEncoder.configAllSettings(swerveConfig.swerveCanCoderConfig);
        angleEncoder.setStatusFramePeriod(CANCoderStatusFrame.VbatAndFaults, 249);
        angleEncoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData, 20);
    }

    private void configAngleMotor() {
        mAngleMotor.configFactoryDefault();
        mAngleMotor.configAllSettings(swerveConfig.swerveAngleFXConfig);
        mAngleMotor.setInverted(SwerveConfig.angleMotorInvert);
        mAngleMotor.setNeutralMode(SwerveConfig.angleNeutralMode);
        resetFalconToAbsolute();
    }

    private void configDriveMotor() {
        mDriveMotor.configFactoryDefault();
        mDriveMotor.configAllSettings(swerveConfig.swerveDriveFXConfig);
        mDriveMotor.setInverted(SwerveConfig.driveMotorInvert);
        mDriveMotor.setNeutralMode(SwerveConfig.driveNeutralMode);
        mDriveMotor.setSelectedSensorPosition(0);
    }

    // -------------------------------------
    // ---------- Logging Methods ----------
    // -------------------------------------

    public void setLoggingOn()	{ m_LogFlag = true; }
    public void setLoggingOff() { m_LogFlag = false; }
    
    public void logDescription(){
      	// Check to see if we should just exit and not log
        if (m_LogFlag == false)  { return; }
        line = "Timestamp,";
        line = "Type,";
        line = "Mod #,Mod Nm";
        line += "Desired Angle,Desired Speed,";
        line += "Orig Cancoder Angle,Orig Falcon Angle,";
        line += "Optimized Angle,Optimized Speed,";
        line += "Delta Angle,Output Angle,";
        line += "Actual Vel, FeedForward";
        //Robot.logger.appendLog(line);
    }

    // ----------- Log Drivetrain data --------------------
    public void logSwerveModuleData() {
    	// Check to see if we should just exit and not log
        if (m_LogFlag == false) { return; }
        //if (moduleNumber != 0)  { return; }

        // ---------- Build Print String -----------------
        line = "SwMod,";    // Record Type
        // ---------- Show Module Num and Name ------------
        line += moduleNumber + "," +moduleName;

        // ------------ Show Desired State ------------
        // line += "," + desiredAngle.getDegrees() + "," + desiredSpeed;
        // line += "," + mCANcoderAngle180CCW.getDegrees() + "," + currentAngle;
        // line += "," + optimizedAngle + "," + optimizedSpeed;
        // line += "," + delta + "," +  outputAngle;
        // line += "," + getDriveVelocityMPS() + "," + feedforward_value;
        // Robot.logger.appendLog(line);
    }

    // private void smartDashboardUpdate(){
    //     // Only needed for testing and tuning
    //     if (moduleNumber == 0) {
    //         SmartDashboard.putNumber("Desired Angle", desiredAngle.getDegrees());
    //         SmartDashboard.putNumber("Desired Speed", desiredSpeed);

    //         SmartDashboard.putNumber("Orig Cancoder Angle", mCANcoderAngle180CCW.getDegrees());
    //         SmartDashboard.putNumber("Orig Falcon Angle", currentAngle);

    //         SmartDashboard.putNumber("Optimized Angle", optimizedAngle);
    //         SmartDashboard.putNumber("Optimized Speed", optimizedSpeed);

    //         SmartDashboard.putNumber("Delta Angle", delta);
    //         SmartDashboard.putNumber("Output Angle", outputAngle);

    //         SmartDashboard.putNumber("Actual Vel", getDriveVelocityMPS());
    //         SmartDashboard.putNumber("FeedForward", feedforward_value);

    //         SmartDashboard.putNumber("Percent Output", percentOutput);
    //         SmartDashboard.putNumber("Falcon Sensor", rawFalconSensorData);
    //     }
    // }
    
}
