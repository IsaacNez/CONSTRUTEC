package construtec.construtec;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Isaac on 10/10/2016.
 */
public class FirstFragment extends Fragment {
    /**
     * This fragment loads the products from EPATEC to CONSTRUTEC
     */
    View myView;
    Button _callEPATEC;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.first_layout,container,false);
        _callEPATEC = (Button) myView.findViewById(R.id._callEPATEC);
        _callEPATEC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient httpClient = new AsyncHttpClient();
                String server = getString(R.string.url)+"admin/get/p_id/undefined";
                httpClient.get(server, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Snackbar.make(myView, "There is an error with your network", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Snackbar.make(myView, "You have updated the database successfuly", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        });

        return myView;
    }
}
