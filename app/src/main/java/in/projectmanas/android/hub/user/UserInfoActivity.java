package in.projectmanas.android.hub.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import in.projectmanas.android.hub.Constants;
import in.projectmanas.android.hub.R;
import in.projectmanas.android.hub.backend.PMUserWrapper;

public class UserInfoActivity extends AppCompatActivity {
    ImageView contactImgImgView;
    private CoordinatorLayout coordinatorLayout;
    private EditText editTextFirst;
    private EditText editTextLast;
    private String email;
    private TextView tUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email = getSharedPreferences(Constants.files.authentication, Context.MODE_PRIVATE)
                .getString(Constants.preferences.username, null);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_user_info);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.setDisplayHomeAsUpEnabled(true);

        Button saveChangesButton = (Button) findViewById(R.id.buttonSaveChanges);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSave();
            }
        });

        findViewById(R.id.b_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogout();
            }
        });

        //Intent for choosing image and bringing it to this activity
        contactImgImgView = (ImageView) findViewById(R.id.imageViewUserImage);
        contactImgImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Set Profile Picture"), 1);
            }
        });

        editTextFirst = (EditText) findViewById(R.id.editTextFname);
        editTextLast = (EditText) findViewById(R.id.editTextLname);
        tUsername = (TextView) findViewById(R.id.textViewUsername);

        ParseUser.getCurrentUser().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    Snackbar.make(coordinatorLayout, e.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    PMUserWrapper user = new PMUserWrapper((ParseUser) object);
                    updateProfile(user);
                }
            }
        });
    }

    private void onLogout() {
        Snackbar.make(coordinatorLayout, R.string.logging_out, Snackbar.LENGTH_INDEFINITE)
                .show();
        //noinspection AccessStaticViaInstance
        ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    Snackbar.make(coordinatorLayout, e.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onLogout();
                                }
                            })
                            .show();
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });
    }

    private void onSave() {
        final String fname = editTextFirst.getText().toString();
        final String lname = editTextLast.getText().toString();

        PMUserWrapper user = new PMUserWrapper(ParseUser.getCurrentUser());
        user.setFirstName(fname);
        user.setLastName(lname);
        Snackbar.make(coordinatorLayout, R.string.saving, Snackbar.LENGTH_INDEFINITE)
                .show();
        user.getUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    Snackbar.make(coordinatorLayout, e.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onSave();
                                }
                            })
                            .show();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void updateProfile(PMUserWrapper user) {
        tUsername.setText(user.getUser().getEmail());
        editTextFirst.setText(user.getFirstName());
        editTextLast.setText(user.getLastName());
    }

    public void onActivityResult(int resCode, int reqCode, Intent data) {
        if (resCode == RESULT_OK)        //If user presses back button
            if (reqCode == 1)
                contactImgImgView.setImageURI(data.getData());
    }


}
