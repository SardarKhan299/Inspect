package co.uk.inspection.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import co.uk.inspection.R;
import co.uk.inspection.TableClasses.Areas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 5/3/2016.
 */

public class CustomAdapterShowAllAreasToDuplicate extends ArrayAdapter<Areas> {

    Context context;
    int layoutResourceId;
    ArrayList<Areas> listOfAreas = new ArrayList<Areas>();
    private SparseBooleanArray mSelectedItemsIds;


    public CustomAdapterShowAllAreasToDuplicate(Context context, int resource, ArrayList<Areas> areas) {
        super(context, resource, areas);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.layoutResourceId = resource;
        this.listOfAreas = areas;

    }



    @Override
    public int getCount() {
        return listOfAreas.size();
    }

    @Override
    public int getViewTypeCount() {
        //Count=Size of ArrayList.
        return listOfAreas.size();
    }

    public List<Areas> getAllAreas() {
        return listOfAreas;
    }


    @Override
    public int getItemViewType(int position) {

        return position;
    }


    @Override
    public Areas getItem(int position) {
        return listOfAreas.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView;
        ViewHolder holder = null;

        if (item == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            item = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.Areaname = (TextView) item.findViewById(R.id.textViewSingleRowShowAllAreas);

            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        // get the object at position
        holder.Areaname.setText("Duplicate "+listOfAreas.get(position).getName());
        holder.Areaname.setTextSize(20);


        return item;

    }



    static class ViewHolder {
        TextView Areaname;
    }

}
