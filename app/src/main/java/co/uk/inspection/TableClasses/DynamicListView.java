package co.uk.inspection.TableClasses;

/**
 * Created by Android on 5/31/2016.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.security.AlgorithmParameterGenerator;
import java.util.ArrayList;


import co.uk.inspection.Activities.Globals;
import co.uk.inspection.CustomAdapters.CustomAdapterShowAllAreas;
import co.uk.inspection.DBHelper.DbHelperClass;

public class DynamicListView extends ListView {

    private final int SMOOTH_SCROLL_AMOUNT_AT_EDGE = 15;
    private final int MOVE_DURATION = 150;
    private final int LINE_THICKNESS = 15;
    public static final int REQUEST_CODE = 100;


    public ArrayList<Areas> mCheeseList;
    ShowAllAreas context;
    DbHelperClass myDb;

    String inspectionName, inspectionType, areaName,fileName;
            int inspectionId,propertyId;


    private int mLastEventY = -1;

    private int mDownY = -1;
    private int mDownX = -1;

    private int mTotalOffset = 0;

    private boolean mCellIsMobile = false;
    private boolean mIsMobileScrolling = false;
    private int mSmoothScrollAmountAtEdge = 0;

    private final int INVALID_ID = -1;
    private long mAboveItemId = INVALID_ID;
    private long mMobileItemId = INVALID_ID;
    private long mBelowItemId = INVALID_ID;

    private BitmapDrawable mHoverCell;
    private Rect mHoverCellCurrentBounds;
    private Rect mHoverCellOriginalBounds;

    private final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;

    private boolean mIsWaitingForScrollFinish = false;
    private int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;



    public DynamicListView(Context context) {
        super(context);
        init(context);
    }


    public DynamicListView(Context context,int inspectionId , int propertyId) {
        super(context);
        Log.d("tag","Constrcutor Call of Dynamic ListView");
        this.inspectionId = inspectionId;
        this.propertyId = propertyId;
        Log.d("tag", " Dynamic Listview InspectionId  is " + inspectionId + "property id is " + propertyId);
    }
    public DynamicListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DynamicListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {

        Log.d("tag","Init Call of ListView ");
        this.context = (ShowAllAreas) context;
        setOnItemLongClickListener(mOnItemLongClickListener);
      //  setOnItemClickListener(mOnItemClickListener);
        setOnScrollListener(mScrollListener);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mSmoothScrollAmountAtEdge = (int) (SMOOTH_SCROLL_AMOUNT_AT_EDGE / metrics.density);
        myDb = new DbHelperClass(context);
    }




//    AdapterView.OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//            Log.d("tag", " Dynamic Listview InspectionId  is " + inspectionId + "property id is " + propertyId);
//
//            Areas area = (Areas) parent.getItemAtPosition(position);
//            Long area_data_id =   myDb.getAreaDataId(area.getId(),inspectionId,propertyId,area.getName());
//
////            Log.d("tag", "Area data id is " + area_data_id);
//
//            Intent i = new Intent(context, InspectionLayout.class);
//            i.putExtra("area_name", area.getName());
//            i.putExtra("position", position);
//            i.putExtra("inspectionId", inspectionId);
//            i.putExtra("areaId", area.getId());
//            i.putExtra("inspectionName", inspectionName);
//            i.putExtra("area_data_id",area_data_id);
//            i.putExtra("propertyId",propertyId);
//
//
//            context.startActivityForResult(i, REQUEST_CODE);
//            context.overridePendingTransition(co.uk.inspection.R.animator.righttoleft, co.uk.inspection.R.animator.lefttoright);
//
//
//
//        }
//    };




    // first important thing
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

            Log.d("tag","Call On Item Long Click Listener");
            Log.d("tag", "Your Starting position is " + pos);
            mTotalOffset = 0;


            int position = pointToPosition(mDownX, mDownY);
            int itemNum = position - getFirstVisiblePosition();

            View selectedView = getChildAt(itemNum);
            mMobileItemId = getAdapter().getItemId(position);
            mHoverCell = getAndAddHoverView(selectedView);
            selectedView.setVisibility(INVISIBLE);

            mCellIsMobile = true;

            updateNeighborViewsForID(mMobileItemId);

            return true;
        }
    };

    private BitmapDrawable getAndAddHoverView(View v) {

      //  Log.d("tag","get and Add Hover View");

        int w = v.getWidth();
        int h = v.getHeight();
        int top = v.getTop();
        int left = v.getLeft();

        Bitmap b = getBitmapWithBorder(v);

        BitmapDrawable drawable = new BitmapDrawable(getResources(), b);

        mHoverCellOriginalBounds = new Rect(left, top, left + w, top + h);
        mHoverCellCurrentBounds = new Rect(mHoverCellOriginalBounds);

        drawable.setBounds(mHoverCellCurrentBounds);

        return drawable;
    }

    private Bitmap getBitmapWithBorder(View v) {

       // Log.d("tag","Call getBitmapWith border");

        Bitmap bitmap = getBitmapFromView(v);
        Canvas can = new Canvas(bitmap);

        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(LINE_THICKNESS);
        paint.setColor(Color.BLACK);

        can.drawBitmap(bitmap, 0, 0, null);
        can.drawRect(rect, paint);

        return bitmap;
    }

    // it return the bitmap showing the screenshot of the view passed in
    private Bitmap getBitmapFromView(View v) {
      //  Log.d("tag","Call getBitmap from View");
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private void updateNeighborViewsForID(long itemID) {
      //  Log.d("tag","Call Update Neighbor View For ID");
        // it get the position of the View
        int position = getPositionForID(itemID);
        CustomAdapterShowAllAreas adapter = (( CustomAdapterShowAllAreas) getAdapter());
        mAboveItemId = adapter.getItemId(position - 1);
        mBelowItemId = adapter.getItemId(position + 1);
    }

    public View getViewForID(long itemID) {
      //  Log.d("tag","Call getView for ID");
        // get first visible position
        int firstVisiblePosition = getFirstVisiblePosition();

        CustomAdapterShowAllAreas adapter = (( CustomAdapterShowAllAreas) getAdapter());
        // get all childs for the view
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            int position = firstVisiblePosition + i;
            long id = adapter.getItemId(position);
            // if id of the child match with the given id of the view then retrun the view
            if (id == itemID) {
                return v;
            }
        }
        return null;
    }

    public int getPositionForID(long itemID) {
    //    Log.d("tag","Call getposition For ID");
        View v = getViewForID(itemID);
        if (v == null) {
            return -1;
        } else {
            // now return the position of the view
            return getPositionForView(v);
        }
    }


    // if we have the selected view then it is drawn on the listView
    // if we are draging then it should be drawing the cell on the top of ListView
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
  //      Log.d("tag", "Call Dispatch Draw");
        if (mHoverCell != null) {
            mHoverCell.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                mActivePointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER_ID) {
                    break;
                }

                int pointerIndex = event.findPointerIndex(mActivePointerId);

                mLastEventY = (int) event.getY(pointerIndex);
                int deltaY = mLastEventY - mDownY;

                // if user is in draging position
                if (mCellIsMobile) {
                    mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left,
                            mHoverCellOriginalBounds.top + deltaY + mTotalOffset);
                    mHoverCell.setBounds(mHoverCellCurrentBounds);
                    invalidate();

                    handleCellSwitch();

                    mIsMobileScrolling = false;
                    handleMobileCellScroll();

                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                touchEventsEnded();
                break;
            case MotionEvent.ACTION_CANCEL:
                touchEventsCancelled();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    touchEventsEnded();
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    private void handleCellSwitch() {

        final int deltaY = mLastEventY - mDownY;
        int deltaYTotal = mHoverCellOriginalBounds.top + mTotalOffset + deltaY;

        View belowView = getViewForID(mBelowItemId);
        View mobileView = getViewForID(mMobileItemId);
        View aboveView = getViewForID(mAboveItemId);

        boolean isBelow = (belowView != null)
                && (deltaYTotal > belowView.getTop());
        boolean isAbove = (aboveView != null)
                && (deltaYTotal < aboveView.getTop());

        if (isBelow || isAbove) {

            final long switchItemID = isBelow ? mBelowItemId : mAboveItemId;
            View switchView = isBelow ? belowView : aboveView;
            final int originalItem = getPositionForView(mobileView);

            if (switchView == null) {
                updateNeighborViewsForID(mMobileItemId);
                return;
            }

            swapElements(mCheeseList, originalItem,
                    getPositionForView(switchView));
            int pos1 = originalItem;
            int pos2 = getPositionForView(switchView);

            Log.d("tag", "item 1 position is " + originalItem);
            Log.d("tag", "item 2 position is " + getPositionForView(switchView));

            String item1Name = mCheeseList.get(pos1).getName();
            String item2Name = mCheeseList.get(pos2).getName();
            int item1Id = mCheeseList.get(pos1).getId();
            int item2Id = mCheeseList.get(pos2).getId();


            Log.d("tag", "Item 1 Name is" + item1Name + " id is " + item1Id + "Item 2 Name is " + item2Name + "id is " + item2Id);
            Log.d("tag","Inspection Id is "+ShowAllAreas.ins_id+"Proeprty Id is"+ShowAllAreas.pro_id);


           int rows =  myDb.changeOrderItem1(pos1, item1Id, ShowAllAreas.ins_id, ShowAllAreas.pro_id, item1Name);
           int row =  myDb.changeOrderItem1(pos2, item2Id,  ShowAllAreas.ins_id , ShowAllAreas.pro_id ,item2Name);

            Log.d("tag","Rows Updated is "+rows);
            Log.d("tag","Rows Updated is "+row);
            mobileView.setVisibility(VISIBLE);
            ((BaseAdapter) getAdapter()).notifyDataSetChanged();


            mDownY = mLastEventY;

            final int switchViewStartTop = switchView.getTop();

            updateNeighborViewsForID(mMobileItemId);

            final ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @SuppressLint("NewApi")
                public boolean onPreDraw() {
                    observer.removeOnPreDrawListener(this);

                    View mobileView = getViewForID(mMobileItemId);
                    if (mobileView != null) mobileView.setVisibility(INVISIBLE);


                    View switchView = getViewForID(switchItemID);

                    mTotalOffset += deltaY;

                    int switchViewNewTop = switchView.getTop();
                    int delta = switchViewStartTop - switchViewNewTop;

                    switchView.setTranslationY(delta);

                    ObjectAnimator animator = ObjectAnimator.ofFloat(
                            switchView, View.TRANSLATION_Y, 0);
                    animator.setDuration(MOVE_DURATION);
                    animator.start();

                    return true;
                }
            });
        }
    }

    private void swapElements(ArrayList arrayList, int indexOne, int indexTwo) {
  //      Log.d("tag","Call Swap Element");
        Object temp = arrayList.get(indexOne);
        arrayList.set(indexOne, arrayList.get(indexTwo));
        arrayList.set(indexTwo, temp);
    }

    private void touchEventsEnded() {

        final View mobileView = getViewForID(mMobileItemId);


        if(mobileView != null) {
            int pos = getPositionForView(mobileView);

            Log.d("tag", "Touch End position is " + pos);
        }

        if (mCellIsMobile || mIsWaitingForScrollFinish) {
            mCellIsMobile = false;
            mIsWaitingForScrollFinish = false;
            mIsMobileScrolling = false;
            mActivePointerId = INVALID_POINTER_ID;

            if (mScrollState != OnScrollListener.SCROLL_STATE_IDLE) {
                mIsWaitingForScrollFinish = true;
                return;
            }

            mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left,
                    mobileView.getTop());

            ObjectAnimator hoverViewAnimator = ObjectAnimator.ofObject(
                    mHoverCell, "bounds", sBoundEvaluator,
                    mHoverCellCurrentBounds);
            hoverViewAnimator
                    .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(
                                ValueAnimator valueAnimator) {
                            invalidate();
                        }
                    });
            hoverViewAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setEnabled(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mAboveItemId = INVALID_ID;
                    mMobileItemId = INVALID_ID;
                    mBelowItemId = INVALID_ID;
                    mobileView.setVisibility(VISIBLE);
                    mHoverCell = null;
                    setEnabled(true);
                    invalidate();
                }
            });
            hoverViewAnimator.start();
        } else {
            touchEventsCancelled();
        }
    }

    private void touchEventsCancelled() {
        View mobileView = getViewForID(mMobileItemId);
        if (mCellIsMobile) {
            mAboveItemId = INVALID_ID;
            mMobileItemId = INVALID_ID;
            mBelowItemId = INVALID_ID;
            mobileView.setVisibility(VISIBLE);
            mHoverCell = null;
            invalidate();
        }
        mCellIsMobile = false;
        mIsMobileScrolling = false;
        mActivePointerId = INVALID_POINTER_ID;
    }

    private final static TypeEvaluator sBoundEvaluator = new TypeEvaluator() {
        public Rect evaluate(float fraction, Rect startValue, Rect endValue) {
            return new Rect(interpolate(startValue.left, endValue.left,
                    fraction), interpolate(startValue.top, endValue.top,
                    fraction), interpolate(startValue.right, endValue.right,
                    fraction), interpolate(startValue.bottom, endValue.bottom,
                    fraction));
        }

        public int interpolate(int start, int end, float fraction) {
            return (int) (start + fraction * (end - start));
        }

        @Override
        public Object evaluate(float arg0, Object arg1, Object arg2) {
            // TODO Auto-generated method stub
            return null;
        }
    };

    private void handleMobileCellScroll() {
        mIsMobileScrolling = handleMobileCellScroll(mHoverCellCurrentBounds);
    }

    public boolean handleMobileCellScroll(Rect r) {
        int offset = computeVerticalScrollOffset();
        int height = getHeight();
        int extent = computeVerticalScrollExtent();
        int range = computeVerticalScrollRange();
        int hoverViewTop = r.top;
        int hoverHeight = r.height();

        if (hoverViewTop <= 0 && offset > 0) {
            smoothScrollBy(-mSmoothScrollAmountAtEdge, 0);
            return true;
        }

        if (hoverViewTop + hoverHeight >= height && (offset + extent) < range) {
            smoothScrollBy(mSmoothScrollAmountAtEdge, 0);
            return true;
        }

        return false;
    }

    public void setCheeseList(ArrayList<Areas> cheeseList) {
        mCheeseList = cheeseList;
    }

    private AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {




        private int mPreviousFirstVisibleItem = -1;
        private int mPreviousVisibleItemCount = -1;
        private int mCurrentFirstVisibleItem;
        private int mCurrentVisibleItemCount;
        private int mCurrentScrollState;

        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

//            Log.d("tag","Call OnScroll");

            mCurrentFirstVisibleItem = firstVisibleItem;
            mCurrentVisibleItemCount = visibleItemCount;

            mPreviousFirstVisibleItem = (mPreviousFirstVisibleItem == -1) ? mCurrentFirstVisibleItem
                    : mPreviousFirstVisibleItem;
            mPreviousVisibleItemCount = (mPreviousVisibleItemCount == -1) ? mCurrentVisibleItemCount
                    : mPreviousVisibleItemCount;

            checkAndHandleFirstVisibleCellChange();
            checkAndHandleLastVisibleCellChange();

            mPreviousFirstVisibleItem = mCurrentFirstVisibleItem;
            mPreviousVisibleItemCount = mCurrentVisibleItemCount;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            mCurrentScrollState = scrollState;
            mScrollState = scrollState;
            isScrollCompleted();
        }

        private void isScrollCompleted() {
            if (mCurrentVisibleItemCount > 0
                    && mCurrentScrollState == SCROLL_STATE_IDLE) {
                if (mCellIsMobile && mIsMobileScrolling) {
                    handleMobileCellScroll();
                } else if (mIsWaitingForScrollFinish) {
                    touchEventsEnded();
                }
            }
        }

        public void checkAndHandleFirstVisibleCellChange() {
           // Log.d("tag","Call First vsisble cell change");
            if (mCurrentFirstVisibleItem != mPreviousFirstVisibleItem) {
                if (mCellIsMobile && mMobileItemId != INVALID_ID) {
                    updateNeighborViewsForID(mMobileItemId);
                    handleCellSwitch();
                }
            }
        }

        public void checkAndHandleLastVisibleCellChange() {
           // Log.d("tag","Call Last Visible Cell Change");
            int currentLastVisibleItem = mCurrentFirstVisibleItem
                    + mCurrentVisibleItemCount;
            int previousLastVisibleItem = mPreviousFirstVisibleItem
                    + mPreviousVisibleItemCount;
            if (currentLastVisibleItem != previousLastVisibleItem) {
                if (mCellIsMobile && mMobileItemId != INVALID_ID) {
                    updateNeighborViewsForID(mMobileItemId);
                    handleCellSwitch();
                }
            }
        }
    };
}