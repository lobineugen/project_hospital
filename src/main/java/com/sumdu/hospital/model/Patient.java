package com.sumdu.hospital.model;

import java.util.Date;

public class Patient {
    private int patientID;
    private String fullName;
    private String passportID;
    private Date dateOfBirth;
    private String addressType;
    private String address;
    private String phoneNumber;
    private String diagnosisMain;
    private String workPlace;

    public Patient(int patientID, String fullName, String passportID, Date dateOfBirth) {
        this.patientID = patientID;
        this.fullName = fullName;
        this.passportID = passportID;
        this.dateOfBirth = dateOfBirth;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDiagnosisMain() {
        return diagnosisMain;
    }

    public void setDiagnosisMain(String diagnosisMain) {
        this.diagnosisMain = diagnosisMain;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }
}
