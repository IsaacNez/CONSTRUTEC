package construtec.construtec;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
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
    Button _showclient;

    String roles="";




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.third_layout, container, false);
        roles = getArguments().getString("role");
        _ucode = getArguments().getInt("u_code");
        _uid = getArguments().getInt("u_id");

        startdata();

        submit_project = (Button) myView.findViewById(R.id.submit_project);
        _showclient = (Button) myView.findViewById(R.id._selectclient);

        _projectName = (EditText) myView.findViewById(R.id._projectname);
        _projectLocation = (EditText) myView.findViewById(R.id._projectlocation);

        submit_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_projectName.getText().toString().matches("") | _projectLocation.getText().toString().matches(""))
                    Toast.makeText(getActivity().getApplicationContext(), "You must enter some data", Toast.LENGTH_LONG).show();
                else {
                    String server = getString(R.string.url)+"project/post";
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
                                Toast.makeText(myView.getContext(),"Se ha hecho el post",Toast.LENGTH_LONG).show();
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


        _showclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater layout = LayoutInflater.from(myView.getContext());
                final View _list = layout.inflate(R.layout.show, null);
                AlertDialog.Builder _window = new AlertDialog.Builder(myView.getContext());
                _window.setView(_list);
                if((_action == 0) |(_action==1)) {
                    chooseclient(_window,_list);
                }
                else if(_action == 3){
                    _showclient.setText("Select your role");
                    ArrayAdapter<String> _listvalue = new ArrayAdapter<String>(_list.getContext(),
                            android.R.layout.simple_list_item_1, charges);
                    _window
                            .setCancelable(false)
                            .setAdapter(_listvalue, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which == 0){

                                        get("user_undefined", "client");
                                        final LayoutInflater superlayout = LayoutInflater.from(_list.getContext());
                                        final View _listchoose = layout.inflate(R.layout.chooseuserdependingon, null);
                                        AlertDialog.Builder _windowchoose = new AlertDialog.Builder(_list.getContext());
                                        _windowchoose.setView(_listchoose);
                                        ArrayAdapter<String> _listclients = new ArrayAdapter<String>(_listchoose.getContext(),
                                        android.R.layout.simple_list_item_2,_especification);
                                        _windowchoose
                                                .setCancelable(false)
                                                .setAdapter(_listclients, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Toast.makeText(_listchoose.getContext(),"You have choosen: "+_especification.get(which)
                                                                +" as your client",Toast.LENGTH_LONG).show();
                                                        _engineer = _ucode;
                                                        _client = _codes.get(which);
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog _windowsecundary = _windowchoose.create();
                                        _windowchoose.show();

                                    }
                                    else if (which == 1){
                                        get("engineer_undefined", "engineer");
                                        final LayoutInflater superlayout = LayoutInflater.from(_list.getContext());
                                        final View _listchoose = layout.inflate(R.layout.chooseuserdependingon, null);
                                        AlertDialog.Builder _windowchoose = new AlertDialog.Builder(_list.getContext());
                                        _windowchoose.setView(_listchoose);
                                        ArrayAdapter<String> _listclients = new ArrayAdapter<String>(_listchoose.getContext(),
                                                android.R.layout.simple_list_item_2,_especification);
                                        _windowchoose
                                                .setCancelable(false)
                                                .setAdapter(_listclients, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        _engineer = _codes.get(which);
                                                        _client = _uid;
                                                        Toast.makeText(_listchoose.getContext(),"You have choosen: "+_especification.get(which)
                                                        +" as your engineer",Toast.LENGTH_LONG).show();
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog _windowsecundary = _windowchoose.create();
                                        _windowchoose.show();


                                    }
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog _windowshow = _window.create();
                    _window.show();
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
                            _codes.add(jsonObj.getInt("u_code"));
                            _names.add(jsonObj.getString("u_name"));
                            String temp = jsonObj.getString("u_name") + ": " + jsonObj.getInt("u_code");
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
                    charges.add("Enginner");
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
    public void chooseclient(AlertDialog.Builder _window,final View _list){
        switch (_action){
            case 0:
                get("user_undefined", "client");
                _showclient.setText("Select your client");
                break;
            case 1:
                _showclient.setText("Select your engineer");
                get("engineer_undefined", "engineer");
        }
        ArrayAdapter<String> _listvalue = new ArrayAdapter<String>(_list.getContext(),
                android.R.layout.simple_list_item_1, _especification);
        _window
                .setCancelable(false)
                .setAdapter(_listvalue, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (_action){
                            case 0:
                                _engineer = _ucode;
                                _client = _codes.get(which);
                                Toast.makeText(_list.getContext(),"You have choosen: "+_especification.get(which)
                                        +" as your client",Toast.LENGTH_LONG).show();
                                dialog.cancel();
                                break;
                            case 1:
                                _engineer = _codes.get(which);
                                _client = _uid;
                                Toast.makeText(_list.getContext(),"You have choosen: "+_especification.get(which)
                                        +" as your engineer",Toast.LENGTH_LONG).show();
                                dialog.cancel();
                                break;
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //_clientshow.setText(_names.get(_userselection.getSelectedItemPosition()));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog _windowshow = _window.create();
        _window.show();
    }

}
