package bastion;

import java.util.Scanner;

public class Bastion {
    private static final int MAX_TASKS = 100;

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
                    break;
                }

                if (input.equals("list")) {
                    printTasks(tasks, taskCount);
                } else if (input.startsWith("mark ")) {
                    try {
                        int taskNumber = Integer.parseInt(input.substring(5)) - 1;
                        if (taskNumber >= 0 && taskNumber < taskCount) {
                            tasks[taskNumber].markAsDone();
                            printLine();
                            System.out.println("Nice! I've marked this task as done:");
                            System.out.println("  " + tasks[taskNumber]);
                        } else {
                            System.out.println("Invalid task number.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please provide a valid task number after 'mark'.");
                    }
                } else if (input.startsWith("unmark ")) {
                    try {
                        int taskNumber = Integer.parseInt(input.substring(7)) - 1;
                        if (taskNumber >= 0 && taskNumber < taskCount) {
                            tasks[taskNumber].markAsNotDone();
                            printLine();
                            System.out.println("OK, I've marked this task as not done yet:");
                            System.out.println("  " + tasks[taskNumber]);
                        } else {
                            System.out.println("Invalid task number.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please provide a valid task number after 'unmark'.");
                    }
                } else if (taskCount == MAX_TASKS) {
                    System.out.println("Sorry, the task list is full.");
                } else {
                    Task task = createTask(input);
                    if (task == null) {
                        System.out.println("I don't understand that command.");
                    } else {
                        tasks[taskCount] = task;
                        taskCount++;
                        printLine();
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + task);
                        System.out.println("Now you have " + taskCount + " tasks in the list.");
                        printLine();
                    }
                }

            }
        }
    }
    
    /**
     * Creates a task based on the given user input.
     *
     * @param input the user input describing the task
     * @return the created task, or null if the input is invalid
     */
    private static Task createTask(String input) {
        if (input.startsWith("todo ")) {
            String description = input.substring(5).strip();
            return description.isEmpty() ? null : new Todo(description);
        }

        if (input.startsWith("deadline ")) {
            int byIndex = input.indexOf(" /by ");
            if (byIndex <= "deadline ".length()
                    || byIndex + " /by ".length() >= input.length()) {
                return null;
            }
            String description = input.substring("deadline ".length(), byIndex).strip();
            String by = input.substring(byIndex + " /by ".length()).strip();
            return description.isEmpty() || by.isEmpty() ? null : new Deadline(description, by);
        }

        if (input.startsWith("event ")) {
            int fromIndex = input.indexOf(" /from ");
            int toIndex = input.indexOf(" /to ", fromIndex + " /from ".length());
            if (fromIndex <= "event ".length() 
                    || toIndex < 0 
                    || toIndex + " /to ".length() >= input.length()) {
                return null;
            }
            String description = input.substring("event ".length(), fromIndex).strip();
            String from = input.substring(fromIndex + " /from ".length(), toIndex).strip();
            String to = input.substring(toIndex + " /to ".length()).strip();
            return description.isEmpty() || from.isEmpty() || to.isEmpty() ? null : new Event(description, from, to);
        }

        return null;
    }

    /** Prints every task in the order it was added. */
    private static void printTasks(Task[] tasks, int taskCount) {
        if (taskCount == 0) {
            System.out.println("Your task list is empty.");
            return;
        }

        System.out.println("Here are the tasks in your list:");
        for (int index = 0; index < taskCount; index++) {
            System.out.println((index + 1) + "." + tasks[index]);
        }
    }

    /** Prints a horizontal separator line. */
    private static void printLine() {
        System.out.println("____________________________________________________________");
    }
}
