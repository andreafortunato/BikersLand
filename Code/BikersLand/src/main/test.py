import os
import re

# print(os.getcwd())
# print(os.path.dirname(os.path.realpath(__file__)))

# rootdir = os.getcwd()
rootdir = os.path.dirname(os.path.realpath(__file__)) + "\\java"

it_lang_fileName = os.path.dirname(os.path.realpath(__file__)) + "\\resources\\com\\bikersland\\languages\\locale_it.properties"
en_lang_fileName = os.path.dirname(os.path.realpath(__file__)) + "\\resources\\com\\bikersland\\languages\\locale_en.properties"

with open(it_lang_fileName, "r") as it_lang_file:
	it_lang_fileLines = it_lang_file.readlines()

with open(en_lang_fileName, "r") as en_lang_file:
	en_lang_fileLines = en_lang_file.readlines()

translationPattern = "(.*?) ="

it_lang_trans = []
en_lang_trans = []

for line in it_lang_fileLines:
	it_lang_trans.append(re.match(translationPattern, line).groups()[0])

for line in en_lang_fileLines:
	en_lang_trans.append(re.match(translationPattern, line).groups()[0])

it_lang_trans.sort()
en_lang_trans.sort()


if it_lang_trans == en_lang_trans:
	lang_trans = it_lang_trans.copy()
else:
	print("Translation files are different!")
	exit(-1)


getStringPattern = ".*bundle\.getString\([\"\'](.*?)[\"\']\).*"
getStringFound = []
missingGetString = []

for subdir, dirs, files in os.walk(rootdir):
	for fileName in files:
		if fileName.endswith(".java"):
			fileDir = subdir + '\\' + fileName
			print("Checking: " + fileName + "...")

			with open(fileDir, "r") as file:
				fileStr = file.read()
				fileGetString = re.findall(getStringPattern, fileStr)
				for getString in fileGetString:
					if getString not in getStringFound:
						getStringFound.append(getString)
					if getString not in lang_trans:
						print("Missing " + getString + " from file " + fileName)
						if getString not in missingGetString:
							missingGetString.append(getString)
			print()

getStringFound.sort()
missingGetString.sort()
print("\n\nDifferent getString found: " + str(len(missingGetString)) + " -> " + str(missingGetString))