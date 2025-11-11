package org.firstinspires.ftc.teamcode.util.commands.command_groups;

import org.firstinspires.ftc.teamcode.util.commands.Command;

public class ParallelCommandGroup implements Command {
    private final Command[] commands;
    private final boolean[] completed;

    public ParallelCommandGroup(Command... commands){
        this.commands = commands;
        //they are initialized as false
        this.completed = new boolean[commands.length];
    }

    @Override
    public void init() {
        for (Command command: commands){
            command.init();
        }
    }

    @Override
    public void run() {
        for (int i = 0; i<commands.length; i++){
            if (!completed[i]){
                Command command = commands[i];
                if (command.isDone()){
                    command.shutdown();
                    completed[i] = true;
                    continue;
                }
                command.run();
            }
        }
    }

    @Override
    public boolean isDone() {
        for (boolean done: completed){
            if (!done){
                return false;
            }
        }
        return true;
    }

    @Override
    public void shutdown() {
        for (int i = 0; i<commands.length; i++) {
            //we don't want to shut down twice so we have to check if we have already completed the command
            if (!completed[i]) {
                commands[i].shutdown();
            }
        }
    }
}
