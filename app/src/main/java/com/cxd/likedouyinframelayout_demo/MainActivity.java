package com.cxd.likedouyinframelayout_demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import com.cxd.likedouyinframelayout.LikeDouYinFrameLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyDiyDialog(MainActivity.this).show();
            }
        });
    }

    public static class MyDiyDialog extends BaseDialog{

        public MyDiyDialog(@NonNull Activity context) {
            super(context);
        }

        @Override
        protected void onConfig(Config c) {
            c.gravity = Gravity.BOTTOM ;
            c.width = -1;
            c.height = DensityUtil.dip2px(context,600);
        }

        @Override
        protected void onCreateView(View view) {
            final int c15 = DensityUtil.dip2px(context,15);
            new GradientDrawableBuilder()
                    .color(Color.WHITE)
                    .conners(new float[]{c15,c15,c15,c15,0,0,0,0})
                    .into(view);

            ((LikeDouYinFrameLayout)view).setOnCloseListener(new LikeDouYinFrameLayout.OnCloseListener() {
                @Override
                public void onClose() {
                    dismiss();
                }
            });
        }

        @Override
        protected Object getLayoutOrView() {
            return R.layout.dialog_dou_yin_diy;
        }
    }
}