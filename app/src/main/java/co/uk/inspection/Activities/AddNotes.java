package co.uk.inspection.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
 * Created by User-10 on 15-Jun-16.
 */
public class AddNotes extends AppCompatActivity {

    public static final String NOTES = "notes";
    EditText edtNotes;
    Button save;
    Toolbar toolbar;
    int pos,areaId,inspectionId,ascId,propertyId;
    String notes ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnotes);
        initToolBar();
        // setup back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtNotes = (EditText) findViewById(R.id.edittextNotes);
        save = (Button) findViewById(R.id.buttonSaveNotes);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor.putString(NOTES, edtNotes.getText().toString());
                prefsEditor.commit();

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
        toolbar.setTitle("Add Notes");
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

            default:
                return super.onOptionsItemSelected(item);
        }



    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("tag", "Back from hardwaer button");
        overridePendingTransition(co.uk.inspection.R.animator.backright, co.uk.inspection.R.animator.backin);
    }


}
