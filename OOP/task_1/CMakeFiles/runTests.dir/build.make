# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.5

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/egor/Documents/programming/labs/OOP/task_1

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/egor/Documents/programming/labs/OOP/task_1

# Include any dependencies generated for this target.
include CMakeFiles/runTests.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/runTests.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/runTests.dir/flags.make

CMakeFiles/runTests.dir/main.cpp.o: CMakeFiles/runTests.dir/flags.make
CMakeFiles/runTests.dir/main.cpp.o: main.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/egor/Documents/programming/labs/OOP/task_1/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/runTests.dir/main.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/runTests.dir/main.cpp.o -c /home/egor/Documents/programming/labs/OOP/task_1/main.cpp

CMakeFiles/runTests.dir/main.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/runTests.dir/main.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/egor/Documents/programming/labs/OOP/task_1/main.cpp > CMakeFiles/runTests.dir/main.cpp.i

CMakeFiles/runTests.dir/main.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/runTests.dir/main.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/egor/Documents/programming/labs/OOP/task_1/main.cpp -o CMakeFiles/runTests.dir/main.cpp.s

CMakeFiles/runTests.dir/main.cpp.o.requires:

.PHONY : CMakeFiles/runTests.dir/main.cpp.o.requires

CMakeFiles/runTests.dir/main.cpp.o.provides: CMakeFiles/runTests.dir/main.cpp.o.requires
	$(MAKE) -f CMakeFiles/runTests.dir/build.make CMakeFiles/runTests.dir/main.cpp.o.provides.build
.PHONY : CMakeFiles/runTests.dir/main.cpp.o.provides

CMakeFiles/runTests.dir/main.cpp.o.provides.build: CMakeFiles/runTests.dir/main.cpp.o


CMakeFiles/runTests.dir/trit_class.cpp.o: CMakeFiles/runTests.dir/flags.make
CMakeFiles/runTests.dir/trit_class.cpp.o: trit_class.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/egor/Documents/programming/labs/OOP/task_1/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building CXX object CMakeFiles/runTests.dir/trit_class.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/runTests.dir/trit_class.cpp.o -c /home/egor/Documents/programming/labs/OOP/task_1/trit_class.cpp

CMakeFiles/runTests.dir/trit_class.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/runTests.dir/trit_class.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/egor/Documents/programming/labs/OOP/task_1/trit_class.cpp > CMakeFiles/runTests.dir/trit_class.cpp.i

CMakeFiles/runTests.dir/trit_class.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/runTests.dir/trit_class.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/egor/Documents/programming/labs/OOP/task_1/trit_class.cpp -o CMakeFiles/runTests.dir/trit_class.cpp.s

CMakeFiles/runTests.dir/trit_class.cpp.o.requires:

.PHONY : CMakeFiles/runTests.dir/trit_class.cpp.o.requires

CMakeFiles/runTests.dir/trit_class.cpp.o.provides: CMakeFiles/runTests.dir/trit_class.cpp.o.requires
	$(MAKE) -f CMakeFiles/runTests.dir/build.make CMakeFiles/runTests.dir/trit_class.cpp.o.provides.build
.PHONY : CMakeFiles/runTests.dir/trit_class.cpp.o.provides

CMakeFiles/runTests.dir/trit_class.cpp.o.provides.build: CMakeFiles/runTests.dir/trit_class.cpp.o


CMakeFiles/runTests.dir/ref_class.cpp.o: CMakeFiles/runTests.dir/flags.make
CMakeFiles/runTests.dir/ref_class.cpp.o: ref_class.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/egor/Documents/programming/labs/OOP/task_1/CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Building CXX object CMakeFiles/runTests.dir/ref_class.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/runTests.dir/ref_class.cpp.o -c /home/egor/Documents/programming/labs/OOP/task_1/ref_class.cpp

CMakeFiles/runTests.dir/ref_class.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/runTests.dir/ref_class.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/egor/Documents/programming/labs/OOP/task_1/ref_class.cpp > CMakeFiles/runTests.dir/ref_class.cpp.i

CMakeFiles/runTests.dir/ref_class.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/runTests.dir/ref_class.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/egor/Documents/programming/labs/OOP/task_1/ref_class.cpp -o CMakeFiles/runTests.dir/ref_class.cpp.s

CMakeFiles/runTests.dir/ref_class.cpp.o.requires:

.PHONY : CMakeFiles/runTests.dir/ref_class.cpp.o.requires

CMakeFiles/runTests.dir/ref_class.cpp.o.provides: CMakeFiles/runTests.dir/ref_class.cpp.o.requires
	$(MAKE) -f CMakeFiles/runTests.dir/build.make CMakeFiles/runTests.dir/ref_class.cpp.o.provides.build
.PHONY : CMakeFiles/runTests.dir/ref_class.cpp.o.provides

CMakeFiles/runTests.dir/ref_class.cpp.o.provides.build: CMakeFiles/runTests.dir/ref_class.cpp.o


CMakeFiles/runTests.dir/tests.cpp.o: CMakeFiles/runTests.dir/flags.make
CMakeFiles/runTests.dir/tests.cpp.o: tests.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/egor/Documents/programming/labs/OOP/task_1/CMakeFiles --progress-num=$(CMAKE_PROGRESS_4) "Building CXX object CMakeFiles/runTests.dir/tests.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/runTests.dir/tests.cpp.o -c /home/egor/Documents/programming/labs/OOP/task_1/tests.cpp

