
import java.io.File;

public class Xml2Text{
	
	public static void main( String[] args){
	
            
        /*    XmlParser  xml1 = new XmlParser("C:\\Users\\zeki\\Desktop\\RTA dataset\\texts\\geschlechtundch01weingoog.txt");
	    xml1.parseXmlFile("C:\\Users\\zeki\\Desktop\\RTA dataset\\texts\\geschlechtundch01weingoog_djvu.xml");
            xml1 = new XmlParser("C:\\Users\\zeki\\Desktop\\RTA dataset\\texts\\dogmengeschicht01harngoog.txt");
	    xml1.parseXmlFile("C:\\Users\\zeki\\Desktop\\RTA dataset\\texts\\dogmengeschicht01harngoog_djvu.xml");
            xml1 = new XmlParser("C:\\Users\\zeki\\Desktop\\RTA dataset\\texts\\immanuelkantskr01kantgoog.txt");
	    xml1.parseXmlFile("C:\\Users\\zeki\\Desktop\\RTA dataset\\texts\\immanuelkantskr01kantgoog_djvu.xml");
            */
            
            String outputfile = null;
            XmlParser xml = null;            
            String inputfile = null;
            String inputfoldername = null, outputFolder = null;
            
           //inputfoldername = "C:\\Users\\zeki\\Documents\\MATLAB\\duplicateDetection\\eng-ger";
           //outputFolder = "datasets/eng-ger/378books";
          // inputfoldername = "C:\\Users\\zeki\\Documents\\MATLAB\\duplicateDetection\\eng-fre";
          // outputFolder = "datasets/eng-fre/444books";
          // inputfoldername = "C:\\Users\\zeki\\Documents\\MATLAB\\duplicateDetection\\eng-lat";
          // outputFolder = "datasets/eng-lat/233books_new";

         //  inputfoldername = "C:\\Users\\zeki\\Documents\\MATLAB\\duplicateDetection\\eng-ger_freud";
         //  outputFolder = "datasets/eng-ger/31books";
         //   inputfoldername = "C:\\Users\\zeki\\Documents\\MATLAB\\duplicateDetection\\AlignmentData\\eng_ia_novels_xml";
         //   outputFolder = "C:\\Users\\zeki\\Documents\\MATLAB\\duplicateDetection\\AlignmentData\\eng_ia_novels_txt";

         //   inputfoldername = "C:\\Users\\zeki\\Documents\\MATLAB\\RecursiveAlignmentData\\dataset\\ia_texts\\xml\\ger";
         //   outputFolder = "C:\\Users\\zeki\\Documents\\MATLAB\\RecursiveAlignmentData\\dataset\\ia_texts\\txt\\ger";
          //   inputfoldername = "C:\\Users\\zeki\\Documents\\NetBeansProjects\\DuplicateDetector\\datasets\\eng-ger\\50K\\20query_books_english_with_match";
           //  outputFolder = "C:\\Users\\zeki\\Documents\\\\NetBeansProjects\\DuplicateDetector\\datasets\\eng-ger\\50K\\20query_books_english_with_match\\text";

             inputfoldername = "C:\\Users\\zeki\\Documents\\NetBeansProjects\\DuplicateDetector\\datasets\\eng-ger\\50K\\20query_books_english_with_match_fixed";
             outputFolder = "C:\\Users\\zeki\\Documents\\\\NetBeansProjects\\DuplicateDetector\\datasets\\eng-ger\\50K\\20query_books_english_with_match_fixed\\text";


          //      inputfoldername = "C:\\Users\\zeki\\Documents\\NetBeansProjects\\DuplicateDetector\\datasets\\eng-ger\\50K\\absent_books_in_the_50K_dataset";
         //    outputFolder = "C:\\Users\\zeki\\Documents\\\\NetBeansProjects\\DuplicateDetector\\datasets\\eng-ger\\50K\\absent_books_in_the_50K_dataset\\text";

           // inputfoldername = "C:\\Users\\zeki\\Documents\\MATLAB\\RecursiveAlignmentData\\dataset\\ia_texts\\xml\\spa";
        //    outputFolder = "C:\\Users\\zeki\\Documents\\MATLAB\\RecursiveAlignmentData\\dataset\\ia_texts\\txt\\spa";

       //     inputfoldername = "C:\\Users\\zeki\\Documents\\MATLAB\\RecursiveAlignmentData\\dataset\\ia_texts\\xml\\fre";
       //     outputFolder = "C:\\Users\\zeki\\Documents\\MATLAB\\RecursiveAlignmentData\\dataset\\ia_texts\\txt\\fre";

            String outputExtension = ".txt";

            File out = new File(outputFolder);
            if (!out.exists()) { out.mkdirs();}    // create the folder if it does not exists
            
            // for each file in the folder
            File folder = new File(inputfoldername);
            File [] files = folder.listFiles();
            for ( int i = 0; i < files.length; i++){

                inputfile = files[i].getAbsolutePath();
                if ( new File( inputfile).isDirectory() ){continue;}

                outputfile = outputFolder + "\\" + inputfile.substring(inputfile.lastIndexOf('\\')+1, inputfile.lastIndexOf('.')) + outputExtension;
                System.out.println(outputfile);

                xml = new XmlParser(outputfile);
		xml.parseXmlFile(inputfile);

                // TODO: here extract unique words of the file too along with .tra file.
                
            }				
	}
}
