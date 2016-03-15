package com.tasking;

/**
 * Created by Arel on 27/02/2016.
 */

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.AuthData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FireBaseDB {
    private static FireBaseDB instance = null;
    private Firebase fireBaseConnection;
    private Manager manager;
    private  String managerUid;

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

    public  String getManagerUid() {
        return managerUid;
    }

    public  void createNewManager(Manager mng){
        //final int isManager = employee.getIsManager();
        manager = mng;



        fireBaseConnection.createUser(manager.getUserName(), manager.getPassword(), new Firebase.ValueResultHandler<Map<String, Object>>() {

            @Override
            public synchronized void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                Firebase temp = fireBaseConnection.child("Managers").child(result.get("uid").toString());
                temp.setValue(0);
               // manager.setUid(result.get("uid").toString());
              //  setManagerUid(result.get("uid").toString());
                //mng.setUid(manager.getUid());
            }

            @Override
            public synchronized void onError(FirebaseError firebaseError) {
                System.out.println(firebaseError.getMessage());//TODO: if create is failed need to toast a msg
            }
        });


        mng.setUid(manager.getUid());

    }

    public void createTeam(Employee manager,ArrayList <TeamMember> TM,String teamName){
        Firebase temp =  fireBaseConnection.child("Managers").child("f38a77a6-2817-4d2b-8a4d-b7ddea9472cf");

        //Team - name of the team
        //arraylist of TM
    }


/*
    public String getUIDByEmail(Employee employee){
        fireBaseConnection.getAuth().getUid();

    }
/*
    public void setManager(Employee manager){//managers
     //   set UID ,set username ->email
    }

    public void setTeamMember(TeamMember tm){
       // set UID ,set username ->email
    }



    public Arraylist<TeamMember> getTeamMembers(UID Manager){

    }
//end of managers//

//************************************/
/*
    public void createTask (string ManagerUID,Task task){
        //generate Task UID by push();
    }


    public Arraylist <Task> void getAllTasks(string ManagerUID){

    }

    public Task getTaskDetails(string taskUID){

    }

    public void updateTaskStatus(string UID){
        //change status of task
    }

    public void acceptTask(string taskUID,TeamMember tm){
        change status of UIDMEMBER
    }
*/

/*

    in local DB -
            1. set employee UID
    2. set task UID

*/
//**********old db*************//

    public void authenticationEmployee(Employee employee){

        fireBaseConnection.authWithPassword(employee.getUserName(), employee.getPassword(), new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                //System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                managerUid = authData.getUid();
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
    public void setTaskAssignees(Task task, Employee employee){
        Firebase temp = fireBaseConnection.child("tasks_assignees").child(task.getName());
      //  temp.setValue(task.getId());
        temp.setValue(employee.getUserName());


    }
    public void setManagerEmployees(Employee manager,ArrayList<TeamMember> tms){
      //  String str = manager.getUserName();
       // str.replace("@","A");
        Firebase temp = fireBaseConnection.child("manager_employees");
        Map<String, String> members = new HashMap<String, String>();
        Map<String, Map<String, String>> users = new HashMap<String, Map<String, String>>();
        for (TeamMember tm : tms) {
            members.put("name",tm.getUserName());
        }
        users.put("new manager",members);
        temp.setValue(users);
/*
           // Firebase usersRef = ref.child("users");
            Map<String, String> alanisawesomeMap = new HashMap<String, String>();
            alanisawesomeMap.put("birthYear", "1912");
            alanisawesomeMap.put("fullName", "Alan Turing");
            Map<String, Map<String, String>> users = new HashMap<String, Map<String, String>>();
            users.put("alanisawesome", alanisawesomeMap);
            usersRef.setValue(users);*/

    }

}
