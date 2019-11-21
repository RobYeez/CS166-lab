SELECT COUNT(*)
FROM part_nyc P
WHERE P.on_hand > 70;

SELECT SUM(
  (SELECT COUNT(N.on_hand)
  FROM part_nyc N, color C
  WHERE N.color = C.color_id AND C.color_name = 'Red')
  +
  (SELECT COUNT(S.on_hand)
  FROM part_sfo S, color C
  WHERE S.color = C.color_id AND C.color_name = 'Red')
);

SELECT S.supplier_id, S.supplier_name
FROM supplier S
WHERE
(SELECT SUM(NYC.on_hand)
FROM part_nyc NYC
WHERE S.supplier_id = NYC.supplier)
>
(SELECT SUM(SFO.on_hand)
FROM part_sfo SFO
WHERE S.supplier_id = SFO.supplier)
ORDER BY S.supplier_id;

SELECT DISTINCT S.supplier_id, S.supplier_name
FROM supplier S, part_nyc P
WHERE S.supplier_id = P.supplier AND P.part_number IN
(SELECT P1.part_number
FROM supplier S, part_nyc P1
WHERE S.supplier_id = P1.supplier
EXCEPT
SELECT SFO.part_number
FROM supplier S, part_sfo SFO
WHERE S.supplier_id = SFO.supplier)
ORDER BY S.supplier_id;

UPDATE part_nyc 
SET on_hand = on_hand - 10
WHERE on_hand >= 10;

DELETE FROM part_nyc 
WHERE on_hand < 30;

-- test index
SELECT COUNT(*)
FROM  part_nyc WITH(INDEX(part_nyc_on_hand_index P))
WHERE P > 70;
