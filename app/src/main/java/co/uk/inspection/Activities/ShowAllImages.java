package co.uk.inspection.Activities;

import android.app.Application;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;

import co.uk.inspection.CustomAdapters.ImageAdapter;
import co.uk.inspection.R;
import co.uk.inspection.TableClasses.Areas;
import co.uk.inspection.TableClasses.ShowAllAreas;

/**
 * Created by Android on 6/2/2016.
 */
public class ShowAllImages extends AppCompatActivity {

    public static final int REQUESTCODE =501;

    Toolbar toolbar;
    GridView gridView;
    String areaName,inspectionName;

    ArrayList<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;
    ImageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all_images);
        initToolBar();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels / 3;
        int height = metrics.heightPixels / 4;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gridView = (GridView) findViewById(R.id.gridView);
        Intent i = getIntent();
        areaName = i.getStringExtra("areaName");
        inspectionName = i.getStringExtra("inspectionName");
        getFromSdcard();
        adapter = new ImageAdapter(ShowAllImages.this,R.layout.single_row_show_all_images,f);
        gridView.setAdapter(adapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setDrawSelectorOnTop(true);
        gridView.setColumnWidth(width);
        gridView.setMinimumHeight(height);
        gridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                Log.d("tag", "On Item Check Stated changed");
                int selectCount = gridView.getCheckedItemCount();
                mode.setTitle(selectCount + " item Selected");

                // Calls toggleSelection method from ListViewAdapter Class
                adapter.toggleSelection(position);

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                Log.d("tag", "Call oncreate action mode");
                mode.getMenuInflater().inflate(co.uk.inspection.R.menu.main, menu);
                // disable the menu item
                menu.findItem(co.uk.inspection.R.id.action_addFutureInspection).setVisible(false);
                menu.findItem(co.uk.inspection.R.id.action_addInspection).setVisible(false);
                menu.findItem(co.uk.inspection.R.id.search).setVisible(false);
                menu.findItem(co.uk.inspection.R.id.delete).setVisible(true);
                menu.findItem(co.uk.inspection.R.id.action_addArea).setVisible(false);
                menu.findItem(co.uk.inspection.R.id.action_addSection).setVisible(false);
                menu.findItem(co.uk.inspection.R.id.action_createReport).setVisible(false);
                menu.findItem(R.id.action_ShowImages).setVisible(false);
                menu.findItem(R.id.rotateImage).setVisible(false);

                return true;

            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                Log.d("tag", "On Prepare Action Mode");
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case co.uk.inspection.R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        Log.d("tag","Call Delete" );

                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = adapter.getSelectedIds();

                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                String selectedItem = adapter
                                        .getItem(selected.keyAt(i));

                                File fdelete = new File(selectedItem.toString());
                                if (fdelete.exists()) {
                                    if (fdelete.delete()) {
                                        Log.d("tag", "File Deleted"+selectedItem.toString());
                                    } else {
                                        Log.d("tag", "File Not Deleted");
                                    }
                                }
                                // Remove selected items following the ids
                                adapter.remove(selectedItem);
                            }
                        }


                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }

            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.removeSelection();
            }
        });



        Log.d("tag", "AreaName is " + areaName + "Inspection Name is " + inspectionName);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(ShowAllImages.this,FullImage.class);
                i.putExtra("imagePath",f.get(position).toString());
                Log.d("tag","Path is "+f.get(position).toString());
                startActivityForResult(i, REQUESTCODE);
            }
        });

    }


    public void getFromSdcard()
    {
        File file1 = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),inspectionName);

        File file = new File(file1,areaName);

        if (file.isDirectory())
        {
            listFile = file.listFiles();


            for (int i = 0; i < listFile.length; i++)
            {

                f.add(listFile[i].getAbsolutePath());

            }
        }
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(co.uk.inspection.R.id.toolbar);
        toolbar.setTitle("Images");
        toolbar.setTitleTextColor(getResources().getColor(co.uk.inspection.R.color.yellow));
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(co.uk.inspection.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(co.uk.inspection.R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

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




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("tag", "Back from hardwaer button");
        overridePendingTransition(R.animator.bottomtotop, R.animator.bottomtotop);
    }



}
