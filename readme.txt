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
USAGE: DjVu2Text [-i<input path>] [-h] [-o<output path>] [-s]
        -i      Optional. Path of file(s) to process. This can be a single XML file or a directory. If this is not specified input is read from stdin. If using stdin, the output file must be specified with the '-o' parameter.
        -h      Optional. Display this help message.
        -o      Optional. Where to write the output. If this is not specified the output is written to the same folder as the input.
        -s      Optional. If specified, non-alpha characters are stripped out.

 
 