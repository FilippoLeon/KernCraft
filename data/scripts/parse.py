#! /usr/bin/env python

import collections
import csv
import json

dict = collections.OrderedDict()

fname = "elements_table.csv"
with open(fname, 'r') as csvfile:
    rd = csv.reader(csvfile, dialect='excel')
    for i, row in enumerate(rd):
        if i == 0:
            header = row.copy()
        else:   
            dict[row[0]] = collections.OrderedDict()
            for j, val in enumerate(row):
                if j == 0:
                    pass
                else:
                    dict[row[0]][header[j]] = val
            print(', '.join(row))

J = json.dumps(dict, indent=4, separators=(', ', ': '))

obj = open('elements.json', 'w')
obj.write(J)
obj.close
