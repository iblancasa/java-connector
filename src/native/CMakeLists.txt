# MIT License

# Copyright (c) 2019 Israel Blancas

# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:

# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.

# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
cmake_minimum_required(VERSION 3.5)
project(java-connector)

if(NOT BUILD_SHARED_LIBS)
    set(error
        "This library can not be built statically. You need to pass the "
        "-DBUILD_SHARED_LIBS=1 parameter to CMake.")
    message(FATAL_ERROR ${error})
endif()

# Find JNI libraries
# Read more: https://cmake.org/cmake/help/v3.13/module/FindJNI.html
find_package(JNI REQUIRED QUIET)

set(submodule "${CMAKE_CURRENT_SOURCE_DIR}/../..")
if(NOT CONNECTOR_INCLUDE_DIR)
    # Search the RTI Connector include folder
    find_file(connector_header
        NAME "lua_binding_ddsConnector.h"
        HINTS
            "${submodule}/rticonnextdds-connector/include/lua_binding"
    )

    if(connector_header_NOTFOUND)
        set(error
            "It was not possible to find the lua_binding_ddsConnector.h "
            "file. Please, check if you cloned this repository using the "
            "Git '--recurse-submodules parameter' or provide the path to "
            "the include directory providing the "
            "-DCONNECTOR_INCLUDE_DIR=<path> parameter to CMake")
        message(FATAL_ERROR ${error})
    endif()

    get_filename_component(CONNECTOR_INCLUDE_DIR
        "${connector_header}"
        DIRECTORY)

    set(CONNECTOR_INCLUDE_DIR "${CONNECTOR_INCLUDE_DIR}/../")
endif()

set(RTI_ARCHITECTURE "x64Linux2.6gcc4.4.5")

# Search the RTI Connector include folder

find_library(connectorlib
    NAME "rtiddsconnector"
    HINTS
        "${CONNECTOR_LIB_DIR}"
        "${submodule}/rticonnextdds-connector/lib/${RTI_ARCHITECTURE}"
)

if(connectorlib_NOTFOUND)
    set(error
        "It was not possible to find the rtiddsconnector library"
        "file. Please, check if you cloned this repository using the "
        "Git '--recurse-submodules parameter' or provide the path to "
        "the library directory providing the "
        "-DCONNECTOR_LIB_DIR=<path> parameter to CMake")
    message(FATAL_ERROR ${error})
endif()

# Create the library target
add_library(connector_jvm
    "${CMAKE_CURRENT_SOURCE_DIR}/src/RTIDDSConnector.c"
)

# Add include directories
target_include_directories(connector_jvm
    PUBLIC
        "${JAVA_INCLUDE_PATH}" # jni.h
        "${JAVA_INCLUDE_PATH2}" # jni_md.h and jniport.h
        "${CONNECTOR_INCLUDE_DIR}"
        "${CMAKE_CURRENT_SOURCE_DIR}/include"
)

# Set the name totally independent from the target name
set_target_properties(connector_jvm
    PROPERTIES
        OUTPUT_NAME "connectorjvm"
)

target_link_libraries(connector_jvm "${connectorlib}")