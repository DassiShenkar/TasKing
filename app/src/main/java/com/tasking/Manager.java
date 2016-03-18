package com.tasking;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class Manager extends Employee{

    public Manager() {
    }

    public Manager(final String userName, String password) {
        Firebase ref = new Firebase("https://tasking-android.firebaseio.com/");
        ref.createUser(userName, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                String uid = result.get("uid").toString();
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                Manager.this.setUserName(userName);
                Manager.this.setPassword(userName);
                Manager.this.setUid(uid);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }
    public Manager(String userName, String password, int isManager,String uid) {
        super(userName, password, isManager);

    }

}