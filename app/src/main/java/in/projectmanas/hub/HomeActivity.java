package in.projectmanas.hub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Kaushik on 2/25/2017.
 */

public class HomeActivity extends AppCompatActivity implements AsyncResponse {

    GoogleAccountCredential mCredential;
    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS_READONLY};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        mCredential.setSelectedAccountName(getIntent().getStringExtra(ConstantsManas.ACCNAME));
        Log.d("crdential here ", getIntent().getStringExtra(ConstantsManas.ACCNAME));
        String[] params = new String[]{"DashBoard!I3:K"};
        ReadSpreadSheet readSpreadSheet = new ReadSpreadSheet(mCredential, this);
        readSpreadSheet.delegate = this;
        readSpreadSheet.execute(params);

    }

    @Override
    public void processFinish(ArrayList<ArrayList<String>> output) {
        //Just logging here for checking the fetched data
        Log.d("check", output.size()+" ");
        for (List<String> row : output) {
            for (String cell : row) {
                Log.d("check", cell);
            }
        }
        //TODO: Do something of this table;
    }
}
