public class SNode<E> {
    private SNode<E>next;
    private E data;
    private int num;
    private static int n=0;
    public SNode(E data){
        this.data=data;
        num=n;
        n++;
    }
    public void setNext(SNode<E>next){
        this.next=next;
    }
    public SNode<E> next(){
        return next;
    }
    public E get(){
        return data;
    }
    public int getNum(){
        return num;
    }
}