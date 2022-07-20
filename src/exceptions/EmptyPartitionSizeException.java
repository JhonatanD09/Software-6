package exceptions;

public class EmptyPartitionSizeException extends Exception{

    public EmptyPartitionSizeException(){
        super("La particion debe tener un tamaño");
    }
}
