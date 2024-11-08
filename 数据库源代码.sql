CREATE DATABASE  IF NOT EXISTS `sciences` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `sciences`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: sciences
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `articles`
--

DROP TABLE IF EXISTS `articles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `articles` (
  `articleID` int NOT NULL AUTO_INCREMENT,
  `authorID` int NOT NULL,
  `title` varchar(50) NOT NULL,
  `content` text NOT NULL,
  `createTime` timestamp NOT NULL,
  `type` varchar(10) NOT NULL,
  PRIMARY KEY (`articleID`),
  KEY `authorID_idx` (`authorID`),
  KEY `articlesType_idx` (`type`),
  CONSTRAINT `articles_authorID` FOREIGN KEY (`authorID`) REFERENCES `user` (`userID`) ON DELETE CASCADE,
  CONSTRAINT `articles_type` FOREIGN KEY (`type`) REFERENCES `articletype` (`type`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `articles`
--

LOCK TABLES `articles` WRITE;
/*!40000 ALTER TABLE `articles` DISABLE KEYS */;
INSERT INTO `articles` VALUES (1,1,'title测试1','asdawf','2023-04-30 16:00:00','生活小窍门'),(2,2,'title测试2','faef','2023-04-30 16:00:00','生活小窍门'),(3,3,'title测试3','wf','2023-04-30 16:00:00','文化生活'),(4,4,'title5','awf','2023-04-30 16:00:00','文化生活'),(5,5,'bbb','asfewwfs','2023-04-30 16:00:00','文化生活'),(6,6,'title测试6','fa','2023-04-30 16:00:00','生活小窍门'),(9,9,'title测试9','fvadfg','2023-04-30 16:00:00','科技生活'),(10,2,'title测试3','faefaw','2023-04-30 16:00:00','生活小窍门'),(13,3,'nmslbst','你好吗','2023-04-30 16:00:00','科技生活'),(15,5,'撕又撕不掉，吃也吃不得，水果上的贴纸到底干嘛用的？','不少人在剥猕猴桃皮前，习惯把果皮上的贴纸先揭下来扔掉。英国姑娘凯莉·安古德（Kelly Angood）看了会大喊：不要的贴纸都给我啊！\n\n凯莉一直从事平面设计工作，偶然注意到水果上的小贴纸。她一时兴起，开始收集这些看起来差不多的水果贴纸，把这当作是吃水果时的额外奖励，甚至在出国旅行时，也会来到当地的水果市场找寻不一样的水果贴纸。\n\n随着收集的贴纸越来越多，凯莉决定建立一座“线上水果贴纸博物馆”。\n\n目前，凯莉已经发布了大约1000张形形色色的水果贴纸，坐拥5万多粉丝。\n\n不过，话说回来了，为什么水果上要弄个贴纸啊？\n\n有编制的水果\n\n2009年，美国苹果行业的传奇人物汤姆·麦森（Tom Mathison）逝世。根据讣告我们得以知晓，麦森是第一个在苹果上贴标签的人。\n\n\n麦森家族拥有一片果园，主要种植苹果。到汤姆接手果园的时候，他试图扩大家族业务，成立了一家公司专门做水果包装生意。\n\n给水果贴上标签，正是他想到的营销策略。\n\n最开始，汤姆只是在苹果的贴纸上印农场和公司名字，以此提升知名度；后来，他又在贴纸上加了瓢虫的图案，表明苹果产自有机农场。\n\n\n\n一张小小的贴纸能蕴含这么多信息，这种巧妙的营销不仅让汤姆的生意获得了成功，也引起了国际农产品标准联合会的注意。\n\n1990年，受汤姆·麦森的启发，国际农产品标准联合会决定推广这种贴纸，并且要在贴纸上展示更多的信息。从此，水果、蔬菜上的小贴纸除了各色各样的图案外，还多了一串数字：PLU码（Price look-up codes）。\n\nPLU码并非强制使用，而是自愿申请使用的。它通常由4到5个数字组成，像我们的身份证号码一样，每一串数字代表不同的商品、品种、种植方法和尺寸等信息。\n\n?4位数的代码，表示果蔬使用传统方法种植，使用过农药、化肥，通常价格亲民；\n\n?5位数、以9开头的代码，表示果蔬是有机种植的，没有使用过人工合成的农药、化肥等；\n\n?5位数、以8开头的代码，曾经代表转基因作物（但没什么公司愿意使用，目前作为普通编号）。\n\n不得不说，全球通用的PLU码给大型跨国水果经销商带来了很多方便：方便区分库存里看似差不多、实则差很多的水果；在物流和销售环节提高效率；增加消费者的品牌认可度；多少提升一点溢价……\n\n而对于讲究生活品质的消费者来说，PLU码也是通过消费获得自我认同的方式。你买的水果、蔬菜上要是有这样的贴纸，可以上官方网站输入编码，输入后4位数字就行（官方网站戳这里），查查它是什么成分。\n\n图片\n\n官方网站查询编码4656，答案是中国产的秋葵丨网站截图\n\n当然，也有些水果蔬菜，并没有PLU码，只是贴了个好看的标签而已。\n\n别贴了，求求别贴了\n\n经销商、部分消费者，以及凯莉，都喜欢果蔬上的贴纸，自然也有人恨水果贴纸。\n\n最常见的恨，来源于麻烦。\n\n香蕉、猕猴桃之类本来就要剥皮吃的水果，你在上面贴个贴纸并无影响；但在苹果、梨、桃这种水果上贴贴纸，就显得有点不安好心了：贴纸撕不干净，麻烦！只能削皮再吃，麻烦！\n\n更深层的恨，来源于更大的麻烦。\n\n传统的PLU码贴纸通常以塑料为主，本身就不可降解；如果贴纸和果皮等厨余垃圾混在一起，对于垃圾处理行业来说，就是一个钻心的麻烦。\n\n做堆肥的厂商也不喜欢PLU码贴纸：一方面是因为贴纸虽然混迹厨余垃圾，但本身不可用作堆肥；另一方面，贴纸会在大量堆肥、施肥过程中产生微塑料，对环境、甚至人体健康可能有长期的负面影响。\n\n总结起来就是：PLU码好，塑料贴纸坏。\n\n人们也在想着办法：\n\n有人研发了一种叫fruitwash的可溶解贴纸，用水洗水果时，这个贴纸就会融化变成水果清洗剂，一举两得，可惜成本比较高，目前还没有得到推广；\n\n图片\n\n可溶于水变成水果清洗剂的PLU码贴纸丨amron exptl\n\n还有公司正在测试使用激光蚀刻技术给硬皮水果直接刻上带有PLU码的标签，从而避免使用贴纸。\n\n图片\n\n带有激光蚀刻标签的牛油果丨Eosta\n\n或许以后水果贴纸会成为历史，这么说来，收集这些奇奇怪怪的贴纸还真能建造一个“历史博物馆”。\n\n另一个“苹果”的贴纸\n\n说了水果界的苹果贴纸，我们也来说说科技界的苹果贴纸。\n\n图片\n\n这玩意儿到底用来干啥的？丨作者供图\n\n以往购买苹果公司的产品时，盒子里都会附赠一个苹果公司Logo贴纸。\n\n最初这个贴纸出现在1977年，苹果当时发售了新一代电脑Apple II。苹果附赠贴纸的目的也很简单：营销。当时的苹果作为一家新兴公司，希望你在各种地方贴上这个贴纸。\n\n而在今年发售的iPad Pro与iPad Air包装中，你不会再找到这张贴纸了。\n\n苹果官方并没有给出取消贴纸的解释，但伴随着苹果一直强调“碳中和”产品，不难猜测这一举动也是为了环保（省钱）。并且苹果已经成为了世界级的公司，大街上随处可见的iPhone、AirPods和Apple Watch已经成为了另一种“苹果贴纸”了。\n\n或许减少一片贴纸并不能给日益恶化的环境带来快速的改变，但注意到存在的问题，已经是迈出重大的一步了。\n\n参考文献\n\n[1]The Surprising, Overlooked Artistry of Fruit Stickers.https://www.atlasobscura.com/articles/surprising-artistry-of-fruit-stickers\n\n[2]StemiltFounder Tom Mathison Dies. https://produceprocessing.net/news/stemilt-founder-tom-mathison-dies/\n\nWhat is a PLU Code? https://www.ifpsglobal.com/plu-codes\n\n[3]There is nothing more annoying than stickers on fruit. https://medium.com/@jakewaddell/there-is-nothing-more-annoying-than-stickers-on-fruit-2c3e5f09c383\n\n[4]PRODUCE STICKERS: A SMALL BUT MIGHTY PROBLEM. https://sustainablepackaging.org/2021/05/05/produce-stickers-a-small-but-mighty-problem/\n\n[5]Dissolving food stickers also wash your fruit. https://newatlas.com/dissolving-food-stickers-wash-fruit/20392/\n\n[6]France: French legislation threatens millions of dollars of US fruit and vegetable exports. https://fas.usda.gov/data/france-french-legislation-threatens-millions-dollars-us-fruit-and-vegetable-exports\n\n[7]Why Do Apple Products Always Have Stickers In Their Packaging. https://www.luxury-paper-box.com/blog/why-do-apple-products-always-have-stickers-in-their-packaging.html\n\n作者：普拉斯G\n\n编辑：李小葵','2024-06-27 02:27:47','文化生活'),(16,5,'asfafa','dsgvaga','2024-06-27 08:25:34','文化生活'),(19,8,'asf','asfjiaoidfha','2024-06-27 09:54:43','科学文化');
/*!40000 ALTER TABLE `articles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `articletype`
--

