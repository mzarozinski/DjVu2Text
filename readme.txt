UMASS DjVu to Text Conversion (V1.0) 
Copyright (C) 2013 by the University of Massachusetts at Amherst 
released under the GNU  GPL v3.0 (see GNU_license.txt)

Written by I. Zeki Yalniz
Maintained by Michael Zarozinski (MichaelZ@cs.umass.edu)


ABOUT THE TOOL
==============
This tool converts DjVu xml files to text. 
 
CONTACT INFORMATION
================================
For further information please contact info@ciir.cs.umass.edu. 

HOW TO COMPILE:
===============
Inside the source folder, type the following command to compile the code (tested for Java version 1.7):
"javac *.java"

HOW TO USE THE TOOL
===================

1 - COMMAND LINE INTERFACE:
---------------------------
USAGE: java -cp . Xml2Text <inputFileORfolderName> <outputFolder>

PARAMETERS:
<inputFileORfolderName>
	full path for input xml file or folder name. If the input file name is a folder, all the files in the folder are processed.
<outputFolder>
	[OPTIONAL] if specified, this is where the output is written. Without this, the output is written in the same folder as the DjVu XML files.
 