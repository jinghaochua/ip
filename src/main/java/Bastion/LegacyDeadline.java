package Bastion;

/**
 * Represents a deadline saved by an earlier version that did not use a parseable date.
 * New deadline commands always create {@link Deadline} instances instead.
 */
public class LegacyDeadline extends Task {
    private final String by;

    /** Creates a legacy deadline while preserving its original text. */
    public LegacyDeadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    @Override
    public String toFileString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }
}
