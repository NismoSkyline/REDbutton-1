package kg.kloop.android.redbutton.groups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alexwalker.sendsmsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GroupsList extends AppCompatActivity implements View.OnClickListener {
    private Button createGroup, setvalueButton;
    private EditText groupName;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference groupsReference;
    private ListView groupsList;
    private ArrayList<GroupMembership> groupMembershipList;
    private ArrayList<String> groupNames;
    GroupListAdapter adapter2;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        init();

        groupMembershipList = new ArrayList<>();
        groupNames = new ArrayList<>();

        adapter2 = new GroupListAdapter(this, groupMembershipList);
        groupsList.setAdapter(adapter2);
    }

    private void init(){
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
                    Map<String, Object> objectMap = (HashMap<String, Object>)postSnapshot.getValue();

                    String groupName = getNameFromMapObject(objectMap);
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    GroupMembership groupMembership = new GroupMembership();
                    groupMembership.setGroupName(groupName);
                    groupMembership.setPending(isInPending(postSnapshot, userId));
                    groupMembership.setMember(isInMembers(postSnapshot, userId));

                    groupNames.add(groupName);
                    groupMembershipList.add(groupMembership);
                    adapter2.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

     void sendRequest(String groupName){
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        Request request = new Request(Uid, userName);
        groupsReference.child(groupName).child("requests").push().setValue(request);
    }

    private String getNameFromMapObject(Map<String, Object> objectMap){
        String groupName = "";

        for (Map.Entry<String, Object> entry: objectMap.entrySet()){
            if (entry.getValue() instanceof String){
                if (entry.getKey().equals("name")){
                    groupName =(String) entry.getValue();
                }
            }
        }

        return groupName;
    }

    private boolean isInPending(DataSnapshot postSnapshot, String userId){
        boolean isPending = false;
        Iterable requests = postSnapshot.child("requests").getChildren();
        Iterator i = requests.iterator();
        while (i.hasNext()){
            DataSnapshot value = (DataSnapshot) i.next();
            Request r =  value.getValue(Request.class);
            if (userId.equals(r.getUserId())){
                isPending = true;
                break;
            }
        }

        return isPending;
    }

    private boolean isInMembers(DataSnapshot postSnapshot, String userId){
        boolean isMember = false;
        Iterable members = postSnapshot.child("members").getChildren();
        Iterator memberIterator = members.iterator();
        while (memberIterator.hasNext()){
            DataSnapshot member = (DataSnapshot) memberIterator.next();
            if (userId.equals(member.getKey())){
                isMember = true;
                break;
            }
        }

        return isMember;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonPush:
                final String newGroupName = groupName.getText().toString();
                DatabaseReference rr = FirebaseDatabase.getInstance().getReference().child("Groups").child(newGroupName);

                rr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Toast.makeText(GroupsList.this, "Это имя группы уже занято", Toast.LENGTH_SHORT).show();
                        } else {
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            GroupRoom group = new GroupRoom(newGroupName, userId);
                            groupsReference.child(newGroupName).setValue(group);
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/Groups/" + newGroupName, group);
                            childUpdates.put("/Users/" + userId + "/pending/" + newGroupName, true);
                            mDatabase.updateChildren(childUpdates);

                            Toast.makeText(GroupsList.this, "Группа создана", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;
            case R.id.buttonValue:
                startActivity(new Intent(GroupsList.this, Approve.class));
                break;
        }
    }
}
