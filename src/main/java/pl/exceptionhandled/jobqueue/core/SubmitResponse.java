package pl.exceptionhandled.jobqueue.core;

import java.util.UUID;

public sealed interface SubmitResponse permits SubmitResponse.Accepted, SubmitResponse.Rejected {
    record Accepted(UUID jobId) implements SubmitResponse {}
    record Rejected(String reason) implements SubmitResponse {}
}
