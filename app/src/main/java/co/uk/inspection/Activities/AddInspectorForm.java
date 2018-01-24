package co.uk.inspection.Activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import co.uk.inspection.DBHelper.DbHelperClass;
import co.uk.inspection.R;

/**
 * Created by Android on 4/27/2016.
 */
public class AddInspectorForm extends AppCompatActivity {


    EditText  inspectorName,inspectorNumber , inspectorEmail;
    Button save;
    Toolbar toolbar;
    DbHelperClass myDB;
    String insName ,number , email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("tag","Oncreate Call Add Inspector FOrm");

        setContentView(R.layout.addinspectorform);
        initToolBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myDB = new DbHelperClass(this);
        inspectorName = (EditText) findViewById(R.id.editTextInspectorName);
        inspectorNumber = (EditText) findViewById(R.id.editTextInspectorNumber);
        inspectorEmail = (EditText) findViewById(R.id.editTextInpectorEmail);
        save = (Button)findViewById(R.id.buttonSaveInspector);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              saveInspectorDataToDb();
            }
        });


    }


    public void saveInspectorDataToDb() {


        insName = inspectorName.getText().toString();
        number = inspectorNumber.getText().toString();
        email = inspectorEmail.getText().toString();

        new InsertInspectorData().execute();




    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("tag", "Back from hardwaer button");
        overridePendingTransition(R.animator.bottomtotop, R.animator.bottomtotop);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.animator.bottomtotop, R.animator.bottomtotop);
                break;

            default:

                break;
        }
        return true;

    }


    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Inspector ");
        toolbar.setTitleTextColor(getResources().getColor(R.color.yellow));
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }



    class InsertInspectorData extends AsyncTask<Void,Void,Void>
    {


        @Override
        protected Void doInBackground(Void... params) {
            boolean insertInspectorData = myDB.insertInspectorData(insName, number, email);
            if(insertInspectorData == true)
            {
                Log.d("tag","Inspector Data Inserted");
            }
            else
            {
                Log.d("tag","Inspector Data Not Inserted Error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            finish();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("tag","On start Call Add Inspector Form");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("tag", "On Resume Call Add Inspector Form");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("tag", "On Pause Call Add Inspector Form");
        myDB.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("tag", "On stop Call Add Inspector Form");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("tag", "On destroy Call Add Inspector Form");
    }
}
