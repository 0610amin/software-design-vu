package nl.vu.cs.softwaredesign;

import java.util.ArrayList;
import java.util.List;

public class TrainingPlan {
    private float predictedWeightChange;
    private int totalWeeks;
    private float predictedFinalWeight;
    private final List<WorkoutSession> sessions;

    public TrainingPlan(int totalWeeks) {
        this.totalWeeks = totalWeeks;
        this.predictedWeightChange = 0.0f;
        this.predictedFinalWeight = 0.0f;
        this.sessions = new ArrayList<>();
    }

    public void addSession(WorkoutSession session) {
        sessions.add(session);
    }

    public void calculateWeightPrediction(UserProfile profile) {
        float totalWorkoutBurn = 0.0f;

        for (WorkoutSession session : sessions) {
            totalWorkoutBurn += session.estimateCaloriesBurn();
        }

        int totalDays = totalWeeks * 7;
        float maintenanceCalories = estimateDailyMaintenance(profile);
        float totalExpenditure = (maintenanceCalories * totalDays) + totalWorkoutBurn;
        float totalIntake = profile.getAvgDailyCalories() * totalDays;
        float calorieDeficit = totalExpenditure - totalIntake;

        predictedWeightChange = -(calorieDeficit / 7700.0f);
        predictedFinalWeight = profile.getWeight() + predictedWeightChange;
    }

    private float estimateDailyMaintenance(UserProfile profile) {
        float base = 22.0f * profile.getWeight();
        float stepContribution = profile.getAvgDailySteps() * 0.04f;
        float sexAdjustment;

        String sex = profile.getSex().toLowerCase();

        if (sex.equals("male")) {
            sexAdjustment = 300.0f;
        } else if (sex.equals("female")) {
            sexAdjustment = 200.0f;
        } else {
            sexAdjustment = 250.0f;
        }

        return base + stepContribution + sexAdjustment;
    }

    public float getPredictedWeightChange() {
        return predictedWeightChange;
    }

    public int getTotalWeeks() {
        return totalWeeks;
    }

    public List<WorkoutSession> getSessions() {
        return new ArrayList<>(sessions);
    }

    public String toDisplayString() {
        StringBuilder builder = new StringBuilder();
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        builder.append("========== Generated Training Plan ==========\n");
        builder.append("Total weeks: ").append(totalWeeks).append("\n");
        builder.append("Total scheduled sessions: ").append(sessions.size()).append("\n");
        builder.append("Predicted weight change: ")
                .append(String.format("%.2f", predictedWeightChange))
                .append(" kg\n");
        builder.append("Predicted final weight: ")
                .append(String.format("%.2f", predictedFinalWeight))
                .append(" kg\n");
        builder.append("=============================================\n");

        for (int i = 0; i < sessions.size(); i++) {
            if (i % 7 == 0) {
                builder.append("\n--- Week ").append((i / 7) + 1).append(" ---\n");
            }

            String label = days[i % 7];
            builder.append(sessions.get(i).toDisplayString(label));
        }

        return builder.toString();
    }
}