package com.tasking;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Map;


public class FireBaseDB {

    private static FireBaseDB instance = null;
    private Firebase firebaseConnection;


    public static FireBaseDB getInstance() {
        if(instance == null) {
            Firebase.getDefaultConfig().setPersistenceEnabled(true);
            instance = new FireBaseDB();
        }
        return instance;
    }

    private FireBaseDB() {
        firebaseConnection = new Firebase("https://tasking-android.firebaseio.com/");
    }


    public  void createUssr(final String username, String password, final MyCallback<String> callback){
        firebaseConnection.createUser(username, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public synchronized void onSuccess(Map<String, Object> result) {
                String uid = result.get("uid").toString();
                firebaseConnection.child("managers").child(uid).child("username").setValue(username);
                if(callback != null){

                }
            }

            @Override
            public synchronized void onError(FirebaseError firebaseError) {
                System.out.println(firebaseError.getMessage());
            }
        });
    }

    public void createTeam(Employee manager,ArrayList <TeamMember> TM,String teamName){
        Firebase temp =  firebaseConnection.child("Managers").child("f38a77a6-2817-4d2b-8a4d-b7ddea9472cf");

    }

    public void authentication(Employee employee){

        firebaseConnection.authWithPassword(employee.getUsername(), employee.getPassword(), new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                //System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                System.out.println(firebaseError.getMessage());
            }
        });
    }

    public void sendResetEmail(Employee employee){
        firebaseConnection.resetPassword(employee.getUsername(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(FirebaseError firebaseError) {
            }
        });
    }

    public void deleteEmployeeFromDB(Employee employee){
        firebaseConnection.removeUser(employee.getUsername(), employee.getPassword(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(FirebaseError firebaseError) {
            }
        });
    }

    public void addNewTask(Task task){

    }

    public void updateTaskDetails(Task task){

    }
    public void setTaskAssignees(Task task, Employee employee){

    }
    public void setManagerEmployees(Employee manager,ArrayList<TeamMember> tms){

    }

}
