-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sty 06, 2025 at 08:58 PM
-- Wersja serwera: 10.4.32-MariaDB
-- Wersja PHP: 8.2.12

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
-- Struktura tabeli dla tabeli `block`
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
(2, '#000000', 'INDESTRUCTIBLE'),
(3, '#FFFF00', 'LUCKY');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `characterlook`
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
-- Struktura tabeli dla tabeli `characters`
--

CREATE TABLE `characters` (
  `CharacterID` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `characterSpeed` int(11) DEFAULT NULL,
  `explodePower` int(11) DEFAULT NULL,
  `explosionSpeed` int(11) DEFAULT NULL,
  `hp` int(11) DEFAULT NULL,
  `maxBombs` int(11) DEFAULT 1,
  `CharacterLookID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `item`
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
-- Struktura tabeli dla tabeli `itemlook`
--

CREATE TABLE `itemlook` (
  `ItemLookID` int(11) NOT NULL,
  `look` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `lucky`
--

CREATE TABLE `lucky` (
  `BlockID` int(11) NOT NULL,
  `lootProbability` int(11) DEFAULT NULL,
  `ItemID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `map`
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
(1, 'grey', 'https://th.bing.com/th/id/R.73264ebc46c4ec0a7eb9147f6802f559?rik=Yt%2bGFQQZURRawQ&riu=http%3a%2f%2fwww.pixelstalk.net%2fwp-content%2fuploads%2f2016%2f10%2fDark-Gray-Backgrounds-Desktop.jpg&ehk=GT3wwryuXDC55xpO7oyDRf%2baVhgM0BW4R5rq%2bpQR60I%3d&risl=&pid=I'),
(2, 'blue', 'https://static.vecteezy.com/system/resources/previews/009/075/727/original/dark-blue-low-poly-layout-vector.jpg');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `mapblock`
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
(1, 1, 0, 2),
(1, 1, 0, 6),
(1, 1, 0, 10),
(1, 1, 1, 2),
(1, 1, 1, 3),
(1, 1, 1, 7),
(1, 1, 2, 0),
(1, 1, 2, 1),
(1, 1, 2, 4),
(1, 1, 2, 13),
(1, 1, 3, 4),
(1, 1, 3, 7),
(1, 1, 3, 13),
(1, 1, 5, 0),
(1, 1, 5, 13),
(1, 1, 6, 0),
(1, 1, 6, 2),
(1, 1, 6, 10),
(1, 1, 6, 12),
(1, 1, 8, 0),
(1, 1, 8, 2),
(1, 1, 8, 10),
(1, 1, 8, 13),
(1, 1, 11, 1),
(1, 1, 11, 2),
(1, 1, 12, 2),
(1, 1, 12, 11),
(1, 1, 13, 11),
(1, 1, 13, 12),
(1, 1, 14, 1),
(1, 1, 15, 2),
(1, 1, 15, 12),
(1, 1, 16, 2),
(1, 1, 16, 10),
(1, 1, 17, 12),
(1, 1, 18, 2),
(1, 1, 18, 10),
(1, 1, 19, 12),
(1, 1, 20, 0),
(1, 1, 20, 6),
(1, 1, 20, 9),
(1, 1, 21, 0),
(1, 1, 21, 9),
(1, 1, 21, 12),
(1, 1, 22, 6),
(1, 1, 22, 10),
(1, 1, 22, 11),
(1, 1, 23, 3),
(1, 1, 23, 7),
(1, 1, 23, 11),
(1, 2, 0, 3),
(1, 2, 0, 4),
(1, 2, 1, 1),
(1, 2, 1, 5),
(1, 2, 1, 8),
(1, 2, 1, 9),
(1, 2, 1, 11),
(1, 2, 1, 12),
(1, 2, 2, 3),
(1, 2, 2, 8),
(1, 2, 2, 10),
(1, 2, 3, 0),
(1, 2, 3, 2),
(1, 2, 3, 5),
(1, 2, 3, 8),
(1, 2, 3, 10),
(1, 2, 4, 2),
(1, 2, 4, 12),
(1, 2, 5, 1),
(1, 2, 5, 3),
(1, 2, 5, 4),
(1, 2, 5, 5),
(1, 2, 5, 7),
(1, 2, 5, 8),
(1, 2, 5, 9),
(1, 2, 5, 11),
(1, 2, 6, 5),
(1, 2, 6, 7),
(1, 2, 7, 1),
(1, 2, 7, 3),
(1, 2, 7, 4),
(1, 2, 7, 5),
(1, 2, 7, 7),
(1, 2, 7, 8),
(1, 2, 7, 9),
(1, 2, 7, 11),
(1, 2, 8, 5),
(1, 2, 8, 7),
(1, 2, 8, 11),
(1, 2, 8, 12),
(1, 2, 9, 0),
(1, 2, 9, 1),
(1, 2, 9, 5),
(1, 2, 9, 7),
(1, 2, 10, 4),
(1, 2, 10, 8),
(1, 2, 10, 11),
(1, 2, 10, 12),
(1, 2, 11, 0),
(1, 2, 11, 3),
(1, 2, 11, 9),
(1, 2, 11, 10),
(1, 2, 13, 2),
(1, 2, 13, 3),
(1, 2, 13, 9),
(1, 2, 13, 10),
(1, 2, 13, 13),
(1, 2, 14, 0),
(1, 2, 14, 4),
(1, 2, 14, 8),
(1, 2, 14, 11),
(1, 2, 15, 1),
(1, 2, 15, 5),
(1, 2, 15, 7),
(1, 2, 15, 13),
(1, 2, 16, 5),
(1, 2, 16, 7),
(1, 2, 16, 11),
(1, 2, 17, 1),
(1, 2, 17, 3),
(1, 2, 17, 4),
(1, 2, 17, 5),
(1, 2, 17, 7),
(1, 2, 17, 8),
(1, 2, 17, 9),
(1, 2, 17, 11),
(1, 2, 18, 5),
(1, 2, 18, 7),
(1, 2, 18, 13),
(1, 2, 19, 1),
(1, 2, 19, 3),
(1, 2, 19, 4),
(1, 2, 19, 5),
(1, 2, 19, 7),
(1, 2, 19, 8),
(1, 2, 19, 9),
(1, 2, 19, 11),
(1, 2, 20, 2),
(1, 2, 20, 3),
(1, 2, 20, 5),
(1, 2, 20, 8),
(1, 2, 20, 11),
(1, 2, 20, 13),
(1, 2, 21, 3),
(1, 2, 21, 10),
(1, 2, 22, 1),
(1, 2, 22, 2),
(1, 2, 22, 4),
(1, 2, 22, 5),
(1, 2, 22, 8),
(1, 2, 22, 12),
(1, 2, 23, 9),
(1, 2, 23, 10),
(1, 3, 0, 13),
(1, 3, 2, 9),
(1, 3, 7, 2),
(1, 3, 7, 10),
(1, 3, 12, 6),
(1, 3, 17, 2),
(1, 3, 17, 10),
(1, 3, 21, 4),
(1, 3, 23, 0);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `mapitem`
--

CREATE TABLE `mapitem` (
  `MapID` int(11) NOT NULL,
  `ItemID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `block`
--
ALTER TABLE `block`
  ADD PRIMARY KEY (`BlockID`);

--
-- Indeksy dla tabeli `characterlook`
--
ALTER TABLE `characterlook`
  ADD PRIMARY KEY (`CharacterLookID`);

--
-- Indeksy dla tabeli `characters`
--
ALTER TABLE `characters`
  ADD PRIMARY KEY (`CharacterID`),
  ADD KEY `CharacterLookID` (`CharacterLookID`);

--
-- Indeksy dla tabeli `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`ItemID`),
  ADD KEY `ItemLookID` (`ItemLookID`);

--
-- Indeksy dla tabeli `itemlook`
--
ALTER TABLE `itemlook`
  ADD PRIMARY KEY (`ItemLookID`);

--
-- Indeksy dla tabeli `lucky`
--
ALTER TABLE `lucky`
  ADD PRIMARY KEY (`BlockID`),
  ADD KEY `ItemID` (`ItemID`);

--
-- Indeksy dla tabeli `map`
--
ALTER TABLE `map`
  ADD PRIMARY KEY (`MapID`);

--
-- Indeksy dla tabeli `mapblock`
--
ALTER TABLE `mapblock`
  ADD PRIMARY KEY (`MapID`,`positionX`,`positionY`),
  ADD KEY `mapblock_fk_2` (`BlockID`);

--
-- Indeksy dla tabeli `mapitem`
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
  ADD CONSTRAINT `mapblock_fk_2` FOREIGN KEY (`BlockID`) REFERENCES `block` (`BlockID`),
  ADD CONSTRAINT `mapblock_ibfk_1` FOREIGN KEY (`MapID`) REFERENCES `map` (`MapID`);

--
-- Constraints for table `mapitem`
--
ALTER TABLE `mapitem`
  ADD CONSTRAINT `mapitem_ibfk_1` FOREIGN KEY (`MapID`) REFERENCES `map` (`MapID`),
  ADD CONSTRAINT `mapitem_ibfk_2` FOREIGN KEY (`ItemID`) REFERENCES `item` (`ItemID`);
COMMIT;

INSERT INTO `characterlook` (`CharacterLookID`, `head`, `behind`, `front`, `leftSide`, `rightSide`) VALUES
(2, '/org/example/explodeitapp/images/ninja.png', '/org/example/explodeitapp/images/ninja.png', '/org/example/explodeitapp/images/ninja.png', '/org/example/explodeitapp/images/ninja.png', '/org/example/explodeitapp/images/ninja.png'),
(3, '/org/example/explodeitapp/images/ninja.png', '/org/example/explodeitapp/images/ninja.png', '/org/example/explodeitapp/images/ninja.png', '/org/example/explodeitapp/images/ninja.png', '/org/example/explodeitapp/images/ninja.png'),
(4, '/org/example/explodeitapp/images/ninja.png', '/org/example/explodeitapp/images/ninja.png', '/org/example/explodeitapp/images/ninja.png', '/org/example/explodeitapp/images/ninja.png', '/org/example/explodeitapp/images/ninja.png');

INSERT INTO `characters` (`CharacterID`, `name`, `characterSpeed`, `explodePower`, `explosionSpeed`, `hp`, `CharacterLookID`) VALUES
(2, 'Ninja', 3, 1, 5, 80, 2),
(3, 'Ninja2', 7, 3, 1, 100, 3);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
