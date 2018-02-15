#!/usr/bin/python3
import sys

def is_simple(number):
    if number % 2 == 0:
        return number == 2
    div = 3
    while div * div <= number and number % div != 0:
        div += 2
    return div * div > number

# if(len(sys.argv) > 1):
#     print(is_simple(int(sys.argv[1])))
