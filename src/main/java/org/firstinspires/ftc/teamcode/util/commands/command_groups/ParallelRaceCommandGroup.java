package org.firstinspires.ftc.teamcode.util.commands.command_groups;

import org.firstinspires.ftc.teamcode.util.commands.Command;

public class ParallelRaceCommandGroup implements Command{
    private Command[] commands;
    private boolean finished;

    public ParallelRaceCommandGroup(Command... commands){
        this.commands = commands;
        this.finished = false;
    }

    @Override
    public void init() {
        for (Command command: commands){
            command.init();
        }
    }

    @Override
    public void run() {
        if (!finished) {
            for (int i = 0; i < commands.length; i++) {
                Command command = commands[i];
                if (command.isDone()) {
                    finished = true;
                    break;
                }
                command.run();
            }
        }
    }

    @Override
    public boolean isDone() {
        return finished;
    }

    @Override
    public void shutdown() {
        for (Command command : commands) {
            command.shutdown();
        }
    }
}
