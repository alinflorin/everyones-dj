package ro.qoffice.everyonesdj.network;

import java.io.Serializable;

public class DataSet implements Serializable{
    private String command="NULL";
    private Object value;
    private String id;
    private boolean error=false;

    public DataSet() {

    }

    public DataSet(String i,String c,Object v) {
        command = c;
        id = i;
        value = v;
    }

    public DataSet(String i,String c,Object v,boolean err) {
        command = c;
        id = i;
        value = v;
        error = err;
    }

    public String getCommand() {
        return command;
    }

    public Object getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

    public boolean getError() {
        return error;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setError(boolean e) {
        error = e;
    }
}
