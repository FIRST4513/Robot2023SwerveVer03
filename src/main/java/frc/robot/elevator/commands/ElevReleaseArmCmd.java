package frc.robot.elevator.commands;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.arm.ArmConfig;
import frc.robot.elevator.ElevatorConfig;

public class ElevReleaseArmCmd extends CommandBase {
    
    static enum CmdState {ELEVRAISING, ARMONSWITCH, ELEVLOWERING, SETTLING, DONE};
    CmdState cmdState = CmdState.ELEVRAISING;
    Timer settleTimer;
    double settleTime = 0.25;       // Time to allow elevator to settle onto bottom

    // Command Constructor
    public ElevReleaseArmCmd() {
        addRequirements(Robot.elevator);
    }

    @Override
    public void initialize() {
        cmdState = CmdState.ELEVRAISING;
    }

    @Override
    public void execute() {
        // Raise Elevator to clear arm and let fall
        Robot.elevator.setMMheight(ElevatorConfig.ElevArmReleaseHt);

        if (cmdState == CmdState.ELEVRAISING) {
            // Lets look to see when it hits the switch on way down
            if (Robot.arm.isRetractLimitSwitchPressed()) {
                cmdState = CmdState.ARMONSWITCH;
            }
        }
        if (cmdState == CmdState.ARMONSWITCH) {
            if (Robot.arm.isRetractLimitSwitchPressed()) {
                // Were still on the switch keep resetting the encoder
                Robot.arm.resetEncoderAngle(ArmConfig.RetractLimitSwitchAngle);
            } else {
                // We have passed through switch, were done
                cmdState = CmdState.ELEVLOWERING;
            }
        }
        if (cmdState == CmdState.ELEVLOWERING) {
            if (Robot.elevator.isLowerLimitSwitchPressed()) {
                cmdState = CmdState.SETTLING;
                settleTimer.reset();
                settleTimer.start();
                return;
            } else {
                Robot.elevator.elevLower();
                return;
            }
        }
        if (cmdState == CmdState.SETTLING) {
            if (settleTimer.get() <= settleTime) {
                // Continue Lowering Elev
                Robot.elevator.elevLower();
            } else {
                cmdState = CmdState.DONE;
                Robot.elevator.resetEncoder();      // Zero out Encoder we are now at bottom
                Robot.elevator.setBrakeMode(true);  // Turn on brake mode
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        Robot.elevator.elevStop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (cmdState == CmdState.DONE) { return true; }
        return false;
    }
}