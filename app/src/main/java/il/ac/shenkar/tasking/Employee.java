package il.ac.shenkar.tasking;


public class Employee {
    private String username;
    private String password;
    private String uid;
    private String managerId;

    public Employee() {

    }

    public Employee(String username, String password) {
        this.username = username;
        this.password = password;
        this.uid = null;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return "Employee{ 'username':'" + username + "'}";
    }
}
