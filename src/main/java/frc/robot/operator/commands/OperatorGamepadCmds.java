package frc.robot.operator.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.intake.commands.IntakeCmds;

public class OperatorGamepadCmds {
    
    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
       // Robot.pilotGamepad.setDefaultCommand(RumbleOperatorCmd(0));
    }

    // ------------- Arm/Elev by TeleOp Commands ---------------
    public static Command ControlArmByJoysticksCmd() {
        return ArmCmds.ArmByJoystickCmd();
    }

    public static Command ControlElevByJoysticksCmd() {
        return ElevatorCmds.ElevByJoystickCmd();
    }

    public static Command ControlArmElevByJoysticksCmd() {
        return new ParallelCommandGroup(
            ArmCmds.ArmByJoystickCmd(),
            ElevatorCmds.ElevByJoystickCmd()
        );
    }

    // ------------- Intake Commands ----------------------------
    public static Command IntakeCubeCmd(){
        return new ParallelCommandGroup(
            OperatorGamepadCmds.SetArmElevToIntakeCubePosCmd(),
            IntakeCmds.IntakeCubeCmd()
        );
    }

    public static Command IntakeConeCmd(){
        return new ParallelCommandGroup(
            OperatorGamepadCmds.SetArmElevToIntakeConePosCmd(),
            IntakeCmds.IntakeConeCmd()
        );
    }

    // GENERAL STRUCTURE OF MOVE TO POSITION COMMANDS:
    // If moving from out-out, then parallel motion used
    // If moving from in-to-out or out-to-in, then check for cube
    //    If cube there: avoid bumper collision
    //    If not, parallel motion used

    
    // --------------------------- Arm/Elev To Intake Positions --------------------

    public static Command SetArmElevToIntakeCubePosCmd() {
        return new ParallelCommandGroup(        
            ElevatorCmds.ElevToIntakeCubePosCmd(),
            ArmCmds.ArmToIntakeCubePosCmd());
    }
    
    public static Command SetArmElevToIntakeConePosCmd() {
        return new ParallelCommandGroup(        
            ElevatorCmds.ElevToIntakeConePosCmd(),
            ArmCmds.ArmToIntakeConePosCmd());
    }

    // -------------------- Arm/Elev To Eject Positions -------------------
    
    public static Command SetArmElevToEjectLowPosCmd() {
        return new ConditionalCommand(
            // True condition: arm outside robot, no worry of bumper collision (out-to-out movement)
            new ParallelCommandGroup(
                ElevatorCmds.ElevToEjectLowPosCmd(),
                ArmCmds.ArmToEjectLowPosCmd()
            ),
            // False condition: arm inside robot, check for cube
            new ConditionalCommand(
                // True condition: no cube, good to go, parallel motion used
                new ParallelCommandGroup(
                    ElevatorCmds.ElevToEjectLowPosCmd(),
                    ArmCmds.ArmToEjectLowPosCmd()
                ),
                // False condition: cube, must avoid bumper collision;
                // raise elevator for clearance, set arm, then move elev back to correct pos
                new SequentialCommandGroup(
                    ElevatorCmds.ElevToBumperClearPosCmd(),
                    new ParallelCommandGroup(
                        ArmCmds.ArmToEjectLowPosCmd(),
                        ElevatorCmds.ElevHoldCmd().until(() -> Robot.arm.isMMtargetReached())
                    ),
                    ElevatorCmds.ElevToEjectLowPosCmd()
                ),
                () -> Robot.intake.isCubeEjectNotDetected()),
            // Condition: is arm outside?
            () -> Robot.arm.isArmOutside()
        );
    }

    public static Command SetArmElevToEjectMidPosCmd() {
        System.out.println("Command Activated");
        return new ConditionalCommand(
            // True condition: arm outside robot, no worry of bumper collision (out-to-out movement)
            new SequentialCommandGroup(
                ElevatorCmds.ElevToEjectMidPosCmd(),
                ArmCmds.ArmToEjectMidPosCmd()
            ),
            // False condition: arm inside robot, check for cube
            new ConditionalCommand (
                // True condition: no cube, good to go, parallel motion used
                new ParallelCommandGroup(
                    ElevatorCmds.ElevToEjectMidPosCmd(),
                    ArmCmds.ArmToEjectMidPosCmd()
                ),
                // False condition: cube, must avoid bumper collision;
                // raise elevator for clearance, set arm, then move elev back to correct pos
                new SequentialCommandGroup(
                    ElevatorCmds.ElevToBumperClearPosCmd(),
                    new ParallelCommandGroup(
                        ArmCmds.ArmToEjectMidPosCmd(),
                        ElevatorCmds.ElevHoldCmd().until(() -> Robot.arm.isMMtargetReached())
                    ),
                    ElevatorCmds.ElevToEjectMidPosCmd()
                ),
                () -> Robot.intake.isCubeEjectNotDetected()),
            // Condition: is arm outside?
            () -> Robot.arm.isArmOutside()
        );

    }

    public static Command SetArmElevToEjectHighPosCmd() {
        return new ConditionalCommand(
            // True condition: arm outside robot, no worry of bumper collision (out-to-out movement)
            new ParallelCommandGroup(
                ElevatorCmds.ElevToEjectHighPosCmd(),
                ArmCmds.ArmToEjectHighPosCmd()
            ),
            // False condition: arm inside robot, check for cube
            new ConditionalCommand(
                // True condition: no cube, good to go, parallel motion used
                new ParallelCommandGroup(
                    ElevatorCmds.ElevToEjectHighPosCmd(),
                    ArmCmds.ArmToEjectHighPosCmd()
                ),
                // False condition: cube, must avoid bumper collision;
                // raise elevator for clearance, set arm, then move elev back to correct pos
                new SequentialCommandGroup(
                    ElevatorCmds.ElevToBumperClearPosCmd(),
                    new ParallelCommandGroup(
                        ArmCmds.ArmToEjectHighPosCmd(),
                        ElevatorCmds.ElevHoldCmd().until(() -> Robot.arm.isMMtargetReached())
                    ),
                    ElevatorCmds.ElevToEjectHighPosCmd()
                ),
                () -> Robot.intake.isCubeEjectNotDetected()),
            // Condition: is arm outside?
            () -> Robot.arm.isArmOutside()
        );
    }


    // -------------------- Arm/Elev to Retracted Positions ---------------

    public static Command SetArmElevToStorePosCmd() {
        return new ConditionalCommand(
            // True condition: arm inside robot, no worry of bumper collision
            new ParallelCommandGroup(
                ElevatorCmds.ElevToStorePosCmd(),
                ArmCmds.ArmToStorePosCmd()
            ),
            // False condition: arm outside robot, check for cube
            new ConditionalCommand(
                // True condition: no cube, good to go, parallel motion used
                new ParallelCommandGroup(
                    ElevatorCmds.ElevToStorePosCmd(),
                    ArmCmds.ArmToStorePosCmd()
                ),
                // False condition: cube, must avoid bumper collision;
                // raise elevator for clearance, set arm, then move elev back to correct pos
                new SequentialCommandGroup(
                    ElevatorCmds.ElevToBumperClearPosCmd(),
                    new ParallelCommandGroup(
                        ArmCmds.ArmToStorePosCmd(),
                        ElevatorCmds.ElevHoldCmd().until(() -> Robot.arm.isMMtargetReached())
                    ),
                    // new DelayCmd(1.0),
                    ElevatorCmds.ElevToStorePosCmd()
                ),
                () -> Robot.intake.isCubeEjectNotDetected()),
            // Condition: is arm outside?
            () -> Robot.arm.isArmInside()
        );
    }

    public static Command SetArmElevToFullRetractPosCmd() {
        // return new ParallelCommandGroup(
        //     ElevatorCmds.ElevToRetractPosCmd(),
        //     ArmCmds.ArmToFullRetractCmd()
        // );
        return new ConditionalCommand(
            // True condition: arm inside robot, no worry of bumper collision
            new ParallelCommandGroup(
                ElevatorCmds.ElevToRetractPosCmd(),
                ArmCmds.ArmToFullRetractCmd()
            ),
            // False condition: arm outside robot, check for cube
            new ConditionalCommand(
                // True condition: no cube, good to go, parallel motion used
                new ParallelCommandGroup(
                    ElevatorCmds.ElevToRetractPosCmd(),
                    ArmCmds.ArmToFullRetractCmd()
                ),
                // False condition: cube, must avoid bumper collision;
                // raise elevator for clearance, set arm, then move elev back to correct pos
                new SequentialCommandGroup(
                    ElevatorCmds.ElevToBumperClearPosCmd(),
                    new ParallelCommandGroup(
                        ArmCmds.ArmToFullRetractCmd(),
                        ElevatorCmds.ElevHoldCmd().until(() -> Robot.arm.isMMtargetReached())
                    ),
                    // new DelayCmd(1.0),
                    ElevatorCmds.ElevToRetractPosCmd()
                ),
                () -> Robot.intake.isCubeEjectNotDetected()),
            // Condition: is arm outside?
            () -> Robot.arm.isArmInside()
        );

    
    }


    // -------------------- Rumble Controller  ---------------

    public static Command RumbleOperatorCmd(double intensity) {
        return new RunCommand(() -> Robot.operatorGamepad.rumble(intensity), Robot.operatorGamepad);
    }

}
