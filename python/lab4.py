#!/usr/bin/python3
import os, sys

def print_files(path):
    l= [[x, y] for x in os.listdir(path) for y in [os.stat(os.path.join(path, x)).st_size]]
    l.sort(key=lambda x: x[0])
    l.sort(key=lambda x: x[1], reverse=True)
    for k, v in l:
        print(k, v)

if(len(sys.argv) > 1):
    print_files(sys.argv[1])
