package exceptions;

public class EmptyProcessTimeException extends Exception{

    public EmptyProcessTimeException(){
        super("El proceso debe tener un tiempo");
    }
}
