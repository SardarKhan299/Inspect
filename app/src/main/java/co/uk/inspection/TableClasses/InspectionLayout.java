package co.uk.inspection.TableClasses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import co.uk.inspection.Activities.AddAscForm;
import co.uk.inspection.Activities.ShowAllImages;
import co.uk.inspection.CustomAdapters.CustomAdapterInspectionLayout;
import co.uk.inspection.DBHelper.DbHelperClass;
import co.uk.inspection.DBHelper.Globals;
import co.uk.inspection.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


/**
 * Created by Android on 3/25/2016.
 */
public class InspectionLayout extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 00000;
    ListView inspectionList;
    CustomAdapterInspectionLayout adapter;
    String area_name,inspectionName;
    Toolbar toolbar;
    int pos,inspectionId,areaId,propertyId,positionToScroll;
    Long area_data_id;
    Button savebutton;
    ImageView  deleteButton ;
    int posFromCamera;

    static DbHelperClass myDb;

    public static final int REQUEST_TAKE_PHOTO = 100;
    public static final int REQUESTCODE =501;

    ArrayList<Accessories> allAccessories = new ArrayList<Accessories>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspectionlayout);

        myDb = new DbHelperClass(this);

        savebutton = (Button) findViewById(R.id.buttonSaveAreaInspection);
        inspectionList = (ListView) findViewById(R.id.listViewInspectionLayout);
        inspectionList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        inspectionList.setClickable(true);

        deleteButton = (ImageView) findViewById(R.id.imageViewDelete);
        deleteButton.setVisibility(View.GONE);

        Intent i = getIntent();
        area_name = i.getStringExtra("area_name");
        pos = i.getIntExtra("position", -1);
        inspectionId = i.getIntExtra("inspectionId", 0);
        propertyId = i.getIntExtra("propertyId", 0);
        areaId = i.getIntExtra("areaId", 0);
        inspectionName = i.getStringExtra("inspectionName");
        area_data_id = i.getLongExtra("area_data_id", 0);

        initToolBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d("tag", " OnCreate Call Inspection Layout areaName is " + area_name + "Area id is " + areaId + "inspection id is" + inspectionId+"Area_data_id is "+area_data_id+"Position is "+pos);
        new GetDataFromDb1().execute();




        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               long area_data_id =  myDb.getAreaDataId(areaId,inspectionId,propertyId,area_name);
                int updateRows = myDb.updateAreaDataComplete(area_data_id);

                Log.d("tag","Area Data Id On Button Click is "+area_data_id);
                Log.d("tag","Rows Updated is "+updateRows);


                Intent result = new Intent();
                result.putExtra("area_name", area_name);
                result.putExtra("position", pos);
                setResult(RESULT_OK, result);
                finish();
                overridePendingTransition(R.animator.backright, R.animator.backin);

            }
        });

        inspectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("tag","Item CLicked");
            }
        });

        inspectionList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                // Capture total checked items
                final int checkedCount = inspectionList.getCheckedItemCount();
                // Set the CAB title according to total checked items

                mode.setTitle(checkedCount + " Selected");
                getSupportActionBar().hide();

                // Calls toggleSelection method from ListViewAdapter Class
                adapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.main, menu);
                // disable the menu item

                menu.findItem(R.id.action_addFutureInspection).setVisible(false);
                menu.findItem(R.id.action_addInspection).setVisible(false);
                menu.findItem(R.id.search).setVisible(false);
                menu.findItem(R.id.delete).setVisible(true);
                menu.findItem(R.id.action_addArea).setVisible(false);
                menu.findItem(R.id.action_addSection).setVisible(false);
                menu.findItem(R.id.action_createReport).setVisible(false);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = adapter
                                .getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                            // get the item
                                Accessories selecteditem = adapter.getItem(selected.keyAt(i));

                                // delete the selected item
                                myDb.deleteAccessory(selecteditem.getId(),area_data_id,selecteditem.getName());

                                // Remove selected items following the ids
                                adapter.remove(selecteditem);
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
                deleteButton.setVisibility(View.INVISIBLE);
                toolbar.setVisibility(View.VISIBLE);
            }
        });





    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // disable the menu item
        menu.findItem(R.id.action_addFutureInspection).setVisible(false);
        menu.findItem(R.id.action_addInspection).setVisible(false);
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.action_addArea).setVisible(false);
        menu.findItem(R.id.action_createReport).setVisible(false);
        menu.findItem(R.id.delete).setVisible(false);
        menu.findItem(R.id.rotateImage).setVisible(false);
        menu.findItem(R.id.notes).setVisible(false);



        return super.onCreateOptionsMenu(menu);
    }


    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(area_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.yellow));
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Log.d("tag", "Call On Activity Result Back");

        if(requestCode == 0)
        {
            if(resultCode == RESULT_OK)
            {

                Log.d("tag","Click on First Item");
                int pos = data.getIntExtra("position", -1);
                Log.d("tag","position is In Activity Result is "+pos);
                View v = inspectionList.getChildAt(pos);
                Log.d("tag","View"+v.toString());
                allAccessories.get(pos).setIscommentCheck(true);
                ImageView img = (ImageView) v.findViewById(R.id.imageViewCommentSingleRowInpectionLayout);
                img.setImageResource(R.drawable.ic_action_monologblack);

            }

        }
         if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {

                Log.d("tag","Click on Second Item");
                int pos = data.getIntExtra("position",-1);
                View v = inspectionList.getChildAt(pos);
                ImageView img = (ImageView) v.findViewById(R.id.imageViewCommentSingleRowInpectionLayout);
                img.setImageResource(R.drawable.ic_action_monologblack);
                allAccessories.get(pos).setIscommentCheck(true);



            }
        }


        if (requestCode == REQUEST_TAKE_PHOTO ) {
            if (resultCode == RESULT_OK ) {
                // Image captured and saved to fileUri specified in the Intent
//                Uri u = (Uri) data.getData();

               posFromCamera  = Globals.cameraPositionToScroll;
                File photoFile = Globals.imageCaptured;
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Log.d("tag", "photoFile is not null");
                    Log.d("tag","File save at"+photoFile.getAbsolutePath()+"Name of the file is "+photoFile.getName());

                    // save file path to database (Save path to iamegs table)

                    long area_data_id = myDb.getselectedAreaDataId(areaId,inspectionId, Globals.areaName,propertyId);
                    Log.d("tag","Selected Area_data_id is"+area_data_id);


                    // check if the id is exist in the table or not
                    long asc_data_id =  myDb.checkselectedAreaDataId(area_data_id,Globals.ascId);
                    Log.d("tag", "Accessory data id is " + asc_data_id);

                    if(asc_data_id == 0l)
                    {
                        Log.d("tag","Value doesnot exist insert a value");

                        long insertedAscDataId = myDb.insertAccessoryDataImage(Globals.ascId,Globals.ascName, area_data_id);
                        Log.d("tag","Id inserted is "+insertedAscDataId);
                        boolean isInserted = myDb.insertImageData(photoFile.getName(),photoFile.getAbsolutePath(),insertedAscDataId,getDateTime());
                        if(isInserted)
                        {
                            Log.d("tag","Images Data Inserted");

                        }
                        else
                        {
                            Log.d("tag","Images Data Not Inserted");
                        }


                    }
                    else
                    {
                        Log.d("tag","Update the value ");
                        // update accessory data table
                        Log.d("tag","Area Data id is exist updating data");
                        Log.d("tag","Already inserted id is "+asc_data_id);
                        boolean isInserted = myDb.insertImageData(photoFile.getName(),photoFile.getAbsolutePath(),asc_data_id,getDateTime());
                        if(isInserted)
                        {
                            Log.d("tag","Images Data Inserted");

                        }
                        else
                        {
                            Log.d("tag","Images Data Not Inserted");
                        }


                    }



                }
                else
                {
                    Log.d("tag", "photo file is null");
                }


                Log.d("tag", "Position of camera is  " + posFromCamera);
                inspectionList.setSelection(posFromCamera);
                inspectionList.smoothScrollToPosition(posFromCamera);
                Accessories a = (Accessories) inspectionList.getItemAtPosition(posFromCamera);
                Log.d("tag", "Your selected Asc Name is " + a.getName());
                Toast.makeText(getApplicationContext()," Your are inspecting on "+a.getName(),Toast.LENGTH_LONG).show();

                showDialogTakeMorePictures();

                //Toast.makeText(this, "Image saved to:\n "+photoFile , Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(this, "Canceled by user", Toast.LENGTH_LONG).show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(this, "No image Captured", Toast.LENGTH_LONG).show();
            }
        }


        // to handle return from comment activity

            if(resultCode == 7)
            {
                Log.d("tag", "Return From Add New Section");
             //  new AllAscOfArea().execute();


                positionToScroll = data.getIntExtra("position", -1);
                Log.d("tag", "Position is " + positionToScroll);
                inspectionList.setSelection(positionToScroll);
                inspectionList.smoothScrollToPosition(positionToScroll);
                Accessories a = (Accessories) inspectionList.getItemAtPosition(positionToScroll);
                Log.d("tag", "Your selected Asc Name is " + a.getName());
                Toast.makeText(getApplicationContext()," Your are inspecting on "+a.getName(),Toast.LENGTH_LONG).show();



            }




    }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void showDialogTakeMorePictures()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(InspectionLayout.this);

        // Setting Dialog Title
        alertDialog.setTitle("Taking Pictures");

        // Setting Dialog Message
        alertDialog.setMessage("Do you want to take more photos?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_action_camera);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed YES button. Write Logic Here

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

// Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.d("tag","Exception occur"+ex.getMessage());
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
                Log.d("tag", "Uri is" + Uri.fromFile(photoFile).toString());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed No button. Write Logic Here
                dialog.dismiss();
                inspectionList.setSelection(posFromCamera);
                inspectionList.smoothScrollToPosition(posFromCamera);

            }
        });


        // Showing Alert Message
        alertDialog.show();


    }



    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {

        // create a unique fileName
        UUID id = UUID.randomUUID();
        String fileName = inspectionName + id;

        // creat a folder
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),inspectionName);

        // if directory not exist create a new dirctory
        if(!storageDir.exists()) {
            Log.d("tag","Directory Not exist Creating New");
            storageDir.mkdir();
        }


        // create a folder and if directory not exist create a new one
        File mydir = new File(storageDir,Globals.areaName);
        if(!mydir.exists())
        {      Log.d("tag","File Not exist Creating New");
            mydir.mkdir();
        }


        // create a file in a directory
        File image = File.createTempFile(
                fileName,  /* prefix */
                ".jpg",         /* suffix */
                mydir      /* directory */
        );

        Globals.imageCaptured = image;
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();

