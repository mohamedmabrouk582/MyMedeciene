package com.example.mohamed.mymedeciene.data;

/**
 * Created by Mohammad mabrouk
 * 0201152644726
 * on 4/19/2018.  time :00:09
 */
public class Comment {
    private String userId;
    private String content;
    private Pharmacy pharmacy;

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
