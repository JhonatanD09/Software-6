package exceptions;

public class EmptyProcessSizeException extends Exception{

    public EmptyProcessSizeException(){
        super("El proceso debe tener un tamaño");
    }
}
