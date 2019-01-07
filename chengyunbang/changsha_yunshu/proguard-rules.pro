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
-keep class com.zt.capacity.jinan_zwt.bean.** { *; }

#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------

#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------

-keepclasseswithmembers class com.zt.capacity.jinan_yunshu.util.JavaScriptinterface {
      <methods>;
}

#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------


#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------
