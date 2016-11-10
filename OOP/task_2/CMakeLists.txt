cmake_minimum_required(VERSION 2.8)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
add_executable(robot _WALL-E/main.cpp _WALL-E/classImpl/parser.cpp _WALL-E/classImpl/map.cpp _WALL-E/classImpl/robot.cpp _WALL-E/classImpl/mapper.cpp _WALL-E/classImpl/surface.cpp)
