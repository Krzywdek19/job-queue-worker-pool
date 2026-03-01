package pl.exceptionhandled.jobqueue.app;

import pl.exceptionhandled.jobqueue.cli.*;
import pl.exceptionhandled.jobqueue.cli.commands.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        CommandParser parser = new CommandParser();
        boolean running = true;
        System.out.println("Job Queue CLI.\nType 'help' for available commands. Type 'exit' to quit.");

        while (running) {
            System.out.print("> ");
            String line = in.readLine();
            if(line == null) break;
            Command cmd = parser.parse(line);
            if(cmd instanceof NoOpCommand) {
            }else if(cmd instanceof HelpCommand) {
                System.out.println("Available commands:\n" +
                        "help - show this message\n" +
                        "exit - exit the application");
            }else if (cmd instanceof ExitCommand) {
                running = false;
            }else if(cmd instanceof SubmitCommand(JobType jobType, String params)) {
                System.out.println("Parsed submit: jobType=" + jobType + ", params=" + params);
            } else if(cmd instanceof InvalidCommand(String message)) {
                System.out.println("Invalid command: " + message);
            }else if(cmd instanceof UnknownCommand(String raw)){
                System.out.println("Unknown command: " + raw);
            }else {
                System.out.println("Unhandled command: " + cmd.getClass().getSimpleName());
            }
        }
        System.out.println("Goodbye!");
    }
}