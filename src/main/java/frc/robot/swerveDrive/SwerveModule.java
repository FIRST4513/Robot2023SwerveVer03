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

public class SwerveModule extends SubsystemBase {
    public String moduleName;
    public int moduleNumber;
    private double angleOffset;
    private WPI_TalonFX mAngleMotor;
    private WPI_TalonFX mDriveMotor;
    private WPI_CANCoder angleEncoder;
    private double lastAngle;
    private SwerveDriveConfig swerveConfig;
    private SwerveModuleState mSwerveModState = new SwerveModuleState();
    private SwerveModulePosition mSwerveModPosition = new SwerveModulePosition();
    private Rotation2d mCANcoderAngle360CCW = new Rotation2d();    
    private Rotation2d mCANcoderAngle180CCW = new Rotation2d();

    SimpleMotorFeedforward feedforward =
            new SimpleMotorFeedforward(
                    SwerveDriveConfig.driveKS, SwerveDriveConfig.driveKV, SwerveDriveConfig.driveKA);

    // ------------- Constructor ------------
    public SwerveModule(
                int moduleNumber, SwerveDriveConfig swerveConfig, SwerveModuleConfig moduleConfig) {
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

        lastAngle = getFalconAngle();
    }

    // ------------ Periodic Update of Wheel State Info. -----------
    @Override
    public void periodic() {
        updateSwerveModuleState();              // Drive Mtr Vel (mps) & Angle Motor Angle
        updateSwerveModulePosition();           // Drive Mtr Dist & Angle Motor Angle
        updateCANcoderAngles();                 // CanCoder Angles (360 and 180 values)
    }


    // -------------------------------------------------------------------------------
    // --------------------------- Drive Motors Methods ------------------------------
    // -------------------------------------------------------------------------------

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {

        // ---------------------- Step 1   Optimize Wheel Angle ----------------------
        // Wheel only needs to rotate a max of 90 degrees to achieve any direction
        Rotation2d currentAngle = mSwerveModState.angle;
        desiredState = SwerveModuleState.optimize(desiredState, currentAngle);
        Rotation2d desiredAngle = desiredState.angle;

        // ---------------------- Step 2   Correct Steer Angle ----------------------
        // Calculate the correct angle to steer the wheel and correct for CTRE controller
        // being continuous +360 -> -360.   

        double delta = desiredAngle.getDegrees() - currentAngle.getDegrees();
        // Converts a delta +-360 to +-180
        if (delta > 180) {
            delta = (delta - 360);
        } else if (delta < -180) {
            delta = (delta + 360);
        }
        double outputAngle = getFalconAngle() + delta;

        // ------------------------- Step 3 Reduce wheel angle Jitter  --------------------------
        if ((Math.abs(desiredState.speedMetersPerSecond) < (SwerveDriveConfig.maxVelocity * 0.05))) {
            outputAngle = lastAngle;
        }

        // --------------------------- Step 4 Set Angle Motor  ----------------------------
        // Uses internal PID to set/hold angle motor at correct position
        mAngleMotor.set(
                ControlMode.Position,
                Conversions.degreesToFalcon(outputAngle, SwerveDriveConfig.angleGearRatio));
        lastAngle = outputAngle;

        // --------------------------- Step 5 Set Drive Motor  ----------------------------
        // Calculate the velocity of the module and send to motor
        if (isOpenLoop) {
            // MPS convert to -1 to +1 Value
            // no PID, we just send a power amount
            double percentOutput = desiredState.speedMetersPerSecond / SwerveDriveConfig.maxVelocity;
            mDriveMotor.set(ControlMode.PercentOutput, percentOutput);
        } else {
            // uses internal PID to maintain accurate velocity
            // possible PID value or feedforward values causing jitter and creep errors?
            double velocity = Conversions.MPSToFalcon(
                                    desiredState.speedMetersPerSecond,
                                    SwerveDriveConfig.wheelCircumference,
                                    SwerveDriveConfig.driveGearRatio);
            mDriveMotor.set(
                    ControlMode.Velocity,
                    velocity,
                    DemandType.ArbitraryFeedForward,
                    feedforward.calculate(desiredState.speedMetersPerSecond));
        }
    }

    public void stop() {
        mDriveMotor.stopMotor();
        mAngleMotor.stopMotor();
    }

    public void setVoltage(double voltage) {
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
     
        Rotation2d angle = Rotation2d.fromDegrees( Conversions.falconToDegrees(
                                // Coversion = Enc-Pos(cnts) * (360.0 / (gearRatio * 2048.0))
                                // This is continous > 360 degrees to < -360 degrees
                                mAngleMotor.getSelectedSensorPosition(),
                                SwerveDriveConfig.angleGearRatio)
                                );

        mSwerveModState = new SwerveModuleState(velocity, angle);
    }

