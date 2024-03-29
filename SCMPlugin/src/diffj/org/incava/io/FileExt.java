package diffj.org.incava.io;

import java.io.*;
import java.util.*;

import diffj.org.incava.qualog.Qualog;


public class FileExt
{
    /**
     * Reads the file into a single string, which is null on error. The returned
     * string will contain end-of-line characters. The <code>arg</code> argument
     * is just so we can overload based on return type.
     */
    public static String readFile(String fileName, String arg)
    {
        return readFile(new File(fileName), arg);
    }

    /**
     * Reads the file into a string array, without end-of-line characters
     * (sequences). The array is null on error. The <code>arg</code> argument is
     * just so we can overload based on return type.
     */
    public static String[] readFile(String fileName, String[] arg)
    {
        return readFile(new File(fileName), arg);
    }

    /**
     * Reads the file into a single string, which is null on error.The
     * <code>arg</code> argument is just so we can overload based on return
     * type.
     */
    public static String readFile(File file, String arg)
    {
        String[] contents = readFile(file, new String[] {});
        if (contents == null) {
            return null;
        }
        else {
            StringBuffer buf      = new StringBuffer();
            String       lineSep  = System.getProperty("line.separator");
            
            for (int i = 0; contents != null && i < contents.length; ++i) {
                buf.append(contents[i] + lineSep);
            }
            
            return buf.toString();
        }
    }

    /**
     * Reads the file into a string array, without end-of-line characters
     * (sequences). The <code>arg</code> argument is just so we can overload
     * based on return type.
     */
    public static String[] readFile(File file, String[] arg)
    {
        try {
            BufferedReader br   = new BufferedReader(new FileReader(file));
            List           list = new ArrayList();

            String in;
            while ((in = br.readLine()) != null) {
                // contents.append(in + System.getProperty("line.separator"));
                list.add(in);
            }

            return (String[])list.toArray(new String[] {});
        }
        catch (Exception e) {
            Qualog.log("exception: " + e);
            return null;
        }
    }

    /**
     * Reads the file into a string array, optionally with end-of-line
     * characters (sequences).
     */
    public static String read(FileReader fr, boolean eoln)
    {
        String lineSep = eoln ? System.getProperty("line.separator") : null;
        StringBuffer contents = new StringBuffer();
        
        try {
            BufferedReader br = new BufferedReader(fr);
            List list = new ArrayList();

            String in;
            while ((in = br.readLine()) != null) {
                // contents.append(in + System.getProperty("line.separator"));
                contents.append(in);
                if (eoln) {
                    contents.append(lineSep);
                }
            }

            return contents.toString();
        }
        catch (Exception e) {
            Qualog.log("exception: " + e);
            return null;
        }
    }

}
