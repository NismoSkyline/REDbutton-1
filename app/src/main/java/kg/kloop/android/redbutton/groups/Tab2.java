package kg.kloop.android.redbutton.groups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.alexwalker.sendsmsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Tab2 extends Fragment {
    private ListView mygroupsListview;
    private GroupListAdapter adapter;
    private ArrayList<GroupMembership> myGroupsList;
    private DatabaseReference userGroupsReference;
    private String userId;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_tab2_my_groups,container,false);

 //       DatabaseReference r2 = userGroupsReference.child("groups");
//
//        r2.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Map<String, Boolean> groups = (HashMap<String, Boolean>) dataSnapshot.getValue();
//                for (String groupname: groups.keySet()){
//                    GroupMembership groupMembership = new GroupMembership(groupname, true, false);
//                    myGroupsList.add(groupMembership);
//                }
//                adapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        init();
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
}