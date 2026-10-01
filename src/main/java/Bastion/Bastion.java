package Bastion;
import java.io.IOException;

/**
 * Starts the Bastion command-line task manager and coordinates its collaborators.
 */
public class Bastion {
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) throws IOException {
        Storage storage = new Storage();
        TaskList tasks = new TaskList(storage.loadTasks());
        Ui ui = new Ui();

        ui.showWelcome();

        while (ui.hasNextCommand()) {
                String input = ui.readCommand();

                if (input.equals("bye")) {
                    ui.showGoodbye();
                    break;
                }

                try{
                    if (input.equals("list")) {
                        ui.showTaskList(tasks);
                    } 
                    else if (input.startsWith("mark ")) {
                        try {
                            int taskNumber = Integer.parseInt(input.substring(5)) - 1;
                            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                                tasks.get(taskNumber).markAsDone();
                                storage.saveTasks(tasks.getTasks());
                                ui.showTaskStatusChanged(tasks.get(taskNumber), true);
                            } else {
                                ui.showError(" Beep Beep Boop!!! Invalid task number.");
                            }
                        } catch (NumberFormatException e) {
                            ui.showError(" Beep Beep Boop!!! Please provide a valid task number after 'mark'.");
                        }
                    } 
                    
                    else if (input.startsWith("unmark ")) {
                        try {
                            int taskNumber = Integer.parseInt(input.substring(7)) - 1;
                            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                                tasks.get(taskNumber).markAsNotDone();
                                storage.saveTasks(tasks.getTasks());
                                ui.showTaskStatusChanged(tasks.get(taskNumber), false);
                            } else {
                                ui.showError("Beep Beep Boop!!! Invalid task number.");
                            }
                        } catch (NumberFormatException e) {
                            ui.showError("Beep Beep Boop!!! Please provide a valid task number after 'unmark'.");
                        }
                    } 
                    
                    else if (input.equals("todo") || input.startsWith("todo ")) {
                        String description = input.equals("todo") ? "" : input.substring(5).strip();
                        if (description.isEmpty()) {
                            throw new BastionException(
                                "Beep Beep Boop!!! The description of a todo cannot be empty.");
                        } else if (tasks.size() == MAX_TASKS) {
                            ui.showError("Beep Beep Boop!!! The task list is full.");
                        } else {
                            tasks.add(new Todo(description));
                            storage.saveTasks(tasks.getTasks());
                            ui.showTaskAdded(tasks.get(tasks.size() - 1), tasks.size());
                        }
                    }
                    
                    else if (input.equals("deadline") || input.startsWith("deadline ")) {
                        if (tasks.size() == MAX_TASKS) {
                            ui.showError("Beep Beep Boop!!! The task list is full.");
                            continue;
                        }
                        Task task = createDeadline(input);
                        if (task == null) {
                            ui.showError("Beep Beep Boop!!! The description or deadline date cannot be empty. Format: deadline [desc] /by [date]");
                        } else {
                            tasks.add(task);
                            storage.saveTasks(tasks.getTasks());
                            ui.showTaskAdded(tasks.get(tasks.size() - 1), tasks.size());
                        }
                    } 
                    
                    else if (input.equals("event") || input.startsWith("event ")) {
                        if (tasks.size() == MAX_TASKS) {
                            ui.showError("Beep Beep Boop!!! The task list is full.");
                            continue;
                        }
                        Task task = createEvent(input);
                        if (task == null) {
                            ui.showError("Beep Beep Boop!!! The description or event timing cannot be empty. Format: event [desc] /from [date] /to [date]");
                        } else {
                            tasks.add(task);
                            storage.saveTasks(tasks.getTasks());
                            ui.showTaskAdded(tasks.get(tasks.size() - 1), tasks.size());
                        }
                    } 

                    else if (input.startsWith("delete ")) {
                        try {
                            String numberString = input.substring(7).strip(); 
                            int taskNumber = Integer.parseInt(numberString) - 1;
                            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                                Task removedTask = tasks.remove(taskNumber);
                                storage.saveTasks(tasks.getTasks());
                                ui.showTaskRemoved(removedTask, tasks.size());
                            } else {
                                ui.showError("Beep Beep Boop!!! Invalid task number.");
                            }

                        } catch (NumberFormatException e) {
                            ui.showError("Beep Beep Boop!!! Please provide a valid task number after 'delete'.");
                        }
                    }

                    else {
                        throw new BastionException(
                            "Beep Beep Boop!!! I'm sorry, but I don't know what that means :-(");
                    }
                        
                } catch (BastionException e) {
                    ui.showError(e.getMessage());
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

}
