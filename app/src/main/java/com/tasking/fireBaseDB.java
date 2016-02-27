/**
 * Created by Arel on 27/02/2016.
 */

package com.tasking;

        import com.firebase.client.Firebase;
        import com.firebase.client.FirebaseError;

        import java.util.Map;


public class fireBaseDB {

    private static fireBaseDB instance = null;
    private Firebase fireBaseConnection;

    public static fireBaseDB getInstance() {  // singleton connection
        if(instance == null) {
            System.out.println("new connection");
            instance = new fireBaseDB();
        }
        return instance;
    }

    private fireBaseDB() {
        fireBaseConnection = new Firebase("https://tasking-android.firebaseio.com/");
    }


    public void createNewEmployee(Employee employee){
        fireBaseConnection.createUser(employee.getUserName(), employee.getPassword(), new Firebase.ValueResultHandler<Map<String, Object>>() {

            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                System.out.println(firebaseError.getMessage());
            }
        });
       // Map<String, Object> isManager = new HashMap<String, Object>();
       // isManager.put("nickname", "Alan The Machine");
       // alanRef.updateChildren(isManager);
    }
/*
    Map<String, Object> nickname = new HashMap<String, Object>();
    nickname.put("nickname", "Alan The Machine");
    alanRef.updateChildren(nickname);

        Firebase newUser = myFirebaseRef.child("users").child("arelGindos1");
        Employee arel = new Employee("arel@gmail.com", "54825", 3);
        newUser.setValue(arel);
 */

}
