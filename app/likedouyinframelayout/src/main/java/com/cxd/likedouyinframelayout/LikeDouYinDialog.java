package com.cxd.likedouyinframelayout;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * 2020/9/27
 * 仿抖音评论弹窗
 */
public abstract class LikeDouYinDialog extends BottomSheetDialogFragment {

    protected AppCompatActivity context ;

    public LikeDouYinDialog(AppCompatActivity context) {
        this.context = context;
    }

    protected abstract void onConfig(Config c);
    protected abstract void onCreateView(View view);
    protected abstract Object getLayoutIdOrView();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Object o = getLayoutIdOrView();
        View view = null ;
        if(o != null){
            if(o instanceof Integer){
                view = LayoutInflater.from(context).inflate(((Integer)o),null);
            }else if(o instanceof View){
                view = (View) o;
            }
        }
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new InnerDialog(getActivity()) {
            @Override
            protected void onConfig(Config c) {
                LikeDouYinDialog.this.onConfig(c);
            }

            @Override
            protected void onCreateView(View view) {
                LikeDouYinDialog.this.onCreateView(view);
            }

            @Override
            protected Object getLayoutIdOrView() {
                return LikeDouYinDialog.this.getLayoutIdOrView();
            }
        };
    }

    public void show(){
        if(context != null && !context.isFinishing()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && context.isDestroyed()) {
                return;
            }
            show(context.getSupportFragmentManager(),null);
        }
    }

    /*内置dialog*/
    private abstract class InnerDialog extends BottomSheetDialog {
        protected Activity context ;
        private Config c ;
        private View view ;
        public InnerDialog(@NonNull Activity context) {
            super(context);

            this.context = context ;
            c = new Config();

            //设置不显示对话框标题
            requestWindowFeature(Window.FEATURE_NO_TITLE);

        }

        protected abstract void onConfig(Config c);
        protected abstract void onCreateView(View view);
        protected abstract Object getLayoutIdOrView();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            onConfig(c); //配置

            Window win = this.getWindow();
            win.setGravity(c.gravity);   // 这里控制弹出的位置
            win.getDecorView().setPadding(0, 0, 0, 0);
            if(!c.retainShadow){ //是否保留阴影
                win.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); //不保留阴影
            }
            if(c.style != null){
                win.setWindowAnimations(c.style);
            }
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = c.width;
            lp.height = c.height;
            win.setAttributes(lp);
            win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            Object o = getLayoutIdOrView();
            view = null ;
            if(o != null){
                if(o instanceof Integer){
                    view = LayoutInflater.from(context).inflate(((Integer)o),null);
                }else if(o instanceof View){
                    view = (View) o;
                }
            }

            this.setContentView(view);
            this.setCancelable(c.cancelAble);
            this.setCanceledOnTouchOutside(c.canceledOnTouchOutside);

            onCreateView(view);
        }

        @Override
        public void onStart() {
            super.onStart();
            if(view != null){
                //固定高度
                View parent = (View) view.getParent();
                BottomSheetBehavior.from(parent).setPeekHeight(c.height);
            }
        }
    }

    /**
     * 配置清单
     */
    public class Config {
        /*是否保留阴影，true-保留 false-不保留*/
        public boolean retainShadow = true ;
        /*弹窗宽度 -1：match_parent -2:wrap_content*/
        public int width = -2 ;
        /*弹窗高度 -1：match_parent -2:wrap_content*/
        public int height = -2 ;
        /*弹窗布局位置*/
        public int gravity = Gravity.CENTER ;
        /*弹窗弹出动画*/
        public Integer style = null ;

        /**
         * cancelAble:
         * dialog弹出后会点击屏幕或物理返回键，dialog是否消失
         *
         * canceledOnTouchOutside:
         * dialog弹出后会点击屏幕，dialog是否消失
         *
         * dialog点击物理返回键都消失，建议在onBackPressed()中进行拦截
         */
        public boolean cancelAble = true ;
        public boolean canceledOnTouchOutside = true ;
    }
}
