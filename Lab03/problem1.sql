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

