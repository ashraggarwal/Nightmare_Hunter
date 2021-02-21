public class DLList<E>{
    private Node<E>dummy;
    private int size;
    public DLList(){
      size=0;
      dummy=new Node<E>(null);
      dummy.setNext(dummy);
      dummy.setPrev(dummy);
    }
    public void add(E data){
      Node<E>temp=dummy.prev();
      Node<E>temp1=new Node<E>(data);
      dummy.setPrev(temp1);
      temp1.setNext(dummy);
      temp.setNext(temp1);
      temp1.setPrev(temp);
      size++;
    }
    public E get(int num){
      Node<E>current=dummy.next();
      for(int i=0;i<num;i++){
        current=current.next();
      }
      return current.get();
    }
    public void remove(E data){
      Node<E>current=dummy;
      while(!current.next().equals(dummy)){
        if(current.next().get().equals(data)){
          Node<E>temp=current.next().next();
          current.setNext(temp);
          temp.setPrev(current);
          size--;
        }
        current=current.next();
      }
    }
    public String toString(){
      String s="";
      Node<E>current=dummy.next();
      while(!current.equals(dummy)){
        s+=current.toString()+"\n";
        current=current.next();
      }
      return s;
    }
    public int size(){
      return size;
    }
  }