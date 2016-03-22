package com.tasking;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by Seymore on 3/21/2016.
 */
public class FirebaseAuthResultHandler implements Firebase.AuthResultHandler {

    private String status;
    private String uid;
    private boolean isManager;
    private boolean hasTeam;
    private boolean online = true;

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

    public boolean isManager() {
        return isManager;
    }

    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }

    public boolean isHasTeam() {
        return hasTeam;
    }

    public void setHasTeam(boolean hasTeam) {
        this.hasTeam = hasTeam;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public void onAuthenticated(AuthData authData) {
            Firebase firebase = new Firebase("https://tasking-android.firebaseio.com/");
            if(uid != null) {
                FirebaseValueEventListener valueEventListener = new FirebaseValueEventListener(authData.getUid());
                valueEventListener.setUid(authData.getUid());
                firebase.addValueEventListener(valueEventListener);
                this.setIsManager(valueEventListener.isManager());
                this.setHasTeam(valueEventListener.isHasTeam());
            }
    }

    @Override
    public void onAuthenticationError(FirebaseError firebaseError) {
        this.setOnline(false);
    }
}
