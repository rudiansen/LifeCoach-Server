CREATE TABLE Person ( 
    idPerson  INTEGER PRIMARY KEY AUTOINCREMENT,
    name      TEXT  DEFAULT 'NULL',
    lastname  TEXT  DEFAULT 'NULL',
    birthdate TEXT  DEFAULT 'NULL',
    email     TEXT,
    username  TEXT   DEFAULT 'NULL'
);
INSERT INTO `Person` (idPerson,name,lastname,birthdate,email,username) VALUES (1,'Frank','Lampard','1978-06-20','frank@gmail.com','frank');
INSERT INTO `Person` (idPerson,name,lastname,birthdate,email,username) VALUES (2,'John','Terry','1980-07-12','terry@gmail.com','terry');
INSERT INTO `Person` (idPerson,name,lastname,birthdate,email,username) VALUES (3,'Ashley','Cole','1980-12-20','cole@gmail.com','cole');
INSERT INTO `Person` (idPerson,name,lastname,birthdate,email,username) VALUES (4,'Rio','Ferdinand','1978-11-07','ferdinand@gmail.com','ferdinand');
INSERT INTO `Person` (idPerson,name,lastname,birthdate,email,username) VALUES (5,'Joe','Hart','1987-04-19','hart@gmail.com','hart');
INSERT INTO `Person` (idPerson,name,lastname,birthdate,email,username) VALUES (6,'Steven','Gerrard','1980-05-30','gerrard@gmail.com','gerrard');
INSERT INTO `Person` (idPerson,name,lastname,birthdate,email,username) VALUES (7,'Raheem','Sterling','1994-12-08','sterling@gmail.com','sterling');
INSERT INTO `Person` (idPerson,name,lastname,birthdate,email,username) VALUES (8,'Adam','Lallana','1988-05-10','lallana@gmail.com','lallana');
INSERT INTO `Person` (idPerson,name,lastname,birthdate,email,username) VALUES (9,'Wayne','Rooney','1985-24-10','rooney@gmail.com','rooney');
INSERT INTO `Person` (idPerson,name,lastname,birthdate,email,username) VALUES (10,'Michael','Carrick','1981-07-28','carrick@gmail.com','carrick');
CREATE TABLE MeasureDefinition ( 
    idMeasureDef INTEGER PRIMARY KEY AUTOINCREMENT,
    measureName  TEXT  DEFAULT 'NULL',
    measureType  TEXT  DEFAULT 'NULL'
);
INSERT INTO `MeasureDefinition` (idMeasureDef,measureName,measureType) VALUES (1,'weight','double');
INSERT INTO `MeasureDefinition` (idMeasureDef,measureName,measureType) VALUES (2,'height','double');
INSERT INTO `MeasureDefinition` (idMeasureDef,measureName,measureType) VALUES (3,'steps','integer');
INSERT INTO `MeasureDefinition` (idMeasureDef,measureName,measureType) VALUES (4,'blood pressure','double');
INSERT INTO `MeasureDefinition` (idMeasureDef,measureName,measureType) VALUES (5,'heart rate','integer');
INSERT INTO `MeasureDefinition` (idMeasureDef,measureName,measureType) VALUES (6,'bmi','double');
CREATE TABLE LifeStatus ( 
    idMeasure  INTEGER PRIMARY KEY AUTOINCREMENT,
    idMeasureDef INTEGER       DEFAULT 'NULL',
    idPerson     INTEGER       DEFAULT 'NULL',
	dateRegistered  TEXT       DEFAULT 'NULL',
    value        TEXT,
    FOREIGN KEY ( idMeasureDef ) REFERENCES MeasureDefinition ( idMeasureDef ) ON DELETE NO ACTION
                                                                                   ON UPDATE NO ACTION,
    CONSTRAINT 'fk_LifeStatus_Person1' FOREIGN KEY ( idPerson ) REFERENCES Person ( idPerson ) ON DELETE NO ACTION
                                                                                                   ON UPDATE NO ACTION 
);
INSERT INTO `LifeStatus` (idMeasure,idMeasureDef,idPerson,dateRegistered,value) VALUES (1,1,1,'2014-12-27','72.3');
INSERT INTO `LifeStatus` (idMeasure,idMeasureDef,idPerson,dateRegistered,value) VALUES (2,2,1,'2014-12-27','1.86');
INSERT INTO `LifeStatus` (idMeasure,idMeasureDef,idPerson,dateRegistered,value) VALUES (3,1,2,'2014-12-27','85');
INSERT INTO `LifeStatus` (idMeasure,idMeasureDef,idPerson,dateRegistered,value) VALUES (4,2,2,'2014-12-27','1.85');
CREATE TABLE `HealthMeasureHistory` (
	`idMeasureHistory`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`idMeasureDef`	NUMBER(10),
	`idPerson`	INTEGER DEFAULT 'NULL',
	`value`	TEXT,
	`timestamp`	TEXT DEFAULT 'NULL',	
	FOREIGN KEY(`idPerson`) REFERENCES Person ( idPerson ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	FOREIGN KEY(`idMeasureDef`) REFERENCES MeasureDefinition ( idMeasureDef ) ON DELETE NO ACTION ON UPDATE NO ACTION
);
INSERT INTO `HealthMeasureHistory` (idMeasureHistory,idPerson,value,timestamp,idMeasureDef) VALUES (1,1,'83','2012-12-27',1);
INSERT INTO `HealthMeasureHistory` (idMeasureHistory,idPerson,value,timestamp,idMeasureDef) VALUES (2,1,'80','2013-02-26',1);
INSERT INTO `HealthMeasureHistory` (idMeasureHistory,idPerson,value,timestamp,idMeasureDef) VALUES (3,1,'75','2013-06-29',1);
CREATE INDEX LifeStatus_fk_LifeStatus_Person1_idx ON LifeStatus ( 
    idPerson 
);
CREATE INDEX LifeStatus_fk_HealthMeasure_MeasureDefinition_idx ON LifeStatus ( 
    idMeasureDef 
);
COMMIT;
