import math

f = open('input.txt', 'r')
res = 0
for line in f:
    if line.strip():
        mass = int(line.strip())
        res = res + math.floor(mass / 3) - 2

print(res)