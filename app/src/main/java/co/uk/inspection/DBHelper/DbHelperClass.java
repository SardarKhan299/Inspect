package co.uk.inspection.DBHelper;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

public class DbHelperClass extends SQLiteOpenHelper {


    // All Static variables

    // Database Version change when the changes in Database
    // Always change number when changes in database otherwise changes will not occur
    public static final int DATABASE_VERSION = 61;

    // Database Name
    public static final String DATABASE_NAME = "propertyInspection.sqlite";

    //table names
    public static final String TABLE_CONTACTS = "property";
    public static final String TABLE_LANDLORDS = "landlord";
    public static final String TABLE_TENANTS = "tenant";
    public static final String TABLE_INSPECTIONS = "inspection";
    public static final String TABLE_INSPECTOR = "inspector";
    public static final String TABLE_AREAS = "areas";
    public static final String TABLE_ACCESSORIES = "accessories";
    public static final String TABLE_AREA_ASC = "area_asc";
    public static final String TABLE_AREA_DATA_SAVE = "area_data";
    public static final String TABLE_ACCESSORY_DATA_SAVE = "asc_data";
    public static final String TABLE_IMAGES ="images";


    // Contacts Table Columns names for Property Table
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_ZIP_CODE = "zip";
    public static final String FOREIGNKEY_ID = "pid";
    public static final String FOREIGNKEY_LANDLORD = "lid";
    public static final String KEY_VISIBILITY = "visiblity";

    // for landlord
    public static final String LKEY_ID = "id";
    public static final String LKEY_NAME = "name";
    public static final String LKEY_ADDRESS = "address";
    public static final String LKEY_NUMBER = "number";
    public static final String LKEY_EMAIL = "email";

    // for tenant
    public static final String TKEY_ID = "id";
    public static final String TKEY_NAME = "name";
    public static final String TKEY_ADDRESS = "address";
    public static final String TKEY_NUMBER = "number";
    public static final String TKEY_EMAIL = "email";

    // for inspection
    public static final String IKEY_ID = "id";
    public static final String IKEY_NAME = "name";
    public static final String IKEY_TYPE = "type";
    public static final String FILENAME = "fileName";
    public static final String FILEPATH = "filePath";
    public static final String FOREIGNKEY_PROPERTY_ID = "pid";
    public static final String FOREIGNKEY_INSPECTOR_ID = "Inspectorid";


    // for inspector
    public static final String InsKEY_ID = "id";
    public static final String InsKEY_NAME = "name";
    public static final String InsKEY_NUM = "num";
    public static final String InsKEY_EMAIL = "email";

    // for areas
    public static final String AKEY_ID = "id";
    public static final String AKEY_NAME = "name";
    public static final String SORTORDERAREA = "sortorder";




    // for accessories
    public static final String ASKEY_ID = "id";
    public static final String ASKEY_NAME = "name";


    // for area_asc
    public static final String AREA_ASC_ID = "id";
    public static final String FOREIGN_KEY_AREA_ID = "aid";
    public static final String FOREIGN_KEY_ASC_ID = "ascid";


    // for area_data
    public static final String AREA_DATA_ID = "area_data_id";
    public static final String FOREIGNKEY_AREA_ID = "aid";
    public static final String FOREIGN_KEY_INPECTION_ID ="ins_id";
    public static final String AREA_NAME = "area_name";
    public static final String ISCOMPLETE = "iscomplete";
    public static final String SORTORDER = "sortorder";

    // for accessory_data
    public static final String ACCESSORY_DATA_ID = "asc_data_id";
    public static final String ASC_NAME ="asc_name";
    public static final String QUALITY = "quality";
    public static final String CONDITION ="condition";
    public static final String COMMENTS = "comments";





    // for images
    public static final String IMAGE_ID="id";
    public static final String IMAGE_PATH ="path";
    public static final String IMAGE_NAME="name";
    public static final String DATETIME = "datetime";





    long last_inserted_id_property;
    long last_inserted_id_landlord;
    long selected_property_id;
    long last_inserted_InspectorId;
    long last_inserted_inpection_id;
    long last_inserted_areas_id;
    long last_inserted_accessories_id;
    long last_inserted_area_asc_id;
    long selected_Area_id;
    long last_inserted_area_data_id;
    long last_inserted_inspection_ascWithComments_id;
    long last_inserted_accessory_data_id;
    long last_inserted_image_id;

    SQLiteDatabase db;

    public DbHelperClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub\


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub



        db.execSQL("PRAGMA foreign_keys = ON;");


        String CREATE_LANDLORD_TABLE = "CREATE TABLE " + TABLE_LANDLORDS + "("
                + LKEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LKEY_NAME + " TEXT," + LKEY_ADDRESS + " TEXT," + LKEY_EMAIL + " TEXT,"
                + LKEY_NUMBER + " TEXT" + ")";

        // property Table
        String CREATE_PROPERTY_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + FOREIGNKEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_ADDRESS + " TEXT,"
                + KEY_ZIP_CODE + " TEXT,"
                + KEY_VISIBILITY + " INTEGER,"
                + FOREIGNKEY_LANDLORD + " INTEGER,"
                + " FOREIGN KEY (" + FOREIGNKEY_LANDLORD + ") REFERENCES " + TABLE_LANDLORDS + "(" + LKEY_ID + "))";


        String CREATE_TENANT_TABLE = "CREATE TABLE " + TABLE_TENANTS + "("
                + TKEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TKEY_NAME + " TEXT," + TKEY_ADDRESS + " TEXT," + TKEY_EMAIL + " TEXT,"
                + TKEY_NUMBER + " TEXT,"
                + FOREIGNKEY_ID + " INTEGER,"
                + " FOREIGN KEY (" + FOREIGNKEY_ID + ") REFERENCES " + TABLE_CONTACTS + "(" + FOREIGNKEY_ID + "))";

        String CREATE_INSPECTOR_TABLE = "CREATE TABLE " + TABLE_INSPECTOR + "("
                + InsKEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + InsKEY_NAME + " TEXT," + InsKEY_NUM + " TEXT,"
                + InsKEY_EMAIL + " TEXT" + ")";

        String CREATE_INSPECTION_TABLE = "CREATE TABLE " + TABLE_INSPECTIONS + "("
                + IKEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + IKEY_NAME + " TEXT,"
                + IKEY_TYPE + " TEXT,"
                + FILENAME + " TEXT,"
                + FILEPATH + " TEXT,"
                + FOREIGNKEY_PROPERTY_ID + " INTEGER,"
                + FOREIGNKEY_INSPECTOR_ID + " INTEGER,"
                + KEY_VISIBILITY + " INTEGER,"
                + " FOREIGN KEY (" + FOREIGNKEY_PROPERTY_ID + ") REFERENCES " + TABLE_CONTACTS + "(" + FOREIGNKEY_ID + "),"
                + " FOREIGN KEY (" + FOREIGNKEY_INSPECTOR_ID + ") REFERENCES " + TABLE_INSPECTOR + "(" + InsKEY_ID + "))";


        String CREATE_AREA_TABLE = "CREATE TABLE " + TABLE_AREAS + "("
                + AKEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_VISIBILITY + " INTEGER,"
                + SORTORDERAREA + " INTEGER,"
                + AKEY_NAME + " TEXT" + ")";

        String CREATE_ACCESSORIES_TABLE = "CREATE TABLE " + TABLE_ACCESSORIES + "("
                + ASKEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_VISIBILITY + " INTEGER,"
                + ASKEY_NAME + " TEXT" + ")";


