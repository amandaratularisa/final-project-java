package com.game.mario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    private int playerId;
    private String username;
    private int totalCoins;
    private List<String> ownedCharacters;
    private String currentCharacter;
    private int gamesPlayed;
    private int highScore;
    private DatabaseManager dbManager;
    private static final String SAVE_FILE = "playerdata.sav";
    
    public PlayerData(String username) {
        this.username = username;
        this.totalCoins = 0;
        this.ownedCharacters = new ArrayList<>();
        this.ownedCharacters.add("HERO");
        this.currentCharacter = "HERO";
        this.gamesPlayed = 0;
        this.highScore = 0;
        this.playerId = -1;
        try {
            this.dbManager = DatabaseManager.getInstance();
        } catch (Exception e) {
            System.out.println("Database not available, using file storage");
            this.dbManager = null;
        }
    }
    
    public void addCoins(int coins) {
        System.out.println("addCoins() called - adding " + coins + " to current " + totalCoins);
        this.totalCoins += coins;
        System.out.println("New total coins: " + totalCoins);
        syncToDatabase();
    }
    
    public boolean spendCoins(int coins) {
        if (totalCoins >= coins) {
            totalCoins -= coins;
            syncToDatabase();
            return true;
        }
        return false;
    }
    
    public void unlockCharacter(String character) {
        if (!ownedCharacters.contains(character)) {
            ownedCharacters.add(character);
            if (dbManager != null && playerId > 0) {
                dbManager.addCharacterToPlayer(playerId, character, false);
            }
        }
    }
    
    public boolean ownsCharacter(String character) {
        return ownedCharacters.contains(character);
    }
    
    public void setCurrentCharacter(String character) {
        if (ownsCharacter(character)) {
            this.currentCharacter = character;
            if (dbManager != null && playerId > 0) {
                dbManager.setCurrentCharacter(playerId, character);
            }
        }
    }
    
    public void incrementGamesPlayed() {
        gamesPlayed++;
        if (dbManager != null && playerId > 0) {
            dbManager.incrementGamesPlayed(playerId);
        }
    }
    
    public void updateHighScore(int score) {
        if (score > highScore) {
            highScore = score;
            if (dbManager != null && playerId > 0) {
                dbManager.updateHighScore(playerId, score);
            }
        }
    }
    
    private void syncToDatabase() {
        System.out.println("syncToDatabase called - playerId: " + playerId + ", coins: " + totalCoins);
        if (dbManager != null && playerId > 0) {
            boolean result = dbManager.updatePlayerCoins(playerId, totalCoins);
            System.out.println("Database update result: " + result);
        } else {
            System.err.println("Cannot sync - dbManager: " + (dbManager != null) + ", playerId: " + playerId);
        }
    }
    
    public void save() {
        System.out.println("PlayerData.save() called - username: " + username);
        if (dbManager != null && dbManager.isConnected()) {
            System.out.println("Using database save");
            syncToDatabase();
        } else {
            System.out.println("Database not connected, using file save");
            saveToFile();
        }
    }
    
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(username);
            oos.writeInt(totalCoins);
            oos.writeObject(ownedCharacters);
            oos.writeObject(currentCharacter);
            oos.writeInt(gamesPlayed);
            oos.writeInt(highScore);
        } catch (IOException e) {
            System.out.println("Error saving player data: " + e.getMessage());
        }
    }
    
    public static PlayerData load(String username) {
        DatabaseManager db = null;
        try {
            db = DatabaseManager.getInstance();
        } catch (Exception e) {
            System.out.println("Database not available, using file storage");
        }
        
        if (db != null && db.isConnected()) {
            return loadFromDatabase(username, db);
        } else {
            return loadFromFile(username);
        }
    }
    
    private static PlayerData loadFromDatabase(String username, DatabaseManager db) {
        System.out.println("Loading from database: " + username);
        DatabaseManager.PlayerRecord record = db.getPlayer(username);
        if (record != null) {
            System.out.println("Found player record - ID: " + record.getId() + ", Coins: " + record.getTotalCoins());
            PlayerData data = new PlayerData(username);
            data.playerId = record.getId();
            data.totalCoins = record.getTotalCoins();
            data.gamesPlayed = record.getGamesPlayed();
            data.highScore = record.getHighScore();
            data.ownedCharacters = db.getPlayerCharacters(data.playerId);
            data.currentCharacter = db.getCurrentCharacter(data.playerId);
            data.dbManager = db;
            System.out.println("PlayerData loaded - ID: " + data.playerId + ", Coins: " + data.totalCoins);
            return data;
        } else {
            System.out.println("Player not found in database, creating new...");
            int newPlayerId = db.createPlayer(username);
            if (newPlayerId > 0) {
                PlayerData data = new PlayerData(username);
                data.playerId = newPlayerId;
                data.dbManager = db;
                System.out.println("New player created - ID: " + data.playerId);
                return data;
            }
        }
        System.err.println("Failed to load player from database!");
        PlayerData data = new PlayerData(username);
        data.dbManager = db;
        return data;
    }
    
    private static PlayerData loadFromFile(String username) {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return new PlayerData(username);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            String savedUsername = (String) ois.readObject();
            int coins = ois.readInt();
            List<String> characters = (List<String>) ois.readObject();
            String current = (String) ois.readObject();
            int games = ois.readInt();
            int high = ois.readInt();
            PlayerData data = new PlayerData(savedUsername);
            data.totalCoins = coins;
            data.ownedCharacters = characters;
            data.currentCharacter = current;
            data.gamesPlayed = games;
            data.highScore = high;
            return data;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading player data: " + e.getMessage());
            return new PlayerData(username);
        }
    }
    
    public String getUsername() { return username; }
    public int getTotalCoins() { return totalCoins; }
    public List<String> getOwnedCharacters() { return ownedCharacters; }
    public String getCurrentCharacter() { return currentCharacter; }
    public int getGamesPlayed() { return gamesPlayed; }
    public int getHighScore() { return highScore; }
    public int getPlayerId() { return playerId; }
    
    public class PlayerStatistics {
        public double getAverageScore() {
            return gamesPlayed > 0 ? (double) highScore / gamesPlayed : 0;
        }
        
        public int getTotalCharactersUnlocked() {
            return ownedCharacters.size();
        }
        
        public String getRank() {
            if (highScore >= 1000) return "Legend";
            if (highScore >= 750) return "Master";
            if (highScore >= 500) return "Expert";
            if (highScore >= 250) return "Intermediate";
            return "Beginner";
        }
        
        public boolean isVeteran() {
            return gamesPlayed >= 10;
        }
    }
    
    public PlayerStatistics getStatistics() {
        return new PlayerStatistics();
    }
}
