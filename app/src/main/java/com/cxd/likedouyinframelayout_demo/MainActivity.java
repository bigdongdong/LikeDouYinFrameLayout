package com.cxd.likedouyinframelayout_demo;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        findViewById(R.id.button).callOnClick();
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
            c.retainShadow = false ;
        }

        @Override
        protected void onCreateView(View view) {
            final int c15 = DensityUtil.dip2px(context,15);
            new GradientDrawableBuilder()
                    .color(0xFFFFFFFF)
                    .conners(new float[]{c15,c15,c15,c15,0,0,0,0})
                    .into(view);
            new GradientDrawableBuilder()
                    .color(0xFFFFFFFF)
                    .conners(new float[]{c15,c15,c15,c15,0,0,0,0})
                    .into(view.findViewById(R.id.headerTV));

            LikeDouYinFrameLayout lyfl = (LikeDouYinFrameLayout) view;

            lyfl.setOnCloseListener(new LikeDouYinFrameLayout.OnCloseListener() {
                @Override
                public void onClose() {
                    dismiss();
                }
            });

            TextView headerTV = view.findViewById(R.id.headerTV);
            headerTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"aaa",Toast.LENGTH_SHORT).show();
                }
            });

            /*nsv 内部item*/
            TextView textView1 = view.findViewById(R.id.textView1);
            if(textView1 != null){
                textView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"aaa",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        protected Object getLayoutOrView() {
            return R.layout.dialog_dou_yin_diy;
        }
    }
}