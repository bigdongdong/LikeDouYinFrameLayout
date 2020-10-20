package com.cxd.likedouyinframelayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * create by cxd on YIYUN
 *
 * 2020/9/30
 * 实现抖音评论弹窗的parent
 * 可以用于各种情况
 * 唯一缺陷：先下拉后上提，contentView底部被裁剪，
 *      而且手指越过top会触发dialog的dismiss事件
 *      
 * 2020/10/12
 * 优化了细节，解决了9/30所述的问题
 *      但仍然和抖音评论弹窗实现有细微差别
 *      要百分百实现抖音评论弹窗，推荐使用
 *      {@link BottomSheetDialogFragment}
 *      + {@link BottomSheetDialog}
 *
 * 修复了点击事件不传递的bug
 *
 * 2020/10/16
 * 优化了寻找可滚动child的算法
 * 增加了关闭的动画时长，以及减慢动画变化速率
 */
public class LikeDouYinFrameLayout extends FrameLayout {
    private final String TAG = "LikeDouYinParentLayout";
    private final float DEFAULT_CLOSE_POSITION_RATIO = 0.35f ; //默认关闭位置坐标比例
    private final int DEFAULT_SPRING_BACK_DURATION = 200 ; //默认回弹时间
    private final int DEFAULT_CLOSE_DURATION = 400 ; //默认关闭时间

    //时间极短的滑动手势时间临界值
    private final int DEFAULT_MAX_CLOSE_TIMESTAMP = 100 ;
    private VelocityTracker velocityTracker ;
    private View innerScrollView ;
    private boolean isFirstTimeLayout = true ;

    private ValueAnimator mAnimator; //回弹和关闭的属性动画
    private OnCloseListener onCloseListener ;

    public LikeDouYinFrameLayout(@NonNull Context context) {
        this(context,null);
    }

    public LikeDouYinFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(isFirstTimeLayout){
            findFirstSupportScrollChild(this);
            isFirstTimeLayout = false ;
        }
    }

    private float mEventDownY ;
    private float mLastEventMoveY ;
    private long mEventDownTimestamp ;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(!isContentViewReachedTheTop()){
            return false;
        }
        final int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastEventMoveY = mEventDownY = event.getY();
                mEventDownTimestamp = System.currentTimeMillis();
                return false;
            case MotionEvent.ACTION_MOVE:
                final float curEventMoveY = event.getY();
                final float moveDy = curEventMoveY - mLastEventMoveY ;
                mLastEventMoveY = curEventMoveY ;

                if(moveDy > 0 && isContentViewReachedTheTop()){
                    return true ;
                }else if(moveDy < 0 && getTop() > 0){
                    return true ;
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(getTop() > 0 && isContentViewReachedTheTop()){
                    return true ;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_MOVE:
                final float moveDy = event.getY() - mEventDownY;
                initVelocityTrackerIfNotExists();
                velocityTracker.addMovement(event);
                final int top = (int) Math.max(0,getTop() + moveDy);
                layout(0, top,getMeasuredWidth(),getMeasuredHeight());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                initVelocityTrackerIfNotExists();
                velocityTracker.computeCurrentVelocity(1000);
                Log.i(TAG, "onTouchEvent: "+(System.currentTimeMillis() - mEventDownTimestamp));
                if(getTop() > getMeasuredHeight() * DEFAULT_CLOSE_POSITION_RATIO){
                    //关闭
                    closeOnAnimation();
                    velocityTracker.clear();
                }else if(System.currentTimeMillis() - mEventDownTimestamp < DEFAULT_MAX_CLOSE_TIMESTAMP){
                    //关闭
                    closeOnAnimation();
                    velocityTracker.clear();
                }else{
                    //回弹
                    springBackOnAnimation();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void findFirstSupportScrollChild(ViewGroup parent){
        final int childCount = parent.getChildCount();
        if(childCount > 0){
            //第一遍筛选符合条件的
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                if(child instanceof ScrollView || child instanceof NestedScrollView
                        || child instanceof RecyclerView || child instanceof WebView){
                    child.setOverScrollMode(View.OVER_SCROLL_NEVER);
                    child.setVerticalScrollBarEnabled(false);
                    if(innerScrollView == null){
                        innerScrollView = child;
                    }
                    return;
                }
            }

            //第二遍筛选下一级
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                if(child instanceof ViewGroup){
                    findFirstSupportScrollChild((ViewGroup) child);
                }
            }
        }
    }

    private void initVelocityTrackerIfNotExists(){
        if(velocityTracker == null){
            velocityTracker = VelocityTracker.obtain();
        }
    }

    private void initAnimatorIfNotExists(){
        if(mAnimator == null){
            mAnimator = new ValueAnimator();
            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setEnabled(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setEnabled(true);
                    if(onCloseListener != null && getTop() == getMeasuredHeight()){
                        onCloseListener.onClose();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    setEnabled(true);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    setEnabled(false);
                }
            });
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    layout(0,(int)animation.getAnimatedValue(),getMeasuredWidth(),getMeasuredHeight());
                }
            });
        }
    }

    /**
     * 是否contentview已经滑动到了顶部
     * @return
     */
    private boolean isContentViewReachedTheTop(){
        return innerScrollView != null && !innerScrollView.canScrollVertically(-1);
    }

    private void springBackOnAnimation(){
        initAnimatorIfNotExists();
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.setDuration(DEFAULT_SPRING_BACK_DURATION);
        mAnimator.setIntValues(getTop(),0);
        mAnimator.start();
    }

    private void closeOnAnimation(){
        initAnimatorIfNotExists();
        mAnimator.setInterpolator(new DecelerateInterpolator(0.9f));
        mAnimator.setDuration(DEFAULT_CLOSE_DURATION);
        mAnimator.setIntValues(getTop(),getMeasuredHeight());
        mAnimator.start();
    }

    /********************** api ***********************/
    public void setOnCloseListener(OnCloseListener listener){
        this.onCloseListener = listener;
    }

    public interface OnCloseListener{
        void onClose();
    }
}
