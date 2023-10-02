package frc.robot.operator;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.lib.gamepads.Gamepad;
import frc.lib.gamepads.mapping.ExpCurve;
import frc.robot.Robot;
import frc.robot.arm.ArmSubSys.ArmStates;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.auto.commands.CenterDriveOnCmd;
import frc.robot.autoBalance.commands.AutoBalanceCommand;
import frc.robot.elevator.ElevFXMotorConfig;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.operator.commands.ArmElevComboMoveCmds;
import frc.robot.operator.commands.OperatorGamepadCmds;
import frc.robot.swerve.commands.LockSwerve;

public class OperatorGamepad extends Gamepad {
    public static ExpCurve intakeThrottleCurve = new ExpCurve(
        OperatorGamepadConfig.intakeSpeedExp,
        OperatorGamepadConfig.intakeSpeedOffset,
        OperatorGamepadConfig.intakeSpeedScaler,
        OperatorGamepadConfig.intakeSpeedDeadband);

    public static ExpCurve elevThrottleCurve = new ExpCurve(
        OperatorGamepadConfig.elevSpeedExp,
        OperatorGamepadConfig.elevSpeedOffset,
        OperatorGamepadConfig.elevSpeedScaler,
        OperatorGamepadConfig.elevSpeedDeadband);

    public static ExpCurve armThrottleCurve = new ExpCurve(
        OperatorGamepadConfig.armSpeedExp,
        OperatorGamepadConfig.armSpeedOffset,
        OperatorGamepadConfig.armSpeedScaler,
        OperatorGamepadConfig.armSpeedDeadband);

    // path testing example
    // static PathPlannerTrajectory ctrTestPath1 = PathPlanner.loadPath(
    //     "CtrScale1", 2.0 , 2.0);     // Max Vel , Max Accel

    public OperatorGamepad() {
        super("Operator", OperatorGamepadConfig.port);
    }
    
    public void setupTeleopButtons() {

        gamepad.aButton      .onTrue(IntakeCmds.IntakeEjectRunCmd());    // possible replace with until version of these?
        gamepad.bButton      .onTrue(IntakeCmds.IntakeRetractRunCmd());
        gamepad.yButton      .onTrue(IntakeCmds.IntakeHoldRunCmd());
        gamepad.xButton      .onTrue(IntakeCmds.IntakeStopCmd());

        gamepad.selectButton.onTrue(new CenterDriveOnCmd());             // keep as button???
        gamepad.startButton  .onTrue(ArmCmds.ResetArmEncoderCmd());
        
        gamepad.Dpad.Right   .onTrue(ArmElevComboMoveCmds.SetArmRunElevMidPosCmd());
        gamepad.Dpad.Down    .onTrue(OperatorGamepadCmds.RunArmElevToIntakePosCmd());
        gamepad.Dpad.Left    .onTrue(OperatorGamepadCmds.RunArmElevToStowPosCmd());
        gamepad.Dpad.Up      .onTrue(ArmElevComboMoveCmds.SetArmRunElevHighPosCmd());

        gamepad.leftBumper   .whileTrue(IntakeCmds.IntakeByJoystickCmd());
        gamepad.rightBumper  .onTrue(OperatorGamepadCmds.ControlArmElevByJoysticksCmd());
    }

    @Override
    public void setupTestButtons() {}

    public void setupDisabledButtons() {}

    public double getElevInput() {
        // throttle curves weren't working as I was needing, so this is- admittedly- a bandaid solution for it.
        // return elevThrottleCurve.calculateMappedVal(gamepad.rightStick.getY()) ;
        double input = gamepad.rightStick.getY();
        if (Math.abs(input) < OperatorGamepadConfig.elevSpeedDeadband) {
            input = 0;
        }
        return input * OperatorGamepadConfig.elevSpeedScaler;
    }

    public double getElevInputWFF() {
        // calculate value with feed forward for elevator
        return getElevInput() + ElevFXMotorConfig.arbitraryFeedForward;
    }

    public double getArmInput() {
        // same here- bandaid solution :/
        double input = gamepad.leftStick.getY();
        if (Math.abs(input) < OperatorGamepadConfig.armSpeedDeadband) {
            input = 0;
        }
        return input * OperatorGamepadConfig.armSpeedScaler;
        // return armThrottleCurve.calculateMappedVal(gamepad.leftStick.getY());
    }

    // irrelevant
    // public double getArmInputWFF() {
    //     // calculate value with feed forward for arm
    //     return getArmInput() + Robot.arm.getHoldPwr();
    // }

    public double getTriggerTwist() {
        // return -gamepad.triggers.getTwist()/3;
        return intakeThrottleCurve.calculateMappedVal(gamepad.triggers.getTwist());
    }
    
    public double getTriggerTwistInvert() {
        return -getTriggerTwist();
    }

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
    }

    public void rumble(DoubleSupplier intensityLeft, DoubleSupplier intensityRight) {
        this.gamepad.setRumble(intensityLeft.getAsDouble(), intensityRight.getAsDouble());
    }

    public static double getLeftRumble() {
        return 0;
    }

    public static double getRightRumble() {
        return 0;
    }
    
    public boolean isArmAndElevAtPos() {
        if ((Robot.arm.isMMtargetReached()) && (Robot.elevator.isMMtargetReached())) {
            return true;
        }
        return false;
    }

    public static Command runBalanceTestCmd() {
        return new SequentialCommandGroup(
            new AutoBalanceCommand(),            
            new LockSwerve()
        );
    }

    /*   "the sparkle" -madi   */

    // path testing example
    // public static Command runTestPathCmd() {
    //     return new SequentialCommandGroup(
    //         TrajectoriesCmds.IntializeRobotAndFollowPathCmd(ctrTestPath1, 5.0),
    //         new LockSwerve()
    //     );
    // }
}
