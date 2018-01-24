package co.uk.inspection.CustomAdapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import co.uk.inspection.Activities.CommentsActivity;
import co.uk.inspection.DBHelper.DbHelperClass;
import co.uk.inspection.DBHelper.Globals;
import co.uk.inspection.R;
import co.uk.inspection.TableClasses.Accessories;
import co.uk.inspection.TableClasses.InspectionLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Android on 3/28/2016.
 */
public   class CustomAdapterInspectionLayout extends ArrayAdapter<Accessories> {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 00000;
    Context context;
    int layoutResourceId;
    ArrayList<Accessories> data = new ArrayList<Accessories>();
    static DbHelperClass myDb;
    String areaName,inspectionName;
    int areaId,inspectionId,propertyId;
    ViewHolder holder = null;
    private SparseBooleanArray mSelectedItemsIds;



    public CustomAdapterInspectionLayout(InspectionLayout inspectionLayout, int singlerowinspectionlayout, ArrayList<Accessories> inspectionList, String area_name, int areaId, int inspectionId,String inspectionName,int propertyId) {
        super(inspectionLayout,singlerowinspectionlayout,inspectionList);
        this.context = inspectionLayout;
        this.layoutResourceId = singlerowinspectionlayout;
        this.data = inspectionList;
        this.myDb = new DbHelperClass(this.context);
        this.areaName = area_name;
        this.areaId = areaId;
        this.inspectionId = inspectionId;
        this.inspectionName = inspectionName;
        this.propertyId = propertyId;
        mSelectedItemsIds = new SparseBooleanArray();


    }

    // for radio button checked state

    @Override
    public int getViewTypeCount() {
        //Count=Size of ArrayList.
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return data.size();
    }


    @Override
    public Accessories getItem(int position) {
        return data.get(position);
    }

