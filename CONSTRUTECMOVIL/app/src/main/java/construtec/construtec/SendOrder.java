package construtec.construtec;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Isaac on 10/11/2016.
 */
public class SendOrder extends Fragment {
    View _assignStage;

    Spinner _projects;
    Spinner _stage;

    ListView _specification;

    JSONArray jsonArray;

    List<String> _rolelist;

    Integer _position =0;

    String param = "";
    String product="";

    Button _buy;


    Integer value=0;
    String values="";
    private List<Integer> _projectsID;
    private List<String> _projectPres;
    List<String> _projectName;
    List<String> _stagesname;

    ArrayAdapter<String> _projectspinneradapter;
    ArrayAdapter<String> _productadapter;
    List<String> _stagesdata;
    ArrayAdapter<String> _stagespinneradapter;
    List<JSONArray> _products;
    List<String> _stageproducts;

    Boolean _canbuy = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _assignStage = inflater.inflate(R.layout.send_order,container,false);
        String _temparray = getArguments().getString("role");
        _projects = (Spinner) _assignStage.findViewById(R.id._projectspinner);
        _stage = (Spinner) _assignStage.findViewById(R.id._stagespinner);
        _specification = (ListView) _assignStage.findViewById(R.id._productspinner);
        _buy = (Button) _assignStage.findViewById(R.id._buy);


