# DragRecyclerView


![主页](https://github.com/YougaKing/DragRecyclerView/blob/master/Res/grid.png)
![主页](https://github.com/YougaKing/DragRecyclerView/blob/master/Res/stage_grid.png)
![主页](https://github.com/YougaKing/DragRecyclerView/blob/master/Res/haha.gif)

Demo演示 [下载](https://github.com/YougaKing/DragRecyclerView/blob/master/Res/app-release.apk)

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
* or Gradle:
```
compile 'com.youga.recyclerwrapper:RecyclerWrapper:0.1'
```
#### 代码片段
```
 if (id == R.id.lm_hor) {
     mAdapter = new RecyclerViewAdapter(this, null);
     mDragRecyclerView.setAdapter(mAdapter, true, new LinearLayoutManager(this,
             LinearLayoutManager.HORIZONTAL, false));
 } else if (id == R.id.lm_ver) {
     mAdapter = new RecyclerViewAdapter(this, null);
     mDragRecyclerView.setAdapter(mAdapter, true, new LinearLayoutManager(this,
             LinearLayoutManager.VERTICAL, false));
 } else if (id == R.id.glm_hor) {
     mAdapter = new StaggeredGridAdapter(this, null, false);
     mDragRecyclerView.setAdapter(mAdapter, true, new GridLayoutManager(this, 4,
             GridLayoutManager.HORIZONTAL, false));
 } else if (id == R.id.glm_ver) {
     mAdapter = new StaggeredGridAdapter(this, null, false);
     mDragRecyclerView.setAdapter(mAdapter, true, new GridLayoutManager(this, 4,
             GridLayoutManager.VERTICAL, false));
 } else if (id == R.id.sglm_hor) {
     mAdapter = new StaggeredGridAdapter(this, null, true);
     mDragRecyclerView.setAdapter(mAdapter, true,
             new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL));
 } else if (id == R.id.sglm_ver) {
     mAdapter = new StaggeredGridAdapter(this, null, true);
     mDragRecyclerView.setAdapter(mAdapter, true,
             new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
 }

```


#### 功能
* RecyclerView 加载更多,可搭配SwipeRefreshLayout 下拉刷新
* 支持EmptyView loading error 图片文字可自定义
* 支持LinearLayoutManager GridLayoutManager StaggeredGridLayoutManager
* 支持HORIZONTAL VERTICAL

#### 版本
* gradlew bintrayupload
* versionCode 4
* versionName '1.1.3'

# 关于作者
* QQ交流群:158506055
* Email: YougaKingWu@gmail.com
