package kg.kloop.android.redbutton.groups;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alexwalker.sendsmsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Approve extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private ListView listView;
    private ArrayList<String> requests;
    ArrayAdapter<String> adapter;
    TextView info;
    String userIdForChange;
    String reqId;

    private HashMap<String, Request> requestsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);

        init();

    }

    private void init(){
        info = (TextView) findViewById(R.id.infotext);
        listView  = (ListView) findViewById(R.id.listView);

        requestsList = new HashMap<>();



        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference().child("Groups").child("secon2").child("requests");

        requests = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, requests);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userName = ((TextView)view).getText().toString();

                for (HashMap.Entry<String, Request> entry: requestsList.entrySet()){
                    Request req = entry.getValue();
                    if (req.getUserName().equals(userName)){
                        userIdForChange = req.getUserId();
                        reqId = entry.getKey();
                        int newCount = req.getAgreeCount() + 1;

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/"+reqId+"/agreeCount", newCount );
                        childUpdates.put("/"+reqId + "/approved/"+ FirebaseAuth.getInstance().getCurrentUser().getUid(), true);

                        ref.updateChildren(childUpdates);

                        //ref.child(reqId).child("agreeCount").setValue(newCount);
                    }
                }

            }
        });

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Request request = dataSnapshot.getValue(Request.class);
                info.setText("");
                info.append("key: " + dataSnapshot.getKey() + "\n");
                info .append("userId: " + request.getUserId() + "\n");

                requests.add(request.getUserName());
                requestsList.put(dataSnapshot.getKey(), request);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
