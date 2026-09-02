import java.util.Scanner;

public class Bastion {
    /** The maximum number of tasks the application keeps. */
    private static final int MAX_TASKS = 100;

    static class Task {
        String description;
        boolean isDone;

        Task(String description) {
            this.description = description;
            this.isDone = false;
        }

        void markAsDone() {
            this.isDone = true;
        }

        void markAsNotDone() {
            this.isDone = false;
        }

        String getStatusIcon() {
            return (isDone ? "X" : " "); // Returns X if done, space if not
        }
    }

    /**
     * Starts the task list application and processes commands until the user enters
     * {@code bye}.
     * Any input other than those commands is added as a task; {@code list} displays
     * stored tasks.
     *
     * @param args command-line arguments, which are not used
     */

    public static void main(String[] args) {
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        printLine();
        System.out.println("Hello! I'm Bastion.");
        System.out.println("What can I do for you?");
        printLine();

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine().strip();

                if (input.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    printLine();
                    break;
                }

                if (input.equals("list")) {
                    printTasks(tasks, taskCount);
                } else if (input.startsWith("mark ")) {
                    try {
                        int taskNumber = Integer.parseInt(input.substring(5)) - 1;
                        tasks[taskNumber].markAsDone();
                        
                        if (taskNumber >= 0 && taskNumber < taskCount) {
                            System.out.println("Nice! I've marked this task as done:");
                            System.out.println("  [" + tasks[taskNumber].getStatusIcon() + "] " + tasks[taskNumber].description);
                        } else {
                            System.out.println("Invalid task number.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please provide a valid task number after 'mark'.");
                    }
                } else if (input.startsWith("unmark ")) {
                    try {
                        int taskNumber = Integer.parseInt(input.substring(7)) - 1;
                        tasks[taskNumber].markAsNotDone();
                        if (taskNumber >= 0 && taskNumber < taskCount) {
                            System.out.println("OK, I've marked this task as not done yet:");
                            System.out.println("  [" + tasks[taskNumber].getStatusIcon() + "] " + tasks[taskNumber].description);
                        } else {
                            System.out.println("Invalid task number.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please provide a valid task number after 'unmark'.");
                    }
                } else if (taskCount == MAX_TASKS) {
                    System.out.println("Sorry, the task list is full.");
                } else {
                    tasks[taskCount] = new Task(input);
                    taskCount++;
                    System.out.println("added: " + input);
                }

                printLine();
            }
        }
    }

    private static void printTasks(Task[] tasks, int taskCount) {
        if (taskCount == 0) {
            System.out.println("Your task list is empty.");
            return;
        }

        for (int index = 0; index < taskCount; index++) {
            System.out.println((index + 1) + ". [" + tasks[index].getStatusIcon() + "] " + tasks[index].description);
        }
    }

    /** Prints the divider used to separate program input and output. */
    private static void printLine() {
        System.out.println("____________________________________________________________");
    }
}
