# AndroidBaseLibrary

## 目的
  将一些APP内常用的功能和基础类抽成一个个lib，使用的时候直接集成进入工程。

## 项目结构

- app  项目路径
- core 存放基础的一些类，以及引用包

*TODO List*
  
- LocationLib 定位
- PushLib     推送
- PayLib      支付
- UpdateLib   升级
- QrCodeLib   二维码
- CameraLib   相机

## Core使用的一些库

#### google support 
- com.android.support:appcompat-v7:24.2.1
- com.android.support:design:24.2.1
- com.android.support:recyclerview-v7:24.2.1

#### ORM
- [com.github.satyan:sugar:1.5](http://satyan.github.io/sugar/)

#### Net
- [com.squareup.okhttp3:okhttp:3.4.2](http://square.github.io/okhttp/)
- com.squareup.okhttp3:logging-interceptor:3.4.2
- [com.squareup.retrofit2:retrofit:2.1.0](https://square.github.io/retrofit/)
- com.squareup.retrofit2:adapter-rxjava:2.1.0
- com.squareup.retrofit2:converter-gson:2.1.0

#### Net debug
- [com.facebook.stetho:stetho:1.4.1](http://facebook.github.io/stetho/)
- com.facebook.stetho:stetho-okhttp3:1.4.1

#### Rx
- [io.reactivex:rxandroid:1.2.1](https://github.com/ReactiveX/RxAndroid)

#### Field and method binding
- [com.jakewharton:butterknife:8.2.1](http://jakewharton.github.io/butterknife/)
- com.jakewharton:butterknife-compiler:8.2.1 (apt)

#### other
- [com.liaoinstan.springview:library:1.2.6](https://github.com/liaoinstan/SpringView) (pullRefresh)
- [com.github.bumptech.glide:glide:3.7.0](https://github.com/bumptech/glide) (Pic)
- [com.yqritc:recyclerview-flexibledivider:1.2.9](https://github.com/yqritc/RecyclerView-FlexibleDivider)
- [com.jakewharton.timber:timber:4.1.2](https://github.com/JakeWharton/timber) (Logging)

这些都可以在 [config.gradle](https://github.com/tanxiaoluo/AndroidBaseLibrary/blob/master/config.gradle) 中看到
