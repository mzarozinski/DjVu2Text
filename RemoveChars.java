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

import java.util.regex.Pattern;

/*
 * class that can be used across multiple programs to strip out characters
 */

/**
 *
 * @author michaelz
 */
public class RemoveChars {
    // TODO: future we may pass in a locale and use that in the regex (ex: "\p{Latin}")

    static Pattern ignoreCharsPattern = Pattern.compile("[^\\p{L}\\s+]"); // pre-compiled regex for matching any UTF-8 letter symbol or space
    static Pattern hyphenPattern = Pattern.compile("-\\s*[\\r\\n]"); // pre-compiled regex for matching hyphenated words

    public static String strip(String str) {

        // MCZ: turn double dashes into spaces, otherwize text like:
        // 'Tis too much proved--that with devotion's visage
        // becomes
        // Tis too much provedthat with devotions visage
        // and Merge hyphenated words at the end of each line.
        // Regular exp: HYPHEN (SPACE|TAB)* (NEWLINE|RETURN)

        String tmpStr = hyphenPattern.matcher(str.replace("--", " ")).replaceAll("");

        // strip out non-text characters and remove any extra spaces
        return (ignoreCharsPattern.matcher(tmpStr).replaceAll("").replaceAll("\\s+", " ").trim());
    }
}
