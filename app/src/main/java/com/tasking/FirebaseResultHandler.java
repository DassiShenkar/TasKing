package com.tasking;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by Seymore on 3/21/2016.
 */
public class FirebaseResultHandler implements Firebase.ValueResultHandler<Map<String, Object>>{

    private String status;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void onSuccess(Map<String, Object> result) {
        this.setUid(result.get("uid").toString());
        this.setStatus("OK");
    }

    @Override
    public void onError(FirebaseError firebaseError) {
        this.setStatus(firebaseError.getMessage());
    }

}
