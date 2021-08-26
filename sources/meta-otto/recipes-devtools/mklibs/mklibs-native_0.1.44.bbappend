# gcc 10 uses c++17 as default, mklibs does not compile with that
CXXFLAGS_append = " -std=gnu++14 "
