package frc.robot.commands.IntakeCommands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Intake;

public class IntakeGiveCmd extends Command{
    private final Intake intake;
    private final Index index;

    public IntakeGiveCmd(Intake intake, Index index){
        this.intake = intake;
        this.index = index;
    }

    @Override
    public void execute() {
        intake.setRoller(0.5);
        index.setIndex(-0.5);
        super.execute();
    }

    @Override
    public void end(boolean interrupted) {
        intake.setRoller(0);
        index.setIndex(0);
        super.end(interrupted);
    }
}
