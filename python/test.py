# coding=utf-8
# -*- coding: utf-8 -*-

import hashlib
import filter
import logging
import os

file = "output-test/output-filtered/7859-785e817e7025c129e107c971247d1f9e.py"

os.system(f"wsl md5sum {file}")
os.system(f"wsl wc -lcL {file}")

with open(file, "r", encoding="utf-8") as f:
    print(1, hashlib.md5(f.read().encode("utf-8")).hexdigest())

with open(file, "r", encoding='utf-8') as f:
    print(2, hashlib.md5(f.read().encode("utf-8")).hexdigest())

with open(file, "rb") as f:
    print(2, hashlib.md5(f.read()).hexdigest())

with open(file, "rb") as f:
    h = hashlib.md5()
    l=55
    for line in f:
        h.update(line)
        ltmp = len(line.decode("utf-8"))
        if ltmp > l:
            l = ltmp
            # print(line, line.decode("utf-8"))
    print(2, h.hexdigest(), l)

with open(file, "rb") as f:
    file_byte = f.read()
    print(3, hashlib.md5(file_byte).hexdigest(), len(file_byte))
    file_content = file_byte.decode("utf-8")
    print(4, hashlib.md5(file_content.encode("utf-8")).hexdigest(), len(file_content))

with open(file, "rb") as f:
    h = hashlib.md5()
    while b := f.read(8192):
        h.update(b)
    print(5, h.hexdigest())

with open(file, "r", encoding="utf-8") as f:
    myhash = hashlib.md5()
    for line in f:
        myhash.update(line.encode("utf-8"))
    print(6, myhash.hexdigest())
