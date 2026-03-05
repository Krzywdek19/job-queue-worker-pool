package pl.exceptionhandled.jobqueue.app;

import pl.exceptionhandled.jobqueue.cli.*;
import pl.exceptionhandled.jobqueue.cli.commands.*;
import pl.exceptionhandled.jobqueue.core.JobQueueService;
import pl.exceptionhandled.jobqueue.core.JobResult;
import pl.exceptionhandled.jobqueue.core.JobStatus;
import pl.exceptionhandled.jobqueue.core.SubmitResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        CommandParser parser = new CommandParser();
        JobQueueService service = new JobQueueService(40, 100);
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
                case SubmitCommand(JobType jobType, String params) -> {
                        SubmitResponse response = service.submit(jobType, params);
                        switch (response){
                            case SubmitResponse.Accepted(UUID jobId) -> System.out.println("ACCEPTED " + jobId);
                            case SubmitResponse.Rejected(String reason) -> System.out.println("REJECTED " + reason);
                        }
                }
                case StatusCommand(UUID jobId) -> service.getStatus(jobId)
                        .ifPresentOrElse(
                                st -> System.out.println("STATUS " + jobId + " " + st),
                                () -> System.out.println("NOT_FOUND " + jobId)
                        );
                case ResultCommand(UUID jobId) ->
                    service.getStatus(jobId).ifPresentOrElse(
                            st ->{
                                if(st != JobStatus.SUCCESS) {
                                    System.out.println("NOT_READY " + jobId + " " + st);
                                    return;
                                }
                                service.result(jobId).ifPresentOrElse(
                                        r -> System.out.println("RESULT " + jobId + " " + format(r)),
                                        () -> System.out.println("RESULT " + jobId + " <missing>")
                                );
                            },
                            () -> System.out.println("NOT_FOUND " + jobId)
                    );
                case ListCommand(int limit) -> {
                    var jobs = service.list(limit);
                    if(jobs.isEmpty()) {
                        System.out.println("(empty)");
                    }else {
                        for(var job : jobs){
                            System.out.println("JOB " + job.id() + " " + job.type() + " " + job.status() + " attempts=" + job.attempts());
                        }
                    }
                }
                case ShutdownCommand ignored -> {
                    System.out.println("Parsed shutdown command");
                    running = false;
                }
                case InvalidCommand(String message) -> System.out.println("Invalid command: " + message);
                case UnknownCommand(String raw) -> System.out.println("Unknown command: " + raw);
            }
        }
        System.out.println("Goodbye!");
    }

    private static String format(JobResult r) {
        return switch (r) {
            case JobResult.SleepResult(long ms) -> "SLEEP " + ms;
            case JobResult.HashResult(String hash) -> "HASH " + hash;
        };
    }
}