        // join table for Areas and Accessories
        String CREATE_AREA_ASC_TABLE = "CREATE TABLE " + TABLE_AREA_ASC + "("
                + AREA_ASC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FOREIGN_KEY_AREA_ID + " INTEGER,"
                + FOREIGN_KEY_ASC_ID + " INTEGER,"
                + " FOREIGN KEY (" + FOREIGN_KEY_AREA_ID + ") REFERENCES " + TABLE_AREAS + "(" + AKEY_ID + "),"
                + " FOREIGN KEY (" + FOREIGN_KEY_ASC_ID + ") REFERENCES " + TABLE_ACCESSORIES + "(" + ASKEY_ID + "))";


        String CREATE_AREA_DATA = "CREATE TABLE " + TABLE_AREA_DATA_SAVE + "("
                + AREA_DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + AREA_NAME + " TEXT,"
                + KEY_VISIBILITY + " INTEGER,"
                + FOREIGN_KEY_AREA_ID + " INTEGER,"
                + FOREIGNKEY_PROPERTY_ID + " INTEGER,"
                + FOREIGN_KEY_INPECTION_ID + " INTEGER,"
                + ISCOMPLETE + " INTEGER,"
                + SORTORDER + " INTEGER,"
                + " FOREIGN KEY (" + FOREIGN_KEY_AREA_ID + ") REFERENCES " + TABLE_AREAS + "(" + AKEY_ID + "),"
                + " FOREIGN KEY (" + FOREIGNKEY_PROPERTY_ID + ") REFERENCES " + TABLE_CONTACTS + "(" + FOREIGNKEY_ID + "),"
                + " FOREIGN KEY (" + FOREIGN_KEY_INPECTION_ID + ") REFERENCES " + TABLE_INSPECTIONS + "(" + IKEY_ID + "))";


        String CREATE_ACCESSORY_DATA = "CREATE TABLE " + TABLE_ACCESSORY_DATA_SAVE + "("
                + ACCESSORY_DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ASC_NAME + " TEXT,"
                + QUALITY + " TEXT,"
                + CONDITION + " TEXT,"
                + COMMENTS + " TEXT,"
                + FOREIGN_KEY_ASC_ID + " INTEGER,"
                + AREA_DATA_ID + " INTEGER,"
                + KEY_VISIBILITY + " INTEGER,"
                + " FOREIGN KEY (" + FOREIGN_KEY_ASC_ID + ") REFERENCES " + TABLE_ACCESSORIES + "(" + ASKEY_ID + "),"
                + " FOREIGN KEY (" + AREA_DATA_ID + ") REFERENCES " + TABLE_AREA_DATA_SAVE + "(" + AREA_DATA_ID + "))";







        String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_IMAGES + "("
                + IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + IMAGE_NAME + " TEXT,"
                + IMAGE_PATH + " TEXT,"
                + ACCESSORY_DATA_ID + " INTEGER,"
                + DATETIME + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + " FOREIGN KEY (" + ACCESSORY_DATA_ID + ") REFERENCES " + TABLE_ACCESSORY_DATA_SAVE + "(" + ACCESSORY_DATA_ID + "))";


// now insert data into join table


        db.execSQL(CREATE_LANDLORD_TABLE);
        db.execSQL(CREATE_PROPERTY_TABLE);
        db.execSQL(CREATE_TENANT_TABLE);
        db.execSQL(CREATE_INSPECTOR_TABLE);
        db.execSQL(CREATE_INSPECTION_TABLE);
        db.execSQL(CREATE_AREA_TABLE);
        db.execSQL(CREATE_ACCESSORIES_TABLE);
        db.execSQL(CREATE_AREA_ASC_TABLE);
        db.execSQL(CREATE_AREA_DATA);
        db.execSQL(CREATE_ACCESSORY_DATA);
        db.execSQL(CREATE_IMAGE_TABLE);


        this.db = db;

        // insert data in Areas of Inspections
        insertAreasData("Entry 1",1,0);
        insertAreasData("Living Room 1",1,1);
        insertAreasData("Dinning Room 1",1,2);
        insertAreasData("Kitchen 1",1,3);
        insertAreasData("Bedroom 1",1,4);
        insertAreasData("Bathroom 1",1,5);
        insertAreasData("Garage 1",1,6);
        insertAreasData("Storage Area 1",1,7);
        insertAreasData("Mechanical 1",1,8);
        insertAreasData("Other", 0,9);

        // insert Data in Asseccories Table
        insertAccessoriesData("Door Bell",1);
        insertAccessoriesData("Front Door",1);
        insertAccessoriesData("Screen Door",1);
        insertAccessoriesData("Walls",1);
        insertAccessoriesData("Ceiling",1);
        insertAccessoriesData("Windows",1);
        insertAccessoriesData("Screens",1);
        insertAccessoriesData("Window Coverings",1);
        insertAccessoriesData("Flooring",1);
        insertAccessoriesData("Base Boards",1);
        insertAccessoriesData("Lightning",1);
        insertAccessoriesData("Doors",1);
        insertAccessoriesData("Power Outlets",1);
        insertAccessoriesData("Air Conditioning",1);
        insertAccessoriesData("Cabenits",1);
        insertAccessoriesData("Lights",1);
        insertAccessoriesData("Counter Top",1);
        insertAccessoriesData("Refrigerator",1);
        insertAccessoriesData("Oven",1);
        insertAccessoriesData("Stove Top",1);
        insertAccessoriesData("Exhaust Vent",1);
        insertAccessoriesData("Sink",1);
        insertAccessoriesData("Garbage Disposal",1);
        insertAccessoriesData("Micorwave",1);
        insertAccessoriesData("Dishwasher",1);
        insertAccessoriesData("Tiling",1);
        insertAccessoriesData("Mirror",1);
        insertAccessoriesData("Closet",1);
        insertAccessoriesData("Pantry",1);
        insertAccessoriesData("Towel Rack",1);
        insertAccessoriesData("Shower",1);
        insertAccessoriesData("Shower Door",1);
        insertAccessoriesData("Bath",1);
        insertAccessoriesData("Faucet",1);
        insertAccessoriesData("Exhaust Fan",1);
        insertAccessoriesData("Toilet Bowl",1);
        insertAccessoriesData("Toilet Seat",1);
        insertAccessoriesData("Toilet Paper Holder",1);
        insertAccessoriesData("Garage Door",1);
        insertAccessoriesData("Water Heater",1);
        insertAccessoriesData("Furnance",1);
        insertAccessoriesData("Air Conditioner",1);
        insertAccessoriesData("A/C Filter",1);
        insertAccessoriesData("Washer",1);
        insertAccessoriesData("Dryer",1);
        insertAccessoriesData("Smoke Detector",1);
        insertAccessoriesData("CO Detector",1);
        insertAccessoriesData("Fire Extinguisher",1);
        insertAccessoriesData("Thermostat",1);
        insertAccessoriesData("Ceiling Fan",1);
        insertAccessoriesData("Others",0);

