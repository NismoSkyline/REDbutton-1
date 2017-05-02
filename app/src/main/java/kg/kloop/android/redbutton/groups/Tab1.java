package kg.kloop.android.redbutton.groups;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alexwalker.sendsmsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Tab1 extends Fragment implements View.OnClickListener {
    private static final String TAG = "Tab1 log";
    private Button createGroup, setvalueButton;
    private EditText groupName;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference groupsReference;
    private ListView groupsList;
    private ArrayList<GroupMembership> groupMembershipList;
    GroupListAdapter adapter2;
    private String userId;
    View v;
    DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_tab1_all_groups,container,false);

        init();
        groupMembershipList = new ArrayList<>();

        adapter2 = new GroupListAdapter(v.getContext(), groupMembershipList, Tab1.this);
        groupsList.setAdapter(adapter2);




        return v;
    }

    private void init(){
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        createGroup = (Button) v.findViewById(R.id.buttonPush);
        createGroup.setOnClickListener(this);
        setvalueButton = (Button) v.findViewById(R.id.buttonValue);
        setvalueButton.setOnClickListener(this);
        groupName = (EditText) v.findViewById(R.id.editGroupName);
        groupsList = (ListView) v.findViewById(R.id.groupsListView);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        groupsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        groupsReference = firebaseDatabase.getReference("Groups");

//        groupsReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    Map<String, Object> objectMap = (HashMap<String, Object>)postSnapshot.getValue();
//
//                    String groupName = getNameFromMapObject(objectMap);
//                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                    GroupMembership groupMembership = new GroupMembership();
//                    groupMembership.setGroupName(groupName);
//                    groupMembership.setPending(isInPending(postSnapshot, userId));
//                    groupMembership.setMember(isInMembers(postSnapshot, userId));
//
//                    groupMembershipList.add(groupMembership);
//                    adapter2.notifyDataSetChanged();
//                    //Toast.makeText(v.getContext(), groupName, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        groupsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateListOnChildAddedOrChildChanged(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "Child changed");
                updateListOnChildAddedOrChildChanged(dataSnapshot);


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


    void sendRequest(String groupName){
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        Request request = new Request(Uid, userName);
        String key = groupsReference.child(groupName).child("requests").push().getKey();
        //groupsReference.child(groupName).child("requests").push().setValue(request);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Groups/" + groupName + "/requests/" + key, request);
        childUpdates.put("/Users/" + userId + "/pending/" + groupName, true);
        mDatabase.updateChildren(childUpdates);
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

    private void updateListOnChildAddedOrChildChanged(DataSnapshot dataSnapshot){
        Map<String, Object> objectMap = (HashMap<String, Object>)dataSnapshot.getValue();

        String groupName = dataSnapshot.getKey();
        GroupMembership groupMembership = new GroupMembership();
        groupMembership.setGroupName(groupName);
        groupMembership.setPending(isInPending(dataSnapshot, userId));
        groupMembership.setMember(isInMembers(dataSnapshot, userId));

        boolean flag = false;
        for (GroupMembership gMembership: groupMembershipList){
            if (gMembership.getGroupName().equals(groupName)){
                //gMembership = groupMembership;
                groupMembershipList.remove(gMembership);
                groupMembershipList.add(groupMembership);
                flag = true;
                break;
            }
        }

        if (!flag) {
            groupMembershipList.add(groupMembership);
        }
        adapter2.notifyDataSetChanged();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.buttonPush:
                final String newGroupName = groupName.getText().toString();
                DatabaseReference rr = FirebaseDatabase.getInstance().getReference().child("Groups").child(newGroupName);

                rr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Toast.makeText(v.getContext(), "Это имя группы уже занято", Toast.LENGTH_SHORT).show();
                        } else {
                            GroupRoom group = new GroupRoom(newGroupName, userId);
                            groupsReference.child(newGroupName).setValue(group);
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/Groups/" + newGroupName, group);
                            childUpdates.put("/Users/" + userId + "/groups/" + newGroupName, true);
                            mDatabase.updateChildren(childUpdates);

//                            GroupMembership groupMembership = new GroupMembership();
//                            groupMembership.setGroupName(newGroupName);
//                            groupMembership.setPending(false);
//                            groupMembership.setMember(true);
//                            groupMembershipList.add(groupMembership);
//                            adapter2.notifyDataSetChanged();

                            Toast.makeText(v.getContext(), "Группа создана", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;
            case R.id.buttonValue:
                startActivity(new Intent(v.getContext(), Approve.class));
                break;
        }
    }
}