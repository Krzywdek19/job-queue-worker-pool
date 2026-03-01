package pl.exceptionhandled.jobqueue.cli.commands;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum JobType {
    SLEEP("sleep"), HASH("hash");
    private final String text;

    JobType(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
    public static boolean containsText(String s){
        if (s == null) return false;
        return fromText(s).isPresent();
    }

    private static final Map<String, JobType> BY_TEXT =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(
                            c -> normalize(c.text),
                            c -> c
                    ));

    private static String normalize(String s) {
        return s.trim().toLowerCase(Locale.ROOT);
    }

    public static Optional<JobType> fromText(String s) {
        if(s == null) return Optional.empty();
        Optional<JobType> jt =  Optional.ofNullable(BY_TEXT.get(normalize(s)));
        if(jt.isEmpty()) {
            try {
                return Optional.of(valueOf(s.trim().toUpperCase(Locale.ROOT)));
            }catch (IllegalArgumentException e){
                return Optional.empty();
            }
        }
        return jt;
    }
}
