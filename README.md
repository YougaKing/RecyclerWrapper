# DragRecyclerView

#### 功能描述
* RecyclerWrapper 是一个零侵入性为RecyclerView提供加载更多、EmptyView的开源库,可搭配SwipeRefreshLayout 下拉刷新
* 支持EmptyView loading error 图片文字可自定义
* 支持LinearLayoutManager GridLayoutManager StaggeredGridLayoutManager
* 支持HORIZONTAL VERTICAL

#### Download
* Download the latest JAR or grab via Maven:
```xml
<dependency>
  <groupId>com.youga.recyclerwrapper</groupId>
  <artifactId>RecyclerWrapper</artifactId>
  <version>0.1</version>
  <type>pom</type>
</dependency>
```
* JitPack:
```
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}

	dependencies {
	        compile 'com.github.YougaKing:RecyclerWrapper:0.0.1'
	}
```

* or jcenter:
```
compile 'com.youga.recyclerwrapper:recyclerwrapper:0.0.1'
```
#### 使用方法
```

 RecyclerWrapper.with(mRecyclerView)
                //自定义FillView
               .fillView(new IFillViewProvider<View, String>() {
                                 @Override
                                 public View createView() {
                                     return LayoutInflater.from(mRecyclerView.getContext()).inflate(R.layout.fill_layout, mRecyclerView, false);
                                 }
             
                                 @Override
                                 public void bindView(View view, String string, @FillWrapper.Type int type) {
                                     TextView textView = (TextView) view.findViewById(R.id.text);
                                     switch (type) {
                                         case FillWrapper.LOAD://加载状态
                                             textView.setText("FillWrapper.LOAD:" + string);
                                             break;
                                         case FillWrapper.EMPTY://数据为空
                                             textView.setText("FillWrapper.EMPTY:" + string);
                                             break;
                                         case FillWrapper.ERROR://网络错误
                                             textView.setText("FillWrapper.ERROR:" + string);
                                             break;
                                     }
                                 }
                             })
                //自定义FootView
               .footView(new IFootViewProvider<View, String>() {
                                 @Override
                                 public View createView() {
                                     return LayoutInflater.from(mRecyclerView.getContext()).inflate(R.layout.nav_header_main, mRecyclerView, false);
                                 }
             
                                 @Override
                                 public void bindView(View view, String string, @FootWrapper.Type int type) {
                                     TextView textView = (TextView) view.findViewById(R.id.textView);
                                     switch (type) {
                                         case FootWrapper.F_LOAD://加载状态
                                             textView.setText("FootWrapper.F_LOAD:" + string);
                                             break;
                                         case FootWrapper.F_FAULT://网络错误
                                             textView.setText("FootWrapper.F_FAULT:" + string);
                                             break;
                                     }
                                 }
                             })
                 //加载更多回掉
                .wrapper(new LoadMoreListener() {
                    @Override
                    public void onLoadMore(int position) {
                        onDrag();
                    }
                })



设置全局FillView/FootView
RecyclerWrapper.registerFillViewProvider();
RecyclerWrapper.registerFootViewProvider();


显示各种状态
mRevealListener = RecyclerWrapper.with(mRecyclerView)
                .wrapper(new LoadMoreListener() {
                    @Override
                    public void onLoadMore(int position) {
                        onDrag();
                    }
                });

mRevealListener.showLoadView(null);
mRevealListener.showErrorView(null);
mRevealListener.showEmptyView(null)

数据是否还有更多
mRevealListener.haveMore(users.size() >= 5, null)


```


#### 版本
* versionCode 1
* versionName '0.0.1'

# 关于作者
* QQ交流群:158506055
* Email: YougaKingWu@gmail.com
