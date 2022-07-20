package exceptions;

public class RepeatedNameException extends Exception{

    public RepeatedNameException(String name){
        super(name + ", ya existe");
    }
}
