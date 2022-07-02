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

DROP TABLE IF EXISTS `alefam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alefam` (
  `codice_alert_af` int NOT NULL AUTO_INCREMENT,
  `codice_aic_af` varchar(11) NOT NULL,
  `lotto_af` varchar(9) NOT NULL,
  PRIMARY KEY (`codice_alert_af`,`codice_aic_af`,`lotto_af`),
  KEY `codice_aic_af` (`codice_aic_af`,`lotto_af`),
  CONSTRAINT `alefam_ibfk_1` FOREIGN KEY (`codice_alert_af`) REFERENCES `alert_farmacia` (`codice_alert`),
  CONSTRAINT `alefam_ibfk_2` FOREIGN KEY (`codice_aic_af`, `lotto_af`) REFERENCES `farmaco` (`codice_aic`, `lotto`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alefam`
--

LOCK TABLES `alefam` WRITE;
/*!40000 ALTER TABLE `alefam` DISABLE KEYS */;
INSERT INTO `alefam` VALUES (1,'12745232','abe775'),(2,'24840074','bgt541'),(3,'24840074','bgt541'),(4,'27860016','frt654'),(5,'42386488','rfq416');
/*!40000 ALTER TABLE `alefam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aleord`
--

DROP TABLE IF EXISTS `aleord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aleord` (
  `codice_alert_ao` int NOT NULL AUTO_INCREMENT,
  `codice_ordine_ao` varchar(5) NOT NULL,
  PRIMARY KEY (`codice_alert_ao`,`codice_ordine_ao`),
  KEY `codice_ordine_ao` (`codice_ordine_ao`),
  CONSTRAINT `aleord_ibfk_1` FOREIGN KEY (`codice_alert_ao`) REFERENCES `alert_farmacia` (`codice_alert`),
  CONSTRAINT `aleord_ibfk_2` FOREIGN KEY (`codice_ordine_ao`) REFERENCES `ordine` (`codice_ordine`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aleord`
--

LOCK TABLES `aleord` WRITE;
/*!40000 ALTER TABLE `aleord` DISABLE KEYS */;
INSERT INTO `aleord` VALUES (6,'47811');

/*!40000 ALTER TABLE `aleord` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alert_farmacia`
--

DROP TABLE IF EXISTS `alert_farmacia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alert_farmacia` (
  `codice_alert` int NOT NULL AUTO_INCREMENT,
  `type` int NOT NULL,
  PRIMARY KEY (`codice_alert`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert_farmacia`
--

LOCK TABLES `alert_farmacia` WRITE;
/*!40000 ALTER TABLE `alert_farmacia` DISABLE KEYS */;
INSERT INTO `alert_farmacia` VALUES (1,1),(2,1),(3,1),(4,1),(5,1),(6,2);
/*!40000 ALTER TABLE `alert_farmacia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `farmaci_ordinati`
--

DROP TABLE IF EXISTS `farmaci_ordinati`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farmaci_ordinati` (
  codice_aic varchar(11) NOT NULL,
  PRIMARY KEY (`codice_aic_ord`),
  FOREIGN KEY (`codice_aic`) REFERENCES `farmaco` (`codice_aic`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farmaci_ordinati`
--

LOCK TABLES `farmaci_ordinati` WRITE;
/*!40000 ALTER TABLE `farmaci_ordinati` DISABLE KEYS */;
INSERT INTO `farmaci_ordinati` VALUES ('12745182'),
                                      ('12745182'),
                                      ('24840074'),
                                      ('27860016');
/*!40000 ALTER TABLE `farmaci_ordinati` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `farmaco`
--

DROP TABLE IF EXISTS `farmaco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farmaco` (
  `codice_aic` varchar(11) NOT NULL,
  `lotto` varchar(9) NOT NULL,
  `nome_farmaco` varchar(255) NOT NULL,
  `principio_attivo` varchar(100) DEFAULT NULL,
  `prescrivibilita` BOOLEAN NOT NULL,
  `data_scadenza` date DEFAULT NULL,
  `costo` double DEFAULT NULL,
  `unita` int NOT NULL,
  PRIMARY KEY (`codice_aic`,`lotto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farmaco`
--

LOCK TABLES `farmaco` WRITE;
/*!40000 ALTER TABLE `farmaco` DISABLE KEYS */;
INSERT INTO `farmaco` VALUES ('12745182','abe789','tachipirina 1000 mg 16 compresse','paracetamolo',false,'2027-07-01',4.53,450),('12745232','abe775','tachipirina 10 mg/ml soluzione per infusione','paracetamolo',false,'2025-06-01',12.5,15),('19655051','bfh845','bentelan 1 mg 10 compresse resistenti ','betametasone',false,'2027-07-01',1.35,219),('24840074','bgt541','cardioaspirin 100 mg 30 compresse gastroresistenti','acido acetilsalicilico',true,'2027-09-01',2.34,40),('27860016','frt654','zitromax 250 mg 6 capsule rigide','azitromicina',true,'2026-05-01',8.5,21),('34246013','trf741','nurofen 200 mg + 30 mg 12 compresse rivestite','ibuprofene',true,'2027-12-01',6,150),('42386488','rfq416','brufen 400 mg 16 compresse rivestite con film','ibuprofene',true,'2025-07-01',4.75,25);
/*!40000 ALTER TABLE `farmaco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ord_farmaci`
--

DROP TABLE IF EXISTS `ord_farmaci`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ord_farmaci` (
  `codice_ordine_of` varchar(5) NOT NULL,
  `codice_aic_of` varchar(11) NOT NULL,
  `lotto_of` varchar(9) NOT NULL,
  unita INTEGER NOT NULL,
  PRIMARY KEY (`codice_ordine_of`,`codice_aic_of`,`lotto_of`),
  KEY `codice_aic_of` (`codice_aic_of`,`lotto_of`),
  CONSTRAINT `ord_farmaci_ibfk_1` FOREIGN KEY (`codice_ordine_of`) REFERENCES `ordine` (`codice_ordine`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ord_farmaci`
--

LOCK TABLES `ord_farmaci` WRITE;
/*!40000 ALTER TABLE `ord_farmaci` DISABLE KEYS */;
INSERT INTO `ord_farmaci` VALUES ('47811','12745182','abe789'),('47811','12745182','abe790'),('47811','24840074','bgt541'),('47811','27860016','frt654');
/*!40000 ALTER TABLE `ord_farmaci` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordine`
--

DROP TABLE IF EXISTS `ordine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordine` (
  `codice_ordine` varchar(5) NOT NULL,
  `data_consegna` date NOT NULL,
  `stato` int NOT NULL,
  PRIMARY KEY (`codice_ordine`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordine`
--

LOCK TABLES `ordine` WRITE;
/*!40000 ALTER TABLE `ordine` DISABLE KEYS */;
INSERT INTO `ordine` VALUES ('47811','2022-07-05',3);
/*!40000 ALTER TABLE `ordine` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-24 19:47:46
/* seleziona tutti i farmaci presenti all'interno del database */ 

select *
from farmaco;

/* aggiornare le unità di farmaco in base all'ordine periodico */
/* Il valore 200 è indicativo. Dipende dall'ordine periodico della farmacia */

UPDATE farmaco
SET unita = unita + 200
WHERE codice_aic = '12745182' or codice_aic = '19655051'
;

/* aggiungere un nuovo prodotto all'interno del magazzino della farmacia
Ho inserito un dato a caso */

INSERT INTO farmaco VALUES
('026803080', 'omeprazen 10 MG 14 capsule rigide gastroresistenti', 'omeprazolo', false, '2023-01-01', 7.85, 45)
;

/* vendere un farmaco */

select unita
from farmaco F1
where F1.codice_aic = '12745182'

UPDATE farmaco
SET unita - (SELECT unita
              FROM farmaco1 F2
              WHERE F2.codice_aic = F1.codice_aic)
;

/* seleziona tutti i farmaci con una quantità di farmaci inferiore a 20 */

SELECT codice_aic, nome_farmaco
FROM farmaco
WHERE unita < 50
;

