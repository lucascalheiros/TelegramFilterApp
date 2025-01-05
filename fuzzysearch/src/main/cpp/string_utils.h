//
// Created by Lucas Calheiros on 05/01/25.
//

#ifndef TELEGRAMFILTERAPP_STRING_UTILS_H
#define TELEGRAMFILTERAPP_STRING_UTILS_H

#include <jni.h>
#include <string>

std::string jstringToString(JNIEnv* env, jstring jStr);
std::string trim(const std::string& str);

#endif //TELEGRAMFILTERAPP_STRING_UTILS_H
