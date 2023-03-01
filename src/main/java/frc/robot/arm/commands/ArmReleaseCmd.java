package frc.robot.arm.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;

// This Command Allows th e Arm to Fall after elevator has raised up
// The arm will hold at the Store position until the elevator has reached the bottom
// It then exits and the default command should then pick up holding the arm at this position

public class ArmReleaseCmd extends CommandBase {
    
    static enum CmdState {FALLING, ONLIMITSW, DONE};
    CmdState cmdState = CmdState.FALLING;

    Timer cmdTimer = new Timer();
    double time, angle;

    // Command Constructor
    public ArmReleaseCmd() {
        addRequirements(Robot.arm);
    }

    @Override
    public void initialize() {
        cmdTimer.reset();
        cmdTimer.start();
        cmdState = CmdState.FALLING;
        Robot.arm.setBrakeMode(false);                                     // Turn off Arm Brake mode to let it fall
        Robot.arm.resetEncoderAngle(ArmConfig.ArmAngleFullRetractPos);     // Initialize motor encoder to back angle                               
    }

    @Override
    public void execute() {
        time = cmdTimer.get();
        angle = Robot.arm.getArmAngle();

        if ( (cmdState == CmdState.FALLING) && Robot.arm.isRetractLimitSwitchPressed() ) {
            // The Arm has now reached the retract switch we can end now.
            // The Elevator lower will end also at this condition
            cmdState = CmdState.ONLIMITSW;
            Robot.arm.setBrakeMode(true);
            System.out.println("----- Arm Release Command Now On Limit Sw  Angle = " + time + " Angle = " + angle);
            return;
        }
        if ( (cmdState == CmdState.ONLIMITSW) && !Robot.arm.isRetractLimitSwitchPressed() ) {
            // The Arm has now reached the retract switch we can end now.
            // The Elevator lower will end also at this condition
            cmdState = CmdState.DONE;
            System.out.println("----- Arm Release Command Now Off Limit Sw  Angle = " + time + " Angle = " + angle);
            return;
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