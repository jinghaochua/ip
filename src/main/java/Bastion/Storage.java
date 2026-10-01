package Bastion;

import java.nio.file.NoSuchFileException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving task data to disk.
 */
public class Storage {
    private static final Path SAVE_FILE = Path.of("data", "bastion.txt");

    /**
     * Saves the current task list to the data file.
     *
     * @param tasks all tasks in the task list
     * @throws IOException if the data file cannot be written
     */
    public void saveTasks(List<Task> tasks) throws IOException {
        List<String> lines = new ArrayList<>();

        for (Task task : tasks) {
            lines.add(task.toFileString());
        }

        Files.createDirectories(SAVE_FILE.getParent());
        Files.write(SAVE_FILE, lines);
    }

    /**
     * Loads saved tasks. Returns an empty list when no save file exists yet.
     *
     * @return saved tasks, or an empty list for a first-time user
     * @throws IOException if an existing save file cannot be read
     */
    public List<Task> loadTasks() throws IOException {
        List<Task> tasks = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(SAVE_FILE);

            for (String line : lines) {
                String[] parts = line.split(" \\| ");

                Task task;
                if (parts[0].equals("T")) {
                    task = new Todo(parts[2]);
                } else if (parts[0].equals("D")) {
                    task = loadDeadline(parts[2], parts[3]);
                } else if (parts[0].equals("E")) {
                    task = new Event(parts[2], parts[3], parts[4]);
                } else {
                    continue;
                }

                if (parts[1].equals("1")) {
                    task.markAsDone();
                }

                tasks.add(task);
            }
        } catch (NoSuchFileException e) {
            // No file or data folder exists yet: this is a new user, so start empty.
        }

        return tasks;
    }

    /**
     * Loads a structured deadline when possible while keeping old free-text deadlines usable.
     */
    private Task loadDeadline(String description, String dateTime) {
        try {
            return new Deadline(description, LocalDateTime.parse(dateTime));
        } catch (DateTimeParseException e) {
            return new LegacyDeadline(description, dateTime);
        }
    }
}
