package construtec.construtec;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Isaac on 10/10/2016.
 */
public class SecondFragment extends Fragment {
    View myView;
    Button _createStage;
    EditText _stagename;
    EditText _stagedescription;
    @Nullable
    @Override
    /**
     * It adds a new stage to the STAGE table, it is only accessed by engineers.
     */
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.second_layout,container,false);
        _createStage = (Button) myView.findViewById(R.id.create);
        _stagename = (EditText) myView.findViewById(R.id._stageName);
        _stagedescription = (EditText) myView.findViewById(R.id._stageDescription);
        _createStage.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(!_stagedescription.getText().toString().matches("")|!_stagename.getText().toString().matches("") ) {
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    String server = getString(R.string.url) + "stage/post";
                    try {
                        JSONObject params = new JSONObject();
                        params.put("s_name", _stagename.getText().toString());
                        params.put("s_description", _stagedescription.getText().toString());
                        System.out.println(params.toString());
                        StringEntity se = new StringEntity(params.toString());
                        asyncHttpClient.post(myView.getContext(), server, se, "application/json", new JsonHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Snackbar.make(myView, "There has been an error", Snackbar.LENGTH_LONG).show();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Snackbar.make(myView, "You have create a stage successfuly", Snackbar.LENGTH_LONG).show();
                                _stagename.setText("");
                                _stagedescription.setText("");
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else{
                    Snackbar.make(myView, "You must specify the name of the stage or its description", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        return myView;
    }
}
