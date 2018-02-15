#!/usr/bin/python3
import sys, lab2

def get_simple(number):
    return [x for x in range(number) if lab2.is_simple(x)]

if(len(sys.argv) > 1):
    print(get_simple(int(sys.argv[1])))
