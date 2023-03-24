package frc.robot.auto.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;


public class PrintAutoTimeCmd extends CommandBase {

        String m_text;
    public PrintAutoTimeCmd(String text ) {
        m_text = text;
    }


    @Override
    public void initialize() {
        System.out.println( m_text + Robot.getAutoTime());
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
    }

     @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
