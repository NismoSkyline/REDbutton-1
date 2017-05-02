package kg.kloop.android.redbutton.groups;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.alexwalker.sendsmsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

//import android.widget.TableLayout;

public class SlidingGroupsActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;
    CharSequence titles[] = {"Группы", "Мои группы"};
    int numOfTabs = 2;
    private DatabaseReference groupsReference;
    private DatabaseReference mDatabase;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, numOfTabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        groupsReference = FirebaseDatabase.getInstance().getReference("Groups");

    }

    void sendRequest(String groupName){
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        Request request = new Request(Uid, userName);
        String key = groupsReference.child(groupName).child("requests").push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Groups/" + groupName + "/requests/" + key, request);
        childUpdates.put("/Users/" + userId + "/pending/" + groupName, true);
        mDatabase.updateChildren(childUpdates);
    }

}
