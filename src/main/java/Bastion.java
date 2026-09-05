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
                    System.out.println("Beep Beep! Hope to see you again soon!");
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
                            System.out.println("Beep Beep! I've marked this task as done:");
                            System.out.println("  " + tasks[taskNumber]);
                            printLine();
                        } else {
                            printLine();
                            System.out.println(" Beep Beep Boop!!! Invalid task number.");
                            printLine();
                        }
                    } catch (NumberFormatException e) {
                        printLine();
                        System.out.println(" Beep Beep Boop!!! Please provide a valid task number after 'mark'.");
                        printLine();
                    }
                } else if (input.startsWith("unmark ")) {
                    try {
                        int taskNumber = Integer.parseInt(input.substring(7)) - 1;
                        if (taskNumber >= 0 && taskNumber < taskCount) {
                            tasks[taskNumber].markAsNotDone();
                            printLine();
                            System.out.println("Beep Beep! I've marked this task as not done yet:");
                            System.out.println("  " + tasks[taskNumber]);
                            printLine();
                        } else {
                            printLine();
                            System.out.println("Beep Beep Boop!!! Invalid task number.");
                            printLine();
                        }
                    } catch (NumberFormatException e) {
                        printLine();
                        System.out.println("Beep Beep Boop!!! Please provide a valid task number after 'unmark'.");
                        printLine();
                    }
                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    String description = input.equals("todo") ? "" : input.substring(5).strip();
                    if (description.isEmpty()) {
                        printLine();
                        System.out.println("Beep Beep Boop!!! The description of a todo cannot be empty.");
                        printLine();
                    } else if (taskCount == MAX_TASKS) {
                        printLine();
                        System.out.println("Beep Beep Boop!!! The task list is full.");
                        printLine();
                    } else {
                        tasks[taskCount] = new Todo(description);
                        taskCount++;
                        printTaskAdded(tasks[taskCount - 1], taskCount);
                    }
                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    if (taskCount == MAX_TASKS) {
                        printLine();
                        System.out.println("Beep Beep Boop!!! The task list is full.");
                        printLine();
                        continue;
                    }
                    Task task = createDeadline(input);
                    if (task == null) {
                        printLine();
                        System.out.println("Beep Beep Boop!!! The description or deadline date cannot be empty. Format: deadline [desc] /by [date]");
                        printLine();
                    } else {
                        tasks[taskCount] = task;
                        taskCount++;
                        printTaskAdded(tasks[taskCount - 1], taskCount);
                    }
                } else if (input.equals("event") || input.startsWith("event ")) {
                    if (taskCount == MAX_TASKS) {
                        printLine();
                        System.out.println("Beep Beep Boop!!! The task list is full.");
                        printLine();
                        continue;
                    }
                    Task task = createEvent(input);
                    if (task == null) {
                        printLine();
                        System.out.println("Beep Beep Boop!!! The description or event timing cannot be empty. Format: event [desc] /from [date] /to [date]");
                        printLine();
                    } else {
                        tasks[taskCount] = task;
                        taskCount++;
                        printTaskAdded(tasks[taskCount - 1], taskCount);
                    }
                } else {
                    printLine();
                    System.out.println("Beep Beep Boop!!! I'm sorry, but I don't know what that means :-(");
                    printLine();
                }
            }
        }
    }

    private static Deadline createDeadline(String input) {
        if (input.equals("deadline")) return null;
        int byIndex = input.indexOf(" /by ");
        if (byIndex <= "deadline ".length() || byIndex + " /by ".length() >= input.length()) {
            return null;
        }
        String description = input.substring("deadline ".length(), byIndex).strip();
        String by = input.substring(byIndex + " /by ".length()).strip();
        return description.isEmpty() || by.isEmpty() ? null : new Deadline(description, by);
    }

    private static Event createEvent(String input) {
        if (input.equals("event")) return null;
        int fromIndex = input.indexOf(" /from ");
        if (fromIndex <= "event ".length()) return null;
        int toIndex = input.indexOf(" /to ", fromIndex + " /from ".length());
        if (toIndex < 0 || toIndex + " /to ".length() >= input.length()) {
            return null;
        }
        String description = input.substring("event ".length(), fromIndex).strip();
        String from = input.substring(fromIndex + " /from ".length(), toIndex).strip();
        String to = input.substring(toIndex + " /to ".length()).strip();
        return description.isEmpty() || from.isEmpty() || to.isEmpty() ? null : new Event(description, from, to);
    }

    private static void printTaskAdded(Task task, int taskCount) {
        printLine();
        System.out.println("Beep Beep! I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        printLine();
    }

    private static void printTasks(Task[] tasks, int taskCount) {
        printLine();
        if (taskCount == 0) {
            System.out.println("Your task list is empty.");
            printLine();
            return;
        }
        System.out.println("Here are the tasks in your list:");
        for (int index = 0; index < taskCount; index++) {
            System.out.println((index + 1) + "." + tasks[index]);
        }
        printLine();
    }

    private static void printLine() {
        System.out.println("____________________________________________________________________________________");
    }
}
