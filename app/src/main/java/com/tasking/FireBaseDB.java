package com.tasking;

/**
 * Created by Arel on 27/02/2016.
 */

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.AuthData;
import java.util.Map;


public class FireBaseDB {
    private static FireBaseDB instance = null;
    private Firebase fireBaseConnection;

    public static FireBaseDB getInstance() {  // singleton connection
        if(instance == null) {
            System.out.println("new connection");
            instance = new FireBaseDB();
        }
        return instance;
    }

    private FireBaseDB() {
        fireBaseConnection = new Firebase("https://tasking-android.firebaseio.com/");
    }


    public void createNewEmployee(Employee employee){
        final int isManager = employee.getIsManager();

        fireBaseConnection.createUser(employee.getUserName(), employee.getPassword(), new Firebase.ValueResultHandler<Map<String, Object>>() {

            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                Firebase temp =  fireBaseConnection.child("isManager").child(result.get("uid").toString());
                temp.setValue(isManager);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                System.out.println(firebaseError.getMessage());//TODO: if create is failed need to toast a msg
            }
        });
    }
    public void authenticationEmployee(Employee employee){
        fireBaseConnection.authWithPassword(employee.getUserName(), employee.getPassword(), new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                System.out.println(firebaseError.getMessage()); //TODO: if auth failed need to toast a msg
            }
        });
    }

    public void changeEmployeeEmail(Employee employee,String newEmail){
        fireBaseConnection.changeEmail(employee.getUserName(), employee.getPassword(), newEmail, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                //TODO: Email changed toast
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                //TODO: email didnt change
            }
        });
    }
    public void changeEmployeePassword(Employee employee,String newPassword){
        fireBaseConnection.changePassword(employee.getUserName(), employee.getPassword(), newPassword, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                //TODO: toast password changed
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                //TODO: error in changing password
            }
        });
    }
    public void sendResetEmail(Employee employee){
        fireBaseConnection.resetPassword(employee.getUserName(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                //TODO: toast - email sent
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                //TODO: error encountered
            }
        });
    }

    public void deleteEmployeeFromDB(Employee employee){
        fireBaseConnection.removeUser(employee.getUserName(), employee.getPassword(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                //TODO: toast-user removed
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                //TODO: error encountered
            }
        });
    }

    public void addNewTask(Task task){
        Firebase newTask = fireBaseConnection.child("tasks").child(task.getName());
        newTask.setValue(task);

        // fireBaseConnection.child("tasks").child(task.getName())//setValue("Do you have data? You'll love Firebase.");myFirebaseRef.child("message").setValue("Do you have data? You'll love Firebase.");
/*
        private String name;
        private String dueDate;
        private String category;
        private String priority;
        private String location;
        private String status;
        private ArrayList<TeamMember> assignees;
        */
    }

    public void updateTaskDetails(Task task){

    }


}
