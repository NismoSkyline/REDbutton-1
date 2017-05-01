package kg.kloop.android.redbutton.groups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.alexwalker.sendsmsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Tab2 extends Fragment {
    private ListView mygroupsListview;
    private GroupListAdapter adapter;
    private ArrayList<GroupMembership> myGroupsList;
    private DatabaseReference userGroupsReference;
    private String userId;
    private static final String TAG = "Tab2 log";
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_tab2_my_groups,container,false);
        init();


        //       DatabaseReference r2 = userGroupsReference.child("groups");
//
        userGroupsReference.child("groups").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Map<String, Boolean> groups = (HashMap<String, Boolean>) dataSnapshot.getValue();
//                for (String groupname: groups.keySet()){
//                    GroupMembership groupMembership = new GroupMembership(groupname, true, false);
//                    myGroupsList.add(groupMembership);
//                    Log.d(TAG, "in the loop");
//                }
//                Log.d(TAG, "In list there are: " + Integer.toString(myGroupsList.size()));
//
//                adapter.notifyDataSetChanged();
//                Log.d(TAG, "In adapter, count: " + Integer.toString(adapter.getCount()));
                updateList(dataSnapshot, true);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userGroupsReference.child("pending").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateList(dataSnapshot, false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return v;
    }

    private void init(){
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userGroupsReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        myGroupsList = new ArrayList<>();
        mygroupsListview = (ListView) v.findViewById(R.id.mygroupsListview);
        adapter = new GroupListAdapter(v.getContext(), myGroupsList, Tab2.this );
        mygroupsListview.setAdapter(adapter);
    }

    private void updateList(DataSnapshot dataSnapshot, boolean isMember){
        Map<String, Boolean> groups = (HashMap<String, Boolean>) dataSnapshot.getValue();
        if (groups != null) {
            for (String groupname : groups.keySet()) {
                GroupMembership groupMembership = new GroupMembership(groupname, isMember, !isMember);
                myGroupsList.add(groupMembership);
                Log.d(TAG, "in the loop");
            }
            Log.d(TAG, "In list there are: " + Integer.toString(myGroupsList.size()));

            adapter.notifyDataSetChanged();
            Log.d(TAG, "In adapter, count: " + Integer.toString(adapter.getCount()));
        }
    }
}