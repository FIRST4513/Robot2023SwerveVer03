package frc.robot.elevator.commands;

import frc.robot.Robot;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.arm.ArmConfig;
import frc.robot.elevator.ElevatorConfig;

public class ElevReleaseArmCmd extends CommandBase {
    
    static enum CmdState {ELEVRAISING, ARMONSWITCH, DONE};
    CmdState cmdState = CmdState.ELEVRAISING;

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
        Robot.elevator.setMMheight(ElevatorConfig.ElevInitReleaseHt);

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
                cmdState = CmdState.DONE;
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        Robot.elevator.elevHoldMtr();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (cmdState == CmdState.DONE) { return true; }
        return false;
    }
}