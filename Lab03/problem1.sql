DROP TABLE if EXISTS Professor
DROP TABLE if EXISTS Dept
DROP TABLE if EXISTS Project
DROP TABLE if EXISTS Graduate
DROP TABLE if EXISTS Work_Dept
DROP TABLE if EXISTS Work_In
DROP TABLE if EXISTS Work_Proj

CREATE TABLE Professor (
             ssn integer NOT NULL,
             name CHAR(32),
             age integer,
             rank integer,
             speciality CHAR(32),
             PRIMARY_KEY (ssn));

CREATE TABLE Dept (
             dno number NOT NULL,
             dname text,
             office text,
             runs number,
             PRIMARY_KEY (dno),
             FOREIGN_KEY(runs) REFERENCES Professor (ssn));

CREATE TABLE Project (
             pno number NOT NULL,
             sponsor text,
             start_date date,
             end_date date,
             budget number,
             manage number,
             PRIMARY_KEY (pno),
             FOREIGN_KEY(manage) REFERENCES Professor(ssn)););

CREATE TABLE Graduate (
             ssn integer NOT NULL,
             name text,
             age number,
             deg_pg text,
             major number,
             PRIMARY_KEY (ssn),
             FOREIGN_KEY(major) REFERENCES Dept(dno));

CREATE TABLE Work_Dept (
             time_pc number NOT NULL,
             ssn numeric(0,9) NOT NULL,
             dno number NOT NULL,
             PRIMARY_KEY ((ssn, dno)),
             FOREIGN_KEY(ssn) REFERENCES Professor(ssn),
             FOREIGN_KEY(dno) REFERENCES Dept(dno));

CREATE TABLE Work_In (
             PRIMARY_KEY ((ssn, pno)),
             FOREIGN_KEY(ssn) REFERENCES Professor(ssn),
             FOREIGN_KEY(pno) REFERENCES Project(pno));

CREATE TABLE Work_Proj (
             since date,
             supervise number,
             PRIMARY_KEY ((pno, ssn)),
             FOREIGN_KEY(pno) REFERENCES Project(pno),
             FOREIGN_KEY(ssn) REFERENCES Graduate(ssn),
             FOREIGN_KEY(supervise) REFERENCES Professor(ssn));

