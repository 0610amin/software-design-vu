package nl.vu.cs.softwaredesign;

import java.util.List;

public class EnduranceStrategy implements TrainingStrategy {

    @Override
    public List<WorkoutSession> createSchedule(UserPreferences prefs, ExerciseCatalog catalog) {
        List<Exercise> exercises = catalog.getExercisesByGoal(GoalType.ENDURANCE);

        Exercise run = pickExercise(exercises, "run");
        Exercise cycling = pickExercise(exercises, "cycling");
        Exercise rowing = pickExercise(exercises, "rowing");
        Exercise jumpRope = pickExercise(exercises, "jump rope");

        WorkoutSession sessionA = new WorkoutSession(5.5f);
        sessionA.addItem(new WorkoutItem(1, "steady", 0, 35, "Steady-state cardio session", run));

        WorkoutSession sessionB = new WorkoutSession(6.5f);
        sessionB.addItem(new WorkoutItem(5, "3 min hard / 2 min easy", 60, 30, "Interval conditioning", cycling));

        WorkoutSession sessionC = new WorkoutSession(6.0f);
        sessionC.addItem(new WorkoutItem(1, "steady", 0, 30, "Cardio endurance", rowing));
        sessionC.addItem(new WorkoutItem(3, "2 min", 45, 10, "Finisher", jumpRope));

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