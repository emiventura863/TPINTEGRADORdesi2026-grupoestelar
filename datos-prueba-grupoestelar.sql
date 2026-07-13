-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: grupoestelar
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `contrato`
--

LOCK TABLES `contrato` WRITE;
/*!40000 ALTER TABLE `contrato` DISABLE KEYS */;
INSERT INTO `contrato` VALUES (1,'Pago mensual por transferencia',10,12,_binary '\0','ACTIVO','2026-08-01',250000,1,1),(2,'PAgo mensual en efectivo',10,24,_binary '\0','ACTIVO','2026-08-05',300000,2,2);
/*!40000 ALTER TABLE `contrato` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `estado_publicacion_historial`
--

LOCK TABLES `estado_publicacion_historial` WRITE;
/*!40000 ALTER TABLE `estado_publicacion_historial` DISABLE KEYS */;
INSERT INTO `estado_publicacion_historial` VALUES (1,'ACTIVA','2026-07-13 16:57:12.175755',1),(2,'PAUSADA','2026-07-13 16:57:12.187725',2),(3,'FINALIZADA','2026-07-13 16:57:12.198095',3),(4,'ACTIVA','2026-07-13 16:57:12.209117',4);
/*!40000 ALTER TABLE `estado_publicacion_historial` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `factura`
--

LOCK TABLES `factura` WRITE;
/*!40000 ALTER TABLE `factura` DISABLE KEYS */;
/*!40000 ALTER TABLE `factura` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `historial_estado_contrato`
--

LOCK TABLES `historial_estado_contrato` WRITE;
/*!40000 ALTER TABLE `historial_estado_contrato` DISABLE KEYS */;
INSERT INTO `historial_estado_contrato` VALUES (1,'BORRADOR','2026-07-13 16:57:58.529769',1),(2,'ACTIVO','2026-07-13 16:58:26.374356',2),(3,'ACTIVO','2026-07-13 16:58:39.363539',1);
/*!40000 ALTER TABLE `historial_estado_contrato` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `historial_estado_factura`
--

LOCK TABLES `historial_estado_factura` WRITE;
/*!40000 ALTER TABLE `historial_estado_factura` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial_estado_factura` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `historial_estado_propiedad`
--

LOCK TABLES `historial_estado_propiedad` WRITE;
/*!40000 ALTER TABLE `historial_estado_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial_estado_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `persona`
--

LOCK TABLES `persona` WRITE;
/*!40000 ALTER TABLE `persona` DISABLE KEYS */;
INSERT INTO `persona` VALUES (1,'Gimenez',_binary '\0','Marcos'),(2,'Fernández',_binary '\0','Lucía'),(3,'Pérez',_binary '\0','Juan'),(4,'López',_binary '\0','María');
/*!40000 ALTER TABLE `persona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `propiedad`
--

LOCK TABLES `propiedad` WRITE;
/*!40000 ALTER TABLE `propiedad` DISABLE KEYS */;
INSERT INTO `propiedad` VALUES (1,NULL,'Córdoba',NULL,NULL,'Av. Siempre Viva 742',_binary '\0','ALQUILADA',NULL,NULL,NULL),(2,NULL,'Rosario',NULL,NULL,'Belgrano 1234',_binary '\0','ALQUILADA',NULL,NULL,NULL),(3,NULL,'Mendoza',NULL,NULL,'San Martín 456',_binary '\0','RESERVADA',NULL,NULL,NULL),(4,NULL,'Córdoba',NULL,NULL,'Mitre 789',_binary '\0','DISPONIBLE',NULL,NULL,NULL);
/*!40000 ALTER TABLE `propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `publicacion`
--

LOCK TABLES `publicacion` WRITE;
/*!40000 ALTER TABLE `publicacion` DISABLE KEYS */;
INSERT INTO `publicacion` VALUES (1,'Depósito de 2 meses. Se aceptan mascotas.','Departamento de 2 ambientes con balcón.',_binary '\0','ACTIVA','2026-07-08',180000,1),(2,'Garantía de 3 meses. No se aceptan mascotas.','Casa con patio y cochera.',_binary '\0','PAUSADA','2026-07-03',220000,2),(3,'Depósito de 1 mes.','Departamento compacto en zona céntrica.',_binary '\0','FINALIZADA','2026-06-23',150000,3),(4,'Se acepta mascotas y 1 mes de garantía.','Departamento luminoso con vista al parque.',_binary '\0','ACTIVA','2026-07-11',260000,4);
/*!40000 ALTER TABLE `publicacion` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-13 17:04:07
