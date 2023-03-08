package frc.robot.operator;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.lib.gamepads.Gamepad;
import frc.lib.gamepads.mapping.ExpCurve;
import frc.robot.Robot;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.auto.commands.DelayCmd;
import frc.robot.autoBalance.commands.AutoBalanceCommand;
import frc.robot.elevator.ElevFXMotorConfig;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.operator.commands.OperatorGamepadCmds;
import frc.robot.swerve.commands.LockSwerve;
import frc.robot.trajectories.commands.TrajectoriesCmds;

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

    // Test for auto balance
    static PathPlannerTrajectory ctrTestPath1 = PathPlanner.loadPath(
        "CtrScale1", 2.0 , 2.0);     // Max Vel , Max Accel
    static PathPlannerTrajectory ctrTestPath2 = PathPlanner.loadPath(
        "CtrScale2", 0.5 , 0.5);     // Max Vel , Max Accel
    static PathPlannerTrajectory rightLongScaleTest1 = PathPlanner.loadPath(
        "BlueRightLongScale1", 4.0 , 4.0);     // Max Vel , Max Accel
    static PathPlannerTrajectory rightLongScaleTest2 = PathPlanner.loadPath(
        "BlueRightLongScale1", 0.5 , 0.5);     // Max Vel , Max Accel

    public OperatorGamepad() {
        super("Operator", OperatorGamepadConfig.port);
    }
    
    public void setupTeleopButtons() {

        //gamepad.aButton     .onTrue(IntakeCmds.IntakeEjectCmd());
        gamepad.bButton     .onTrue(IntakeCmds.IntakeCubeCmd());
        gamepad.yButton     .onTrue(IntakeCmds.IntakeConeCmd());
        gamepad.xButton     .onTrue(IntakeCmds.IntakeStopCmd());
 
        //gamepad.aButton     .onTrue(OperatorGamepadCmds.runTestPathCmd());
        gamepad.aButton     .onTrue(runCtrTestPathCmd());

        //gamepad.aButton     .onTrue(OperatorGamepadCmds.runTestPathCmd());
        gamepad.bButton     .onTrue(runRightLongScalePathCmd());

        //gamepad.selectButton.onTrue(OperatorGamepadCmds.SetArmElevToFullRetractPosCmd());
        gamepad.selectButton.onTrue(runBalanceTestCmd());
        
        gamepad.Dpad.Up     .onTrue(OperatorGamepadCmds.SetArmElevToEjectHighPosCmd());
        gamepad.Dpad.Down   .onTrue(OperatorGamepadCmds.SetArmElevToEjectLowPosCmd());
        gamepad.Dpad.Left   .onTrue(OperatorGamepadCmds.SetArmElevToEjectMidPosCmd());
        gamepad.Dpad.Right  .onTrue(OperatorGamepadCmds.SetArmElevToStorePosCmd());

        gamepad.rightBumper .onTrue(OperatorGamepadCmds.ControlArmElevByJoysticksCmd());
        gamepad.leftBumper  .whileTrue(IntakeCmds.IntakeByJoystickCmd());

        gamepad.startButton .onTrue(ArmCmds.ResetArmEncoderCmd());
    }

    @Override
    public void setupTestButtons() {
        // gamepad.aButton     .onTrue(OperatorGamepadCmds.runTestPathCmd());
    }

    public void setupDisabledButtons() {
    }


    public double getElevInput() {
        return elevThrottleCurve.calculateMappedVal(gamepad.rightStick.getY()) ;

        // double yValue = gamepad.rightStick.getY();
        // if (Math.abs(yValue) < 0.05) {
        //     yValue = 0.0;
        // }
        // if (OperatorGamepadConfig.elevYInvert) {
        //     return yValue * -0.5;
        // } else {
        //     return yValue * 0.5;
        // }
    }

    public double getElevInputWFF() {
        // calculate value with feed forward for elevator
        return getElevInput() + ElevFXMotorConfig.arbitraryFeedForward;
    }

    public double getArmInput() {
        return armThrottleCurve.calculateMappedVal(gamepad.leftStick.getY());

        // double yValue = gamepad.leftStick.getY();
        // if (Math.abs(yValue) < 0.175) {
        //     yValue = 0.0;
        // }
        // if (OperatorGamepadConfig.armYInvert) {
        //     return yValue * -0.5;
        // } else {
        //     return yValue * 0.5;
        // }
    }

    public double getArmInputWFF() {
        // calculate value with feed forward for arm
        return getArmInput() + Robot.arm.getHoldPwr();
    }

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
    public boolean isArmAndElevAtPos() {
        if ((Robot.arm.isMMtargetReached()) && (Robot.elevator.isMMtargetReached())) {
            return true;
        }
        return false;
    }

    public static Command runCtrTestPathCmd() {
        return new SequentialCommandGroup(
            TrajectoriesCmds.IntializeRobotAndFollowPathCmd(ctrTestPath1, 5.0),
            new DelayCmd(0.5),
            TrajectoriesCmds.FollowPathCmd(ctrTestPath2, 5.0),
            //new AutoBalanceCommand(),            
            new LockSwerve()
        );
    }
    
    
    public static Command runRightLongScalePathCmd() {
        return new SequentialCommandGroup(
            TrajectoriesCmds.IntializeRobotAndFollowPathCmd(rightLongScaleTest1, 5.0),
            new DelayCmd(0.5),
            TrajectoriesCmds.FollowPathCmd(rightLongScaleTest2, 5.0),
            //new AutoBalanceCommand(),            
            new LockSwerve()
        );
    }

    public static Command runBalanceTestCmd() {
        return new SequentialCommandGroup(
            new AutoBalanceCommand(),            
            new LockSwerve()
        );
    }
}
