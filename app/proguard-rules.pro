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

#retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
#-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}


#Dexture
# Preserve all Dexter classes and method names

-keepattributes InnerClasses, Signature, *Annotation*

-keep class com.karumi.dexter.** { *; }
-keep interface com.karumi.dexter.** { *; }
-keepclasseswithmembernames class com.karumi.dexter.** { *; }
-keepclasseswithmembernames interface com.karumi.dexter.** { *; }

#room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**


#custome Chrome
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/juankysoriano/Documents/Work/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keep class members class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class app.tapho.ui.login.model.** { *; }
-keep class app.tapho.network.BaseRes.** { *; }
-keep class app.tapho.ui.login.referral_Model.** { *; }
-keep class app.tapho.ui.model.** { *; }
-keep class app.tapho.ui.merchants.model.** { *;}
-keep class app.tapho.ui.tcash.AddMoneyPopup.AddMoneyModel.**{ *; }
-keep class app.tapho.ui.tcash.model.** { *; }
-keep class app.tapho.ui.help.model.** { *; }
-keep class app.tapho.ui.BuyVoucher.CategoriesModel.** { *; }
-keep class app.tapho.ui.BuyVoucher.VoucherListViewModel.** { *; }
-keep class app.tapho.ui.BuyVoucher.VoucherDetails.** { *; }
-keep class app.tapho.ui.BuyVoucher.BuyVoucherApimodel.** { *; }
-keep class app.tapho.ui.RechargeService.ModelData.** { *; }
-keep class app.tapho.ui.Stories.Model.** { *; }
-keep class app.tapho.ui.games.models.** { *; }
-keep class app.tapho.ui.Faqs.Model.** { *; }
-keep class app.tapho.ui.News.Model.** { *; }
-keep class app.tapho.ui.PaytmPaymentGateway.InitiateTransactionModel.** { *; }
-keep class app.tapho.ui.PaytmPaymentGateway.TransactionProcess.** { *; }
-keep class app.tapho.ui.PaytmPaymentGateway.TransactionStatus.** { *; }
-keep class app.tapho.ui.walletTransactionData.** { *; }
-keep class app.tapho.ui.localbizzUI.Model.** { *; }
