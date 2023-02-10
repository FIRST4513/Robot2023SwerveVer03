package frc.robot.auto.commands;

import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.arm.ArmConfig;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.auto.AutoConfig;
import frc.robot.auto.AutoSetup;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.swerveDrive.commands.SwerveDriveCmds;
import frc.robot.trajectories.commands.FollowTrajectoryCmd;
import frc.robot.trajectories.commands.TrajectoriesCmds;

public class AutoCmds {

    private static Translation2d ctrOfRot = new Translation2d( 0, 0);

    // TrajectoriesCmds.IntializePathFollowingCmd(GetFirstBalls),
    // new FollowTrajectoryCmd(GoTo2ndShots).withTimeout(4),
    // TrajectoriesCmds.IntializePathFollowingCmd(DriveOneMeterFwd),
    // new FollowTrajectoryCmd(DriveOneMeterFwd),
    // new DelayCmd(0.5),

    // public static Command followPathAndIntakeCmd(PathPlannerTrajectory path, double time){
    //     return new FollowTrajectoryCmd(path).deadlineWith(intakeCmd()).withTimeout(time);
    // }

    public static Command PlaceObjectCmd() {
        return new ParallelCommandGroup(
            // 1. raise elev to start pos
            ElevatorCmds.ElevGoToPIDPosCmd(AutoSetup.elevStartPos).withTimeout(2),
            // 2. raise arm to target pos
            ArmCmds.armToPIDPositionCmd(AutoSetup.armPosition).withTimeout(2),
            // 3. elev lower to final target pos
            ElevatorCmds.ElevGoToPIDPosCmd(AutoSetup.elevEndPos).withTimeout(2),
            // 4. eject
            IntakeCmds.intakeEjectCmd(),
            // Intake Arm to get ready for following path
            ElevatorCmds.ElevGoToPIDPosCmd(AutoConfig.kElevTop).withTimeout(2),
            ArmCmds.armToPIDPositionCmd(ArmConfig.ArmAngleStorePos),
            ElevatorCmds.ElevGoToBottomCmd().withTimeout(2)
        );
    }

    public static Command PlaceObjectRunPathCmd( PathPlannerTrajectory path, double time){
        return new SequentialCommandGroup(
            PlaceObjectCmd(),
            TrajectoriesCmds.IntializePathFollowingCmd(path , time)
        );
    }

    public static Command crossLineShortCmd(PathPlannerTrajectory path) {
        return new SequentialCommandGroup(
            TrajectoriesCmds.IntializePathFollowingCmd(path, 5.0)
        );
    }


}
