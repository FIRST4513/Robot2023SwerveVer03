package frc.lib.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.motorControllers.TalonSRXSetup;

public class FollowerTalonSRX extends SubsystemBase {
    protected WPI_TalonSRX motorFollower;
    protected WPI_TalonSRX leader;

    public FollowerTalonSRX(
            int followerID,
            String canBus,
            TalonSRXConfiguration config,
            boolean inverted,
            WPI_TalonSRX leader) {             
        motorFollower = new WPI_TalonSRX(followerID);
        TalonSRXSetup.configFollowerSetup(motorFollower, config);
        motorFollower.setInverted(inverted);
        this.leader = leader;
    }

    public FollowerTalonSRX(
            int followerID, TalonSRXConfiguration config, boolean inverted, WPI_TalonSRX leader) {
        this(followerID, "rio", config, inverted, leader);
    }

    @Override
    public void periodic() {
        motorFollower.follow(leader);
    }

    public WPI_TalonSRX get() {
        return motorFollower;
    }
}
