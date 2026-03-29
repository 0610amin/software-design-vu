package nl.vu.cs.softwaredesign;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class CliApplication {
    private final Scanner scanner = new Scanner(System.in);
    private final PlanGenerator planGenerator = new PlanGenerator();
    private final ProgressTracker progressTracker = new ProgressTracker();
    private final JsonImporter jsonImporter = new JsonImporter();
    private final PdfExporter pdfExporter = new PdfExporter();
    private final ExerciseCatalog exerciseCatalog = createDefaultCatalog();

    private User currentUser;
    private TrainingPlan currentPlan;
    private boolean running = true;

    public void start() {
        System.out.println("Welcome to Sabic Fit");

        while (running) {
            printMenu();
            String command = scanner.nextLine().trim();
            handleInput(command);
            System.out.println();
        }
    }

    public void handleInput(String command) {
        switch (command) {
            case "1":
                currentUser = createUser();
                currentPlan = null;
                break;

            case "2":
                if (userExists()) {
                    updateUserProfile(currentUser);
                    currentPlan = null;
                }
                break;

            case "3":
                if (userExists()) {
                    updateUserPreferences(currentUser);
                    currentPlan = null;
                }
                break;

            case "4":
                if (userExists()) {
                    showSummary(currentUser);
                }
                break;

            case "5":
                if (userExists()) {
                    currentPlan = planGenerator.generatePlan(
                            currentUser.getProfile(),
                            currentUser.getPreferences(),
                            exerciseCatalog
                    );
                    System.out.println("Training plan generated successfully.");
                }
                break;

            case "6":
                if (currentPlan == null) {
                    System.out.println("No training plan generated yet.");
                } else {
                    System.out.println(currentPlan.toDisplayString());
                }
                break;

            case "7":
                importExerciseLibrary();
                break;

            case "8":
                exportCurrentPlanToPdf();
                break;

            case "9":
                reviewAndAdjustPlan();
                break;

            case "0":
                running = false;
                System.out.println("Goodbye.");
                break;

            default:
                System.out.println("Invalid option.");
                break;
        }
    }

    private void printMenu() {
        System.out.println("=================================");
        System.out.println("1. Create user");
        System.out.println("2. Update user profile");
        System.out.println("3. Update user preferences");
        System.out.println("4. Show current user summary");
        System.out.println("5. Generate training plan");
        System.out.println("6. Show generated plan");
        System.out.println("7. Import exercise library from JSON");
        System.out.println("8. Export current plan to PDF");
        System.out.println("9. Evaluate progress and adjust workload");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private boolean userExists() {
        if (currentUser == null) {
            System.out.println("No user exists yet. Please create a user first.");
            return false;
        }
        return true;
    }

    private User createUser() {
        System.out.print("Enter user id: ");
        String id = readNonEmptyString("User id cannot be empty. Enter user id: ");

        System.out.println("--- Enter basic profile ---");
        float weight = readValidWeight();
        String sex = readValidSex();
        int calories = readValidCalories();
        int steps = readValidSteps();
        UserProfile profile = new UserProfile(weight, sex, calories, steps);

        System.out.println("--- Enter goals & training options ---");
        GoalType goal = readGoalType();
        int daysPerWeek = readTrainingDays();
        List<String> preferredWorkoutTypes = readWorkoutTypes();
        UserPreferences preferences = new UserPreferences(goal, daysPerWeek, preferredWorkoutTypes);

        User user = new User(id, profile, preferences);
        System.out.println("User created successfully.");
        showSummary(user);

        return user;
    }

    private void updateUserProfile(User user) {
        System.out.println("Updating profile for user: " + user.getId());

        float weight = readValidWeight();
        String sex = readValidSex();
        int calories = readValidCalories();
        int steps = readValidSteps();

        user.getProfile().updateMetrics(weight, sex, calories, steps);
        System.out.println("Profile updated successfully.");
    }

    private void updateUserPreferences(User user) {
        System.out.println("Updating preferences for user: " + user.getId());

        GoalType goal = readGoalType();
        int daysPerWeek = readTrainingDays();
        List<String> preferredWorkoutTypes = readWorkoutTypes();

        user.getPreferences().updatePreferences(goal, daysPerWeek, preferredWorkoutTypes);
        System.out.println("Preferences updated successfully.");
    }

    private void showSummary(User user) {
        System.out.println(user.getProfile().getSummary());
        System.out.println(user.getPreferences().getSummary());
    }

    private void importExerciseLibrary() {
        System.out.print("Enter JSON file path: ");
        String filePath = scanner.nextLine().trim();
        ImportMode mode = readImportMode();

        try {
            jsonImporter.importLibrary(filePath, mode, exerciseCatalog);
            System.out.println("Exercise library imported successfully.");
            System.out.println("Current exercise count: " + exerciseCatalog.getExerciseCount());
        } catch (IOException e) {
            System.out.println("Import failed: could not read file.");
            System.out.println("Reason: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Import failed: invalid JSON content.");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    private void exportCurrentPlanToPdf() {
        if (currentPlan == null) {
            System.out.println("No training plan generated yet.");
            return;
        }

        System.out.print("Enter output PDF path: ");
        String filePath = scanner.nextLine().trim();

        try {
            pdfExporter.exportPlan(currentPlan, filePath);
            System.out.println("PDF exported successfully.");
        } catch (IOException e) {
            System.out.println("PDF export failed.");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    private void reviewAndAdjustPlan() {
        if (currentPlan == null) {
            System.out.println("No training plan generated yet.");
            return;
        }

        List<WorkoutSession> sessions = currentPlan.getSessions();

        if (sessions.isEmpty()) {
            System.out.println("The current plan has no sessions.");
            return;
        }

        WorkoutSession firstSession = sessions.get(0);
        firstSession.markCompleted();

        progressTracker.evaluateDeviation(currentPlan, firstSession);
        progressTracker.adjustWorkload(currentPlan);

        System.out.println("Updated plan preview:");
        System.out.println(currentPlan.toDisplayString());
    }

    private float readValidWeight() {
        while (true) {
            System.out.print("Enter weight in kg: ");
            String input = scanner.nextLine().trim().replace(",", ".");

            try {
                float value = Float.parseFloat(input);
                if (value < 25 || value > 400) {
                    System.out.println("Warning: unrealistic weight. Enter a value between 25 and 400.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid weight.");
            }
        }
    }

    private String readValidSex() {
        while (true) {
            System.out.print("Enter sex (male/female/other): ");
            String input = scanner.nextLine().trim().toLowerCase(Locale.ROOT);

            if (input.equals("male") || input.equals("female") || input.equals("other")) {
                return capitalize(input);
            }

            System.out.println("Invalid input. Please enter male, female, or other.");
        }
    }

    private int readValidCalories() {
        while (true) {
            System.out.print("Enter average calories per day: ");
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value < 500 || value > 10000) {
                    System.out.println("Warning: unrealistic calories. Enter a value between 500 and 10000.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a whole number.");
            }
        }
    }

    private int readValidSteps() {
        while (true) {
            System.out.print("Enter average steps per day: ");
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value < 0 || value > 100000) {
                    System.out.println("Warning: unrealistic steps. Enter a value between 0 and 100000.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a whole number.");
            }
        }
    }

    private GoalType readGoalType() {
        while (true) {
            System.out.print("Choose goal (1=Strength, 2=Endurance, 3=Weight Loss): ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    return GoalType.STRENGTH;
                case "2":
                    return GoalType.ENDURANCE;
                case "3":
                    return GoalType.WEIGHT_LOSS;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private int readTrainingDays() {
        while (true) {
            System.out.print("Enter number of training days per week (1-7): ");
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value < 1 || value > 7) {
                    System.out.println("Please enter a number between 1 and 7.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a whole number.");
            }
        }
    }

    private ImportMode readImportMode() {
        while (true) {
            System.out.print("Choose import mode (1=REPLACE, 2=MERGE_UPDATE): ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    return ImportMode.REPLACE;
                case "2":
                    return ImportMode.MERGE_UPDATE;
                default:
                    System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
    }

    private List<String> readWorkoutTypes() {
        while (true) {
            System.out.print("Enter preferred workout types separated by commas: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Please enter at least one workout type.");
                continue;
            }

            String[] parts = input.split(",");
            List<String> result = new ArrayList<>();

            for (String part : parts) {
                String cleaned = part.trim();
                if (!cleaned.isEmpty()) {
                    result.add(cleaned);
                }
            }

            if (result.isEmpty()) {
                System.out.println("Please enter at least one valid workout type.");
            } else {
                return result;
            }
        }
    }

    private String readNonEmptyString(String retryMessage) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.print(retryMessage);
        }
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase(Locale.ROOT)
                + text.substring(1).toLowerCase(Locale.ROOT);
    }

    private ExerciseCatalog createDefaultCatalog() {
        ExerciseCatalog catalog = new ExerciseCatalog();

        catalog.addExercise(new Exercise("ex1", "Barbell Squat", Difficulty.INTERMEDIATE,
                Arrays.asList("Legs", "Glutes"), Arrays.asList("Barbell", "Rack")));

        catalog.addExercise(new Exercise("ex2", "Bench Press", Difficulty.INTERMEDIATE,
                Arrays.asList("Chest", "Triceps"), Arrays.asList("Barbell", "Bench")));

        catalog.addExercise(new Exercise("ex3", "Bent Over Row", Difficulty.INTERMEDIATE,
                Arrays.asList("Back", "Biceps"), Arrays.asList("Barbell")));

        catalog.addExercise(new Exercise("ex4", "Deadlift", Difficulty.ADVANCED,
                Arrays.asList("Posterior Chain"), Arrays.asList("Barbell")));

        catalog.addExercise(new Exercise("ex5", "Shoulder Press", Difficulty.INTERMEDIATE,
                Arrays.asList("Shoulders", "Triceps"), Arrays.asList("Dumbbells")));

        catalog.addExercise(new Exercise("ex6", "Pull Up", Difficulty.INTERMEDIATE,
                Arrays.asList("Back", "Biceps"), Arrays.asList("Pull-up Bar")));

        catalog.addExercise(new Exercise("ex7", "Easy Run", Difficulty.BEGINNER,
                Arrays.asList("Legs", "Cardio"), Arrays.asList("Shoes")));

        catalog.addExercise(new Exercise("ex8", "Cycling", Difficulty.BEGINNER,
                Arrays.asList("Legs", "Cardio"), Arrays.asList("Bike")));

        catalog.addExercise(new Exercise("ex9", "Rowing", Difficulty.INTERMEDIATE,
                Arrays.asList("Full Body", "Cardio"), Arrays.asList("Rowing Machine")));

        catalog.addExercise(new Exercise("ex10", "Jump Rope", Difficulty.BEGINNER,
                Arrays.asList("Cardio", "Calves"), Arrays.asList("Jump Rope")));

        catalog.addExercise(new Exercise("ex11", "Circuit Training", Difficulty.INTERMEDIATE,
                Arrays.asList("Full Body", "Cardio"), Arrays.asList("Bodyweight")));

        catalog.addExercise(new Exercise("ex12", "Burpees", Difficulty.INTERMEDIATE,
                Arrays.asList("Full Body", "Cardio"), Arrays.asList("Bodyweight")));

        catalog.addExercise(new Exercise("ex13", "Bodyweight Squats", Difficulty.BEGINNER,
                Arrays.asList("Legs"), Arrays.asList("Bodyweight")));

        catalog.addExercise(new Exercise("ex14", "Jogging", Difficulty.BEGINNER,
                Arrays.asList("Legs", "Cardio"), Arrays.asList("Shoes")));

        return catalog;
    }
}