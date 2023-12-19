#include <jni.h>
#include <string>

#include <unistd.h>
#include <android/log.h>

const char *APP_PACKAGE = "com.flower.basket.orderflower";
const std::string CommURL = "api/Community/GetAll";

//std::string APP_DIR = "/data/user/0/com.flower.basket.orderflower/";
//std::string FILES_DIR = "files/";

extern "C"
JNIEXPORT jstring JNICALL
Java_com_flower_basket_orderflower_activity_MainActivity_getTune(JNIEnv *env,
                                                                             jobject clazz) {
    // TODO: implement getTune()
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
    // TODO: implement getCommandTail()
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
    // TODO: implement getData()
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
    // TODO: implement getUniqueID()
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
    // TODO: implement getUserData()
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
    // TODO: implement getUsers()
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
    // TODO: implement getTotal()
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
    // TODO: implement dataStr()
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
    // TODO: implement getIV()
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
    // TODO: implement getPointer()
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

    // TODO: implement getPhotoDir()
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
    // TODO: implement getVideoContent()
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
    // TODO: implement getTune()
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
    // TODO: implement getCommandTail()
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
    // TODO: implement getKey()
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
    // TODO: implement getIV()
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
    // TODO: implement baseURL()
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
    // TODO: implement tokenPrecedence()
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
    // TODO: implement headerName()
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
Java_com_flower_basket_orderflower_api_AppData_getCommunityURL(JNIEnv *env, jobject thiz) {
    // TODO: implement getCommunityURL()
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