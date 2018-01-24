package co.uk.inspection.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.uk.inspection.DBHelper.DbHelperClass;
import co.uk.inspection.R;
import co.uk.inspection.TableClasses.Areas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Android on 3/24/2016.
 */
public class CustomAdapterShowAllAreas extends ArrayAdapter<Areas> {

    Context context;
    int layoutResourceId,propertyId,inspectionId;
    ArrayList<Areas> listOfAreas = new ArrayList<Areas>();
    private SparseBooleanArray mSelectedItemsIds;
    static DbHelperClass myDb;
    final int INVALID_ID = -1;
    private final int INVALID = -1;
    protected int DELETE_POS = -1;
    protected int RIGHT_SWIPE = 123;



    HashMap<Areas, Integer> mIdMap = new HashMap<>();

    public CustomAdapterShowAllAreas(Context context, int resource, ArrayList<Areas> areas,int property_id , int inspection_id) {
        super(context, resource, areas);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.layoutResourceId = resource;
        this.listOfAreas = areas;
        this.propertyId = property_id;
        this.inspectionId = inspection_id;
        this.myDb = new DbHelperClass(this.context);
        // for sorting
        for (int i = 0; i < areas.size(); ++i) {
            mIdMap.put(areas.get(i), i);
        }

    }

    @Override
    public int getCount() {
        return listOfAreas.size();
    }



    public List<Areas> getAllAreas() {
        return listOfAreas;
    }

    public void onSwipeItem(boolean isRight, int position) {
        // TODO Auto-generated method stub
        if (isRight == false) {
            DELETE_POS = position;
        } else if (DELETE_POS == position) {
            DELETE_POS = INVALID;
        }
        else if(isRight == true)
        {
            DELETE_POS = RIGHT_SWIPE;
        }
        //
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        Areas item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
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
            holder.Areaname = (TextView) item.findViewById(co.uk.inspection.R.id.textViewSingleRowShowAllAreas);
            holder.icon = (ImageView) item.findViewById(co.uk.inspection.R.id.imageViewArea);
            holder.delete = (ImageView) item.findViewById(R.id.buttonDelete);
            holder.deleteLayout = (LinearLayout) item.findViewById(R.id.deleteLayout);

            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        // get the object at position
        holder.Areaname.setText(listOfAreas.get(position).getName());
        holder.Areaname.setTextSize(30);


       if (DELETE_POS == position) {

        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.righttoleftswipe);
       holder.deleteLayout.startAnimation(hyperspaceJumpAnimation);

           holder.deleteLayout.setVisibility(View.VISIBLE);
//
//        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.righttoleftswipe);
//        item.startAnimation(hyperspaceJumpAnimation);

//           holder.Areaname.startAnimation(hyperspaceJumpAnimation);
//           holder.icon.setVisibility(View.INVISIBLE);
//           holder.Areaname.setVisibility(View.INVISIBLE);

       }
//       else if(DELETE_POS == RIGHT_SWIPE)
//       {
//           Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.lefttorightswipe);
//           holder.icon.startAnimation(hyperspaceJumpAnimation);
//           holder.Areaname.startAnimation(hyperspaceJumpAnimation);
//           holder.icon.setVisibility(View.VISIBLE);
//           holder.Areaname.setVisibility(View.VISIBLE);
//
//           holder.delete.setVisibility(View.GONE);
//       }
       else {
           Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.lefttorightswipe);
           holder.deleteLayout.startAnimation(hyperspaceJumpAnimation);
           holder.deleteLayout.setVisibility(View.GONE);
       }

       holder.delete.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               // TODO Auto-generated method stub
               deleteItem(position);
               Log.d("tag","Item Deleted");
           }
       });

        // check if area is checked or not
        if(isAeraComplete(listOfAreas.get(position).getName(), listOfAreas.get(position).getId()))
        {
            holder.icon.setImageResource(co.uk.inspection.R.drawable.ic_action_tick);

        }

        else
        {

            holder.icon.setImageResource(co.uk.inspection.R.drawable.ic_action_cancel);
        }



        return item;

    }

    private void deleteItem(int position) {

        Areas a = getItem(position);
        myDb.deleteAreas(a.getId(),inspectionId,propertyId,a.getName());
        listOfAreas.remove(position);
        DELETE_POS = INVALID;
        notifyDataSetChanged();
    }

    private boolean isAeraComplete(String area_name, int area_id) {

       int iscomplete =  myDb.checkCompleteAreaName(area_name,area_id,inspectionId,propertyId);

        if(iscomplete == 1)
        {
            return  true;
        }

        return false;
    }

    @Override
    public void remove(Areas object) {
        listOfAreas.remove(object);
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
        TextView Areaname;
        ImageView icon,delete;
        LinearLayout deleteLayout;
    }


}
