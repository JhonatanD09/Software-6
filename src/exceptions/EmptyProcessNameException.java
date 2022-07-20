package exceptions;

public class EmptyProcessNameException extends Exception{

    public EmptyProcessNameException(){
        super("El proceso debe tener un nombre");
    }
}
