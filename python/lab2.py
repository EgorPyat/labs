#!/usr/bin/python3
import sys, math, time

def is_simple(number):
    for div in range(2, int(math.sqrt(number)) + 1):
        if number % div == 0:
            return False
    return True
if(len(sys.argv) > 1):
    st = time.clock()
    print(is_simple(int(sys.argv[1])))
    fn = time.clock()
    print((fn - st), " seconds")
