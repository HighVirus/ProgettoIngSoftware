-- MySQL dump 10.13  Distrib 8.0.29, for Linux (x86_64)
--
-- Host: localhost    Database: db_azienda
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,1,'Alessandro','Spank','alessandrospank@gmail.com','culocane'),(2,2,'Edoardo','Mannino','edoardomannino@gmail.com','ciaociao'),(3,2,'Alberto','Scannaliato','albertoscannaliato@gmail.com','provapass5'),(4,3,'Gabriele','Saporito','gabrielesaporito@gmail.com','retedue'),(5,1,'Maria','Magro','mariamagro@gmail.com','treppitre');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `catalogo_aziendale`
--

DROP TABLE IF EXISTS `catalogo_aziendale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `catalogo_aziendale` (
  `codice_aic` varchar(8) NOT NULL,
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
/*!40000 ALTER TABLE `catalogo_aziendale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `corriere_as`
--

DROP TABLE IF EXISTS `corriere_as`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `corriere_as` (
  `IDCORRIERE_C` int NOT NULL AUTO_INCREMENT,
  `codice_ordine_c` varchar(5) NOT NULL,
  PRIMARY KEY (`IDCORRIERE_C`,`codice_ordine_c`),
  KEY `codice_ordine_c` (`codice_ordine_c`),
  CONSTRAINT `corriere_as_ibfk_1` FOREIGN KEY (`IDCORRIERE_C`) REFERENCES `account` (`ID`),
  CONSTRAINT `corriere_as_ibfk_2` FOREIGN KEY (`codice_ordine_c`) REFERENCES `ordini` (`codice_ordine`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `corriere_as`
--

LOCK TABLES `corriere_as` WRITE;
/*!40000 ALTER TABLE `corriere_as` DISABLE KEYS */;
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
  `partita_iva` varchar(11) DEFAULT NULL,
  `nome_farmacia` varchar(255) DEFAULT NULL,
  `cap` varchar(255) DEFAULT NULL,
  `indirizzo` varchar(255) DEFAULT NULL,
  `numero_civico` varchar(5) DEFAULT NULL,
  UNIQUE KEY `partita_iva` (`partita_iva`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farmacia`
--

LOCK TABLES `farmacia` WRITE;
/*!40000 ALTER TABLE `farmacia` DISABLE KEYS */;
INSERT INTO `farmacia` VALUES ('15486232231','la mia farmacia','90115','via Ernesto Basile','64'),('45781004476','farmacia pennino','02475','viale Europa','41');
/*!40000 ALTER TABLE `farmacia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `farmordini`
--

DROP TABLE IF EXISTS `farmordini`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farmordini` (
  `codice_aic_mf` varchar(9) NOT NULL,
  `codice_ordine_mf` varchar(5) NOT NULL,
  PRIMARY KEY (`codice_aic_mf`,`codice_ordine_mf`),
  KEY `codice_ordine_mf` (`codice_ordine_mf`),
  CONSTRAINT `farmordini_ibfk_1` FOREIGN KEY (`codice_aic_mf`) REFERENCES `magazzino_aziendale` (`codice_aic`),
  CONSTRAINT `farmordini_ibfk_2` FOREIGN KEY (`codice_ordine_mf`) REFERENCES `ordini` (`codice_ordine`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farmordini`
--

LOCK TABLES `farmordini` WRITE;
/*!40000 ALTER TABLE `farmordini` DISABLE KEYS */;
/*!40000 ALTER TABLE `farmordini` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `magazzino_aziendale`
--

DROP TABLE IF EXISTS `magazzino_aziendale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `magazzino_aziendale` (
  `codice_aic` varchar(8) NOT NULL,
  `lotto` varchar(9) DEFAULT NULL,
  `nome_farmaco` varchar(255) NOT NULL,
  `principio_attivo` varchar(100) NOT NULL,
  `prescrivibilita` boolean NOT NULL,
  `data_scadenza` date DEFAULT NULL,
  `costo` double DEFAULT '0',
  `unita` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`codice_aic`),
  UNIQUE KEY `lotto` (`lotto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `magazzino_aziendale`
--

LOCK TABLES `magazzino_aziendale` WRITE;
/*!40000 ALTER TABLE `magazzino_aziendale` DISABLE KEYS */;
INSERT INTO `magazzino_aziendale` VALUES ('12745182','abe789','tachipirina 1000 mg 16 compresse','paracetamolo','si','2027-07-01',4.54,4503),('12745232','abe775','tachipirina 10 mg/ml soluzione per infusione','paracetamolo','no','2025-06-01',12.5,157),('19655051','bfh845','bentelan 1 mg 10 compresse resistenti ','betametasone','no','2022-07-01',1.35,19),('24840074','bgt541','cardioaspirin 100 mg 30 compresse gastroresistenti','acido acetilsalicilico','si','2022-09-01',2.35,871),('27860016','frt654','zitromax 250 mg 6 capsule rigide','azitromicina','si','2023-05-01',8.5,210),('34246013','trf741','nurofen 200 mg + 30 mg 12 compresse rivestite','ibuprofene','si','2024-12-01',6.67,0),('42386488','rfq416','brufen 400 mg 16 compresse rivestite con film','ibuprofene','si','2022-07-01',4.75,5);
/*!40000 ALTER TABLE `magazzino_aziendale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `magcat`
--

DROP TABLE IF EXISTS `magcat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `magcat` (
  `codice_aic` varchar(8) NOT NULL,
  `lotto` varchar(9) NOT NULL,
  `nome_farmaco` varchar(255) NOT NULL,
  PRIMARY KEY (`codice_aic`),
  CONSTRAINT `magcat_ibfk_1` FOREIGN KEY (`codice_aic`) REFERENCES `catalogo_aziendale` (`codice_aic`),
  CONSTRAINT `magcat_ibfk_2` FOREIGN KEY (`codice_aic`) REFERENCES `magazzino_aziendale` (`codice_aic`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `magcat`
--

LOCK TABLES `magcat` WRITE;
/*!40000 ALTER TABLE `magcat` DISABLE KEYS */;
/*!40000 ALTER TABLE `magcat` ENABLE KEYS */;
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
  `id_corriere` int NOT NULL AUTO_INCREMENT,
  `codice_aic_farmaco` varchar(9) DEFAULT NULL,
  `partita_iva_f` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`codice_ordine`),
  UNIQUE KEY `id_corriere` (`id_corriere`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordini`
--

LOCK TABLES `ordini` WRITE;
/*!40000 ALTER TABLE `ordini` DISABLE KEYS */;
INSERT INTO `ordini` VALUES ('47811','2022-07-05',4,'12745182','94165746623');
/*!40000 ALTER TABLE `ordini` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-15 22:12:22
