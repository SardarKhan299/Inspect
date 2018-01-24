package co.uk.inspection.CustomAdapters;

import java.util.ArrayList;

import co.uk.inspection.TableClasses.Property;



import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Property> implements Filterable {

	Context context ;
	int layoutResourceId;
	 ArrayList<Property> data = new ArrayList<Property>();
	ArrayList<Property> filterList =new ArrayList<Property>();
	CustomFillter filter;
	
	public CustomAdapter(Context context, int resource, ArrayList<Property> propertyData) {
		super(context, resource, propertyData);
		// TODO Auto-generated constructor stub
		 this.layoutResourceId = resource;
		  this.context = context;
		  this.data =  propertyData;
		this.filterList = propertyData;

		
		
	}




	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Property getItem(int position) {
		return data.get(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View item = convertView;
		ViewHolder holder = null;

		if (item == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			item = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.Propertyname = (TextView) item.findViewById(co.uk.inspection.R.id.textViewPropertyNameSingleRow);
			holder.propertyAddress = (TextView) item.findViewById(co.uk.inspection.R.id.textViewPropertyAddressSingleRow);

			item.setTag(holder);
		} else {
			holder = (ViewHolder) item.getTag();
		}

		// get the object at position
	 holder.Propertyname.setText(data.get(position).getName());
	 holder.Propertyname.setTextSize(30);
	 holder.propertyAddress.setText(data.get(position).getAddress()+" "+data.get(position).getPostCode());

	 
		
		
		
		return item;

	}

	static class ViewHolder {
		TextView Propertyname;
		TextView propertyAddress;

		
	
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
				ArrayList<Property> filters = new ArrayList<Property>();

				// get specific item
				for(int i =0 ; i<filterList.size();i++)
				{
					if(filterList.get(i).getName().toUpperCase().contains(constraint))
					{
						// if constraint is in the list
						Property p1 = new Property();
						p1.setName(filterList.get(i).getName());
						p1.setAddress(filterList.get(i).getAddress());
						p1.setPostCode(filterList.get(i).getPostCode());
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
			data = (ArrayList<Property>)results.values;
			notifyDataSetChanged();

		}
	}

}
