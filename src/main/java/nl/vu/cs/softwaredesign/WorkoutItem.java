package nl.vu.cs.softwaredesign;

public class WorkoutItem {
    private int sets;
    private String reps;
    private int restSec;
    private int durationMin;
    public String notes;
    private final Exercise exercise;

    public WorkoutItem(int sets, String reps, int restSec, int durationMin, String notes, Exercise exercise) {
        this.sets = sets;
        this.reps = reps;
        this.restSec = restSec;
        this.durationMin = durationMin;
        this.notes = notes;
        this.exercise = exercise;
    }

    public int getSets() {
        return sets;
    }

    public String getReps() {
        return reps;
    }

    public int getRestSec() {
        return restSec;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public void setRestSec(int restSec) {
        this.restSec = restSec;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

    public float estimateCaloriesBurn() {
        String name = exercise.getName().toLowerCase();
        float ratePerMinute = 5.0f;

        if (name.contains("run") || name.contains("jogging") || name.contains("cycling")
                || name.contains("rowing") || name.contains("jump rope")) {
            ratePerMinute = 9.0f;
        } else if (name.contains("circuit") || name.contains("burpee")) {
            ratePerMinute = 10.0f;
        } else if (name.contains("squat") || name.contains("bench") || name.contains("deadlift")
                || name.contains("press") || name.contains("row") || name.contains("pull")) {
            ratePerMinute = 6.0f;
        } else if (name.contains("recovery")) {
            ratePerMinute = 3.0f;
        }

        return durationMin * ratePerMinute;
    }

    public WorkoutItem copyWithProgression(GoalType goal, int weekNumber) {
        int newSets = sets;
        int newDuration = durationMin;

        if (goal == GoalType.STRENGTH && weekNumber > 1 && weekNumber % 2 == 0) {
            newSets = sets + 1;
        }

        if ((goal == GoalType.ENDURANCE || goal == GoalType.WEIGHT_LOSS) && weekNumber > 1) {
            newDuration = durationMin + ((weekNumber - 1) * 3);
        }

        return new WorkoutItem(newSets, reps, restSec, newDuration, notes, exercise);
    }

    public String toDisplayString() {
        return "- " + exercise.getName() +
                " | sets: " + sets +
                " | reps: " + reps +
                " | rest: " + restSec + " sec" +
                " | duration: " + durationMin + " min" +
                " | notes: " + notes;
    }
}