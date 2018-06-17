# DigestNumber
一个精致的Android数字变换控件（内含一个时间变换控件）

[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)
[![stable](http://badges.github.io/stability-badges/dist/stable.svg)](http://github.com/badges/stability-badges)
[[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badge/)

## 特性

* 自适应控件大小
* 跨线程状态同步
* 支持java代码设置
* 可自定义参数

## 集成步骤

在工程中添加依赖

```groovy
dependencies {
    implementation 'cn.tecotaku:digitalnumber-android:1.0.1'
}
```

## 运行截图
![](https://github.com/SinoReimu/DigitalNumber-android/blob/master/screenshot/screen.gif)

## 使用

直接引用

```java
    <cn.tecotaku.cn.digital_clock.view.ClockView
        android:layout_width="match_parent"
        android:background="#000"
        android:layout_height="match_parent" />
```
高级设置 （阴影半径与线条颜色）
```java
    <cn.tecotaku.cn.digital_clock.view.ClockView
        android:layout_width="match_parent"
        android:background="#000"
        android:layout_height="match_parent"
        hak:lineColor="#66ccff"
        hak:shadowRadius="14" />
```

### attrs设置状态布局资源


```xml
    <attr name="lineColor" format="color" />
    <attr name="shadowRadius" format="integer"/>
```

## 单独使用Number类
Number类需要绑定在一个自定义的View中，当你创建完一个自定义View之后你只需要
### 1.调用构造函数
```java
    public Number(int x, int y, int length, int initColor, int shadowRadius,int initStatus)
```
参数名 | 类型 | 作用
------------ | ------------- | ------------
x(必选) | int | 数字在View中的起始X坐标
y(必选) | int | 数字在View中的起始Y坐标
length(必选) | int | 数字的每一条边的长度 |
initColor(可选 默认#66ccff) | int | 数字的初始颜色（shadow颜色）
shadowRadius(可选 默认14) | int | shadow半径
initStatus(可选 默认0) | int | 初始状态的数字

### 2.绑定Cnavas绘制
在onDraw中，调用Number的onDraw方法，类似这样
```java
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        number.onDraw(canvas);
    }
```
然后它就被绑定到了这个Canvas上

### 3.更新它的数字状态
通过调用
```java
    void updateNumber(int newNum) //必须在主线程调用（使用ValueAnimator）
```
更新数字状态（可以更新到同样的数字，将自动过滤），可用于各类需要数字变动的控件

如果要自定义Number线条样式，可以直接更改Number.getPaint()获取到的Paint，或者继承Number类复写其中的属性和方法。

## 贡献
如果你对于这个组件的改进有任何意见可以直接提出ISSUE，我会以最快的速度进行回复。或者你有自己的想法也可以加入进来，直接Pull Request就可以啦~

## License
> Copyright (C) 2017-2018 Hoshikawa Shiro.
> Licensed under the MIT.
> (See the [LICENSE](https://github.com/SinoReimu/DigitalNumber-android/blob/master/LICENSE) file for the whole license text.)
