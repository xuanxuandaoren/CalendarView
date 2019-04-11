# CalendarView
自定义的日历控件，可以根据自己需求定制
## 自定义日历控件，可以根据需求定制属于自己的日历
---
### 使用引导

### 如何更改成自己需要的样式。
由于很多人还是问我日历自定义控件怎么修改，我觉得我的日历是很容易修改的，所以在前面特意了强调一下

由于我使用的面向对象思想写的 ，把每一天看成一个对象，所以画整个日历就变成了画一天。DayManager会自动控制去创建Day对象调用Day 的drawDays 方法，在创建的时候会把

1. width 单个对象的宽度
2. height 单个对象的高度
3. location_x 对象除于第几行
4. location_y 对象处于第几列 
5. public String text 日期的文本 ，即第几天

给初始化。我们可以根绝以上的信息就可以计算出画一天的的区域，然后根据自己的需要在这个区域画出自己需要的东西。即修改drawDays方法就可以。
### 效果图
最近笔者的朋友需要写一个关于考勤的日历，效果如下![笔者朋友的预览图](http://img.blog.csdn.net/20160804102049628)，但在网上找了半天都找不到合适的，于是乎就向笔者求助，笔者本来觉得在晚上随便找个日历demo，然后随便的修改几下就可以完成，于是顺口答应，找来几个发现代码修改起来都比较麻烦，于是乎就想为何不自己写一个可以修改起来很方便的日历控件呢 ？那岂不是以后用起来也很方便么 ？于是乎笔者就动手写了一个日记控件，运行起来效果图如下![笔者自己写的控件效果](http://img.blog.csdn.net/20160804102116386)，[demo下载地址](https://github.com/xuanxuandaoren/CalendarView)看完效果图我们就来谈一下笔者自己写自定义控件的思路
### 自定义控件思路
笔者在写自定义控件的时候一般采用MVC模式，即是模型(model)－视图(view)－控制器(controller)，首先我们分析一下model
#### 建立模型
建立模型之前，首先我们分析一下日历控件，以便于建立模型，先看图
![示例图片](http://img.blog.csdn.net/20160804102647066)
我们把一个方框看做一个对象，要确定这个对象我们需要确定那些关键属性呢 ？

 1. 我们要知道这个对象在第几行第几列
 2. 我们要知道每个对象的宽和高，这样我们才可以确定一个对象的位置
 3. 这个对象包含的文字
 4. 其他还有背景类型，打卡类型等附加的属性，这样，一个对象就可以确定下来了。
 好了，那我们开始上代码，首先建立一个day的类
 

```
/**
 * 日期的类
 * Created by xiaozhu on 2016/8/7.
 */
public class Day {
    /**
     * 单个日期格子的宽
     */
    public int width;
    /**
     * 单个日期格子的高
     */
    public int height;
    /**
     * 日期的文本
     */
    public String text;
    /**
     * 文本字体的颜色
     */
    public int textClor;
    /**
     * 日期背景的类型 0代表无任何背景，1代表正常打卡，2代表所选日期，3代表当前日期 4,代表即是当前日期，也被选中
     */
    public int backgroundStyle;
    /**
     * 字体的大小
     */
    public float textSize;
    /**
     * 背景的半径
     */
    public int backgroundR;
    /**
     * 出勤的类型 0为不画，1为正常考勤，2为异常，3为出差外出灯
     */
    public int workState;
    /**
     * 出勤状态的半径
     */
    public int workStateR = 5;
    /**
     * 字体在第几行
     */
    public int location_x;
    /**
     * 字体在第几列
     */
    public int location_y;

    /**
     * 创建日期对象
     * @param width 每个日期格子的宽度
     * @param height 每个日期格子的高度
     */
    public Day(int width, int height) {
        this.width = width;
        this.height = height;
    }
```
定义完我们所需要的属性之后再给day添加一个可以画自己的方法，这样一个完整的模型才算完成，画自己的方法如下

```
 /**
  * 画天数
  *
  * @param canvas  要画的画布
  * @param paint   画笔
  * @param context 画布的上下文对象
  */
    public void drawDays(Canvas canvas, Context context, Paint paint) {
        //取窄的边框为圆的半径
        backgroundR = width > height ? height : width;
        //画背景
        drawBackground(canvas, paint);

        //画数字
        drawTaxt(canvas, paint);

        //画考勤
        drawWorkState(canvas, paint);


    }

    /**
     * 画考勤
     *
     * @param canvas
     * @param paint
     */
    private void drawWorkState(Canvas canvas, Paint paint) {
        //确定圆心位置
        float cx = location_x * width + width / 2;
        float xy = location_y * height + height * 44 / 60;
        paint.setStyle(Paint.Style.FILL);
        //根据工作状态设置画笔颜色
        if (workState == 0) {
            return;
        }
        switch (workState) {
            case 1:
                paint.setColor(0xFF46CC6E);
                break;
            case 2:
                paint.setColor(0xFFF16269);
                break;
            case 3:
                paint.setColor(0xFF3E81ED);
                break;

        }
        canvas.drawCircle(cx, xy, workStateR, paint);
    }

    /**
     * 花数字
     *
     * @param canvas
     * @param paint
     */
    private void drawTaxt(Canvas canvas, Paint paint) {
        //根据圆的半径设置字体的大小
        textSize = backgroundR / 3;
        paint.setTextSize(textSize);
        paint.setColor(textClor);
        paint.setStyle(Paint.Style.FILL);
        //计算文字的宽度
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        int w = rect.width();
        //计算画文字的位置
        float x = location_x * width + (width - w) / 2;
        float y = location_y * height + (height + textSize/2) / 2;
        canvas.drawText(text, x, y, paint);
    }

    /**
     * 画背景
     *
     * @param canvas
     * @param paint
     */
    private void drawBackground(Canvas canvas, Paint paint) {
        //画背景 根据背景状态设置画笔类型
        if (backgroundStyle == 0) {
            return;
        }
        switch (backgroundStyle) {
            case 1:
                paint.setColor(0xFFECF1F4);
                paint.setStyle(Paint.Style.FILL);
                break;
            case 2:
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(0xFF457BF4);
                break;
            case 3:
                paint.setColor(0xFF457BF4);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
                break;
        }
        //计算圆心的位置
        float cx = location_x * width + width / 2;
        float cy = location_y * height + height / 2;
        canvas.drawCircle(cx, cy, backgroundR * 9 / 20, paint);

    }
```
这样，一个完整的模型就建立完毕了
#### 建立控制类
我们现在建立一个控制的类，即DayManager类，并声明几个关键的参数

```
/**
 * 日期的管理类
 * Created by xiaozhu on 2016/8/7.
 */
public class DayManager {
    /**
     * 记录当前的时间
     */
    public static  String currentTime;

    /**
     * 当前的日期
     */
    private static int current = -1;
    /**
     * 储存当前的日期
     */
    private static int tempcurrent=-1;
    /**
     *
     */
    static String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
    static String[] dayArray = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
```
下面就是日起管理类的关键代码，即创建日期集合，并赋值的过程

```
  /**
     * 根据日历对象创建日期集合
     *
     * @param calendar 根据日历生成月份数据，改变这个就可以改变月数
     * @param width 控件的宽度
     * @param heigh 控件的高度
     * @return 返回的天数的集合
     */
    public static List<Day> createDayByCalendar(Calendar calendar, int width, int heigh) {
        //初始化休息的天数
        initRestDays(calendar);
        //模拟数据 ，无关紧要，
        imitateData();

        List<Day> days = new ArrayList<>();


        Day day = null;


        int dayWidth = width / 7;
        int dayHeight = heigh / (calendar.getActualMaximum(Calendar.WEEK_OF_MONTH) + 1);
        //添加星期对象，里面储存的分别是，日、一、二、三、四、五、六，
        for (int i = 0; i < 7; i++) {
            day = new Day(dayWidth, dayHeight);
            //为星期设置位置，为第0行，
            day.location_x = i;
            day.location_y = 0;
            day.text = weeks[i];
            //设置日期颜色
            day.textClor = 0xFF699CF0;
            days.add(day);

        }
        int count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstWeekCount = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
       //生成每一天的对象，其中第i次创建的是第i+1天
        for (int i = 0; i < count; i++) {
            day = new Day(dayWidth, dayHeight);
            day.text = dayArray[i];

            calendar.set(Calendar.DAY_OF_MONTH, i + 1);
            //设置每个天数的位置，即在表格的第几行第几列
            day.location_y = calendar.get(Calendar.WEEK_OF_MONTH);
            day.location_x = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            //设置日期选择状态
            if (i == current - 1) {
                day.backgroundStyle = 3;
                day.textClor = 0xFF4384ED;

            } else if (i == select - 1) {
                day.backgroundStyle = 2;

                day.textClor = 0xFFFAFBFE;

            } else {
                day.backgroundStyle = 1;
                day.textClor = 0xFF8696A5;
            }
            //设置工作状态
            if (restDays.contains(1 + i)) {
                day.workState = 0;
            } else if (abnormalDays.contains(i + 1)) {

                day.workState = 2;
            } else if (outDays.contains(i + 1)) {
                day.workState = 3;
            } else {
                day.workState = 1;
            }
            days.add(day);
        }

        return days;
    }
     /**
     * 模拟数据
     */
    private static void imitateData() {
        abnormalDays.add(2);
        abnormalDays.add(11);
        abnormalDays.add(16);
        abnormalDays.add(17);
        abnormalDays.add(23);

        outDays.add(8);
        outDays.add(9);
        outDays.add(18);
        outDays.add(22);

    }

    /**
     * 初始化休息的天数  计算休息的天数
     *
     * @param calendar
     */
    private static void initRestDays(Calendar calendar) {
        int total = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < total; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i + 1);
            if (calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7) {
                restDays.add(i + 1);
            }
        }
    }
```
好了，一个日期控制类就完成了
#### 建立view类，对日期进行显示
我们现在建立一个CalendarView类，并让他继承View类，并重写onDraw方法。

```
/**
 * 自定义的日历控件
 * Created by xiaozhu on 2016/8/1.
 */
public class CalendarView extends View {

    private Context context;
    /**
     * 画笔
     */
    private Paint paint;
    /***
     * 当前的时间
     */
    private Calendar calendar;
      @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取day集合并绘制
        List<Day> days = DayManager.createDayByCalendar(calendar, getMeasuredWidth(), getMeasuredHeight());
        for (Day day : days) {
            day.drawDays(canvas, context, paint);
        }

    }
```
我们在onDraw里面调用DayManager的createDayByCalendar方法，并把当前控件显示的日历和控件的宽高传进去，获取日期的集合，遍历集合，调用对象的drawDays方法，把画笔和画布穿进去，一个完整的日历就画出来了。最后我们对他进行完善，即添加点击响应事件。下面我们重写onTouchEvent方法

```
 @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            //判断点击的是哪个日期
            float x = event.getX();
            float y = event.getY();
            //计算点击的是哪个日期
            int locationX = (int) (x * 7 / getMeasuredWidth());
            int locationY = (int) ((calendar.getActualMaximum(Calendar.WEEK_OF_MONTH) + 1) * y / getMeasuredHeight());
            if (locationY == 0) {
                return super.onTouchEvent(event);
            } else if (locationY == 1) {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                System.out.println("xiaozhu" + calendar.get(Calendar.DAY_OF_WEEK) + ":" + locationX);
                if (locationX < calendar.get(Calendar.DAY_OF_WEEK) - 1) {
                    return super.onTouchEvent(event);
                }
            } else if (locationY == calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)) {
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                if (locationX > calendar.get(Calendar.DAY_OF_WEEK) + 1) {
                    return super.onTouchEvent(event);
                }
            }
            calendar.set(Calendar.WEEK_OF_MONTH, (int) locationY);
            calendar.set(Calendar.DAY_OF_WEEK, (int) (locationX + 1));
            DayManager.setSelect(calendar.get(Calendar.DAY_OF_MONTH));
            //添加点击监听
            if (listener!=null){
                listener.selectChange(this,calendar.getTime());
            }
            invalidate();

        }
        return super.onTouchEvent(event);
    }
```
好了，一个完整的日历控件就画完了。
#### 在activity里面使用
创建布局文件，在布局文件添加控件，然后再给控件添加月份标题

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="300dp"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.example.calendar.MainActivity">

    <RelativeLayout
        android:padding="10dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        <TextView
            android:clickable="true"
            android:id="@+id/tv_pre"
            android:textColor="#4586ED"
            android:layout_centerVertical="true"
            android:textSize="16sp"
            android:text="7月"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
        <TextView
            android:layout_centerInParent="true"
            android:id="@+id/tv_month"
            android:textColor="#768797"
            android:layout_centerVertical="true"
            android:textSize="16sp"
            android:text="2016年8月"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
        <TextView
            android:clickable="true"
            android:layout_alignParentRight="true"
            android:id="@+id/tv_next"
            android:textColor="#4586ED"
            android:layout_centerVertical="true"
            android:textSize="16sp"
            android:text="8月"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
    </RelativeLayout>

    <com.example.calendar.view.CalendarView
        android:id="@+id/calendar"
        android:layout_width="300dp"
        android:layout_height="300dp" />
</LinearLayout>
```
在activity里面查找到控件，并通过CaldendarView里面的setCalendar方法来显示calendar代表的月份。
### 更改自己想要的视图
#### 更改统一样式
如果我们想要更改所有布局的样式，只需要在Day类里面drawDays方法里面画上自己需要的东西即可
，我们需要用到的几个属性是

 1. width 单个对象的宽度
 2. height 单个对象的高度
 3. location_x 对象除于第几行
 4. location_y 对象处于第几列
 然后我们就可以确定我们绘图的区域即（location_x *width，location_y *height ）为区域左上角坐标（（location_x +1）*width，（location_y+1） *height ）为右下角左边，可以在此区域画上任何你想添加的东西，如果需要改变背景，既可以重新写drawBackground方法，如果不想要代表工作状态的小点，可以把drawWorkState给去掉，而width ，height ， location_x ，location_y 都是通过DayManager在控制，完全不用你去考虑

#### 更改特殊样式
如果你想给某一天添加特殊的属性，

 1. 给Day里面添加一个属性
 2. 在DayManager里面生成包含天数的集合时判断是否是该天数，如果是设置该属性，如果你想控制某几天显示该属性，就声明一个集合，在生成天数事判断集合是否包含该天数，如果包含，则设置该属性
 3. 根据属性画对应的图标。
这样就可以画出自己想要的效果了。


		
