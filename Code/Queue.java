public class Queue<E>{
    private SNode<E>front,back;
    private int size;
    public void add(E data){
        SNode<E>temp=new SNode<E>(data);
        if(front==null){
            front=temp;
            back=temp;
        }else{
            back.setNext(temp);
            back=temp;
        }
        size++;
    }
    public E remove(){
        E temp=null;
        if(front!=null){
            if(front.equals(back)){
                temp=front.get();
                front=null;
                back=null;
                size--;
            }else{
                temp=front.get();
                front=front.next();
                size--;
            }
        }
        return temp;
    }
    public int size(){
        return size;
    }
    public int frontNum(){
        if(front!=null){
            return front.getNum();
        }
        return -1;
    }
    public Iterator<E>iterator(){
        return new Iterator<E>(front);
    }
}