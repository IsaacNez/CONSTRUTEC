package construtec.construtec;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Isaac on 10/10/2016.
 */
public class SecondFragment extends Fragment {
    View myView;
    Button _createStage;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.second_layout,container,false);
        _createStage = (Button) myView.findViewById(R.id.create);
        _createStage.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "You have create a stage successfuly", Toast.LENGTH_LONG).show();
                Intent _return = new Intent(getActivity(),UserInterface.class);
                startActivity(_return);
            }
        });

        return myView;
    }
}
