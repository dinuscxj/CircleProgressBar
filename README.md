
## Android CircleProgressBar 
The CircleProgressBar extends ProgressBar, It has both solid and line two styles. Besides, progress value can be freely customized.
If you are interested in cool loading animations, you can see [LoadingDrawable](https://github.com/dinuscxj/LoadingDrawable).

![](https://raw.githubusercontent.com/dinuscxj/CircleProgressBar/master/Preview/CircleProgressBar.gif?width=300)

### Usage

#### Gradle
 ```gradle
 dependencies {
    compile 'com.dinuscxj:circleprogressbar:1.0.0'
 }
 ```

#### Used in xml:

```xml
<com.dinuscxj.progressbar.CircleProgressBar
	android:id="@+id/line_progress"
	android:layout_marginTop="@dimen/default_margin"
	android:layout_width="50dp"
	android:layout_height="50dp" />
```

### Attributes
There are several attributes you can set:

The **style**:

* solid
* line

The **progress text**:

* text color
* text size
* visibility
* format

The **progress circle**:

* width
* color
* background color

The **line style**:

* width
* count

for example :
```xml
<com.dinuscxj.progressbar.CircleProgressBar
	android:layout_width="50dp"
	android:layout_height="50dp"

	app:style="line"

	app:progress_text_color="@color/holo_purple"
	app:progress_text_size="@dimen/progress_text_size"
	app:draw_progress_text="true"
	app:progress_text_format_pattern="@string/progress_text_format_pattern"

	app:progress_stroke_width="1dp"
	app:progress_color="@color/holo_purple"
	app:progress_background_color="@color/holo_darker_gray"

	app:line_width="4dp"
	app:line_count="30"/>
```

### About me
I like Google, like Android, like open source, like doing something interesting. :)
If you like LoadingDrawable or use it, you can star this repo and send me some feedback. Thanks! ~_~

### License
    Copyright 2015-2019 dinuscxj

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
