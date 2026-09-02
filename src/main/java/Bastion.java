import java.util.Scanner;

public class Bastion {
    /** The maximum number of tasks the application keeps. */
    private static final int MAX_TASKS = 100;

    static class Task {
        protected String description;
        protected boolean isDone;

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
            return (isDone ? "X" : " ");
        }

        @Override
        public String toString() {
            return "[" + getStatusIcon() + "] " + description;
        }
    }

    static class Todo extends Task {
        Todo(String description) {
            super(description);
        }

        @Override
        public String toString() {
            return "[T]" + super.toString();
        }
    }

    static class Deadline extends Task {
        protected String by;

        Deadline(String description, String by) {
            super(description);
            this.by = by;
        }

        @Override
        public String toString() {
            return "[D]" + super.toString() + " (by: " + by + ")";
        }
    }

    static class Event extends Task {
        protected String from;
        protected String to;

        Event(String description, String from, String to) {
            super(description);
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
        }
    }

    /**
     * Starts the task list application and processes commands until the user enters
     * {@code bye}.
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
                    handleMark(tasks, taskCount, input, true);
                } else if (input.startsWith("unmark ")) {
                    handleMark(tasks, taskCount, input, false);
                } else if (input.startsWith("todo ")) {
                    taskCount = addTask(tasks, taskCount, "todo", input.substring(5).trim());
                } else if (input.startsWith("deadline ")) {
                    String details = input.substring(9).trim();
                    int byIndex = details.lastIndexOf(" /by ");
                    if (byIndex >= 0) {
                        String description = details.substring(0, byIndex).trim();
                        String by = details.substring(byIndex + 4).trim();
                        taskCount = addTask(tasks, taskCount, "deadline", description, by);
                    } else {
                        taskCount = addTask(tasks, taskCount, "deadline", details, "");
                    }
                } else if (input.startsWith("event ")) {
                    String details = input.substring(6).trim();
                    int fromIndex = details.indexOf(" /from ");
                    int toIndex = details.lastIndexOf(" /to ");
                    if (fromIndex >= 0 && toIndex > fromIndex) {
                        String description = details.substring(0, fromIndex).trim();
                        String from = details.substring(fromIndex + 6, toIndex).trim();
                        String to = details.substring(toIndex + 4).trim();
                        taskCount = addTask(tasks, taskCount, "event", description, from, to);
                    } else {
                        taskCount = addTask(tasks, taskCount, "event", details, "", "");
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

    private static int addTask(Task[] tasks, int taskCount, String type, String description) {
        if (description.isEmpty()) {
            System.out.println("The description of a task cannot be empty.");
            return taskCount;
        }

        if (taskCount == MAX_TASKS) {
            System.out.println("Sorry, the task list is full.");
            return taskCount;
        }

        switch (type) {
        case "todo":
            tasks[taskCount] = new Todo(description);
            break;
        case "deadline":
            tasks[taskCount] = new Deadline(description, "");
            break;
        case "event":
            tasks[taskCount] = new Event(description, "", "");
            break;
        default:
            tasks[taskCount] = new Task(description);
            break;
        }

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + tasks[taskCount]);
        System.out.println("Now you have " + (taskCount + 1) + " tasks in the list.");
        return taskCount + 1;
    }

    private static int addTask(Task[] tasks, int taskCount, String type, String description, String by) {
        if (description.isEmpty()) {
            System.out.println("The description of a task cannot be empty.");
            return taskCount;
        }

        if (taskCount == MAX_TASKS) {
            System.out.println("Sorry, the task list is full.");
            return taskCount;
        }

        tasks[taskCount] = new Deadline(description, by);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + tasks[taskCount]);
        System.out.println("Now you have " + (taskCount + 1) + " tasks in the list.");
        return taskCount + 1;
    }

    private static int addTask(Task[] tasks, int taskCount, String type, String description, String from, String to) {
        if (description.isEmpty()) {
            System.out.println("The description of a task cannot be empty.");
            return taskCount;
        }

        if (taskCount == MAX_TASKS) {
            System.out.println("Sorry, the task list is full.");
            return taskCount;
        }

        tasks[taskCount] = new Event(description, from, to);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + tasks[taskCount]);
        System.out.println("Now you have " + (taskCount + 1) + " tasks in the list.");
        return taskCount + 1;
    }

    private static void handleMark(Task[] tasks, int taskCount, String input, boolean isDone) {
        try {
            int taskNumber = Integer.parseInt(input.substring(input.indexOf(' ') + 1)) - 1;
            if (taskNumber < 0 || taskNumber >= taskCount) {
                System.out.println("Invalid task number.");
                return;
            }

            if (isDone) {
                tasks[taskNumber].markAsDone();
                System.out.println("Nice! I've marked this task as done:");
            } else {
                tasks[taskNumber].markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
            }
            System.out.println("  " + tasks[taskNumber]);
        } catch (NumberFormatException e) {
            System.out.println("Please provide a valid task number after 'mark'.");
        }
    }

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

    /** Prints the divider used to separate program input and output. */
    private static void printLine() {
        System.out.println("____________________________________________________________");
    }
}
