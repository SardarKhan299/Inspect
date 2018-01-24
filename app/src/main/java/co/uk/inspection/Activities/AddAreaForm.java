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
 * Created by Android on 5/2/2016.
 */



public class AddAreaForm extends AppCompatActivity {

    Toolbar toolbar;
    static DbHelperClass myDB;
    String areaName,area_name_selected;
    int inspectionId,propertyId,areaId,duplicateAreaSelected;
    Button saveAreaName;
    EditText areaNameToSave;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addareaview);
        initToolBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myDB = new DbHelperClass(this);

        Intent i = getIntent();
        inspectionId = i.getIntExtra("inspectionId", 0);
        propertyId = i.getIntExtra("propertyId", 0);
        areaId = i.getIntExtra("areaId",0);
        area_name_selected = i.getStringExtra("areaName");
        duplicateAreaSelected = i.getIntExtra("duplicate",0);

        saveAreaName = (Button) findViewById(R.id.buttonSaveAreaName);
        areaNameToSave = (EditText) findViewById(R.id.editTextAreaName);


        Log.d("tag", "Inspection Id is " + inspectionId + "Property id is " + propertyId);

        saveAreaName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (duplicateAreaSelected == 0)
                {
                    // for Blank New Activity

                    areaName = areaNameToSave.getText().toString();
                areaName = WordUtils.capitalizeFully(areaName);
                Long area_data_id = myDB.checkDuplicateAreaName(areaName, propertyId, inspectionId);
                if (area_data_id == 0l) {
                    Log.d("tag", "Check for Common Table ");
                    Long area_id = myDB.checkCommonAreaName(areaName);
                    if (area_id == 0l) {
                        Log.d("tag", "Add New Area Name");

                        int sortOrder = myDB.getMaxSortOrder();
                        sortOrder +=1;
                        Log.d("tag","new Sort order is "+sortOrder);

                        Long insertedAreaId = myDB.insertInspectionAreaDataForDuplicate(10, inspectionId, propertyId, areaName,sortOrder);
                        Log.d("tag", "Inserted Area Id in Areas is " + insertedAreaId);
                        setResult(RESULT_OK);
                        finish();
                        overridePendingTransition(R.animator.backright, R.animator.backin);

                    } else {
                        Log.d("tag", "Id Already Exist in Common Areas");
                        Toast.makeText(getApplicationContext(), "Name Already exist", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d("tag", "Id Already Exist in Your Table Area Name");
                    Toast.makeText(getApplicationContext(), "Name Already exist", Toast.LENGTH_SHORT).show();

                }

            }

                else if(duplicateAreaSelected == 1)
                {
                    Log.d("tag","Duplicated Selected");


                    areaName = areaNameToSave.getText().toString();
                    areaName = WordUtils.capitalizeFully(areaName);
                    Long area_data_id = myDB.checkDuplicateAreaName(areaName, propertyId, inspectionId);
                    if (area_data_id == 0l) {
                        Log.d("tag", "Check for Common Table ");
                        Long area_id = myDB.checkCommonAreaName(areaName);
                        if (area_id == 0l) {
                            Log.d("tag", "Add New Area Name");
                            int sortOrder = myDB.getMaxSortOrder();
                            sortOrder +=1;
                            Log.d("tag","new Sort order is "+sortOrder);

                            Long insertedAreaId = myDB.insertInspectionAreaDataForDuplicate(areaId, inspectionId, propertyId, areaName,sortOrder);
                            Log.d("tag", "Inserted Area Id in Areas is " + insertedAreaId);
                            setResult(RESULT_OK);
                            finish();
                            overridePendingTransition(R.animator.backright, R.animator.backin);

                        } else {
                            Log.d("tag", "Id Already Exist in Common Areas");
                            Toast.makeText(getApplicationContext(), "Name Already exist", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.d("tag", "Id Already Exist in Your Table Area Name");
                        Toast.makeText(getApplicationContext(), "Name Already exist", Toast.LENGTH_SHORT).show();

                    }
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
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        myDB.close();
    }


}
