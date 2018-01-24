package co.uk.inspection.TableClasses;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import co.uk.inspection.Activities.AddAreaList;
import co.uk.inspection.Activities.AddNotes;
import co.uk.inspection.Activities.Globals;
import co.uk.inspection.CustomAdapters.CustomAdapterShowAllAreas;
import co.uk.inspection.DBHelper.DbHelperClass;
import co.uk.inspection.MainActivity;
import co.uk.inspection.R;



import com.itextpdf.text.DocumentException;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Android on 3/24/2016.
 */
public class ShowAllAreas extends AppCompatActivity implements SwipeListView.SwipeListViewCallback {

    private static final int REQUEST_CODE_CREATOR = 2;
    private static final int REQUEST_CODE_RESOLUTION = 3;
    DynamicListView showAllAreas;
    Toolbar toolbar;
    ImageView areaImage, deleteButton ;
    public static final int REQUEST_CODE = 100;

    public boolean isAllowCreateReport = false;
    public boolean checkNetwork = false;


    CustomAdapterShowAllAreas adapter;
    ArrayList<Areas> listOfAreas = new ArrayList<Areas>();
    static DbHelperClass myDb;
    File myFile;

    private int serverResponseCode = 0;

    private String upLoadServerUri = null;


    int pos,inspectionId,propertyId;
    String inspectionName, inspectionType, areaName,fileName;
    String areaNameToDelete;
    public static int ins_id ;
    public static int pro_id;






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("tag", "On Create Call");
        setContentView(co.uk.inspection.R.layout.showallareas);

        myDb = new DbHelperClass(this);
        initToolBar();


        areaImage = (ImageView) findViewById(co.uk.inspection.R.id.imageViewArea);
//        deleteButton = (ImageView) findViewById(co.uk.inspection.R.id.imageViewDelete);
//        deleteButton.setVisibility(View.GONE);


        // setup back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        inspectionName = i.getStringExtra("inspectionName");
        inspectionType = i.getStringExtra("inspectionType");
        inspectionId = i.getIntExtra("inspectionId", 0);
        propertyId = i.getIntExtra("propertyId", 0);

        Log.d("tag", "Inspection Name is " + inspectionName);
        Log.d("tag", "Inspection Type is " + inspectionType);
        Log.d("tag","Inspection Id is "+inspectionId);
        Log.d("tag", "Proeprty id in Areas is " + propertyId);


        this.pro_id = propertyId;
        this.ins_id = inspectionId;

        showAllAreas = new DynamicListView(ShowAllAreas.this,inspectionId,propertyId);
        showAllAreas = (DynamicListView) findViewById(R.id.listview);
        SwipeListView l = new SwipeListView(ShowAllAreas.this, this);
        l.exec();

        upLoadServerUri = "http://property.tenantfind.co.uk/Inspector/uploadToServer.php";





        // get all areas from db
        new GetAllAreas().execute();




