package frc.robot.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;

public class ArmReleaseCmd extends CommandBase {
    
    static enum CmdState {WAITING, ARMONSWITCH, ARMOFFSWITCH, SETTLING, DONE};
    CmdState cmdState = CmdState.WAITING;

    // Command Constructor
    public ArmReleaseCmd() {
        addRequirements(Robot.arm);
    }

    @Override
    public void initialize() {
        cmdState = CmdState.WAITING;
        Robot.arm.setBrakeMode(false);          // Turn off Arm Brake mode
        Robot.arm.resetEncoderAngle(ArmConfig.ArmAngleFullRetractPos);     // Initialize motor encoder
        System.out.println("Arm Release Init - AbsEnc=" + Robot.arm.getAbsoluteArmAngle() +
                                         "     MtrAng=" + Robot.arm.getArmAngle());
        System.out.println("Arm Release Init - AbsVolt=" + Robot.arm.getAbsoluteArmVolt() +
                                          "    MtrEnc=" + Robot.arm.getEncoderCnt());                                         
    }

    @Override
    public void execute() {

        if (cmdState == CmdState.WAITING) {
            if (Robot.arm.isRetractLimitSwitchPressed() ) {
                // The Arm has now reached the retracted switch
                cmdState = CmdState.ARMONSWITCH;
                System.out.println("Arm Release OnSwitch - AbsEnc=" + Robot.arm.getAbsoluteArmAngle() +
                                                    "     MtrEnc=" + Robot.arm.getArmAngle());
                System.out.println("Arm Release OnSwitch - AbsVolt=" + Robot.arm.getAbsoluteArmVolt() +
                                                    "    MtrEnc=" + Robot.arm.getEncoderCnt());  
                return;
            } else {
                // Were still waiting for Arm to fall to the switch (keep waiting)
                return;
            }
        }
        if (cmdState == CmdState.ARMONSWITCH) {
            if (Robot.arm.isRetractLimitSwitchPressed() == false) {
                // We have now dropped farther and cleared the switch
                //Robot.arm.resetEncoder(ArmConfig.RetractLimitSwitchAngle);      // Zero out Encoder we are now at bottom
                cmdState = CmdState.SETTLING;
                System.out.println("Arm Release Settling - AbsEnc=" + Robot.arm.getAbsoluteArmAngle() +
                                                    "     MtrEnc=" + Robot.arm.getArmAngle());
                System.out.println("Arm Release Settling - AbsVolt=" + Robot.arm.getAbsoluteArmVolt() +
                                                    "    MtrEnc=" + Robot.arm.getEncoderCnt());  

                return;
            } else {
                // Were still On switch so keep on falling
                return;
            }
        }
        if (cmdState == CmdState.SETTLING) {
            if (Robot.arm.getArmAngle() < -20.0 ) {
                // The arm is not down far enough so keep on falling
                return;
            } else {
                // The Arm has fallen far enough so we can get out
                // Hold will take over after this (default cmd)
                cmdState = CmdState.DONE;
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
            System.out.println("Arm Release Done - AbsEnc=" + Robot.arm.getAbsoluteArmAngle() +
                                             "     MtrAng=" + Robot.arm.getArmAngle());
            System.out.println("Arm Release Done - AbsVolt=" + Robot.arm.getAbsoluteArmVolt() +
                                              "    MtrEnc=" + Robot.arm.getEncoderCnt());  
            return true; 
        }
        return false;
    }
}