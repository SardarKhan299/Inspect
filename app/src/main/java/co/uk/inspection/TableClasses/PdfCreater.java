package co.uk.inspection.TableClasses;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import co.uk.inspection.Activities.AddNotes;
import co.uk.inspection.Activities.Globals;
import co.uk.inspection.DBHelper.DbHelperClass;
import co.uk.inspection.R;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Android on 4/19/2016.
 */
public class PdfCreater {


    LinkedHashMap<String , ArrayList<AccessoryData>> myHashMap ;
    HashMap<String , ArrayList<ImagesData>> mHashMapForImages;



    Context mContext;
    int inspectionId;
    int propertyId;
    int landLordId;
    String tenantEmail;
    int tenantNumber;
    String tenantAddress;
    String inspectionName,inspectionType;
    String landLordName = "Landlord ";
    String tenantName = "Tenant";
    String fileName;
    String inspectorName ="Inspector";
    int inspectorNumber;
    String inspectorEmail;
    String currentDateTime;


    File myFile;
    DbHelperClass myDB;
    private static Font logoFont = new Font(Font.FontFamily.TIMES_ROMAN,25, Font.BOLD);
    private static Font topHeadingFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    private static Font h1Font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    private static Font tableCellFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
    private static Font h2Font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static Font h3Font = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
    private static Font formFont = new Font(Font.FontFamily.TIMES_ROMAN, 9.5f, Font.NORMAL);
    private static Font formFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 9.5f, Font.NORMAL);
    private static Font clausesFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);
    public PdfCreater()
    {}


   public PdfCreater(Context con,String inspectionName , int inspectionId, int propertyId,String inspectionType)
   {
       mContext = con;
       this.inspectionName = inspectionName;
       this.inspectionId = inspectionId;
       this.propertyId = propertyId;
       myDB = new DbHelperClass(mContext);
       this.inspectionType = inspectionType;
       myHashMap = new LinkedHashMap<String, ArrayList<AccessoryData>>();
       mHashMapForImages = new HashMap<String,ArrayList<ImagesData>>();


   }

    public void createPdf() throws FileNotFoundException, IOException, DocumentException {

        myHashMap = new LinkedHashMap<String, ArrayList<AccessoryData>>();
        ArrayList<AccessoryData> dataList = new ArrayList<AccessoryData>();
        ArrayList<ImagesData> imgList = new ArrayList<ImagesData>();


        // get current Date and Time
        currentDateTime = getDateTime();


     //   Log.d("tag", "HashMap size is " + myHashMap.size());

        // get Inspector Data

        Cursor cInspector = myDB.getInspectorData(1);
        if (cInspector.getCount() == 0) {
            Log.d("tag","No Data to Show");

        } else
        {
            while (cInspector.moveToNext())
            {
                inspectorName = cInspector.getString(0);
                inspectorNumber = cInspector.getInt(1);
                inspectorEmail = cInspector.getString(2);

            }
       //     Log.d("tag","Data of Inspector is "+inspectionName+"Email is "+inspectorEmail);

        }

        // get LandLord Name
        Cursor c1 = myDB.getlanLordId(propertyId);

        if(c1.getCount() == 0)
        {

        }
        else
        {
            while (c1.moveToNext()) {
                landLordId = c1.getInt(0);
                Log.d("tag", "Land Lord Id is " + landLordId);
                Cursor cu = myDB.getlanLordName(landLordId);
                while (cu.moveToNext())
                {
                    landLordName = cu.getString(0);
                }
            }

        }

        Cursor tenantCursor = myDB.getTenantData(propertyId);
        if(tenantCursor.getCount() == 0)
        {}
        else
        {
            while (tenantCursor.moveToNext())
            {
                tenantName = tenantCursor.getString(0);
                tenantNumber = tenantCursor.getInt(1);
                tenantEmail = tenantCursor.getString(2);
                tenantAddress = tenantCursor.getString(3);
            }
        }


        Cursor c = myDB.getAreaDataIdFromDbForPDF(this.propertyId, this.inspectionId);

        if(c.getCount() == 0)
        {
            Log.d("tag", "No Data To Show");
        }





        while(c.moveToNext())
        {
            // Log.d("tag","Values are"+c.getColumnName(0)+c.getColumnName(1)+c.getColumnName(2)+c.getColumnName(3)+c.getColumnName(4)+c.getColumnName(5)+c.getColumnName(6));

       //     Log.d("tag","Area Name is  "+c.getString(1)+"AreaDataid is "+c.getInt(0));



            if(myHashMap.containsKey(c.getString(1)))
            {
       //             Log.d("tag","Value Exist");

                AccessoryData asc = new AccessoryData();

                         Log.d("tag","Area Name is  "+c.getString(1)+"AreaDataid is "+c.getInt(0));
     //                    Log.d("tag", "Asc id is " + c.getLong(7));




                asc.setArea_data_id(c.getInt(0));
                asc.setArea_name(c.getString(1));
                asc.setId(c.getLong(8));
                asc.setAsc_name(c.getString(9));
                asc.setQuality(c.getString(10));
                asc.setCondition(c.getString(11));
                asc.setComments(c.getString(12));

                myHashMap.get(c.getString(1)).add(asc);


            }
            else
            {

        //            Log.d("tag","Value  NOT Exist");
        //        Log.d("tag", "Asc id is " + c.getLong(7));
             dataList = new ArrayList<>();
                AccessoryData asc = new AccessoryData();
                     Log.d("tag","Area Name is  "+c.getString(1)+"AreaDataid is "+c.getInt(0));
             //   Log.d("tag", "Asc id is " + c.getLong(7));

                asc.setArea_data_id(c.getInt(0));
                asc.setArea_name(c.getString(1));
                asc.setId(c.getLong(8));
                asc.setAsc_name(c.getString(9));
                asc.setQuality(c.getString(10));
                asc.setCondition(c.getString(11));
                asc.setComments(c.getString(12));
                dataList.add(asc);

                myHashMap.put(c.getString(1), dataList);

            }





        }









        // to get Images Data

        Cursor cursorImages = myDB.getImagesDataForPDF(this.propertyId, this.inspectionId);

        if(cursorImages.getCount() == 0)
        {
            Log.d("tag", "No Data To Show");
        }

        while (cursorImages.moveToNext())
        {

            if(mHashMapForImages.containsKey(cursorImages.getString(1)))
            {
            //    Log.d("tag","Value Exist");


                ImagesData img = new ImagesData();
                //      Log.d("tag","Area Name is  "+c.getString(1)+"AreaDataid is "+c.getInt(0));
            //    Log.d("tag", "Asc id is " + cursorImages.getLong(7));
                img.setAscName(cursorImages.getString(9));
                img.setId(cursorImages.getInt(16));
                img.setName(cursorImages.getString(17));
                img.setPath(cursorImages.getString(18));
                img.setTime(currentDateTime);


                mHashMapForImages.get(cursorImages.getString(1)).add(img);


            }
            else
            {

//                Log.d("tag","Value  NOT Exist");
              //  Log.d("tag", "Asc id is " + cursorImages.getLong(7));

                imgList = new ArrayList<ImagesData>();
                ImagesData img = new ImagesData();
                //      Log.d("tag","Area Name is  "+c.getString(1)+"AreaDataid is "+c.getInt(0));
             //   Log.d("tag", "Asc id is " + cursorImages.getLong(7));
                img.setAscName(cursorImages.getString(9));
                img.setId(cursorImages.getInt(16));
                img.setName(cursorImages.getString(17));
                img.setPath(cursorImages.getString(18));
                img.setTime(currentDateTime);
                Log.d("tag", "Path is  " + cursorImages.getString(17));

                imgList.add(img);

                mHashMapForImages.put(cursorImages.getString(1), imgList);

            }

        }



        //create pdf in documents directory
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "mypdf");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i("tag", "Pdf Directory created");
        }


        // check if file existPath exist in db or not If exist open the file and do changes in old file
        // if not exist create the new file and save its path in db for later use



        Cursor fileCursor = myDB.checkFilePath(propertyId , inspectionId , inspectionName , inspectionType);