        // insert join table data
        // for entry
        insertAreaAscData(1, 1);
        insertAreaAscData(1, 2);
        insertAreaAscData(1, 3);
        insertAreaAscData(1, 4);
        insertAreaAscData(1, 5);
        insertAreaAscData(1, 6);
        insertAreaAscData(1, 7);
        insertAreaAscData(1, 8);
        insertAreaAscData(1, 9);
        insertAreaAscData(1, 10);
        insertAreaAscData(1, 11);
        // for living room
        insertAreaAscData(2, 4);
        insertAreaAscData(2, 5);
        insertAreaAscData(2, 12);
        insertAreaAscData(2, 6);
        insertAreaAscData(2, 7);
        insertAreaAscData(2, 8);
        insertAreaAscData(2, 9);
        insertAreaAscData(2, 10);
        insertAreaAscData(2, 16);
        insertAreaAscData(2, 13);
        insertAreaAscData(2, 50);
        // for dinnig room
        insertAreaAscData(3, 4);
        insertAreaAscData(3, 5);
        insertAreaAscData(3, 12);
        insertAreaAscData(3, 6);
        insertAreaAscData(3, 7);
        insertAreaAscData(3, 8);
        insertAreaAscData(3, 9);
        insertAreaAscData(3, 10);
        insertAreaAscData(3, 16);
        insertAreaAscData(3, 13);
        insertAreaAscData(3, 50);
        //for kitchen
        insertAreaAscData(4, 4);
        insertAreaAscData(4, 5);
        insertAreaAscData(4, 12);
        insertAreaAscData(4, 6);
        insertAreaAscData(4, 7);
        insertAreaAscData(4, 8);
        insertAreaAscData(4, 9);
        insertAreaAscData(4, 10);
        insertAreaAscData(4, 16);
        insertAreaAscData(4, 14);
        insertAreaAscData(4, 13);
        insertAreaAscData(4, 15);
        insertAreaAscData(4, 17);
        insertAreaAscData(4, 29);
        insertAreaAscData(4, 18);
        insertAreaAscData(4, 19);
        insertAreaAscData(4, 20);
        insertAreaAscData(4, 21);
        insertAreaAscData(4, 22);
        insertAreaAscData(4, 23);
        insertAreaAscData(4, 24);
        insertAreaAscData(4, 25);
        insertAreaAscData(4, 26);
        // for bedroom
        insertAreaAscData(5, 4);
        insertAreaAscData(5, 5);
        insertAreaAscData(5, 12);
        insertAreaAscData(5, 6);
        insertAreaAscData(5, 7);
        insertAreaAscData(5, 8);
        insertAreaAscData(5, 9);
        insertAreaAscData(5, 10);
        insertAreaAscData(5, 16);
        insertAreaAscData(5, 13);
        insertAreaAscData(5, 28);
        insertAreaAscData(5, 27);
        // for bathroom
        insertAreaAscData(6, 4);
        insertAreaAscData(6, 5);
        insertAreaAscData(6, 12);
        insertAreaAscData(6, 6);
        insertAreaAscData(6, 7);
        insertAreaAscData(6, 8);
        insertAreaAscData(6, 9);
        insertAreaAscData(6, 10);
        insertAreaAscData(6, 16);
        insertAreaAscData(6, 13);
        insertAreaAscData(6, 15);
        insertAreaAscData(6, 30);
        insertAreaAscData(6, 17);
        insertAreaAscData(6, 31);
        insertAreaAscData(6, 32);
        insertAreaAscData(6, 33);
        insertAreaAscData(6, 27);
        insertAreaAscData(6, 22);
        insertAreaAscData(6, 34);
        insertAreaAscData(6, 26);
        insertAreaAscData(6, 35);
        insertAreaAscData(6, 36);
        insertAreaAscData(6, 37);
        insertAreaAscData(6, 38);
        // for garage
        insertAreaAscData(7, 4);
        insertAreaAscData(7, 5);
        insertAreaAscData(7, 12);
        insertAreaAscData(7, 6);
        insertAreaAscData(7, 7);
        insertAreaAscData(7, 8);
        insertAreaAscData(7, 9);
        insertAreaAscData(7, 10);
        insertAreaAscData(7, 16);
        insertAreaAscData(7, 13);
        insertAreaAscData(7, 39);
        // for storage area
        insertAreaAscData(8, 4);
        insertAreaAscData(8, 5);
        insertAreaAscData(8, 12);
        insertAreaAscData(8, 6);
        insertAreaAscData(8, 7);
        insertAreaAscData(8, 9);
        insertAreaAscData(8, 10);
        insertAreaAscData(8, 16);
        insertAreaAscData(8, 13);

        // for mechanical
        insertAreaAscData(9, 40);
        insertAreaAscData(9, 41);
        insertAreaAscData(9, 42);
        insertAreaAscData(9, 43);
        insertAreaAscData(9, 44);
        insertAreaAscData(9, 45);
        insertAreaAscData(9, 46);
        insertAreaAscData(9, 47);
        insertAreaAscData(9, 48);
        insertAreaAscData(9, 49);

