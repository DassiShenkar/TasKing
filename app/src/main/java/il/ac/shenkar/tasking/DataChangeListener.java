package il.ac.shenkar.tasking;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class DataChangeListener implements ValueEventListener {

    private static DataChangeListener instance;
    private DataSnapshot snapshot;

    public DataSnapshot getSnapshot() {
        return snapshot;
    }

    private DataChangeListener(){

    }

    public static DataChangeListener getInstance() {
        if(instance == null){
            instance = new DataChangeListener();
        }
        return instance;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        snapshot = dataSnapshot;
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
