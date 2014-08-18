
import java.io.File;
import java.io.FilenameFilter;

// MCZ 6/2013 - modifying to accept parameters: file_or_directory [output_directory]
// You can pass in a single file to process or a whole directory. If you want the 
// output to go some place other than where the input is, specify a directory
// as the 2nd parameter. 
// If a single file is passed in, it must end with the "xml" file extension.
public class Xml2Text {

    public static void main(String[] args) {

        // make sure there's at least 1 argument (either file or folder to process)
        // if only 1 arg is given, we'll write the output to the same folder.

        if (args.length == 0) {
            System.out.println("Usage: Xml2Text <input path or file> [output path]");
            System.exit(0);
        }

        String outputFolder = null;
        if (args.length > 1) {
            outputFolder = args[1];
        }

        File[] files = null;
        String outputExtension = ".txt";

        // determine if we're processing a single file or a folder
        if (args[0].toLowerCase().endsWith(".xml")) {
            // processing ONE file
            files = new File[1];
            files[0] = new File(args[0]);
        } else {
            // we were passed in a path
            File folder = new File(args[0]);
            // get all the XML files in that folder
            files = folder.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".xml");
                }
            });
            if (files == null) {
                System.err.println("No files found in path: " + args[0]);
                System.exit(0);
            }
        }

        for (int i = 0; i < files.length; i++) {
            String inputFile = files[i].getAbsolutePath();
            if (new File(inputFile).isDirectory()) {
                continue;
            }

            // get the name of the output file
            String tmpFileName = files[i].getName();
            String outFileName = tmpFileName.substring(0, tmpFileName.length() - 4) + outputExtension;

            String outputFileAndPath = null;

            // if no output path was specified on the command line put them
            // in the same folder
            if (outputFolder == null) {
                outputFileAndPath = files[i].getPath().substring(0, files[i].getPath().lastIndexOf(File.separator) + 1) + outFileName;
            } else {
                File out = new File(outputFolder);
                if (!out.exists()) {
                    out.mkdirs();// create the folder if it does not exists
                }
                outputFileAndPath = outputFolder + File.separator + outFileName;
            }

            XmlParser xml = new XmlParser(outputFileAndPath);
            xml.parseXmlFile(inputFile);

        }
    }
}
