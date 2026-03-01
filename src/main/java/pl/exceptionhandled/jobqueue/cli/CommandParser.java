package pl.exceptionhandled.jobqueue.cli;

import pl.exceptionhandled.jobqueue.cli.commands.*;

import java.util.Locale;

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
                if (parts.length < 3) {
                    yield new InvalidCommand("Usage: submit <job-name> <job-params>");
                }
                if(!JobType.containsText(parts[1])) {
                    yield new InvalidCommand("Unknown job type: " + parts[1]);
                }
                JobType jobName = JobType.fromText(parts[1]).get();
                String jobParams = parts[2];
                yield new SubmitCommand(jobName, jobParams);
            }
            default -> new UnknownCommand(trimmed);
        };
    }
}
