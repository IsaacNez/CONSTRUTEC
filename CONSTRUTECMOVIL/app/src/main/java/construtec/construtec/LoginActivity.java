package construtec.construtec;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import com.loopj.android.http.*;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView _newRegister;
    String userName="";
    Integer userID = 0;
    Integer userCode=0;
    JSONArray userRole = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        _newRegister = (TextView) findViewById(R.id.RegisterNewClient);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String ID = mEmailView.getText().toString();
                if(ID.matches("@|[a-zA-Z]+|$|!|#|%|&")){
                    mEmailView.setError("Numeric characters only");
                    View focus = null;
                    focus = mEmailView;
                    focus.requestFocus();
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = mEmailView.getText().toString();
                if(ID.matches("@|[a-zA-Z]+|$|!|#|%|&")||mPasswordView.getText().toString().matches("")){
                    Toast.makeText(LoginActivity.this,"User ID or password doesn't match",Toast.LENGTH_LONG).show();
                }else{

                    if(userRole != null) {
                        getUser();

                    }
                }
            }
        });

        _newRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this,UserRegistration.class);
                startActivity(register);
            }
        });

    }

    public void getUser(){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        String server = getResources().getString(R.string.url)+"user/get/u_id,u_password/"+mEmailView.getText().toString()+','+mPasswordView.getText().toString();
        System.out.println(server);

        httpClient.get(server, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    JSONObject newObject = response.getJSONObject(0);
                    if (newObject == null) {
                        Toast.makeText(getApplicationContext(), "Your ID or password is incorrect", Toast.LENGTH_LONG).show();
                    } else {
                        userName = newObject.getString("q_name");
                        userCode = newObject.getInt("q_code");
                        userID = newObject.getInt("q_id");
                        userRole = new JSONArray(newObject.get("q_role").toString());
                        System.out.println(newObject.toString() + "Esta es la prueba " + (new JSONArray(newObject.get("q_role").toString())).toString()
                                + " " + userID);

                        System.out.println(userRole.toString() + "Esta es la impresion de los roles");
                        Intent user = new Intent(LoginActivity.this, UserInterface.class);
                        user.putExtra("jsonArray", userRole.toString());
                        user.putExtra("Name", userName);
                        user.putExtra("UCode", userCode);
                        user.putExtra("UID", userID);
                        startActivity(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Your ID or password is incorrect", Toast.LENGTH_LONG).show();
                }

            }
        });
        httpClient.cancelAllRequests(true);

    }


}
