package frc.robot.elevator.commands;

import frc.robot.Robot;
import frc.robot.arm.ArmConfig;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.elevator.ElevatorConfig;

// This Command Allows the Arm to Fall after elevator has raised up to a safe height
// After the Arm has reached the retract limit switch the elevator can then lower to bottom
// The arm will hold at the Store position until the elevator has reached the bottom
// It then exits and the default command should then pick up holding the arm at this position

public class ElevReleaseArmCmd extends CommandBase {
    
    static enum CmdState {ELEVRAISING, ELEVLOWERING, SETTLING, DONE};
    CmdState cmdState = CmdState.ELEVRAISING;
    Timer settleTimer = new Timer();
    double settleTime = 0.25;       // Time to allow elevator to settle onto bottom

    // Command Constructor
    public ElevReleaseArmCmd() {
        addRequirements(Robot.elevator);
    }

    @Override
    public void initialize() {
        cmdState = CmdState.ELEVRAISING;
        Robot.elevator.setBrakeMode(true);                              // Turn on brake mode
        Robot.elevator.setMMheight(ElevatorConfig.ElevArmReleaseHt);    // Get Elev Moving up
    }

    @Override
    public void execute() {
        // Raise Elevator to clear arm and let fall

        if (cmdState == CmdState.ELEVRAISING) {
            // Lets look to see when it hits the switch on way down
            if (Robot.arm.isRetractLimitSwitchPressed()) {
                // Arm has fallen enough to be seen by retract limit switch
                cmdState = CmdState.ELEVLOWERING;
            } else {
                Robot.elevator.setMMheight(ElevatorConfig.ElevArmReleaseHt);    // Keep on Moving elev up
                return;
            }
        }

        if (cmdState == CmdState.ELEVLOWERING) {
            if (Robot.elevator.isLowerLimitSwitchPressed()) {
                // the elevator has reached the bottom
                cmdState = CmdState.SETTLING;
                settleTimer.reset();
                settleTimer.start();
                return;
            } else {
                // Continue to lower elev
                Robot.elevator.elevLower();
                return;
            }
        }

        if (cmdState == CmdState.SETTLING) {
            if (settleTimer.get() <= settleTime) {
                // Continue Letting Elev fall the last inch
                Robot.elevator.elevStop();
            } else {
                cmdState = CmdState.DONE;
                Robot.elevator.resetEncoder();      // Zero out Encoder we are now at bottom
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        Robot.elevator.elevStop();
        Robot.elevator.setBrakeMode(true);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (cmdState == CmdState.DONE) { return true; }
        return false;
    }
}

// 37
// 28.8
