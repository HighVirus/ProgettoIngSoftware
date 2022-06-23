-- MySQL dump 10.13  Distrib 8.0.29, for Linux (x86_64)
--
-- Host: localhost    Database: dbazienda
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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `type` int NOT NULL,
  `nome` varchar(100) DEFAULT NULL,
  `cognome` varchar(100) DEFAULT NULL,
  `email` varchar(200) NOT NULL,
  `password` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,1,'Alessandro','Spank','1','1'),(2,2,'Alessandro','Spank','2','2'),(3,3,'Alessandro','Spank','3','3'),(4,2,'Edoardo','Mannino','edoardomannino@gmail.com','ciaociao'),(5,2,'Alberto','Scannaliato','albertoscannaliato@gmail.com','provapass5'),(6,3,'Gabriele','Saporito','gabrielesaporito@gmail.com','retedue'),(7,1,'Maria','Magro','mariamagro@gmail.com','treppitre');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `catalogo_aziendale`
--

DROP TABLE IF EXISTS `catalogo_aziendale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `catalogo_aziendale` (
  `codice_aic` varchar(9) NOT NULL,
  `nome_farmaco` varchar(255) NOT NULL,
  `principio_attivo` varchar(100) NOT NULL,
  `costo` double DEFAULT '0',
  PRIMARY KEY (`codice_aic`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `catalogo_aziendale`
--

LOCK TABLES `catalogo_aziendale` WRITE;
/*!40000 ALTER TABLE `catalogo_aziendale` DISABLE KEYS */;
INSERT INTO `catalogo_aziendale` VALUES ('12745182','tachipirina 1000 mg 16 compresse','paracetamolo',4.54),('12745232','tachipirina 10 mg/ml soluzione per infusione','paracetamolo',12.5),('19655051','bentelan 1 mg 10 compresse resistenti ','betametasone',1.35),('24840074','cardioaspirin 100 mg 30 compresse gastroresistenti','acido acetilsalicilico',2.35),('27860016','zitromax 250 mg 6 capsule rigide','azitromicina',8.5),('34246013','nurofen 200 mg + 30 mg 12 compresse rivestite','ibuprofene',6.67),('42386488','brufen 400 mg 16 compresse rivestite con film','ibuprofene',4.75);
/*!40000 ALTER TABLE `catalogo_aziendale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `corriere_as`
--

