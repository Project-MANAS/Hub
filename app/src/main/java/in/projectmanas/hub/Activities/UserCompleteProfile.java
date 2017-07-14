package in.projectmanas.hub.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import in.projectmanas.hub.AsyncResponse;
import in.projectmanas.hub.R;

import static in.projectmanas.hub.Activities.FirstRunActivity.mCredential;

public class UserCompleteProfile extends AppCompatActivity implements AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_complete_profile);
        linkViews();
        String params[] = getIntent().getStringArrayExtra("params");
        updateDetails(params);
    }

    private void linkViews() {

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
        Log.d("got", output.get(0).get(0));
    }
}
