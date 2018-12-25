ALTER VIEW OVERRIDES.Examples AS
SELECT e.id,
       COALESCE(o.name, e.name) name,
       o.example_id
FROM OVERRIDES.p_Examples e
       LEFT JOIN OVERRIDES.p_Example_overrides o
                 ON e.id = o.example_id;
