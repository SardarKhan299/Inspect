package co.uk.inspection.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import co.uk.inspection.CustomAdapters.ImageAdapter;
import co.uk.inspection.R;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Android on 6/2/2016.
 */
public class FullImage extends AppCompatActivity {




    Toolbar toolbar;
    String imgPath,inspectionName;
    ImageView fullImage;
    PhotoViewAttacher mAttacher;

    ArrayList<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);
        initToolBar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fullImage = (ImageView) findViewById(R.id.imageViewFullImage);

        Intent i = getIntent();
        imgPath = i.getStringExtra("imagePath");

        Log.d("tag", "AreaName is " + imgPath );

        // this is for zoom funtionality
        mAttacher = new PhotoViewAttacher(fullImage);


        uri = Uri.fromFile(new File(imgPath));

        Picasso.with(getApplicationContext())
                .load(uri)
                .fit()
                .error(R.drawable.logo)
                .into(fullImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        mAttacher.update();
                    }

                    @Override
                    public void onError() {

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
        menu.findItem(R.id.action_addSection).setVisible(false);
        menu.findItem(R.id.action_ShowImages).setVisible(false);



        return super.onCreateOptionsMenu(menu);
    }


    public void initToolBar() {
        toolbar = (Toolbar) findViewById(co.uk.inspection.R.id.toolbar);
        toolbar.setTitle("Full Image");
        toolbar.setTitleTextColor(getResources().getColor(co.uk.inspection.R.color.yellow));
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(co.uk.inspection.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(co.uk.inspection.R.color.yellow), PorterDuff.Mode.SRC_ATOP);
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

            case R.id.rotateImage:

                float rotation  = fullImage.getRotation()+90;
               fullImage.setRotation(rotation);
                mAttacher.update();

                break;

            default:

                break;
        }
        return true;

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("tag", "Back from hardwaer button");
        overridePendingTransition(R.animator.bottomtotop, R.animator.bottomtotop);
    }







}
