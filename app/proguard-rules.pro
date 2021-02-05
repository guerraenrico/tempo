# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-printconfiguration /tmp/full-r8-config.txt

# Useful links
# https://medium.com/androiddevelopers/practical-proguard-rules-examples-5640a3907dc9
# https://jebware.com/blog/?p=418

# move classes to root
-repackageclasses

# Reduces the size of the output more.
-allowaccessmodification

# Obfuscates Intrinsics, https://proandroiddev.com/is-your-kotlin-code-really-obfuscated-a36abf033dde
#-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
#  public static void checkExpressionValueIsNotNull(java.lang.Object, java.lang.String);
#  public static void checkFieldIsNotNull(java.lang.Object, java.lang.String);
#  public static void checkFieldIsNotNull(java.lang.Object, java.lang.String, java.lang.String);
#  public static void checkNotNull(java.lang.Object);
#  public static void checkNotNull(java.lang.Object, java.lang.String);
#  public static void checkNotNullExpressionValue(java.lang.Object, java.lang.String);
#  public static void checkNotNullParameter(java.lang.Object, java.lang.String);
#  public static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
#  public static void checkReturnedValueIsNotNull(java.lang.Object, java.lang.String);
#  public static void checkReturnedValueIsNotNull(java.lang.Object, java.lang.String, java.lang.String);
#  public static void throwUninitializedPropertyAccessException(java.lang.String);
#}

# Remove Timber log
#-assumenosideeffects class timber.log.Timber {
#    public static void v(...);
#    public static void d(...);
#    public static void i(...);
#}

#-keep class androidx.compose.ui.platform.AndroidAmbientsKt
