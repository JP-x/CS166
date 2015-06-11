DROP TABLE WORK_EXPR;
DROP TABLE EDUCATIONAL_DETAILS;
DROP TABLE MESSAGE;
DROP TABLE CONNECTION_USR;
DROP TABLE USR;


CREATE TABLE USR(
	userId text UNIQUE NOT NULL, 
	password varchar(30) NOT NULL,
	email text NOT NULL,
	name char(50),
	dateOfBirth date,
	Primary Key(userId));

CREATE TABLE WORK_EXPR(
	userId text NOT NULL, 
	company char(50) NOT NULL, 
	role char(50) NOT NULL,
	location char(50),
	startDate date,
	endDate date,
	PRIMARY KEY(userId,company,role,startDate));

CREATE TABLE EDUCATIONAL_DETAILS(
	userId text NOT NULL, 
	instituitionName char(50) NOT NULL, 
	major char(50) NOT NULL,
	degree char(50) NOT NULL,
	startdate date,
	enddate date,
	PRIMARY KEY(userId,major,degree));

CREATE TABLE MESSAGE(
	msgId integer UNIQUE NOT NULL, 
	senderId text NOT NULL,
	receiverId text NOT NULL,
	contents text NOT NULL,
	sendTime timestamp,
	deleteStatus integer,
	status char(30) NOT NULL,
	PRIMARY KEY(msgId));

CREATE TABLE CONNECTION_USR(
	userId text NOT NULL, 
	connectionId text NOT NULL, 
	status char(30) NOT NULL,
	PRIMARY KEY(userId,connectionId));
