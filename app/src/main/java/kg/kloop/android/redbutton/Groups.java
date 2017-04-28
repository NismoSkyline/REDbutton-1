package kg.kloop.android.redbutton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alexwalker.sendsmsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Groups extends AppCompatActivity implements View.OnClickListener {
    private Button createGroup, setvalueButton;
    private EditText groupName;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference groupsReference;
    private FirebaseUser firebaseUser;
    private ListView groupsList;
    private String[] list;
    private ArrayList<String> names;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        init();

        names = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);
        groupsList.setAdapter(adapter);
    }

    private void init(){
        createGroup = (Button) findViewById(R.id.buttonPush);
        createGroup.setOnClickListener(this);
        setvalueButton = (Button) findViewById(R.id.buttonValue);
        setvalueButton.setOnClickListener(this);
        groupName = (EditText) findViewById(R.id.editGroupName);

        groupsList = (ListView) findViewById(R.id.groupsListView);
        groupsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(Groups.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
                String groupName = ((TextView) view).getText().toString();
                String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                //Toast.makeText(Groups.this, Uid, Toast.LENGTH_SHORT).show();
                Request request = new Request(Uid, userName);
                groupsReference.child(groupName).child("requests").push().setValue(request);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        groupsReference = firebaseDatabase.getReference("Groups");

        groupsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GroupRoom group = dataSnapshot.getValue(GroupRoom.class);
                names.add(group.getName());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonPush:

                //groupsReference.push().setValue("Pushed");
                GroupRoom group = new GroupRoom(groupName.getText().toString());
                //groupsReference.push().setValue(group);
//                ArrayList<String> members = new ArrayList<>();
//                members.add("user1");
//                members.add("user2");
//                group.setMembers(members);
                //groupsReference.child(group.getName()).child("members").setValue(group.getMembers());
                //String id = groupsReference.push().getKey();
                //group.setId(id);
                groupsReference.child(group.getName()).setValue(group);

                break;
            case R.id.buttonValue:
                startActivity(new Intent(Groups.this, Approve.class));
                break;
        }
    }
}
