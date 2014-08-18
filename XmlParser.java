
import java.io.*;
import org.xml.sax.*;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileInputStream;

public class XmlParser extends DefaultHandler {

    private SAXParserFactory factory; 
    private SAXParser saxParser; 
    int count = 0;
    boolean paragraph = false;
    boolean pageColumn = false;
    boolean isGoogle = false;
    int pageCount = 0;
    String paragraph_str = "";
    String page_str = "";
    String lines = "";
    private BufferedWriter bw;
    private final String MRT = "machine readable text";

    public XmlParser() {

        try {

            factory = SAXParserFactory.newInstance();
            saxParser = factory.newSAXParser();
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("g"), "UTF8"));

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public XmlParser(String outFile) {

        try {

            factory = SAXParserFactory.newInstance();
            saxParser = factory.newSAXParser();
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF8"));


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void parseXmlFile(String fileName) {

        try {

            InputStream inputStream = new FileInputStream(new File(fileName));
            saxParser.parse(inputStream, this);


        } catch (Exception e) {
            System.err.println("Error processing file: " + fileName);
            e.printStackTrace();
        }

    }

    public void parseXmlFile(String fileName, String outFile) {

        try {

            InputStream inputStream = new FileInputStream(new File(fileName));
            saxParser.parse(inputStream, this);


        } catch (Exception e) {
            System.err.println("Error processing file: " + fileName);
            e.printStackTrace();
        }

    }

    public void startDocument() {

        try {
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void endDocument() {

        try {

            bw.close();
            //System.out.println( pageCount);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
 
    public void dumpToFile(String str) {

        try {

            bw.write(str);
            bw.newLine();
            bw.flush();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
 
        if (qName.equals("LINE")) {
            paragraph = true;
        }

        if (qName.equals("PAGECOLUMN")) {
            pageColumn = true;
            pageCount++;
        }

    }

    public void endElement(String uri, String localName, String qName) {

        if (qName.equals("LINE")) {
            paragraph = false;
            if (pageCount < 5) {
                if (isGoogle == false) {
                    lines += paragraph_str.trim() + "\n";
                }

            } else {
                lines += paragraph_str.trim() + "\n";
            }
 			
            paragraph_str = "";

        }

        if (qName.equals("PAGECOLUMN")) {

            dumpToFile(lines.trim());
            lines = "";
            pageColumn = false;
            isGoogle = false;
        }


    }

    public void characters(char buf[], int offset, int len) {

        try {

            String s = new String(buf, offset, len);
            if (paragraph) {
                String st = s.trim();
                if (st.length() > 0) {

					// some books have 5 pages of a "google preamble" which we
					// don't want to include.
                    if (st.equalsIgnoreCase("google")) {
                        isGoogle = true;
                    }

                    if (st.equals("'")) {
                        paragraph_str = paragraph_str.trim() + st + "";
                    } else {
                        paragraph_str += st + " ";
                    }
                }

            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
