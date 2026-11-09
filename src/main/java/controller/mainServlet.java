package controller;

import dao.ScoreDAO;
import model.GameScore;
import service.MathService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Controller Servlet for the Math Whiz game.
 * Mapped to "/game", "/submit", and "/leaderboard".
 *
 * All inputs (numbers and player answers) are provided by the user.
 * The server evaluates them dynamically using Java MathService methods.
 */
@WebServlet(name = "GameServlet", urlPatterns = {"/game", "/submit", "/leaderboard"})
public class GameServlet extends HttpServlet {

    private ScoreDAO scoreDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.scoreDAO = new ScoreDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String action = request.getParameter("action");

        if ("/leaderboard".equals(path) || "leaderboard".equalsIgnoreCase(action)) {
            showLeaderboard(request, response);
            return;
        }

        if ("start".equalsIgnoreCase(action)) {
            startGame(request, response);
            return;
        }

        // Default: forward to index.jsp
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String action = request.getParameter("action");

        if ("/submit".equals(path) || "submit".equalsIgnoreCase(action)) {
            processAnswers(request, response);
            return;
        }

        if ("start".equalsIgnoreCase(action)) {
            startGame(request, response);
            return;
        }

        doGet(request, response);
    }

    private void startGame(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String playerName = request.getParameter("playerName");
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Math Adventurer";
        } else {
            playerName = playerName.trim();
        }

        request.setAttribute("playerName", playerName);
        request.setAttribute("difficulty", "NORMAL");

        request.getRequestDispatcher("/game.jsp").forward(request, response);
    }

    private void processAnswers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String playerName = request.getParameter("playerName");
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Math Adventurer";
        } else {
            playerName = playerName.trim();
        }

        String difficulty = request.getParameter("difficulty");
        if (difficulty == null || difficulty.trim().isEmpty()) {
            difficulty = "NORMAL";
        }

        // --- QUESTION 1: Prime Number (2 Marks) ---
        // Both the integer to test (q1Num) and user's claim (q1Ans) come from user input
        int q1Num = parseInteger(request.getParameter("q1Num"), 7);
        int expectedQ1 = MathService.isPrime(q1Num);
        String q1Raw = request.getParameter("q1Ans");
        Integer q1Ans = parseIntegerNullable(q1Raw);
        boolean primeCorrect = (q1Ans != null && q1Ans == expectedQ1);
        int q1Score = primeCorrect ? 2 : 0;

        // --- QUESTION 2: Modulus Operation (2 Marks) ---
        // Operands q2A, q2B and remainder q2Ans all come from user input
        int q2A = parseInteger(request.getParameter("q2A"), 17);
        int q2B = parseInteger(request.getParameter("q2B"), 5);
        int expectedQ2 = 0;
        boolean modError = false;
        String modErrorMsg = "";
        try {
            expectedQ2 = MathService.calculateMod(q2A, q2B);
        } catch (ArithmeticException e) {
            modError = true;
            modErrorMsg = e.getMessage();
        }
        String q2Raw = request.getParameter("q2Ans");
        Integer q2Ans = parseIntegerNullable(q2Raw);
        boolean modCorrect = (!modError && q2Ans != null && q2Ans == expectedQ2);
        int q2Score = modCorrect ? 2 : 0;

        // --- QUESTION 3: Trigonometry 1 (3 Marks) ---
        // Function type, angle, and player answer all come from user input
        String q3Type = request.getParameter("q3Type");
        if (q3Type == null) q3Type = "sin";
        double q3Deg = parseDouble(request.getParameter("q3Deg"), 30.0);
        double expectedQ3 = 0.0;
        boolean trig1Error = false;
        String trig1ErrorMsg = "";
        try {
            expectedQ3 = MathService.calculateTrig(q3Type, q3Deg);
        } catch (ArithmeticException e) {
            trig1Error = true;
            trig1ErrorMsg = e.getMessage();
        }
        String q3Raw = request.getParameter("q3Ans");
        Double q3Ans = parseDoubleNullable(q3Raw);
        boolean trig1Correct = (!trig1Error && q3Ans != null &&
                MathService.isTrigAnswerCorrect(q3Ans, expectedQ3, MathService.TRIG_TOLERANCE));
        int q3Score = trig1Correct ? 3 : 0;

        // --- QUESTION 4: Trigonometry 2 (3 Marks) ---
        // Function type, angle, and player answer all come from user input
        String q4Type = request.getParameter("q4Type");
        if (q4Type == null) q4Type = "cos";
        double q4Deg = parseDouble(request.getParameter("q4Deg"), 60.0);
        double expectedQ4 = 0.0;
        boolean trig2Error = false;
        String trig2ErrorMsg = "";
        try {
            expectedQ4 = MathService.calculateTrig(q4Type, q4Deg);
        } catch (ArithmeticException e) {
            trig2Error = true;
            trig2ErrorMsg = e.getMessage();
        }
        String q4Raw = request.getParameter("q4Ans");
        Double q4Ans = parseDoubleNullable(q4Raw);
        boolean trig2Correct = (!trig2Error && q4Ans != null &&
                MathService.isTrigAnswerCorrect(q4Ans, expectedQ4, MathService.TRIG_TOLERANCE));
        int q4Score = trig2Correct ? 3 : 0;

        // --- TOTAL SCORE CALCULATION (TASK 06) ---
        int totalScore = 0;
        if (primeCorrect) totalScore += 2;
        if (modCorrect)   totalScore += 2;
        if (trig1Correct) totalScore += 3;
        if (trig2Correct) totalScore += 3;

        // --- SAVE TO DATABASE (TASK 07) ---
        Timestamp now = new Timestamp(System.currentTimeMillis());
        GameScore gameScore = new GameScore(playerName, totalScore, difficulty, now);
        boolean dbSaved = false;
        try {
            dbSaved = scoreDAO.saveScore(gameScore);
        } catch (Exception e) {
            System.err.println("Database save notice: " + e.getMessage());
        }

        // Set attributes for result.jsp
        request.setAttribute("playerName", playerName);
        request.setAttribute("totalScore", totalScore);
        request.setAttribute("maxScore", 10);
        request.setAttribute("difficulty", difficulty);
        request.setAttribute("playedAt", gameScore.getFormattedPlayedAt());
        request.setAttribute("dbSaved", dbSaved);

        // Q1 details
        request.setAttribute("q1Num", q1Num);
        request.setAttribute("q1User", (q1Raw != null && !q1Raw.isEmpty()) ? q1Raw + " (" + (q1Ans != null && q1Ans == 1 ? "Prime" : "Not Prime") + ")" : "No answer");
        request.setAttribute("q1Expected", expectedQ1 + " (" + (expectedQ1 == 1 ? "Prime" : "Not Prime") + ")");
        request.setAttribute("q1Correct", primeCorrect);
        request.setAttribute("q1Score", q1Score);
        request.setAttribute("q1Max", 2);

        // Q2 details
        request.setAttribute("q2Expr", q2A + " % " + q2B);
        request.setAttribute("q2User", (q2Raw != null && !q2Raw.isEmpty()) ? q2Raw : "No answer");
        request.setAttribute("q2Expected", modError ? modErrorMsg : String.valueOf(expectedQ2));
        request.setAttribute("q2Correct", modCorrect);
        request.setAttribute("q2Score", q2Score);
        request.setAttribute("q2Max", 2);

        // Q3 details
        request.setAttribute("q3Expr", q3Type + "(" + ((int) q3Deg) + "°)");
        request.setAttribute("q3User", (q3Raw != null && !q3Raw.isEmpty()) ? q3Raw : "No answer");
        request.setAttribute("q3Expected", trig1Error ? trig1ErrorMsg : String.valueOf(MathService.roundToFourDecimals(expectedQ3)));
        request.setAttribute("q3Correct", trig1Correct);
        request.setAttribute("q3Score", q3Score);
        request.setAttribute("q3Max", 3);

        // Q4 details
        request.setAttribute("q4Expr", q4Type + "(" + ((int) q4Deg) + "°)");
        request.setAttribute("q4User", (q4Raw != null && !q4Raw.isEmpty()) ? q4Raw : "No answer");
        request.setAttribute("q4Expected", trig2Error ? trig2ErrorMsg : String.valueOf(MathService.roundToFourDecimals(expectedQ4)));
        request.setAttribute("q4Correct", trig2Correct);
        request.setAttribute("q4Score", q4Score);
        request.setAttribute("q4Max", 3);

        request.getRequestDispatcher("/result.jsp").forward(request, response);
    }

    private void showLeaderboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<GameScore> topScores = scoreDAO.getTop10Scores();
        request.setAttribute("topScores", topScores);
        request.getRequestDispatcher("/leaderboard.jsp").forward(request, response);
    }

    private int parseInteger(String val, int defaultVal) {
        if (val == null) return defaultVal;
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    private Integer parseIntegerNullable(String val) {
        if (val == null || val.trim().isEmpty()) return null;
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private double parseDouble(String val, double defaultVal) {
        if (val == null) return defaultVal;
        try {
            return Double.parseDouble(val.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    private Double parseDoubleNullable(String val) {
        if (val == null || val.trim().isEmpty()) return null;
        try {
            return Double.parseDouble(val.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
