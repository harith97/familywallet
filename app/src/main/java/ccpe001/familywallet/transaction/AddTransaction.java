package ccpe001.familywallet.transaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ccpe001.familywallet.R;

public class AddTransaction extends AppCompatActivity {


    private TextView txtLocation,txtCategory;
    private Spinner spinCurrency, spinAccount;
    int PLACE_PICKER_REQUEST=1;
    private EditText txtAmount, txtDate, txtTime, txtTitle;
    String categoryName, categoryID, title, date, amount,currency,time,location, account,type;
    Integer currencyIndex, accountIndex;
    Context cnt = this;
    private DatabaseReference mDatabase;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtAmount =(EditText)findViewById(R.id.txtAmount);
        txtDate = (EditText) findViewById(R.id.txtDate);
        txtTime = (EditText) findViewById(R.id.txtTime);
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        spinCurrency = (Spinner) findViewById(R.id.spinCurrency);
        spinAccount = (Spinner) findViewById(R.id.spinAccount);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlacePickerActivity();
            }
        });


        if (savedInstanceState == null) {
            Bundle extras = this.getIntent().getExtras();
            if(extras == null) {
                categoryName = null;
                categoryID = null;
                title = null;
                date = null;
                amount = null;
                time = null;
                currencyIndex = null;
                accountIndex = null;
                location = null;
                type = null;

            } else {
                categoryName = extras.getString("categoryName");
                categoryID = extras.getString("categoryID");
                title = extras.getString("title");
                date = extras.getString("date");
                time = extras.getString("time");
                amount = extras.getString("amount");
                location = extras.getString("location");
                currencyIndex = extras.getInt("currencyIndex");
                accountIndex = extras.getInt("accountIndex");
                type=extras.getString("transactionType");
                txtTitle.setText(title);
                txtAmount.setText(amount);
                txtDate.setText(date);
                txtTime.setText(time);
                txtLocation.setText(location);
                spinAccount.setSelection(accountIndex);
                spinCurrency.setSelection(currencyIndex);



            }

        } else {
            categoryName = (String) savedInstanceState.getSerializable("categoryName");
            categoryID = (String) savedInstanceState.getSerializable("categoryID");
            title = (String) savedInstanceState.getSerializable("title");
            date = (String) savedInstanceState.getSerializable("date");
            amount = (String) savedInstanceState.getSerializable("amount");
            location = (String) savedInstanceState.getSerializable("location");
            currencyIndex = (Integer) savedInstanceState.getSerializable("currencyIndex");
            accountIndex = (Integer) savedInstanceState.getSerializable("accountIndex");
            type = (String) savedInstanceState.getSerializable("transactionType");


        }

        txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = txtAmount.getText().toString();
                date = txtDate.getText().toString();
                time = txtTime.getText().toString();
                title = txtTitle.getText().toString();
                currencyIndex = spinCurrency.getSelectedItemPosition();
                accountIndex = spinCurrency.getSelectedItemPosition();
                location = txtLocation.getText().toString();
                Intent intent = new Intent(AddTransaction.this,TransactionCategory.class);
                intent.putExtra("title",title);
                intent.putExtra("amount",amount);
                intent.putExtra("date",date);
                intent.putExtra("time",time);
                intent.putExtra("location",location);
                intent.putExtra("currencyIndex",currencyIndex);
                intent.putExtra("accountIndex",accountIndex);
                intent.putExtra("transactionType",type);
                startActivity(intent);
            }
        });

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                categoryName = null;
                categoryID = null;
            } else {
                categoryName = extras.getString("categoryName");
                categoryID = extras.getString("categoryID");
            }
        } else {
            categoryName = (String) savedInstanceState.getSerializable("categoryName");
            categoryID = (String) savedInstanceState.getSerializable("categoryID");
        }

        if (categoryName!=null){
            txtCategory.setText(categoryName);
        }


        txtAmount.setFilters(new InputFilter[] {new CurrencyFormatInputFilter()});

        if (date==null){
            long Cdate = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            String dateString = sdf.format(Cdate);
            txtDate.setText(dateString);
        }
        if (time==null){
            long Cdate = System.currentTimeMillis();
            SimpleDateFormat stf = new SimpleDateFormat("h:mm a");
            String timeString = stf.format(Cdate);
            txtTime.setText(timeString);
        }
        if (type!=null){
            getSupportActionBar().setTitle(type);
            if (type.equals("Income")){

                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.income)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.income));
                }
            }
            else if(type.equals("Expense")){
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.expense)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.expense));
                }
            }
        }
    }



    private void startPlacePickerActivity() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();

        try {
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displaySelectedPlaceFromPlacePicker(Intent data) {
        Place placeSelected = PlacePicker.getPlace(data, this);

        String name = placeSelected.getName().toString();
        String address = placeSelected.getAddress().toString();

        //TextView enterCurrentLocation = (TextView) findViewById(R.id.txtLocation);
        txtLocation.setText(name + ", " + address);
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        txtCategory = (TextView)findViewById(R.id.txtCategory);
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            displaySelectedPlaceFromPlacePicker(data);
        }

    }

    public class CurrencyFormatInputFilter implements InputFilter {

        Pattern mPattern = Pattern.compile("(0|[1-9]+[0-9]*)?(\\.[0-9]{0,2})?");

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {

            String result =
                    dest.subSequence(0, dstart) + source.toString() + dest.subSequence(dend, dest.length());

            Matcher matcher = mPattern.matcher(result);

            if (!matcher.matches()) return dest.subSequence(dstart, dend);

            return null;
        }
    }



    public void onStart(){
        super.onStart();
        TextView txtDate = (TextView) findViewById(R.id.txtDate);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateDialog dialog = new DateDialog(v);
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft,"DatePicker");

            }
        });

        TextView txtTime = (TextView) findViewById(R.id.txtTime);
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeDialog dialog = new TimeDialog(v);
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft,"TimePicker");

            }
        });



    }



    public void saveTransaction(View view) {

        TransactionDetails td;
        amount = txtAmount.getText().toString();
        date = txtDate.getText().toString();
        time = txtTime.getText().toString();
        title = txtTitle.getText().toString();
        currency = spinCurrency.getSelectedItem().toString();
        location = txtLocation.getText().toString();
        account = spinAccount.getSelectedItem().toString();

        if (categoryName==null){
            categoryName = "Other";
            categoryID = Integer.toString(R.drawable.cat_other);
        }

        if (amount.isEmpty()) {
            Toast.makeText(this, "Set the Amount first", Toast.LENGTH_SHORT).show();
        }
        else {
            //DatabaseOps dbOp = new DatabaseOps(cnt);
            //dbOp.addData(amount, title, categoryName, date, Integer.parseInt(categoryID), time, account, location, type, currency, "uID");
            td = new TransactionDetails("uid",amount, title, categoryName, date, Integer.parseInt(categoryID), time, account, location, type, currency);
            mDatabase.child("Transactions").push().setValue(td);
            //Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
            Intent intent = new Intent("ccpe001.familywallet.DASHBOARD");
            startActivity(intent);
        }
    }



}