DROP TABLE IF EXISTS `corriere_as`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `corriere_as` (
  `codice_ordine_c` varchar(5) NOT NULL,
  `idaccount_c` int NOT NULL,
  PRIMARY KEY (`codice_ordine_c`,`idaccount_c`),
  KEY `idaccount_c` (`idaccount_c`),
  CONSTRAINT `corriere_as_ibfk_1` FOREIGN KEY (`codice_ordine_c`) REFERENCES `ordini` (`codice_ordine`),
  CONSTRAINT `corriere_as_ibfk_2` FOREIGN KEY (`idaccount_c`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `corriere_as`
--

LOCK TABLES `corriere_as` WRITE;
/*!40000 ALTER TABLE `corriere_as` DISABLE KEYS */;
INSERT INTO `corriere_as` VALUES ('47811',4);
/*!40000 ALTER TABLE `corriere_as` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `farmaccount`
--

DROP TABLE IF EXISTS `farmaccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farmaccount` (
  `IDACCOUNT_F` int NOT NULL AUTO_INCREMENT,
  `partita_iva` varchar(255) NOT NULL,
  PRIMARY KEY (`IDACCOUNT_F`,`partita_iva`),
  KEY `partita_iva` (`partita_iva`),
  CONSTRAINT `farmaccount_ibfk_1` FOREIGN KEY (`IDACCOUNT_F`) REFERENCES `account` (`ID`),
  CONSTRAINT `farmaccount_ibfk_2` FOREIGN KEY (`partita_iva`) REFERENCES `farmacia` (`partita_iva`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farmaccount`
--

LOCK TABLES `farmaccount` WRITE;
/*!40000 ALTER TABLE `farmaccount` DISABLE KEYS */;
INSERT INTO `farmaccount` VALUES (2,'15486232231'),(3,'45781004476');
/*!40000 ALTER TABLE `farmaccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `farmacia`
--

DROP TABLE IF EXISTS `farmacia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farmacia` (
  `partita_iva` varchar(11) NOT NULL,
  `nome_farmacia` varchar(255) DEFAULT NULL,
  `cap` varchar(255) DEFAULT NULL,
  `indirizzo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`partita_iva`),
  UNIQUE KEY `partita_iva` (`partita_iva`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farmacia`
--

LOCK TABLES `farmacia` WRITE;
/*!40000 ALTER TABLE `farmacia` DISABLE KEYS */;
INSERT INTO `farmacia` VALUES ('15486232231','la mia farmacia','90115','via Ernesto Basile 64'),('45781004476','farmacia pennino','02475','viale Europa 41');
/*!40000 ALTER TABLE `farmacia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `farmacia_ord`
--

DROP TABLE IF EXISTS `farmacia_ord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farmacia_ord` (
  `codice_ordine_fo` varchar(5) NOT NULL,
  `partita_iva_fo` varchar(11) NOT NULL,
  PRIMARY KEY (`codice_ordine_fo`,`partita_iva_fo`),
  KEY `partita_iva_fo` (`partita_iva_fo`),
  CONSTRAINT `farmacia_ord_ibfk_1` FOREIGN KEY (`codice_ordine_fo`) REFERENCES `ordini` (`codice_ordine`),
  CONSTRAINT `farmacia_ord_ibfk_2` FOREIGN KEY (`partita_iva_fo`) REFERENCES `farmacia` (`partita_iva`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farmacia_ord`
--

LOCK TABLES `farmacia_ord` WRITE;
/*!40000 ALTER TABLE `farmacia_ord` DISABLE KEYS */;
INSERT INTO `farmacia_ord` VALUES ('47811','15486232231');
/*!40000 ALTER TABLE `farmacia_ord` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `magazzino_aziendale`
--

DROP TABLE IF EXISTS `magazzino_aziendale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `magazzino_aziendale` (
  `codice_aic` varchar(9) NOT NULL,
  `lotto` varchar(9) DEFAULT NULL,
  `nome_farmaco` varchar(255) NOT NULL,
  `principio_attivo` varchar(100) NOT NULL,
  `prescrivibilita` tinyint(1) NOT NULL,
  `data_scadenza` date DEFAULT NULL,
  `costo` double DEFAULT '0',
  `unita` int NOT NULL DEFAULT '0',
  UNIQUE KEY `lotto` (`lotto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `magazzino_aziendale`
--

LOCK TABLES `magazzino_aziendale` WRITE;
/*!40000 ALTER TABLE `magazzino_aziendale` DISABLE KEYS */;
INSERT INTO `magazzino_aziendale` VALUES ('12745182','abe789','tachipirina 1000 mg 16 compresse','paracetamolo',0,'2027-07-01',4.54,4503),('12745182','abe790','tachipirina 1000 mg 16 compresse','paracetamolo',0,'2028-07-01',4.54,4503),('12745232','abe775','tachipirina 10 mg/ml soluzione per infusione','paracetamolo',1,'2025-06-01',12.5,157),('19655051','bfh845','bentelan 1 mg 10 compresse resistenti ','betametasone',0,'2022-07-01',1.35,19),('24840074','bgt541','cardioaspirin 100 mg 30 compresse gastroresistenti','acido acetilsalicilico',0,'2022-09-01',2.35,871),('27860016','frt654','zitromax 250 mg 6 capsule rigide','azitromicina',1,'2023-05-01',8.5,210),('34246013','trf741','nurofen 200 mg + 30 mg 12 compresse rivestite','ibuprofene',1,'2024-12-01',6.67,0),('42386488','rfq416','brufen 400 mg 16 compresse rivestite con film','ibuprofene',0,'2022-07-01',4.75,5);
/*!40000 ALTER TABLE `magazzino_aziendale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `magcat`
--

DROP TABLE IF EXISTS `magcat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `magcat` (
  `codice_aic_mc` varchar(9) NOT NULL,
  `lotto_mc` varchar(9) NOT NULL,
  PRIMARY KEY (`codice_aic_mc`,`lotto_mc`),
  KEY `lotto_mc` (`lotto_mc`),
  CONSTRAINT `magcat_ibfk_1` FOREIGN KEY (`codice_aic_mc`) REFERENCES `catalogo_aziendale` (`codice_aic`),
  CONSTRAINT `magcat_ibfk_2` FOREIGN KEY (`lotto_mc`) REFERENCES `magazzino_aziendale` (`lotto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `magcat`
--

LOCK TABLES `magcat` WRITE;
/*!40000 ALTER TABLE `magcat` DISABLE KEYS */;
INSERT INTO `magcat` VALUES ('12745232','abe775'),('12745182','abe789'),('12745182','abe790'),('19655051','bfh845'),('24840074','bgt541'),('27860016','frt654'),('42386488','rfq416'),('34246013','trf741');
/*!40000 ALTER TABLE `magcat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ord_far`
--

DROP TABLE IF EXISTS `ord_far`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ord_far` (
  `codice_ordine_o` varchar(5) NOT NULL,
  `codice_aic_o` varchar(9) NOT NULL,
  `lotto_o` varchar(9) NOT NULL,
  `unita` int NOT NULL,
  KEY `ord_far_ibfk_1` (`codice_ordine_o`),
  CONSTRAINT `ord_far_ibfk_1` FOREIGN KEY (`codice_ordine_o`) REFERENCES `ordini` (`codice_ordine`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ord_far`
--

LOCK TABLES `ord_far` WRITE;
/*!40000 ALTER TABLE `ord_far` DISABLE KEYS */;
INSERT INTO `ord_far` VALUES ('47811','24840074','bgt541',550),('47811','12745182','abe789',23),('47811','12745182','abe790',40),('47811','27860016','frt654',350);
/*!40000 ALTER TABLE `ord_far` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordini`
--

DROP TABLE IF EXISTS `ordini`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordini` (
  `codice_ordine` varchar(5) NOT NULL,
  `data_consegna` date NOT NULL,
  `stato` int NOT NULL,
  PRIMARY KEY (`codice_ordine`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordini`
--

LOCK TABLES `ordini` WRITE;
/*!40000 ALTER TABLE `ordini` DISABLE KEYS */;
INSERT INTO `ordini` VALUES ('47811','2022-07-05',3);
/*!40000 ALTER TABLE `ordini` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_mag`
--

DROP TABLE IF EXISTS `prod_mag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prod_mag` (
  `codice_aic_pm` varchar(9) NOT NULL,
  `lotto_pm` varchar(9) NOT NULL,
  PRIMARY KEY (`codice_aic_pm`,`lotto_pm`),
  KEY `lotto_pm` (`lotto_pm`),
  CONSTRAINT `prod_mag_ibfk_1` FOREIGN KEY (`codice_aic_pm`) REFERENCES `produzione_farmaco` (`codice_aic_p`),
  CONSTRAINT `prod_mag_ibfk_2` FOREIGN KEY (`lotto_pm`) REFERENCES `magazzino_aziendale` (`lotto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_mag`
--

LOCK TABLES `prod_mag` WRITE;
/*!40000 ALTER TABLE `prod_mag` DISABLE KEYS */;
INSERT INTO `prod_mag` VALUES ('12745232','abe775'),('12745182','abe789'),('19655051','bfh845'),('24840074','bgt541'),('27860016','frt654'),('42386488','rfq416'),('34246013','trf741');
/*!40000 ALTER TABLE `prod_mag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produzione_farmaco`
--

DROP TABLE IF EXISTS `produzione_farmaco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produzione_farmaco` (
  `codice_aic_p` varchar(9) NOT NULL,
  `start_production_date` date NOT NULL,
  `production_period` int NOT NULL,
  `unita_production` int NOT NULL,
  PRIMARY KEY (`codice_aic_p`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produzione_farmaco`
--

LOCK TABLES `produzione_farmaco` WRITE;
/*!40000 ALTER TABLE `produzione_farmaco` DISABLE KEYS */;
/*!40000 ALTER TABLE `produzione_farmaco` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-23 20:57:48
