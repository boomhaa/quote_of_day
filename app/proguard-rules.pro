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
-keep, allowobfuscation, allowshrinking interface retrofit2.Call
-keep, allowobfuscation, allowshrinking class retrofit2.Response

-keep, allowobfuscation, allowshrinking class kotlin.coroutines.Continuation

# Сохраняем модель Quote
-keep class com.example.citate_of_day.data.Quote { *; }

-keepattributes Signature
-keepattributes *Annotation*

# Не трогать поля моделей
-keep class com.google.gson.** { *; }

-keep class com.android.tools.r8.internal.** { *; }

# Полностью отключаем обработку R8 для тестов
-keep class **Test* { *; }
-keep class * extends org.junit.** { *; }
-keepclassmembers class * extends org.junit.** { *; }

# Сохраняем все классы в тестовых пакетах
-keep class *Test { *; }
-keep class *Test$* { *; }
