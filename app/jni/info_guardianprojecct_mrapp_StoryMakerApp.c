/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//#define LOG_NDEBUG 0

//#include "JNIHelp.h"
#include "jni.h"

#include <fcntl.h>
#include <sys/stat.h>
#include <stdio.h>

//#include <utils/Log.h>

//#include <android_runtime/AndroidRuntime.h>


void
Java_info_guardianprojecct_mrapp_StoryMakerApp_createfifonative(JNIEnv* env)
{
    int status;
    status = mkfifo("/data/data/info.guardianproject.mrapp/pipe0", S_IWUSR | S_IRUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH ); 
    status = mkfifo("/data/data/info.guardianproject.mrapp/pipe1", S_IWUSR | S_IRUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH );
    status = mkfifo("/data/data/info.guardianproject.mrapp/pipe2", S_IWUSR | S_IRUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH );
    status = mkfifo("/data/data/info.guardianproject.mrapp/pipe3", S_IWUSR | S_IRUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH );
    status = mkfifo("/data/data/info.guardianproject.mrapp/pipe4", S_IWUSR | S_IRUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH );
    status = mkfifo("/data/data/info.guardianproject.mrapp/pipe5", S_IWUSR | S_IRUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH );
    status = mkfifo("/data/data/info.guardianproject.mrapp/pipe6", S_IWUSR | S_IRUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH );
    status = mkfifo("/data/data/info.guardianproject.mrapp/pipe7", S_IWUSR | S_IRUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH );
    status = mkfifo("/data/data/info.guardianproject.mrapp/pipe8", S_IWUSR | S_IRUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH );
    status = mkfifo("/data/data/info.guardianproject.mrapp/pipe9", S_IWUSR | S_IRUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH );
}
