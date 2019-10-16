-PROBLEM 1
DROP TABLE if EXISTS Professor
DROP TABLE if EXISTS Dept
DROP TABLE if EXISTS Project
DROP TABLE if EXISTS Graduate
DROP TABLE if EXISTS Work_Dept
DROP TABLE if EXISTS Work_In
DROP TABLE if EXISTS Work_Proj

CREATE TABLE Professor (
             ssn integer NOT NULL,
             name char(32),
             age integer,
             rank integer,
             speciality char(32),
             PRIMARY_KEY (ssn));

CREATE TABLE Dept (
             dno integer NOT NULL,
             dname char(32),
             office char(32),
             runs integer,
             PRIMARY_KEY (dno) NOT NULL,
             FOREIGN_KEY(runs) REFERENCES Professor (ssn));

CREATE TABLE Project (
             pno integer NOT NULL,
             sponsor char(32),
             start_date date,
             end_date date,
             budget integer,
             manage integer,
             PRIMARY_KEY (pno) NOT NULL,
             FOREIGN_KEY(manage) REFERENCES Professor(ssn));

CREATE TABLE Graduate (
             ssn integer NOT NULL,
             name char(32),
             age integer,
             deg_pg char(32),
             major integer,
             advise char(32),
             PRIMARY_KEY (ssn) NOT NULL,
             FOREIGN_KEY(major) REFERENCES Dept(dno))
             FOREIGN_KEY(advise) REFERENCES Graduate(ssn);

CREATE TABLE Work_Dept (
             time_pc integer NOT NULL,
             ssn integer NOT NULL,
             dno integer NOT NULL,
             PRIMARY_KEY ((ssn, dno)) NOT NULL,
             FOREIGN_KEY(ssn) REFERENCES Professor(ssn),
             FOREIGN_KEY(dno) REFERENCES Dept(dno));

CREATE TABLE Work_In (
             ssn integer NOT NULL,
             pno integer NOT NULL,
             PRIMARY_KEY ((ssn, pno)) NOT NULL,
             FOREIGN_KEY(ssn) REFERENCES Professor(ssn),
             FOREIGN_KEY(pno) REFERENCES Project(pno));

CREATE TABLE Work_Proj (
             since date,
             pno integer NOT NULL,
             ssn integer NOT NULL,
             supervise integer,
             PRIMARY_KEY ((pno, ssn)),
             FOREIGN_KEY(pno) REFERENCES Project(pno),
             FOREIGN_KEY(ssn) REFERENCES Graduate(ssn),
             FOREIGN_KEY(supervise) REFERENCES Professor(ssn));

-PROBLEM 2
DROP TABLE if EXISTS Album
DROP TABLE if EXISTS Songs
DROP TABLE if EXISTS Instrument
DROP TABLE if EXISTS Musicians
DROP TABLE if EXISTS Perform
DROP TABLE if EXISTS Producer
DROP TABLE if EXISTS Place
DROP TABLE if EXISTS Telephone
DROP TABLE if EXISTS Home
DROP TABLE if EXISTS Lives
DROP TABLE if EXISTS Plays

CREATE TABLE Album (
             albumIdentifier integer NOT NULL,
             copyrightDate date,
             speed integer,
             title char(32),
             producer char(32),
             PRIMARY_KEY(albumIdentifier),
             FOREIGN_KEY(producer) REFERENCES Musicians(ssn));

CREATE TABLE Songs (
             songID integer NOT NULL,
             title char(32),
             author char(32),
             appears char(32) NOT NULL,
             PRIMARY_KEY(songID),
             FOREIGN_KEY(appears) REFERENCES Album(albumIdentifier));

CREATE TABLE Instrument (
             instrID integer NOT NULL,
             key char(32),
             dname char(32),
             PRIMARY_KEY(instrID));

CREATE TABLE Musicians (
             ssn integer NOT NULL,
             name char(32),
             lives char(32),
             PRIMARY_KEY(ssn),
             FOREIGN_KEY(lives) REFERENCES Place(address));

CREATE TABLE Plays (
             ssn integer NOT NULL,
             instrID integer NOT NULL,
             PRIMARY_KEY((ssn, instrID)),
             FOREIGN_KEY(ssn) REFERENCES Musicians(ssn),
             FOREIGN_KEY(instrID) REFERENCES Instrument(instrID));

CREATE TABLE Perform (
             ssn integer NOT NULL,
             songID integer NOT NULL,
             PRIMARY_KEY ((ssn, songID)),
             FOREIGN_KEY(ssn) REFERENCES Musicians(ssn),
             FOREIGN_KEY(songID) REFERENCES Songs(songID));

CREATE TABLE Producer (
             ssn integer NOT NULL,
             albumIdentifier integer NOT NULL,
             PRIMARY_KEY((ssn, albumIdentifier)),
             FOREIGN_KEY(ssn) REFERENCES Musicians(ssn),
             FOREIGN_KEY(albumIdentifier) REFERENCES(albumIdentifier));

CREATE TABLE Appears (
             albumIdentifier integer NOT NULL,
             songID integer NOT NULL,
             PRIMARY_KEY((albumIdentifier, songID)),
             FOREIGN_KEY(albumIdentifier) REFERENCES Album(albumIdentifier),
             FOREIGN_KEY(songID) REFERENCES Song(songID));

CREATE TABLE Place (
             address char(32) NOT NULL,
             home integer,
             PRIMARY_KEY(address),
             FOREIGN_KEY(home) REFERENCES Telephone(phone_no));

CREATE TABLE Telephone (
             phone_no integer,
             home integer,
             FOREIGN_KEY(home) REFERENCES Place(address));

CREATE TABLE Home (
             lives char(32),
             phone_no integer NOT NULL,
             PRIMARY_KEY(phone_no),
             FOREIGN_KEY(phone_no) REFERENCES Telephone(phone_no));

CREATE TABLE Lives (
             ssn integer NOT NULL,
             address char(32) NOT NULL,
             PRIMARY_KEY((ssn, address)),
             FOREIGN_KEY(ssn) REFERENCES Musicians(ssn),
             FOREIGN_KEY(address) REFERENCES Place(address));
