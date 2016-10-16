package javier.newyorkmarathon.ui;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javier.newyorkmarathon.R;
import javier.newyorkmarathon.domain.UserField;
import javier.newyorkmarathon.managers.DataManager;

public class RegisterActivity extends AppCompatActivity {

    LinearLayout _formContainer;
    ProgressBar _progressBar;
    List<UserField> _userFields;
    HashMap<String, EditText> _editTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _editTexts =  new HashMap<>();
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        _progressBar = (ProgressBar) findViewById(R.id.spinner);
        setSupportActionBar(toolbar);
        _formContainer = (LinearLayout) findViewById(R.id.formContainer);
        getFields();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Register
                registerUser();
            }
        });
    }

    private void getFields() {
        new AsyncTask<Void, Void, List<UserField>>() {
            @Override
            protected List<UserField> doInBackground(Void... params) {
                List<UserField> fields = null;
                try {
                    fields= DataManager.getInstance().fetchFieds();
                } catch (IOException e) {
                    Log.e("fetchFields", e.getMessage());
                    showNoNetworkMessage(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getFields();
                        }
                    });
                }
                return fields;
            }

            @Override
            protected void onPostExecute(List<UserField>fields) {
               _userFields = fields;
                if(fields!=null) {
                    for (UserField field : fields) {
                        appendField(field);
                    }
                }
                _progressBar.setVisibility(View.GONE);
            }
        }.execute();
    }

    private void showNoNetworkMessage(DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle(getString(R.string.network_error_title))
                .setMessage(getString(R.string.network_error_message))
                .setPositiveButton(getString(R.string.try_again), onClickListener)
                .setNegativeButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void appendField(UserField field){
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        TextInputLayout titleWrapper = new TextInputLayout(this);
        titleWrapper.setLayoutParams(layoutParams);
        titleWrapper.setHint(field.text);
        _formContainer.addView(titleWrapper);

        EditText editText = new EditText(this);
        editText.setLayoutParams(layoutParams);
        editText.setSingleLine(true);
        titleWrapper.addView(editText);
        _editTexts.put(field.fieldName,editText);
    }

    private void registerUser(){
        final Boolean[] error = {false};
        String params="";
        for (Map.Entry<String, EditText> entry : _editTexts.entrySet()) {
            params+=entry.getKey()+"="+entry.getValue().getText()+"&";
        }
        _progressBar.setVisibility(View.VISIBLE);
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                try {
                    DataManager.getInstance().saveUser(params[0]);
                } catch (IOException e) {
                    Log.e("registerUser", e.getMessage());
                    error[0] = true;
                    showNoNetworkMessage(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            registerUser();
                        }
                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                _progressBar.setVisibility(View.GONE);
                if(!error[0]){
                    registeredSuccessfully();
                }
            }
        }.execute(params);

    }

    private void registeredSuccessfully(){
        Snackbar.make(_formContainer, R.string.registered_success, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        for (Map.Entry<String, EditText> entry : _editTexts.entrySet()) {
           entry.getValue().setText("");
        }
    }
}
