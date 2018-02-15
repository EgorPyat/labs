#!/usr/bin/python3
import sys
from bitarray import bitarray

def list_sieve(number):
    s = list(range(2, number))
    for i in s:
        if i != 0:
            for j in range(2 * i, number, i):
                s[j - 2] = 0
    return list(filter(lambda n : n != 0, s))

def set_sieve(number):
    s = set(range(2, number))
    for i in range(2, number):
        if i in s:
            s -= set(range(2 * i, number, i))
    return s

def bitarray_sieve(number):
    pass

if(len(sys.argv) > 1):
    print(    list_sieve(int(sys.argv[1])))
    print(     set_sieve(int(sys.argv[1])))
    print(bitarray_sieve(int(sys.argv[1])))
