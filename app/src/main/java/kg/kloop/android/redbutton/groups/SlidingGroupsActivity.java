package kg.kloop.android.redbutton.groups;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.alexwalker.sendsmsapp.R;

//import android.widget.TableLayout;

public class SlidingGroupsActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;
    CharSequence titles[] = {"Tab 1", "Tab 2", "Tab 3"};
    int numOfTabs = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, numOfTabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


    }

}
