public class Iterator<E> {
    private SNode<E>current;
    public Iterator(SNode<E>node){
        this.current=node;
    }
    public boolean hasNext(){
        return current!=null;
    }
    public E next(){
        E data=current.get();
        if(this.current!=null)
            this.current=current.next();
        return data;
    }  
}