                showAllAreas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



            }

        });





    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(co.uk.inspection.R.id.toolbar);
        toolbar.setTitle("List of Areas");
        toolbar.setTitleTextColor(getResources().getColor(co.uk.inspection.R.color.yellow));
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(co.uk.inspection.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(co.uk.inspection.R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        Log.d("tag", "Call On Activity Result");

if (resultCode == 1)
{
    // call code to show data from database
new AllAreasOfUser().execute();
    pos = data.getIntExtra("position",0);

}
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("tag", "return");
                areaName = data.getStringExtra("area_name");
                pos = data.getIntExtra("position", -1);
                Log.d("tag","Position is"+pos);
                isAllowCreateReport = true;

            }

        }




    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(co.uk.inspection.R.menu.main, menu);

        // disable the menu item
        menu.findItem(co.uk.inspection.R.id.action_addFutureInspection).setVisible(false);
        menu.findItem(co.uk.inspection.R.id.action_addInspection).setVisible(false);
        menu.findItem(co.uk.inspection.R.id.search).setVisible(false);
        menu.findItem(co.uk.inspection.R.id.delete).setVisible(false);
        menu.findItem(co.uk.inspection.R.id.action_addSection).setVisible(false);
        menu.findItem(R.id.action_ShowImages).setVisible(false);
        menu.findItem(R.id.rotateImage).setVisible(false);
        menu.findItem(R.id.notes).setVisible(true);

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
                // Add Area
                Intent i = new Intent(ShowAllAreas.this,AddAreaList.class);
                i.putExtra("propertyId",propertyId);
                i.putExtra("inspectionId",inspectionId);
                startActivityForResult(i, MainActivity.REQUESTCODE);
                overridePendingTransition(co.uk.inspection.R.animator.toptobottom, co.uk.inspection.R.animator.toptobottom);

                return true;
            case co.uk.inspection.R.id.action_createReport:
                 checkNetwork =   isNetworkAvailable();
                if(checkNetwork) {
                    generatePDF(inspectionName, inspectionId, propertyId, inspectionType);
                }else {
                    Toast.makeText(getApplicationContext(),"You are Not connected to Internet ",Toast.LENGTH_SHORT).show();
                }

                return  true;
            case R.id.notes:
                Intent startNotes = new Intent(ShowAllAreas.this, AddNotes.class);
                startActivityForResult(startNotes, MainActivity.REQUESTCODE);
                overridePendingTransition(co.uk.inspection.R.animator.toptobottom, co.uk.inspection.R.animator.toptobottom);
                return  true;



            default:
                return super.onOptionsItemSelected(item);
        }



    }


public void generatePDF(String inspectionName , int inspectionId , int propertyId, String inspectionType)
{
    new GeneratePDF().execute();

}

//    public void promptForNextAction()
//    {
//        final String[] options = { "email", "preview",
//                "cancel" };
//        AlertDialog.Builder builder = new AlertDialog.Builder(ShowAllAreas.this);
//        builder.setTitle("Note Saved, What Next?");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (options[which].equals("email")) {
//                    emailNote();
//                } else if (options[which].equals("preview")) {
//                    viewPdf();
//                } else if (options[which].equals("cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//
//        builder.show();
//
//    }




    // show Alert Dialog Box
    private void showInputDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, co.uk.inspection.R.style.CustomDialogTheme);
        builder.setTitle("Add Area");
        View view = getLayoutInflater().inflate(co.uk.inspection.R.layout.addareaview, null);
        builder.setView(view);



        final AlertDialog dialog = builder.create();
        dialog.show();





    }




    @Override
    protected void onPause() {
        super.onPause();
        Log.d("tag", "On Pause Call");
        myDb.close();

    }

    @Override
    public ListView getListView() {
        return showAllAreas;
    }

    @Override
    public void onSwipeItem(boolean isRight, int position) {
        adapter.onSwipeItem(isRight, position);
        View row = getListView().getChildAt(position);
      //  removeRow(row,position);
    }

    private void removeRow(final View row, final int position) {

        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.righttoleftswipe);
        row.startAnimation(hyperspaceJumpAnimation);
    }


    @Override
    public void onItemClickListener(ListAdapter adapter, int position) {

        Areas area = (Areas) showAllAreas.getItemAtPosition(position);
        //     Toast.makeText(getApplicationContext(),"Selected Area" +area.getName(),Toast.LENGTH_SHORT).show();


        Long area_data_id =   myDb.getAreaDataId(area.getId(),inspectionId,propertyId,area.getName());

        Log.d("tag","Area data id is "+area_data_id);

        Intent i = new Intent(ShowAllAreas.this, InspectionLayout.class);
        i.putExtra("area_name", area.getName());
        i.putExtra("position", position);
        i.putExtra("inspectionId", inspectionId);
        i.putExtra("areaId", area.getId());
        i.putExtra("inspectionName", inspectionName);
        i.putExtra("area_data_id",area_data_id);
        i.putExtra("propertyId", propertyId);


        startActivityForResult(i, REQUEST_CODE);
        overridePendingTransition(co.uk.inspection.R.animator.righttoleft, co.uk.inspection.R.animator.lefttoright);




    }


    public class GeneratePDF extends AsyncTask<Void, Void, String > {
        PdfCreater gen = new PdfCreater(ShowAllAreas.this, inspectionName, inspectionId, propertyId, inspectionType);
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowAllAreas.this);
            pDialog.setMessage("Creating Report...");
            pDialog.show();
            pDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {


            String data = "";
            Log.d("tag", "Generate PDF BackGround Running " + inspectionName);


            try {
                gen.createPdf();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }


            return data;
        }


        @Override
        protected void onPostExecute(String a) {

            Log.d("tag", "On Post Execute Of BAckground");

            pDialog.dismiss();

            //  this code is for the uplaod file to drive api

            //create pdf in documents directory
            File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "mypdf");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
                Log.i("tag", "Pdf Directory created");
            }


            Cursor fileCursor = myDb.checkFilePath(propertyId, inspectionId, inspectionName, inspectionType);
