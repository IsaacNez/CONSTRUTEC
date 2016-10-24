package construtec.construtec;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _assignStage = inflater.inflate(R.layout.search_product,container,false);

        _productsearch = (EditText) _assignStage.findViewById(R.id._productspecification);

        _search = (Button) _assignStage.findViewById(R.id._searchaction);


        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_productsearch.getText().toString().matches("")){
                    Snackbar.make(_assignStage,"Please specify information to make the search",Snackbar.LENGTH_LONG).show();
                }else{

                }
            }
        });
        return _assignStage;
    }

    private void getStages(String product){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        String server = getString(R.string.url)+"Costumerserviceprod/get/date,p_id/"+date+","+"'"+product+"'";

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(server, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try{
                    if(response.length()==0){
                        Snackbar.make(_assignStage,"There isn't any stage that matches your search",Snackbar.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
