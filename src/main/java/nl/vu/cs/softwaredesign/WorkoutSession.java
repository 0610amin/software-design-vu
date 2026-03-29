package nl.vu.cs.softwaredesign;

import java.util.ArrayList;
import java.util.List;

public class WorkoutSession {
    private float estimatedFatigue;
    private boolean isCompleted;
    private final List<WorkoutItem> items;

    public WorkoutSession(float estimatedFatigue) {
        this.estimatedFatigue = estimatedFatigue;
        this.isCompleted = false;
        this.items = new ArrayList<>();
    }

    public void addItem(WorkoutItem item) {
        items.add(item);
    }

    public void markCompleted() {
        isCompleted = true;
    }

    public float getEstimatedFatigue() {
        return estimatedFatigue;
    }

    public void setEstimatedFatigue(float estimatedFatigue) {
        this.estimatedFatigue = estimatedFatigue;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public List<WorkoutItem> getItems() {
        return new ArrayList<>(items);
    }

    public float estimateCaloriesBurn() {
        float total = 0.0f;
        for (WorkoutItem item : items) {
            total += item.estimateCaloriesBurn();
        }
        return total;
    }

    public String toDisplayString(String label) {
        StringBuilder builder = new StringBuilder();
        builder.append(label)
                .append(" | estimated fatigue: ")
                .append(String.format("%.1f", estimatedFatigue));

        if (isCompleted) {
            builder.append(" | completed");
        }

        builder.append("\n");

        for (WorkoutItem item : items) {
            builder.append("  ").append(item.toDisplayString()).append("\n");
        }

        return builder.toString();
    }
}