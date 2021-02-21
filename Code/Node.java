public class Node<E>{
    private Node<E>next;
    private Node<E>prev;
    private E data;
    public Node(E data){
      this.data=data;
      next=null;
      prev=null;
    }
    public void setNext(Node<E>next){
      this.next=next;
    }
    public void setPrev(Node<E>prev){
      this.prev=prev;
    }
    public Node<E>next(){
      return next;
    }
    public Node<E>prev(){
      return prev;
    }
    public E get(){
      return data;
    }
  }