# Boblesortering
# (Den enkleste men akk så ineffektive sorteringsmetoden)

def boblesorter (a):
    while True:
        endret = False
        for i in range(1,len(a)):
            if a[i-1] > a[i]:
                t = a[i-1];  a[i-1] = a[i];  a[i] = t
                endret = True
        if not endret: return 0

data = [ 3, 17, -3, 0, 3, 1, 12 ]
boblesorter(data)
print("Resultatet er", data)
