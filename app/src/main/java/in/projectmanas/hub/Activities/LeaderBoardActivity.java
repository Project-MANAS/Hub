package in.projectmanas.hub.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.util.ArrayList;

import in.projectmanas.hub.AsyncResponse;
import in.projectmanas.hub.Fragments.PartFragment;
import in.projectmanas.hub.R;

/**
 * Created by knnat on 14-07-2017.
 */

public class LeaderBoardActivity extends AppCompatActivity implements AsyncResponse {

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
        toolbar.setTitle("Leaderboard");
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        startActivity(new Intent(LeaderBoardActivity.this, HomeActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(LeaderBoardActivity.this, Users.class));
                        break;
                    case 2:
                        String[] params1 = new String[]{"Leaderboard!A4:F"};
                        updateDetails(params1);
                        break;
                    case 3:
                        String[] params2 = new String[]{"Leaderboard!G4:L"};
                        updateDetails(params2);
                        break;
                    case 4:
                        String[] params3 = new String[]{"Leaderboard!M4:R"};
                        updateDetails(params3);
                        break;
                    case 5:
                        String[] params4 = new String[]{"Leaderboard!S4:X"};
                        updateDetails(params4);
                        break;
                }
            }
        });
        //toolbar.setTitleTextColor(Color.WHITE);
        //setSupportActionBar(toolbar);

        //tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
        mCredential = FirstRunActivity.mCredential;

        //Log.d("crdential here ", getIntent().getStringExtra(ConstantsManas.ACCNAME));
        String[] params = new String[]{"Leaderboard!A4:F"};
        updateDetails(params);
    }

    private void updateDetails(String[] params) {
        //Log.d("Updating :" , "Details");
        ReadSpreadSheet readSpreadSheet = new ReadSpreadSheet(mCredential, this);
        readSpreadSheet.delegate = this;
        readSpreadSheet.execute(params);
        //Log.d("Updating :" , "Details");
    }

    private void linkViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_home);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.home_toolbar_layout);
        viewPager = (ViewPager) findViewById(R.id.home_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.home_tab_layout);
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"DashBoard", "Users", "Attendance", "In time", "Out Time", "Punch Duration"}));
    }

    @Override
    public void processFinish(ArrayList<ArrayList<String>> output) {
        Log.d("Something:", "came");
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
            monthList.add(row.get(2) + " : " + row.get(3));
        }
        monthlyFragment.setStringList(monthList);
        fragments.add(monthlyFragment);

        PartFragment yearlyFragment = PartFragment.getInstance();
        ArrayList<String> yearList = new ArrayList<>();
        for (ArrayList<String> row : output) {
            yearList.add(row.get(4) + " : " + row.get(5));
        }
        yearlyFragment.setStringList(yearList);
        fragments.add(yearlyFragment);

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
