package co.uk.inspection;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import co.uk.inspection.DBHelper.DbHelperClass;

public class AddInspectionForm extends AppCompatActivity {

	Spinner chooseInscpectionType;
	Toolbar toolbar;
	EditText inspectionName ;
	String inspectionType,propertyName;
	DbHelperClass myDB;
	String name,type;
	Long selected_proeprty_id;
	Button save;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addinspectionfor);
		initToolBar();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Intent i = getIntent();
		propertyName = i.getStringExtra("propertyName");
		Log.d("tag", "Property Name is " + propertyName);
		//create Database
		myDB = new DbHelperClass(this);
		spinnerWork();
		setupVariables();

		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveInspectionDataToDb();
			}
		});


	}

	public void setupVariables() {
		inspectionName = (EditText) findViewById(R.id.editTextInspectionName);
		save = (Button)findViewById(R.id.buttonSaveInspection);


	}

	public void spinnerWork() {


		// Initializing a String Array
		String[] inspection_types = new String[]{
				"Choose Inspection Type",
				"Move In Inspection",
		"Move Out Inspection",
		"Periodic Inspection",

		};

		chooseInscpectionType = (Spinner) findViewById(R.id.spinnerInspectionType);
		// Initializing an ArrayAdapter
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this,android.R.layout.simple_spinner_item,inspection_types){
			@Override
			public View getDropDownView(int position, View convertView,
										ViewGroup parent) {
				View view = super.getDropDownView(position, convertView, parent);
				TextView tv = (TextView) view;
				if(position%2 == 1) {
					// Set the item background color
					tv.setBackgroundColor(Color.parseColor("#FFF9A600"));
				}
				else {
					// Set the alternate item background color
					tv.setBackgroundColor(Color.parseColor("#FFE49200"));
				}
				return view;
			}
		};
		adapter.setDropDownViewResource(R.layout.spinnerdropdownview);
	//	adapter.setDropDownViewTheme();
		chooseInscpectionType.setAdapter(adapter);

		chooseInscpectionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				inspectionType = (String) parent.getItemAtPosition(position);
				Log.d("tag","Inspection Type is "+inspectionType);


			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
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

	public void saveInspectionDataToDb() {


		 name = inspectionName.getText().toString();
		 type = inspectionType;


		selected_proeprty_id =	myDB.getSelectedPropertyId(propertyName);

		long idOfDuplicateName = myDB.checkInspectionName(name,selected_proeprty_id);
		if(idOfDuplicateName == 0l)
		{

		} else
		{
			Toast.makeText(getApplicationContext(),"Name Already Exist",Toast.LENGTH_SHORT).show();
			return;
		}



		new InsertInspectionData().execute();




	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d("tag", "Back from hardwaer button");
		overridePendingTransition(R.animator.bottomtotop, R.animator.bottomtotop);
	}

	public void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("Add Inspection ");
		toolbar.setTitleTextColor(getResources().getColor(R.color.yellow));
		setSupportActionBar(toolbar);
		final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		upArrow.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
		getSupportActionBar().setHomeAsUpIndicator(upArrow);
	}



	class InsertInspectionData extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params) {

			Log.d("tag","Property Id is "+selected_proeprty_id);
			boolean insertInspectionData = myDB.insertInspectionData(name,type,selected_proeprty_id);

			if(insertInspectionData == true)
			{
				Log.d("tag","Inspection Data Inserted");
			}
			else
			{
				Log.d("tag","Inspection Data Not Inserted Error");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {

			Intent resultIntent = new Intent();
// TODO Add extras or a data URI to this intent as appropriate.
			resultIntent.putExtra("property_id",selected_proeprty_id);
			setResult(Activity.RESULT_OK, resultIntent);
			finish();

			super.onPostExecute(aVoid);
		}
	}

}
