package kg.kloop.android.redbutton.groups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.alexwalker.sendsmsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class GroupsList extends AppCompatActivity implements View.OnClickListener {
    private Button createGroup, setvalueButton;
    private EditText groupName;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference groupsReference;
    private ListView groupsList;
    private ArrayList<GroupMembership> groupMembershipList;
    GroupListAdapter adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        init();

        groupMembershipList = new ArrayList<>();

        adapter2 = new GroupListAdapter(this, groupMembershipList);
        groupsList.setAdapter(adapter2);
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

            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        groupsReference = firebaseDatabase.getReference("Groups");

        groupsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    GroupRoom group = postSnapshot.getValue(GroupRoom.class);
                    GroupMembership groupMembership = new GroupMembership();
                    groupMembership.setGroupName(group.getName());

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Iterable data = postSnapshot.child("requests").getChildren();
                    Iterator i = data.iterator();
                    while (i.hasNext()){
                        DataSnapshot value = (DataSnapshot) i.next();
                        Request r =  value.getValue(Request.class);
                        if (userId.equals(r.getUserId())){
                            groupMembership.setPending(true);
                        }
                    }

                    groupMembershipList.add(groupMembership);
                    adapter2.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        groupsReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                GroupRoom group = dataSnapshot.getValue(GroupRoom.class);
//
//                GroupMembership groupMembership = new GroupMembership();
//                groupMembership.setGroupName(group.getName());
//
//                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                Iterable data = dataSnapshot.child("requests").getChildren();
//                Iterator i = data.iterator();
//                while (i.hasNext()){
//                    DataSnapshot value = (DataSnapshot) i.next();
//                    Request r =  value.getValue(Request.class);
//                    if (userId.equals(r.getUserId())){
//                        groupMembership.setPending(true);
//                    }
//                }
//
//                groupMembershipList.add(groupMembership);
//                adapter2.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

     void sendRequest(String groupName){
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        Request request = new Request(Uid, userName);
        groupsReference.child(groupName).child("requests").push().setValue(request);
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
                startActivity(new Intent(GroupsList.this, Approve.class));
                break;
        }
    }
}
