package frc.robot.auto.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.Rmath;
import frc.robot.Robot;

public class ArmElevDriveCmd extends CommandBase {
    double angle = 0;
    double currArmAngle, currElevHt;
    double tgtArmAngle, tgtElevHt, timeOut;
    boolean elevTripped, armTripped;

    Timer cmdTmr = new Timer();
    double time;

    public ArmElevDriveCmd(double armAngle, double elevHt, double to) {
        addRequirements(Robot.arm);
        addRequirements(Robot.elevator);
        tgtArmAngle = armAngle;
        tgtElevHt = elevHt;
        timeOut = to;

    }

    @Override
    public void initialize() {
        cmdTmr.reset();
        cmdTmr.start();
        elevTripped = false;
        armTripped = false;
        time = Rmath.mRound(cmdTmr.get(), 2);
        System.out.println("ArmElevDriveCmd Started  (ArmTgtAngle=" +
            tgtArmAngle + "   ElevTgtHt=" + tgtElevHt + "  time=" + cmdTmr.get() + " )" );
    }

    @Override
    public void execute() {
        currArmAngle = Robot.arm.getArmAngle();
        currElevHt = Robot.elevator.getElevHeightInches();
        time = Rmath.mRound(cmdTmr.get(), 2);

        Robot.arm.setMMangle(tgtArmAngle);
        Robot.elevator.setMMheight(tgtElevHt);

        if (isElevAtPosition() && !elevTripped) {
            elevTripped = true; // Only want to trip first condition
            System.out.println("ArmElevDriveCmd Elevator Is at position  (ArmTgtAngle=" +
                        tgtArmAngle + "   ElevTgtHt=" + tgtElevHt + "  time=" + time + " )" );
        }
        if (isArmAtPosition() && !armTripped) {
            armTripped = true; // Only want to trip first condition
            System.out.println("ArmElevDriveCnd Arm Is at position  (ArmTgtAngle=" +
                        tgtArmAngle + "   ElevTgtHt=" + tgtElevHt + "  time=" + time + " )" );
        }
    }

    @Override
    public void end(boolean interrupted) {
        Robot.arm.stopArm();
        Robot.elevator.elevStop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // If both are at target were done
        if( elevTripped && armTripped ) {
            System.out.println("ArmElevDriveCnd Completed  (ArmAngle=" + 
                            currArmAngle + "   ElevtHt=" + currElevHt+")" + "   Time=" + time );
            return true;
        }
        if (cmdTmr.get() > timeOut) {
            System.out.println("ArmElevDriveCnd Timed Out !   (ArmAngle=" +
                            currArmAngle + "   ElevtHt=" + currElevHt+")" + "   Time=" + time );
            return true;
        }
        return false;
    }

    boolean isElevAtPosition(){
        // Is Elevator within 0.5 inches of target
        if ( Math.abs(tgtElevHt - currElevHt)   <= 0.5) {
            return true;
        }
        return false;
    }

    boolean isArmAtPosition(){
        // Is Arm within 2 degrees of target
        if ( Math.abs(tgtArmAngle - currArmAngle)   <= 2.0) {
            return true;
        }
        return false;
    }
}