        // for other
        insertAreaAscData(10, 4);
        insertAreaAscData(10, 5);
        insertAreaAscData(10, 9);
        insertAreaAscData(10, 12);
        insertAreaAscData(10, 16);



    }

    // this method call when the version of the database is changed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        Log.d("tag", "OnUpgrade Call");

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANDLORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TENANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSPECTOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSPECTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AREAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCESSORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AREA_ASC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AREA_DATA_SAVE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCESSORY_DATA_SAVE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);



        // Create tables again
        onCreate(db);

    }

    // Insert Data To Db

    public boolean insertLandLordData(String name) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(LKEY_NAME, name);

        // Check If Landlord Already Exist
        Cursor mCursor = db.rawQuery("select 1 from " + TABLE_LANDLORDS + " where " + LKEY_NAME + "=?",
                new String[]{name});

        if (mCursor.getCount() > 0) {
            if (mCursor.moveToFirst())
                last_inserted_id_landlord = Long.parseLong((mCursor.getString(0)));
            Log.d("tag", "id is " + last_inserted_id_landlord);
            mCursor.close();

            return true;
		/* record exist */
        }


        last_inserted_id_landlord = db.insert(TABLE_LANDLORDS, null, value);
        if (last_inserted_id_landlord == -1)
            return false;
        else
            return true;

    }


    public boolean insertPropertyData(String name, String address, String zip) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(KEY_NAME, name);
        value.put(KEY_ADDRESS, address);
        value.put(KEY_ZIP_CODE, zip);
        value.put(FOREIGNKEY_LANDLORD, last_inserted_id_landlord);
        value.put(KEY_VISIBILITY, 1);


        last_inserted_id_property = db.insert(TABLE_CONTACTS, null, value);
        if (last_inserted_id_property == -1)
            return false;
        else
            return true;


    }


    public boolean insertTenantData(String name, String address, String email, String phoneNumber) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(TKEY_NAME, name);
        value.put(TKEY_ADDRESS, address);
        value.put(TKEY_EMAIL, email);
        value.put(TKEY_NUMBER, phoneNumber);
        value.put(FOREIGNKEY_ID, last_inserted_id_property);


        long result = db.insert(TABLE_TENANTS, null, value);
        if (result == -1)
            return false;
        else
            return true;

    }



    public boolean insertInspectorData(String name, String number, String email) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(InsKEY_NAME, name);
        value.put(InsKEY_NUM, number);
        value.put(InsKEY_EMAIL, email);

        last_inserted_InspectorId = db.insert(TABLE_INSPECTOR, null, value);
        if (last_inserted_InspectorId == -1)
            return false;
        else
            return true;


    }

    public boolean insertInspectionData(String name, String type,long property_id) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(IKEY_NAME, name);
        value.put(IKEY_TYPE, type);
        value.put(KEY_VISIBILITY, 1);
        value.put(FOREIGNKEY_PROPERTY_ID, property_id);

        last_inserted_inpection_id = db.insert(TABLE_INSPECTIONS, null, value);
        if (last_inserted_inpection_id == -1)
            return false;
        else
            return true;

    }






    //  for other class to addArea
    public boolean insertArea(String name) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(AKEY_NAME, name);

        last_inserted_areas_id = db.insert(TABLE_AREAS, null, value);
        if (last_inserted_inpection_id == -1)
            return false;
        else
            return true;
    }

    // for insert Data From this class because it use this class db object
    public boolean insertAreasData(final String name, int visibility , int position) {

        ContentValues value = new ContentValues();
        value.put(AKEY_NAME, name);
        value.put(KEY_VISIBILITY, visibility);
        value.put(SORTORDER, position);
        last_inserted_areas_id = this.db.insert(TABLE_AREAS, null, value);

        if (last_inserted_inpection_id == -1)
            return false;
        else
            return true;

    }

    public boolean insertAccessoriesData(String name,int visibility) {

        ContentValues value = new ContentValues();

        value.put(ASKEY_NAME, name);
        value.put(KEY_VISIBILITY, visibility);
        last_inserted_accessories_id = this.db.insert(TABLE_ACCESSORIES, null, value);
        if (last_inserted_accessories_id == -1)
            return false;
        else
            return true;
    }


    public boolean insertAreaAscData(int area_id, int asc_id) {

        ContentValues value = new ContentValues();
        value.put(FOREIGN_KEY_AREA_ID, area_id);
        value.put(FOREIGN_KEY_ASC_ID, asc_id);

        last_inserted_area_asc_id = this.db.insert(TABLE_AREA_ASC, null, value);
        if (last_inserted_area_asc_id == -1)
            return false;
        else
            return true;
    }




    // this is because the sort order issue we add the sort order when we create the table
    public long insertInspectionAreaDataForDuplicate(int area_id,int inspection_id,int propertyId,String area_name,int position)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(FOREIGN_KEY_AREA_ID,area_id);
        value.put(FOREIGN_KEY_INPECTION_ID,inspection_id);
        value.put(FOREIGNKEY_PROPERTY_ID,propertyId);
        value.put(AREA_NAME,area_name);
        value.put(KEY_VISIBILITY,1);
        value.put(ISCOMPLETE,0);
        value.put(SORTORDER,position);



        last_inserted_area_data_id = db.insert(TABLE_AREA_DATA_SAVE, null, value);
        Log.d("tag","inserted Area_data id in DbHelper class is "+last_inserted_area_data_id);
        return last_inserted_area_data_id;

    }



    public long insertInspectionAreaData(int area_id,int inspection_id,int propertyId,String area_name,int position)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(FOREIGN_KEY_AREA_ID,area_id);
        value.put(FOREIGN_KEY_INPECTION_ID,inspection_id);
        value.put(FOREIGNKEY_PROPERTY_ID,propertyId);
        value.put(AREA_NAME,area_name);
        value.put(KEY_VISIBILITY,1);
        value.put(ISCOMPLETE,0);
        value.put(SORTORDER,position);



        last_inserted_area_data_id = db.insert(TABLE_AREA_DATA_SAVE, null, value);
        Log.d("tag","inserted Area_data id in DbHelper class is "+last_inserted_area_data_id);
        return last_inserted_area_data_id;

    }


    public long insertInspectionAscData(int asc_id, long area_data_id , String asc_name)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(FOREIGN_KEY_ASC_ID,asc_id);
        value.put(AREA_DATA_ID,area_data_id);
        value.put(ASC_NAME,asc_name);
        value.put(KEY_VISIBILITY,1);

        last_inserted_area_data_id = db.insert(TABLE_ACCESSORY_DATA_SAVE, null, value);
        Log.d("tag","inserted Area_data id in DbHelper class is "+last_inserted_area_data_id);
        return last_inserted_area_data_id;

    }




    public long insertAscAndAreaData(int asc_id,long area_data_id,String asc_name)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(FOREIGN_KEY_ASC_ID,asc_id);
        value.put(AREA_DATA_ID,area_data_id);
        value.put(ASC_NAME,asc_name);
        value.put(KEY_VISIBILITY,1);

        long last_inserted_Asc_data_id = db.insert(TABLE_ACCESSORY_DATA_SAVE, null, value);
        Log.d("tag","inserted Asc_data id in DbHelper class is "+last_inserted_Asc_data_id);
        return last_inserted_Asc_data_id;

    }





       public boolean insertAccessoryData(int asc_id,String asc_name,long area_data_id,String quality) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(FOREIGN_KEY_ASC_ID, asc_id);
        value.put(ASC_NAME,asc_name);
        value.put(AREA_DATA_ID,area_data_id);
        value.put(QUALITY,quality);


        last_inserted_accessory_data_id = db.insert(TABLE_ACCESSORY_DATA_SAVE, null, value);
        if (last_inserted_accessory_data_id == -1)
            return false;
        else
            return true;
    }



    public boolean insertAccessoryDataCondition(int asc_id,String asc_name,long area_data_id,String condition) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(FOREIGN_KEY_ASC_ID, asc_id);
        value.put(ASC_NAME,asc_name);
        value.put(AREA_DATA_ID,area_data_id);
        value.put(CONDITION,condition);

        last_inserted_accessory_data_id = db.insert(TABLE_ACCESSORY_DATA_SAVE, null, value);
        Log.d("tag","Inserted id in accessory data is "+last_inserted_accessory_data_id);
        if (last_inserted_accessory_data_id == -1)
            return false;
        else
            return true;
    }


    public boolean insertAccessoryDataComments(int asc_id,String asc_name,long area_data_id,String comments) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(FOREIGN_KEY_ASC_ID, asc_id);
        value.put(ASC_NAME,asc_name);
        value.put(AREA_DATA_ID,area_data_id);
        value.put(COMMENTS,comments);
        value.put(KEY_VISIBILITY,1);

        last_inserted_accessory_data_id = db.insert(TABLE_ACCESSORY_DATA_SAVE, null, value);
        Log.d("tag","Inserted id in accessory data is "+last_inserted_accessory_data_id);
        if (last_inserted_accessory_data_id == -1)
            return false;
        else
            return true;
    }



    public int  changeOrderItem1(int positionItem2,int area_id , int inspection_id , int property_id,String area_name )
    {
        Log.d("tag","Position in DB helper "+positionItem2);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(SORTORDER, positionItem2);
        Log.d("tag","Vlaues are areaID "+area_id+"inspectionId"+inspection_id+"proeprty Id "+property_id+"Area Name is "+area_name);

        // updating row
        return db.update(TABLE_AREA_DATA_SAVE , value, KEY_VISIBILITY + " = '" + 1 + "' AND " + FOREIGN_KEY_AREA_ID + " = ? AND "+ FOREIGN_KEY_INPECTION_ID +" = ? AND " + FOREIGNKEY_PROPERTY_ID + " = ? AND  " + AREA_NAME + " = ? " ,
                new String[] { String.valueOf(area_id),String.valueOf(inspection_id),String.valueOf(property_id),area_name });

    }

