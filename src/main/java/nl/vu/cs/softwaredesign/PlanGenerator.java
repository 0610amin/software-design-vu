package nl.vu.cs.softwaredesign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlanGenerator {
    private static final int DEFAULT_TOTAL_WEEKS = 4;

    public TrainingPlan generatePlan(UserProfile profile, UserPreferences prefs, ExerciseCatalog catalog) {
        TrainingStrategy strategy = createStrategy(prefs.getTargetGoal());
        TrainingPlan plan = new TrainingPlan(DEFAULT_TOTAL_WEEKS);

        List<WorkoutSession> templateSessions = strategy.createSchedule(prefs, catalog);
        List<Integer> trainingDayIndices = getTrainingDayIndices(prefs.getDaysPerWeek());

        for (int week = 1; week <= DEFAULT_TOTAL_WEEKS; week++) {
            int templateIndex = 0;

            for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
                if (trainingDayIndices.contains(dayIndex)) {
                    WorkoutSession template = templateSessions.get(templateIndex % templateSessions.size());
                    WorkoutSession actualSession = copySessionForWeek(template, prefs.getTargetGoal(), week);
                    plan.addSession(actualSession);
                    templateIndex++;
                } else {
                    plan.addSession(createRestSession());
                }
            }
        }

        plan.calculateWeightPrediction(profile);
        return plan;
    }

    protected TrainingStrategy createStrategy(GoalType goal) {
        switch (goal) {
            case STRENGTH:
                return new StrengthStrategy();
            case ENDURANCE:
                return new EnduranceStrategy();
            case WEIGHT_LOSS:
                return new WeightLossStrategy();
            default:
                return new WeightLossStrategy();
        }
    }

    private WorkoutSession copySessionForWeek(WorkoutSession template, GoalType goal, int weekNumber) {
        WorkoutSession session = new WorkoutSession(template.getEstimatedFatigue() + ((weekNumber - 1) * 0.4f));

        for (WorkoutItem item : template.getItems()) {
            session.addItem(item.copyWithProgression(goal, weekNumber));
        }

        return session;
    }

    private WorkoutSession createRestSession() {
        WorkoutSession restSession = new WorkoutSession(1.0f);

        Exercise recoveryExercise = new Exercise(
                "rest",
                "Recovery Walk",
                Difficulty.BEGINNER,
                Arrays.asList("Recovery"),
                Arrays.asList("None")
        );

        restSession.addItem(new WorkoutItem(
                1,
                "easy",
                0,
                20,
                "Scheduled rest day / active recovery",
                recoveryExercise
        ));

        return restSession;
    }

    private List<Integer> getTrainingDayIndices(int daysPerWeek) {
        List<Integer> indices = new ArrayList<>();

        switch (daysPerWeek) {
            case 1:
                indices.add(2);
                break;
            case 2:
                indices.add(1);
                indices.add(4);
                break;
            case 3:
                indices.add(0);
                indices.add(2);
                indices.add(4);
                break;
            case 4:
                indices.add(0);
                indices.add(2);
                indices.add(4);
                indices.add(5);
                break;
            case 5:
                indices.add(0);
                indices.add(1);
                indices.add(3);
                indices.add(4);
                indices.add(5);
                break;
            case 6:
                indices.add(0);
                indices.add(1);
                indices.add(2);
                indices.add(3);
                indices.add(4);
                indices.add(5);
                break;
            case 7:
                indices.add(0);
                indices.add(1);
                indices.add(2);
                indices.add(3);
                indices.add(4);
                indices.add(5);
                indices.add(6);
                break;
            default:
                indices.add(0);
                indices.add(2);
                indices.add(4);
                break;
        }

        return indices;
    }
}