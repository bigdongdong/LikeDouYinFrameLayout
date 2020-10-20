# LikeDouYinFrameLayout
类似抖音评论弹窗的根布局控件LikeDouYinFrameLayout

# 截图预览（Screen Recrod）
<img  width = "500" src = "https://github.com/bigdongdong/LikeDouYinFrameLayout/blob/master/preview/preview.gif"></img></br>


# 项目配置

```
  allprojects {
      repositories {
          ...
          maven { url 'https://jitpack.io' }  //添加jitpack仓库
      }
  }
  
  dependencies {
	  implementation 'com.github.bigdongdong:LikeDouYinFrameLayout:${lastest_version}' //添加依赖
	  
  }
```

# 使用方式
## xml文件
```xml
<com.cxd.likedouyinframelayout.LikeDouYinFrameLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"            
    android:id="@+id/ldyfl"
    >
  
  <!--内容布局部分-->
  .............
  
</com.cxd.likedouyinframelayout.LikeDouYinFrameLayout>
```

## java文件
```java
LikeDouYinFrameLayout ldyfl = view.findViewById(R.id.ldyfl);
ldyfl.setOnCloseListener(new LikeDouYinFrameLayout.OnCloseListener() {
    @Override
    public void onClose() {
        //在这里dismiss当前弹窗
        dialog.dismiss();
    }
});
```
