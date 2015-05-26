#!/usr/bin/python

import glob, os, re
from subprocess import call

JAVA_BASE_DIR = './crypto-strats-server/src/main/java'
TYPE_DIR = JAVA_BASE_DIR + '/io/hbar/fx/data/series/types'

TYPE_REGEX = re.compile('\s(\w*)\s*\{')
FIELDS_REGEX = re.compile('\s(\w*)[,;]', re.MULTILINE|re.DOTALL)

for filename in glob.glob(os.path.join(TYPE_DIR, '*.java')):
	typeFile = open(filename, 'r')
	typeData = typeFile.read()

	typeString = TYPE_REGEX.findall(typeData)[0]
	fieldList = FIELDS_REGEX.findall(typeData)

	typeFile.close()

	
	protoFileName = typeString + 'Series.proto'
	protoFile = open(protoFileName, 'w')

	protoFile.write('syntax = "proto2";\n')
	protoFile.write('option java_package = "io.hbar.protobuf.schema";\n')
	protoFile.write('option java_outer_classname = "' + typeString + 'SeriesSchema";\n\n')

	protoFile.write('message ' + typeString + 'Series {\n\n')

	protoFile.write('\tmessage ' + typeString + ' {\n\n')

	protoFile.write('\t\trequired int32 timestamp = 1;\n')

	for i in range(0, len(fieldList)):
		protoFile.write('\t\trequired double ' + fieldList[i].lower() + ' = ' + str(i + 2) + ';\n')

	protoFile.write('\n\t}\n\n\trepeated ' + typeString + ' series = 1;\n\n}')

	protoFile.close()

	os.system('/usr/local/bin/protoc --java_out=' + JAVA_BASE_DIR + ' ' + protoFileName)




