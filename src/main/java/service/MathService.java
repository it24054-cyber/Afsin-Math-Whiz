package service;

public class MathService {
    public static final double TRIG_TOLERANCE = 0.05;

    // Task 03: Prime Number Function (1 = Prime, 0 = Not Prime)
    public static int isPrime(int number) {
        if (number < 2) return 0;
        if (number == 2) return 1;
        if (number % 2 == 0) return 0;
        for (int i = 3; (long) i * i <= number; i += 2) {
            if (number % i == 0) return 0;
        }
        return 1;
    }

    // Task 04: Modulus Function (Handle zero divisor)
    public static int calculateMod(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Validation error: Divisor cannot be zero.");
        }
        return a % b;
    }

    // Task 05: Trigonometric Functions
    public static double calculateSin(double degree) {
        return Math.sin(Math.toRadians(degree));
    }

    public static double calculateCos(double degree) {
        return Math.cos(Math.toRadians(degree));
    }

    public static double calculateTan(double degree) {
        double normalized = Math.abs(degree) % 180.0;
        if (Math.abs(normalized - 90.0) < 1e-7) {
            throw new ArithmeticException("Validation error: Tangent is undefined for " + degree + "°");
        }
        return Math.tan(Math.toRadians(degree));
    }

    public static double calculateTrig(String type, double degree) {
        switch (type.toLowerCase().trim()) {
            case "sin": return calculateSin(degree);
            case "cos": return calculateCos(degree);
            case "tan": return calculateTan(degree);
            default: throw new IllegalArgumentException("Unknown trig type: " + type);
        }
    }

    public static boolean isTrigAnswerCorrect(double playerAnswer, double expectedAnswer, double tolerance) {
        return Math.abs(playerAnswer - expectedAnswer) <= tolerance;
    }

    public static double roundToFourDecimals(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}
