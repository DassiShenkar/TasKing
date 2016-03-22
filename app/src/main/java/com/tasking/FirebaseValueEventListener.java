package com.tasking;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by Seymore on 3/21/2016.
 */
public class FirebaseValueEventListener implements ValueEventListener {

    private String uid;
    private boolean isManager;
    private boolean hasTeam;
    private boolean hasTasks;

    public FirebaseValueEventListener(String uid){
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public boolean isHasTasks() {
        return hasTasks;
    }

    public void setHasTasks(boolean hasTasks) {
        this.hasTasks = hasTasks;
    }

    public boolean isManager() {
        return isManager;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        //Manager
        if (dataSnapshot.child("Managers").child(this.getUid()).child("username").getValue() != null) {
            setIsManager(true);
            //Manager with team
            if (dataSnapshot.child("Managers").child(uid).getChildrenCount() > 1) {
                setHasTeam(true);
                //Manager with no team
            } else {
                setHasTeam(false);
            }
            // Team Member
        } else {
            setIsManager(false);
        }

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

}
