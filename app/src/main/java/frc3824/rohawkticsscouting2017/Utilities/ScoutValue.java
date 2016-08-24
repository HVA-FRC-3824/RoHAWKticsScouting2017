package frc3824.rohawkticsscouting2017.Utilities;

/**
 * @author Andrew Messing
 *         Created: 8/11/16
 */
public class ScoutValue {

    private final static String TAG = "ScoutValue";

    public enum Type {INTEGER, DOUBLE, STRING, BOOLEAN, OBJECT}

    public class TypeException extends Exception
    {
        public TypeException(String message)
        {
            super(message);
        }
    }

    private Type mType;
    private int mValue_int;
    private double mValue_double;
    private String mValue_string;
    private boolean mValue_boolean;
    private Object mValue_object;

    public ScoutValue(int value)
    {
        mType = Type.INTEGER;
        mValue_int = value;
    }

    public ScoutValue(float value)
    {
        mType = Type.DOUBLE;
        mValue_double = value;
    }

    public ScoutValue(double value)
    {
        mType = Type.DOUBLE;
        mValue_double = value;
    }

    public ScoutValue(String value)
    {
        mType = Type.STRING;
        mValue_string = value;
    }

    public ScoutValue(boolean value)
    {
        mType = Type.BOOLEAN;
        mValue_boolean = value;
    }

    public ScoutValue(Object value)
    {
        mType = Type.OBJECT;
        mValue_object = value;
    }

    public Type getType()
    {
        return mType;
    }

    public int getInt() throws TypeException {
        if(mType == Type.INTEGER)
        {
            return mValue_int;
        }

        throw new TypeException(String.format("This ScoutValue is a %s not an int",enumToString(mType)));
    }

    public double getDouble() throws TypeException {
        if(mType == Type.DOUBLE)
        {
            return mValue_double;
        }

        throw new TypeException(String.format("This ScoutValue is a %s not a double", enumToString(mType)));
    }

    public String getString() throws TypeException {
        if(mType == Type.STRING)
        {
            return mValue_string;
        }

        throw new TypeException(String.format("This ScoutValue is a %s not a string",enumToString(mType)));
    }

    public boolean getBoolean() throws TypeException {
        if(mType == Type.BOOLEAN)
        {
            return  mValue_boolean;
        }

        throw new TypeException(String.format("This ScoutValue is a %s not a boolean",enumToString(mType)));
    }

    public Object getObject() throws TypeException {
        if(mType == Type.OBJECT)
        {
            return mValue_object;
        }
        throw new TypeException(String.format("This ScoutValue is a %s not a object",enumToString(mType)));
    }

    private String enumToString(Type type)
    {
        switch (type)
        {
            case INTEGER:
                return "int";
            case DOUBLE:
                return "double";
            case STRING:
                return "string";
            case BOOLEAN:
                return "boolean";
            case OBJECT:
                return "object";
            default:
                assert false;
                return "";
        }
    }
}
