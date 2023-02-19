package frc.robot.operator.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.auto.commands.DelayCmd;
import frc.robot.elevator.ElevatorConfig;
import frc.robot.elevator.commands.ElevatorCmds;

public class OperatorGamepadCmds {
    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
        Robot.pilotGamepad.setDefaultCommand(RumbleOperatorCmd(0));
    }

    public static Command ControlArmElevByJoysticksCmd() {
        return new ParallelCommandGroup(
            // ArmCmds.ArmByJoystickCmd(),
            ElevatorCmds.ElevByJoystickCmd()
        );
    }

    /** Command that can be used to rumble the pilot controller */
    public static Command RumbleOperatorCmd(double intensity) {
        return new RunCommand(() -> Robot.pilotGamepad.rumble(intensity), Robot.pilotGamepad);
    }

    // GENERAL STRUCTURE OF POS COMMANDS:
    // If moving from out-out, then parallel motion used
    // If moving from in-to-out or out-to-in, then check for cube
    //    If cube there: avoid bumper collision
    //    If not, parallel motion used

    public static Command SetArmElevToFullBackPosCmd() {
        return new ParallelCommandGroup(
            ElevatorCmds.ElevGoToBottomCmd(),
            ArmCmds.ArmToFullRetractCmd()
        );
    }

    public static Command SetArmElevToStorePosCmd() {
        return new ConditionalCommand(
            // True condition: arm inside robot, no worry of bumper collision
            new ParallelCommandGroup(
                ElevatorCmds.ElevGoToBottomCmd(),
                ArmCmds.ArmToPIDPositionCmd(ArmConfig.ArmAngleStorePos)
            ),
            // False condition: arm outside robot, check for cube
            new ConditionalCommand(
                // True condition: no cube, good to go, parallel motion used
                new ParallelCommandGroup(
                    ElevatorCmds.ElevGoToBottomCmd(),
                    ArmCmds.ArmToPIDPositionCmd(ArmConfig.ArmAngleStorePos)
                ),
                // False condition: cube, must avoid bumper collision;
                // raise elevator for clearance, set arm, then move elev back to correct pos
                new SequentialCommandGroup(
                    ElevatorCmds.ElevGoToPIDheightCmd(ElevatorConfig.ElevClearHt),
                    new DelayCmd(2.0),
                    ArmCmds.ArmToPIDPositionCmd(ArmConfig.ArmAngleStorePos),
                    new DelayCmd(1.0),
                    ElevatorCmds.ElevGoToBottomCmd()
                ),
                () -> Robot.intake.isCubeEjectDetected()),
            // Condition: is arm outside?
            () -> Robot.arm.isArmInside()
        );
    }
    
    public static Command SetArmElevToLowPosCmd() {
        return new ConditionalCommand(
            // True condition: arm outside robot, no worry of bumper collision (out-to-out movement)
            new ParallelCommandGroup(
                ElevatorCmds.ElevGoToBottomCmd(),
                ArmCmds.ArmToPIDPositionCmd(ArmConfig.ArmAngleLowPos)
            ),
            // False condition: arm inside robot, check for cube
            new ConditionalCommand(
                // True condition: no cube, good to go, parallel motion used
                new ParallelCommandGroup(
                    ElevatorCmds.ElevGoToBottomCmd(),
                    ArmCmds.ArmToPIDPositionCmd(ArmConfig.ArmAngleLowPos)
                ),
                // False condition: cube, must avoid bumper collision;
                // raise elevator for clearance, set arm, then move elev back to correct pos
                new SequentialCommandGroup(
                    ElevatorCmds.ElevGoToPIDheightCmd(ElevatorConfig.ElevClearHt),
                    new DelayCmd(2.0),
                    ArmCmds.ArmToPIDPositionCmd(ArmConfig.ArmAngleLowPos),
                    new DelayCmd(1.0),
                    ElevatorCmds.ElevGoToBottomCmd()
                ),
                () -> Robot.intake.isCubeEjectDetected()),
            // Condition: is arm outside?
            () -> Robot.arm.isArmOutside()
        );
    }

    public static Command SetArmElevToMidPosCmd() {
        return new ConditionalCommand(
            // True condition: arm outside robot, no worry of bumper collision (out-to-out movement)
            new ParallelCommandGroup(
                ElevatorCmds.ElevGoToPIDheightCmd(ElevatorConfig.ElevMidHt),
                ArmCmds.ArmToPIDPositionCmd(ArmConfig.ArmAngleMidPos)
            ),
            // False condition: arm inside robot, check for cube
            new ConditionalCommand(
                // True condition: no cube, good to go, parallel motion used
                new ParallelCommandGroup(
                    ElevatorCmds.ElevGoToPIDheightCmd(ElevatorConfig.ElevMidHt),
                    ArmCmds.ArmToPIDPositionCmd(ArmConfig.ArmAngleMidPos)
                ),
                // False condition: cube, must avoid bumper collision;
                // raise elevator for clearance, set arm, then move elev back to correct pos
                new SequentialCommandGroup(
                    ElevatorCmds.ElevGoToPIDheightCmd(ElevatorConfig.ElevClearHt),
                    new DelayCmd(2.0),
                    ArmCmds.ArmToPIDPositionCmd(ArmConfig.ArmAngleMidPos),
                    new DelayCmd(1.0),
                    ElevatorCmds.ElevGoToPIDheightCmd(ElevatorConfig.ElevMidHt)
                ),
                () -> Robot.intake.isCubeEjectDetected()),
            // Condition: is arm outside?
            () -> Robot.arm.isArmOutside()
        );
    }

    public static Command SetArmElevToHighPosCmd() {
        return new ConditionalCommand(
            // True condition: arm outside robot, no worry of bumper collision (out-to-out movement)
            new ParallelCommandGroup(
                ElevatorCmds.ElevGoToPIDheightCmd(ElevatorConfig.ElevHighHt),
                ArmCmds.ArmToPIDPositionCmd(ArmConfig.ArmAngleHighPos)
            ),
            // False condition: arm inside robot, check for cube
            new ConditionalCommand(
                // True condition: no cube, good to go, parallel motion used
                new ParallelCommandGroup(
                    ElevatorCmds.ElevGoToPIDheightCmd(ElevatorConfig.ElevHighHt),
                    ArmCmds.ArmToPIDPositionCmd(ArmConfig.ArmAngleHighPos)
                ),
                // False condition: cube, must avoid bumper collision;
                // raise elevator for clearance, set arm, then move elev back to correct pos
                new SequentialCommandGroup(
                    ElevatorCmds.ElevGoToPIDheightCmd(ElevatorConfig.ElevClearHt),
                    new DelayCmd(2.0),
                    ArmCmds.ArmToPIDPositionCmd(ArmConfig.ArmAngleHighPos),
                    new DelayCmd(1.0),
                    ElevatorCmds.ElevGoToPIDheightCmd(ElevatorConfig.ElevHighHt)
                ),
                () -> Robot.intake.isCubeEjectDetected()),
            // Condition: is arm outside?
            () -> Robot.arm.isArmOutside()
        );
    }
    
}
