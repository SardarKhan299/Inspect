package co.uk.inspection;

import java.util.ArrayList;

import co.uk.inspection.CustomAdapters.CustomAdapter;
import co.uk.inspection.DBHelper.DbHelperClass;
import co.uk.inspection.TableClasses.Property;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class showAllProperties extends AppCompatActivity {

    ListView showPropertiesList;
	static DbHelperClass myDb;
	ArrayList<Property> propertyData = null;

	Toolbar toolbar;
    Long selected_proeprty_id;
    String propertyName = null;
    int propertyId = 0;
    CustomAdapter adapter;
     static boolean alreadyExecuted = false;
    Cursor dataFromDb;
    TextView emptyText;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showallproperties);
		myDb = new DbHelperClass(this);
        initToolBar();
		propertyData = new ArrayList<Property>();
		showPropertiesList = (ListView) findViewById(R.id.listViewShowAllProperties);
        emptyText = (TextView) findViewById(R.id.textviewEmtpyProeprties);

        // for search Affect
        handleIntent(getIntent());
        // setup back button

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        // this will call the method once in its life time of the App
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        if(prefs.getBoolean("firstRun", true)) {
//            Log.d("tag","Method Call First Time ");
//            insertAreasData(); // <-- your function
//            prefs.edit().putBoolean("firstRun", false).commit();
//        }


        showPropertiesList.setEmptyView(emptyText);
         showAllData();

	 adapter = new CustomAdapter(showAllProperties.this,R.layout.singlerowforshowallproperties, propertyData);
	
		
	    showPropertiesList.setAdapter(adapter);
       // showPropertiesList.setTextFilterEnabled(true);
	   
		
		showPropertiesList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				// get the item when user selected that
				Property p = (Property) arg0.getItemAtPosition(arg2);
			//	Log.d("tag", "Name is "+p.getName());

				showInputDialog(arg2,p);
				
				
			}
		
		});
		

		
	}












    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_addFutureInspection).setVisible(false);
        menu.findItem(R.id.action_addInspection).setVisible(false);
        menu.findItem(R.id.action_addArea).setVisible(false);
        menu.findItem(R.id.action_createReport).setVisible(false);
        menu.findItem(R.id.action_addSection).setVisible(false);
        menu.findItem(R.id.delete).setVisible(false);
        menu.findItem(R.id.action_ShowImages).setVisible(false);
        menu.findItem(R.id.rotateImage).setVisible(false);
        menu.findItem(R.id.notes).setVisible(false);






        // Associate searchable configuration with the SearchView

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(showAllProperties.this.getComponentName()));
        searchView.setIconifiedByDefault(false);
   //     searchView.setSubmitButtonEnabled(true);


        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // this is your adapter that will be filtered
                return false;



            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                if(adapter != null) {

                    adapter.getFilter().filter(newText);
                    System.out.println("on text chnge text: " + newText);
                    return false;

                }

                return false;

            }
        };
        searchView.setOnQueryTextListener(textChangeListener);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_addInspection:

                return true;
            case R.id.action_addFutureInspection:

                return true;

            case android.R.id.home:
                finish();
                overridePendingTransition(R.animator.backright, R.animator.backin);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }


    public class GetAllproperties extends AsyncTask<Void,Void,ArrayList<Property>>
    {
        ArrayList<Property> list = new ArrayList<Property>();
        @Override
        protected ArrayList<Property> doInBackground(Void... params) {

            SQLiteDatabase database = myDb.getReadableDatabase();
            String[] columns = {DbHelperClass.FOREIGNKEY_PROPERTY_ID, DbHelperClass.KEY_NAME, DbHelperClass.KEY_ADDRESS, DbHelperClass.KEY_ZIP_CODE,};
            Cursor res = database.query(DbHelperClass.TABLE_CONTACTS, columns, DbHelperClass.KEY_VISIBILITY + " = '" + 1 + "' ", null, null, null, null);

            Cursor allData = res;

            if(allData.getCount() == 0)
            {
                Log.d("tag", "No Data To Show");
            }

            //StringBuffer buffer = new StringBuffer();


            while(allData.moveToNext())
            {

//		buffer.append("id :"+allData.getString(0)+"\n");
//		buffer.append(""+allData.getString(1));
//		buffer.append("Address :"+allData.getString(2)+"\n");
//		buffer.append("Zip :"+allData.getString(3)+"\n\n ");
                Property p1 = new Property();
                propertyId = allData.getInt(0);
                p1.setId(propertyId);
                propertyName = allData.getString(1);
                p1.setName(propertyName);
                p1.setAddress(allData.getString(2));
                p1.setPostCode(allData.getString(3));
                Log.d("tag", "propertyName is  "+propertyName);
                Log.d("tag","Property Id is "+propertyId);
                list.add(p1);

            }



            res.close();
            allData.close();
            return list;


        }


        @Override
        protected void onPostExecute(ArrayList<Property> list) {
            propertyData = list;
            adapter = new CustomAdapter(showAllProperties.this,R.layout.singlerowforshowallproperties, propertyData);
            showPropertiesList.setAdapter(adapter);
        }
    }


    public void showAllData()
	{
        new GetAllproperties().execute();
	}

	

	// show Alert Dialog Box
		private void showInputDialog(final int position, final Property p){
		    final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomDialogTheme);
		    builder.setTitle("Manage Property");
		    View view = getLayoutInflater().inflate(R.layout.dialogview, null);
		    builder.setView(view);





		    builder.setNegativeButton("Open Inspection", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent i = new Intent(showAllProperties.this, ShowAllInspections.class);
                    Log.d("tag","Property Id is "+p.getId());
                    i.putExtra("propertyName", p.getName());
                    i.putExtra("propertyId",p.getId());
                    startActivity(i);
                    overridePendingTransition(R.animator.righttoleft, R.animator.lefttoright);

                }
            });



		    
//
//		    builder.setNeutralButton("Delete Property", new DialogInterface.OnClickListener() {
//		        @Override
//		        public void onClick(DialogInterface dialog, int which) {
//
//                    selected_proeprty_id = myDb.getSelectedPropertyId(propertyName);
//                    int rowsAffected = myDb.deleteProperty(selected_proeprty_id);
//                    if(rowsAffected == 1 )
//                    {
//                        Log.d("tag", "property Deleted");
//                        propertyData.remove(position);
//                        adapter.notifyDataSetChanged();
//                    }
//                    else
//                    {
//                        Log.d("tag","No property Deleted");
//
//
//                    }
//
//
//
//
//		        }
//		    });
		    
		


            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                         @Override
                                         public void onShow(DialogInterface arg0) {
                                             dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.white));
                                             dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.yellow));
                                         }
                                     });

                    dialog.show();





        }


	public void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("List of Properties");
        toolbar.setTitleTextColor(getResources().getColor(R.color.yellow));
		setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
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
        overridePendingTransition(R.animator.backright, R.animator.backin);
    }







}
