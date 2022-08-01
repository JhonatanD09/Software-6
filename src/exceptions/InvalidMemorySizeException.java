package exceptions;

public class InvalidMemorySizeException extends Exception{

    public InvalidMemorySizeException(){
        super("El tamaño de memoria debe ser un numero");
    }
}