//        if(fileCursor.getCount() == 0)
//        {
//            // get Current Date and Time and create the file
//            Date date = new Date() ;
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
//             fileName = inspectionName + timeStamp;
//            myFile = new File(pdfFolder,fileName + ".pdf");
//
//        }
            if (fileCursor.getCount() > 0) {
                while (fileCursor.moveToNext()) {

                    fileName = fileCursor.getString(0);

                    if (fileName != null && !fileName.isEmpty()) {
                        Log.d("tag", "File Name from Db is " + fileName);

                        myFile = new File(pdfFolder, fileName + ".pdf");


                    } else {

                        // get Current Date and Time and create the file
                        Date date = new Date();
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                        UUID id = UUID.randomUUID();
                        fileName = inspectionName + id;
                        myFile = new File(pdfFolder, fileName + ".pdf");
                        Log.d("tag", "File Name not exist  is " + fileName);

                    }

                }

            }

            new Upload().execute();
         //   gen.promptForNextAction();
        }


         class Upload extends AsyncTask<Void, Void,Integer> {
            ProgressDialog pDialog;



             @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(ShowAllAreas.this);
                 pDialog.setMessage("Uploading File to Server This might take Time depend on your network speed");
                 pDialog.show();
                 pDialog.setCancelable(false);
            }


            @Override
            protected Integer  doInBackground(Void... params) {

                int code = uploadFile(myFile.toString());

                Log.d("tag","Response Code is "+code);

                return code;


            }


            @Override
            protected void onPostExecute(Integer code) {
                Log.d("tag", "Complete Upload Code is " + code);
                pDialog.dismiss();
                if(code == 200)
                {
                    gen.promptForNextAction();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Network Error Check your Network Connection and Try Again",Toast.LENGTH_SHORT).show();
                }

            }
        }

    }
    public class GetAllAreas extends AsyncTask<Void,Void,ArrayList<Areas>>
    {
        ArrayList<Areas> allAreas = new ArrayList<Areas>();
        @Override
        protected ArrayList<Areas> doInBackground(Void... params) {


            Log.d("tag", "GET ALL AREAS BACKGROUND CALL");

            Cursor areas = myDb.getAllAreas();

            if(areas.getCount() == 0)
            {
                Log.d("tag", "No Data To Show");
                return null;
            }

            //StringBuffer buffer = new StringBuffer();
            String InspectionName = null;

            int i =0;
            while(areas.moveToNext())
            {

//		buffer.append("id :"+allData.getString(0)+"\n");
//		buffer.append(""+allData.getString(1));
//		buffer.append("Address :"+allData.getString(2)+"\n");
//		buffer.append("Zip :"+allData.getString(3)+"\n\n ");
                Areas area = new Areas();
                area.setName(areas.getString(0));
                area.setId(areas.getInt(1));

                // check if inspection id exist..
                long asc_data_id =  myDb.checkInspectionAndProeprtyId(propertyId, inspectionId,areas.getInt(1));
          //      Log.d("tag", "Accessory data id is " + asc_data_id);
                if(asc_data_id == 0l)
                {
                    Log.d("tag","Position is "+i);
                    Long insertedAreaId = myDb.insertInspectionAreaData(areas.getInt(1),inspectionId,propertyId,areas.getString(0),areas.getInt(2));

                //    Log.d("tag","Inserted Area Id in Areas is "+insertedAreaId);
                }
                else
                {
                    Log.d("tag","Id Already Exist in DB");
                }



                //     Log.d("tag", "Area is   " + areas.getString(0));
                // 	  Log.d("tag","Area id is"+areas.getInt(1));

                allAreas.add(area);
                i++;

            }


            areas.close();
            return allAreas;


        }


        @Override
        protected void onPostExecute(ArrayList<Areas> list) {
        new AllAreasOfUser().execute();
        }
    }



    public class AllAreasOfUser extends AsyncTask<Void,Void,ArrayList<Areas>>
    {
        ArrayList<Areas> allAreas = new ArrayList<Areas>();
        @Override
        protected ArrayList<Areas> doInBackground(Void... params) {

            Log.d("tag", "GET ALL AREAS OF SPECIFIC USER BACKGROUND CALL");

     //       Log.d("tag","inspection Id is"+inspectionId+"Proeprty Id "+propertyId);
            Cursor areas = myDb.getAllAreasOfUser(propertyId, inspectionId);

            if(areas.getCount() == 0)
            {
                Log.d("tag", "No Data To Show");
                return null;
            }


            String InspectionName = null;

            while(areas.moveToNext())
            {


                Areas area = new Areas();
                area.setName(areas.getString(0));
                area.setId(areas.getInt(1));



                    Log.d("tag", "Area is   " + areas.getString(0));
                // 	  Log.d("tag","Area id is"+areas.getInt(1));

                allAreas.add(area);

            }


            areas.close();
            return allAreas;


        }


        @Override
        protected void onPostExecute(ArrayList<Areas> list) {
            listOfAreas = list;
            adapter = new CustomAdapterShowAllAreas(ShowAllAreas.this, co.uk.inspection.R.layout.singlerowshowallareas, listOfAreas,propertyId,inspectionId);
            showAllAreas.setCheeseList(listOfAreas);
            showAllAreas.setAdapter(adapter);
            showAllAreas.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                adapter.notifyDataSetChanged();

            // check the tick sign
            Log.d("tag", "Position is" + pos);
            showAllAreas.post(new Runnable() {
                @Override
                public void run() {
                    showAllAreas.smoothScrollToPosition(pos);
                    showAllAreas.setSelection(pos);
                }
            });

        }
    }




    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){
                    Log.d("SendEmail","Mail Sent");

                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.i("result", line);


                    JSONObject data = new JSONObject(line);
                    if(data.getString("error").equals("false")) {
                        co.uk.inspection.DBHelper.Globals.filepathToDownload = data.getString("file_path");
                        Log.d("tag","File path is "+co.uk.inspection.DBHelper.Globals.filepathToDownload );
                    }
                }
                reader.close();



                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
                conn.disconnect();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);
            }


            return serverResponseCode;

        } // End else block
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //deleteButton.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDb = new DbHelperClass(this);
        //deleteButton.setVisibility(View.GONE);
        Log.d("tag", "On Resume Call");
       new AllAreasOfUser().execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("tag", "On Stop Call");


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("tag", "On Start Call");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("tag", "On Destroy Call");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("tag", "Back from hardwaer button");
        overridePendingTransition(co.uk.inspection.R.animator.backright, co.uk.inspection.R.animator.backin);
    }


}
