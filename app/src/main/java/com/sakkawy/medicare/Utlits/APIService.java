package com.sakkawy.medicare.Utlits;

import com.sakkawy.medicare.Notifications.MyResponse;
import com.sakkawy.medicare.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAtbqeEcg:APA91bHiAsQrczalxCF2X5cSatmAm-fEhC_pyz8jibd5oGWYn-9-MSRhSO3TS56By8CgqcRp1zutpzeWWWY-rRZy4gXH96LGxotgxBc-1gbTY1lbCWIgpwLZbU0n4awTnrQUjr8edZnn"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