    public SwerveModuleState getState() {
        // used by swerve drive odometry
        return mSwerveModState;
    }


    // -------------------------------------------------------------------------------
    // ---------------------- Wheel CANcoder Angle Methods ---------------------------
    // -------------------------------------------------------------------------------
        
    private void updateCANcoderAngles() {
        mCANcoderAngle360CCW = Rotation2d.fromDegrees( getCANcoderAngle360() );
        mCANcoderAngle180CCW = Rotation2d.fromDegrees( getCANcoderAngle180() );
    }

    public double getCANcoderAngle360(){
        // Returns CANcoder angle with offset applied, continous 0 to 360 degrees (CCW).
        // 0 Degrees is wheel staight forward
        double angle = getCANcoderAngleAbsolute() - angleOffset;
        return convertAngleTo360CCW( angle );        
    }

    public double getCANcoderAngle180(){
        // Returns CANcoder angle with offset applied, continous 180 to -180 degrees (CCW).
        // 0 Degrees is wheel staight forward
        double angle = getCANcoderAngle360();
        return convertAngleTo180CCW( angle); 
    }
   
    public double getCANcoderAngleAbsolute(){
        // Returns CANcoder angle with NO offset applied, continous 0 to 360 degrees (CCW).
        double angle = angleEncoder.getAbsolutePosition();
        return angle;
    }

    double convertAngleTo360CCW(double angle){
        //Convert angle to continous 0-360 degrees (CCW).
        if (angle < 0) {
            angle = angle + 360;
        }
        return angle;
    }
    double convertAngleTo180CCW(double angle){
        //Convert angle with offset to +180(CCW) through -180 degrees (CW).
        if (angle > 180) {
            angle = 360 - angle;
        }
        return angle;
    }

    // -------------------------------------------------------------------------------
    // ------------------------ Angle Motor Angle Methods ----------------------------
    // -------------------------------------------------------------------------------

    public double getTargetAngle() {
        return lastAngle;
    }

    public double getFalconAngle() {
        // returns encoder.counts * (360.0 / (gearRatio * 2048.0));
        // Each encoder count = 0.00823125 degrees
        double falconDegrees = Conversions.falconToDegrees(
            mAngleMotor.getSelectedSensorPosition(), SwerveDriveConfig.angleGearRatio);
        
        // if (this.moduleNumber == 0) {
        //     SmartDashboard.putNumber("Falcon Degrees", falconDegrees);
        //     SmartDashboard.putNumber("SDC Gear Ratio", SwerveDriveConfig.angleGearRatio);
        //     SmartDashboard.putNumber("Angle Motor Sensor", mAngleMotor.getSelectedSensorPosition());
        // }

        return falconDegrees;
    }

    public String getName() {
        return moduleName;
    }


    // -------------------------------------------------------------------------------
    // -------------------------- Wheel Position Methods -----------------------------
    // -------------------------------------------------------------------------------
        
    private void updateSwerveModulePosition() {
        double position =
                Conversions.FalconToMeters(
                        mDriveMotor.getSelectedSensorPosition(),
                        SwerveDriveConfig.wheelCircumference,
                        SwerveDriveConfig.driveGearRatio);
        mSwerveModPosition = new SwerveModulePosition(position, mSwerveModState.angle);
    }

    public SwerveModulePosition getPosition() {
        return mSwerveModPosition;
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
        resetToAbsolute();
    }

    // Initialize Angle Motor Encoder based on absolute CanCoder Value
    // Called in configure Angle Motor at initial Constructor
    public void resetToAbsolute() {
        double absolutePosition = Conversions.degreesToFalcon(
                            getCANcoderAngle360(),
                            SwerveDriveConfig.angleGearRatio);
        mAngleMotor.setSelectedSensorPosition(absolutePosition);
    }

    private void configDriveMotor() {
        mDriveMotor.configFactoryDefault();
        mDriveMotor.configAllSettings(swerveConfig.swerveDriveFXConfig);
        mDriveMotor.setInverted(SwerveDriveConfig.driveMotorInvert);
        mDriveMotor.setNeutralMode(SwerveDriveConfig.driveNeutralMode);
        mDriveMotor.setSelectedSensorPosition(0);
    }

    public void setBrakeMode(Boolean enabled) {
        if (enabled) {
            mDriveMotor.setNeutralMode(NeutralMode.Brake);
            mAngleMotor.setNeutralMode(NeutralMode.Brake);
        } else {
            mDriveMotor.setNeutralMode(NeutralMode.Coast);
            mAngleMotor.setNeutralMode(NeutralMode.Coast);
        }
    }

}
