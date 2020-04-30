#Python script to replace certain parameters in given opera json files to more easily
#convert them to POJOs
import sys
import fileinput
import re

if(len(sys.argv) < 2):
    sys.exit()
    
for jsonFile in sys.argv[1:]:
    with open(jsonFile, 'r') as file:
        fileData = file.read()
        
    fileData = fileData.replace('\"a\":[', '\"mean\":[')
    fileData = fileData.replace('\"s\":[', '\"sd\":[')
    fileData = fileData.replace('\"m\":[', '\"min\":[')
    fileData = fileData.replace('\"M\":[', '\"max\":[')
    fileData = fileData.replace('\"a\":', '\"mean\":')
    fileData = fileData.replace('\"s\":', '\"sd\":')
    fileData = fileData.replace('\"m\":', '\"min\":')
    fileData = fileData.replace('\"M\":', '\"max\":')
    fileData = fileData.replace('\"class\":', '\"clazz\":')
    fileData = fileData.replace('\"?\"', '0')
    fileData = re.sub('\"Desc\w*_i\":', '\"Desc_i\":', fileData)
    fileData = re.sub('\"conc\w*\":', '\"conc\":', fileData)
    
    outputFile = "converted/" + jsonFile
    with open(outputFile, 'w') as file:
        file.write(fileData)