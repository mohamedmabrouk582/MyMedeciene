package com.example.mohamed.mymedeciene.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :18:04
 */

@SuppressWarnings("unused")
public class Pharmacy implements Parcelable {
    private String phName;
    private String phPhone;
    private String phImgURL;
    private String phLocation;
    private String latLang;

    @Override
    public String toString() {
        return "Pharmacy{" +
                "phName='" + phName + '\'' +
                ", phPhone='" + phPhone + '\'' +
                ", phImgURL='" + phImgURL + '\'' +
                ", phLocation='" + phLocation + '\'' +
                '}';
    }

    public Pharmacy(String phName, String phPhone, String phImgURL, String phLocation, String latLang) {
        this.phName = phName;
        this.phPhone = phPhone;
        this.phImgURL = phImgURL;
        this.phLocation = phLocation;
        this.latLang = latLang;
    }

    public String getLatLang() {
        return latLang;
    }

    public void setLatLang(String latLang) {
        this.latLang = latLang;
    }

    public Pharmacy() {
    }


    public String getPhName() {
        return phName;
    }

    public void setPhName(String phName) {
        this.phName = phName;
    }

    public String getPhPhone() {
        return phPhone;
    }

    public void setPhPhone(String phPhone) {
        this.phPhone = phPhone;
    }

    public String getPhImgURL() {
        return phImgURL;
    }

    public void setPhImgURL(String phImgURL) {
        this.phImgURL = phImgURL;
    }

    public String getPhLocation() {
        return phLocation;
    }

    public void setPhLocation(String phLocation) {
        this.phLocation = phLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.phName);
        dest.writeString(this.phPhone);
        dest.writeString(this.phImgURL);
        dest.writeString(this.phLocation);
        dest.writeString(this.latLang);
    }

    protected Pharmacy(Parcel in) {
        this.phName = in.readString();
        this.phPhone = in.readString();
        this.phImgURL = in.readString();
        this.phLocation = in.readString();
        this.latLang = in.readString();
    }

    public static final Creator<Pharmacy> CREATOR = new Creator<Pharmacy>() {
        @Override
        public Pharmacy createFromParcel(Parcel source) {
            return new Pharmacy(source);
        }

        @Override
        public Pharmacy[] newArray(int size) {
            return new Pharmacy[size];
        }
    };
}
