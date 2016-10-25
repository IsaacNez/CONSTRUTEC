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
public class SearchByProduct extends Fragment {
    View _assignStage;

    EditText _productsearch;

    Button _search;
    Button _terminate;
    Button _addcomment;

    Spinner _sxpspinner;
    Spinner _sxpcomments;

    TextView _sxpname;
    TextView _sxpphone;
    TextView _sxplocation;
    TextView _sxpdate;

    ArrayAdapter<String> _sxpstagesadapter;
    ArrayAdapter<String> _sxpcommentadapter;

    List<JSONObject> _stageslist;
    List<String> _stagesname;
    List<String> _commentslist;

    Integer _position = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _assignStage = inflater.inflate(R.layout.search_product,container,false);

        _productsearch = (EditText) _assignStage.findViewById(R.id._productspecification);

        _sxpname = (TextView) _assignStage.findViewById(R.id._sxpname);
        _sxpphone = (TextView) _assignStage.findViewById(R.id._sxpphone);
        _sxplocation = (TextView) _assignStage.findViewById(R.id._sxplocation);
        _sxpdate = (TextView) _assignStage.findViewById(R.id._sxpdate);

        _sxpname.setVisibility(View.INVISIBLE);
        _sxpphone.setVisibility(View.INVISIBLE);
        _sxplocation.setVisibility(View.INVISIBLE);
        _sxpdate.setVisibility(View.INVISIBLE);

        _search = (Button) _assignStage.findViewById(R.id._searchaction);
        _addcomment = (Button) _assignStage.findViewById(R.id._sxpadd);
        _terminate = (Button) _assignStage.findViewById(R.id._sxpfin);

        _sxpspinner = (Spinner) _assignStage.findViewById(R.id._sxpspinner);
        _sxpcomments = (Spinner) _assignStage.findViewById(R.id._sxpcomments);
        _sxpspinner.setVisibility(View.INVISIBLE);
        _sxpcomments.setVisibility(View.INVISIBLE);

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
                                        getComments(_stageslist.get(_position).getString("gcs_sname").toString(),
                                                _stageslist.get(_position).getInt("gcs_pid"),
                                                _stageslist.get(_position).getInt("gcs_uid"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                _commentview.show();
            }
        });

        _sxpspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    _position = position;
                    _sxpname.setText(_stageslist.get(position).getString("gcs_uname").toString());
                    _sxpphone.setText(("" + _stageslist.get(position).getInt("gcs_uphone")));
                    _sxplocation.setText(_stageslist.get(position).getString("gcs_plocation").toString());
                    _sxpdate.setText(_stageslist.get(position).getString("gcs_datestart").toString());
                    getComments(_stageslist.get(position).getString("gcs_sname").toString(), _stageslist.get(position).getInt("gcs_pid"),
                            _stageslist.get(position).getInt("gcs_uid"));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_productsearch.getText().toString().matches("")){
                    Snackbar.make(_assignStage,"Please specify information to make the search",Snackbar.LENGTH_LONG).show();
                }else{
                    getStages(_productsearch.getText().toString());
                }
            }
        });
        return _assignStage;
    }

    private void getStages(String product){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        String server = getString(R.string.url)+"Costumerserviceprod/get/date,p_id/"+date+","+product;

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(server, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try{
                    if(response.length()==0){
                        Snackbar.make(_assignStage,"There isn't any stages that matches your search",Snackbar.LENGTH_LONG).show();
                        _addcomment.setVisibility(View.INVISIBLE);
                        _terminate.setVisibility(View.INVISIBLE);
                    }else {
                        _sxpname.setVisibility(View.VISIBLE);
                        _sxpphone.setVisibility(View.VISIBLE);
                        _sxplocation.setVisibility(View.VISIBLE);
                        _sxpdate.setVisibility(View.VISIBLE);
                        _sxpspinner.setVisibility(View.VISIBLE);
                        _stageslist = new ArrayList<JSONObject>();
                        _stagesname = new ArrayList<String>();
                        _sxpstagesadapter = new ArrayAdapter<String>(_assignStage.getContext(),
                                android.R.layout.simple_spinner_dropdown_item,_stagesname);
                        _sxpspinner.setAdapter(_sxpstagesadapter);
                        _sxpstagesadapter.notifyDataSetChanged();
                        for(int i = 0; i < response.length(); i++){
                            if (response.getJSONObject(i).getString("gcs_status").matches("Terminada")) {
                                _stageslist.add(response.getJSONObject(i));
                                _stagesname.add(response.getJSONObject(i).getString("gcs_sname"));
                            }
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });
    }

    public void getComments(String s_name, Integer p_id, Integer u_id){
        String server = getString(R.string.url)+"comment/get/s_name,p_id,u_id/"+
                "'"+s_name+"'"+","+p_id+","+u_id;
        AsyncHttpClient httpClient = new AsyncHttpClient();
        _commentslist = new ArrayList<>();
        httpClient.get(server, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if(response.length()==0){
                    Snackbar.make(_assignStage,"This stage does not have any comment. \n Add one if you want!",Snackbar.LENGTH_LONG).show();
                    _sxpcomments.setVisibility(View.INVISIBLE);
                }else {
                    try {
                        _sxpcomments.setVisibility(View.VISIBLE);
                        _commentslist = new ArrayList<String>();
                        for (int i = 0; i < response.length(); i++) {
                            _commentslist.add(response.getJSONObject(i).getString("c_description"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                _sxpcommentadapter = new ArrayAdapter<String>(_assignStage.getContext(),
                        android.R.layout.simple_spinner_dropdown_item,_commentslist);
                _sxpcomments.setAdapter(_sxpcommentadapter);
                _sxpcommentadapter.notifyDataSetChanged();
            }
        });
    }

    private void sendComment(int position, String message){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        String server = getString(R.string.url)+"comment/post";
        try{
            JSONObject _new = new JSONObject();
            _new.put("c_description",message);
            _new.put("s_name", _stageslist.get(position).getString("gcs_sname"));
            _new.put("p_id",_stageslist.get(position).getInt("gcs_pid"));
            _new.put("u_id",_stageslist.get(position).getInt("gcs_uid"));
            System.out.println(_new.toString());
            StringEntity se = new StringEntity(_new.toString());
            httpClient.post(_assignStage.getContext(),server,se,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Snackbar.make(_assignStage,"It is an error with your transaction. \n Try it in a few minutes",Snackbar.LENGTH_LONG).show();
                }

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
