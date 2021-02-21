import java.io.Serializable;
public class Container<E> implements Serializable{
    private static final long serialVersionUID = 1L;
    private E data;
    public E get(){
        return data;
    }
    public void set(E data){
        this.data=data;
    }
}