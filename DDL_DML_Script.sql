--- Keresztes David
--- Kesmarki hazifeladat

----- DDL of Company1 table -----
CREATE TABLE Company1(
userid bigint NOT NULL,
username varchar(20),
ageOfEmployee bigint NOT NULL,
hasCar boolean,
birthday date
);

DROP TABLE Company1;


----- DML of Company1 table -----
SELECT * FROM Company1;
INSERT INTO Company1 values(154, 'Helga', 46, TRUE, '1977-04-23');
INSERT INTO Company1 values(155, 'Jena', 26, TRUE, '1995-12-24');
INSERT INTO Company1 values(156, 'Jasmine', 20, FALSE, '2001-11-01');
INSERT INTO Company1 values(157, 'Bernard', 40, TRUE, '1981-01-30');
UPDATE company1 SET username = 'Berry' WHERE username = 'Bernard';
DELETE FROM company1 WHERE userid = 157;


------------------------------------------------------------------------------------------------------


----- DDL of Company2 table -----
CREATE TABLE Company2(
userid bigint NOT NULL,
username varchar(20),
ageOfEmployee bigint NOT NULL,
hasCar boolean,
birthday date
);

DROP TABLE Company2;


----- DML of Company2 table -----
SELECT * FROM Company2;
INSERT INTO Company2 values(154, 'Dave', 71, TRUE, '1950-04-23');
INSERT INTO Company2 values(155, 'Christine', 26, TRUE, '1995-12-24');
INSERT INTO Company2 values(156, 'Arnold', 36, FALSE, '1985-11-01');
INSERT INTO Company2 values(157, 'Billy', 54, TRUE, '1967-01-30');

SELECT COUNT(username) AS NumberOfEmployee FROM Company2;
SELECT AVG(ageOfEmployee) AS AverageAge FROM Company2;
SELECT COUNT(hasCar) AS NumberOfCars FROM Company2;
SELECT COUNT(nullif(hasCar = false, true)) AS NumberOfCars FROM company2; 
SELECT username, current_date - birthday+1 AS hanynapos FROM Company2;
UPDATE company2 SET username = 'Dagobert' WHERE username = 'Dave';
DELETE FROM company2 WHERE userid = 157; 


------------------------------------------------------------------------------------------------------



