-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 10, 2025 at 02:14 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `platformer_game`
--

-- --------------------------------------------------------

--
-- Table structure for table `achievements`
--

CREATE TABLE `achievements` (
  `id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `achievement_name` varchar(100) NOT NULL,
  `achievement_description` text DEFAULT NULL,
  `unlocked_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `game_progress`
--

CREATE TABLE `game_progress` (
  `id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `level_number` int(11) NOT NULL,
  `completed` tinyint(1) DEFAULT 0,
  `stars` int(11) DEFAULT 0,
  `best_time` int(11) DEFAULT 0,
  `coins_collected` int(11) DEFAULT 0,
  `deaths` int(11) DEFAULT 0,
  `completed_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `game_progress`
--

INSERT INTO `game_progress` (`id`, `player_id`, `level_number`, `completed`, `stars`, `best_time`, `coins_collected`, `deaths`, `completed_at`) VALUES
(4, 2, 1, 1, 0, 0, 3, 0, NULL),
(5, 2, 2, 1, 0, 0, 4, 0, NULL),
(6, 2, 3, 1, 0, 0, 8, 0, NULL),
(7, 2, 4, 1, 0, 0, 7, 0, NULL),
(8, 2, 5, 1, 0, 0, 3, 0, NULL),
(9, 2, 6, 1, 0, 0, 7, 0, NULL),
(10, 2, 7, 1, 0, 0, 7, 0, NULL),
(11, 2, 8, 1, 0, 0, 2, 0, NULL),
(12, 2, 9, 1, 0, 0, 1, 0, NULL),
(13, 3, 1, 1, 0, 0, 2, 0, NULL),
(14, 3, 2, 1, 0, 0, 4, 0, NULL),
(15, 3, 3, 1, 0, 0, 8, 0, NULL),
(16, 3, 4, 1, 0, 0, 8, 0, NULL),
(23, 5, 1, 1, 0, 0, 2, 0, NULL),
(24, 6, 1, 1, 0, 0, 2, 0, NULL),
(25, 6, 2, 1, 0, 0, 4, 0, NULL),
(26, 7, 1, 1, 0, 0, 2, 0, NULL),
(27, 7, 2, 1, 0, 0, 4, 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `players`
--

CREATE TABLE `players` (
  `player_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `total_coins` int(11) DEFAULT 0,
  `games_played` int(11) DEFAULT 0,
  `high_score` int(11) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `last_login` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `players`
--

INSERT INTO `players` (`player_id`, `username`, `total_coins`, `games_played`, `high_score`, `created_at`, `last_login`) VALUES
(2, 'amanda', 81, 39, 42, '2025-11-23 13:37:15', '2025-12-05 02:17:45'),
(3, 'cancan', 59, 1, 9, '2025-12-04 18:09:58', '2025-12-04 18:16:50'),
(5, 'audi', 4, 1, 4, '2025-12-05 01:57:39', '2025-12-05 01:58:10'),
(6, 'duano', 10, 1, 10, '2025-12-05 02:11:00', '2025-12-05 02:13:15'),
(7, 'will', 15, 3, 10, '2025-12-05 02:18:57', '2025-12-05 02:25:08');

-- --------------------------------------------------------

--
-- Table structure for table `player_characters`
--

CREATE TABLE `player_characters` (
  `id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `character_name` varchar(50) NOT NULL,
  `is_current` tinyint(1) DEFAULT 0,
  `unlocked_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `player_characters`
--

INSERT INTO `player_characters` (`id`, `player_id`, `character_name`, `is_current`, `unlocked_at`) VALUES
(2, 2, 'HERO', 0, '2025-11-23 13:37:15'),
(3, 3, 'HERO', 1, '2025-12-04 18:09:58'),
(5, 5, 'HERO', 1, '2025-12-05 01:57:39'),
(6, 6, 'HERO', 1, '2025-12-05 02:11:00'),
(7, 2, 'DRAGON', 1, '2025-12-05 02:17:01'),
(8, 7, 'HERO', 1, '2025-12-05 02:18:57');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `achievements`
--
ALTER TABLE `achievements`
  ADD PRIMARY KEY (`id`),
  ADD KEY `player_id` (`player_id`);

--
-- Indexes for table `game_progress`
--
ALTER TABLE `game_progress`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_player_level` (`player_id`,`level_number`);

--
-- Indexes for table `players`
--
ALTER TABLE `players`
  ADD PRIMARY KEY (`player_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `player_characters`
--
ALTER TABLE `player_characters`
  ADD PRIMARY KEY (`id`),
  ADD KEY `player_id` (`player_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `achievements`
--
ALTER TABLE `achievements`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `game_progress`
--
ALTER TABLE `game_progress`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `players`
--
ALTER TABLE `players`
  MODIFY `player_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `player_characters`
--
ALTER TABLE `player_characters`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `achievements`
--
ALTER TABLE `achievements`
  ADD CONSTRAINT `achievements_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`player_id`) ON DELETE CASCADE;

--
-- Constraints for table `game_progress`
--
ALTER TABLE `game_progress`
  ADD CONSTRAINT `game_progress_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`player_id`) ON DELETE CASCADE;

--
-- Constraints for table `player_characters`
--
ALTER TABLE `player_characters`
  ADD CONSTRAINT `player_characters_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`player_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
