package construtec.construtec;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Isaac on 10/10/2016.
 */
public class FirstFragment extends Fragment {
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
                Snackbar.make(myView,"You have updated the database successfuly",Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();
            }
        });

        return myView;
    }
}
