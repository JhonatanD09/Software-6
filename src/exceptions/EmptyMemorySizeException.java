package exceptions;

public class EmptyMemorySizeException extends Exception{

    public EmptyMemorySizeException(){
        super("Debe ingresar un tamaño de memoria");
    }
}
