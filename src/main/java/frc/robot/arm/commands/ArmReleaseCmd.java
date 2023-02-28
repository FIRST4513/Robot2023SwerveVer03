package frc.robot.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;

// This Command Allows th e Arm to Fall after elevator has raised up
// The arm will hold at the Store position until the elevator has reached the bottom
// It then exits and the default command should then pick up holding the arm at this position

public class ArmReleaseCmd extends CommandBase {
    
    static enum CmdState {FALLING, HOLDING, DONE};
    CmdState cmdState = CmdState.FALLING;

    // Command Constructor
    public ArmReleaseCmd() {
        addRequirements(Robot.arm);
    }

    @Override
    public void initialize() {
        cmdState = CmdState.FALLING;
        Robot.arm.setBrakeMode(false);          // Turn off Arm Brake mode to let it fall when elev high enough
        Robot.arm.resetEncoderAngle(ArmConfig.ArmAngleFullRetractPos);     // Initialize motor encoder                                
    }

    @Override
    public void execute() {

        if (cmdState == CmdState.FALLING) {
            if (Robot.arm.isRetractLimitSwitchPressed() ) {
                // The Arm has now reached the retracted switch
                cmdState = CmdState.HOLDING;
                Robot.arm.setBrakeMode(true);
                Robot.arm.setMMangle( ArmConfig.ArmAngleStorePos );     // Move and hold at store pos
                return;
            } else {
                // Were still waiting for Arm to fall to the switch (keep waiting)
                return;
            }
        }
        if (cmdState == CmdState.HOLDING) {
            if (Robot.elevator.getElevHeightInches() < 1.0 ) {
                // The Elev has now lowered back down to almost bottom
                cmdState = CmdState.DONE;
                return;
            } else {
                // Were still waiting for Elev to get to the bottom
                return;
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        Robot.arm.setBrakeMode(true);  // Turn on Arm Brake mode
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (cmdState == CmdState.DONE) {
            return true; 
        }
        return false;
    }
}