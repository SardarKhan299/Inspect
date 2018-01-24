package co.uk.inspection.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import co.uk.inspection.CustomAdapters.CustomAdapterShowAllAreasToDuplicate;
import co.uk.inspection.DBHelper.DbHelperClass;
import co.uk.inspection.TableClasses.Areas;

import java.util.ArrayList;

/**
 * Created by Android on 5/3/2016.
 */


public class AddAreaList extends AppCompatActivity {

    Toolbar toolbar;
    ListView showAllAreas;
    static DbHelperClass myDb;
    ArrayList<Areas> listOfAreas = new ArrayList<Areas>();
    CustomAdapterShowAllAreasToDuplicate adapter;
    int inspectionId, propertyId;
    TextView blankSection;
    public static final int REQUEST_CODE_AREA = 123;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(co.uk.inspection.R.layout.addarealist);
        initToolBar();
        myDb = new DbHelperClass(this);
        Intent i = getIntent();
        inspectionId = i.getIntExtra("inspectionId", 0);
        propertyId = i.getIntExtra("propertyId", 0);


        showAllAreas = (ListView) findViewById(co.uk.inspection.R.id.listViewShowAllAreas);
        blankSection = (TextView) findViewById(co.uk.inspection.R.id.textviewNewBlankArea);



        // setup back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // get all areas from db
        new GetAllAreasToDuplicate().execute();

        Log.d("tag", "Inspection Id is " + inspectionId + "Property id is " + propertyId);


        blankSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag","New Section Click");
                // Add Area
                Intent i = new Intent(AddAreaList.this,AddAreaForm.class);
                i.putExtra("propertyId",propertyId);
                i.putExtra("inspectionId",inspectionId);
                startActivityForResult(i,REQUEST_CODE_AREA);
                overridePendingTransition(co.uk.inspection.R.animator.righttoleft, co.uk.inspection.R.animator.lefttoright);
            }
        });

        showAllAreas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Areas area = (Areas) showAllAreas.getItemAtPosition(position);
                Log.d("tag","Area Name is "+area.getName()+"Area id is "+area.getId());

                Intent i = new Intent(AddAreaList.this,AddAreaForm.class);
                i.putExtra("areaName", area.getName());
                i.putExtra("position", position);
                i.putExtra("inspectionId", inspectionId);
                i.putExtra("areaId", area.getId());
                i.putExtra("propertyId", propertyId);
                i.putExtra("duplicate",1);
                startActivityForResult(i, REQUEST_CODE_AREA);
                overridePendingTransition(co.uk.inspection.R.animator.righttoleft, co.uk.inspection.R.animator.lefttoright);


            }

        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("tag", "Back from hardwaer button");
        overridePendingTransition(co.uk.inspection.R.animator.bottomtotop, co.uk.inspection.R.animator.bottomtotop);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(co.uk.inspection.R.animator.bottomtotop, co.uk.inspection.R.animator.bottomtotop);
                break;

            default:

                break;
        }
        return true;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_AREA)
        {
            if(resultCode == RESULT_OK)
            {
                finish();
                overridePendingTransition(co.uk.inspection.R.animator.bottomtotop, co.uk.inspection.R.animator.bottomtotop);
            }
        }

    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(co.uk.inspection.R.id.toolbar);
        toolbar.setTitle("Add Area List");
        toolbar.setTitleTextColor(getResources().getColor(co.uk.inspection.R.color.yellow));
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(co.uk.inspection.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(co.uk.inspection.R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }


    public class GetAllAreasToDuplicate extends AsyncTask<Void, Void, ArrayList<Areas>> {
        ArrayList<Areas> allAreas = new ArrayList<Areas>();

        @Override
        protected ArrayList<Areas> doInBackground(Void... params) {


            Log.d("tag", "GET ALL AREAS BACKGROUND CALL");

            Cursor areas = myDb.getAllAreasToDuplicate(propertyId, inspectionId);

            if (areas.getCount() == 0) {
                Log.d("tag", "No Data To Show");
                return null;
            }

            while (areas.moveToNext()) {

                Areas area = new Areas();
                area.setName(areas.getString(0));
                area.setId(areas.getInt(1));

                Log.d("tag", "Area is   " + areas.getString(0));
                Log.d("tag", "Area id is" + areas.getInt(1));

                allAreas.add(area);

            }


            areas.close();
            return allAreas;


        }


        @Override
        protected void onPostExecute(ArrayList<Areas> list) {
            listOfAreas = list;
            adapter = new CustomAdapterShowAllAreasToDuplicate(AddAreaList.this, co.uk.inspection.R.layout.singlerowshowallareastoduplicate, listOfAreas);
            showAllAreas.setAdapter(adapter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myDb.close();
    }
}
