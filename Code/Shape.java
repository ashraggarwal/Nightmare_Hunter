import java.util.ArrayList;
import java.io.Serializable;
public interface Shape extends Serializable{
    public ArrayList<Triangle>getTriangles();
    public ArrayList<Sphere>getSpheres();
}