package in.projectmanas.hub;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.List;

/**
 * Created by Kaushik on 2/25/2017.
 */

public class ReadSpreadSheet extends AsyncTask<String, Void, List<List<String>>> {
    private com.google.api.services.sheets.v4.Sheets mService = null;
    private Exception mLastError = null;
    private String range;

    ReadSpreadSheet(GoogleAccountCredential credential) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Project Manas Hub")
                .build();
    }

    /**
     * Background task to call Google Sheets API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected List<List<String>> doInBackground(String[] params) {
        try {
            range = params[0];
            return getDataFromApi();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }


    private List<List<String>> getDataFromApi() throws IOException {
        String spreadsheetId = "1FGI6TBtBW86w6xoZPQYzBIAEQ7coItk5QmX0hrzH-zw";

        ValueRange response = this.mService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        List<List<String>> valueStrings = null;
        if (values != null) {
            Log.d("size", values.size() + " ");
            for (List row : values) {
                Log.d("adasd", row.get(0) + " " + row.get(1) + " " + row.get(2));
                List<String> currentRow = null;
                for (Object ob : row) {
                    currentRow.add(ob.toString());
                }
                valueStrings.add(currentRow);
            }
        }
        return valueStrings;
    }


    @Override
    protected void onPostExecute(List<List<String>> output) {
        Log.d("done", "done");
    }

}
