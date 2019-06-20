# Vtil-调试Ui的工具

Vtil的目标是打造一个便捷调试Ui的工具，


## 特点功能:
 - 支持点击控件显示其属性
 - 支持控件属性修改
 - 支持Move模式,控件屏幕随意拖动并显示距离
 - 支持普通模式下可以触摸滑动选择控件
 - 支持选中子空间时再点击选中其父控件
 - 支持3d效果展示控件的绘制情况
 - 支持安装Vtil对应插件实现与Ide的交互

## 传送门


## Demo

#### 项目演示


## 简单用例
#### 1.在 build.gradle 中添加依赖
```
releaseImplementation 'com.github.QitLab.Vtil:vtil_no_op:beta-2.0'
debugImplementation 'com.github.QitLab.Vtil:vtil:beta-2.0'
```

#### 2.在Luancher的Activity中添加代码
```java
//onCreate
Vtil.register();

//onDestroy
Vtil.unregister();

```


## 赞赏

如果你喜欢 Vtil 的设计，感觉 Vtil 帮助到了你，可以点右上角 "Star" 支持一下 谢谢！ ^_^