//        long InspectionAscId = myDb.getselectedInspectionAscId(data.get((int)holder.AccessoryName.getTag()).getId());
//        Log.d("tag", "Inspection Accessory Id In Custom adapter is " + InspectionAscId);
//
//        if (InspectionAscId == 0) {
//
//            // means if inspectionid is not inserted then create a record in inspectionAsseccory table
//            // then take this record id and insert into condition table with condition
//
//            long inspection_ascId = myDb.insertInspection_ascData(areaId, inspectionId, data.get((int)holder.AccessoryName.getTag()).getId());
//
//            // save data into images table with inspection_ascId
//
//        } else {
//
//            // save data into images table with inspection_ascId as previously generated
//
//
//        }


        // save the path to Database of Images table
        Log.d("tag","File Created or Store at"+mCurrentPhotoPath);
        return image;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                Intent result = new Intent();
                result.putExtra("area_name", area_name);
                result.putExtra("position", pos);
                setResult(1,result);
                finish();
                overridePendingTransition(R.animator.backright, R.animator.backin);
                return true;
            case R.id.action_addSection:
          // add section here
                // Add Area
                Intent i = new Intent(InspectionLayout.this,AddAscForm.class);
                i.putExtra("area_data_id", area_data_id);
                startActivityForResult(i, REQUESTCODE);
                overridePendingTransition(R.animator.toptobottom, R.animator.toptobottom);

                return  true;

            case R.id.action_ShowImages:
                // add section here
                // Add Area
                Intent it = new Intent(InspectionLayout.this,ShowAllImages.class);
                it.putExtra("areaName",area_name);
                it.putExtra("inspectionName",inspectionName);
                startActivityForResult(it, REQUESTCODE);
                overridePendingTransition(R.animator.toptobottom, R.animator.toptobottom);

                return  true;

            default:
                return super.onOptionsItemSelected(item);
        }



    }



    // inner class
    class GetDataFromDb1 extends AsyncTask<Void,Void,ArrayList<Accessories>>
    {


        @Override
        protected ArrayList<Accessories> doInBackground(Void... params) {

            Log.d("tag", "Get Data From Db Do in Background");

            ArrayList<Accessories> allAccessories = new ArrayList<Accessories>();

            Long aId = myDb.getAreaId(area_name,propertyId,inspectionId);
           // Log.d("tag","Area id is "+aId);

            Cursor accessoriesCursor = myDb.getAllAccessoriesOfSpecificArea(aId);
            Cursor accessoriesNameCursor = null;

            if(accessoriesCursor.getCount() == 0)
            {
                Log.d("tag", "No Data To Show");
                return null;
            }

            //StringBuffer buffer = new StringBuffer();


            int  AccessoryId ;

            while(accessoriesCursor.moveToNext())
            {

                AccessoryId = accessoriesCursor.getInt(0);

                Log.d("tag", "Accessories Id is   " + AccessoryId);

                accessoriesNameCursor = myDb.getAllAccessoriesName(AccessoryId);

                if(accessoriesNameCursor.getCount() == 0)
                {
                    Log.d("tag", "No Data To Show");
                    return null;
                }

                while (accessoriesNameCursor.moveToNext())
                {
                    Accessories asc = new Accessories();
                    asc.setName(accessoriesNameCursor.getString(0));
                    asc.setId(accessoriesNameCursor.getInt(1));



                    // check if inspection id exist..
                    long asc_data_id =  myDb.checkAccessoryAndAreaDataIds(accessoriesNameCursor.getInt(1),area_data_id,accessoriesNameCursor.getString(0));
                    //      Log.d("tag", "Accessory data id is " + asc_data_id);
                    if(asc_data_id == 0l)
                    {
                            Long insertedAscDataId = myDb.insertAscAndAreaData(accessoriesNameCursor.getInt(1),area_data_id,accessoriesNameCursor.getString(0));
                            Log.d("tag","Inserted AscData Id in InspectionLayout is "+insertedAscDataId);
                    }
                    else
                    {
                        Log.d("tag","Id Already Exist in DB");
                    }


                    allAccessories.add(asc);


                }
                //	asc.setName(accessoriesCursor.getString(0));

                //	allAccessories.add(asc);


            }



            accessoriesNameCursor.close();
            accessoriesCursor.close();
            return allAccessories;

        }

        @Override
        protected void onPostExecute(ArrayList<Accessories> data) {
            super.onPostExecute(data);
            if(data != null)
            {
//                allAccessories = data;
//                Log.d("tag","Post Execute");
//                adapter = new CustomAdapterInspectionLayout(InspectionLayout.this,R.layout.singlerowinspectionlayout,allAccessories,area_name,areaId,inspectionId,inspectionName,propertyId);
//                inspectionList.setAdapter(adapter);
                new AllAscOfArea().execute();

            }

        }

    }




    public class AllAscOfArea extends AsyncTask<Void,Void,ArrayList<Accessories>>
    {
        ArrayList<Accessories> allAccessoriesOfArea = new ArrayList<Accessories>();
        @Override
        protected ArrayList<Accessories> doInBackground(Void... params) {

            Log.d("tag", "GET ALL ASC OF SPECIFIC Area BACKGROUND CALL");

            //       Log.d("tag","inspection Id is"+inspectionId+"Proeprty Id "+propertyId);
            Cursor accessories = myDb.getAllAscOfArea(area_data_id);

            if(accessories.getCount() == 0)
            {
                Log.d("tag", "No Data To Show");
                return null;
            }


            String InspectionName = null;

            while(accessories.moveToNext())
            {


                Accessories asc = new Accessories();
                asc.setName(accessories.getString(0));
                asc.setId(accessories.getInt(1));



                Log.d("tag", "Asc Name  is   " + accessories.getString(0));
                // 	  Log.d("tag","Area id is"+areas.getInt(1));

                allAccessoriesOfArea.add(asc);

            }


            accessories.close();
            return allAccessoriesOfArea;


        }


        @Override
        protected void onPostExecute(ArrayList<Accessories> list) {
            allAccessories = list;
            adapter = new CustomAdapterInspectionLayout(InspectionLayout.this,R.layout.singlerowinspectionlayout,allAccessories,area_name,areaId,inspectionId,inspectionName,propertyId);
            inspectionList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            inspectionList.post(new Runnable() {
                @Override
                public void run() {

                    inspectionList.smoothScrollToPosition(positionToScroll);
                    inspectionList.setSelection(positionToScroll);
                }
            });


        }
    }






    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("tag", "Back from hardwaer button");
        Intent result = new Intent();
        result.putExtra("area_name", area_name);
        result.putExtra("position", pos);
        setResult(1,result);
        finish();
        overridePendingTransition(R.animator.backright, R.animator.backin);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("tag","Onstart Call Inspection Layout");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("tag","OnResume Call InspectionLayout");
        // save accessory data to Accessory Table
        new AllAscOfArea().execute();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("tag","OnPause Call InspectionLayout");
        myDb.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("tag", "OnStop Call Inpsection Layout");

    }
}
