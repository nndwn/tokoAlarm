# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class androidx.multidex.** { *; }
-dontwarn androidx.**
-keep class android.support.multidex.** { *; }
-dontwarn android.support.**
-keep class androidx.startup.** { *; }

# Keep MQTT library classes
-keep class org.eclipse.paho.** { *; }
-dontwarn org.eclipse.paho.**

# Keep classes from the repository you're using
-keep class com.github.hannesa2.paho.** { *; }
-dontwarn com.github.hannesa2.paho.**

# Keep your MqttHelper class and its methods
-keep class com.tester.iotss.utils.helpers.MqttHelper { *; }

-dontwarn com.android.org.conscrypt.SSLParametersImpl
-dontwarn org.apache.harmony.xnet.provider.jsse.SSLParametersImpl