package pl.exceptionhandled.jobqueue.app;

import pl.exceptionhandled.jobqueue.cli.*;
import pl.exceptionhandled.jobqueue.cli.commands.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

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
            switch (cmd) {
                case NoOpCommand ignored -> {
                }
                case HelpCommand ignored -> System.out.println("""
                        Available commands:
                          submit <type> <payload>
                          status <jobId>
                          result <jobId>
                          list [limit]
                          shutdown
                          help
                          exit
                        Types:
                          SLEEP <ms>
                          HASH <text>
                        """);
                case ExitCommand ignored -> running = false;
                case SubmitCommand(JobType jobType, String params) ->
                        System.out.println("Parsed submit: jobType=" + jobType + ", params=" + params);
                case StatusCommand(UUID jobId) -> System.out.println("Parsed status command for job ID: " + jobId);
                case ResultCommand(UUID jobId) -> System.out.println("Parsed result command for job ID: " + jobId);
                case ListCommand(int limit) -> System.out.println("Parsed list command with limit: " + limit);
                case ShutdownCommand ignored -> {
                    System.out.println("Parsed shutdown command");
                    running = false;
                }
                case InvalidCommand(String message) -> System.out.println("Invalid command: " + message);
                case UnknownCommand(String raw) -> System.out.println("Unknown command: " + raw);
                default -> System.out.println("Unhandled command: " + cmd.getClass().getSimpleName());
            }
        }
        System.out.println("Goodbye!");
    }
}