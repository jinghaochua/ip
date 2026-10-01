package Bastion;
import java.io.IOException;
import java.util.Scanner;

/**
 * Starts the Bastion command-line task manager and coordinates its collaborators.
 */
public class Bastion {
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) throws IOException {
        Storage storage = new Storage();
        TaskList tasks = new TaskList(storage.loadTasks());

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

                try{
                    if (input.equals("list")) {
                        printTasks(tasks);
                    } 
                    else if (input.startsWith("mark ")) {
                        try {
                            int taskNumber = Integer.parseInt(input.substring(5)) - 1;
                            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                                tasks.get(taskNumber).markAsDone();
                                storage.saveTasks(tasks.getTasks());
                                printLine();
                                System.out.println("Beep Beep! I've marked this task as done:");
                                System.out.println("  " + tasks.get(taskNumber));
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
                    } 
                    
                    else if (input.startsWith("unmark ")) {
                        try {
                            int taskNumber = Integer.parseInt(input.substring(7)) - 1;
                            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                                tasks.get(taskNumber).markAsNotDone();
                                storage.saveTasks(tasks.getTasks());
                                printLine();
                                System.out.println("Beep Beep! I've marked this task as not done yet:");
                                System.out.println("  " + tasks.get(taskNumber));
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
                    } 
                    
                    else if (input.equals("todo") || input.startsWith("todo ")) {
                        String description = input.equals("todo") ? "" : input.substring(5).strip();
                        if (description.isEmpty()) {
                            throw new BastionException(
                                "Beep Beep Boop!!! The description of a todo cannot be empty.");
                        } else if (tasks.size() == MAX_TASKS) {
                            printLine();
                            System.out.println("Beep Beep Boop!!! The task list is full.");
                            printLine();
                        } else {
                            tasks.add(new Todo(description));
                            storage.saveTasks(tasks.getTasks());
                            printTaskAdded(tasks.get(tasks.size() - 1), tasks.size());
                        }
                    }
                    
                    else if (input.equals("deadline") || input.startsWith("deadline ")) {
                        if (tasks.size() == MAX_TASKS) {
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
                            tasks.add(task);
                            storage.saveTasks(tasks.getTasks());
                            printTaskAdded(tasks.get(tasks.size() - 1), tasks.size());
                        }
                    } 
                    
                    else if (input.equals("event") || input.startsWith("event ")) {
                        if (tasks.size() == MAX_TASKS) {
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
                            tasks.add(task);
                            storage.saveTasks(tasks.getTasks());
                            printTaskAdded(tasks.get(tasks.size() - 1), tasks.size());
                        }
                    } 

                    else if (input.startsWith("delete ")) {
                        try {
                            String numberString = input.substring(7).strip(); 
                            int taskNumber = Integer.parseInt(numberString) - 1;
                            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                                Task removedTask = tasks.remove(taskNumber);
                                storage.saveTasks(tasks.getTasks());
                                
                                printLine();
                                System.out.println("Beep Beep! I've removed this task:");
                                System.out.println("  " + removedTask);
                                System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                                printLine();
                            } else {
                                printLine();
                                System.out.println("Beep Beep Boop!!! Invalid task number.");
                                printLine();
                            }

                        } catch (NumberFormatException e) {
                            printLine();
                            System.out.println("Beep Beep Boop!!! Please provide a valid task number after 'delete'.");
                            printLine();
                        }
                    }

                    else {
                        throw new BastionException(
                            "Beep Beep Boop!!! I'm sorry, but I don't know what that means :-(");
                    }
                        
                } catch (BastionException e) {
                    printLine();
                    System.out.println(e.getMessage());
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

    private static void printTasks(TaskList tasks) {
        printLine();
        if (tasks.isEmpty()) {
            System.out.println("Your task list is empty.");
            printLine();
            return;
        }
        System.out.println("Here are the tasks in your list:");
        for (int index = 0; index < tasks.size(); index++) {
            System.out.println((index + 1) + "." + tasks.get(index));
        }
        printLine();
    }

    private static void printLine() {
        System.out.println("____________________________________________________________________________________");
    }
}