//        if(fileCursor.getCount() == 0)
//        {
//            // get Current Date and Time and create the file
//            Date date = new Date() ;
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
//             fileName = inspectionName + timeStamp;
//            myFile = new File(pdfFolder,fileName + ".pdf");
//
//        }
         if(fileCursor.getCount() > 0)
        {
            while (fileCursor.moveToNext()) {

                fileName = fileCursor.getString(0);

                if(fileName != null && !fileName.isEmpty() )
                {
                    Log.d("tag", "File Name from Db is " + fileName);

                    myFile = new File(pdfFolder, fileName + ".pdf");

                }
                else {


                    UUID id = UUID.randomUUID();
                    fileName = inspectionName +id;
                    myFile = new File(pdfFolder,fileName + ".pdf");
                    Log.d("tag", "File Name not exist  is " + fileName);

                }

            }

        }


        int rowUpdated = myDB.updateInspectionFileData(propertyId,inspectionId,inspectionName,inspectionType,fileName,myFile.getAbsolutePath());
        Log.d("tag","Number of Rows Updated is "+rowUpdated+"File Path is "+myFile.getAbsolutePath());
        OutputStream output = new FileOutputStream(myFile);




        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();

        //Step 4 Add content
        Paragraph para = new Paragraph("Tenant Find",logoFont);
        para.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(para, 1);
        document.add(para);

