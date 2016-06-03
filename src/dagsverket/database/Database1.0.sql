DROP TABLE product;
DROP TABLE taskcar;
DROP TABLE car;
DROP TABLE persontask;
--DROP TABLE taskequipment;
DROP TABLE equipment;
DROP TABLE task;
DROP TABLE contactInfo;
DROP TABLE employer;
DROP TABLE worker;
DROP TABLE supervisor;
DROP TABLE person;
DROP TABLE login;
---------------------------------------------

CREATE TABLE person(
person_number INT NOT NULL AUTO_INCREMENT,
first_name VARCHAR(20),
last_name VARCHAR(20),
CONSTRAINT person_pk PRIMARY KEY(person_number)
)ENGINE=INNODB;

CREATE TABLE employer(
employer_number INT NOT NULL AUTO_INCREMENT,
business_name VARCHAR(20),
PRIMARY KEY(employer_number))ENGINE=INNODB;

CREATE TABLE login(
  username VARCHAR(30),
  password VARCHAR(30),
  CONSTRAINT login_pk PRIMARY KEY(username))ENGINE=INNODB;

CREATE TABLE contactInfo(
  contact_id INT NOT NULL AUTO_INCREMENT,
  employer_number INT NOT NULL,
  type VARCHAR (20),
  number VARCHAR (16),
  CONSTRAINT PRIMARY KEY (contact_id),
  foreign key (employer_number) REFERENCES employer(employer_number) ON DELETE CASCADE ON UPDATE CASCADE
  )ENGINE=INNODB;

CREATE TABLE worker(
person_number INT NOT NULL,
can_work_outside BOOLEAN,
CONSTRAINT worker_pk PRIMARY KEY(person_number),
FOREIGN KEY(person_number)  REFERENCES person(person_number) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=INNODB;

CREATE TABLE supervisor(
person_number INT NOT NULL,
phone_number VARCHAR(20),
email_adr VARCHAR(50),
CONSTRAINT supervisor_pk PRIMARY KEY(person_number),
FOREIGN KEY(person_number) REFERENCES person(person_number) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=INNODB;

CREATE TABLE task(
task_name VARCHAR(20),
task_number INT NOT NULL AUTO_INCREMENT,
supervisor_number INT,
employer_number INT NOT NULL,
description VARCHAR(355),
is_outside BOOLEAN,
befaring VARCHAR(40),
cost FLOAT,
startdate DATE,
completed BOOLEAN,
adress VARCHAR(100),
zipcode VARCHAR(4),
post VARCHAR(20),
username VARCHAR(30),
regDate DATE,
paid BOOLEAN,
PRIMARY KEY(task_number),
FOREIGN KEY(employer_number)  REFERENCES employer(employer_number) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=INNODB;

CREATE TABLE car(
car_number INT AUTO_INCREMENT,
plate_number VARCHAR(10),
colour VARCHAR(20),
number_of_seats INT,
CONSTRAINT car_pk PRIMARY KEY(car_number))ENGINE=INNODB;

CREATE TABLE equipment(
equipment_number INT AUTO_INCREMENT,
task_number INT,
type_name VARCHAR(30),
CONSTRAINT equipment_pk PRIMARY KEY(equipment_number),
FOREIGN KEY(task_number) REFERENCES task(task_number) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=INNODB;

CREATE TABLE product(
product_number INT AUTO_INCREMENT,
p_name VARCHAR(50),
stock INT,
cost DOUBLE,
CONSTRAINT product_pk PRIMARY KEY(product_number))ENGINE=INNODB;



CREATE TABLE persontask(
person_number INT NOT NULL,
task_number INT NOT NULL,
CONSTRAINT persontask_pk PRIMARY KEY(person_number,task_number),
FOREIGN KEY(person_number)  REFERENCES person(person_number) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(task_number)  REFERENCES task(task_number) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=INNODB;

CREATE TABLE taskcar(
car_number INT AUTO_INCREMENT,
plate_number VARCHAR(10) NOT NULL,
task_number INT NOT NULL,
CONSTRAINT taskcar_pk PRIMARY KEY(car_number,task_number),
FOREIGN KEY(car_number)  REFERENCES car(car_number) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(task_number)  REFERENCES task(task_number) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=INNODB;

DROP VIEW workers;
DROP VIEW jobslast30daysview;
DROP VIEW lastdateworkedview;
DROP VIEW jobsnext30daysview;

CREATE VIEW workers AS SELECT worker.person_number, person.first_name, person.last_name, worker.can_work_outside, persontask.task_number, task.startdate
FROM person NATURAL JOIN worker LEFT JOIN persontask ON (worker.person_number = persontask.person_number) LEFT JOIN task ON(persontask.task_number = task.task_number);

CREATE VIEW jobslast30daysview AS SELECT worker.person_number, Count(task.task_number) AS jobslast30days 
FROM person NATURAL JOIN worker LEFT JOIN persontask ON (worker.person_number = persontask.person_number)
LEFT JOIN task ON(persontask.task_number = task.task_number) WHERE (TO_DAYS(CURRENT_DATE)-30 < TO_DAYS(startdate)) OR startdate is null 
GROUP BY first_name;

CREATE VIEW jobsnext30daysview AS SELECT worker.person_number, Count(task.task_number) AS jobsnext30days 
FROM person NATURAL JOIN worker LEFT JOIN persontask ON (worker.person_number = persontask.person_number)
LEFT JOIN task ON(persontask.task_number = task.task_number) WHERE (((TO_DAYS(CURRENT_DATE) > TO_DAYS(startdate)-30)) AND (TO_DAYS(CURRENT_DATE) < TO_DAYS(startdate))) OR startdate is null 
GROUP BY first_name;

CREATE VIEW lastdateworkedview AS SELECT worker.person_number, Max(startdate) AS lastdateworked
FROM person NATURAL JOIN worker LEFT JOIN persontask ON (worker.person_number = persontask.person_number)
LEFT JOIN task ON(persontask.task_number = task.task_number) WHERE TO_DAYS(CURRENT_DATE) > TO_DAYS(startdate)
GROUP BY first_name;