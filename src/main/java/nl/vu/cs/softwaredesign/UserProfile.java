package nl.vu.cs.softwaredesign;

public class UserProfile {
    private float weight;
    private String sex;
    private int avgDailyCalories;
    private int avgDailySteps;

    public UserProfile(float weight, String sex, int avgDailyCalories, int avgDailySteps) {
        this.weight = weight;
        this.sex = sex;
        this.avgDailyCalories = avgDailyCalories;
        this.avgDailySteps = avgDailySteps;
    }

    public void updateMetrics(float weight, String sex, int avgDailyCalories, int avgDailySteps) {
        this.weight = weight;
        this.sex = sex;
        this.avgDailyCalories = avgDailyCalories;
        this.avgDailySteps = avgDailySteps;
    }

    public float getWeight() {
        return weight;
    }

    public String getSex() {
        return sex;
    }

    public int getAvgDailyCalories() {
        return avgDailyCalories;
    }

    public int getAvgDailySteps() {
        return avgDailySteps;
    }

    public String getSummary() {
        return "----- Current Profile Summary -----\n" +
                "Weight: " + String.format("%.1f", weight) + " kg\n" +
                "Sex: " + sex + "\n" +
                "Average calories per day: " + avgDailyCalories + "\n" +
                "Average steps per day: " + avgDailySteps + "\n" +
                "-----------------------------------";
    }
}