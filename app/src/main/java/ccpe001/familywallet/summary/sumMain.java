package ccpe001.familywallet.summary;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;*/

import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;
import ccpe001.familywallet.R;

public class sumMain extends Fragment {


    //meeka athule charts tika karapn gihan, navigation drawer eka 'Reports' walin meekaai display weene
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Toast.makeText(getContext(),"For Reports",Toast.LENGTH_LONG).show();

        return inflater.inflate(R.layout.sum_main, container, false);


    }



}
