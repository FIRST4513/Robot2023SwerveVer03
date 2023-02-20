package frc.robot.elevator.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;
import frc.robot.elevator.ElevatorConfig;

public class ElevReleaseArmCmd extends CommandBase {
    
    Timer delayTimer = new Timer();
    static enum CmdState {RAISING, ONSWITCH, DONE};
    CmdState cmdState = CmdState.RAISING;

    // Command Constructor
    public ElevReleaseArmCmd() {
        addRequirements(Robot.elevator);
    }

    @Override
    public void initialize() {
        Robot.elevator.setMMheight(ElevatorConfig.ElevInitReleaseHt);

        delayTimer.reset();
        delayTimer.start();
    }

    @Override
    public void execute() {
        // Raise Elevator to clear arm and let fall
        Robot.elevator.setMMheight(ElevatorConfig.ElevInitReleaseHt);

        if (cmdState == CmdState.RAISING) {
            // Lets look to see when it hits the switch on way down
            if (Robot.arm.isLowerLimitSwitchPressed()) {
                cmdState = CmdState.ONSWITCH;
            }
        }
        if (cmdState == CmdState.ONSWITCH) {
            if (Robot.arm.isLowerLimitSwitchPressed()) {
                // Were still on the switch keep resetting the encoder
                Robot.arm.resetEncoder(ArmConfig.lowerSoftLimitPos);
            } else {
                // We have passed through switch, were done
                cmdState = CmdState.DONE;
            }
        }
    }

    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (cmdState == CmdState.DONE) { return true; }
        return false;
    }
}