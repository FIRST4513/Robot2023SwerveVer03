package frc.robot.grabber;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

public class GrabberCmds {

    public static void setupDefaultCommand() {
        // not really needed
        //Robot.grabber.setDefaultCommand(stop());
    }

    // -------- Open/Close to Preset Positions --------
    public static Command grabberOpenHalfCmd() {
        return new RunCommand(() -> Robot.grabber.setGrabberServoToCtrPosition(), Robot.grabber);
    }    
    public static Command grabberOpenFullyCmd() {
        return new RunCommand(() -> Robot.grabber.setGrabberServoToOpenPosition(), Robot.grabber);
    }  

    public static Command grabberCloseFullyCmd() {
        return new RunCommand(() -> Robot.grabber.setGrabberServoToClosedPosition(), Robot.grabber);
    }  

    // ---------- Open/Close by joystick ------------
    public static Command grabberOpenCmd() {
        return new RunCommand(() -> Robot.grabber.grabberOpen(), Robot.grabber);
    }  
    public static Command grabberCloseCmd() {
        return new RunCommand(() -> Robot.grabber.grabberClose(), Robot.grabber);
    }  

    // ---------- Open/Close to a given position ------------
    public static Command openToPositionCmd(double pos) {
        return new RunCommand(() -> Robot.grabber.setGrabberServoToPosition(pos), Robot.grabber);
    }

    // ----------- Disable Servo -----------------
    public static Command stop() {
        return new RunCommand(() -> Robot.grabber.stop(), Robot.grabber);
    }
}
