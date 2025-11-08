package org.firstinspires.ftc.teamcode.util.commands.actions;

import org.firstinspires.ftc.teamcode.util.commands.Command;

import java.util.function.Supplier;

public class Instant implements Command {

    private Command command = null;
    //It can return null because we handled those cases in our methods
    private final Supplier<Command> command_creator;

    public Instant(Supplier<Command> creator) {
        command_creator = creator;
    }


    @Override
    public void init() {
        //lazily initialize the command
        this.command = command_creator.get();
    }

    @Override
    public void run() {
        if (command != null) {
            command.run();
        }
    }

    //either there is no command or we are done
    @Override
    public boolean isDone() {
        return command == null || command.isDone();
    }

    //if we have a command shut it down
    @Override
    public void shutdown() {
        if (command != null) {
            command.shutdown();
        }
    }
}
