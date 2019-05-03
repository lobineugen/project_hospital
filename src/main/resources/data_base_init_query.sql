CREATE TABLE IF NOT EXISTS sm_patients (
  patientID     INTEGER PRIMARY KEY,
  fullName      TEXT,
  passportID    TEXT,
  dateOfBirth   DATE,
  addressType   TEXT,
  address       TEXT,
  phoneNumber   TEXT,
  diagnosisMain TEXT,
  workPlace     TEXT
);
CREATE INDEX sm_patients_id_index
  ON sm_patients (patientID);

