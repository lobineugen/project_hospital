package com.sumdu.hospital.model;

import java.sql.Date;
import java.util.List;

public class Analysis {
    private int cardID;
    private int analysisId;
    private String type;
    private Date date;
    private List<AnalysisParameter> parameters;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<AnalysisParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<AnalysisParameter> parameters) {
        this.parameters = parameters;
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public int getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(int analysisId) {
        this.analysisId = analysisId;
    }
}
