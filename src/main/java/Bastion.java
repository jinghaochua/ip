import java.util.Scanner;

/**
 * A small command-line task list that stores tasks in memory for one program run.
 */
public class Bastion {
    /** The maximum number of tasks the application keeps. */
    private static final int MAX_TASKS = 100;

    /**
     * Starts the task list application and processes commands until the user enters {@code bye}.
     * Any input other than those commands is added as a task; {@code list} displays stored tasks.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String[] tasks = new String[MAX_TASKS];
        int taskCount = 0;

        printLine();
        System.out.println("Hello! I'm Bastion.");
        System.out.println("What can I do for you?");
        printLine();

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();

                if (input.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    printLine();
                    break;
                }

                if (input.equals("list")) {
                    printTasks(tasks, taskCount);
                } else if (taskCount == MAX_TASKS) {
                    System.out.println("Sorry, the task list is full.");
                } else {
                    tasks[taskCount] = input;
                    taskCount++;
                    System.out.println("added: " + input);
                }

                printLine();
            }
        }
    }

    /** Prints every stored task in its numbered list position. */
    private static void printTasks(String[] tasks, int taskCount) {
        if (taskCount == 0) {
            System.out.println("Your task list is empty.");
            return;
        }

        for (int index = 0; index < taskCount; index++) {
            System.out.println((index + 1) + ". " + tasks[index]);
        }
    }

    /** Prints the divider used to separate program input and output. */
    private static void printLine() {
        System.out.println("____________________________________________________________");
    }
}
