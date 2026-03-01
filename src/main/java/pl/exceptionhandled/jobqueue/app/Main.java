package pl.exceptionhandled.jobqueue.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        boolean running = true;
        // TODO: print welcome

        while (running) {
            // TODO: prompt
            String line = in.readLine();
            if(line == null) break;

            String trimmed = line.trim();
            if(trimmed.isEmpty()) continue;
            String cmd = trimmed.toLowerCase();

            switch (cmd) {
                case "exit":
                    running = false;
                    break;
                case "help":
                    System.out.println("Available commands:\n- help: show this message\n- exit: quit the application");
                    break;
                default:
                    System.out.println("Unknown command: " + trimmed);
            }
        }
        // TODO: goodbye (optional)
    }
}