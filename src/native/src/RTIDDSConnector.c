#include "lua_binding/lua_binding_ddsConnector.h"
#include "RTIDDSConnector.h"
#include <stdio.h>
#include <stdlib.h>
#include <jni.h>


JNIEXPORT jlong JNICALL Java_RTIDDSConnector_RTIDDSConnector_1new
  (JNIEnv *env, jobject obj){

      return (jlong) RTIDDSConnector_new(
            "MyParticipantLibrary::Zero",
            "./Simple.xml", NULL);
  }