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
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.swerve.SwerveModuleConfig;
import frc.lib.util.Conversions;

public class SwerveModule extends SubsystemBase {
    public int moduleNumber;
    private double angleOffset;
    private WPI_TalonFX mAngleMotor;
    private WPI_TalonFX mDriveMotor;
    private WPI_CANCoder angleEncoder;
    private double lastAngle;
    private SwerveDriveConfig swerveConfig;
    private SwerveModuleState mSwerveModState = new SwerveModuleState();
    private SwerveModulePosition mSwerveModPosition = new SwerveModulePosition();
    private Rotation2d mCANcoderAngle = new Rotation2d();

    SimpleMotorFeedforward feedforward =
            new SimpleMotorFeedforward(
                    SwerveDriveConfig.driveKS, SwerveDriveConfig.driveKV, SwerveDriveConfig.driveKA);

    // ------------- Constructor ------------
    public SwerveModule(
        int moduleNumber, SwerveDriveConfig swerveConfig, SwerveModuleConfig moduleConfig) {
        this.moduleNumber = moduleNumber;
        this.swerveConfig = swerveConfig;
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
        mSwerveModState = getCANState();        // Drive Mtr Vel (mps) & Angle Motor Angle (rad)
        mSwerveModPosition = getCANPosition();  // Drive Mtr Dist & Angle Motor Angle
        mCANcoderAngle = getCANcoderAngle();    // CanCoder Absolute Position
    }


    // -------------------------------------------------------------------------------
    // --------------------------- Drive Motors Methods ------------------------------
    // -------------------------------------------------------------------------------

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {

        // ---------------------- Step 1   Optimize Wheel Angle ----------------------
        // Wheel will only need to rotate a max of 90 degrees to achieve any direction
        Rotation2d currentAngle = getState().angle;
        desiredState = SwerveModuleState.optimize(desiredState, currentAngle);

        // ---------------------- Step 2   Correct Steer Angle ----------------------
        // Calculate the correct angle to steer the wheel and correct for CTRE controller
        // not being continuous.   Why not use CanCoder (internal PID) ???????
        Rotation2d desiredAngle = desiredState.angle;
        double delta = desiredAngle.getDegrees() - currentAngle.getDegrees();
        if (delta > 180) {
            delta = (delta - 360);
        } else if (delta < -180) {
            delta = (delta + 360);
        }
        double outputAngle = getFalconAngle() + delta;

        // ------------------------- Step 3 Reduce wheel angle Jitter  --------------------------
        if ((Math.abs(desiredState.speedMetersPerSecond) < (SwerveDriveConfig.maxVelocity * 0.01))) {
            outputAngle = lastAngle;
        }

        // --------------------------- Step 4 Set Angle Motor  ----------------------------
        mAngleMotor.set(
                ControlMode.Position,
                Conversions.degreesToFalcon(outputAngle, SwerveDriveConfig.angleGearRatio));
        lastAngle = outputAngle;

        // --------------------------- Step 5 Set Drive Motor  ----------------------------
        // Calculate the velocity of the module and send to motor
        if (isOpenLoop) {
            double percentOutput = desiredState.speedMetersPerSecond / SwerveDriveConfig.maxVelocity;
            mDriveMotor.set(ControlMode.PercentOutput, percentOutput);
        } else {
            double velocity =
                    Conversions.MPSToFalcon(
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

    // Initialize Angle Motor Encoder based on absolute CanCoder Value
    // Called in configure Angle Motor at initial Constructor
    public void resetToAbsolute() {
        double offset = angleOffset;
        double absolutePosition =
                Conversions.degreesToFalcon(
                        getAbsoluteAngle().getDegrees() - offset, SwerveDriveConfig.angleGearRatio);
        mAngleMotor.setSelectedSensorPosition(absolutePosition);
    }


    // -------------------------------------------------------------------------------
    // ---------------------------- Module State Methods -----------------------------
    // -------------------------------------------------------------------------------

    public SwerveModuleState getState() {
        return mSwerveModState;
    }

    private SwerveModuleState getCANState() {
        double velocity =
                Conversions.falconToMPS(
                        mDriveMotor.getSelectedSensorVelocity(),
                        SwerveDriveConfig.wheelCircumference,
                        SwerveDriveConfig.driveGearRatio);
        Rotation2d angle =
                Rotation2d.fromDegrees(
                        // counts * (360.0 / (gearRatio * 2048.0));
                        Conversions.falconToDegrees(
                                mAngleMotor.getSelectedSensorPosition(),
                                SwerveDriveConfig.angleGearRatio));
        return new SwerveModuleState(velocity, angle);
    }


    // -------------------------------------------------------------------------------
    // ---------------------------- Wheel Angle Methods ------------------------------
    // -------------------------------------------------------------------------------
        
    private Rotation2d getCANcoderAngle() {
        Rotation2d position = Rotation2d.fromDegrees(angleEncoder.getAbsolutePosition());
        return position;
    }

    public Rotation2d getAbsoluteAngle() {
        return mCANcoderAngle;
    }

    public double getTargetAngle() {
        return lastAngle;
    }

    public double getFalconAngle() {
        return Conversions.falconToDegrees(
                mAngleMotor.getSelectedSensorPosition(), SwerveDriveConfig.angleGearRatio);
    }


    // -------------------------------------------------------------------------------
    // -------------------------- Wheel Position Methods -----------------------------
    // -------------------------------------------------------------------------------
        
    private SwerveModulePosition getCANPosition() {
        double position =
                Conversions.FalconToMeters(
                        mDriveMotor.getSelectedSensorPosition(),
                        SwerveDriveConfig.wheelCircumference,
                        SwerveDriveConfig.driveGearRatio);
        return new SwerveModulePosition(position, mSwerveModState.angle);
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
        mCANcoderAngle = getCANcoderAngle();
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
