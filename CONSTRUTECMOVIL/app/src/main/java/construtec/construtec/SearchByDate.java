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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Isaac on 10/11/2016.
 */
public class SearchByDate extends Fragment {
    View _assignStage;
    List<JSONObject> _jsonList;
    List<String> _stages;
    List<String> _comments;

    Integer _position =0;

    ArrayAdapter<String> _stagesadapter;
    ArrayAdapter<String> _commentsadapter;

    Spinner _stagespinner;
    Spinner _stagecomments;

    TextView _owner;
    TextView _ownerphone;
    TextView _projectlocation;
    TextView _startdate;

    Button _addcomment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _assignStage = inflater.inflate(R.layout.search_date,container,false);
        _stagespinner = (Spinner) _assignStage.findViewById(R.id._stagessearch);
        _stagecomments = (Spinner) _assignStage.findViewById(R.id._stagecomments);
        _owner = (TextView) _assignStage.findViewById(R.id._owner);
        _ownerphone = (TextView) _assignStage.findViewById(R.id._ownerphone);
        _projectlocation = (TextView) _assignStage.findViewById(R.id._projectlocation);
        _startdate = (TextView) _assignStage.findViewById(R.id._startdate);
        _addcomment = (Button) _assignStage.findViewById(R.id._addcomment);
        getDataByDate();


        _stagesadapter = new ArrayAdapter<String>(_assignStage.getContext(),
                android.R.layout.simple_spinner_dropdown_item,_stages);
        _stagespinner.setAdapter(_stagesadapter);
        _stagespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    _position = position;
                    _owner.setText(_jsonList.get(position).getString("gcs_uname").toString());
                    _ownerphone.setText(("" + _jsonList.get(position).getInt("gcs_uphone")));
                    _projectlocation.setText(_jsonList.get(position).getString("gcs_plocation").toString());
                    _startdate.setText(_jsonList.get(position).getString("gcs_datestart").toString());
                    getComments(_jsonList.get(position).getString("gcs_sname").toString(), _jsonList.get(position).getInt("gcs_pid"),
                            _jsonList.get(position).getInt("gcs_uid"));
                    _commentsadapter = new ArrayAdapter<String>(_assignStage.getContext(),
                            android.R.layout.simple_spinner_dropdown_item, _comments);
                    _stagecomments.setAdapter(_commentsadapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        _stagecomments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder _commentview = new AlertDialog.Builder(_assignStage.getContext());
                final EditText _comment = new EditText(_assignStage.getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                _comment.setLayoutParams(lp);
                _commentview.setView(_comment);
                _commentview.setTitle("Comment");
                _commentview.setMessage("Write a new comment");
                _commentview.setCancelable(false)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (_comment.getText().toString().matches("")) {
                                    Toast.makeText(_commentview.getContext(), "You can't add an empty comment", Toast.LENGTH_LONG).show();
                                } else {
                                    sendComment(_position, _comment.getText().toString());
                                    try {
                                        getComments(_jsonList.get(_position).getString("gcs_sname").toString(), _jsonList.get(_position).getInt("gcs_pid"),
                                                _jsonList.get(_position).getInt("gcs_uid"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                _commentview.show();
            }
        });

        return _assignStage;
    }

    public void getDataByDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        String server = getString(R.string.url)+"generaluser/get/p_id/"+date;
        System.out.println(server);
        _jsonList = new ArrayList<>();
        _stages = new ArrayList<>();
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(server, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        if (!response.getJSONObject(i).getString("gcs_status").matches("Terminada")) {
                            _jsonList.add(response.getJSONObject(i));
                            _stages.add(response.getJSONObject(i).getString("gcs_sname"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                _stagesadapter.notifyDataSetChanged();
            }
        });
    }

    public void getComments(String s_name, Integer p_id, Integer u_id){
        String server = getString(R.string.url)+"comment/get/s_name,p_id,u_id/"+
                "'"+s_name+"'"+","+p_id+","+u_id;
        AsyncHttpClient httpClient = new AsyncHttpClient();
        _comments = new ArrayList<>();
        httpClient.get(server, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if(response.length()==0){
                    Snackbar.make(_assignStage,"This stage does not have any comment. \n Add one if you want!",Snackbar.LENGTH_LONG).show();
                    _stagecomments.setVisibility(View.INVISIBLE);
                }else {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            _comments.add(response.getJSONObject(i).getString("c_description"));
                            System.out.println(response.getJSONObject(i).getString("c_description"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                _commentsadapter.notifyDataSetChanged();
            }
        });
    }

    private void sendComment(int position, String message){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        String server = getString(R.string.url)+"comment/post";
        try{
            JSONObject _new = new JSONObject();
            _new.put("c_description",message);
            _new.put("s_name",_jsonList.get(position).getString("gcs_sname"));
            _new.put("p_id",_jsonList.get(position).getInt("gcs_pid"));
            _new.put("u_id",_jsonList.get(position).getInt("gcs_uid"));
            System.out.println(_new.toString());
            StringEntity se = new StringEntity(_new.toString());
            httpClient.post(_assignStage.getContext(),server,se,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Snackbar.make(_assignStage,"Your comment has been added",Snackbar.LENGTH_LONG).show();
                }
            });
        }catch (JSONException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
