package com.game.mario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/platformer_game";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static DatabaseManager instance = null;
    private Connection connection;
    
    private DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }
    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    private void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Attempting to connect to database...");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Database connected successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to database!");
            System.err.println("ERROR: " + e.getMessage());
            System.err.println("Make sure MySQL is running and database platformer_game exists.");
            System.err.println("Run this SQL first: CREATE DATABASE IF NOT EXISTS platformer_game;");
            e.printStackTrace();
            connection = null;
        }
    }
    
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    public int createPlayer(String username) {
        String sql = "INSERT INTO players (username, total_coins, games_played, high_score) VALUES (?, 0, 0, 0)";
        try {
            connect();
            if (connection == null || connection.isClosed()) {
                System.err.println("Connection is null or closed!");
                return -1;
            }
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int playerId = rs.getInt(1);
                    addCharacterToPlayer(playerId, "HERO", true);
                    return playerId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating player: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    public boolean addCharacterToPlayer(int playerId, String characterName, boolean isCurrent) {
        String sql = "INSERT INTO player_characters (player_id, character_name, is_current) VALUES (?, ?, ?)";
        try {
            connect();
            if (isCurrent) {
                String resetSql = "UPDATE player_characters SET is_current = FALSE WHERE player_id = ?";
                PreparedStatement resetStmt = connection.prepareStatement(resetSql);
                resetStmt.setInt(1, playerId);
                resetStmt.executeUpdate();
            }
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, playerId);
            stmt.setString(2, characterName);
            stmt.setBoolean(3, isCurrent);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding character: " + e.getMessage());
            return false;
        }
    }
    
    public PlayerRecord getPlayer(String username) {
        String sql = "SELECT player_id, username, total_coins, games_played, high_score FROM players WHERE username = ?";
        try {
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new PlayerRecord(
                    rs.getInt("player_id"),
                    rs.getString("username"),
                    rs.getInt("total_coins"),
                    rs.getInt("games_played"),
                    rs.getInt("high_score")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting player: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public List<String> getPlayerCharacters(int playerId) {
        List<String> characters = new ArrayList<>();
        String sql = "SELECT character_name FROM player_characters WHERE player_id = ?";
        try {
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                characters.add(rs.getString("character_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting characters: " + e.getMessage());
        }
        return characters;
    }
    
    public String getCurrentCharacter(int playerId) {
        String sql = "SELECT character_name FROM player_characters WHERE player_id = ? AND is_current = TRUE";
        try {
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("character_name");
            }
        } catch (SQLException e) {
            System.err.println("Error getting current character: " + e.getMessage());
        }
        return "HERO";
    }
    
    public LevelProgress getLevelProgress(int playerId, int levelNumber) {
        String sql = "SELECT * FROM game_progress WHERE player_id = ? AND level_number = ?";
        try {
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, playerId);
            stmt.setInt(2, levelNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new LevelProgress(
                    rs.getInt("level_number"),
                    rs.getBoolean("completed"),
                    rs.getInt("stars"),
                    rs.getInt("best_time"),
                    rs.getInt("coins_collected")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting level progress: " + e.getMessage());
        }
        return null;
    }
    
    public boolean updatePlayerCoins(int playerId, int totalCoins) {
        String sql = "UPDATE players SET total_coins = ? WHERE player_id = ?";
        try {
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, totalCoins);
            stmt.setInt(2, playerId);
            int rows = stmt.executeUpdate();
            System.out.println("Updated coins for player " + playerId + ": " + totalCoins + " (rows: " + rows + ")");
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating coins: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateHighScore(int playerId, int highScore) {
        String sql = "UPDATE players SET high_score = ? WHERE player_id = ? AND high_score < ?";
        try {
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, highScore);
            stmt.setInt(2, playerId);
            stmt.setInt(3, highScore);
            int rows = stmt.executeUpdate();
            System.out.println("Updated high score for player " + playerId + ": " + highScore + " (rows: " + rows + ")");
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating high score: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean incrementGamesPlayed(int playerId) {
        String sql = "UPDATE players SET games_played = games_played + 1 WHERE player_id = ?";
        try {
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, playerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error incrementing games played: " + e.getMessage());
            return false;
        }
    }
    
    public boolean setCurrentCharacter(int playerId, String characterName) {
        try {
            connect();
            String resetSql = "UPDATE player_characters SET is_current = FALSE WHERE player_id = ?";
            PreparedStatement resetStmt = connection.prepareStatement(resetSql);
            resetStmt.setInt(1, playerId);
            resetStmt.executeUpdate();
            String setSql = "UPDATE player_characters SET is_current = TRUE WHERE player_id = ? AND character_name = ?";
            PreparedStatement setStmt = connection.prepareStatement(setSql);
            setStmt.setInt(1, playerId);
            setStmt.setString(2, characterName);
            return setStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error setting current character: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateLevelProgress(int playerId, int levelNumber, boolean completed, int stars, int time, int coins) {
        String checkSql = "SELECT * FROM game_progress WHERE player_id = ? AND level_number = ?";
        String insertSql = "INSERT INTO game_progress (player_id, level_number, completed, stars, best_time, coins_collected) VALUES (?, ?, ?, ?, ?, ?)";
        String updateSql = "UPDATE game_progress SET completed = ?, stars = ?, best_time = ?, coins_collected = ? WHERE player_id = ? AND level_number = ?";
        try {
            connect();
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, playerId);
            checkStmt.setInt(2, levelNumber);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                updateStmt.setBoolean(1, completed);
                updateStmt.setInt(2, stars);
                updateStmt.setInt(3, time);
                updateStmt.setInt(4, coins);
                updateStmt.setInt(5, playerId);
                updateStmt.setInt(6, levelNumber);
                return updateStmt.executeUpdate() > 0;
            } else {
                PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                insertStmt.setInt(1, playerId);
                insertStmt.setInt(2, levelNumber);
                insertStmt.setBoolean(3, completed);
                insertStmt.setInt(4, stars);
                insertStmt.setInt(5, time);
                insertStmt.setInt(6, coins);
                return insertStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error updating level progress: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deletePlayer(int playerId) {
        String sql = "DELETE FROM players WHERE player_id = ?";
        try {
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, playerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting player: " + e.getMessage());
            return false;
        }
    }
    
    public static class PlayerRecord {
        private final int id;
        private final String username;
        private final int totalCoins;
        private final int gamesPlayed;
        private final int highScore;
        
        public PlayerRecord(int id, String username, int totalCoins, int gamesPlayed, int highScore) {
            this.id = id;
            this.username = username;
            this.totalCoins = totalCoins;
            this.gamesPlayed = gamesPlayed;
            this.highScore = highScore;
        }
        
        public int getId() { return id; }
        public String getUsername() { return username; }
        public int getTotalCoins() { return totalCoins; }
        public int getGamesPlayed() { return gamesPlayed; }
        public int getHighScore() { return highScore; }
    }
    
    public static class LevelProgress {
        private final int levelNumber;
        private final boolean completed;
        private final int stars;
        private final int bestTime;
        private final int coinsCollected;
        
        public LevelProgress(int levelNumber, boolean completed, int stars, int bestTime, int coinsCollected) {
            this.levelNumber = levelNumber;
            this.completed = completed;
            this.stars = stars;
            this.bestTime = bestTime;
            this.coinsCollected = coinsCollected;
        }
        
        public int getLevelNumber() { return levelNumber; }
        public boolean isCompleted() { return completed; }
        public int getStars() { return stars; }
        public int getBestTime() { return bestTime; }
        public int getCoinsCollected() { return coinsCollected; }
    }
    
    public List<PlayerRecord> getAllPlayers() {
        List<PlayerRecord> players = new ArrayList<>();
        String sql = "SELECT player_id, username, total_coins, games_played, high_score FROM players ORDER BY username";
        try {
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                players.add(new PlayerRecord(
                    rs.getInt("player_id"),
                    rs.getString("username"),
                    rs.getInt("total_coins"),
                    rs.getInt("games_played"),
                    rs.getInt("high_score")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all players: " + e.getMessage());
        }
        return players;
    }
    
    public List<PlayerRecord> getTopPlayers(int limit) {
        List<PlayerRecord> players = new ArrayList<>();
        String sql = "SELECT player_id, username, total_coins, games_played, high_score FROM players ORDER BY high_score DESC, total_coins DESC LIMIT ?";
        try {
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                players.add(new PlayerRecord(
                    rs.getInt("player_id"),
                    rs.getString("username"),
                    rs.getInt("total_coins"),
                    rs.getInt("games_played"),
                    rs.getInt("high_score")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting top players: " + e.getMessage());
        }
        return players;
    }
    
    public int getCurrentLevel(String username) {
        String sql = "SELECT MAX(level_number) as max_level FROM game_progress gp " +
                     "JOIN players p ON gp.player_id = p.player_id " +
                     "WHERE p.username = ? AND gp.completed = TRUE";
        try {
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int maxLevel = rs.getInt("max_level");
                return maxLevel + 1;
            }
        } catch (SQLException e) {
            System.err.println("Error getting current level: " + e.getMessage());
        }
        return 1;
    }
    
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}