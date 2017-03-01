package in.projectmanas.hub;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.util.Arrays;



/**
 * Created by Kaushik on 2/25/2017.
 */

public class HomeActivity extends AppCompatActivity {

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
        String[] params = new String[]{"DashBoard!I4:K"};
        new ReadSpreadSheet(mCredential).execute(params);
    }
}
