CREATE TABLE OVERRIDES.p_Example_overrides (
  id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  example_id INTEGER FOREIGN KEY REFERENCES OVERRIDES.p_Examples(id),
  name VARCHAR(99)
);

ALTER VIEW OVERRIDES.Examples AS
SELECT e.id, COALESCE(o.name, e.name) name
FROM OVERRIDES.p_Examples e
LEFT JOIN OVERRIDES.p_Example_overrides o
ON e.id = o.example_id;
