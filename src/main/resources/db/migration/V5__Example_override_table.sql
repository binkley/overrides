CREATE TABLE OVERRIDES.p_Example_overrides (
  id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  example_id INTEGER FOREIGN KEY REFERENCES OVERRIDES.p_Example(id),
  name VARCHAR(99)
);

ALTER VIEW OVERRIDES.Example AS
SELECT e.id, COALESCE(o.name, e.name) name
FROM OVERRIDES.p_Example e
LEFT JOIN OVERRIDES.p_Example_overrides o
ON e.id = o.example_id;
