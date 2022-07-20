package exceptions;

public class EmptyPartitionNameException extends Exception{

    public EmptyPartitionNameException(){
        super("La particion debe tener un nombre");
    }
}
