CREATE TABLE IF NOT EXISTS sm_patients (
  id             TEXT PRIMARY KEY,
  full_name      TEXT,
  age            INTEGER,
  address        TEXT,
  diagnosis_main TEXT
);