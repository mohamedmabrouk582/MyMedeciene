package com.example.mohamed.mymedeciene.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 23/01/2018.  time :21:53
 */

public class FullDrug implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.drug, flags);
        dest.writeParcelable(this.pharmacy, flags);
    }

    protected FullDrug(Parcel in) {
        this.drug = in.readParcelable(Drug.class.getClassLoader());
        this.pharmacy = in.readParcelable(Pharmacy.class.getClassLoader());
    }

    public static final Parcelable.Creator<FullDrug> CREATOR = new Parcelable.Creator<FullDrug>() {
        @Override
        public FullDrug createFromParcel(Parcel source) {
            return new FullDrug(source);
        }

        @Override
        public FullDrug[] newArray(int size) {
            return new FullDrug[size];
        }
    };
}
