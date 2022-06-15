-- MySQL dump 10.13  Distrib 8.0.29, for Linux (x86_64)
--
-- Host: localhost    Database: db_farmacia
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
-- Table structure for table `farmaco`
--

DROP TABLE IF EXISTS `farmaco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farmaco` (
  `codice_aic` varchar(255) NOT NULL,
  `lotto` varchar(9) DEFAULT NULL,
  `nome_farmaco` varchar(255) NOT NULL,
  `principio_attivo` varchar(100) DEFAULT NULL,
  `prescrivibilita` varchar(2) NOT NULL,
  `data_scadenza` date DEFAULT NULL,
  `costo` float DEFAULT '0',
  `unita` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`codice_aic`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farmaco`
--

LOCK TABLES `farmaco` WRITE;
/*!40000 ALTER TABLE `farmaco` DISABLE KEYS */;
INSERT INTO `farmaco` VALUES 
('12745182','abe789','tachipirina 1000 mg 16 compresse','paracetamolo','no','2027-07-01',4.54,250),
('12745232','abe775','tachipirina 10 mg/ml soluzione per infusione','paracetamolo','no','2025-06-01',12.5,15),
('19655051','bfh845','bentelan 1 mg 10 compresse resistenti ','betametasone','no','2022-07-01',1.35,19),
('24840074','bgt541','cardioaspirin 100 mg 30 compresse gastroresistenti','acido acetilsalicilico','si','2022-09-01',2.35,40),
('27860016','frt654','zitromax 250 mg 6 capsule rigide','azitromicina','si','2023-05-01',8.5,21),
('34246013','trf741','nurofen 200 mg + 30 mg 12 compresse rivestite','ibuprofene','si','2024-12-01',6.67,150),
('42386488','rfq416','brufen 400 mg 16 compresse rivestite con film','ibuprofene','si','2022-07-01',4.75,25)
;


/*!40000 ALTER TABLE `farmaco` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-15 15:44:29

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
('026803080', 'omeprazen 10 MG 14 capsule rigide gastroresistenti', 'omeprazolo', 'no', '2023-01-01', 7.85, 45)
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
WHERE unita < 20
;

