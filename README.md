
## CircleProgressBar
[中文版文档](https://github.com/dinuscxj/CircleProgressBar/blob/master/README-ZH.md)&nbsp;&nbsp;&nbsp;

The [CircleProgressBar](https://github.com/dinuscxj/CircleProgressBar) extends View, It has both solid and line two styles. Besides, progress value can be freely customized.
If you are interested in cool loading animations, you can see [LoadingDrawable](https://github.com/dinuscxj/LoadingDrawable).

![](https://raw.githubusercontent.com/dinuscxj/CircleProgressBar/master/Preview/CircleProgressBar.gif?width=300)

### Usage

#### Gradle
 ```gradle
 dependencies {
    compile 'com.dinuscxj:circleprogressbar:1.3.0'
 }
 ```

#### Used in xml:

```java
<com.dinuscxj.progressbar.CircleProgressBar
	android:id="@+id/line_progress"
	android:layout_marginTop="@dimen/default_margin"
	android:layout_width="50dp"
	android:layout_height="50dp" />
```

### Attributes
There are several attributes you can set:

The **style**:

* line （default）
* solid_line
* solid

The **progress text**:

* text color
* text size
* visibility
* format

The **progress circle**:

* width
* start degree
* start color
* end color
* background color
* draw background outside progress

The **progress_shader**

* linear (default，but if start_color and end_color equivalence, will not set the shader)
* radial
* sweep

The **progress_stroke_cap**

* butt (default)
* round
* square

The **line style**:

* width
* count

for example :
In the xml

```java
<com.dinuscxj.progressbar.CircleProgressBar
	android:layout_width="50dp"
	android:layout_height="50dp"

	app:style="line"
	
	app:background_color="@color/holo_red_light"

	app:progress_text_color="@color/holo_purple"
	app:progress_text_size="@dimen/progress_text_size"

	app:progress_stroke_width="1dp"
	app:progress_start_color="@color/holo_purple"
	app:progress_end_color="@color/holo_green_light"
	app:progress_background_color="@color/holo_darker_gray"
	app:progress_start_degree="-90"
	app:drawBackgroundOutsideProgress="true"

	app:progress_shader="sweep"
    
	app:progress_stroke_cap="round"

	app:line_width="4dp"
	app:line_count="30"/>
```

format progress
``` java
     private static final class MyProgressFormatter implements ProgressFormatter {
         private static final String DEFAULT_PATTERN = "%d%%";
 
         @Override
         public CharSequence format(int progress, int max) {
             return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max * 100));
         }
     }
     
     // set the ProgressFormatter as you want
     CircleProgressBar.setProgressFormatter(new MyProgressFromatter);
     // hide the progress
     CircleProgressBar.setProgressFormatter(null);
     
```
### Advantages
1. extends View, do not care to save the current state of progress, CircleProgressBar has written in to help us in onSaveInstanceState () and onRestoreInstanceState (Parcelable state).
2. highly customizable, you can set the two styles of progress bar, you can set the color of the bars, you can set the color and size of the progress of the text, due to the progress of the code for formatting text uses String.format (), so progress text can be customized as needed
3. Code elegant, very comprehensive code comments, neat format, you can set the relevant properties directly in xml.

### About me
I like Android, like open source, and like doing something interesting. :)
If you like CircleProgressBar or use it, you can star this repo and send me some feedback. Thanks! ~_~
 ***QQ Group:*** **342748245**

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
