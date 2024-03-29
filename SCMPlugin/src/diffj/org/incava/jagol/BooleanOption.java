package diffj.org.incava.jagol;

import java.io.*;
import java.util.*;


/**
 * Represents an option that is an boolean.
 */
public class BooleanOption extends Option
{
    private Boolean value;
    
    public BooleanOption(String longName, String description)
    {
        super(longName, description);

        value = null;
    }

    /**
     * Returns the value. This is null if it has not been set.
     */
    public Boolean getValue()
    {
        return value;
    }

    /**
     * Sets the value.
     */
    public void setValue(Boolean value)
    {
        this.value = value;
    }

    /**
     * Sets the value from the string, for a boolean type.
     */
    public void setValue(String value) throws InvalidTypeException
    {
        String lcvalue = value.toLowerCase();
        if (lcvalue.equals("yes") || lcvalue.equals("true")) {
            setValue(Boolean.TRUE);
        }
        else if (lcvalue.equals("no") || lcvalue.equals("false")) {
            setValue(Boolean.FALSE);
        }
        else {
            throw new InvalidTypeException(longName + " expects boolean argument (yes/no/true/false), not '" + value + "'");
        }
    }

    /**
     * Sets from a list of command-line arguments. Returns whether this option
     * could be set from the current head of the list.
     */
    public boolean set(String arg, List args) throws OptionException
    {
        if (arg.equals("--" + longName)) {
            // args.remove(0);
            setValue(Boolean.TRUE);
        }
        else if (arg.equals("--no-" + longName) || arg.equals("--no" + longName)) {
            // args.remove(0);
            setValue(Boolean.FALSE);
        }
        else if (shortName != 0 && arg.equals("-" + shortName)) {
            // args.remove(0);
            setValue(Boolean.TRUE);
        }
        else {
            return false;
        }
        return true;
    }

    public String toString()
    {
        return value == null ? "" : value.toString();
    }

}
