# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-printconfiguration /tmp/full-r8-config.txt

# Useful links
# https://medium.com/androiddevelopers/practical-proguard-rules-examples-5640a3907dc9
# https://jebware.com/blog/?p=418

-verbose

# move classes to root
-repackageclasses

# Reduces the size of the output more.
-allowaccessmodification

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
# Keep custom exceptions
-keep public class * extends java.lang.Exception

## Obfuscates Intrinsics, https://proandroiddev.com/is-your-kotlin-code-really-obfuscated-a36abf033dde
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
  public static void checkExpressionValueIsNotNull(java.lang.Object, java.lang.String);
  public static void checkFieldIsNotNull(java.lang.Object, java.lang.String);
  public static void checkFieldIsNotNull(java.lang.Object, java.lang.String, java.lang.String);
  public static void checkNotNull(java.lang.Object);
  public static void checkNotNull(java.lang.Object, java.lang.String);
  public static void checkNotNullExpressionValue(java.lang.Object, java.lang.String);
  public static void checkNotNullParameter(java.lang.Object, java.lang.String);
  public static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
  public static void checkReturnedValueIsNotNull(java.lang.Object, java.lang.String);
  public static void checkReturnedValueIsNotNull(java.lang.Object, java.lang.String, java.lang.String);
  public static void throwUninitializedPropertyAccessException(java.lang.String);
}

# Keep object methods
-keepclassmembers final class * {
    public final <methods>;
}

# For enumeration classes
-keepclassmembers enum * {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# Remove TempoLogger log
-assumenosideeffects class com.enricog.core.logger.api.TempoLogger {
    public final void d(...);
}
