#!/usr/bin/python3
import sys, math, time
from bitarray import bitarray

def list_sieve(number):
    s = list(range(2, number))
    # print(sys.getsizeof(s) / 1024 / 1024, "MBs")
    for i in range(2, int(math.sqrt(number)) + 1):
        if s[i - 2] != 0:
            for j in range(2 * i, number, i):
                s[j - 2] = 0
    return s

def set_sieve(number):
    s = set(range(2, number))
    # print(sys.getsizeof(s) / 1024 / 1024, "Mbs")
    for i in range(2, int(math.sqrt(number)) + 1):
        if i in s:
            s -= set(range(2 * i, number, i))
    return s

def bitarray_sieve(number):
    s = bitarray(range(2, number))
    # print(sys.getsizeof(s), "Bytes")
    for i in range(2, int(math.sqrt(number)) + 1):
        if s[i - 2] is True:
            for j in range(2 * i, number, i):
                s[j - 2] = False
    return s

if(len(sys.argv) > 1):
    try:
        # print(    list_sieve(int(sys.argv[1])))
        # print(     set_sieve(int(sys.argv[1])))
        # print(bitarray_sieve(int(sys.argv[1])))
        st = time.clock()
        list_sieve(int(sys.argv[1]))
        fn = time.clock()
        print((fn - st), " seconds")
    except(MemoryError):
        print("list memory error")
    try:
        st = time.clock()
        set_sieve(int(sys.argv[1])) # MemoryError N = 100000000
        fn = time.clock()
        print((fn - st), " seconds")
    except(MemoryError):
        print("set memory error")
    try:
        st = time.clock()
        bitarray_sieve(int(sys.argv[1]))
        fn = time.clock()
        print((fn - st), " seconds")
    except(MemoryError):
        print("bitarray memory error")
