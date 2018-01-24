package co.uk.inspection;

import java.util.ArrayList;

import co.uk.inspection.CustomAdapters.CustomAdapterShowAllInspection;
import co.uk.inspection.DBHelper.DbHelperClass;
import co.uk.inspection.TableClasses.Accessories;
import co.uk.inspection.TableClasses.Inspection;
import co.uk.inspection.TableClasses.ShowAllAreas;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ShowAllInspections  extends AppCompatActivity{


	ListView showAllInspection;
	public static String propertyName;
	ArrayList<Inspection> inspectionList =  new ArrayList<Inspection>();
	CustomAdapterShowAllInspection adapter ;
	Toolbar toolbar;
	static DbHelperClass myDb;
	public static Long propertyId;
	TextView emptyText;
	ImageView deleteButton ;
	int property_id=0;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showallinspection);
		myDb = new DbHelperClass(this);
		Intent i = getIntent();
		propertyName = i.getStringExtra("propertyName");
		property_id = i.getIntExtra("propertyId", 0);
		Log.d("tag", "property Name In Inspection is  " + propertyName + "Property Id is " + property_id);
		initToolBar();

		showAllInspection = (ListView) findViewById(R.id.listViewShowAllInspectionList);
		showAllInspection.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		emptyText = (TextView) findViewById(R.id.textViewEmptyShowAllInspection);
		deleteButton = (ImageView) findViewById(R.id.imageViewDelete);
		deleteButton.setVisibility(View.GONE);

		// for search Affect
		handleIntent(getIntent());
		// setup back button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);



		showAllInspection.setEmptyView(emptyText);
		new GetAllInspection().execute();

		try {

			adapter = new CustomAdapterShowAllInspection(ShowAllInspections.this, R.layout.chooseinspectionsinglerow, inspectionList);
			showAllInspection.setAdapter(adapter);
		}
		catch (Exception ex)
		{
			System.out.println("Null pointer Exception Occur");
		}
		showAllInspection.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// Show All Areas Of Inspection

				// for not selecting the item
				arg1.setSelected(false);

               Inspection ins = (Inspection) showAllInspection.getItemAtPosition(arg2);
                Intent i = new Intent(ShowAllInspections.this, ShowAllAreas.class);
                i.putExtra("inspectionName",ins.getName());
                i.putExtra("inspectionType",ins.getType());
				i.putExtra("inspectionId",ins.getId());
				i.putExtra("propertyId",property_id);
                startActivity(i);
				overridePendingTransition(R.animator.righttoleft, R.animator.lefttoright);



			}
		});

