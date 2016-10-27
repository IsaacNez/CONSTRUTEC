package construtec.construtec;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.INotificationSideChannel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Isaac on 10/10/2016.
 */
public class ThirdFragment extends Fragment{
    View myView;
    JSONArray rolearray;
    EditText _projectName;
    EditText _projectLocation;

    List<Integer> _codes = new ArrayList<>();
    List<String> _names = new ArrayList<>();
    List<String> _especification;
    List<String> charges = new ArrayList<>();

    Integer _tempClient = 0;
    Integer _action = 0;
    Integer _ucode=0;
    Integer _uid=0;
    Integer _engineer = 0;
    Integer _client = 0;

    Button submit_project;
    String roles="";
    String _temp="";

    Spinner _roles;
    Spinner _choose;
    TextView _chooseview;

    ArrayAdapter<String> _listvalue;
    ArrayAdapter<String> _listclients;
    ArrayAdapter<String> _listvaluerole;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.third_layout, container, false);
        roles = getArguments().getString("role");
        _ucode = getArguments().getInt("u_code");
        _uid = getArguments().getInt("u_id");
        _roles = (Spinner) myView.findViewById(R.id._rolespinner);
        _choose = (Spinner) myView.findViewById(R.id._generalspinner);
        _chooseview = (TextView) myView.findViewById(R.id._choose);

        startdata();
        if(_action == 0){
            _chooseview.setText("Choose an enginner");
            get("user_undefined", "engineer");
            _roles.setVisibility(View.INVISIBLE);
             _listvalue = new ArrayAdapter<String>(myView.getContext(),
                    android.R.layout.simple_spinner_dropdown_item, _especification);
            _choose.setAdapter(_listvalue);
        }else if(_action == 1){
            get("user_undefined", "client");
            _chooseview.setText("Choose a client");
            _roles.setVisibility(View.INVISIBLE);
            _listvalue = new ArrayAdapter<String>(myView.getContext(),
                    android.R.layout.simple_spinner_dropdown_item, _especification);
            _choose.setAdapter(_listvalue);

        }else{
           _listvaluerole = new ArrayAdapter<String>(myView.getContext(),
                    android.R.layout.simple_spinner_dropdown_item, charges);
            _roles.setAdapter(_listvaluerole);
            _listvaluerole.notifyDataSetChanged();
            _chooseview.setText("");
        }

        _roles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _temp = parent.getItemAtPosition(position).toString();
                if(parent.getItemAtPosition(position).toString().matches("Engineer"))
                    getother("blabla", "client");
                else {
                    getother("blabla", "engineer");
                }
                 _listclients = new ArrayAdapter<String>(myView.getContext(),
                        android.R.layout.simple_spinner_dropdown_item,_especification);
                _choose.setAdapter(_listclients);




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _choose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(_action);
                switch (_action){
                    case 1:
                        _engineer = _ucode;
                        _client = _codes.get(position);
                        Toast.makeText(myView.getContext(),"You have choosen: "+_especification.get(position)
                                +" as your client",Toast.LENGTH_LONG).show();
                        break;
                    case 0:
                        _engineer = _codes.get(position);
                        _client = _uid;
                        Toast.makeText(myView.getContext(),"You have choosen: "+_especification.get(position)
                                +" as your engineer",Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        if(_temp.matches("Engineer")){
                            _chooseview.setText("Choose your client");
                            Toast.makeText(myView.getContext(),"You have choosen: "+_especification.get(position)
                                    +" as your client",Toast.LENGTH_LONG).show();
                            _engineer = _ucode;
                            _client = _codes.get(position);
                        }else{
                            _chooseview.setText("Choose your engineer");
                            _engineer = _codes.get(position);
                            _client = _uid;
                            Toast.makeText(myView.getContext(),"You have choosen: "+_especification.get(position)
                                    +" as your engineer",Toast.LENGTH_LONG).show();
                        }
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit_project = (Button) myView.findViewById(R.id.submit_project);

        _projectName = (EditText) myView.findViewById(R.id._projectname);
        _projectLocation = (EditText) myView.findViewById(R.id._projectlocation);

        submit_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_projectName.getText().toString().matches("") | _projectLocation.getText().toString().matches(""))
                    Toast.makeText(getActivity().getApplicationContext(), "You must enter some data", Toast.LENGTH_LONG).show();
                else {
                    String server = getString(R.string.url) + "project/post";
                    final AsyncHttpClient client = new AsyncHttpClient();
                    JSONObject params = new JSONObject();
                    try {
                        params.put("p_name", _projectName.getText().toString());
                        params.put("p_location", _projectLocation.getText().toString());
                        params.put("u_code", _engineer);
                        params.put("u_id", _client);
                        System.out.println(params.toString());
                        StringEntity se = new StringEntity(params.toString());
                        client.post(myView.getContext(), server, se, "application/json", new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Toast.makeText(myView.getContext(), "Se ha hecho el post", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });






        return myView;
    }


    /**
     * Depending on the role the user has choose (in teh case it owns more than one role)
     * it seends the correct information to be inserted into the project table.
     * @param action
     * @param controller
     */
    public synchronized void get(String action, final String controller){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        String server = getString(R.string.url)+controller+"/get/u_id/"+action;
        System.out.println(server);
        _especification = new ArrayList<>();

        httpClient.get(server, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println(response.toString());
                try {
                    if (controller.matches("engineer")) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObj = response.getJSONObject(i);
                            _codes.add(jsonObj.getInt("u_id"));
                            _names.add(jsonObj.getString("u_name"));
                            String temp = jsonObj.getString("u_name") + ": " + jsonObj.getInt("u_id");
                            _especification.add(temp);
                        }
                    } else if (controller.matches("client")) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObj = response.getJSONObject(i);
                            _codes.add(jsonObj.getInt("u_id"));
                            _names.add(jsonObj.getString("u_name"));
                            String temp = jsonObj.getString("u_name") + ": " + jsonObj.getInt("u_id");
                            _especification.add(temp);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                _listvalue.notifyDataSetChanged();


            }
        });
        httpClient.cancelAllRequests(true);

    }


    public synchronized void getother(String action, final String controller){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        String server = getString(R.string.url)+controller+"/get/u_id/"+action;
        System.out.println(server);
        _especification = new ArrayList<>();

        httpClient.get(server, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println(response.toString());
                try {
                    if (controller.matches("engineer")) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObj = response.getJSONObject(i);
                            _codes.add(jsonObj.getInt("u_id"));
                            _names.add(jsonObj.getString("u_name"));
                            String temp = jsonObj.getString("u_name") + ": " + jsonObj.getInt("u_id");
                            _especification.add(temp);
                        }
                    } else if (controller.matches("client")) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObj = response.getJSONObject(i);
                            _codes.add(jsonObj.getInt("u_id"));
                            _names.add(jsonObj.getString("u_name"));
                            String temp = jsonObj.getString("u_name") + ": " + jsonObj.getInt("u_id");
                            _especification.add(temp);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                _listclients.notifyDataSetChanged();

            }
        });
        httpClient.cancelAllRequests(true);

    }



    /**
     * Checks if the user that log on has more than one role, if it is that case
     * it makes the user to choose which role to use.
     */
    private synchronized void startdata(){
        try {
            rolearray = new JSONArray(roles);
            for(int i = 0 ; i<rolearray.length();i++){
                if (rolearray.getInt(i)==2)
                    charges.add("Engineer");
                else if (rolearray.getInt(i)==3)
                    charges.add("Client");
            }
            if(rolearray.length()>=2) {
                _action = 3;
            }
            if(rolearray.length()==1){
                if(rolearray.getInt(0)==2){
                    _action = 0;
                }
                else if(rolearray.getInt(0)==3){
                    _action =1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * It is when it has more than one role, and it choose between then, the correct data is set in
     * the correct popup to be display and they choose.
     * @param _window
     * @param _list
     */


}