    Accessories getProduct(int position) {
        return ((Accessories) getItem(position));
    }

    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {
        View item = convertView;


        if (item == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            item = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.AccessoryName = (TextView) item.findViewById(co.uk.inspection.R.id.textViewsingleRowInspectionLayout);
            holder.AccessoryName.setTag(position);

            // comment picture
            holder.comment = (ImageView) item.findViewById(co.uk.inspection.R.id.imageViewCommentSingleRowInpectionLayout);
            holder.comment.setTag(position);

            // camera picture
            holder.picture = (ImageView) item.findViewById(co.uk.inspection.R.id.imageViewCameraSingleRowInpectionLayout);
            holder.picture.setTag(position);

            // Radio Buttons

            holder.rGood = (RadioButton) item.findViewById(co.uk.inspection.R.id.radioButtonGood);
            holder.rGood.setTag(position);

            holder.rFair = (RadioButton) item.findViewById(co.uk.inspection.R.id.radioButtonFair);
            holder.rFair.setTag(position);


            holder.rDirty = (RadioButton) item.findViewById(co.uk.inspection.R.id.radioButtonDirty);
            holder.rDirty.setTag(position);

            holder.rRepair = (RadioButton) item.findViewById(co.uk.inspection.R.id.radioButtonRepair);
            holder.rRepair.setTag(position);

            holder.rReplace = (RadioButton) item.findViewById(co.uk.inspection.R.id.radioButtonReplace);
            holder.rReplace.setTag(position);

            holder.rNone = (RadioButton) item.findViewById(R.id.radioButtonNone);
            holder.rNone.setTag(position);

            holder.groupCondition = (RadioGroup) item.findViewById(co.uk.inspection.R.id.groupCondition);
            holder.groupCondition.setTag(position);

            holder.groupChange = (RadioGroup) item.findViewById(co.uk.inspection.R.id.radioGroupChange);
            holder.groupChange.setTag(position);





            item.setTag(holder);

        } else {
            holder = (ViewHolder) item.getTag();
        }

        Accessories a = getProduct(position);
        // get the object at position
        holder.AccessoryName.setText(data.get(position).getName());
        holder.AccessoryName.setTextSize(30);



        // for radio button persisitency (Radio Button keep their value when moving between activities)

        long areaDataId = myDb.getselectedAreaDataId(areaId,inspectionId,areaName,propertyId);
        Log.d("tag","Area Data Id Radio Buttons GetView "+areaDataId);

        long ascDataId = myDb.checkselectedAreaDataId(areaDataId,data.get(position).getId());

        if(ascDataId == 0l)
        {
            // insert data into accessory data table
            Log.d("tag","No Data To show For Radio Buttons ");

        }
        else
        {
            // update accessory data table
            Log.d("tag","Area Data id is exist For Radio Buttons "+ascDataId);

            Cursor res = myDb.getQualityData(ascDataId);

            String quality = null;
            String condition = null;
            if (res.getCount() > 0) {
                if (res.moveToFirst()) {
                    quality = res.getString(0);
                    condition = res.getString(1);

                }
            }

            res.close();
            Log.d("tag", "Quality of Radio Button is " + quality+"Condition is "+condition);
            if(quality != null) {
                if (quality.equals("Dirty")) {
                    holder.rDirty.setChecked(true);
                }
                if(quality.equals("Fair"))
                {
                    holder.rFair.setChecked(true);
                }
                if(quality.equals("Good"))
                {
                    holder.rGood.setChecked(true);
                }

            }
            if(condition !=null) {
                if (condition.equals("Replace")) {
                    holder.rReplace.setChecked(true);
                }
                if(condition.equals("Repair"))
                {
                    holder.rRepair.setChecked(true);
                }
                if(condition.equals("None"))
                {
                    holder.rNone.setChecked(true);
                }
            }




        }




        // check comment image
        if(data.get(position).iscommentCheck())
        {
            holder.comment.setImageResource(co.uk.inspection.R.drawable.ic_action_monologblack);
        }
        else {
            holder.comment.setImageResource(co.uk.inspection.R.drawable.ic_action_monolog);
        }



        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();
                Intent i = new Intent(context, CommentsActivity.class);
                Log.d("tag", "position is" + pos);
                i.putExtra("position", pos);
                i.putExtra("id", data.get(pos).getId());
                i.putExtra("ascName",data.get(pos).getName());
                i.putExtra("areaId",areaId);
                i.putExtra("inspectionId",inspectionId);
                i.putExtra("propertyId",propertyId);
                i.putExtra("areaName",areaName);
                ((InspectionLayout) context).startActivityForResult(i, pos);
                ((InspectionLayout) context).overridePendingTransition(co.uk.inspection.R.animator.righttoleft, co.uk.inspection.R.animator.lefttoright);

            }
        });


        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "Call Onclick camera");
                int pos = (int) holder.picture.getTag();
                Accessories a = getProduct(position);
                Log.d("tag","Id of Accessory is "+a.getId()+"Accessory Name is "+a.getName());
               dispatchTakePictureIntent(inspectionId, areaId, areaName, a.getId(), a.getName(),position);
            }
        });




        // quality group (Good , Fair , Bad)
        holder.groupCondition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
            //    Log.d("tag", "Radio Buttons " + childCount);
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        Log.d("tag", "Value is" + btn.getText().toString() + "position is " + position + "accessory is " + data.get(position).getName() + "id is " + data.get(position).getId());

                        // get the id of the area where user is saving data
                        long AreaDataId = myDb.getselectedAreaDataId(areaId,inspectionId,areaName,propertyId);
                        Log.d("tag", " Area Data Id  in Custom Adapter is  " + AreaDataId);

                        // check if areaDataId exist in AccessoryData Table
                        long areaDataId = myDb.checkselectedAreaDataId(AreaDataId,data.get(position).getId());

                        Log.d("tag","Area_data id is"+areaDataId);

                        if(areaDataId == 0l)
                        {
                            // insert data into accessory data table
                            Log.d("tag","Area Data id is not exist in the accessory data table now inserting");
                            boolean isInserted = myDb.insertAccessoryData(data.get(position).getId(),data.get(position).getName(),AreaDataId,btn.getText().toString());
                            if(isInserted)
                            {
                                Log.d("tag","Data is inserted");
                            }

                        }
                        else
                        {
                            // update accessory data table
                            Log.d("tag","Area Data id is exist updating data");
                            int rowsUpdated = myDb.updateAccessoryData(data.get(position).getId(),data.get(position).getName(),AreaDataId,btn.getText().toString());
                            Log.d("tag","Rows updated is "+rowsUpdated);

                        }





                    }
                }
            }
        });


        holder.groupChange.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int childCount = group.getChildCount();
         //       Log.d("tag", "Radio Buttons in change Group is  " + childCount);
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        Log.d("tag", "Value is" + btn.getText().toString() + "position is " + position + "accessory is " + data.get(position).getName() + "id is " + data.get(position).getId());


                        Log.d("tag","area_id is "+areaId+"Inspection id is "+inspectionId);

                        long area_data_id = myDb.getselectedAreaDataId(areaId,inspectionId,areaName,propertyId);
                        Log.d("tag","Inserted Area_data_id is"+area_data_id);


                        // check if the id is exist in the table or not
                       long asc_data_id =  myDb.checkselectedAreaDataId(area_data_id,data.get(position).getId());
                        Log.d("tag","Accessory data id is "+asc_data_id);

                        if(asc_data_id == 0l)
                        {
                            Log.d("tag","Value doesnot exist insert a value");

                            boolean isInserted = myDb.insertAccessoryDataCondition(data.get(position).getId(), data.get(position).getName(), area_data_id, btn.getText().toString());
                            if(isInserted)
                            {
                                Log.d("tag","Data is inserted");
                            }
                        }
                        else
                        {
                            Log.d("tag","Update the value ");
                            // update accessory data table
                            Log.d("tag","Area Data id is exist updating data");
                            int rowsUpdated = myDb.updateAccessoryDataCondition(data.get(position).getId(), data.get(position).getName(), area_data_id, btn.getText().toString());
                            Log.d("tag","Rows updated is "+rowsUpdated);

                        }


                    }
                }



            }
        });


        return item;

    }









    public  class ViewHolder {
        TextView AccessoryName;
        ImageView comment,picture;
        RadioGroup groupChange,groupCondition;
        RadioButton rGood,rFair,rDirty,rRepair,rReplace,rNone;



    }


    @Override
    public void remove(Accessories object) {
        data.remove(object);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Accessories> data1)
    {
     data.addAll(data1);
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


    private void dispatchTakePictureIntent(int inspection_id,int area_id,String areaName , int asc_id , String asc_name,int position) {

        Log.d("tag","Inspection id is "+inspectionId+"area id is"+area_id+"Area name is "+areaName+"accessory Id is"+asc_id+"Accessory Name is "+asc_name);
        Log.d("tag","Taking Picture");

        Globals.areaName = areaName;
        Globals.ascId = asc_id;
        Globals.ascName = asc_name;

        Log.d("tag","Globals  Variables Area name is "+Globals.areaName+"accessory Id is"+Globals.ascId+"Accessory Name is "+Globals.ascName);




        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

// Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            Log.d("tag","Exception occur"+ex.getMessage());
        }

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
        Globals.cameraPositionToScroll = position;
        Log.d("tag", "Uri is" + Uri.fromFile(photoFile).toString());
        ((InspectionLayout) context).startActivityForResult(takePictureIntent, InspectionLayout.REQUEST_TAKE_PHOTO);


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




}


