package nl.vu.cs.softwaredesign;

public class ProgressTracker {

    public void evaluateDeviation(TrainingPlan plan, WorkoutSession actualPerformance) {
        if (!actualPerformance.isCompleted()) {
            System.out.println("Deviation detected: the session is not marked as completed.");
            return;
        }

        if (actualPerformance.getEstimatedFatigue() > 8.5f) {
            System.out.println("Deviation detected: fatigue is high.");
        } else {
            System.out.println("Actual performance is within the expected range.");
        }
    }

    public void adjustWorkload(TrainingPlan plan) {
        for (WorkoutSession session : plan.getSessions()) {
            if (session.getEstimatedFatigue() > 8.0f) {
                for (WorkoutItem item : session.getItems()) {
                    if (item.getSets() > 1) {
                        item.setSets(item.getSets() - 1);
                    }
                    item.setRestSec(item.getRestSec() + 15);

                    if (item.getDurationMin() > 10) {
                        item.setDurationMin(item.getDurationMin() - 5);
                    }
                }
            }
        }

        System.out.println("Workload adjusted for high-fatigue sessions.");
    }
}