showAllInspection.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
	@Override
	public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {


		Log.d("tag", "Call on item Checked state changed");
		// Capture total checked items
		final int checkedCount = showAllInspection.getCheckedItemCount();
		// Set the CAB title according to total checked items

		mode.setTitle(checkedCount + " Selected");


		final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		upArrow.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
		getSupportActionBar().setHomeAsUpIndicator(upArrow);

		// Calls toggleSelection method from ListViewAdapter Class
		adapter.toggleSelection(position);


	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		Log.d("tag", "Call on create action mode");


		mode.getMenuInflater().inflate(R.menu.main, menu);
		// disable the menu item
		menu.findItem(R.id.action_addFutureInspection).setVisible(false);
		menu.findItem(R.id.action_addInspection).setVisible(false);
		menu.findItem(R.id.search).setVisible(false);
		menu.findItem(R.id.delete).setVisible(true);
		menu.findItem(R.id.action_addSection).setVisible(false);


		return true;



	}









	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		Log.d("tag","Call on prepared action mode");
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		Log.d("tag","Call on action item clicked");
		switch (item.getItemId()) {
			case R.id.delete:
				// Calls getSelectedIds method from ListViewAdapter Class
				SparseBooleanArray selected = adapter
						.getSelectedIds();
				// Captures all selected ids with a loop
				for (int i = (selected.size() - 1); i >= 0; i--) {
					if (selected.valueAt(i)) {
						Inspection selecteditem = adapter
								.getItem(selected.keyAt(i));

						// Delete Inspection from database
						myDb.deleteInspection(selecteditem.getId(),property_id);

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

		Log.d("tag","Call on destroy action mode");
		adapter.removeSelection();
		deleteButton.setVisibility(View.INVISIBLE);
		toolbar.setVisibility(View.VISIBLE);
	}
});
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(getIntent());
	}

	// show All Accessories of Specific Area
	public static ArrayList<Accessories> showAllAccessories(String areaName) {
		// TODO Auto-generated method stub

		ArrayList<Accessories> allAccessories = new ArrayList<Accessories>();

		Long aId = myDb.getSelectedAreaId(areaName);

		Log.d("tag", "Area id is " + aId);


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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == MainActivity.REQUESTCODE)
		{
			if(resultCode == RESULT_OK)
			{
				Log.d("tag", "return");
				propertyId = data.getLongExtra("property_id",1L);
				Log.d("tag","Property Id ON Activity Result is"+propertyId);
				 new GetAllInspection().execute();
			}
			
		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_addArea).setVisible(false);
		menu.findItem(R.id.action_createReport).setVisible(false);
		menu.findItem(R.id.delete).setVisible(false);
		menu.findItem(R.id.action_addSection).setVisible(false);
		menu.findItem(R.id.action_ShowImages).setVisible(false);
		menu.findItem(R.id.rotateImage).setVisible(false);
		menu.findItem(R.id.notes).setVisible(false);
		menu.findItem(R.id.action_addFutureInspection).setVisible(false);



		// Associate searchable configuration with the SearchView

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		MenuItem searchItem = menu.findItem(R.id.search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		searchView.setSearchableInfo(searchManager.getSearchableInfo(ShowAllInspections.this.getComponentName()));
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
				return  false;



			}
		};
		searchView.setOnQueryTextListener(textChangeListener);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

			case R.id.action_addInspection:

               Intent i = new Intent(ShowAllInspections.this,AddInspectionForm.class);
				i.putExtra("propertyName",propertyName);
				i.putExtra("proeprtyId",property_id);
				startActivityForResult(i, MainActivity.REQUESTCODE);
				overridePendingTransition(R.animator.toptobottom, R.animator.toptobottom);
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



	public void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("List of Inspection");
		toolbar.setTitleTextColor(getResources().getColor(R.color.yellow));
		setSupportActionBar(toolbar);
		final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		upArrow.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
		getSupportActionBar().setHomeAsUpIndicator(upArrow);
	}





	public class GetAllInspection extends AsyncTask<Void,Void,ArrayList<Inspection>>
	{

		@Override
		protected ArrayList<Inspection> doInBackground(Void... params) {


			ArrayList<Inspection> allinspections = new ArrayList<Inspection>();
			Long pId = myDb.getSelectedPropertyId(propertyName);

			Cursor allInspection = myDb.getAllInspections(pId);

			if(allInspection.getCount() == 0)
			{
				Log.d("tag", "No Data To Show");
				return null;
			}

			//StringBuffer buffer = new StringBuffer();


			String  InspectionName ;

			while(allInspection.moveToNext())
			{

				Inspection ins = new Inspection();
				InspectionName = allInspection.getString(1);
				ins.setId(allInspection.getInt(0));
				ins.setName(allInspection.getString(1));
				ins.setType(allInspection.getString(2));
				Log.d("tag", "Inspection is  "+InspectionName);

				allinspections.add(ins);

			}


			allInspection.close();
			return allinspections;

		}


		@Override
		protected void onPostExecute(ArrayList<Inspection> list) {
			inspectionList = list;
			adapter = new CustomAdapterShowAllInspection(ShowAllInspections.this, R.layout.chooseinspectionsinglerow, inspectionList);
			showAllInspection.setAdapter(adapter);
		}
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		deleteButton.setVisibility(View.GONE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		myDb.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		deleteButton.setVisibility(View.GONE);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d("tag", "Back from hardwaer button");
		overridePendingTransition(R.animator.backright, R.animator.backin);
		deleteButton.setVisibility(View.GONE);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
