package nl.vu.cs.softwaredesign;

import java.util.ArrayList;
import java.util.List;

public class Exercise {
    private final String id;
    private final String name;
    private final Difficulty difficultyLevel;
    private final List<String> targetMuscleGroups;
    private final List<String> requiredEquipment;

    public Exercise(String id, String name, Difficulty difficultyLevel,
                    List<String> targetMuscleGroups, List<String> requiredEquipment) {
        this.id = id;
        this.name = name;
        this.difficultyLevel = difficultyLevel;
        this.targetMuscleGroups = new ArrayList<>(targetMuscleGroups);
        this.requiredEquipment = new ArrayList<>(requiredEquipment);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Difficulty getDifficultyLevel() {
        return difficultyLevel;
    }

    public List<String> getTargetMuscleGroups() {
        return new ArrayList<>(targetMuscleGroups);
    }

    public List<String> getRequiredEquipment() {
        return new ArrayList<>(requiredEquipment);
    }
}