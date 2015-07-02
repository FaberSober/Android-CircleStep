# Android-CircleStep
A simple view displays several steps by circle.
<br><br><br>
![alt tag](https://github.com/FaberSober/Android-CircleStep/blob/master/art/circlestepview.gif)
## Usage
```xml
dependencies {
    compile 'com.faber.circlestepview:library:1.0.0@aar'
}
```
```xml
<com.faber.circlestepview.CircleStepView
        android:id="@+id/circle_step_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="16dp"
        app:circlestep_circle_count="3"
        app:circlestep_path_color="@color/light_green"
        app:circlestep_current_inner_circle_color="@color/light_blue"
        app:circlestep_current_outer_circle_color="@color/light_green"
        app:circlestep_circle_color="@color/light_gray"
        app:circlestep_circle_text_size="14sp"
        app:circlestep_text_below_circle_size="14sp"
        app:circlestep_text_below_circle_color="@color/text_dark_grey"/>
```

Set the circle indicate text like this
```java
CircleStepView mCircleStepView = ((CircleStepView) findViewById(R.id.circle_step_view));
mCircleStepView.setTextBelowCircle("Step 1", "Step 2", "Step 3");
```
