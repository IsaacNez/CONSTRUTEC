package construtec.construtec;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
 * Created by Isaac on 10/11/2016.
 */
public class AssignStage extends Fragment {
    View _addstage;

    List<String> _projectPres;
    List<Integer> _projectsID;
    List<String> _projectName;

    Spinner _projectspinner;
    Spinner _stagespinner;

    ArrayAdapter<String> _projectadapter;
    ArrayAdapter<String> _stageadapter;
    private List<String> _stagesdata;

    Button _add;

    ListView _stagedetails;

    EditText _stageSD;
    EditText _stageDE;

    Integer _position;
    Integer _stagechoice;
    private List<String> _stages;
    private ArrayAdapter<String> _stagedetailadapter;


    Integer _ucode=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _addstage = inflater.inflate(R.layout.assign_stage,null);
        _ucode = getArguments().getInt("u_code");
        getprojects();
        _projectspinner = (Spinner) _addstage.findViewById(R.id._projectspinner);
        _stagespinner = (Spinner) _addstage.findViewById(R.id._stagespinner);
        _add = (Button) _addstage.findViewById(R.id._addbutton);
        _stagedetails = (ListView) _addstage.findViewById(R.id._stagedetails);
        _stageDE = (EditText) _addstage.findViewById(R.id._stageDE);
        _stageSD = (EditText) _addstage.findViewById(R.id._stageSD);



        _projectadapter = new ArrayAdapter<String>(_addstage.getContext(),
                android.R.layout.simple_list_item_1,_projectPres);
        _projectspinner.setAdapter(_projectadapter);
        _projectspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _position = position;
                getStages(_projectsID.get(position));
                _stageadapter = new ArrayAdapter<String>(_addstage.getContext(),
                        android.R.layout.simple_spinner_dropdown_item, _stagesdata);
                _stagespinner.setAdapter(_stageadapter);
                getprojectdetails(_projectsID.get(_position));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        _stagespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _stagechoice = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_stageSD.getText().toString().matches("") |_stageDE.getText().toString().matches("")){
                    Snackbar.make(_addstage,"You need to specify the End Date or the Start Date of your Stage",Snackbar.LENGTH_LONG).show();
                }else {
                    AsyncHttpClient httpClient = new AsyncHttpClient();
                    String server = getString(R.string.url) + "projectxstage/post";
                    try {
                        JSONObject params = new JSONObject();
                        params.put("p_id", _projectsID.get(_position));
                        params.put("s_name", _stagesdata.get(_stagechoice));
                        params.put("pxs_datestart", _stageSD.getText().toString());
                        params.put("pxs_dateend", _stageDE.getText().toString());
                        params.put("pxs_status", "Construyendo");
                        StringEntity se = new StringEntity(params.toString());
                        httpClient.post(_addstage.getContext(),server,se,"application/json",new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Snackbar.make(_addstage,"You have successfully added a new Stage",Snackbar.LENGTH_LONG).show();
                                getStages(_projectsID.get(_position));
                                getprojectdetails(_projectsID.get(_position));
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

        return _addstage;
    }

    public void getprojects(){
        final AsyncHttpClient httpClient = new AsyncHttpClient();
        _projectPres = new ArrayList<>();
        _projectsID = new ArrayList<>();
        _projectName = new ArrayList<>();
        String server = getString(R.string.url)+"project/get/u_code/"+_ucode;
        httpClient.get(server, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {


                    for (int i = 0; i < response.length(); i++) {
                        JSONObject _newJsonObject = response.getJSONObject(i);
                        _projectsID.add(_newJsonObject.getInt("p_id"));
                        _projectName.add(_newJsonObject.getString("p_name"));
                        _projectPres.add("Project ID: "+_newJsonObject.getInt("p_id") + "\n " +"Project Name: "+ _newJsonObject.getString("p_name")+"\n"+
                        "Project Budget: "+_newJsonObject.getInt("p_budget")+"\n");
                    }
                    _projectadapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                httpClient.cancelAllRequests(true);

            }
        });





    }

    public void getprojectdetails(int p_id){
        final AsyncHttpClient httpClient = new AsyncHttpClient();
        String server = getString(R.string.url)+"projectdetails/get/p_id/"+p_id;
        System.out.println("Project details: "+server);
        httpClient.get(server, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    System.out.println(response.toString());


                        _stages = new ArrayList<>();
                        _stagedetailadapter = new ArrayAdapter<String>(_addstage.getContext(), android.R.layout.simple_list_item_1,_stages);
                        _stagedetails.setAdapter(_stagedetailadapter);
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject _newJSONObject = response.getJSONObject(i);
                            JSONArray _newJSONArray = _newJSONObject.getJSONArray("stages");
                            if(_newJSONArray.length()>0) {
                                for (int j = 0; j < _newJSONArray.length(); j++) {

                                    JSONObject _stageJSONObject = _newJSONArray.getJSONObject(j);
                                    String action = "Name: " + _stageJSONObject.getString("s_name") + "\n"
                                            + "Status: " + _stageJSONObject.getString("s_status") + "\n" +
                                            "Date Start: " + _stageJSONObject.getString("s_datestart") + "\n"
                                            + "Date End: " + _stageJSONObject.getString("s_dateend")+"\n"+
                                            "Stage Budget: "+_stageJSONObject.getString("gpd_budget")+ "\n";
                                    if (!_stages.contains(action))
                                        _stages.add(action);

                                }
                            }else{
                                Snackbar.make(_addstage,"This project doesn't owns any stage",Snackbar.LENGTH_LONG).show();
                            }


                        }
                        _stagedetailadapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    public List<String> getStages(Integer p_id){
        final AsyncHttpClient httpClient = new AsyncHttpClient();
        String server = getString(R.string.url)+"stage/get/p_id/stages,"+p_id;
        System.out.println("STAGES"+server);
        final List<String> _temp = new ArrayList<>();
        _stagesdata = new ArrayList<>();
        httpClient.get(server, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    System.out.println(response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject _newJSON = response.getJSONObject(i);
                        _stagesdata.add(_newJSON.getString("s_name"));


                    }
                    _stageadapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return _temp;
    }
}
