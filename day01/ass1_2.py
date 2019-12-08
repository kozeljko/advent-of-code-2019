import math

def recur(mass):
    res = math.floor(mass / 3) - 2
    if res <= 0:
        return 0
    else:
        return res + recur(res)

f = open('input.txt', 'r')
res = 0
for line in f:
    if line.strip():
        mass = int(line.strip())
        fuel = math.floor(mass / 3) - 2
        res = res + fuel + recur(fuel)

print(res)