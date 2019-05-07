-- tables
-- Table: sm_patients
CREATE TABLE IF NOT EXISTS sm_patients
(
  patientID   INTEGER NOT NULL
    CONSTRAINT sm_patients_pk PRIMARY KEY,
  fullName    TEXT,
  passportID  TEXT,
  dateOfBirth DATE,
  addressType TEXT,
  address     TEXT,
  phoneNumber TEXT,
  workPlace   TEXT
);

CREATE INDEX IF NOT EXISTS patientID_index
  ON sm_patients (patientID ASC);

-- Table: sm_cards
CREATE TABLE IF NOT EXISTS sm_cards
(
  cardID        INTEGER NOT NULL
    CONSTRAINT sm_cards_pk PRIMARY KEY,
  patientID     INTEGER NOT NULL,
  cardNumber    INTEGER,
  week          TEXT,
  dateIn        DATE,
  dateOut       DATE,
  mainDiagnosis TEXT,
  complication  TEXT,
  pvt           TEXT,
  concomitant   TEXT,
  CONSTRAINT sm_cards_sm_patients FOREIGN KEY (patientID)
    REFERENCES sm_patients (patientID)
    ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS sm_cards_idx_1
  ON sm_cards (cardID ASC);

-- End of file.

CREATE TABLE sequence
(
  id INTEGER
);
INSERT INTO sequence
VALUES (1);