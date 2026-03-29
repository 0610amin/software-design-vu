package nl.vu.cs.softwaredesign;

import java.util.ArrayList;
import java.util.List;

public class UserPreferences {
    private GoalType targetGoal;
    private int daysPerWeek;
    private List<String> preferredWorkoutTypes;

    public UserPreferences(GoalType targetGoal, int daysPerWeek, List<String> preferredWorkoutTypes) {
        this.targetGoal = targetGoal;
        this.daysPerWeek = daysPerWeek;
        this.preferredWorkoutTypes = new ArrayList<>(preferredWorkoutTypes);
    }

    public void updatePreferences(GoalType targetGoal, int daysPerWeek, List<String> preferredWorkoutTypes) {
        this.targetGoal = targetGoal;
        this.daysPerWeek = daysPerWeek;
        this.preferredWorkoutTypes = new ArrayList<>(preferredWorkoutTypes);
    }

    public GoalType getTargetGoal() {
        return targetGoal;
    }

    public int getDaysPerWeek() {
        return daysPerWeek;
    }

    public List<String> getPreferredWorkoutTypes() {
        return new ArrayList<>(preferredWorkoutTypes);
    }

    public String getSummary() {
        return "----- Current Preferences Summary -----\n" +
                "Target goal: " + targetGoal + "\n" +
                "Training days per week: " + daysPerWeek + "\n" +
                "Preferred workout types: " + String.join(", ", preferredWorkoutTypes) + "\n" +
                "---------------------------------------";
    }
}