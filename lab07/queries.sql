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



