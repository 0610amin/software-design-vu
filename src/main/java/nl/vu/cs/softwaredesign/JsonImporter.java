package nl.vu.cs.softwaredesign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonImporter {

    public void importLibrary(String filePath, ImportMode mode, ExerciseCatalog catalog) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        List<ImportedExercise> importedExercises = mapper.readValue(
                new File(filePath),
                new TypeReference<List<ImportedExercise>>() {}
        );

        if (mode == ImportMode.REPLACE) {
            catalog.clear();
        }

        for (ImportedExercise imported : importedExercises) {
            validateImportedExercise(imported);

            Exercise exercise = new Exercise(
                    imported.id,
                    imported.name,
                    Difficulty.valueOf(imported.difficultyLevel.trim().toUpperCase()),
                    imported.targetMuscleGroups,
                    imported.requiredEquipment
            );

            catalog.updateExercise(exercise);
        }
    }

    private void validateImportedExercise(ImportedExercise exercise) {
        if (exercise.id == null || exercise.id.isBlank()) {
            throw new IllegalArgumentException("Exercise id is missing in JSON.");
        }
        if (exercise.name == null || exercise.name.isBlank()) {
            throw new IllegalArgumentException("Exercise name is missing in JSON.");
        }
        if (exercise.difficultyLevel == null || exercise.difficultyLevel.isBlank()) {
            throw new IllegalArgumentException("Exercise difficultyLevel is missing in JSON.");
        }
        if (exercise.targetMuscleGroups == null) {
            throw new IllegalArgumentException("targetMuscleGroups is missing for exercise: " + exercise.name);
        }
        if (exercise.requiredEquipment == null) {
            throw new IllegalArgumentException("requiredEquipment is missing for exercise: " + exercise.name);
        }
    }

    private static class ImportedExercise {
        public String id;
        public String name;
        public String difficultyLevel;
        public List<String> targetMuscleGroups;
        public List<String> requiredEquipment;
    }
}