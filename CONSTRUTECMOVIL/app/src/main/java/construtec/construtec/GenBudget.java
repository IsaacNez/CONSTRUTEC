package construtec.construtec;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Isaac on 10/11/2016.
 */
public class GenBudget extends Fragment {
    View _assignStage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _assignStage = inflater.inflate(R.layout.gen_budget,container,false);
        return _assignStage;
    }
}