// add image to pdf
        Drawable d = mContext.getResources().getDrawable(co.uk.inspection.R.drawable.logopdf);
        BitmapDrawable bitDw = ((BitmapDrawable) d);
        Bitmap bmp = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        image.setAbsolutePosition(350f, 620f);
        image.scaleAbsolute(180f, 180f);
        document.add(image);

        para = new Paragraph("237 Lincoln Road",h1Font);
        para.setAlignment(Element.ALIGN_LEFT);
        document.add(para);

        para = new Paragraph("Peterborough",h1Font);
        para.setAlignment(Element.ALIGN_LEFT);
        document.add(para);

        para = new Paragraph("PE1 2PL",h1Font);
        para.setAlignment(Element.ALIGN_LEFT);
        document.add(para);

        para = new Paragraph("United Kingdom",h1Font);
        para.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(para, 3);
        document.add(para);


        para = new Paragraph(this.inspectionType,logoFont);
        para.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(para, 1);
        document.add(para);




        LineSeparator line = new LineSeparator(
                5, 100, null, Element.ALIGN_CENTER, -2);
        document.add(line);



        PdfPTable tab = new PdfPTable(2);

        tab.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        tab.setHorizontalAlignment(Element.ALIGN_LEFT);

        Paragraph ph = new Paragraph(" 28 Craig Street ",h1Font);
        PdfPCell ce = new PdfPCell();
        ce.setBorder(Rectangle.NO_BORDER);
        ce.setMinimumHeight(30f);
        ce.setVerticalAlignment(Element.ALIGN_MIDDLE);
        ce.addElement(ph);
        tab.addCell(ce);

        ph = new Paragraph(" Report Generated : "+currentDateTime,h1Font);
        ce = new PdfPCell();
        ce.setBorder(Rectangle.NO_BORDER);
        ce.setMinimumHeight(30f);
        ph.setAlignment(Element.ALIGN_RIGHT);
        ce.addElement(ph);
        tab.addCell(ce);

        ph = new Paragraph("Peterborough",h1Font);
        ce = new PdfPCell();
        ce.setBorder(Rectangle.NO_BORDER);
        ce.setMinimumHeight(30f);
        ce.setVerticalAlignment(Element.ALIGN_MIDDLE);
        ce.addElement(ph);
        tab.addCell(ce);


        ph = new Paragraph(inspectorName,h1Font);
        ce = new PdfPCell();
        ce.setBorder(Rectangle.NO_BORDER);
        ce.setMinimumHeight(30f);
        ce.setVerticalAlignment(Element.ALIGN_RIGHT);
        ph.setAlignment(Element.ALIGN_RIGHT);
        ce.addElement(ph);
        tab.addCell(ce);


        ph = new Paragraph("United Kingdom",h1Font);
        ce = new PdfPCell();
        ce.setBorder(Rectangle.NO_BORDER);
        ce.setMinimumHeight(30f);
        ce.setVerticalAlignment(Element.ALIGN_MIDDLE);
        ce.addElement(ph);
        tab.addCell(ce);


        ph = new Paragraph(Integer.toString(inspectorNumber),h1Font);
        ce = new PdfPCell();
        ce.setBorder(Rectangle.NO_BORDER);
        ce.setMinimumHeight(30f);
        ph.setAlignment(Element.ALIGN_RIGHT);
        ce.setVerticalAlignment(Element.ALIGN_MIDDLE);
        ce.addElement(ph);
        tab.addCell(ce);


//        ph = new Paragraph("",h1Font);
//        ce = new PdfPCell();
//        ce.setBorder(Rectangle.NO_BORDER);
//        ce.setMinimumHeight(30f);
//        ce.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        ce.addElement(ph);
//        tab.addCell(ce);

