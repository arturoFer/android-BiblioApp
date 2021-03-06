# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\arturo\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#GLIDE
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#EVENTBUS
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

#EPUBLIB
-keep class nl.siegmann.epublib.** {*;}
-keep class org.slf4j.** {*;}

-dontwarn nl.siegmann.epublib.**
-dontwarn org.slf4j.**

-dontnote org.apache.http.**
-dontnote android.net.http.**

-dontnote org.afgl.biblioapp.estanteria.events.EstanteriaEvent
-dontnote org.afgl.biblioapp.libro.events.LibroEvent