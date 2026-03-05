package pl.exceptionhandled.jobqueue.core;

public sealed interface JobResult permits JobResult.HashResult, JobResult.SleepResult {
    record HashResult(String sha256Hex) implements JobResult {}
    record SleepResult(long sleptMs) implements JobResult {}
}