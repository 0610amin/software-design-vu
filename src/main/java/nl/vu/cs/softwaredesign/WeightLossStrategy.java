package nl.vu.cs.softwaredesign;

import java.util.List;

public class WeightLossStrategy implements TrainingStrategy {

    @Override
    public List<WorkoutSession> createSchedule(UserPreferences prefs, ExerciseCatalog catalog) {
        List<Exercise> exercises = catalog.getExercisesByGoal(GoalType.WEIGHT_LOSS);

        Exercise circuit = pickExercise(exercises, "circuit");
        Exercise burpees = pickExercise(exercises, "burpee");
        Exercise bodyweightSquats = pickExercise(exercises, "bodyweight");
        Exercise jogging = pickExercise(exercises, "jogging");

        WorkoutSession sessionA = new WorkoutSession(7.0f);
        sessionA.addItem(new WorkoutItem(4, "10-12", 45, 20, "Main calorie-burning circuit", circuit));
        sessionA.addItem(new WorkoutItem(3, "10", 30, 8, "High-intensity finisher", burpees));

        WorkoutSession sessionB = new WorkoutSession(6.0f);
        sessionB.addItem(new WorkoutItem(1, "steady", 0, 30, "Low to medium intensity cardio", jogging));
        sessionB.addItem(new WorkoutItem(3, "15-20", 30, 8, "Leg volume work", bodyweightSquats));

        WorkoutSession sessionC = new WorkoutSession(6.5f);
        sessionC.addItem(new WorkoutItem(3, "12-15", 30, 15, "Mixed conditioning block", circuit));
        sessionC.addItem(new WorkoutItem(2, "8-10", 30, 6, "Conditioning spikes", burpees));

        return List.of(sessionA, sessionB, sessionC);
    }

    private Exercise pickExercise(List<Exercise> exercises, String keyword) {
        for (Exercise exercise : exercises) {
            if (exercise.getName().toLowerCase().contains(keyword)) {
                return exercise;
            }
        }
        return exercises.get(0);
    }
}