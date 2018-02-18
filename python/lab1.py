#!/usr/bin/python3
import sys

def factorize(number):
    div = 2
    list = []
    while(number > 1):
        pow = 0;
        while(number % div == 0):
            number /= div
            pow += 1
        if(pow > 0): list.append([div, pow])
        div += 1
    return list

if(len(sys.argv) > 1):
    print(factorize(int(sys.argv[1])))
