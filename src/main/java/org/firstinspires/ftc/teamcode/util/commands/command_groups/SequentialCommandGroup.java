package org.firstinspires.ftc.teamcode.util.commands.command_groups;

import org.firstinspires.ftc.teamcode.util.commands.Command;

//Will take commands and run them sequentially until all of them are finished
public class SequentialCommandGroup implements Command {
    private Command[] commands;
    private int current_command;

    public SequentialCommandGroup(Command... commands){
        this.commands = commands;
        this.current_command = 0;
    }

    @Override
    public void init() {
        commands[current_command].init();
    }
    @Override
    public void run() {
        if (!isDone()){
            //if we have finished the current command
            if (commands[current_command].isDone()){
                //clean up the current command and initialize the next one(if it exists)
                commands[current_command].shutdown();
                current_command++;
                if (!isDone()) {
                    commands[current_command].init();
                }
            }
            //we are calling isDone again because we might change current_command which isDone depends on
            if (!isDone()){
                commands[current_command].run();
            }
        }
    }

    @Override
    public boolean isDone() {
        return (current_command>=commands.length);
    }

    @Override
    public void shutdown() {
        if (!isDone()){
            commands[current_command].shutdown();
        }
    }
}
