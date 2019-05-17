package com.sumdu.hospital.model;

import java.sql.Date;

public class ExpertConsultation {
    private int consID;
    private Date date;
    private String doctor;
    private String conclusion;
    private int cardID;

    public ExpertConsultation() {
    }

    public ExpertConsultation(int consID) {
        this.consID = consID;
    }

    public ExpertConsultation(Date date, String doctor, String conclusion) {
        this.date = date;
        this.doctor = doctor;
        this.conclusion = conclusion;
    }

    public ExpertConsultation(int consID, Date date, String doctor, String conclusion) {
        this.consID = consID;
        this.date = date;
        this.doctor = doctor;
        this.conclusion = conclusion;
    }

    public int getConsID() {
        return consID;
    }

    public void setConsID(int consID) {
        this.consID = consID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    @Override
    public String toString() {
        return "ExpertConsultation{" +
                "consID=" + consID +
                ", date=" + date +
                ", doctor='" + doctor + '\'' +
                ", conclusion='" + conclusion + '\'' +
                ", cardID=" + cardID +
                '}';
    }
}
