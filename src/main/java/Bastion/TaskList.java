package Bastion;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the tasks currently managed by Bastion and provides task-list operations.
 */
public class TaskList {
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     * A copy is made so later changes to the supplied list do not affect this task list.
     *
     * @param tasks tasks to initially include
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Returns the task at a zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Removes and returns the task at a zero-based index. */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }

    /** Returns whether the list contains no tasks. */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the tasks for persistence. Callers should not change the returned list.
     *
     * @return the current tasks
     */
    public List<Task> getTasks() {
        return tasks;
    }
}
