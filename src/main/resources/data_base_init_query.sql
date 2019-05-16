-- tables
-- Table: sm_patients
CREATE TABLE IF NOT EXISTS sm_patients
(
  patientID         INTEGER NOT NULL
    CONSTRAINT sm_patients_pk PRIMARY KEY,
  fullName          TEXT,
  passportID        TEXT,
  dateOfBirth       DATE,
  addressType       TEXT,
  address           TEXT,
  phoneNumber       TEXT,
  workPlace         TEXT,
  pvtStart          date,
  repeatPvtStart    date,
  pvtEnd            date,
  repeatPvtEnd      date,
  allergicReactions text,
  ogkSurvey         text
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

-- Table: sm_expert_consultations
CREATE TABLE IF NOT EXISTS sm_expert_consultations
(
  consID     INTEGER NOT NULL
    CONSTRAINT sm_expert_consultations_pk PRIMARY KEY,
  cardID     INTEGER NOT NULL,
  date       DATE,
  doctor     TEXT,
  conclusion TEXT,
  CONSTRAINT sm_expert_consultations FOREIGN KEY (cardID)
    REFERENCES sm_cards (cardID)
    ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS sm_expert_consultations_idx_1
  ON sm_expert_consultations (consID ASC);

CREATE TABLE sequence
(
  id INTEGER
);
INSERT INTO sequence
VALUES (1);