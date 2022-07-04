-- MySQL dump 10.13  Distrib 8.0.29, for Linux (x86_64)
--
-- Host: localhost    Database: dbfarmacia
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alefam`
--

DROP TABLE IF EXISTS `alert_farmacia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alert_farmacia`
(
    piva           VARCHAR(11) NOT NULL,
    `codice_alert` int         NOT NULL AUTO_INCREMENT,
    `type`         int         NOT NULL,
    PRIMARY KEY (`codice_alert`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK
TABLES `alert_farmacia` WRITE;
/*!40000 ALTER TABLE `alert_farmacia` DISABLE KEYS */;
INSERT INTO `alert_farmacia`
VALUES ('15486232231', 1, 1),
       ('15486232231', 2, 1),
       ('15486232231', 3, 1),
       ('15486232231', 4, 1),
       ('15486232231', 5, 1),
       ('15486232231', 6, 3);
/*!40000 ALTER TABLE `alert_farmacia` ENABLE KEYS */;
UNLOCK
TABLES;

DROP TABLE IF EXISTS `alefam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alefam`
(
    `codice_alert_af` int         NOT NULL AUTO_INCREMENT,
    piva_af           VARCHAR(11) NOT NULL,
    `codice_aic_af`   varchar(11) NOT NULL,
    `lotto_af`        varchar(9)  NOT NULL,
    PRIMARY KEY (`codice_alert_af`, `codice_aic_af`, `lotto_af`),
    KEY               `codice_aic_af` (`codice_aic_af`,`lotto_af`),
    CONSTRAINT `alefam_ibfk_1` FOREIGN KEY (`codice_alert_af`) REFERENCES `alert_farmacia` (`codice_alert`),
    CONSTRAINT `alefam_ibfk_2` FOREIGN KEY (`piva_af`, `codice_aic_af`, `lotto_af`) REFERENCES `farmaco` (`piva`, `codice_aic`, `lotto`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alefam`
--

LOCK
TABLES `alefam` WRITE;
/*!40000 ALTER TABLE `alefam` DISABLE KEYS */;
INSERT INTO `alefam`
VALUES (1, '15486232231', '12745232', 'abe775'),
       (2, '15486232231', '24840074', 'bgt541'),
       (3, '15486232231', '24840074', 'bgt541'),
       (4, '15486232231', '27860016', 'frt654'),
       (5, '15486232231', '42386488', 'rfq416');
/*!40000 ALTER TABLE `alefam` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `aleord`
--

DROP TABLE IF EXISTS `aleord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aleord`
(
    `codice_alert_ao`  int        NOT NULL AUTO_INCREMENT,
    `codice_ordine_ao` varchar(5) NOT NULL,
    PRIMARY KEY (`codice_alert_ao`, `codice_ordine_ao`),
    KEY                `codice_ordine_ao` (`codice_ordine_ao`),
    CONSTRAINT `aleord_ibfk_1` FOREIGN KEY (`codice_alert_ao`) REFERENCES `alert_farmacia` (`codice_alert`),
    CONSTRAINT `aleord_ibfk_2` FOREIGN KEY (`codice_ordine_ao`) REFERENCES `ordine` (`codice_ordine`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aleord`
--

LOCK
TABLES `aleord` WRITE;
/*!40000 ALTER TABLE `aleord` DISABLE KEYS */;
INSERT INTO `aleord`
VALUES (6, '47811');

/*!40000 ALTER TABLE `aleord` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `alert_farmacia`
--
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert_farmacia`
--



--
-- Table structure for table `farmaci_ordinati`
--

DROP TABLE IF EXISTS `farmaci_ordinati`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farmaci_ordinati`
(
    piva       VARCHAR(11) NOT NULL,
    codice_aic varchar(11) NOT NULL,
    FOREIGN KEY (piva, `codice_aic`) REFERENCES `farmaco` (piva, `codice_aic`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farmaci_ordinati`
--

LOCK
TABLES `farmaci_ordinati` WRITE;
/*!40000 ALTER TABLE `farmaci_ordinati` DISABLE KEYS */;
INSERT INTO `farmaci_ordinati`
VALUES ('15486232231', '12745182'),
       ('15486232231', '12745182'),
       ('15486232231', '24840074'),
       ('15486232231', '27860016');
