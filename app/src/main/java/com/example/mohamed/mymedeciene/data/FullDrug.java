package com.example.mohamed.mymedeciene.data;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 23/01/2018.  time :21:53
 */

public class FullDrug {
    private Drug drug;
    private Pharmacy pharmacy;

    @Override
    public String toString() {
        return "FullDrug{" +
                "drug=" + drug.toString() +
                ", pharmacy=" + pharmacy .toString()+
                '}';
    }

    public FullDrug(Drug drug, Pharmacy pharmacy) {
        this.drug = drug;
        this.pharmacy = pharmacy;
    }

    public FullDrug() {
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public Drug getDrug() {
        return drug;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }
}
