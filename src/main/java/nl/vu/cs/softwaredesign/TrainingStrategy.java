package nl.vu.cs.softwaredesign;

import java.util.List;

public interface TrainingStrategy {
    List<WorkoutSession> createSchedule(UserPreferences prefs, ExerciseCatalog catalog);
}