package in.projectmanas.hub.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.projectmanas.hub.AsyncResponse;
import in.projectmanas.hub.ConstantsManas;
import in.projectmanas.hub.Fragments.MonthlyFragment;
import in.projectmanas.hub.Fragments.WeeklyFragment;
import in.projectmanas.hub.Fragments.YearlyFragment;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_home);
        linkViews();

        toolbar.setTitle("Home");
        //toolbar.setTitleTextColor(Color.WHITE);
        //setSupportActionBar(toolbar);
        final ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(WeeklyFragment.getInstance());
        fragments.add(MonthlyFragment.getInstance());
        fragments.add(YearlyFragment.getInstance());
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
        toolbar = (Toolbar) findViewById(R.id.home_toolbar_layout);
        viewPager = (ViewPager) findViewById(R.id.home_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.home_tab_layout);
    }

    @Override
    public void processFinish(ArrayList<ArrayList<String>> output) {
        //Just logging here for checking the fetched data
        Log.d("check", output.size() + " ");
        for (List<String> row : output) {
            for (String cell : row) {
                Log.d("check", cell);
            }
        }
        //TODO: Do something of this table;
    }
}
