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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaushik on 2/25/2017.
 */

public class ReadSpreadSheet extends AsyncTask<String, Void, ArrayList<ArrayList<String>>> {
    private com.google.api.services.sheets.v4.Sheets mService = null;
    private Exception mLastError = null;
    private String range;
    public AsyncResponse delegate = null;

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
    protected ArrayList<ArrayList<String>> doInBackground(String[] params) {
        try {
            range = params[0];
            return getDataFromApi();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }


    private ArrayList<ArrayList<String>> getDataFromApi() throws IOException {
        String spreadsheetId = "1FGI6TBtBW86w6xoZPQYzBIAEQ7coItk5QmX0hrzH-zw";

        ValueRange response = this.mService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        ArrayList<ArrayList<String>> valueStrings = new ArrayList<>();
        if (values != null) {
            //Log.d("size", values.size() + " ");
            for (List row : values) {
                //Log.d("adasd", row.get(0) + " " + row.get(1) + " " + row.get(2));
                ArrayList<String> currentRow = new ArrayList<>();
                for (Object ob : row) {
                    //Log.d("Output here", ob.toString());
                    currentRow.add(ob.toString());
                }
                valueStrings.add(currentRow);
            }
        }
        //Log.d("Output recieved of size", valueStrings.size() + "");
        return valueStrings;
    }


    @Override
    protected void onPostExecute(ArrayList<ArrayList<String>> output) {
        //Log.d("Output recieved of size", output.size() + "");
        delegate.processFinish(output);
    }

}
