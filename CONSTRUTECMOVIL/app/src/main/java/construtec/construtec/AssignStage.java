package construtec.construtec;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
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

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Isaac on 10/11/2016.
 */
public class AssignStage extends Fragment {
    View _assignStage;
    TextView _projectsview;
    List<Integer> _projectsID = new ArrayList<>();
    List<String> _projectName = new ArrayList<>();
    List<String> _projectPres = new ArrayList<>();
    List<String> _stagesdata = new ArrayList<String>();
    List<String> _stages = new ArrayList<>();
    Integer _temp=0;

    Integer u_id = 0;
    Integer u_code = 0;
    String u_name = "";
    String jsonArray = "";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _assignStage = inflater.inflate(R.layout.assign_stage,container,false);
        /*
        Seteo de los datos al list view de los projectos.
         */
        try {
            getprojects();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        u_id = getArguments().getInt("u_id");
        u_code = getArguments().getInt("u_code");
        u_name = getArguments().getString("u_name");
        jsonArray = getArguments().getString("jsonArray");


        _projectsview = (TextView) _assignStage.findViewById(R.id.titleprojects);
        _projectsview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater _layout = LayoutInflater.from(_assignStage.getContext());
                final View _showprojects = _layout.inflate(R.layout.chooseprojects, null);
                final AlertDialog.Builder _windowpro = new AlertDialog.Builder(_assignStage.getContext());
                _windowpro.setView(_showprojects);
                ArrayAdapter<String> _listviewAdapter = new ArrayAdapter<String>(_showprojects.getContext(),
                        android.R.layout.simple_list_item_1,_projectPres);
                _windowpro.setCancelable(false)
                        .setAdapter(_listviewAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getprojectdetails(_projectsID.get(which));
                                start(_showprojects,which);


                            }
                        });
                AlertDialog _wa = _windowpro.create();
                _windowpro.show();

            }
        });



        return _assignStage;
    }

    private void start(View _showprojects, int which) {
        final LayoutInflater _layoutstage = LayoutInflater.from(_showprojects.getContext());
        final View _showprojectsstage = _layoutstage.inflate(R.layout.chooseprojects, null);
        AlertDialog.Builder _windowprostages = new AlertDialog.Builder(_showprojects.getContext());
        _windowprostages.setView(_showprojectsstage);
        showstages(_windowprostages, _showprojectsstage, _projectsID.get(which));
    }

    public void getprojects() throws JSONException {
        final AsyncHttpClient httpClient = new AsyncHttpClient();

        String server = "http://isaac:7249/api/project/get/u_id/undefined";
        httpClient.get(server, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject _newJsonObject = response.getJSONObject(i);
                        _projectsID.add(_newJsonObject.getInt("p_id"));
                        _projectName.add(_newJsonObject.getString("p_name"));
                        _projectPres.add(_newJsonObject.getInt("p_id") + ": " + _newJsonObject.getString("p_name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                httpClient.cancelAllRequests(true);

            }
        });

    }

    public void getprojectdetails(int p_id){
        final AsyncHttpClient httpClient = new AsyncHttpClient();
        String server = "http://isaac:7249/api/projectdetails/get/p_id/"+p_id;
        httpClient.get(server,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try{
                    for(int i = 0;i<response.length();i++){
                        JSONObject _newJSONObject = response.getJSONObject(i);
                        JSONArray _newJSONArray = _newJSONObject.getJSONArray("stages");
                        for(int j = 0; j<_newJSONArray.length();j++){

                            JSONObject _stageJSONObject = _newJSONArray.getJSONObject(j);
                            String action = "Name: " + _stageJSONObject.getString("s_name") + "\n"
                                    + "Status: " + _stageJSONObject.getString("s_status") + "\n" +
                                    "Start date: " + _stageJSONObject.getString("s_datestart") + "\n"
                                    + "Start end: " + _stageJSONObject.getString("s_dateend")+"\n";
                            if(!_stagesdata.contains(action))
                                _stagesdata.add(action);

                        }

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });


    }

    public void showstages(AlertDialog.Builder _windowpro, View _showprojects, Integer _integer){
        ArrayAdapter<String> _listviewAdapter = new ArrayAdapter<String>(_showprojects.getContext(),
                android.R.layout.simple_list_item_1,_stagesdata);
        final Integer _project = _integer;
        _windowpro.setCancelable(false)
                .setAdapter(_listviewAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        /**FragmentManager frg = getFragmentManager();
                        frg.beginTransaction()
                                .replace(R.layout.assign_stage,new addStage())
                                .commit();*/
                        Intent ca = new Intent(getActivity(),addStage.class);
                        ca.putExtra("p_id",_project);
                        ca.putExtra("u_id",u_id);
                        ca.putExtra("u_code",u_code);
                        ca.putExtra("u_name",u_name);
                        ca.putExtra("jsonArray",jsonArray);
                        startActivity(ca);
                    }
                });
        AlertDialog _windowproco = _windowpro.create();
        _windowpro.show();
    }

    public List<String> getStages(){
        final AsyncHttpClient httpClient = new AsyncHttpClient();
        String server = "http://isaac:7249/api/stage/get/p_id/undefined";
        final List<String> _temp = new ArrayList<>();
        httpClient.get(server,null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject _newJSON = response.getJSONObject(i);
                        _temp.add(_newJSON.getString("s_name"));


                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        _stages = _temp;
        return _temp;
    }
}
