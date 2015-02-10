
--
-- Dumping data for table `exchange`
--

INSERT INTO `exchange` (`ID`, `EXCHANGE`, `NAME`, `OPEN`, `CLOSE`, `TIMEZONE`) VALUES
('1', 'NASDAQ', 'NASDAQ Main', '09:30', '16:00', 'America/New_York'),
('2', 'NYSE', 'New York SE', '09:30', '16:00', 'America/New_York'),
('3', 'SMART', 'IB SMART', '09:30', '16:00', 'America/New_York'),
('4', 'IDEAL', 'IDEAL FX', '00:00', '23:59', 'America/New_York');

--
-- Dumping data for table `strategies`
--

INSERT INTO `strategies` (`ID`, `NAME`, `TYPE`, `SYMBOL`, `LOADVALUES`, `ENABLED`, `AMOUNT`, `SIMULATED`, `TIMESTAMP`) VALUES
('1', 'STOCK1#50-58', 'org.mot.core.esper.SampleAlgo', 'STOCK#1', 'avg1=50;avg2=58', 1, '1000', 1, '2015-01-14 14:25:51'),
('2', 'STOCK2#60-78', 'org.mot.core.esper.SampleAlgo', 'STOCK#2', 'avg1=60;avg2=78', 1, '1000', 1, '2015-01-14 14:25:51'),
('3', 'STOCK3#50-98', 'org.mot.core.esper.SampleAlgo', 'STOCK#3', 'avg1=50;avg2=98', 1, '1000', 1, '2015-01-14 14:25:51');

--
-- Dumping data for table `watchlist`
--

INSERT INTO `watchlist` (`ID`, `SYMBOL`, `CURRENCY`, `EXCHANGE`, `TYPE`, `NAME`, `EXECUTOR`, `ENABLED`) VALUES
('1', 'STOCK#1', 'USD', 'SMART', 'STK', 'Sample Stock #1', NULL, 1),
('2', 'STOCK#2', 'USD', 'SMART', 'STK', 'Sample Stock #2', NULL, 1),
('3', 'STOCK#3', 'USD', 'SMART', 'STK', 'Sample Stock #3', NULL, 1);

