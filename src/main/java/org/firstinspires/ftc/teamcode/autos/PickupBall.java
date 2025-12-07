package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.AlignRotator;
import org.firstinspires.ftc.teamcode.util.commands.actions.DriveForward;
import org.firstinspires.ftc.teamcode.util.commands.actions.IntakeCommand;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;

// Jonah was here
@Autonomous(name="Pickup Ball")
public class PickupBall extends OpModeBase {

    private Command seq;

    private boolean is_done = false;

    @Override
    public void init(){
        super.init();

        this.seq = new SequentialCommandGroup(new IntakeCommand(1.5, 1.0, robot));
        this.seq.init();
    }

    @Override
    public void loop() {
        if (!seq.isDone() && !is_done){
            seq.run();
        }else{
            is_done = true;
            seq.shutdown();
        }
    }
}
