-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 11, 2024 at 03:56 PM
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
-- Database: `explodeit`
--

-- --------------------------------------------------------

--
-- Table structure for table `block`
--

CREATE TABLE `block` (
  `BlockID` int(11) NOT NULL,
  `color` varchar(255) DEFAULT NULL,
  `type` enum('LUCKY','DESTRUCTIBLE','INDESTRUCTIBLE') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `block`
--

INSERT INTO `block` (`BlockID`, `color`, `type`) VALUES
(1, '#FF0000', 'DESTRUCTIBLE'),
(2, '#000000', 'INDESTRUCTIBLE');

-- --------------------------------------------------------

--
-- Table structure for table `characterlook`
--

CREATE TABLE `characterlook` (
  `CharacterLookID` int(11) NOT NULL,
  `head` varchar(255) DEFAULT NULL,
  `behind` varchar(255) DEFAULT NULL,
  `front` varchar(255) DEFAULT NULL,
  `leftSide` varchar(255) DEFAULT NULL,
  `rightSide` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `characters`
--

CREATE TABLE `characters` (
  `CharacterID` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `characterSpeed` int(11) DEFAULT NULL,
  `explodePower` int(11) DEFAULT NULL,
  `explosionSpeed` int(11) DEFAULT NULL,
  `hp` int(11) DEFAULT NULL,
  `CharacterLookID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `item`
--

CREATE TABLE `item` (
  `ItemID` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `ItemLookID` int(11) NOT NULL,
  `timeOfEffect` int(11) DEFAULT NULL,
  `dropProbability` int(11) DEFAULT NULL,
  `CharacterSpeed` int(11) DEFAULT NULL,
  `ExplodePower` int(11) DEFAULT NULL,
  `ExplosionSpeed` int(11) DEFAULT NULL,
  `hp` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `itemlook`
--

CREATE TABLE `itemlook` (
  `ItemLookID` int(11) NOT NULL,
  `look` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `lucky`
--

CREATE TABLE `lucky` (
  `BlockID` int(11) NOT NULL,
  `lootProbability` int(11) DEFAULT NULL,
  `ItemID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `map`
--

CREATE TABLE `map` (
  `MapID` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `color` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `map`
--

INSERT INTO `map` (`MapID`, `name`, `color`) VALUES
(1, 'pink', 'https://static.vecteezy.com/system/resources/previews/009/924/602/non_2x/cute-pink-abstract-minimal-background-perfect-for-wallpaper-backdrop-postcard-background-vector.jpg'),
(2, 'papier', 'https://th.bing.com/th/id/OIP.fwoEjEQVWRpgjvVnTqcOQAHaEo?rs=1&pid=ImgDetMain.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `mapblock`
--

CREATE TABLE `mapblock` (
  `MapID` int(11) NOT NULL,
  `BlockID` int(11) NOT NULL,
  `positionX` int(11) NOT NULL,
  `positionY` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mapblock`
--

INSERT INTO `mapblock` (`MapID`, `BlockID`, `positionX`, `positionY`) VALUES
(1, 1, 0, 0),
(1, 2, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `mapitem`
--

CREATE TABLE `mapitem` (
  `MapID` int(11) NOT NULL,
  `ItemID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `block`
--
ALTER TABLE `block`
  ADD PRIMARY KEY (`BlockID`);

--
-- Indexes for table `characterlook`
--
ALTER TABLE `characterlook`
  ADD PRIMARY KEY (`CharacterLookID`);

--
-- Indexes for table `characters`
--
ALTER TABLE `characters`
  ADD PRIMARY KEY (`CharacterID`),
  ADD KEY `CharacterLookID` (`CharacterLookID`);

--
-- Indexes for table `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`ItemID`),
  ADD KEY `ItemLookID` (`ItemLookID`);

--
-- Indexes for table `itemlook`
--
ALTER TABLE `itemlook`
  ADD PRIMARY KEY (`ItemLookID`);

--
-- Indexes for table `lucky`
--
ALTER TABLE `lucky`
  ADD PRIMARY KEY (`BlockID`),
  ADD KEY `ItemID` (`ItemID`);

--
-- Indexes for table `map`
--
ALTER TABLE `map`
  ADD PRIMARY KEY (`MapID`);

--
-- Indexes for table `mapblock`
--
ALTER TABLE `mapblock`
  ADD PRIMARY KEY (`MapID`,`BlockID`),
  ADD KEY `BlockID` (`BlockID`);

--
-- Indexes for table `mapitem`
--
ALTER TABLE `mapitem`
  ADD PRIMARY KEY (`MapID`,`ItemID`),
  ADD KEY `ItemID` (`ItemID`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `characters`
--
ALTER TABLE `characters`
  ADD CONSTRAINT `characters_ibfk_1` FOREIGN KEY (`CharacterLookID`) REFERENCES `characterlook` (`CharacterLookID`) ON DELETE CASCADE;

--
-- Constraints for table `item`
--
ALTER TABLE `item`
  ADD CONSTRAINT `item_ibfk_1` FOREIGN KEY (`ItemLookID`) REFERENCES `itemlook` (`ItemLookID`);

--
-- Constraints for table `lucky`
--
ALTER TABLE `lucky`
  ADD CONSTRAINT `lucky_ibfk_1` FOREIGN KEY (`BlockID`) REFERENCES `block` (`BlockID`) ON DELETE CASCADE,
  ADD CONSTRAINT `lucky_ibfk_2` FOREIGN KEY (`ItemID`) REFERENCES `item` (`ItemID`);

--
-- Constraints for table `mapblock`
--
ALTER TABLE `mapblock`
  ADD CONSTRAINT `mapblock_ibfk_1` FOREIGN KEY (`MapID`) REFERENCES `map` (`MapID`),
  ADD CONSTRAINT `mapblock_ibfk_2` FOREIGN KEY (`BlockID`) REFERENCES `block` (`BlockID`);

--
-- Constraints for table `mapitem`
--
ALTER TABLE `mapitem`
  ADD CONSTRAINT `mapitem_ibfk_1` FOREIGN KEY (`MapID`) REFERENCES `map` (`MapID`),
  ADD CONSTRAINT `mapitem_ibfk_2` FOREIGN KEY (`ItemID`) REFERENCES `item` (`ItemID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