CMakeFiles/runTests.dir/tests.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/runTests.dir/tests.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/egor/Documents/programming/labs/OOP/task_1/tests.cpp > CMakeFiles/runTests.dir/tests.cpp.i

CMakeFiles/runTests.dir/tests.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/runTests.dir/tests.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/egor/Documents/programming/labs/OOP/task_1/tests.cpp -o CMakeFiles/runTests.dir/tests.cpp.s

CMakeFiles/runTests.dir/tests.cpp.o.requires:

.PHONY : CMakeFiles/runTests.dir/tests.cpp.o.requires

CMakeFiles/runTests.dir/tests.cpp.o.provides: CMakeFiles/runTests.dir/tests.cpp.o.requires
	$(MAKE) -f CMakeFiles/runTests.dir/build.make CMakeFiles/runTests.dir/tests.cpp.o.provides.build
.PHONY : CMakeFiles/runTests.dir/tests.cpp.o.provides

CMakeFiles/runTests.dir/tests.cpp.o.provides.build: CMakeFiles/runTests.dir/tests.cpp.o


CMakeFiles/runTests.dir/iter_class.cpp.o: CMakeFiles/runTests.dir/flags.make
CMakeFiles/runTests.dir/iter_class.cpp.o: iter_class.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/egor/Documents/programming/labs/OOP/task_1/CMakeFiles --progress-num=$(CMAKE_PROGRESS_5) "Building CXX object CMakeFiles/runTests.dir/iter_class.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/runTests.dir/iter_class.cpp.o -c /home/egor/Documents/programming/labs/OOP/task_1/iter_class.cpp

CMakeFiles/runTests.dir/iter_class.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/runTests.dir/iter_class.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/egor/Documents/programming/labs/OOP/task_1/iter_class.cpp > CMakeFiles/runTests.dir/iter_class.cpp.i

CMakeFiles/runTests.dir/iter_class.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/runTests.dir/iter_class.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/egor/Documents/programming/labs/OOP/task_1/iter_class.cpp -o CMakeFiles/runTests.dir/iter_class.cpp.s

CMakeFiles/runTests.dir/iter_class.cpp.o.requires:

.PHONY : CMakeFiles/runTests.dir/iter_class.cpp.o.requires

CMakeFiles/runTests.dir/iter_class.cpp.o.provides: CMakeFiles/runTests.dir/iter_class.cpp.o.requires
	$(MAKE) -f CMakeFiles/runTests.dir/build.make CMakeFiles/runTests.dir/iter_class.cpp.o.provides.build
.PHONY : CMakeFiles/runTests.dir/iter_class.cpp.o.provides

CMakeFiles/runTests.dir/iter_class.cpp.o.provides.build: CMakeFiles/runTests.dir/iter_class.cpp.o


# Object files for target runTests
runTests_OBJECTS = \
"CMakeFiles/runTests.dir/main.cpp.o" \
"CMakeFiles/runTests.dir/trit_class.cpp.o" \
"CMakeFiles/runTests.dir/ref_class.cpp.o" \
"CMakeFiles/runTests.dir/tests.cpp.o" \
"CMakeFiles/runTests.dir/iter_class.cpp.o"

# External object files for target runTests
runTests_EXTERNAL_OBJECTS =

runTests: CMakeFiles/runTests.dir/main.cpp.o
runTests: CMakeFiles/runTests.dir/trit_class.cpp.o
runTests: CMakeFiles/runTests.dir/ref_class.cpp.o
runTests: CMakeFiles/runTests.dir/tests.cpp.o
runTests: CMakeFiles/runTests.dir/iter_class.cpp.o
runTests: CMakeFiles/runTests.dir/build.make
runTests: /usr/lib/libgtest.a
runTests: CMakeFiles/runTests.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/egor/Documents/programming/labs/OOP/task_1/CMakeFiles --progress-num=$(CMAKE_PROGRESS_6) "Linking CXX executable runTests"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/runTests.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/runTests.dir/build: runTests

.PHONY : CMakeFiles/runTests.dir/build

CMakeFiles/runTests.dir/requires: CMakeFiles/runTests.dir/main.cpp.o.requires
CMakeFiles/runTests.dir/requires: CMakeFiles/runTests.dir/trit_class.cpp.o.requires
CMakeFiles/runTests.dir/requires: CMakeFiles/runTests.dir/ref_class.cpp.o.requires
CMakeFiles/runTests.dir/requires: CMakeFiles/runTests.dir/tests.cpp.o.requires
CMakeFiles/runTests.dir/requires: CMakeFiles/runTests.dir/iter_class.cpp.o.requires

.PHONY : CMakeFiles/runTests.dir/requires

CMakeFiles/runTests.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/runTests.dir/cmake_clean.cmake
.PHONY : CMakeFiles/runTests.dir/clean

CMakeFiles/runTests.dir/depend:
	cd /home/egor/Documents/programming/labs/OOP/task_1 && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/egor/Documents/programming/labs/OOP/task_1 /home/egor/Documents/programming/labs/OOP/task_1 /home/egor/Documents/programming/labs/OOP/task_1 /home/egor/Documents/programming/labs/OOP/task_1 /home/egor/Documents/programming/labs/OOP/task_1/CMakeFiles/runTests.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/runTests.dir/depend

