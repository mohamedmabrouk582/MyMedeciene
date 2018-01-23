package com.example.mohamed.mymedeciene.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :18:46
 */

@SuppressWarnings("unused")
public class Drug implements Parcelable {
    private String name;
    private String type;
    private String price;
    private String quantity;
    private String img;
    private String phKey;



    public Drug() {
    }

    public Drug(String name, String type, String price, String quantity, String img, String phKey) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.img = img;
        this.phKey = phKey;
    }



    public String getPhKey() {
        return phKey;
    }

    public void setPhKey(String phKey) {
        this.phKey = phKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.price);
        dest.writeString(this.quantity);
        dest.writeString(this.img);
        dest.writeString(this.phKey);
    }

    protected Drug(Parcel in) {
        this.name = in.readString();
        this.type = in.readString();
        this.price = in.readString();
        this.quantity = in.readString();
        this.img = in.readString();
        this.phKey = in.readString();
    }

    public static final Creator<Drug> CREATOR = new Creator<Drug>() {
        @Override
        public Drug createFromParcel(Parcel source) {
            return new Drug(source);
        }

        @Override
        public Drug[] newArray(int size) {
            return new Drug[size];
        }
    };
}
