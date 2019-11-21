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
  WHERE S.color = C.color_id AND C.color_name = 'Green')
)
AS sum_red;

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
FROM supplier S, part_nyc NYC
WHERE S.supplier_id = NYC.supplier AND NYC.part_number IN
(SELECT NYC.part_number
FROM supplier S, part_nyc NYC
WHERE S.supplier_id = NYC.supplier
EXCEPT
SELECT SFO.part_number
FROM supplier S, part_sfo SFO
WHERE S.supplier_id = SFO.supplier)
ORDER BY S.supplier_id;

UPDATE part_nyc P
SET P.on_hand = P.on_hand - 10
WHERE P.on_hand >= 10;

DELETE FROM part_nyc P
WHERE P.on_hand < 30;
