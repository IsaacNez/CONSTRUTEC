package construtec.construtec;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Isaac on 10/10/2016.
 */
public class RegistrateCustomer extends Fragment {
    View myView;
    Button RegisterE;
    /*
    Variables that relate to the data introduced by the customer
     */
    EditText E_ID;
    EditText E_Name;
    EditText E_LName;
    EditText E_Phone;
    EditText E_Pass;
    EditText E_PassCheck;
    /*

     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.registratecustomer,container,false);
        RegisterE = (Button) myView.findViewById(R.id.CuRegisterE);
        E_ID = (EditText) myView.findViewById(R.id.Cu_ID);
        E_Name = (EditText) myView.findViewById(R.id.Cu_Name);
        E_LName = (EditText) myView.findViewById(R.id.Cu_LName);
        E_Phone = (EditText) myView.findViewById(R.id.Cu_Phone);
        E_Pass = (EditText) myView.findViewById(R.id.Cupassword);
        E_PassCheck = (EditText) myView.findViewById(R.id.Cupasswordcheck);
        RegisterE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckData()){
                    Snackbar.make(myView, "Your data is incorrect, please try again.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    //sendInfo();
                    Intent _return = new Intent(getActivity(),LoginActivity.class);
                    startActivity(_return);
                }

            }
        });
        return myView;
    }
    private boolean CheckData(){

        if(!E_ID.getText().toString().matches("[0-9]+"))
            return false;
        else if (!E_Name.getText().toString().matches("[a-zA-Z]+")| !E_LName.getText().toString().matches("[a-zA-Z]+"))
            return false;
        else if (!E_Phone.getText().toString().matches("[0-9]+"))
            return false;
        else if (!E_Pass.getText().toString().contentEquals(E_PassCheck.getText().toString()))
            return false;
        else
            return true;
    }
    private void sendInfo(){
        String server = "";
        HttpClient newEngineer = new DefaultHttpClient();
        HttpPost toSend = new HttpPost(server);
        String json = "";
        JSONObject E_Json = new JSONObject();
        try{
            E_Json.accumulate("U_ID",E_ID.getText().toString());
            E_Json.accumulate("U_Name",E_Name.getText().toString());
            E_Json.accumulate("U_LName",E_LName.getText().toString());
            E_Json.accumulate("U_Phone",E_Phone.getText().toString());
            E_Json.accumulate("U_Password",E_Pass.getText().toString());
            E_Json.accumulate("Role",3);

            json = E_Json.toString();
            StringEntity ent = new StringEntity(json);
            toSend.setEntity(ent);
            toSend.setHeader("Accept", "application/json");
            toSend.setHeader("Content-type", "application/json");
            newEngineer.execute(toSend);
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
