package co.uk.inspection;



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


import co.uk.inspection.Activities.AddInspectorForm;

public class MainActivity extends AppCompatActivity  {
	
	public static final int REQUESTCODE =123;
	private static final int RESOLVE_CONNECTION_REQUEST_CODE = 456;
	private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 000000;
	private static final int MY_PERMISSIONS_REQUEST_CAMERA = 000001;
	private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 000002;



	Button addProperty,manageProperty,addInspector;
    Toolbar toolbar;
	//GoogleApiClient mGoogleApiClient;


	@Override
	protected void onStart() {
		super.onStart();
	//	mGoogleApiClient.connect();
		checkWriteExternalPermission();
		checkCameraPermission();
		checkReadExternalStoragePermission();


	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addProperty = (Button) findViewById(R.id.buttonAddProperty);
		manageProperty = (Button) findViewById(R.id.buttonManageProperties);
		addInspector = (Button) findViewById(R.id.buttonAddInspector);
		initToolBar();
//
//		if (mGoogleApiClient == null) {
//			// Create the API client and bind it to an instance variable.
//			// We use this instance as the callback for connection and connection
//			// failures.
//			// Since no account name is passed, the user is prompted to choose.
//			mGoogleApiClient = new GoogleApiClient.Builder(this)
//					.addApi(Drive.API)
//					.addScope(Drive.SCOPE_FILE)
//					.addConnectionCallbacks(this)
//					.addOnConnectionFailedListener(this)
//					.build();
//		}
//		// Connect the client. Once connected, the camera is launched.
//		mGoogleApiClient.connect();




		manageProperty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Show List of Properties

				Intent i = new Intent(MainActivity.this, showAllProperties.class);
				startActivity(i);
				overridePendingTransition(R.animator.righttoleft, R.animator.lefttoright);
			}
		});
		
		
		
		addProperty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//	Log.d("tag", ""+myDB.toString());

				Intent i = new Intent(MainActivity.this, formAddProperty.class);
				startActivityForResult(i, REQUESTCODE);
				overridePendingTransition(R.animator.toptobottom, R.animator.toptobottom);

			}
		});

		addInspector.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// load add inspector Form

				Intent i = new Intent(MainActivity.this, AddInspectorForm.class);
				startActivity(i);
				overridePendingTransition(R.animator.toptobottom, R.animator.toptobottom);
			}
		});


	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUESTCODE)
		{
			if(resultCode == RESULT_OK)
			{
				Log.d("tag", "return");

				
			}
			
		}

//	if(requestCode == RESOLVE_CONNECTION_REQUEST_CODE) {
//		if (resultCode == RESULT_OK) {
//			mGoogleApiClient.connect();
//		}
//	}


	}



	public void checkWriteExternalPermission()
	{
		// Here, thisActivity is the current activity
		if (ContextCompat.checkSelfPermission(MainActivity.this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			// No explanation needed, we can request the permission.

			ActivityCompat.requestPermissions(MainActivity.this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
					MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

			// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
			// app-defined int constant. The callback method gets the
			// result of the request.

		}
	}

	private void checkReadExternalStoragePermission()
	{
		// Here, thisActivity is the current activity
		if (ContextCompat.checkSelfPermission(MainActivity.this,
				Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			// No explanation needed, we can request the permission.

			ActivityCompat.requestPermissions(MainActivity.this,
					new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
					MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

			// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
			// app-defined int constant. The callback method gets the
			// result of the request.

		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
				Log.d("tag", "Permission ");
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay! Do the
					// contacts-related task you need to do.


					Log.d("tag", "Permission Granted Write External ");

				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
					Log.d("tag", "Permission Not Granted Write External ");
				}
				return;
			}

			case MY_PERMISSIONS_REQUEST_CAMERA: {
				Log.d("tag", "Permission ");
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay! Do the
					// contacts-related task you need to do.


					Log.d("tag", "Permission Granted Camera ");

				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
					Log.d("tag", "Permission Not Granted");
				}
				return;
			}

				case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
					Log.d("tag", "Permission ");
					// If request is cancelled, the result arrays are empty.
					if (grantResults.length > 0
							&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

						// permission was granted, yay! Do the
						// contacts-related task you need to do.


						Log.d("tag", "Permission Granted Read External");

					} else {

						// permission denied, boo! Disable the
						// functionality that depends on this permission.
						Log.d("tag", "Permission Not Granted Read External ");
					}
					return;
				}
				// other 'case' lines to check for other
				// permissions this app might request
			}
		}






	public void checkCameraPermission()
	{
		// Here, thisActivity is the current activity
		if (ContextCompat.checkSelfPermission(MainActivity.this,
				Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED) {
			// No explanation needed, we can request the permission.

			ActivityCompat.requestPermissions(MainActivity.this,
					new String[]{Manifest.permission.CAMERA},
					MY_PERMISSIONS_REQUEST_CAMERA);

			// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
			// app-defined int constant. The callback method gets the
			// result of the request.

		}





	}





	public void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("Inspector");
		toolbar.setTitleTextColor(getResources().getColor(R.color.yellow));

		setSupportActionBar(toolbar);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d("tag", "Back from hardwaer button");
		overridePendingTransition(R.animator.backright, R.animator.backin);
	}


}
