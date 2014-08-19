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
 
import java.io.*;
import org.xml.sax.*;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileInputStream;
import java.util.LinkedList;
 
public class XmlParser extends DefaultHandler {
  
    // inner class that holds word & it's coords
    private class Word {

        Word(String _word, String _coords) {
            word = _word;
            coords = _coords;
        }
        String word;
        String coords;

        boolean isNewline() {
            return (word == "\n" && coords == null);
        }

        boolean isHyphen() {
            return (word.equals("-") && coords == null);
        }

    };
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
	private String wordCoords = ""; // image coordinates of the word
    private BufferedWriter bw;
    // list of words per line
    private LinkedList<Word> lineWords = new LinkedList<Word>();
    private String currentWord = "";
    private boolean blnHyphenated = false;
	private int physicalPageCount = -1; // start at -1 so first page is "page zero"

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
	
   // version for reading from STDIN
    public void parseXmlFile() {

        try {
            saxParser.parse(System.in, this);
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
 
    public void endDocument() {

        // write out  any remaining words
        writeOutWords();
        dumpToFile(lines.trim());

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

        if (qName.equals("WORD")) {
            // Get the coords for the word  
            wordCoords = attributes.getValue("coords");
            blnHyphenated = false;
        }

        // get the physical page count
        if (qName.equals("PARAM")) {

            String v = attributes.getValue("name");
            if (v.equals("PAGE")) {
                physicalPageCount++;
            }
        }

        if (qName.equals("LINE")) {
            paragraph = true;
        }

        if (qName.equals("PAGECOLUMN")) {
            pageColumn = true;
            pageCount++;
        }

    }

    public void endElement(String uri, String localName, String qName) {

        if (qName.equals("WORD")) {
            if (pageCount > 4 || isGoogle == false) {
                if (currentWord.length() > 0) {
                    this.lineWords.add(new Word(currentWord, this.wordCoords));
                }
                if (blnHyphenated) {
                    this.lineWords.add(new Word("-", null));
                }

            }

            currentWord = "";
            wordCoords = "";
        }

        if (qName.equals("LINE")) {
            paragraph = false;

            if (pageCount > 4 || isGoogle == false) {

                // if there's no hyphenated word at the end of this line, write out the line
                if (!blnHyphenated) {
                    writeOutWords();
                } else {
                    // insert a "new line" 
                    this.lineWords.add(new Word("\n", null));
                }
            }
 			
            paragraph_str = "";

        }

        if (qName.equals("PAGECOLUMN")) {

            dumpToFile(lines.trim());
            lines = "";
            pageColumn = false;

        }
	}

    private void writeOutWords() {

        boolean prevHyphen = false;
        boolean addNewline = false;
        Integer lineOffset = 0;

        for (Word w : this.lineWords) {

            // do NOT write out the 2nd part of hyphenated words. Since we're
            // concatenation them together in the text.
  
            if (w.isHyphen()) {
                prevHyphen = true;
                continue;
            }
            if (w.isNewline()) {

                if (prevHyphen) {
                    addNewline = true; // defer the newline until we write the hyphenated word
                } else {
                    lines += "\n";
                    lineOffset = 0;
                }

            } else {

                if (prevHyphen && addNewline) {
                    prevHyphen = false; // don't put a space between the words
                } else if (lineOffset > 0) { // don't put space at start of the line
                    lines += " ";
                }
                lines += w.word;
                lineOffset++;

                if (addNewline) {
                    addNewline = false;
                    lines += "\n";
                    lineOffset = 0;
                }

            }

        } // end loop through words

        lines += "\n";
 
        lineWords.clear();

    }

    public void characters(char buf[], int offset, int len) {

        try {

            String s = new String(buf, offset, len);
            if (paragraph && s.trim().length() > 0) {

                // see if the word ends in a hyphen 
                blnHyphenated = s.endsWith("-");
				String st = s;
				if (Xml2Text.blnStripNonAlpha == true){
					// we need to treat each <WORD> as a single word, the "RemoveChars" class
					// replaces "--" with a space, we do NOT want that to happen for DjVu files
					// so "shrink" that to one dash. 
					st = RemoveChars.strip(s.replace("--", "-"));
				}
                if (st.length() > 0) {

					// some books have 5 pages of a "google preamble" which we
					// don't want to include.
                    if (st.equalsIgnoreCase("google")) {
                        isGoogle = true;
                    }
                    currentWord += st;
                } else {
                    // if - after we stripped out all the non-alpha characters,
                    // we have nothing - reset the hyphenated flag that may have
                    // be set above. We only want to join hyphenated words we we
                    // actually have words.
                    blnHyphenated = false;
                }

            }
 
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
