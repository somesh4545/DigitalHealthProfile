package com.somanibrothersservices.digitalhealthprofile;

import java.util.ArrayList;
import java.util.List;

public class DoctorModel {
    public String name , qual;
    public List<String> patients;
    public List<PatientModel> patientModelList;

    public DoctorModel(String name, String qual) {
        this.name = name;
        this.qual = qual;
        patients = new ArrayList<>();
        patientModelList = new ArrayList<>();
    }

    public List<PatientModel> getPatientModelList() {
        return patientModelList;
    }

    public void setPatientModelList(List<PatientModel> patientModelList) {
        this.patientModelList = patientModelList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQual() {
        return qual;
    }

    public void setQual(String qual) {
        this.qual = qual;
    }

    public List<String> getPatients() {
        return patients;
    }

    public void setPatients(List<String > patients) {
        this.patients = patients;
    }
}
