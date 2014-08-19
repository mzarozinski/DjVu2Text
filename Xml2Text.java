/*  Copyright (C) <2013>  University of Massachusetts Amherst

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
 
import java.io.File;
import java.io.FilenameFilter;

 
public class Xml2Text {

    static private String outputFolder = null; // where to write the output, if null, write out to same folder as the input
    static private File[] files = null; // files to parse
    static final String outputExtension = ".txt";
	static boolean blnStripNonAlpha = false;
	
    public static void main(String[] args) {

	    readCommandLine(args);

        // if no "-i" param, use stdin
        if (files == null) {

            // if reading from STDIN, we'll take the book label from the output file.
            // We assume it ends in .txt
            String label = outputFolder.substring(outputFolder.lastIndexOf(File.separator) + 1, outputFolder.length() - 4);
            XmlParser xml = new XmlParser(outputFolder);
            xml.parseXmlFile();

        } else {

            for (int i = 0; i < files.length; i++) {
                parseFile(files[i]);
            }
        } // end we were passed an input file or path
	}
	
     private static void parseFile(File fileName)  {

        String inputFile = fileName.getAbsolutePath();
        if (new File(inputFile).isDirectory()) {
            return;
        }
        // get the name of the output file
        String tmpFileName = fileName.getName();
        String outFileName = tmpFileName.substring(0, tmpFileName.length() - 4) + outputExtension;
        // get the ID of the book, strip off the path and the trailing "_djvu.xml"
        String fileID = tmpFileName.substring(0, tmpFileName.length() - 9);
        String outputFileAndPath = null;
        // if no output path was specified on the command line put them
        // in the same folder
        if (outputFolder == null) {
            outputFileAndPath = fileName.getPath().substring(0, fileName.getPath().lastIndexOf(File.separator) + 1) + outFileName;
        } else {
            File out = new File(outputFolder);
            if (!out.exists()) {
                out.mkdirs();// create the folder if it does not exists
            }
            outputFileAndPath = outputFolder + File.separator + outFileName;
        }

        XmlParser xml = new XmlParser(outputFileAndPath);  
        xml.parseXmlFile(inputFile);

	} // end parseFile()
	
	  private static void readCommandLine(String args[]) {

        if (args.length == 0) {
            System.err.println(usage());
            System.exit(1);
        }
        // split the switch and the value
        for (String arg : args) {
            String flag = null;
            String value = null;
            try {
                flag = arg.substring(0, 2);
                value = arg.substring(2);
            } catch (Exception ex) {
                System.err.println("\nInavlid command line argument: " + arg);
                System.err.println(usage());
                System.exit(1);
            }

            if (flag.toLowerCase().equals("-h")) {
                    System.out.println(usage());
                    System.exit(0);
			} else if (flag.toLowerCase().equals("-i")) {
 
                    // determine if we're processing a single file or a folder
                    if (value.toLowerCase().endsWith(".xml")) {
                        // processing ONE file
                        files = new File[1];
                        files[0] = new File(value);
                    } else {
                        // we were passed in a path
                        File folder = new File(value);
                        // get all the XML files in that folder
                        files = folder.listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                return name.toLowerCase().endsWith(".xml");
                            }
                        });
                        if (files == null) {
                            System.err.println("No files found in path: " + value);
                            System.exit(0);
                        }
                    }
			} else if (flag.toLowerCase().equals("-o")) {
                    outputFolder = value;
			} else if (flag.toLowerCase().equals("-s")) {
                    blnStripNonAlpha = true;
			} else {
                    System.err.println("Invalid command line switch: " + flag);
                    System.err.println(usage());
                    System.exit(1);
            }
			
        } // end loop through args

        // check that is we are using STDIN we need an output destination
        if (files == null && outputFolder == null) {
            System.err.println("\nYou must specify either an input file/folder or an output folder.");
            System.err.println(usage());
            System.exit(1);
        }

    } // end readCommandLine

	  private static String usage() {
        String USAGE = "\nDjVu2Text [-i<input path>] [-h] [-o<output path>] [-s]\n"
                + "\t-i\tOptional. Path of file(s) to process. This can be a single XML file or a directory. If this is not specified input is read from stdin. If using stdin, the output file must be specified with the '-o' parameter.\n"
                + "\t-h\tOptional. Display this help message.\n"
                + "\t-o\tOptional. Where to write the output. If this is not specified the output is written to the same folder as the input.\n"
                + "\t-s\tOptional. If specified, non-alpha characters are stripped out.\n";
        return (USAGE);
    } // end usage()
	
}
