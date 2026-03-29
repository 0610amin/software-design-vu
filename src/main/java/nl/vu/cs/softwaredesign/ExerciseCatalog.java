package nl.vu.cs.softwaredesign;

import java.util.ArrayList;
import java.util.List;

public class ExerciseCatalog {
    private final List<Exercise> exercises = new ArrayList<>();

    public List<Exercise> getExercisesByGoal(GoalType goal) {
        List<Exercise> result = new ArrayList<>();

        for (Exercise exercise : exercises) {
            String name = exercise.getName().toLowerCase();

            switch (goal) {
                case STRENGTH:
                    if (name.contains("squat") || name.contains("bench") || name.contains("deadlift")
                            || name.contains("press") || name.contains("row") || name.contains("pull")) {
                        result.add(exercise);
                    }
                    break;

                case ENDURANCE:
                    if (name.contains("run") || name.contains("cycling") || name.contains("rowing")
                            || name.contains("jogging") || name.contains("jump rope")) {
                        result.add(exercise);
                    }
                    break;

                case WEIGHT_LOSS:
                    if (name.contains("circuit") || name.contains("burpee") || name.contains("jogging")
                            || name.contains("run") || name.contains("jump rope") || name.contains("bodyweight")) {
                        result.add(exercise);
                    }
                    break;

                default:
                    break;
            }
        }

        if (result.isEmpty()) {
            return new ArrayList<>(exercises);
        }

        return result;
    }

    public void addExercise(Exercise ex) {
        exercises.add(ex);
    }

    public void updateExercise(Exercise ex) {
        for (int i = 0; i < exercises.size(); i++) {
            if (exercises.get(i).getId().equals(ex.getId())) {
                exercises.set(i, ex);
                return;
            }
        }
        exercises.add(ex);
    }

    public void clear() {
        exercises.clear();
    }

    public int getExerciseCount() {
        return exercises.size();
    }
}