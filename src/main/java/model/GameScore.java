package model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class GameScore {
    private int id;
    private String playerName;
    private int score;
    private String difficulty;
    private Timestamp playedAt;

    public GameScore() {
        this.difficulty = "NORMAL";
        this.playedAt = new Timestamp(System.currentTimeMillis());
    }

    public GameScore(String playerName, int score, String difficulty, Timestamp playedAt) {
        this.playerName = playerName;
        this.score = score;
        this.difficulty = (difficulty != null) ? difficulty : "NORMAL";
        this.playedAt = (playedAt != null) ? playedAt : new Timestamp(System.currentTimeMillis());
    }

    public GameScore(int id, String playerName, int score, String difficulty, Timestamp playedAt) {
        this.id = id;
        this.playerName = playerName;
        this.score = score;
        this.difficulty = (difficulty != null) ? difficulty : "NORMAL";
        this.playedAt = (playedAt != null) ? playedAt : new Timestamp(System.currentTimeMillis());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public Timestamp getPlayedAt() { return playedAt; }
    public void setPlayedAt(Timestamp playedAt) { this.playedAt = playedAt; }

    public String getFormattedPlayedAt() {
        if (playedAt == null) return "N/A";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(playedAt);
    }
}
