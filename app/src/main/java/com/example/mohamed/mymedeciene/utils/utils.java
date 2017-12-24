package com.example.mohamed.mymedeciene.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 19/11/2017.  time :22:52
 */

public class utils {

    public static boolean isEmailValid(String email){
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern= Pattern.compile(EMAIL_PATTERN);
        matcher=pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidMobile(String phone) {
        boolean check=false;
        if (!TextUtils.isEmpty(phone)){
            if (!Pattern.matches("[a-zA-Z]+", phone)) {
                if (phone.length() < 6 || phone.length() > 13) {
                    // if(phone.length() != 10) {
                    check = false;
                } else {
                    check = true;
                }
            } else {
                check = false;
            }
        }
        return check;
    }
}
