# Add project specific ProGuard rules here.
# This is a UI-only sandbox module; minification is disabled by default
# (see app/build.gradle.kts release block), so these rules stay inactive
# unless isMinifyEnabled is later switched to true.

-keep class androidx.compose.** { *; }
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
