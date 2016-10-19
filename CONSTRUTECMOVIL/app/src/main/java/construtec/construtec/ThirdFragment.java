package construtec.construtec;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by Isaac on 10/10/2016.
 */
public class ThirdFragment extends Fragment {
    View myView;
    JSONArray rolearray;
    TextView chooserole;
    TextView chooseclient;
    TextView chooseengineer;
    TextView _waiting;
    EditText _projectName;
    EditText _projectLocation;

    Spinner spclients;
    Spinner spchooserole;

    List<Integer> _codes = new ArrayList<>();
    List<String> _names = new ArrayList<>();
    List<String> _especification = new ArrayList<>();
    List<String> charges = new ArrayList<>();
    Integer _tempClient = 0;
    Integer _action = 0;
    Button submit_project;
    Integer _ucode=0;
    Integer _uid=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.third_layout,container,false);
        chooserole = (TextView) myView.findViewById(R.id.roletext);
        spchooserole = (Spinner) myView.findViewById(R.id.chargesuser);
        chooseclient = (TextView) myView.findViewById(R.id.choseclient);
        chooseengineer = (TextView) myView.findViewById(R.id.choseengineer);
        _waiting = (TextView) myView.findViewById(R.id.waiting);
        submit_project = (Button) myView.findViewById(R.id.submit_project);
        _projectName = (EditText) myView.findViewById(R.id._projectname);
        _projectLocation = (EditText) myView.findViewById(R.id._projectlocation);
        spclients = (Spinner) myView.findViewById(R.id.spinnerclients);
       chooserole.setVisibility(myView.INVISIBLE);
        spchooserole.setVisibility(myView.INVISIBLE);
        chooseclient.setVisibility(myView.INVISIBLE);
        chooseengineer.setVisibility(myView.INVISIBLE);
        spclients.setVisibility(myView.INVISIBLE);
        _waiting.setVisibility(myView.INVISIBLE);
        String roles = getArguments().getString("role");
        _ucode = getArguments().getInt("u_code");
        _uid = getArguments().getInt("u_id");
        try {
            rolearray = new JSONArray(roles);
            for(int i = 0 ; i<rolearray.length();i++){
                if (rolearray.getInt(i)==2)
                    charges.add("Enginner");
                else if (rolearray.getInt(i)==3)
                    charges.add("Client");
            }
            if(rolearray.length()>=2) {
                chooserole.setVisibility(myView.VISIBLE);
                spchooserole.setVisibility(myView.VISIBLE);
                spclients.setVisibility(myView.VISIBLE);
                _waiting.setVisibility(myView.VISIBLE);
            }
            if(rolearray.length()==1){
                if(rolearray.getInt(0)==2){
                    _action = 0;
                    chooserole.setVisibility(myView.INVISIBLE);
                    _waiting.setVisibility(myView.INVISIBLE);
                    chooseclient.setVisibility(myView.VISIBLE);
                    spclients.setVisibility(myView.VISIBLE);
                    ArrayAdapter<String> clients = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,_especification);
                    spclients.setAdapter(clients);
                    get("user_undefined");
                    spclients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            _tempClient = _codes.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if(rolearray.getInt(0)==3){
                    _action =1;
                    _waiting.setVisibility(myView.INVISIBLE);
                    chooseengineer.setVisibility(myView.VISIBLE);
                    ArrayAdapter<String> clients = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,_especification);
                    spclients.setAdapter(clients);
                    get("engineer_undefined");
                    spclients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            _tempClient = _codes.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,charges);
        spchooserole.setAdapter(newAdapter);

        spchooserole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString() == "Engineer") {
                    _action = 0;
                    _waiting.setVisibility(myView.INVISIBLE);
                    chooseclient.setVisibility(myView.VISIBLE);
                    ArrayAdapter<String> clients = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,_especification);
                    spclients.setAdapter(clients);
                    get("user_undefined");
                    spclients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            _tempClient = _codes.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (parent.getItemAtPosition(position).toString() == "Client") {
                    _action = 1;
                    _waiting.setVisibility(myView.INVISIBLE);
                    chooseengineer.setVisibility(myView.VISIBLE);
                    ArrayAdapter<String> clients = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,_especification);
                    spclients.setAdapter(clients);
                    get("engineer_undefined");
                    spclients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            _tempClient = _codes.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit_project.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(_action==0){
                    if(_projectName.getText().toString().matches("") | _projectLocation.getText().toString().matches(""))
                        Toast.makeText(getContext(),"You must enter some data",Toast.LENGTH_LONG).show();
                    else{
                        String server = "http://isaac:7249/api/project/post";
                        AsyncHttpClient client = new AsyncHttpClient();
                        JSONObject params = new JSONObject();
                        try {
                            params.put("p_name", _projectName);
                            params.put("p_location",_projectLocation);
                            params.put("u_code",_ucode);
                            params.put("u_id", _tempClient);
                            StringEntity se = new StringEntity(params.toString());
                            client.post(getContext(),server, (HttpEntity) se,"application/json",new JsonHttpResponseHandler());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }
        });
        return myView;
    }

    public void get(String action){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        String server = "http://isaac:7249/api/user/get/u_id/"+action;
        System.out.println(server);

        httpClient.get(server, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObj = response.getJSONObject(i);
                        _codes.add(jsonObj.getInt("u_code"));
                        _names.add(jsonObj.getString("u_name"));
                        String temp = jsonObj.getString("u_name") + ": " + jsonObj.getInt("u_code");
                        _especification.add(temp);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        httpClient.cancelAllRequests(true);

    }
}
