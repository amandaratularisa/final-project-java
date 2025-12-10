-- ===================================
-- Pixel Heroes - Database Schema (FIXED)
-- ===================================

CREATE DATABASE IF NOT EXISTS platformer_game;
USE platformer_game;

-- Tabel Players (Data pemain)
CREATE TABLE IF NOT EXISTS players (
    player_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    total_coins INT DEFAULT 0,
    games_played INT DEFAULT 0,
    high_score INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabel Characters (Karakter yang dimiliki pemain)
CREATE TABLE IF NOT EXISTS player_characters (
    id INT PRIMARY KEY AUTO_INCREMENT,
    player_id INT NOT NULL,
    character_name VARCHAR(50) NOT NULL,
    is_current BOOLEAN DEFAULT FALSE,
    unlocked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE
);

-- Tabel Game Progress (Progress level per pemain)
CREATE TABLE IF NOT EXISTS game_progress (
    id INT PRIMARY KEY AUTO_INCREMENT,
    player_id INT NOT NULL,
    level_number INT NOT NULL,
    completed BOOLEAN DEFAULT FALSE,
    stars INT DEFAULT 0,
    best_time INT DEFAULT 0,
    coins_collected INT DEFAULT 0,
    deaths INT DEFAULT 0,
    completed_at TIMESTAMP NULL,
    FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE,
    UNIQUE KEY unique_player_level (player_id, level_number)
);

-- Tabel Achievements (Pencapaian pemain)
CREATE TABLE IF NOT EXISTS achievements (
    id INT PRIMARY KEY AUTO_INCREMENT,
    player_id INT NOT NULL,
    achievement_name VARCHAR(100) NOT NULL,
    achievement_description TEXT,
    unlocked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE
);
