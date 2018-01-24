package co.uk.inspection.CustomAdapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import co.uk.inspection.TableClasses.Inspection;


public class CustomAdapterChooseInspection  extends ArrayAdapter<Inspection>{

	Context context ;
	int layoutResourceId;
	 ArrayList<Inspection> inspectionList = new ArrayList<Inspection>();
	
	public CustomAdapterChooseInspection(Context context, int resource, ArrayList<Inspection> propertyData) {
		super(context, resource, propertyData);
		// TODO Auto-generated constructor stub
		 this.layoutResourceId = resource;
		  this.context = context;
		  this.inspectionList =  propertyData;
		
		
	}
	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View item = convertView;
		ViewHolder holder = null;

		if (item == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			item = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.InspectionType = (TextView) item.findViewById(co.uk.inspection.R.id.textViewInspectionTypeSingleRow);
			
			
			
			item.setTag(holder);
		} else {
			holder = (ViewHolder) item.getTag();
		}

		// get the object at position
     Inspection ins = inspectionList.get(position);
	 holder.InspectionType.setText(ins.getType());
	 holder.InspectionType.setTextSize(20);
	
		
		
		
		return item;

	}

	static class ViewHolder {
		TextView InspectionType;
				
	
	}
	
	
	
}
