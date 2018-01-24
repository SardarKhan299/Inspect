package co.uk.inspection.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.uk.inspection.DBHelper.DbHelperClass;
import co.uk.inspection.R;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by Android on 5/5/2016.
 */
public class AddAscForm extends AppCompatActivity {

    Toolbar toolbar;
    static DbHelperClass myDB;
    String ascName;
    long area_data_id;
    Button saveAscName;
    EditText ascNameToSave;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addareaview);
        initToolBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myDB = new DbHelperClass(this);

        Intent i = getIntent();
        area_data_id = i.getLongExtra("area_data_id", 0l);


        saveAscName = (Button) findViewById(R.id.buttonSaveAreaName);
        ascNameToSave = (EditText) findViewById(R.id.editTextAreaName);


        Log.d("tag", "On Create Of Add Accessory Form ");
        Log.d("tag", "Area Data Id  is " + area_data_id  );

        saveAscName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if name already Exist

                ascName = ascNameToSave.getText().toString();
                ascName = WordUtils.capitalizeFully(ascName);

                Long asc_data_id = myDB.checkDuplicateAscName(ascName,area_data_id);

                if (asc_data_id == 0l) {
                    Log.d("tag", "Check for Common Table Asc ");
                    Long asc_id = myDB.checkCommonAscName(ascName);
                    if (asc_id == 0l) {
                        Log.d("tag", "Add New Asc Name ");
                        Long insertedAreaId = myDB.insertInspectionAscData(51, area_data_id , ascName );
                        Log.d("tag", "Inserted Asc Id in Areas is " + insertedAreaId);
                        setResult(RESULT_OK);
                        finish();
                        overridePendingTransition(R.animator.backright, R.animator.backin);

                    } else {
                        Log.d("tag", "Id Already Exist in Common Asc ");
                        Toast.makeText(getApplicationContext(), "Name Already exist", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d("tag", "Id Already Exist in Your Table Area Name");
                    Toast.makeText(getApplicationContext(), "Name Already exist", Toast.LENGTH_SHORT).show();

                }


            }
        });




    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("tag", "Back from hardwaer button");
        overridePendingTransition(R.animator.backright, R.animator.backin);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.animator.backright, R.animator.backin);
                break;

            default:

                break;
        }
        return true;

    }


    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add New Area ");
        toolbar.setTitleTextColor(getResources().getColor(R.color.yellow));
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }




    @Override
    protected void onPause() {
        super.onPause();
        myDB.close();
    }
}
