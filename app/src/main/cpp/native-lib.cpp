#include <jni.h>
#include <string>

#include <unistd.h>
#include <android/log.h>

const char *APP_PACKAGE = "com.flower.basket.orderflower";

//std::string APP_DIR = "/data/user/0/com.flower.basket.orderflower/";
//std::string FILES_DIR = "files/";

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_activity_MainActivity_getTune(JNIEnv *env,
                                                                 jobject clazz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string hello;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            hello = "G+5LVC/uFRf7GusdRx/YYw==";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_activity_EditorActivity_tailpiece(
        JNIEnv *env, jobject clazz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string hello;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            hello = "Jqj+J7j1Dt3lxwCYMJO/iZoR/7L7Ebuo26DPLpOa+fYh1DEp9zHO+A==";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_activity_SplashActivity_getData(JNIEnv *env,
                                                                   jobject clazz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string hello;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            hello = "MAGn0yYHidc=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_activity_SplashActivity_getUniqueID(JNIEnv *env,
                                                                       jobject clazz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string hello;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            hello = "pim6TXLPd0vi7EklM4jl1w==";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_activity_SplashActivity_getUData(JNIEnv *env,
                                                                    jobject clazz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string hello;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            hello = "FnscwvH5+Ou9aUemSQE9rg==";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_activity_SplashActivity_getUsers(JNIEnv *env,
                                                                    jobject clazz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string hello;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            hello = "oBQP0H1U8zU=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_activity_SplashActivity_getTotal(JNIEnv *env,
                                                                    jobject clazz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string hello;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            hello = "asp7X7MhBhU=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_preference_AppPreference_dataStr(
        JNIEnv *env,
        jobject clazz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string hello;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            hello = "1Q21EHgh0b9jen1JQqJNx9dBe8125eAO6mqQgSCRqbuH7/DbOfgWHkhByjetmGFUvPo4tt3aUJm/"
                    "Aug75ZTstGQivnPQZcnicsCqL/i8m6bDUhhmJl4qWPALFYwab52I/h/xSax4YDgAC/jpNkaAjLoh"
                    "QqihqEWaU55T3TdNBQpEAyJQIHaObLY2q4lI69P6zNXzTRyLYmxmXFGzE8tj2h8cbATztSfijDDW"
                    "VQPgl5g8sia+HbwLypP69rd7Vx1EoFGN4q/NJuZ4D6tNOgQQvmoRQ9n7wsx7nzbMW/JfHZ3k1xuV"
                    "DPt9ils6VyyIf11mVrVaIVygWM1XwTKh0oQwgw7tvc+VXM7YJtU4+rJMcBqVoK5VEcVBXlTAMjes"
                    "aWk2rbfNq5e/DWw=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_utilities_AESHelper_getIV(JNIEnv *env,
                                                             jclass clazz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string hello;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            hello = "kLoJrDxI";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_utilities_AESHelper_getPointer(JNIEnv *env,
                                                                  jclass clazz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string hello;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            hello = "Df5P6iT2x";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_activity_MainActivity_00024Companion_getPhotos(
        JNIEnv *env, jobject thiz, jstring basePath) {

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");
    const char *bPath = env->GetStringUTFChars(basePath, 0);

    std::string hello;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            hello = bPath;
            hello.append("/");
            hello.append("Photos");
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_activity_MainActivity_00024Companion_getContent(
        JNIEnv *env, jobject thiz, jstring basePath) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");
    const char *bPath = env->GetStringUTFChars(basePath, 0);

    std::string hello;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            hello = bPath;
            hello.append("/");
            hello.append("Content");
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_activity_MainActivity_00024Companion_getTune(
        JNIEnv *env, jobject thiz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string hello;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            hello = "G+5LVC/uFRf7GusdRx/YYw==";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_activity_VideoEditorActivity_00024Companion_tailpiece(
        JNIEnv *env, jobject thiz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string hello;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            hello = "Jqj+J7j1Dt3lxwCYMJO/iZoR/7L7Ebuo26DPLpOa+fYh1DEp9zHO+A==";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getKey(JNIEnv *env, jobject thiz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string key;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            key = "rnGlxh19tXMX12hF";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }
    return env->NewStringUTF(key.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getIV(JNIEnv *env, jobject thiz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string hello;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            hello = "vohqW23hnZYHeEcQ";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_RetroClient_baseURL(JNIEnv *env, jobject thiz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
//            url = "http://www.flowerbasket.somee.com/";
            url = "jg6LZ4/vPvxFbI5GyHwtHVhYFuCcbz9wtmd+K8dQH+XhDBTTs0ibc+G59dXh7/DR";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_RetroClient_headerPrecedence(JNIEnv *env, jobject thiz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "Bearer";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_RetroClient_headerName(JNIEnv *env, jobject thiz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "Authorization";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getCommunity(JNIEnv *env, jobject thiz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "N8XK/IDNd0CViqtmaMkSeU/u1ObGA7SHEjmFzRhuJuc=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getRegister(JNIEnv *env, jobject thiz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "IPuHa0cCtcgUolVHIV3Fi1q161q+ePIEgSMItrfPx6M=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getLogin(JNIEnv *env, jobject thiz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "oW+PLhkeux359Q8HW4Hipw==";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getAllUsers(JNIEnv *env, jobject thiz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "Tm9p/SEfd2+qevYa7r7G/ex153yqQdM8azRNq4VG29s=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getUpdateUser(JNIEnv *env, jobject thiz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "FZM4CP8OKAF1WE6VlQ8IjuxY8RMgUUsjDX5bXB61wNA=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getChangePassword(JNIEnv *env, jobject thiz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "Jstf1PiBL30lLYPH1fYDP9wdLXP+vn+dtlNuZoDWtuM=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getAllFlowers(JNIEnv *env, jobject thiz) {

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "VVxujt07nsexZMi5EoEuBynicJFnj5RPqWzWHGl03sU=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getAddSubscription(JNIEnv *env, jobject thiz) {

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "iUK7sinKK2p4AGUEz7C+bgRIlGTbUGBwYXnYORo3pA0=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getAllSubscriptions(JNIEnv *env,
                                                                   jobject thiz) {
    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "iUK7sinKK2p4AGUEz7C+bjyivd98i3l9LudwORncErA=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getSubscription(JNIEnv *env, jobject thiz) {

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "iUK7sinKK2p4AGUEz7C+buJOIHanw/g0Uf4P/UFi33A=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getUpdateSubscription(JNIEnv *env, jobject thiz) {

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "iUK7sinKK2p4AGUEz7C+bpg1cYYZxoJ8aaEutUMg1JU=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getManageVacationMode(JNIEnv *env, jobject thiz) {

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "iUK7sinKK2p4AGUEz7C+brUPfkjQTK8kwb1YeIZ49V2t4oIbndMR903t9Nh/C5Hh";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getDeleteSubscription(JNIEnv *env, jobject thiz) {

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "iUK7sinKK2p4AGUEz7C+brLd7GVuu/nsh67kAyn4ZcU=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getAllOrders(JNIEnv *env, jobject thiz) {

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "B0I85C5sXarAibvgeW3HtPBUfkqVKh06ebBkK4rhlLw=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getGenerateOrder(JNIEnv *env, jobject thiz) {

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "ArDio8fy/1JHquXti7ggVuRi4OoBxCwTjJlv/PWwaR4=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getUpdateOrderStatus(JNIEnv *env, jobject thiz) {

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "0EkW7FlcwFhNFSn8MtmKsGEQkISY9aszW1UDO/5W020=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getVendor(JNIEnv *env, jobject thiz) {

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "MoBLoVVAInL+rr3zXQMZvVXGHvYhk8RoNa0Awb59RhY=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getUpdateFlower(JNIEnv *env, jobject thiz) {

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "bkMDETnBL896nZVQFo+Af2RxvmHjZr4lmzz5Qfvc0jI=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getReportOrders(JNIEnv *env, jobject thiz) {

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "woQgZ7qKOtZX2XzI2JUboFjhIuMcZRhb+EBxWuK3e5o=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_api_AppData_getTotalFlowers(JNIEnv *env, jobject thiz) {

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    std::string url;
    if (cmdline) {
        char application_id[100] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);

        if (strcmp(application_id, APP_PACKAGE) == 0) {
            url = "/nZhga++h+OdPVGyujq2qaXHeOAE8RVKPq9jvAqT0oc=";
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "Error", "Data not matched");
        }
        fclose(cmdline);
    }

    return env->NewStringUTF(url.c_str());
}