DROP TABLE IF EXISTS `articletype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `articletype` (
  `type` varchar(10) NOT NULL,
  PRIMARY KEY (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `articletype`
--

LOCK TABLES `articletype` WRITE;
/*!40000 ALTER TABLE `articletype` DISABLE KEYS */;
INSERT INTO `articletype` VALUES ('文化生活'),('生活小窍门'),('科学文化'),('科技生活'),('管理员通知');
/*!40000 ALTER TABLE `articletype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorite`
--

DROP TABLE IF EXISTS `favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite` (
  `userID` int NOT NULL,
  `articleID` int NOT NULL,
  PRIMARY KEY (`userID`,`articleID`),
  KEY `articleID_idx` (`articleID`),
  CONSTRAINT `favorite_articleID` FOREIGN KEY (`articleID`) REFERENCES `articles` (`articleID`) ON DELETE CASCADE,
  CONSTRAINT `favorite_userID` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorite`
--

LOCK TABLES `favorite` WRITE;
/*!40000 ALTER TABLE `favorite` DISABLE KEYS */;
/*!40000 ALTER TABLE `favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `articleID` int NOT NULL,
  `userID` int NOT NULL,
  `review` varchar(200) NOT NULL,
  `createTime` timestamp NOT NULL,
  `likeNum` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`articleID`,`createTime`),
  KEY `review_articleID_idx` (`articleID`),
  KEY `review_userID_idx` (`userID`),
  CONSTRAINT `review_articleID` FOREIGN KEY (`articleID`) REFERENCES `articles` (`articleID`) ON DELETE CASCADE,
  CONSTRAINT `review_userID` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (1,9,'ge','2024-03-13 16:00:00',0),(1,2,'asfawasfa','2024-03-14 16:00:00',0),(1,5,'wrg','2024-03-16 16:00:00',0),(1,3,'er','2024-03-18 16:00:00',0),(2,5,'afsaw','2024-03-14 16:00:00',0),(2,8,'fg','2024-03-15 16:00:00',0),(3,7,'h','2024-03-11 16:00:00',0),(3,2,'faef','2024-03-14 21:00:00',0),(4,4,'fgtdh','2024-03-14 20:00:00',0),(5,5,'fgrgg','2024-03-14 19:00:00',0),(6,2,'fsd','2024-03-14 18:00:00',0),(9,4,'wr','2024-03-15 07:00:00',0);
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `userID` int NOT NULL AUTO_INCREMENT,
  `userName` varchar(10) NOT NULL,
  `createData` date NOT NULL,
  `password` varchar(15) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `userType` varchar(10) NOT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `userName_UNIQUE` (`userName`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'小红','2023-10-03','12346','56215','用户'),(2,'小紫','2025-03-06','12346','6225','用户'),(3,'小蓝','2023-02-24','123456','123456','用户'),(4,'咲','2024-06-15','123456','123456','用户'),(5,'猪猪侠','2023-02-15','123456','123456','用户'),(6,'望','2020-10-02','123456','123456','用户'),(7,'小町','2016-12-25','123456','123456','管理员'),(8,'小春','2022-10-23','123456','123456','管理员'),(9,'花凛','2021-05-13','123456','123456','用户'),(10,'Ir','2024-06-26','123456',NULL,'用户'),(11,'Fe50C','2024-06-26','123456',NULL,'管理员'),(12,'aaaaa','2024-06-27','123456','aaaa.a','用户'),(13,'aaa','2024-06-27','123456','asd','用户'),(19,'asfhas','2024-06-27','156259','51959@168.com','用户'),(20,'fasfwa','2024-06-27','4534538','51959@168.com','用户'),(21,'wafaf','2024-06-27','23453','51959@168.com','用户'),(22,'wfasf','2024-06-27','53843','51959@168.com','用户'),(23,'fawf','2024-06-27','535','51959@168.com','用户'),(24,'453453','2024-06-27','456453','76786','用户');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'sciences'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-28 11:14:36
