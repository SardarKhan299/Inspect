package co.uk.inspection.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import co.uk.inspection.R;

/**
 * Created by Android on 6/2/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    Context context;
    int layoutResourceId;
    private SparseBooleanArray mSelectedItemsIds;
    Uri uri;
    ViewHolder holder;



    ArrayList<String> f = new ArrayList<String>();

    public ImageAdapter(Context context, int resource,ArrayList<String> imagesPath) {
        this.f = imagesPath;
        this.context = context;
        this.layoutResourceId = resource;
        mSelectedItemsIds = new SparseBooleanArray();
    }


    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }


    public int getCount() {
        return f.size();
    }

    @Override
    public String getItem(int position) {
        return f.get(position);
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void remove(String object) {
        f.remove(object);
        notifyDataSetChanged();
    }



    public long getItemId(int position) {
        return position;
    }




    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder.imageview = (ImageView) convertView.findViewById(R.id.full_image_view);

            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int width = metrics.widthPixels / 3;
            int height = metrics.heightPixels / 4;

            holder.imageview.getLayoutParams().height = height;
            holder.imageview.getLayoutParams().width = width;
            holder.imageview.requestLayout();

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }



        uri = Uri.fromFile(new File(f.get(position)));

        Picasso.with(context)
                .load(uri)
                .fit()
                .error(R.drawable.logo)
                .into(holder.imageview);

        Picasso picasso = new Picasso.Builder(context).listener(new Picasso.Listener() {
            @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        }).build();


        return convertView;
    }


}
class ViewHolder {
    ImageView imageview;
}
