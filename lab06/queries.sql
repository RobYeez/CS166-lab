SELECT COUNT(*), S.sname
FROM Suppliers S, Parts P, Catalog C
WHERE P.pid = C.pid AND S.sid = C.sid
GROUP BY S.sid;

SELECT COUNT(*),S.sname
FROM Suppliers S, Parts P, Catalog C
WHERE P.pid = C.pid AND S.sid = C.sid
GROUP BY S.sid
HAVING COUNT(*) > 2;

SELECT S.sname, COUNT(*)
FROM Suppliers S, Parts P, Catalog C
WHERE P.pid = C.pid AND S.sid = C.sid AND S.sid IN
(SELECT S.sid
 FROM Suppliers S, Parts P, Catalog C
 WHERE P.pid = C.pid AND S.sid = C.sid AND P.color = 'Green'
 EXCEPT
 SELECT S.sid 
 FROM Suppliers S, Parts P, Catalog C
 WHERE P.pid = C.pid AND S.sid = C.sid AND P.color != 'Green'
)GROUP BY S.sid;

SELECT S.sname, MAX(C.cost)
FROM Suppliers S, Parts P, Catalog C
WHERE P.pid = C.pid AND S.sid = C.sid AND S.sid IN
(SELECT S.sid
 FROM Suppliers S, Parts P, Catalog C
 WHERE P.pid = C.pid AND S.sid = C.sid AND P.color = 'Green'
 INTERSECT
 SELECT S.sid
 FROM Suppliers S, Parts P, Catalog C
 WHERE P.pid = C.pid AND S.sid = C.sid AND P.color = 'Red'
)GROUP BY S.sid;

