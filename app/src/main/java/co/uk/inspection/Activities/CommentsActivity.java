package co.uk.inspection.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import co.uk.inspection.DBHelper.DbHelperClass;
import co.uk.inspection.R;

/**
 * Created by Android on 3/30/2016.
 */

public class CommentsActivity extends AppCompatActivity {

    EditText edtComment;
    Button save;
    Toolbar toolbar;
    int pos,areaId,inspectionId,ascId,propertyId;
    String areaName;
    static DbHelperClass myDb;
    String asc_name =null;
    String commentFromDb ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(co.uk.inspection.R.layout.addcomment);
        initToolBar();
        // setup back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        myDb = new DbHelperClass(getApplicationContext());
        pos = i.getIntExtra("position",-1);
        ascId = i.getIntExtra("id",0);
        areaId = i.getIntExtra("areaId",0);
        inspectionId = i.getIntExtra("inspectionId", 0);
        asc_name = i.getStringExtra("ascName");
        propertyId = i.getIntExtra("propertyId", 0);
        areaName = i.getStringExtra("areaName");

        Log.d("tag","Data in Comments Activity Position  is "+pos+"Acs Id is "+ascId+"Area id is"+areaId+"Inspection Id is "+inspectionId+"Property Id is"+propertyId);

        edtComment = (EditText) findViewById(co.uk.inspection.R.id.edittextComment);
        save = (Button) findViewById(co.uk.inspection.R.id.buttonSaveComment);

        final long area_data_id = myDb.getAreaDataId(areaId,inspectionId,propertyId,areaName);
        Log.d("tag","Area Data Id in Comment Activity is "+area_data_id);


          commentFromDb =  myDb.getselectedAscComment(ascId,area_data_id,asc_name);


        // check if comments are null for first time
        if(commentFromDb != null && !commentFromDb.isEmpty())
        {
            edtComment.setText(commentFromDb);
            // set the cursor to after text
            edtComment.setSelection(edtComment.getText().length());

        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d("tag", "area_id is " + areaId + "Inspection id is " + inspectionId);

                // check if the id is exist in the table or not
                long asc_data_id = myDb.checkselectedAreaDataId(area_data_id, ascId);
                Log.d("tag", "Accessory data id is " + asc_data_id);

                if (asc_data_id == 0l) {
                    Log.d("tag", "Value doesnot exist insert a value");

                    boolean isInserted = myDb.insertAccessoryDataComments(ascId, asc_name, area_data_id, edtComment.getText().toString());
                    if (isInserted) {
                        Log.d("tag", "Data is inserted");
                    }
                } else {
                    Log.d("tag", "Update the value ");
                    // update accessory data table
                    Log.d("tag", "Area Data id is exist updating data");
                    int rowsUpdated = myDb.updateAccessoryDataComments(ascId, asc_name, area_data_id, edtComment.getText().toString());
                    Log.d("tag", "Rows updated is " + rowsUpdated);

                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("position", pos);
                setResult(7, resultIntent);
                finish();
                overridePendingTransition(co.uk.inspection.R.animator.backright, co.uk.inspection.R.animator.backin);

            }
        });


    }


    public void initToolBar() {
        toolbar = (Toolbar) findViewById(co.uk.inspection.R.id.toolbar);
        toolbar.setTitle("Add Comment");
        toolbar.setTitleTextColor(getResources().getColor(co.uk.inspection.R.color.yellow));
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(co.uk.inspection.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(co.uk.inspection.R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(co.uk.inspection.R.menu.main, menu);

        // disable the menu item
        menu.findItem(co.uk.inspection.R.id.action_addFutureInspection).setVisible(false);
        menu.findItem(co.uk.inspection.R.id.action_addInspection).setVisible(false);
        menu.findItem(co.uk.inspection.R.id.search).setVisible(false);
        menu.findItem(co.uk.inspection.R.id.action_addArea).setVisible(false);
        menu.findItem(co.uk.inspection.R.id.action_createReport).setVisible(false);
        menu.findItem(R.id.action_ShowImages).setVisible(false);
        menu.findItem(co.uk.inspection.R.id.delete).setVisible(false);
        menu.findItem(R.id.action_addSection).setVisible(false);
        menu.findItem(R.id.notes).setVisible(false);
        menu.findItem(R.id.rotateImage).setVisible(false);





        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(co.uk.inspection.R.animator.backright, co.uk.inspection.R.animator.backin);
                return true;
            case co.uk.inspection.R.id.action_addArea:

                //      showInputDialog();
                return  true;

            default:
                return super.onOptionsItemSelected(item);
        }



    }


    @Override
    protected void onPause() {
        super.onPause();
        myDb.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("tag", "Back from hardwaer button");
        overridePendingTransition(co.uk.inspection.R.animator.backright, co.uk.inspection.R.animator.backin);
    }
}
