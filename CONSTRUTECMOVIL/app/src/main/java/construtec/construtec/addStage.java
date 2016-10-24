package construtec.construtec;

import android.app.Activity;
import android.app.Fragment;
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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Isaac on 10/22/2016.
 */
public class addStage extends Activity {
    List<String> _stages = new ArrayList<>();
    ListView _stageschoice;
    Integer _pid =0;
    String _stage = "";
    Button _send;
    EditText _stageSD;
    EditText _stageDE;

    Integer u_id = 0;
    Integer u_code =0;
    String u_name = "";
    String jsonArray = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_stage);
        Intent intento = getIntent();
        getStages();
        _pid = intento.getIntExtra("p_id",0);
        u_id = intento.getIntExtra("u_id",0);
        u_code = intento.getIntExtra("u_code",0);
        u_name = intento.getStringExtra("u_name");
        jsonArray = intento.getStringExtra("jsonArray");
        _stageschoice = (ListView) findViewById(R.id._stagespinner);
        _send = (Button) findViewById(R.id._addStageButton);
        _stageSD = (EditText) findViewById(R.id._stageSD);
        _stageDE = (EditText) findViewById(R.id._stageDE);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getBaseContext(),
                android.R.layout.simple_list_item_1,_stages);
        _stageschoice.setAdapter(arrayAdapter);
        _stageschoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "Se ha escogido: " + parent.getItemAtPosition(position), Snackbar.LENGTH_LONG).show();
                _stage = parent.getItemAtPosition(position).toString();
            }
        });

        _send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(_stageSD.getText().toString().matches("")|_stageDE.getText().toString().matches("")|_stage.matches("")){
                    Snackbar.make(v,"There is an error in your data",Snackbar.LENGTH_LONG).show();
                }else {
                    AsyncHttpClient httpClient = new AsyncHttpClient();
                    String server = R.string.url+"api/Projectxstage/post";
                    try {
                        JSONObject params = new JSONObject();
                        params.put("p_id", _pid);
                        params.put("s_name", _stage);
                        params.put("pxs_datestart", _stageSD.getText().toString());
                        params.put("pxs_dateend", _stageDE.getText().toString());
                        params.put("pxs_status", "Construyendo");
                        StringEntity se = new StringEntity(params.toString());
                        httpClient.post(getApplicationContext(),server,se,"application/json", new JsonHttpResponseHandler(){
                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                System.out.println(errorResponse.toString());
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Snackbar.make(v,"Your stage has been added",Snackbar.LENGTH_LONG).show();
                                Intent newIntent = new Intent(addStage.this,UserInterface.class);
                                newIntent.putExtra("UID",u_id)
                                        .putExtra("UCode",u_code)
                                        .putExtra("Name",u_name)
                                        .putExtra("jsonArray",jsonArray);
                                startActivity(newIntent);
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



    }

    public List<String> getStages(){
        final AsyncHttpClient httpClient = new AsyncHttpClient();
        String server = "http://isaac:7249/api/stage/get/p_id/undefined";
        final List<String> _temp = new ArrayList<>();
        httpClient.get(server, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject _newJSON = response.getJSONObject(i);
                        _temp.add(_newJSON.getString("s_name"));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        _stages = _temp;
        return _temp;
    }

}