//    public int  changeOrderItem2(int positionItem1,int area_id , int inspection_id , int property_id,String area_name )
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues value = new ContentValues();
//        value.put(SORTORDER, positionItem1);
//        // updating row
//        return db.update(TABLE_AREA_DATA_SAVE , value, KEY_VISIBILITY + " = '" + 1 + "' AND " + FOREIGN_KEY_AREA_ID + " = ? AND "+ FOREIGN_KEY_INPECTION_ID +" = ? AND " + FOREIGNKEY_PROPERTY_ID + " = ? AND  " + AREA_NAME + " = ? " ,
//                new String[] { String.valueOf(area_id),String.valueOf(inspection_id),String.valueOf(property_id),area_name });
//
//    }



    public long insertAccessoryDataImage(int asc_id,String asc_name,long area_data_id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(FOREIGN_KEY_ASC_ID, asc_id);
        value.put(ASC_NAME,asc_name);
        value.put(AREA_DATA_ID,area_data_id);
        value.put(KEY_VISIBILITY,1);

        last_inserted_accessory_data_id = db.insert(TABLE_ACCESSORY_DATA_SAVE, null, value);
        Log.d("tag","Inserted id in accessory data is "+last_inserted_accessory_data_id);
        if (last_inserted_accessory_data_id == -1)
            return 0l;
        else
            return last_inserted_accessory_data_id;
    }


    public boolean insertImageData(String img_name,String img_path,long asc_data_id,String dateTime) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(IMAGE_NAME, img_name);
        value.put(IMAGE_PATH,img_path);
        value.put(ACCESSORY_DATA_ID,asc_data_id);
        value.put(DATETIME,dateTime);

        last_inserted_image_id = db.insert(TABLE_IMAGES, null, value);
        Log.d("tag","Inserted id in Images Table is "+last_inserted_image_id);
        if (last_inserted_image_id == -1)
            return false;
        else
            return true;
    }


    public int updateAccessoryData(int asc_id,String asc_name,long area_data_id,String quality)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("tag", "area_data_id is " + area_data_id + "ascid is" + asc_id);
        ContentValues value = new ContentValues();
        value.put(FOREIGN_KEY_ASC_ID, asc_id);
        value.put(ASC_NAME,asc_name);
        value.put(AREA_DATA_ID,area_data_id);
        value.put(QUALITY, quality);

        // updating row
        return db.update(TABLE_ACCESSORY_DATA_SAVE, value, AREA_DATA_ID + " = ? AND "+ FOREIGN_KEY_ASC_ID +" = ? AND " + ASC_NAME + " = ? " ,
                new String[] { String.valueOf(area_data_id),String.valueOf(asc_id),String.valueOf(asc_name) });

    }

    public int updateInspectionFileData(int property_id , int inspectionId , String inspectionName, String inspectionType , String fileName , String filePath ) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(FILENAME, fileName);
        value.put(FILEPATH, filePath);

        // updating row
        return db.update(TABLE_INSPECTIONS, value,KEY_VISIBILITY + " = '" + 1 + "' AND "+ FOREIGNKEY_PROPERTY_ID + " = ? AND "+ IKEY_ID +" = ? AND " + IKEY_NAME + " = ? AND " + IKEY_TYPE + " = ? " ,
                new String[] { String.valueOf(property_id),String.valueOf(inspectionId),inspectionName , inspectionType});

    }


    public int updateAccessoryDataCondition(int asc_id,String asc_name,long area_data_id,String condition)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("tag","area_data_id is "+area_data_id+"ascid is"+asc_id);
        ContentValues value = new ContentValues();
        value.put(FOREIGN_KEY_ASC_ID, asc_id);
        value.put(ASC_NAME,asc_name);
        value.put(AREA_DATA_ID,area_data_id);
        value.put(CONDITION,condition);

        // updating row
        return db.update(TABLE_ACCESSORY_DATA_SAVE, value, KEY_VISIBILITY + " = '" + 1 + "' AND "+ AREA_DATA_ID + " = ? AND "+ FOREIGN_KEY_ASC_ID +" = ? AND "+ ASC_NAME + " = ? " ,
                new String[] { String.valueOf(area_data_id),String.valueOf(asc_id),String.valueOf(asc_name) });

    }

    public int updateAccessoryDataComments(int asc_id,String asc_name,long area_data_id,String comments)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("tag","area_data_id is "+area_data_id+"ascid is"+asc_id);
        ContentValues value = new ContentValues();
        value.put(FOREIGN_KEY_ASC_ID, asc_id);
        value.put(ASC_NAME,asc_name);
        value.put(AREA_DATA_ID,area_data_id);
        value.put(COMMENTS,comments);
        value.put(KEY_VISIBILITY,1);

        // updating row
        return db.update(TABLE_ACCESSORY_DATA_SAVE, value, AREA_DATA_ID + " = ? AND "+ FOREIGN_KEY_ASC_ID +" = ? AND "+ ASC_NAME + " = ? " ,
                new String[] { String.valueOf(area_data_id),String.valueOf(asc_id),String.valueOf(asc_name) });

    }


