package frc.lib.subsystems.angleMech;

import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.lib.subsystems.MotorFXSubsystem;

public abstract class AngleMechSubsystem extends MotorFXSubsystem {
    public AngleMechConfig config;

    public AngleMechSubsystem(AngleMechConfig config) {
        super(config);
        this.config = config;
    }

    public void setMMPosition(double position) {
        motorLeader.set(ControlMode.MotionMagic, position);
    }

    public void enableSoftLimits(boolean fwdEnable, boolean revEnable) {
        motorLeader.configForwardSoftLimitEnable(fwdEnable);
        motorLeader.configReverseSoftLimitEnable(revEnable);
    }
}
