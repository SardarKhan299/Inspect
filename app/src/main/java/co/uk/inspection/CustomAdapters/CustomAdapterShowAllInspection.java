package co.uk.inspection.CustomAdapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import co.uk.inspection.R;
import co.uk.inspection.TableClasses.Inspection;

public class CustomAdapterShowAllInspection extends ArrayAdapter<Inspection>{
	Context context ;
	int layoutResourceId;
	 ArrayList<Inspection> inspectionList = new ArrayList<Inspection>();
    ArrayList<Inspection> filterList =new ArrayList<Inspection>();
    CustomFillter filter;
    private SparseBooleanArray mSelectedItemsIds;


    public CustomAdapterShowAllInspection(Context context, int resource, ArrayList<Inspection> propertyData) {
		super(context, resource, propertyData);
		// TODO Auto-generated constructor stub
        mSelectedItemsIds = new SparseBooleanArray();
		 this.layoutResourceId = resource;
        this.context = context;
        try
        {
            this.inspectionList =  propertyData;
            this.filterList = propertyData;
        }
        catch (NullPointerException ex)
        {
            Log.d("tag","Excepotion Null"+ex.toString());
        }

		
		
	}






    @Override
    public Inspection getItem(int position) {
        return inspectionList.get(position);
    }

    @Override
    public int getCount() {
        if(inspectionList != null)
        {
            return inspectionList.size();
        }
        else
        return 0;


    }

    @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View item = convertView;
		ViewHolder holder = null;
try {
    if (item == null) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        item = inflater.inflate(layoutResourceId, parent, false);
        holder = new ViewHolder();
        holder.InspectionType = (TextView) item.findViewById(R.id.textViewInspectionTypeSingleRow);
        holder.InspectionName = (TextView) item.findViewById(R.id.textViewInspectionNameSingleRowShowAllInpsectio);


        item.setTag(holder);
    } else {
        holder = (ViewHolder) item.getTag();
    }

    // get the object at position
    Inspection ins = inspectionList.get(position);
    holder.InspectionName.setText(ins.getName());
    holder.InspectionName.setTextSize(30);
    holder.InspectionType.setText(ins.getType());

}

catch (NullPointerException ex)
{
    Log.d("tag","Excepotion Null"+ex.toString());
}

    return item;


}




    @Override
    public void remove(Inspection object) {
        inspectionList.remove(object);
        notifyDataSetChanged();
    }


    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }



	static class ViewHolder {
		TextView InspectionType;
		TextView InspectionName;
				
	
	}



    @Override
    public Filter getFilter() {
        if(filter == null)
        {
            filter = new CustomFillter();
        }
        return  filter;

    }

    class CustomFillter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if(constraint != null && constraint.length()>0)
            {
                constraint = constraint.toString().toUpperCase();
                ArrayList<Inspection> filters = new ArrayList<Inspection>();

                // get specific item
                for(int i =0 ; i<filterList.size();i++)
                {
                    if(filterList.get(i).getName().toUpperCase().contains(constraint))
                    {
                        // if constraint is in the list
                        Inspection p1 = new Inspection();
                        p1.setName(filterList.get(i).getName());
                        p1.setType(filterList.get(i).getType());
                        filters.add(p1);


                    }

                }
                // set the size
                results.count = filters.size();
                results.values = filters;



            }

            else
            {
                // if no query use the old List filter List

                results.count = filterList.size();
                results.values = filterList;

            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // now publish the List
            inspectionList = (ArrayList<Inspection>)results.values;
            notifyDataSetChanged();

        }
    }


	
}