// Inspector Email

//        ph = new Paragraph(inspectorEmail,h1Font);
//        ce = new PdfPCell();
//        ce.setBorder(Rectangle.NO_BORDER);
//        ce.setMinimumHeight(30f);
//        ph.setAlignment(Element.ALIGN_RIGHT);
//        ce.setVerticalAlignment(Element.ALIGN_RIGHT);
//        ce.addElement(ph);
//        tab.addCell(ce);




        addEmptyLine(ph, 3);

        document.add(tab);






        LineSeparator line11 = new LineSeparator(
                5, 100, null, Element.ALIGN_CENTER, -2);
        document.add(line11);



        PdfPTable table11 = new PdfPTable(2);

        table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table11.setHorizontalAlignment(Element.ALIGN_LEFT);

        Paragraph phrase = new Paragraph("Landlord ",topHeadingFont);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setMinimumHeight(30f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(phrase);
        table11.addCell(cell);

        phrase = new Paragraph("Tenant",topHeadingFont);
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setMinimumHeight(30f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(phrase);
        table11.addCell(cell);

        phrase = new Paragraph(landLordName,h1Font);
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setMinimumHeight(30f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(phrase);
        table11.addCell(cell);


        phrase = new Paragraph(tenantName,h1Font);
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setMinimumHeight(30f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(phrase);
        table11.addCell(cell);


        phrase = new Paragraph("",h1Font);
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setMinimumHeight(30f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(phrase);
        table11.addCell(cell);


        phrase = new Paragraph(Integer.toString(tenantNumber),h1Font);
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setMinimumHeight(30f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(phrase);
        table11.addCell(cell);


        phrase = new Paragraph("",h1Font);
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setMinimumHeight(30f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(phrase);
        table11.addCell(cell);


        phrase = new Paragraph(tenantEmail,h1Font);
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setMinimumHeight(30f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(phrase);
        table11.addCell(cell);


        phrase = new Paragraph("",h1Font);
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setMinimumHeight(30f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(phrase);
        table11.addCell(cell);


        phrase = new Paragraph(tenantAddress,h1Font);
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setMinimumHeight(30f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(phrase);
        table11.addCell(cell);


        addEmptyLine(phrase, 3);

        document.add(table11);



        LineSeparator line12 = new LineSeparator(
                5, 100, null, Element.ALIGN_CENTER, -2);
        document.add(line12);






        int id = 0;
        boolean isbreak = false;
        int previousValue =0;
        String name = null;





//    Log.d("tag","Area name is "+area.getName());
//
//    if(name == null)
//        name = area.getName();
//
//    para = new Paragraph(area.getName(), topHeadingFont);
//    para.setAlignment(Element.ALIGN_LEFT);
//    addEmptyLine(para, 2);
//    document.add(para);

        Log.d("tag", "Size of hashmap is " + myHashMap.size());

//
//    para = new Paragraph(areaData.get(j).toString(), topHeadingFont);
//    para.setAlignment(Element.ALIGN_LEFT);
//    addEmptyLine(para, 2);
//    document.add(para);



        PdfPTable table = new PdfPTable(4); // 3 columns.


        float[] columnWidths = {1f, 1f, 1f, 2f};

        table.setWidths(columnWidths);

        PdfPCell cell1 = new PdfPCell(new Paragraph("Name ", topHeadingFont));
        PdfPCell cell2 = new PdfPCell(new Paragraph("Quality", topHeadingFont));
        PdfPCell cell3 = new PdfPCell(new Paragraph("Condition", topHeadingFont));
        PdfPCell cell4 = new PdfPCell(new Paragraph("Comments", topHeadingFont));
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);


        for (Map.Entry<String, ArrayList<AccessoryData>> entry : myHashMap.entrySet()) {
            String key = entry.getKey();
            ArrayList<AccessoryData> value = entry.getValue();

       Log.d("tag","Key is "+key.toString());

                para = new Paragraph(key.toString(), topHeadingFont);
    para.setAlignment(Element.ALIGN_LEFT);
    addEmptyLine(para, 2);
    document.add(para);

            document.add(table);

            for(AccessoryData asc : value) {



                PdfPTable table1 = new PdfPTable(4); // 3 columns.


                float[] columnWidths1 = {1f, 1f, 1f, 2f};

                table1.setWidths(columnWidths1);

                Log.d("tag","Value is "+asc.getAsc_name());

                // draw table here
                PdfPCell cell11 = new PdfPCell(new Paragraph(asc.getAsc_name(), tableCellFont));
                PdfPCell cell21 = new PdfPCell(new Paragraph(asc.getQuality(), tableCellFont));
                PdfPCell cell31 = new PdfPCell(new Paragraph(asc.getCondition(), tableCellFont));
                PdfPCell cell41 = new PdfPCell(new Paragraph(asc.getComments(), tableCellFont));

                table1.addCell(cell11);
                table1.addCell(cell21);
                table1.addCell(cell31);
                table1.addCell(cell41);

                document.add(table1);


            }





            // ...
        }




      // landlord Signature
        para = new Paragraph("Landlord Signature",topHeadingFont);
        para.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(para, 1);
        document.add(para);

        LineSeparator line13 = new LineSeparator(
                5, 100, null, Element.ALIGN_CENTER, -2);
        document.add(line13);

        Paragraph p1 = new Paragraph("");
        addEmptyLine(p1,2);
        document.add(p1);


        PdfPTable table1 = new PdfPTable(1); // 1 columns.

        PdfPCell cell31 = new PdfPCell();
        cell31.setBorder(Rectangle.BOX);
        cell31.setMinimumHeight(100f);
        cell31.setVerticalAlignment(Element.ALIGN_LEFT);

        Paragraph para1 = new Paragraph("", topHeadingFont);
        para1.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(para1, 1);
        cell31.addElement(para1);

        PdfPCell cell32 = new PdfPCell();
        cell32.setBorder(Rectangle.NO_BORDER);
        cell32.setMinimumHeight(10f);
        cell32.setVerticalAlignment(Element.ALIGN_LEFT);

        Paragraph para0 = new Paragraph(landLordName, topHeadingFont);
        para0.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(para0, 1);


        cell31.addElement(para1);
        cell32.addElement(para0);

        table1.addCell(cell31);
        table1.addCell(cell32);

        document.add(table1);




  //  Log.d("tag", "Size of ArrayList is " + areaData.size());


//        id = ascData.get(i).getArea_data_id();
        //  Log.d("tag","Id is "+id);


        //  Log.d("tag", "Size is " + ascData.size() + "i value is " + i);
        //   Log.d("tag","Id is "+id);

//


        Paragraph paras = new Paragraph("Photos", topHeadingFont);
        paras.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(paras, 1);
        document.add(paras);

        LineSeparator line14 = new LineSeparator(
                5, 100, null, Element.ALIGN_CENTER, -2);
        document.add(line14);

        Paragraph p2 = new Paragraph("");
        addEmptyLine(p2, 2);
        document.add(p2);


        for (Map.Entry<String, ArrayList<ImagesData>> entry : mHashMapForImages.entrySet()) {
            String key = entry.getKey();
            ArrayList<ImagesData> value = entry.getValue();

      //      Log.d("tag", "Key is " + key.toString());

            para = new Paragraph(key.toString(), topHeadingFont);
            para.setAlignment(Element.ALIGN_LEFT);
            addEmptyLine(para, 2);
            document.add(para);

            PdfPTable table01 = new PdfPTable(3); // 3 columns.

            float[] columnWidths02 = {1f,1f,1f};

            table01.setWidths(columnWidths02);


            for(ImagesData img : value) {



                PdfPCell cell02 = new PdfPCell( new Paragraph(img.getPath(), tableCellFont));
                cell02.setBorder(Rectangle.NO_BORDER);
                // draw table here



                if(img.getPath() != null) {



               //     Log.d("tag","Value is "+img.getPath());

                    File file = new File(img.getPath());
                    long length = file.length() / 1024; // Size in KB
                 //   Log.d("tag","Size of Image is "+   length);

                    if(length > 0 ) {


                        // use to compress the image
                        Bitmap bitmap = compressImage(img.getPath());

                        ByteArrayOutputStream st = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, st);
                        byte[] byteArray = st.toByteArray();


                        Image im = Image.getInstance(byteArray);
                        // it reduce the image size
                      //  im.setCompressionLevel(1);
                        cell02.addElement(im);
                    }
                    cell02.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell02.addElement(new Paragraph(img.getAscName(), tableCellFont));
                    cell02.addElement(new Paragraph(img.getTime(), tableCellFont));
                    cell02.setBorder(Rectangle.NO_BORDER);
                    cell02.setPaddingBottom(10);

                   // cell02.setFixedHeight(250f);
                  //  cell02.setPadding(10);

                    // split the Date and Time
                    String[] timeandDate = img.getTime().split(" ");
                 //   Log.d("tag", "date is " + timeandDate[0] + "Time is " + timeandDate[1]);

                    table01.addCell(cell02);


                }







            }


            table01.completeRow();
            table01.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            document.add(table01);


            // ...
        }



        // landlord Signature
        para = new Paragraph("Tenant Signature",topHeadingFont);
        para.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(para, 1);
        document.add(para);

        LineSeparator line15 = new LineSeparator(
                5, 100, null, Element.ALIGN_CENTER, -2);
        document.add(line15);

        Paragraph p11 = new Paragraph("");
        addEmptyLine(p11,2);
        document.add(p11);


        PdfPTable table01 = new PdfPTable(1); // 1 columns.

        PdfPCell cell031 = new PdfPCell();
        cell031.setBorder(Rectangle.BOX);
        cell031.setMinimumHeight(100f);
        cell031.setVerticalAlignment(Element.ALIGN_LEFT);

        Paragraph para01 = new Paragraph("", topHeadingFont);
        para01.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(para01, 1);
        cell031.addElement(para01);

        PdfPCell cell032 = new PdfPCell();
        cell032.setBorder(Rectangle.NO_BORDER);
        cell032.setMinimumHeight(10f);
        cell032.setVerticalAlignment(Element.ALIGN_LEFT);

        Paragraph para00 = new Paragraph(tenantName, topHeadingFont);
        para00.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(para00, 1);


        cell031.addElement(para01);
        cell032.addElement(para00);

        table01.addCell(cell031);
        table01.addCell(cell032);

        document.add(table01);






        SharedPreferences myPrefs = mContext.getSharedPreferences("myPrefs", mContext.MODE_WORLD_READABLE);
        String notesText = myPrefs.getString(AddNotes.NOTES,"Notes");

        para = new Paragraph("Notes",topHeadingFont);
        para.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(para, 1);
        document.add(para);

        Log.d("tag", "Notes are" + notesText);

        LineSeparator linenotes = new LineSeparator(
                5, 100, null, Element.ALIGN_CENTER, -2);
        document.add(linenotes);


        Paragraph pEmpty = new Paragraph("");
        addEmptyLine(pEmpty,2);
        document.add(pEmpty);

        PdfPTable tableNotes = new PdfPTable(1); // 1 columns.




// cell with text of notes
        PdfPCell cellNoteText = new PdfPCell();
        cellNoteText.setBorder(Rectangle.BOX);
        cellNoteText.setMinimumHeight(100f);
        cellNoteText.setVerticalAlignment(Element.ALIGN_LEFT);

        Paragraph paraNotes = new Paragraph(notesText, h1Font);
        para1.setAlignment(Element.ALIGN_LEFT);
        addEmptyLine(para1, 1);
        cellNoteText.addElement(paraNotes);



        tableNotes.addCell(cellNoteText);


        document.add(tableNotes);




        //Step 5: Close the document
        document.close();
        myDB.close();

    }


    public Bitmap compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 600.0f;
        float maxWidth = 400.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
     //       Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
       //         Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
         //       Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
        //        Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }



        return  scaledBitmap;

    }



    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = mContext.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    public void viewPdf(){

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent1 = Intent.createChooser(intent, "Open File");
        try {
            mContext.startActivity(intent1);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
            Toast.makeText(mContext,"you donot have any pdf viewer kindly install it",Toast.LENGTH_SHORT).show();
        }

    }

// check if intent is available
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public void emailNote()
    {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_SUBJECT, "My subject");
        email.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(new StringBuilder()
                .append("<p><b>Link to Donwload The File \n </b></p>")
                .append("<a>"+ co.uk.inspection.DBHelper.Globals.filepathToDownload+"</a>")
                .toString()));
        Log.d("tag", "Path is " + co.uk.inspection.DBHelper.Globals.filepathToDownload);
        email.setType("message/rfc822");
        mContext.startActivity(email);
    }


    public void promptForNextAction()
    {
        final String[] options = { "email", "preview",
                "cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("PDF Created choose option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("email")) {
                    emailNote();
                } else if (options[which].equals("preview")) {
                    viewPdf();
                } else if (options[which].equals("cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();

    }


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }






}
