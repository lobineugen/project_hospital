package com.sumdu.hospital.model;

public class Patient {
    private String id;
    private String fullName;
    private int age;
    private String address;
    private String diagnosisMain;

    public Patient(String id, String fullName, int age, String address, String diagnosisMain) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.address = address;
        this.diagnosisMain = diagnosisMain;
    }

    public Patient(String id, String fullName, int age) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDiagnosisMain() {
        return diagnosisMain;
    }

    public void setDiagnosisMain(String diagnosisMain) {
        this.diagnosisMain = diagnosisMain;
    }
}
