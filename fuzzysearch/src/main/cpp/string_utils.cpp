//
// Created by Lucas Calheiros on 05/01/25.
//

#include "string_utils.h"
#include <jni.h>
#include <string>


std::string jstringToString(JNIEnv* env, jstring jStr) {
    const char* chars = env->GetStringUTFChars(jStr, nullptr);
    std::string str(chars);
    env->ReleaseStringUTFChars(jStr, chars);
    return str;
}


std::string trim(const std::string& str) {
    const auto start = std::find_if(str.begin(), str.end(), [](unsigned char ch) { return !std::isspace(ch); });
    const auto end = std::find_if(str.rbegin(), str.rend(), [](unsigned char ch) { return !std::isspace(ch); }).base();
    return (start < end) ? std::string(start, end) : std::string();
}
