public class Hand extends Joint{
    private Vector x,y,z;
    public Hand(double xPos,double yPos,double zPos,double size,Vector x,Vector y,Vector z){
        super(xPos,yPos,zPos,size);
        super.addRotate(x);
        super.addRotate(y);
        super.addRotate(z);
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public Vector getX(){
        return x;
    }
    public Vector getY(){
        return y;
    }
    public Vector getZ(){
        return z;
    }
}