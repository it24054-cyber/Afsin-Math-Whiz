package dao;

import model.GameScore;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {
    // Task 07: INSERT using PreparedStatement
    public boolean saveScore(GameScore score) {
        String insertSQL = "INSERT INTO game_scores (player_name, score, difficulty, played_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, score.getPlayerName());
            pstmt.setInt(2, score.getScore());
            pstmt.setString(3, score.getDifficulty());
            pstmt.setTimestamp(4, score.getPlayedAt());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Task 08: SELECT Top 10 Scores
    public List<GameScore> getTopScores(int limit) {
        List<GameScore> scores = new ArrayList<>();
        String selectSQL = "SELECT id, player_name, score, difficulty, played_at FROM game_scores ORDER BY score DESC, played_at ASC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    scores.add(new GameScore(
                        rs.getInt("id"),
                        rs.getString("player_name"),
                        rs.getInt("score"),
                        rs.getString("difficulty"),
                        rs.getTimestamp("played_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }

    public List<GameScore> getTop10Scores() {
        return getTopScores(10);
    }
}
