package nl.vu.cs.softwaredesign;

import java.util.List;

public class StrengthStrategy implements TrainingStrategy {

    @Override
    public List<WorkoutSession> createSchedule(UserPreferences prefs, ExerciseCatalog catalog) {
        List<Exercise> exercises = catalog.getExercisesByGoal(GoalType.STRENGTH);

        Exercise squat = pickExercise(exercises, "squat");
        Exercise bench = pickExercise(exercises, "bench");
        Exercise row = pickExercise(exercises, "row");
        Exercise deadlift = pickExercise(exercises, "deadlift");
        Exercise press = pickExercise(exercises, "press");
        Exercise pull = pickExercise(exercises, "pull");

        WorkoutSession sessionA = new WorkoutSession(7.5f);
        sessionA.addItem(new WorkoutItem(4, "5-8", 120, 18, "Main lower-body lift", squat));
        sessionA.addItem(new WorkoutItem(4, "6-8", 90, 15, "Main pushing lift", bench));
        sessionA.addItem(new WorkoutItem(3, "8-10", 75, 12, "Upper back assistance", row));

        WorkoutSession sessionB = new WorkoutSession(8.0f);
        sessionB.addItem(new WorkoutItem(4, "4-6", 150, 18, "Heavy posterior-chain work", deadlift));
        sessionB.addItem(new WorkoutItem(4, "6-8", 90, 14, "Shoulder strength", press));
        sessionB.addItem(new WorkoutItem(3, "6-10", 75, 12, "Vertical pulling", pull));

        WorkoutSession sessionC = new WorkoutSession(7.0f);
        sessionC.addItem(new WorkoutItem(3, "8-10", 90, 15, "Technique and volume squat work", squat));
        sessionC.addItem(new WorkoutItem(3, "8-10", 75, 14, "Bench volume work", bench));
        sessionC.addItem(new WorkoutItem(3, "10-12", 60, 12, "Back accessory work", row));

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