package exceptions;

public class InvalidSizeException extends Exception{

    public InvalidSizeException(){
        super("El tamaño debe ser un numero");
    }
}

