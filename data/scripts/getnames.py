#! /usr/bin/env python

import collections
import csv
import json

langs = {
	"english" : "en_US.lang",
	"french" : "fr_FR.lang",
	"german" : "de_DE.lang",
	"spanish" : "es_ES.lang",
	"italian" : "it_IT.lang"
}

dictionary = {
	"english" : "",
	"french" : "",
	"german" : "",
	"spanish" : "",
	"italian" : ""
}

fname = "elements_table.csv"
with open(fname, 'r', encoding="iso-8859-1") as csvfile:
    rd = csv.reader(csvfile, dialect='excel')
    for i, row in enumerate(rd):
        if i == 0:
            header = row.copy()
        else:   
            for j, val in enumerate(row):
                if header[j] in dictionary.keys():
                    dictionary[header[j]] += "element.{}.name={}\n".format(row[1], val.capitalize())


for key, value in langs.items():
	obj = open(value, 'w',encoding="utf-8")
	obj.write(dictionary[key])
	obj.close()