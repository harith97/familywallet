package ccpe001.familywallet.budget;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

import ccpe001.familywallet.R;

public class budgetUpdate extends AppCompatActivity implements View.OnClickListener  {
    EditText strDt,endDt;
    private  int day,mon,yr;
    private String[] arraySpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.arraySpinner = new String[] {
                "Food", "Travel"
        };
        setContentView(R.layout.budget_update);
        strDt=(EditText)findViewById(R.id.startDate);
        endDt=(EditText)findViewById(R.id.endDate);
        strDt.setOnClickListener(this);
        endDt.setOnClickListener(this);
        Spinner s = (Spinner) findViewById(R.id.catSelect);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, arraySpinner);
        s.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v==strDt){
            final Calendar c= Calendar.getInstance();
            day=c.get(Calendar.DAY_OF_MONTH);
            mon=c.get(Calendar.MONTH);
            yr=c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    strDt.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                }
            }
                    ,day,mon,yr);
            datePickerDialog.show();
        }
        if (v==endDt){
            final Calendar c= Calendar.getInstance();
            day=c.get(Calendar.DAY_OF_MONTH);
            mon=c.get(Calendar.MONTH);
            yr=c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    endDt.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                }
            },day,mon,yr);
            datePickerDialog.show();
        }
    }
    public void  toForecast(View view){
        Intent newInt1 = new Intent(this,forecast.class);
        startActivity(newInt1);
    }

}
