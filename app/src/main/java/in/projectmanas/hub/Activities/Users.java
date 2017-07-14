package in.projectmanas.hub.Activities;

import android.content.Intent;
import android.icu.util.Output;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import in.projectmanas.hub.AsyncResponse;
import in.projectmanas.hub.R;

import static in.projectmanas.hub.Activities.FirstRunActivity.mCredential;

public class Users extends AppCompatActivity implements AsyncResponse{

    private ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        linkViews();
        String[] params = getIntent().getStringArrayExtra("params");
        updateDetails(params);
    }

    private void linkViews() {
        userListView = (ListView) findViewById(R.id.lv_users);
    }

    private void updateDetails(String[] params) {
        //Log.d("Updating :" , "Details");
        ReadSpreadSheet readSpreadSheet = new ReadSpreadSheet(mCredential, this);
        readSpreadSheet.delegate = this;
        readSpreadSheet.execute(params);
        //Log.d("Updating :" , "Details");
    }

    @Override
    public void processFinish(ArrayList<ArrayList<String>> output) {
        ArrayList<String> userList = new ArrayList<>();
        for(ArrayList<String> temp : output) {
            userList.add(temp.get(0));
        }
        userListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userList));
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String params[] = new String[]{"Leaderboard!A"+(4+i)+":X"+(4+i)};
                startActivity(new Intent(Users.this, UserCompleteProfile.class).putExtra("params", params));
            }
        });
    }
}
