# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Administrator\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------
-keep class com.zt.capacity.common.bean.** { *; }

#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------

#eventBus
#百度地图包
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**
#百度语音包
-keep class com.baidu.speech.**{*;}
#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}

#极光
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#gson
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

#ARouter
-keep public class com.alibaba.android.arouter.routes.**{*;}
#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------

-keepclassmembers class com.zt.capacity.jinan_zwt.util.BaseJavaScript {
    <methods>;
}

#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------


#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------
