#include <jni.h>

JNIEXPORT jstring JNICALL
Java_app_tapho_utils_ViewUtilsKt_SecretKey(JNIEnv *env, jclass clazz) {
    return (*env)-> NewStringUTF(env, "6d66fb7debfd15bf716bb14752b9603b");
}

JNIEXPORT jstring JNICALL
Java_app_tapho_utils_ViewUtilsKt_IVKey(JNIEnv *env, jclass clazz) {
    return (*env)-> NewStringUTF(env, "1234567890123456");

}

JNIEXPORT jstring JNICALL
Java_app_tapho_network_MyApi_00024Companion_BaseURL(JNIEnv *env, jobject thiz) {
    return (*env)-> NewStringUTF(env, "https://tapfo.in/delta/api/");
}

JNIEXPORT jstring JNICALL
Java_app_tapho_network_MyApiV2_00024Companion_BaseURL(JNIEnv *env, jobject thiz) {
    return (*env)-> NewStringUTF(env, "https://tapfo.in/delta/api/v2/");
}