package in.projectmanas.hub.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.util.ArrayList;
import java.util.Arrays;

import in.projectmanas.hub.AsyncResponse;
import in.projectmanas.hub.ConstantsManas;
import in.projectmanas.hub.Fragments.PartFragment;
import in.projectmanas.hub.R;


/**
 * Created by Kaushik on 2/25/2017.
 */

public class HomeActivity extends AppCompatActivity implements AsyncResponse {

    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS_READONLY};
    GoogleAccountCredential mCredential;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private DrawerLayout drawerLayout;
    private ListView drawerList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_home);
        linkViews();

        toolbar.setTitle("Dashboard");
        //toolbar.setTitleTextColor(Color.WHITE);
        //setSupportActionBar(toolbar);

        //tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);

        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        mCredential.setSelectedAccountName(getIntent().getStringExtra(ConstantsManas.ACCNAME));
        //Log.d("crdential here ", getIntent().getStringExtra(ConstantsManas.ACCNAME));
        updateDetails();
    }

    private void updateDetails() {
        String[] params = new String[]{"DashBoard!H3:K"};
        ReadSpreadSheet readSpreadSheet = new ReadSpreadSheet(mCredential, this);
        readSpreadSheet.delegate = this;
        readSpreadSheet.execute(params);
    }

    private void linkViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_home);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.home_toolbar_layout);
        viewPager = (ViewPager) findViewById(R.id.home_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.home_tab_layout);
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"Leaderboard", "Users"}));
    }

    @Override
    public void processFinish(ArrayList<ArrayList<String>> output) {
        final ArrayList<Fragment> fragments = new ArrayList<>();
        PartFragment weeklyFragment = PartFragment.getInstance();
        ArrayList<String> weekList = new ArrayList<>();
        for (ArrayList<String> row : output) {
            weekList.add(row.get(0) + " : " + row.get(1));
        }
        weeklyFragment.setStringList(weekList);
        fragments.add(weeklyFragment);

        PartFragment monthlyFragment = PartFragment.getInstance();
        ArrayList<String> monthList = new ArrayList<>();
        for (ArrayList<String> row : output) {
            monthList.add(row.get(0) + " : " + row.get(2));
        }
        monthlyFragment.setStringList(monthList);
        fragments.add(monthlyFragment);

        PartFragment yearlyFragment = PartFragment.getInstance();
        ArrayList<String> yearList = new ArrayList<>();
        for (ArrayList<String> row : output) {
            yearList.add(row.get(0) + " : " + row.get(3));
        }
        yearlyFragment.setStringList(yearList);
        fragments.add(yearlyFragment);


        /*
        MonthlyFragment monthlyFragment = MonthlyFragment.getInstance();
        monthlyFragment.setStringList(output);
        fragments.add(monthlyFragment);
        YearlyFragment yearlyFragment = YearlyFragment.getInstance();
        fragments.add(yearlyFragment);
        yearlyFragment.setStringList(output);
        */
        final String[] titles = {"Weekly", "Monthly", "Yearly"};
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }
}