/*!40000 ALTER TABLE `farmaci_ordinati` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `farmaco`
--

DROP TABLE IF EXISTS `farmaco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farmaco`
(
    `piva`             VARCHAR(11)  NOT NULL,
    `codice_aic`       varchar(11)  NOT NULL,
    `lotto`            varchar(9)   NOT NULL,
    `nome_farmaco`     varchar(255) NOT NULL,
    `principio_attivo` varchar(100) DEFAULT NULL,
    `prescrivibilita`  BOOLEAN      NOT NULL,
    `data_scadenza`    date         DEFAULT NULL,
    `costo`            double       DEFAULT NULL,
    `unita`            int          NOT NULL,
    PRIMARY KEY (`piva`, `codice_aic`, `lotto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farmaco`
--

LOCK
TABLES `farmaco` WRITE;
/*!40000 ALTER TABLE `farmaco` DISABLE KEYS */;
INSERT INTO `farmaco`
VALUES ('15486232231', '12745182', 'abe789', 'tachipirina 1000 mg 16 compresse', 'paracetamolo', false,
        '2027-07-01', 4.53, 450),
       ('15486232231', '12745232', 'abe775', 'tachipirina 10 mg/ml soluzione per infusione', 'paracetamolo', false,
        '2025-06-01', 12.5, 15),
       ('15486232231', '19655051', 'bfh845', 'bentelan 1 mg 10 compresse resistenti ', 'betametasone', false,
        '2027-07-01', 1.35, 219),
       ('15486232231', '24840074', 'bgt541', 'cardioaspirin 100 mg 30 compresse gastroresistenti',
        'acido acetilsalicilico', true, '2027-09-01', 2.34, 40),
       ('15486232231', '27860016', 'frt654', 'zitromax 250 mg 6 capsule rigide', 'azitromicina', true, '2026-05-01',
        8.5, 21),
       ('15486232231', '34246013', 'trf741', 'nurofen 200 mg + 30 mg 12 compresse rivestite', 'ibuprofene', true,
        '2027-12-01', 6,
        150),
       ('45781004476', '12745232', 'abe775', 'tachipirina 10 mg/ml soluzione per infusione', 'paracetamolo', false,
        '2025-06-01', 12.5, 15),
       ('15486232231', '42386488', 'rfq416', 'brufen 400 mg 16 compresse rivestite con film', 'ibuprofene', true,
        '2025-07-01', 4.75,
        25);
/*!40000 ALTER TABLE `farmaco` ENABLE KEYS */;
UNLOCK
TABLES;


--
-- Table structure for table `ordine`
--

DROP TABLE IF EXISTS `ordine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordine`
(
    `codice_ordine` varchar(5)  NOT NULL,
    piva            VARCHAR(11) NOT NULL,
    `data_consegna` date        NOT NULL,
    `stato`         int         NOT NULL,
    PRIMARY KEY (`codice_ordine`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordine`
--

LOCK
TABLES `ordine` WRITE;
/*!40000 ALTER TABLE `ordine` DISABLE KEYS */;
INSERT INTO `ordine`
VALUES ('47811', '15486232231', '2022-07-05', 3);
/*!40000 ALTER TABLE `ordine` ENABLE KEYS */;
UNLOCK
TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;


CREATE TABLE `ord_farmaci`
(
    `codice_ordine_of` varchar(5)  NOT NULL,
    `codice_aic_of`    varchar(11) NOT NULL,
    `lotto_of`         varchar(9)  NOT NULL,
    `data_consegna`    date        NOT NULL,
    `unita`            int         NOT NULL,
    PRIMARY KEY (`codice_ordine_of`, `codice_aic_of`, `lotto_of`),
    KEY                `codice_aic_of` (`codice_aic_of`,`lotto_of`),
    CONSTRAINT `ord_farmaci_ibfk_1` FOREIGN KEY (`codice_ordine_of`) REFERENCES `ordine` (`codice_ordine`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

