package frc.robot.operator.commands;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;
import frc.robot.arm.ArmSubSys;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.auto.commands.DelayCmd;
import frc.robot.elevator.ElevatorConfig;
import frc.robot.elevator.commands.ElevatorCmds;

public class OperatorGamepadCmds {
    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
        Robot.pilotGamepad.setDefaultCommand(rumbleOperatorCmd(0));
    }

    public static Command controlByJoysticks() {
        return new ParallelCommandGroup(
            ArmCmds.armByJoystick(),
            ElevatorCmds.elevByJoystick()
        );
    }

    /** Command that can be used to rumble the pilot controller */
    public static Command rumbleOperatorCmd(double intensity) {
        return new RunCommand(() -> Robot.pilotGamepad.rumble(intensity), Robot.pilotGamepad);
    }

    // GENERAL STRUCTURE OF POS COMMANDS:
    // If moving from out-out, then parallel motion used
    // If moving from in-to-out or out-to-in, then check for cube
    //    If cube there: avoid bumper collision
    //    If not, parallel motion used

    public static Command setFullBackPosCmd() {
        return new ParallelCommandGroup(
            ElevatorCmds.goToBottomCmd(),
            ArmCmds.armToFullRetractCmd()
        );
    }

    public static Command setStorePosCmd() {
        return new ConditionalCommand(
            // True condition: arm inside robot, no worry of bumper collision
            new ParallelCommandGroup(
                ElevatorCmds.goToBottomCmd(),
                ArmCmds.armToPIDPositionCmd(ArmConfig.ArmAngleLowPos)
            ),
            // False condition: arm outside robot, check for cube
            new ConditionalCommand(
                // True condition: no cube, good to go, parallel motion used
                new ParallelCommandGroup(
                    ElevatorCmds.goToBottomCmd(),
                    ArmCmds.armToPIDPositionCmd(ArmConfig.ArmAngleLowPos)
                ),
                // False condition: cube, must avoid bumper collision;
                // raise elevator for clearance, set arm, then move elev back to correct pos
                new SequentialCommandGroup(
                    ElevatorCmds.goToPIDPosCmd(ElevatorConfig.ElevClearPos),
                    new DelayCmd(2.0),
                    ArmCmds.armToPIDPositionCmd(ArmConfig.ArmAngleLowPos),
                    new DelayCmd(1.0),
                    ElevatorCmds.goToBottomCmd()
                ),
                () -> Robot.intake.isCubeNotDetected()),
            // Condition: is arm outside?
            () -> Robot.arm.isArmInside()
        );
    }
    
    public static Command setLowPosCmd() {
        return new ConditionalCommand(
            // True condition: arm outside robot, no worry of bumper collision (out-to-out movement)
            new ParallelCommandGroup(
                ElevatorCmds.goToBottomCmd(),
                ArmCmds.armToPIDPositionCmd(ArmConfig.ArmAngleLowPos)
            ),
            // False condition: arm inside robot, check for cube
            new ConditionalCommand(
                // True condition: no cube, good to go, parallel motion used
                new ParallelCommandGroup(
                    ElevatorCmds.goToBottomCmd(),
                    ArmCmds.armToPIDPositionCmd(ArmConfig.ArmAngleLowPos)
                ),
                // False condition: cube, must avoid bumper collision;
                // raise elevator for clearance, set arm, then move elev back to correct pos
                new SequentialCommandGroup(
                    ElevatorCmds.goToPIDPosCmd(ElevatorConfig.ElevClearPos),
                    new DelayCmd(2.0),
                    ArmCmds.armToPIDPositionCmd(ArmConfig.ArmAngleLowPos),
                    new DelayCmd(1.0),
                    ElevatorCmds.goToBottomCmd()
                ),
                () -> Robot.intake.isCubeNotDetected()),
            // Condition: is arm outside?
            () -> Robot.arm.isArmOutside()
        );
    }

    public static Command setMidPosCmd() {
        return new ConditionalCommand(
            // True condition: arm outside robot, no worry of bumper collision (out-to-out movement)
            new ParallelCommandGroup(
                ElevatorCmds.goToPIDPosCmd(ElevatorConfig.ElevMidPos),
                ArmCmds.armToPIDPositionCmd(ArmConfig.ArmAngleMidPos)
            ),
            // False condition: arm inside robot, check for cube
            new ConditionalCommand(
                // True condition: no cube, good to go, parallel motion used
                new ParallelCommandGroup(
                    ElevatorCmds.goToPIDPosCmd(ElevatorConfig.ElevMidPos),
                    ArmCmds.armToPIDPositionCmd(ArmConfig.ArmAngleMidPos)
                ),
                // False condition: cube, must avoid bumper collision;
                // raise elevator for clearance, set arm, then move elev back to correct pos
                new SequentialCommandGroup(
                    ElevatorCmds.goToPIDPosCmd(ElevatorConfig.ElevClearPos),
                    new DelayCmd(2.0),
                    ArmCmds.armToPIDPositionCmd(ArmConfig.ArmAngleMidPos),
                    new DelayCmd(1.0),
                    ElevatorCmds.goToPIDPosCmd(ElevatorConfig.ElevMidPos)
                ),
                () -> Robot.intake.isCubeNotDetected()),
            // Condition: is arm outside?
            () -> Robot.arm.isArmOutside()
        );
    }

    public static Command setHighPosCmd() {
        return new ConditionalCommand(
            // True condition: arm outside robot, no worry of bumper collision (out-to-out movement)
            new ParallelCommandGroup(
                ElevatorCmds.goToPIDPosCmd(ElevatorConfig.ElevHighPos),
                ArmCmds.armToPIDPositionCmd(ArmConfig.ArmAngleHighPos)
            ),
            // False condition: arm inside robot, check for cube
            new ConditionalCommand(
                // True condition: no cube, good to go, parallel motion used
                new ParallelCommandGroup(
                    ElevatorCmds.goToPIDPosCmd(ElevatorConfig.ElevHighPos),
                    ArmCmds.armToPIDPositionCmd(ArmConfig.ArmAngleHighPos)
                ),
                // False condition: cube, must avoid bumper collision;
                // raise elevator for clearance, set arm, then move elev back to correct pos
                new SequentialCommandGroup(
                    ElevatorCmds.goToPIDPosCmd(ElevatorConfig.ElevClearPos),
                    new DelayCmd(2.0),
                    ArmCmds.armToPIDPositionCmd(ArmConfig.ArmAngleHighPos),
                    new DelayCmd(1.0),
                    ElevatorCmds.goToPIDPosCmd(ElevatorConfig.ElevHighPos)
                ),
                () -> Robot.intake.isCubeNotDetected()),
            // Condition: is arm outside?
            () -> Robot.arm.isArmOutside()
        );
    }
}
