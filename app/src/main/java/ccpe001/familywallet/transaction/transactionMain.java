package ccpe001.familywallet.transaction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ccpe001.familywallet.R;

/**
 * Created by Knight on 4/23/2017.
 */

public class transactionMain extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_transaction, container, false);
    }
}
