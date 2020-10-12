package com.cxd.likedouyinframelayout_demo;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseDialog extends Dialog {
    protected Activity context ;
    private Config c ;
    public BaseDialog(@NonNull Activity context) {
        super(context);

        this.context = context ;
        c = new Config();

        //设置不显示对话框标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    protected abstract void onConfig(Config c);
    protected abstract void onCreateView(View view);
    protected abstract Object getLayoutOrView();


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

        Object o = getLayoutOrView();
        View view = null ;
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

    /**
     * 配置清单
     */
    public class Config {
        public boolean retainShadow = true ; //是否保留阴影，默认保留
        public int width = -2 ;
        public int height = -2 ;
        public int gravity = Gravity.CENTER ;
        public Integer style = null ;
        public boolean cancelAble = true ;
        public boolean canceledOnTouchOutside = true ;
    }
}
