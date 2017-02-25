package in.projectmanas.hub;

import android.util.Log;

import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;

import java.util.List;

/**
 * Created by Kaushik on 2/25/2017.
 */

public class ReadSpreadSheet {

    public static final String SPREADSHEET_URL = "https://docs.google.com/spreadsheets/d/1FGI6TBtBW86w6xoZPQYzBIAEQ7coItk5QmX0hrzH-zw";
    List<Sheet> sheetsList;

    ReadSpreadSheet() {
        Spreadsheet spreadsheet = new Spreadsheet();
        sheetsList = spreadsheet.setSpreadsheetUrl(SPREADSHEET_URL).getSheets();
        Log.d("hehe", String.valueOf(sheetsList.size()));
    }
}
