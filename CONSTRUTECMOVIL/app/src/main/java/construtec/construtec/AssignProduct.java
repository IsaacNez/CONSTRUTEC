package construtec.construtec;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
 * Created by Isaac on 10/11/2016.
 */
public class AssignProduct extends Fragment {
    View myView;
    List<Integer> _projectsID = new ArrayList<>();
    List<String> _projectName = new ArrayList<>();
    List<String> _projectPres = new ArrayList<>();

    List<Integer> pr_id = new ArrayList<>();
    List<String> pr_name = new ArrayList<>();
    List<Integer> pr_price = new ArrayList<>();
    List<String> _productsadapter = new ArrayList<>();

    Integer u_id = 0;
    Integer u_code = 0;
    String u_name ="";
    String jsonArray = "";
    Integer p_id = 0;
    String s_name = "";
    Integer pr_pos = 0;

    Spinner _showprojects;
    Spinner _showstages;
    Spinner _showproducts;

    TextView _showstagestitle;
    TextView _showproductstitle;

    List<String> _stagesdata= new ArrayList<>();
    List<JSONArray> _products = new ArrayList<>();
    List<String> _stagename = new ArrayList<>();
    ArrayAdapter<String> _projectspinneradapter;
    ArrayAdapter<String> _stagespinneradapter;
    ArrayAdapter<String> _productspinner;

    Button _addproduct;
    EditText _amount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.assign_prodcuts, null);
        u_id = getArguments().getInt("u_id", 0);
        u_code = getArguments().getInt("u_code", 0);
        u_name = getArguments().getString("u_name");
        jsonArray = getArguments().getString("jsonArray");

        getprojects();


        _showprojects = (Spinner) myView.findViewById(R.id._listviewprojectspro);
        _showstages = (Spinner) myView.findViewById(R.id._stagespinner);
        _showproducts = (Spinner) myView.findViewById(R.id._addproduct);
        _addproduct = (Button) myView.findViewById(R.id._addproducttoDB);
        _amount = (EditText) myView.findViewById(R.id._pramount);

        _showstages.setVisibility(View.INVISIBLE);
        _showproducts.setVisibility(View.INVISIBLE);


         _projectspinneradapter = new ArrayAdapter<String>(myView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, _projectPres);
        _showprojects.setAdapter(_projectspinneradapter);
        _showproducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pr_pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _productspinner = new ArrayAdapter<String>(myView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,_productsadapter);
        _showproducts.setAdapter(_productspinner);

        _stagespinneradapter = new ArrayAdapter<String>(myView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,_stagesdata);
        _showstages.setAdapter(_stagespinneradapter);
        _showstages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s_name = _stagename.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _showprojects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getproducts();
                getprojectdeatils(_projectsID.get(position));
                p_id = _projectsID.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        _addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!_amount.getText().toString().matches("")) {
                    AsyncHttpClient httpClient = new AsyncHttpClient();
                    String server = getResources().getString(R.string.url) + "stagexproduct/post";
                    try {
                        JSONObject params = new JSONObject();
                        params.put("s_name", s_name);
                        params.put("pr_id", pr_id.get(pr_pos));
                        params.put("p_id", p_id);
                        params.put("pr_price", pr_price.get(pr_pos));
                        params.put("pr_quantity", _amount.getText().toString());
                        StringEntity se = new StringEntity(params.toString());
                        httpClient.post(myView.getContext(), server, se, "application/json", new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                _amount.setText("");
                                Snackbar.make(myView,"Your order has been posted",Snackbar.LENGTH_LONG).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else{
                    Snackbar.make(myView,"You must specify an amount",Snackbar.LENGTH_LONG).show();
                }
            }
        });
        return myView;

        //_showstagestitle = (TextView) findViewById(R.id._choosestage);
        //_showproductstitle = (TextView) findViewById(R.id.chooseproducts);

    }
    public void getprojects() {
        final AsyncHttpClient httpClient = new AsyncHttpClient();

        String server = "http://isaac:7249/api/project/get/u_code/"+u_code;
        httpClient.get(server, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Projects: "+response.toString()+" "+response.length());
                if(response.length()==0){
                    Toast.makeText(myView.getContext(),"You don't own any project",Toast.LENGTH_LONG).show();
                }else {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject _newJsonObject = response.getJSONObject(i);
                            _projectsID.add(_newJsonObject.getInt("p_id"));
                            _projectName.add(_newJsonObject.getString("p_name"));
                            String action = _newJsonObject.getInt("p_id") + ": " + _newJsonObject.getString("p_name");
                            _projectPres.add(action);
                            System.out.println(_newJsonObject.getInt("p_id") + ": " + _newJsonObject.getString("p_name"));
                            _showstages.setVisibility(View.VISIBLE);
                            _projectspinneradapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    public void getprojectdeatils(int p_id){
        final AsyncHttpClient httpClient = new AsyncHttpClient();
        String server = "http://isaac:7249/api/projectdetails/get/p_id/"+p_id;
        httpClient.get(server,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if(response.length()==0){
                    Toast.makeText(myView.getContext(),"This project does not have a stage associaded, please create one",Toast.LENGTH_LONG).show();
                }else {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject _newJSONObject = response.getJSONObject(i);
                            JSONArray _newJSONArray = _newJSONObject.getJSONArray("stages");
                            for (int j = 0; j < _newJSONArray.length(); j++) {

                                JSONObject _stageJSONObject = _newJSONArray.getJSONObject(j);
                                String action = "Name: " + _stageJSONObject.getString("s_name")+ "\n";
                                _stagename.add(_stageJSONObject.getString("s_name"));
                                _products.add(_stageJSONObject.getJSONArray("products"));
                                if (!_stagesdata.contains(action))
                                    _stagesdata.add(action);
                                _showproducts.setVisibility(View.VISIBLE);
                                _stagespinneradapter.notifyDataSetChanged();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void getproducts(){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        String server = getResources().getString(R.string.url)+"product/get/pr_id/undefined";
        System.out.println("El server es:"+server);
        httpClient.get(server, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Productos recibidos: "+response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject _newJSON = response.getJSONObject(i);
                        pr_id.add(_newJSON.getInt("pr_id"));
                        pr_name.add(_newJSON.getString("pr_name"));
                        pr_price.add(_newJSON.getInt("pr_price"));
                        _productsadapter.add(_newJSON.getString("pr_name") + ": ₡" + _newJSON.getInt("pr_price"));
                        _productspinner.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        httpClient.cancelAllRequests(true);

    }
}
