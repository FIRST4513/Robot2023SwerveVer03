package frc.lib.subsystems.linearMech;

import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.lib.subsystems.MotorFXSubsystem;

public abstract class LinearMechSubsystem extends MotorFXSubsystem {
    public LinearMechConfig config;

    public LinearMechSubsystem(LinearMechConfig config) {
        super(config);
        this.config = config;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public void setManualOutput(double speed) {
        motorLeader.set(speed);
    }

    public void setMMPosition(double position) {
        motorLeader.set(ControlMode.MotionMagic, position);
    }

    public void enableSoftLimits(boolean fwdEnable, boolean revEnable) {
        motorLeader.configForwardSoftLimitEnable(fwdEnable);
        motorLeader.configReverseSoftLimitEnable(revEnable);
    }
}
