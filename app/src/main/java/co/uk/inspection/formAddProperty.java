package co.uk.inspection;

import co.uk.inspection.DBHelper.DbHelperClass;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class formAddProperty extends AppCompatActivity{

	EditText edtPropertyName,edtPropertyAddress,edtZip,edtLandLordName,edtTenantName,edtTenantAddress,edtTenantPhoneNum,edtTenantEmail;
	DbHelperClass myDB;
	Toolbar toolbar;
	Button save;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("tag","Oncreate Call Add Property");
		setContentView(R.layout.formarddproperty);
		//create Database
		myDB = new DbHelperClass(this);
		initToolBar();

		// setup Varaibles Ids
		setUpVariables();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Log.d("tag", "" + myDB.getDatabaseName());


		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				savePropertyDataToDb();
				setResult(RESULT_OK);
				finish();
				overridePendingTransition(R.animator.bottomtotop, R.animator.bottomtotop);
			}
		});



		
		
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d("tag", "Back from hardwaer button");
		overridePendingTransition(R.animator.bottomtotop, R.animator.bottomtotop);
	}

	private void setUpVariables() {
		// TODO Auto-generated method stub
		edtPropertyName = (EditText) findViewById(R.id.editTextPropertyName);
		edtPropertyAddress = (EditText) findViewById(R.id.editTextPropertyAddress);
		edtZip = (EditText) findViewById(R.id.editTextZipCode);
		edtLandLordName = (EditText) findViewById(R.id.editTextLandLordName);
		edtTenantName = (EditText) findViewById(R.id.editTextTenantName);
		edtTenantAddress = (EditText) findViewById(R.id.editTextTenantAddress);
		edtTenantPhoneNum = (EditText) findViewById(R.id.editTextTenantPhoneNumber);
		edtTenantEmail = (EditText) findViewById(R.id.editTextTenantEmail);
		save = (Button)findViewById(R.id.saveData);


		
	
		
		
	}

	public void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("Add Property");
		toolbar.setTitleTextColor(getResources().getColor(R.color.yellow));
		setSupportActionBar(toolbar);
		final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		upArrow.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
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


	private void savePropertyDataToDb() {
		// TODO Auto-generated method stub
		String propertyName = edtPropertyName.getText().toString();
		String propertyAddress = edtPropertyAddress.getText().toString();
		String zip = edtZip.getText().toString();
		
		String LandLordName = edtLandLordName.getText().toString();
		
		
		String TenatName = edtTenantName.getText().toString();
		String TenatAddress = edtTenantAddress.getText().toString();
		String TenatEmail = edtTenantEmail.getText().toString();
		String TenatNumber = edtTenantPhoneNum.getText().toString();
		
boolean insertedL =	myDB.insertLandLordData(LandLordName);
		
		if(insertedL == true)
		{
			Log.d("tag", "data Inserted Land LORD"+myDB.getDatabaseName());
			Toast.makeText(getApplicationContext(), "CREATED",Toast.LENGTH_SHORT).show();
		}
		else
		{
			Log.d("tag", "data Not Inserted");
		}
		
	boolean inserted =	myDB.insertPropertyData(propertyName,propertyAddress,zip);
	
		if(inserted == true)
		{
			Log.d("tag", "data Inserted"+myDB.getDatabaseName());
			Toast.makeText(getApplicationContext(), "CREATED",Toast.LENGTH_SHORT).show();
		}
		else
		{
			Log.d("tag", "data Not Inserted");
		}
		
		
		
boolean insertedT =	myDB.insertTenantData(TenatName, TenatAddress, TenatEmail, TenatNumber);
		
		if(insertedT == true)
		{
			Log.d("tag", "data Inserted Tenants"+myDB.getDatabaseName());
			Toast.makeText(getApplicationContext(), "CREATED",Toast.LENGTH_SHORT).show();
		}
		else
		{
			Log.d("tag", "data Not Inserted");
		}
		
		
		
	}



	@Override
	protected void onStart() {
		super.onStart();
		Log.d("tag", "On start Call Add Property");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("tag", "On Resume Call Add Property");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("tag", "On Pause Call Add Property");
		myDB.close();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("tag", "On stop Call Add Property");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("tag", "On destroy Call Add Property");
	}




}
