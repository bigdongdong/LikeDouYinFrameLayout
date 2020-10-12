package com.cxd.likedouyinframelayout_demo;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

/**
 * create by chenxiaodong on 2020/7/1
 * 快速生成背景
 */
public class GradientDrawableBuilder {
    private Integer conner ;
    private float[] conners ;
    private Integer color ;
    private int[] colors ;
    private GradientDrawable.Orientation orientation ;
    private Integer storkeColor ;
    private Integer storkeWidth ;

    /* ！！！ 只有view的size完全一致才可以复用GradientDrawable ！！！*/
    public GradientDrawable build(){
        GradientDrawable gd = new GradientDrawable();
        if(conner != null){
            gd.setCornerRadius(conner);
        }

        if(conners != null){
            gd.setCornerRadii(conners);
        }

        if(color != null){
            gd.setColor(color);
        }

        if(colors != null){
            gd.setColors(colors);
        }

        if(orientation != null){
            gd.setOrientation(orientation);
        }

        if(storkeWidth != null && storkeColor != null){
            gd.setStroke(storkeWidth,storkeColor);
        }

        return gd;
    }

    public void into(View view){
        GradientDrawable gd = new GradientDrawable();
        if(conner != null){
            gd.setCornerRadius(conner);
        }

        if(conners != null){
            gd.setCornerRadii(conners);
        }

        if(color != null){
            gd.setColor(color);
        }

        if(colors != null){
            gd.setColors(colors);
        }

        if(orientation != null){
            gd.setOrientation(orientation);
        }

        if(storkeWidth != null && storkeColor != null){
            gd.setStroke(storkeWidth,storkeColor);
        }

        view.setBackground(gd);
    }

    public GradientDrawableBuilder conner(int conner){
        this.conner = conner ;
        return this ;
    }

    public GradientDrawableBuilder conners(float[] conners){
        this.conners = conners ;
        return this ;
    }

    /**
     * 四个角的圆角，单位依然是px
     * @param c1  px
     * @param c2  px
     * @param c3  px
     * @param c4  px
     * @return
     */
    public GradientDrawableBuilder conners(float c1 , float c2 , float c3 , float c4){
        this.conners = new float[]{c1,c1,c2,c2,c3,c3,c4,c4};
        return this ;
    }

    public GradientDrawableBuilder color(int color){
        this.color = color ;
        return this ;
    }

    public GradientDrawableBuilder colors(int[] colors){
        this.colors = colors ;
        return this ;
    }

    public GradientDrawableBuilder storkeColor(int storkeColor){
        this.storkeColor = storkeColor ;
        return this ;
    }

    public GradientDrawableBuilder storkeWidth(int storkeWidth){
        this.storkeWidth = storkeWidth ;
        return this ;
    }

    public GradientDrawableBuilder orientation(GradientDrawable.Orientation orientation){
        this.orientation = orientation ;
        return this ;
    }
}
