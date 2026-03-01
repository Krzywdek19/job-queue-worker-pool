package pl.exceptionhandled.jobqueue.cli;

import pl.exceptionhandled.jobqueue.cli.commands.*;

import java.util.Locale;
import java.util.UUID;

public class CommandParser {
    public Command parse(String line) {
        String trimmed = line.trim();
        String[] parts = trimmed.split("\\s+", 3);
        if(trimmed.isEmpty()){
            return new NoOpCommand();
        }
        String cmd = parts[0].toLowerCase(Locale.ROOT);

        return switch (cmd) {
            case "help" -> new HelpCommand();
            case "exit" -> new ExitCommand();
            case "submit" -> {
                if (parts.length != 3) {
                    yield new InvalidCommand("Usage: submit <type> <payload>");
                }
                if(!JobType.containsText(parts[1])) {
                    yield new InvalidCommand("Unknown job type: " + parts[1]);
                }
                JobType jobName = JobType.fromText(parts[1]).get();
                String jobParams = parts[2];
                yield new SubmitCommand(jobName, jobParams);
            }
            case "status", "result" -> {
                if (parts.length != 2) {
                    yield new InvalidCommand("Usage: " + cmd + " <jobId>");
                }
                String jobId = parts[1];
                UUID id;
                try {
                    id = UUID.fromString(jobId);
                }catch (IllegalArgumentException e) {
                    yield new InvalidCommand("Invalid job ID format: " + jobId);
                }
                if(cmd.equals("status")) {
                    yield new StatusCommand(id);
                }else {
                    yield new ResultCommand(id);
                }
            }
            case "list" -> {
                if (parts.length > 2) {
                    yield new InvalidCommand("Usage: list");
                }
                if(parts.length == 1) yield  new ListCommand(20);
                if(!parts[1].matches("\\d+")) {
                    yield new InvalidCommand("Limit must be a number: " + parts[1]);
                }
                int limit = Integer.parseInt(parts[1]);
                if(limit <= 0) {
                    yield new InvalidCommand("Limit must be a positive number: " + limit);
                }
                yield new ListCommand(limit);
            }
            case "shutdown" -> new ShutdownCommand();
            default -> new UnknownCommand(trimmed);
        };
    }
}