        try {
            _rolelist = new ArrayList<>();
            jsonArray = new JSONArray(_temparray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(jsonArray.length()>1){
            for(int i= 0; i < jsonArray.length(); i++){
                try {
                    if(jsonArray.getInt(i) == 2)
                        _rolelist.add("Engineer");
                    else if (jsonArray.getInt(i)==3)
                        _rolelist.add("Client");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayAdapter _list = new ArrayAdapter(_assignStage.getContext(),android.R.layout.simple_list_item_1,_rolelist);
            AlertDialog.Builder _choose = new AlertDialog.Builder(_assignStage.getContext());
            _choose.setTitle("Choose you role");
            _choose.setCancelable(false)
                    .setAdapter(_list, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(_rolelist.get(which).matches("Engineer")){
                                param = "u_code";
                                value = getArguments().getInt("u_code");
                            }else if (_rolelist.get(which).matches("Client")){
                                param = "u_id";
                                value = getArguments().getInt("u_id");
                            }
                            getProject();
                        }
                    });
        }else{
            try {
                if(jsonArray.getInt(0) == 2) {
                    param = "u_code";
                    value = getArguments().getInt("u_code");
                }else if (jsonArray.getInt(0)==3) {
                    param = "u_id";
                    value = getArguments().getInt("u_id");
                }
                getProject();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        _projectspinneradapter = new ArrayAdapter<String>(_assignStage.getContext(),
                android.R.layout.simple_spinner_dropdown_item,_projectPres);
        _projects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getprojectdeatils(_projectsID.get(position));
                _stagespinneradapter = new ArrayAdapter<String>(_assignStage.getContext(),
                        android.R.layout.simple_spinner_dropdown_item, _stagesdata);
                _stage.setAdapter(_stagespinneradapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        _stage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getproducts(_products.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        _buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!_canbuy){
                    Snackbar.make(_assignStage,"This stage hasn't any product associated with. \n Try another one!",Snackbar.LENGTH_LONG).show();
                }else{
                    String server = getString(R.string.url)+"order/post";
                    AsyncHttpClient httpClient = new AsyncHttpClient();
                    try{
                        Random r = new Random();
                        int i1 = (r.nextInt(100000000-0) + 0);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String date = sdf.format(new Date());
                        JSONObject _params = new JSONObject();
                        _params.put("O_ID",i1);
                        _params.put("O_Priority",0);
                        _params.put("OStatus","Empacando");
                        _params.put("OrderDate",date);
                        _params.put("OPlatform","App");
                        _params.put("Products",product);
                        _params.put("Amount",values);
                        StringEntity se = new StringEntity(_params.toString());
                        httpClient.post(_assignStage.getContext(),server,se,"application/json",new JsonHttpResponseHandler(){
                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Snackbar.make(_assignStage,"There is a network error",Snackbar.LENGTH_LONG).show();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Snackbar.make(_assignStage,"Your order has been posted",Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }catch (JSONException e){
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return _assignStage;
    }

    /**
     * This function get the projects that are going to be assigned
     * to the spinner, then to be able to get it details.
     */
    private void getProject(){
        String server = getString(R.string.url)+"project/get/"+param+"/"+value;
        AsyncHttpClient httpclient = new AsyncHttpClient();
        _projectPres = new ArrayList<>();
        _projectName = new ArrayList<>();
        _projectsID = new ArrayList<>();
        httpclient.get(server, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (response.length() == 0) {
                    Toast.makeText(_assignStage.getContext(), "You don't own any project", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject _newJsonObject = response.getJSONObject(i);
                            _projectsID.add(_newJsonObject.getInt("p_id"));
                            _projectName.add(_newJsonObject.getString("p_name"));
                            String action = _newJsonObject.getInt("p_id") + ": " + _newJsonObject.getString("p_name");
                            _projectPres.add(action);
                            System.out.println(_newJsonObject.getInt("p_id") + ": " + _newJsonObject.getString("p_name"));


                        }
                        _projects.setAdapter(_projectspinneradapter);
                        _projectspinneradapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * This function obtains the details of a project, using its unique
     * identifier. It uses as a paramater the project id called from the
     * action that select the project in the project spinner.
     * @param p_id
     */
    public void getprojectdeatils(int p_id){
        final AsyncHttpClient httpClient = new AsyncHttpClient();
        String server = getString(R.string.url)+"projectdetails/get/p_id/"+p_id;
        _products = new ArrayList<>();
        _stagesdata = new ArrayList<>();
        _stagesname = new ArrayList<>();
        httpClient.get(server,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if(response.length()==0){
                    Toast.makeText(_assignStage.getContext(),"This project does not have a stage associaded, please create one",Toast.LENGTH_LONG).show();
                }else {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject _newJSONObject = response.getJSONObject(i);
                            JSONArray _newJSONArray = _newJSONObject.getJSONArray("stages");
                            for (int j = 0; j < _newJSONArray.length(); j++) {

                                JSONObject _stageJSONObject = _newJSONArray.getJSONObject(j);
                                String action = "Name: " + _stageJSONObject.getString("s_name")+ "\n";
                                _stagesname.add(_stageJSONObject.getString("s_name"));
                                _products.add(_stageJSONObject.getJSONArray("products"));
                                if (!_stagesdata.contains(action))
                                    _stagesdata.add(action);
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

    /**
     * Depending of the values gotten by the get project details
     * and the position of the stage, it gets the products of that stage
     * and present it to the user.
     * @param _products
     */
    public void getproducts(JSONArray _products){

        System.out.println(_products.toString());
        _stageproducts = new ArrayList<>();
        _productadapter = new ArrayAdapter<String>(_assignStage.getContext(),
                android.R.layout.simple_list_item_1,_stageproducts);
        _specification.setAdapter(_productadapter);
        Integer _temp=0;
        for(int i = 0; i < _products.length(); i++){
            try {
                JSONObject _newproduct = _products.getJSONObject(i);

                if(i == _products.length()-1){
                    product += _newproduct.getInt("p_id");
                    values += _newproduct.getInt("p_quantity");
                }else{
                    product += _newproduct.getInt("p_id")+",";
                    values += _newproduct.getInt("p_quantity")+",";
                }
                String action = "Product ID: "+_newproduct.getInt("p_id")+
                        "\n"+"Quantity: "+_newproduct.getInt("p_quantity");
                if(_newproduct.getInt("p_id") >0) {
                    _canbuy = true;
                    _stageproducts.add(action);
                }else{
                    _temp++;
                    System.out.println(_temp+" "+_products.length());
                }
                _productadapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(_temp == _products.length())
            _canbuy=false;
    }
}
