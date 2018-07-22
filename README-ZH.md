
## CircleProgressBar
[CircleProgressBar](https://github.com/dinuscxj/CircleProgressBar)继承View, 是包含实心和线条两种风格的圆环进度条. 此外, 进度值可以随意定制.
如果你对酷炫的进度条比较感兴趣, 或许你更喜欢 [LoadingDrawable](https://github.com/dinuscxj/LoadingDrawable).

![](https://raw.githubusercontent.com/dinuscxj/CircleProgressBar/master/Preview/CircleProgressBar.gif?width=300)

### 用法

#### Gradle
 ```gradle
 dependencies {
    compile 'com.dinuscxj:circleprogressbar:1.3.0'
 }
 ```

#### 用在xml中:

```java
<com.dinuscxj.progressbar.CircleProgressBar
	android:id="@+id/line_progress"
	android:layout_marginTop="@dimen/default_margin"
	android:layout_width="50dp"
	android:layout_height="50dp" />
```

### 属性
有下面这些属性你可以设置:

The **style**:

* line (默认)
* solid_line
* solid

The **background color**

* background_color

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

* linear (默认，如果start_color和end_color相同则不会设置shader)
* radial
* sweep

The **progress_stroke_cap**

* butt (默认)
* round
* square

The **line style**:

* width
* count

例如 :
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

格式化 progress
``` java
     private static final class MyProgressFormatter implements ProgressFormatter {
         private static final String DEFAULT_PATTERN = "%d%%";
 
         @Override
         public CharSequence format(int progress, int max) {
             return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max * 100));
         }
     }
     
     // 设置你想要的ProgressFormatter
     CircleProgressBar.setProgressFormatter(new MyProgressFromatter);
     // 隐藏进度的显示
     CircleProgressBar.setProgressFormatter(null);
     
```
### 优点
1. 继承View， 不必关心当前进度状态的保存， CircleProgressBar 已经在onSaveInstanceState（）和 onRestoreInstanceState(Parcelable state)中写好了。
2. 定制性很强，可以设置两种风格的进度条，设置进度条的颜色和进度文本的颜色和大小， 由于代码中对于进度文本的格化化是使用的String.format(), 所以进度文本可以根据需要随意定制
3. 代码优雅，代码注释很全面，格式整齐，可以直接在xml中设置相关的属性。

### 关于我
本人喜欢android、开源而且比较喜欢做一些有意思的东西 :)
如果你喜欢CircleProgressBar或者正在使用它，欢迎star这个项目， 并且希望反馈给我一些建议. 谢谢! ~_~
 ***QQ 群:*** **342748245**

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