//    public int updateInspectionAscData(long inspectionAscId, int area_id, int inspection_id, int asc_id)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues value = new ContentValues();
//        value.put(FOREIGN_KEY_AREA_ID,area_id);
//        value.put(FOREIGN_KEY_INPECTION_ID,inspection_id);
//        value.put(FOREIGN_KEY_ASC_ID,asc_id);
//
//        // updating row
//        return db.update(TABLE_INSPECTION_ACCESSORY, value, INSPECTION_ASC_Id + " = ?",
//                new String[] { String.valueOf(inspectionAscId) });
//
//    }
//
//
//
//
//    public int updateConditionData(long conditionId, String type,long inspectionAscId)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues value = new ContentValues();
//        value.put(INSPECTION_ASC_Id,inspectionAscId);
//        value.put(CONDITION_TYPE,type);
//
//        // updating row
//        return db.update(TABLE_CONDITIONS, value, CONDITION_ID + " = ?",
//                new String[] { String.valueOf(conditionId) });
//
//    }
//
//
//
//    public boolean insertInspection_ascDataWithComments(int area_id,int inspection_id,int asc_id,String comments)
//    {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues value = new ContentValues();
//        value.put(FOREIGN_KEY_AREA_ID,area_id);
//        value.put(FOREIGN_KEY_INPECTION_ID,inspection_id);
//        value.put(FOREIGN_KEY_ASC_ID,asc_id);
//        value.put(COMMENTS,comments);
//
//
//        last_inserted_inspection_ascWithComments_id = db.insert(TABLE_INSPECTION_ACCESSORY, null, value);
//        Log.d("tag","inserted id in DbHelper class is of Comments is "+last_inserted_inspection_ascWithComments_id);
//        if (last_inserted_inspection_ascWithComments_id == -1)
//            return false;
//        else
//            return true;
//
//
//    }
//    public int updateInspectionAscDataWithComments(long inspectionAscId, int area_id, int inspection_id, int asc_id,String comments)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues value = new ContentValues();
//        value.put(FOREIGN_KEY_AREA_ID,area_id);
//        value.put(FOREIGN_KEY_INPECTION_ID,inspection_id);
//        value.put(FOREIGN_KEY_ASC_ID,asc_id);
//        value.put(COMMENTS,comments);
//
//        // updating row
//        return db.update(TABLE_INSPECTION_ACCESSORY, value, INSPECTION_ASC_Id + " = ?",
//                new String[] { String.valueOf(inspectionAscId) });
//
//    }



    // Getting Data From Db

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Cursor getAreaDataIdFromDbForPDF(int propertyId, int inspectionId )
    {
        SQLiteDatabase db = getReadableDatabase();
        String MY_QUERY = "SELECT * FROM " + TABLE_AREA_DATA_SAVE + " a LEFT OUTER JOIN "+TABLE_ACCESSORY_DATA_SAVE+" b ON a." + AREA_DATA_ID +  " =   b." + AREA_DATA_ID + " WHERE a." + FOREIGNKEY_PROPERTY_ID + " = ? AND  a."+ FOREIGN_KEY_INPECTION_ID + " = ? AND a."+ KEY_VISIBILITY + " = '" + 1 + "' AND a."+ ISCOMPLETE + " = '" + 1 + "' AND b."+ KEY_VISIBILITY + " = '" + 1 + "' ORDER BY  a." + SORTORDER + "  ASC " ;

        Cursor res = db.rawQuery(MY_QUERY, new String[]{String.valueOf(propertyId), String.valueOf(inspectionId)}, null);

//        SQLiteDatabase db = getReadableDatabase();
//        String[] columns = {AREA_DATA_ID, AREA_NAME};
//          db.query(TABLE_AREA_DATA_SAVE, columns, KEY_VISIBILITY + " = '" + 1 + "' AND  "+ ISCOMPLETE + " = '" + 1 + "' AND " + FOREIGNKEY_PROPERTY_ID + " = '" + propertyId + "' AND  " + FOREIGN_KEY_INPECTION_ID + " = '" + inspectionId + "' " , null, null, null, null);
        Log.d("tag","Size of Result Set is "+res.getCount());
        return res;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public int getMaxSortOrder()
    {
        int sortOrder =0;
        SQLiteDatabase db = getReadableDatabase();
        String MY_QUERY = "SELECT MAX("+SORTORDER+") as o FROM " + TABLE_AREA_DATA_SAVE  ;

        Cursor res = db.rawQuery(MY_QUERY, null, null);
        Log.d("tag","Size of Result Set is "+res.getCount());

        if(res.getCount() > 0) {

            if(res.moveToFirst())
            {
                sortOrder = res.getInt(res.getColumnIndex("o"));
            }
        }
        return sortOrder;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Cursor getImagesDataForPDF(int propertyId, int inspectionId )
    {
        SQLiteDatabase db = getReadableDatabase();
        String MY_QUERY = "SELECT * FROM " + TABLE_AREA_DATA_SAVE + " a LEFT OUTER JOIN "+TABLE_ACCESSORY_DATA_SAVE+" b ON a." + AREA_DATA_ID +  " =   b." + AREA_DATA_ID + " LEFT OUTER JOIN " + TABLE_IMAGES +" c ON b." + ACCESSORY_DATA_ID + " = c." + ACCESSORY_DATA_ID + " WHERE a." + FOREIGNKEY_PROPERTY_ID + " = ? AND  a."+ FOREIGN_KEY_INPECTION_ID + " = ? AND a."+ KEY_VISIBILITY + " = '" + 1 + "' AND a."+ ISCOMPLETE + " = '" + 1 + "' AND b."+ KEY_VISIBILITY + " = '" + 1 + "' " ;

        Cursor res = db.rawQuery(MY_QUERY, new String[]{String.valueOf(propertyId), String.valueOf(inspectionId)}, null);

//        SQLiteDatabase db = getReadableDatabase();
//        String[] columns = {AREA_DATA_ID, AREA_NAME};
//          db.query(TABLE_AREA_DATA_SAVE, columns, KEY_VISIBILITY + " = '" + 1 + "' AND  "+ ISCOMPLETE + " = '" + 1 + "' AND " + FOREIGNKEY_PROPERTY_ID + " = '" + propertyId + "' AND  " + FOREIGN_KEY_INPECTION_ID + " = '" + inspectionId + "' " , null, null, null, null);
        Log.d("tag","Size of Result Set is "+res.getCount());
        return res;

    }

    public Cursor getlanLordId(int propertyId)
    {

        int lId = 0;
        Cursor res1 = null;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {FOREIGNKEY_LANDLORD};
        Cursor res = db.query(TABLE_CONTACTS, columns, KEY_VISIBILITY + " = '" + 1 + "' AND  "+ FOREIGNKEY_PROPERTY_ID + " = '" + propertyId + "' " , null, null, null, null);
//        if(res.getCount() > 0)
//        {
//            String[] column = {LKEY_NAME};
//             res1 = db.query(TABLE_LANDLORDS, columns, FOREIGNKEY_LANDLORD + " = '" + res.getInt(0) + "' " , null, null, null, null);
//
//        }

        return  res;
    }

    public Cursor getlanLordName(int lId)
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {LKEY_NAME};
        Cursor res = db.query(TABLE_LANDLORDS, columns, LKEY_ID + " = '" + lId + "' ", null, null, null, null);
        return  res;
    }

    public Cursor getInspectorData(int inspectorId)
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {InsKEY_NAME,InsKEY_NUM,InsKEY_EMAIL};
        Cursor res = db.query(TABLE_INSPECTOR, columns, InsKEY_ID + " = '" + inspectorId + "' ", null, null, null, null);
        return  res;
    }

    public Cursor getTenantData(int propertyId)
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {TKEY_NAME,TKEY_NUMBER,TKEY_EMAIL,TKEY_ADDRESS};
        Cursor res = db.query(TABLE_TENANTS, columns, FOREIGNKEY_PROPERTY_ID + " = '" + propertyId + "' " , null, null, null, null);
        return  res;
    }


    public Cursor getAscDataFromDbForPDF(int area_data_id )
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {ACCESSORY_DATA_ID,ASC_NAME,QUALITY,CONDITION,COMMENTS};
        Cursor res = db.query(TABLE_ACCESSORY_DATA_SAVE, columns, KEY_VISIBILITY + " = '" + 1 + "' AND  "+ AREA_DATA_ID + " = '" + area_data_id + "' " , null, null, null, null);
        return res;

    }

    public Cursor getAllProperties() {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {FOREIGNKEY_PROPERTY_ID, KEY_NAME, KEY_ADDRESS, KEY_ZIP_CODE,};
        Cursor res = db.query(TABLE_CONTACTS, columns, KEY_VISIBILITY + " = '" + 1 + "' ", null, null, null, null);
        return res;
    }


    public Cursor getAllAreasOfUser(int property_id, int inspection_id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {AREA_NAME, FOREIGNKEY_AREA_ID};
        Cursor res = db.query(TABLE_AREA_DATA_SAVE, columns, KEY_VISIBILITY + " = '" + 1 + "' AND "+FOREIGNKEY_PROPERTY_ID +" = '" + property_id + "' AND "+ FOREIGN_KEY_INPECTION_ID + " = '" + inspection_id + "' "  , null, null, null, SORTORDER + " ASC ");
        Log.d("tag","Res size is"+res.getCount());
        return res;
    }

    public Cursor getAllAscOfArea(long area_data_id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {ASC_NAME, FOREIGN_KEY_ASC_ID};
        Cursor res = db.query(TABLE_ACCESSORY_DATA_SAVE, columns, KEY_VISIBILITY + " = '" + 1 + "' AND "+ AREA_DATA_ID +" = '" + area_data_id + "' " , null, null, null, null);
        Log.d("tag","Res size is"+res.getCount());
        return res;
    }

    public Cursor getAllInspections(long propertyId) {
        Log.d("tag", "Property Id is in Db Helper" + propertyId);
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {IKEY_ID, IKEY_NAME, IKEY_TYPE, FOREIGNKEY_PROPERTY_ID,};
        Cursor res = db.query(TABLE_INSPECTIONS, columns, KEY_VISIBILITY + " = '" + 1 + "' AND "+FOREIGNKEY_PROPERTY_ID + " = '" + propertyId + "' ", null, null, null, null);
        return res;
    }

    public int deleteProperty(long properyId) {
        Log.d("tag", "Proeprty id in Delete Method is " + properyId);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VISIBILITY, 0);
        // updating row
        return db.update(TABLE_CONTACTS, values, FOREIGNKEY_PROPERTY_ID + " = ?",
                new String[]{String.valueOf(properyId)});


    }


    public int deleteAreas(int areaId , int inspectionId , int propertyId,String areaName) {
        Log.d("tag", "Area id in Delete Method is " + areaId);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VISIBILITY, 0);
        // updating row
        return db.update(TABLE_AREA_DATA_SAVE, values, FOREIGN_KEY_AREA_ID + " = ? AND "+ FOREIGN_KEY_INPECTION_ID +" = ? AND "+ FOREIGNKEY_PROPERTY_ID +" = ? AND "+ AREA_NAME + " =  ? ",
                new String[]{String.valueOf(areaId),String.valueOf(inspectionId),String.valueOf(propertyId),areaName});


    }


    public int deleteAccessory(int ascId , long area_data_id ,String ascName) {
        Log.d("tag", "Asc id in Delete Method is " + ascId);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VISIBILITY, 0);
        // updating row
        return db.update(TABLE_ACCESSORY_DATA_SAVE, values, FOREIGN_KEY_ASC_ID + " = ? AND "+ AREA_DATA_ID +" = ? AND "+ ASC_NAME + " =  ? ",
                new String[]{String.valueOf(ascId),String.valueOf(area_data_id),ascName});


    }


    public int deleteInspection(int inspectionId , int propertyId) {
        Log.d("tag", "Inspection id in Delete Method is " + inspectionId);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VISIBILITY, 0);
        // updating row
        return db.update(TABLE_INSPECTIONS, values, IKEY_ID + " = ? AND "+ FOREIGNKEY_PROPERTY_ID +" = ?",
                new String[]{String.valueOf(inspectionId),String.valueOf(propertyId)});


    }


    public Cursor getAllAreas() {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {AKEY_NAME,AKEY_ID,SORTORDERAREA};
        Cursor res = db.query(TABLE_AREAS, columns,  KEY_VISIBILITY + " = '" + 1 + "' ", null, null, null, null);
        return res;
    }


    public Cursor getAllAreasToDuplicate(int propertyId, int inspectionId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {AREA_NAME,FOREIGN_KEY_AREA_ID};
        Cursor res = db.query(TABLE_AREA_DATA_SAVE, columns, KEY_VISIBILITY + " = '" + 1 + "' AND "+FOREIGNKEY_PROPERTY_ID + " = '" + propertyId + "' AND "+FOREIGN_KEY_INPECTION_ID + " = '" + inspectionId + "'", null, null, null, null);
        return res;
    }


    public Cursor getAllAccessoriesOfSpecificArea(long area_id) {
    //    Log.d("tag", "Area id in Db Helper Class" + area_id);
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {FOREIGN_KEY_ASC_ID};
        Cursor res = db.query(TABLE_AREA_ASC, columns, FOREIGN_KEY_AREA_ID + " = '" + area_id + "' ", null, null, null, null);
        Log.d("tag", "Result set size is  OF SpecificArea" +res.getCount());

        return res;

    }

    public Cursor getAllAccessoriesName(long asc_id) {
  //      Log.d("tag", "Accessory id in Db Helper Class" + asc_id);
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {ASKEY_NAME,ASKEY_ID};
        Cursor res = db.query(TABLE_ACCESSORIES, columns, ASKEY_ID + " = '" + asc_id + "' ", null, null, null, null);
        return res;

    }

    public long getSelectedPropertyId(String name) {

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {FOREIGNKEY_PROPERTY_ID};
        Cursor res = db.query(TABLE_CONTACTS, columns, KEY_NAME + " = '" + name + "' ", null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                selected_property_id = Long.parseLong(res.getString(0));
           //     Log.d("tag", "Property Id is" + selected_property_id);
                res.close();
                return selected_property_id;
            }
        }

        return selected_property_id;

    }



    public long getSelectedAreaId(String name) {

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {AKEY_ID};
        Cursor res = db.query(TABLE_AREAS, columns, KEY_NAME + " = '" + name + "' ", null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                selected_Area_id = res.getLong(0);
                Log.d("tag", "Area Id is" + selected_Area_id);
                res.close();
                return selected_Area_id;
            }
        }

        return selected_property_id;
    }



    public long getAreaId(String name,int propertyId,int inspectionId) {

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {FOREIGN_KEY_AREA_ID};
        Cursor res = db.query(TABLE_AREA_DATA_SAVE, columns, KEY_VISIBILITY + " = '" + 1 + "' AND "+ AREA_NAME + " = '" + name + "' AND " + FOREIGNKEY_PROPERTY_ID + " = '" + propertyId + "' AND "+ FOREIGN_KEY_INPECTION_ID + " = '" + inspectionId + "' ", null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                selected_Area_id = res.getLong(0);
         //       Log.d("tag", "Area Id is" + selected_Area_id);
                res.close();
                return selected_Area_id;
            }
        }

        return selected_property_id;
    }



    public long getselectedAreaDataId(int area_id,int inspection_id,String areaName,int pId)
    {
        long selectedAreaDataId=0l;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {AREA_DATA_ID};
        Cursor res = db.query(TABLE_AREA_DATA_SAVE, columns, KEY_VISIBILITY + " = '" + 1 + "' AND "+ FOREIGN_KEY_AREA_ID +" = '" + area_id + "' AND "+ FOREIGN_KEY_INPECTION_ID + " = '" + inspection_id + "' AND "+ AREA_NAME + " = '" + areaName + "' AND "+ FOREIGNKEY_PROPERTY_ID + " = '" + pId + "'" , null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                selectedAreaDataId = Long.parseLong(res.getString(0));
              //  Log.d("tag", "Inspection Accessory Id is " + selectedAreaDataId);
                res.close();
                return selectedAreaDataId;
            }
        }

        return selectedAreaDataId;

    }


    public String getselectedAscComment(int asc_id,long area_data_id, String asc_name)
    {
        String comment = null;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COMMENTS};
        Cursor res = db.query(TABLE_ACCESSORY_DATA_SAVE, columns, KEY_VISIBILITY + " = '" + 1 + "' AND " + FOREIGN_KEY_ASC_ID +" = '" + asc_id + "' AND "+ AREA_DATA_ID + " = '" + area_data_id + "' AND " + ASC_NAME + " = '" + asc_name + "' " , null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                comment = res.getString(0);
                  Log.d("tag", "Comment is " + comment);
                res.close();
                return comment;
            }
        }

        return comment;

    }



    public long getAreaDataId(int area_id,int inspection_id,int property_id,String area_name)
    {
        long selectedAreaDataId=0l;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {AREA_DATA_ID};
        Cursor res = db.query(TABLE_AREA_DATA_SAVE, columns,KEY_VISIBILITY + " = '" + 1 + "' AND " + FOREIGN_KEY_AREA_ID +" = '" + area_id + "' AND "+ FOREIGN_KEY_INPECTION_ID + " = '" + inspection_id + "' AND " + FOREIGNKEY_PROPERTY_ID + " = '" + property_id + "' AND "+ AREA_NAME + " = '" + area_name + "' " , null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                selectedAreaDataId = Long.parseLong(res.getString(0));
              //   Log.d("tag", "Area data id  is " + selectedAreaDataId);
                res.close();
                return selectedAreaDataId;
            }
        }

        return selectedAreaDataId;

    }





    public long checkselectedAreaDataId(long area_data_id,int asc_id)
    {

        Log.d("tag","Values are"+"area data id is"+area_data_id+"acs id is "+asc_id);
        long selectedAreaDataId=0l;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {ACCESSORY_DATA_ID};
        Cursor res = db.query(TABLE_ACCESSORY_DATA_SAVE, columns,KEY_VISIBILITY + " = '" + 1 + "' AND "+ AREA_DATA_ID +" = '" + area_data_id + "' AND  " + FOREIGN_KEY_ASC_ID + " = '" + asc_id + "' " , null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                selectedAreaDataId = Long.parseLong(res.getString(0));
                Log.d("tag", "AREADATA ID  in dbhelper class  is " + selectedAreaDataId);
                res.close();
                return selectedAreaDataId;
            }
        }

        return selectedAreaDataId;

    }


    public long checkInspectionName(String name, long propertyId)
    {

        Log.d("tag","Values are"+"inspection Name is"+name);
        long selectedAreaDataId=0l;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {IKEY_ID};
        Cursor res = db.query(TABLE_INSPECTIONS, columns,KEY_VISIBILITY + " = '" + 1 + "' AND "+ IKEY_NAME +" = '" + name + "' AND "+ FOREIGNKEY_PROPERTY_ID +" = '" + propertyId + "' ", null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                selectedAreaDataId = Long.parseLong(res.getString(0));
                Log.d("tag", "AREADATA ID  in dbhelper class  is " + selectedAreaDataId);
                res.close();
                return selectedAreaDataId;
            }
        }

        return selectedAreaDataId;

    }


    public Cursor getQualityData(long asc_data_id)
    {

        long selectedAscDataId=0l;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {QUALITY,CONDITION};
        Cursor res = db.query(TABLE_ACCESSORY_DATA_SAVE, columns,KEY_VISIBILITY + " = '" + 1 + "' AND "+ ACCESSORY_DATA_ID + " = '" + asc_data_id + "' " , null, null, null, null);


        return res;

    }


    public long checkAreaandInspectionId(int area_id,int inspection_id)
    {

        Log.d("tag","Values are"+"area id is"+area_id+"inspection id is "+inspection_id);
        long selectedAreaDataId=0l;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {AREA_DATA_ID};
        Cursor res = db.query(TABLE_AREA_DATA_SAVE, columns, FOREIGN_KEY_AREA_ID +" = '" + area_id + "' AND  "+FOREIGN_KEY_INPECTION_ID +" = '" + inspection_id + "' " , null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                selectedAreaDataId = Long.parseLong(res.getString(0));
                Log.d("tag", "Area data id from area_data tables in dbhelper class  is " + selectedAreaDataId);
                res.close();
                return selectedAreaDataId;
            }
        }

        return selectedAreaDataId;

    }


    public long checkDuplicateAreaName(String area_name,int propertyId,int inspectionId)
    {

        Log.d("tag","Values are inspection id is "+inspectionId+"propertyId is"+propertyId+"Area Name is "+area_name);
        long selectedAreaDataId=0l;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {AREA_DATA_ID};
        Cursor res = db.query(TABLE_AREA_DATA_SAVE, columns, KEY_VISIBILITY + " = '" + 1 + "' AND "+FOREIGN_KEY_INPECTION_ID +" = '" + inspectionId + "' AND  "+FOREIGNKEY_PROPERTY_ID +" = '" + propertyId + "' AND  "+AREA_NAME +" = '" + area_name + "' " , null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                selectedAreaDataId = Long.parseLong(res.getString(0));
                Log.d("tag", "Area data id from area_data tables in dbhelper class  is " + selectedAreaDataId);
                res.close();
                return selectedAreaDataId;
            }
        }

        return selectedAreaDataId;

    }



    public long checkDuplicateAscName(String asc_name,long area_data_id)
    {

        Log.d("tag","Values are Asc Name is "+asc_name+"Area Data Id is "+area_data_id);
        long selectedAreaDataId=0l;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {ACCESSORY_DATA_ID};
        Cursor res = db.query(TABLE_ACCESSORY_DATA_SAVE, columns, KEY_VISIBILITY + " = '" + 1 + "' AND "+ ASC_NAME +" = '" + asc_name + "' AND  "+ AREA_DATA_ID +" = '" + area_data_id + "' " , null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                selectedAreaDataId = Long.parseLong(res.getString(0));
                Log.d("tag", "Area data id from area_data tables in dbhelper class  is " + selectedAreaDataId);
                res.close();
                return selectedAreaDataId;
            }
        }

        return selectedAreaDataId;

    }


    public long checkCommonAreaName(String area_name)
    {

        Log.d("tag","Area Name is "+area_name);
        long selectedAreaDataId=0l;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {AKEY_ID};
        Cursor res = db.query(TABLE_AREAS, columns, KEY_VISIBILITY + " = '" + 1 + "' AND "+AKEY_NAME +" = '" + area_name + "' " , null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                selectedAreaDataId = Long.parseLong(res.getString(0));
                Log.d("tag", "Area data id from area_data tables in dbhelper class  is " + selectedAreaDataId);
                res.close();
                return selectedAreaDataId;
            }
        }

        return selectedAreaDataId;

    }



    public long checkCommonAscName(String asc_name)
    {

        Log.d("tag","Asc Name is "+asc_name);
        long selectedAreaDataId=0l;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {ASKEY_ID};
        Cursor res = db.query(TABLE_ACCESSORIES, columns, KEY_VISIBILITY + " = '" + 1 + "' AND "+ASKEY_NAME +" = '" + asc_name + "' " , null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                selectedAreaDataId = Long.parseLong(res.getString(0));
                Log.d("tag", "Asc id from asc_data tables in dbhelper class  is " + selectedAreaDataId);
                res.close();
                return selectedAreaDataId;
            }
        }

        return selectedAreaDataId;

    }










    public long checkInspectionAndProeprtyId(int property_id,int inspection_id,int area_id)
    {

        Log.d("tag","Values are"+"Property id is"+property_id+"inspection id is "+inspection_id);
        long selectedAreaDataId=0l;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {AREA_DATA_ID};
        Cursor res = db.query(TABLE_AREA_DATA_SAVE, columns, FOREIGNKEY_PROPERTY_ID +" = '" + property_id + "' AND  "+FOREIGN_KEY_INPECTION_ID +" = '" + inspection_id + "' AND  "+FOREIGN_KEY_AREA_ID +" = '" + area_id + "' " , null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                selectedAreaDataId = Long.parseLong(res.getString(0));
                Log.d("tag", "Area data id from area_data tables in dbhelper class  is " + selectedAreaDataId);
                res.close();
                return selectedAreaDataId;
            }
        }

        return selectedAreaDataId;

    }



    public long checkAccessoryAndAreaDataIds(int asc_id,long area_data_id,String asc_name)
    {

        Log.d("tag","Values are"+"Accessory Id  is"+asc_id+"Areadata id is  "+area_data_id+"Asc Name is "+asc_name);
        long selectedAreaDataId=0l;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {ACCESSORY_DATA_ID};
        Cursor res = db.query(TABLE_ACCESSORY_DATA_SAVE, columns,FOREIGN_KEY_ASC_ID +" = '" + asc_id + "' AND  "+ AREA_DATA_ID +" = '" + area_data_id + "' AND  "+ ASC_NAME +" = '" + asc_name + "' " , null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                selectedAreaDataId = Long.parseLong(res.getString(0));
                Log.d("tag", "Area data id from area_data tables in dbhelper class  is " + selectedAreaDataId);
                res.close();
                return selectedAreaDataId;
            }
        }

        return selectedAreaDataId;

    }




    public Cursor checkFilePath(int propertyId , int inspectionId , String inspectionName , String inspectionType )
    {

        Log.d("tag","Values are"+" inspection  Id  is"+inspectionId+" Proeprty Id is   "+propertyId+"Inspection Name is "+inspectionName);

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {FILENAME};
        Cursor res = db.query(TABLE_INSPECTIONS, columns,KEY_VISIBILITY + " = '" + 1 + "' AND  " + FOREIGNKEY_PROPERTY_ID +" = '" + propertyId + "' AND  "+ IKEY_ID +" = '" + inspectionId + "' AND  "+ IKEY_NAME +" = '" + inspectionName + "' AND  " + IKEY_TYPE +" = '" + inspectionType + "'  " , null, null, null, null);

        return res;

    }

    public int checkCompleteAreaName(String area_name,int area_id,int inspection_id , int property_id)
    {

     //   Log.d("tag","Values are"+"Area Name is "+area_name);
        int iscomplete = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {ISCOMPLETE};
        Cursor res = db.query(TABLE_AREA_DATA_SAVE, columns, KEY_VISIBILITY + " = '" + 1 + "' AND  " + AREA_NAME +" = '" + area_name + "' AND  "+ FOREIGN_KEY_AREA_ID +" = '" + area_id + "' AND  " + FOREIGNKEY_PROPERTY_ID + " = '" + property_id + "' AND  " +  FOREIGN_KEY_INPECTION_ID + " = '" + inspection_id + "' " , null, null, null, null);
        if (res.getCount() > 0) {
            if (res.moveToFirst()) {
                iscomplete = Integer.parseInt(res.getString(0));
       //         Log.d("tag", " Complete Aream name is  in dbhelper class  is " + iscomplete);
                res.close();
                return iscomplete;
            }
        }

        return iscomplete;

    }


    public int updateAreaDataComplete(long area_data_id )
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(ISCOMPLETE,1);

        // updating row
        return db.update(TABLE_AREA_DATA_SAVE, value, AREA_DATA_ID + " = ? ",
                new String[] {String.valueOf(area_data_id) });

    }









}
