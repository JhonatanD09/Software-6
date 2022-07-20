package exceptions;

public class InvalidTimeException extends Exception{

    public InvalidTimeException(){
        super("El tiempo debe ser un numero");
    }
}
