import java.util.ArrayList;
import java.io.Serializable;
public class Person implements Shape,Trackable,Serializable{
    private static final long serialVersionUID = 1L;
    private Joint head,neck,shoulderL,shoulderR,elbowL,elbowR,waist,hipL,hipR,kneeL,kneeR,footL,footR,bound,boundX,boundY,boundZ;
    private Hand handR,handL;
    private double scale;
    private ArrayList<Sphere>s;
    private ArrayList<Triangle>t;
    private ArrayList<Cylinder>c;
    private ArrayList<Joint>joints;
    private ArrayList<Limb>limbs;
    private int r,g,b;
    private Vector pos;
    private double[][]movePattern;
    private int walkTime,attackTime;
    private RectPrism bounds;
    private int texture; 
    private double[]rotate;
    private Sword sword;
    private int period;
    private boolean inAir;
    public Person(Vector pos,double scale,int r,int g,int b){
        this.rotate=new double[8];
        this.scale=scale;
        this.r=r;
        this.g=g;
        this.b=b;
        double hSize=0.15;
        //double size=0.05*0.8;
        double size=0.05*0.8;
        double size2=0.04;
        this.pos=pos;
        head=new Joint(0.0,0.0,0.92,scale*hSize);
        neck=new Joint(0.0,0.0,0.72);
        shoulderL=new Joint(-0.12,0.0,0.72,scale*size);
        shoulderR=new Joint(0.12,0.0,0.72,scale*size);
        elbowL=new Joint(/*-0.16*/-0.18,0.0,0.56,scale*size);
        elbowR=new Joint(/*0.16*/0.18,0.0,0.56,scale*size);
        handL=new Hand(/*-0.16*/-0.18,0.0,0.40,scale*size,new Vector(-1.0,0.0,0.0),new Vector(0.0,0.0,-1.0),new Vector(0.0,-1.0,0.0));
        handR=new Hand(/*0.16*/0.18,0.0,0.40,scale*size,new Vector(-1.0,0.0,0.0),new Vector(0.0,0.0,-1.0),new Vector(0.0,-1.0,0.0));
        waist=new Joint(0.0,0.0,0.43);
        hipL=new Joint(-0.06,0.0,0.43,scale*size);
        hipR=new Joint(0.06,0.0,0.43,scale*size);
        kneeL=new Joint(-0.06,0.0,0.22,scale*size);
        kneeR=new Joint(0.06,0.0,0.22,scale*size);
        footL=new Joint(-0.06,0.0,0.04,scale*size);
        footR=new Joint(0.06,0.0,0.04,scale*size);
        bound=new Joint(-0.18,-0.05,0.0);
        boundX=new Joint(0.18,-0.05,0.0);
        boundY=new Joint(-0.18,0.05,0.0);
        boundZ=new Joint(-0.18,0.05,1.0);
        joints=new ArrayList<Joint>();
        s=new ArrayList<Sphere>();
        joints.add(head);//0
        joints.add(neck);//1
        joints.add(shoulderL);//2
        joints.add(shoulderR);//3
        joints.add(elbowL);//4
        joints.add(elbowR);//5
        joints.add(handL);//6
        joints.add(handR);//7
        joints.add(waist);//8
        joints.add(hipL);//9
        joints.add(hipR);//10
        joints.add(kneeL);//11
        joints.add(kneeR);//12
        joints.add(footL);//13
        joints.add(footR);//14
        joints.add(bound);
        joints.add(boundX);
        joints.add(boundY);
        joints.add(boundZ);
        shoulderL.addRotate(new Vector(1.0,0.0,0.0));
        shoulderR.addRotate(new Vector(-1.0,0.0,0.0));
        shoulderL.addRotate(new Vector(0.0,1.0,0.0));
        shoulderR.addRotate(new Vector(0.0,1.0,0.0));
        elbowL.addRotate(new Vector(1.0,0.0,0.0));
        elbowR.addRotate(new Vector(-1.0,0.0,0.0));
        neck.addRotate(new Vector(0.0,0.0,1.0));
        neck.addRotate(new Vector(0.0,-1.0,0.0));
        hipL.addRotate(new Vector(1.0,0.0,0.0));
        hipR.addRotate(new Vector(-1.0,0.0,0.0));
        kneeL.addRotate(new Vector(1.0,0.0,0.0));
        kneeR.addRotate(new Vector(-1.0,0.0,0.0));
        double bellyWidth=0.95;
        double bellyDepth=1.1;
        neck.addRotate(shoulderR.minus(neck).multiply(bellyWidth));
        neck.addRotate(new Vector(0.0,size2*1.2*bellyDepth,0.0));
        limbs=new ArrayList<Limb>();
        limbs.add(new Limb(head,neck,size2));
        limbs.add(new Limb(neck,shoulderL,size2));
        limbs.add(new Limb(neck,shoulderR,size2));
        limbs.add(new Limb(neck,waist,neck.getRotate().get(2),neck.getRotate().get(3)));
        limbs.add(new Limb(shoulderL,elbowL,size2));
        limbs.add(new Limb(shoulderR,elbowR,size2));
        limbs.add(new Limb(elbowL,handL,size2));
        limbs.add(new Limb(elbowR,handR,size2));
        limbs.add(new Limb(waist,hipL,size2));
        limbs.add(new Limb(waist,hipR,size2));
        limbs.add(new Limb(hipL,kneeL,size2));
        limbs.add(new Limb(hipR,kneeR,size2));
        limbs.add(new Limb(kneeL,footL,size2));
        limbs.add(new Limb(kneeR,footR,size2));
        //{hipL,kneeL,hipR,kneeR,shoulderL,elbowL,shoulderR,elbowR}
        //movePattern=new double[][]{{Math.PI/12,-Math.PI/24,Math.PI/12,Math.PI/24},{Math.PI/6,-Math.PI/5,Math.PI/9,Math.PI/9},{-Math.PI/15,-Math.PI/15,-Math.PI/9,Math.PI/4},{-Math.PI/15,0.0,-Math.PI/9,0.0}};
        movePattern=new double[][]{{0.0,0.0,0.0,0.0},{Math.PI/4,Math.PI/4,Math.PI/4,Math.PI/4},{Math.PI/4,Math.PI/4,Math.PI/4,Math.PI/4},{Math.PI/4,Math.PI/4,Math.PI/4,Math.PI/4}};
        walkTime=-1;
        texture=-1;
        attackTime=-1;
        sword=new Sword(handL,scale,pos,scale*0.6,1.0,12,13);        
        turn(Math.PI);
    }
    public Person(Vector pos,double scale,int r,int g,int b,int texture){
        this.rotate=new double[8];
        this.scale=scale;
        this.texture=texture;
        this.r=r;
        this.g=g;
        this.b=b;
        double hSize=0.15;
        double size=0.05*0.8;
        double size2=0.04;
        this.pos=pos;
        head=new Joint(0.0,0.0,0.92,scale*hSize);
        neck=new Joint(0.0,0.0,0.72);
        shoulderL=new Joint(-0.12,0.0,0.72,scale*size);
        shoulderR=new Joint(0.12,0.0,0.72,scale*size);
        elbowL=new Joint(/*-0.16*/-0.18,0.0,0.56,scale*size);
        elbowR=new Joint(/*0.16*/0.18,0.0,0.56,scale*size);
        handL=new Hand(/*-0.16*/-0.18,0.0,0.40,scale*size,new Vector(-1.0,0.0,0.0),new Vector(0.0,0.0,-1.0),new Vector(0.0,-1.0,0.0));
        handR=new Hand(/*0.16*/0.18,0.0,0.40,scale*size,new Vector(-1.0,0.0,0.0),new Vector(0.0,0.0,-1.0),new Vector(0.0,-1.0,0.0));
        waist=new Joint(0.0,0.0,0.43);
        hipL=new Joint(-0.06,0.0,0.43,scale*size);
        hipR=new Joint(0.06,0.0,0.43,scale*size);
        kneeL=new Joint(-0.06,0.0,0.22,scale*size);
        kneeR=new Joint(0.06,0.0,0.22,scale*size);
        footL=new Joint(-0.06,0.0,0.04,scale*size);
        footR=new Joint(0.06,0.0,0.04,scale*size);
        bound=new Joint(-0.18,-0.05,0.0);
        boundX=new Joint(0.18,-0.05,0.0);
        boundY=new Joint(-0.18,0.05,0.0);
        boundZ=new Joint(-0.18,0.05,1.0);
        joints=new ArrayList<Joint>();
        s=new ArrayList<Sphere>();
        joints.add(head);//0
        joints.add(neck);//1
        joints.add(shoulderL);//2
        joints.add(shoulderR);//3
        joints.add(elbowL);//4
        joints.add(elbowR);//5
        joints.add(handL);//6
        joints.add(handR);//7
        joints.add(waist);//8
        joints.add(hipL);//9
        joints.add(hipR);//10
        joints.add(kneeL);//11
        joints.add(kneeR);//12
        joints.add(footL);//13
        joints.add(footR);//14
        joints.add(bound);
        joints.add(boundX);
        joints.add(boundY);
        joints.add(boundZ);
        shoulderL.addRotate(new Vector(1.0,0.0,0.0));
        shoulderR.addRotate(new Vector(-1.0,0.0,0.0));
        shoulderL.addRotate(new Vector(0.0,1.0,0.0));
        shoulderR.addRotate(new Vector(0.0,1.0,0.0));
        shoulderL.addRotate(new Vector(0.0,0.0,-1.0));
        shoulderR.addRotate(new Vector(0.0,0.0,-1.0));
        elbowL.addRotate(new Vector(1.0,0.0,0.0));
        elbowR.addRotate(new Vector(-1.0,0.0,0.0));
        elbowL.addRotate(new Vector(0.0,0.0,-1.0));
        elbowR.addRotate(new Vector(0.0,0.0,-1.0));
        neck.addRotate(new Vector(0.0,0.0,1.0));
        neck.addRotate(new Vector(0.0,-1.0,0.0));
        hipL.addRotate(new Vector(1.0,0.0,0.0));
        hipR.addRotate(new Vector(-1.0,0.0,0.0));
        kneeL.addRotate(new Vector(1.0,0.0,0.0));
        kneeR.addRotate(new Vector(-1.0,0.0,0.0));
        double bellyWidth=0.95;
        double bellyDepth=1.1;
        neck.addRotate(shoulderR.minus(neck).multiply(bellyWidth));
        neck.addRotate(new Vector(0.0,size2*1.2*bellyDepth,0.0));
        limbs=new ArrayList<Limb>();
        limbs.add(new Limb(head,neck,size2));
        limbs.add(new Limb(neck,shoulderL,size2));
        limbs.add(new Limb(neck,shoulderR,size2));
        limbs.add(new Limb(neck,waist,neck.getRotate().get(2),neck.getRotate().get(3)));
        limbs.add(new Limb(shoulderL,elbowL,size2));
        limbs.add(new Limb(shoulderR,elbowR,size2));
        limbs.add(new Limb(elbowL,handL,size2));
        limbs.add(new Limb(elbowR,handR,size2));
        limbs.add(new Limb(waist,hipL,size2));
        limbs.add(new Limb(waist,hipR,size2));
        limbs.add(new Limb(hipL,kneeL,size2));
        limbs.add(new Limb(hipR,kneeR,size2));
        limbs.add(new Limb(kneeL,footL,size2));
        limbs.add(new Limb(kneeR,footR,size2));
        //{hipL,kneeL,hipR,kneeR,shoulderL,elbowL,shoulderR,elbowR}
        //movePattern=new double[][]{{Math.PI/12,-Math.PI/24,Math.PI/12,Math.PI/24},{Math.PI/6,-Math.PI/5,Math.PI/9,Math.PI/9},{-Math.PI/15,-Math.PI/15,-Math.PI/9,Math.PI/4},{-Math.PI/15,0.0,-Math.PI/9,0.0}};
        movePattern=new double[][]{{0.0,0.0,0.0,0.0},{Math.PI/4,Math.PI/4,Math.PI/4,Math.PI/4},{Math.PI/4,Math.PI/4,Math.PI/4,Math.PI/4},{Math.PI/4,Math.PI/4,Math.PI/4,Math.PI/4}};
        walkTime=-1;
        attackTime=-1;
        sword=new Sword(handL,scale,pos,scale*0.6,1.4,13,12);
        period=4;
        inAir=false;
        turn(Math.PI);
    }
    public void startAttackAnimation(){
        if(attackTime==-1){
            attackTime++;
            undoWalkingAnimationArms();
        }
    }
    public void setInAir(boolean inAir){
        this.inAir=inAir;
    }
    public void attackAnimation(){
        int time1=4;
        int time2=4;
        int time3=4;
        int time4=4;
        if(attackTime>=0){
            if(walkTime!=-1&&inAir==false){
                undoWalkingAnimationArms(period,0.10,0.05);
            }
            //endWalk();
            if(attackTime<time1){
                shoulderL.rotate(subset(new int[]{2,4,6}),0,40*0.05/time1);
                shoulderL.rotate(subset(new int[]{4,6}),1,-40*0.02/time1);
                elbowL.rotate(subset(new int[]{4,6}),0,40*0.04/time1);
                //elbowL.rotate(subset(new int[]{6}),1,0.01);
            }else if(attackTime<(time1+time2)){
                elbowL.rotate(subset(new int[]{4,6}),0,-30*0.05/time2);
                shoulderL.rotate(subset(new int[]{4,6}),1,30*0.06/time2);
                shoulderL.rotate(subset(new int[]{2,4,6}),0,-30*0.05/time2);
                handL.rotate(subset(new int[]{6}),0,30*0.06/time2);
            }else if(attackTime<(time1+time2+time3)){
                elbowL.rotate(subset(new int[]{4,6}),0,30*0.01/time3);
                shoulderL.rotate(subset(new int[]{2,4,6}),0,30*0.04/time3);
                elbowL.rotate(subset(new int[]{6}),1,30*0.04/time3);
                handL.rotate(subset(new int[]{6}),0,-30*0.06/time2);
                //shoulderL.rotate(subset(new int[]{4,6}),1,0.05);
            }else if(attackTime<(time1+time2+time3+time4)){
                elbowL.rotate(subset(new int[]{6}),1,-30*0.04/time4);
                elbowL.rotate(subset(new int[]{4,6}),0,-30*(0.4/30)/time4);
                shoulderL.rotate(subset(new int[]{2,4,6}),0,-30*(1.7/30)/time4);
                shoulderL.rotate(subset(new int[]{4,6}),1,-30*(1.0/30)/time4);
            }else{
                attackTime=-1;
                redoWalkingAnimationArms();
                return;
            }
            attackTime++;
        }
    }
    public void hitMonster(ArrayList<Monster>monsters,double[]damage,Container<Monster>currentMonster,double attackPower){
        RectPrism blade=sword.getHitBox();
		for(int i=0;i<monsters.size();i++){
            ArrayList<RectPrism>hitBoxes=monsters.get(i).getHitBoxes();
            for(int j=0;j<hitBoxes.size();j++){
                if(hitBoxes.get(j).colliding(blade)&&attackTime>=0){
                    damage[i]+=attackPower;
                    monsters.get(i).takeDamage(attackPower);
                    currentMonster.set(monsters.get(i));
                    //System.out.println(damage[i]);
                }
            }
        }
    }
    public Sword getSword(){
        return sword;
    }
    public ArrayList<Joint>subset(int[]nums){
        ArrayList<Joint>sub=new ArrayList<Joint>();
        for(int i=0;i<nums.length;i++){
            sub.add(joints.get(nums[i]));
        }
        return sub;
    }
    public double triangleWave(int x,int period){
        return 1-(2.0/period)*Math.abs((x%period)-period/2);
    }
    public int squareWave(int x,int period){
        int time=x/period;
        return (time%2)*2-1;
    }
    public void doWalkingAnimation(){
        //System.out.println(kneeL.minus(footL).mag());
        //System.out.println(kneeR.minus(footR).mag());
        //shoulderL.rotate(subset(new int[]{4,6}),0,Math.PI/20);
        walkTime++;
        doWalkingAnimationLegs(period,0.15,0.11);
        doWalkingAnimationArms(period,0.10,0.05);
    }
    public void undoWalkingAnimationArms(int constant,double constant2,double constant3){
        shoulderL.rotate(subset(new int[]{4,6}),0,10*constant2/constant*squareWave(walkTime+constant/2,constant));
        elbowL.rotate(subset(new int[]{6}),0,10*constant3/constant*squareWave(walkTime,constant));
        shoulderR.rotate(subset(new int[]{5,7}),0,10*constant2/constant*squareWave(walkTime+constant/2,constant));
        elbowR.rotate(subset(new int[]{7}),0,-10*constant3/constant*squareWave(walkTime,constant));
    }
    public void undoWalkingAnimationArms(){
        elbowL.rotate(subset(new int[]{6}),0,-rotate[5]);
        shoulderL.rotate(subset(new int[]{4,6}),0,-rotate[4]);
        elbowR.rotate(subset(new int[]{7}),0,-rotate[7]);
        shoulderR.rotate(subset(new int[]{5,7}),0,-rotate[6]);
    }
    public void redoWalkingAnimationArms(){
        shoulderL.rotate(subset(new int[]{4,6}),0,rotate[4]);
        elbowL.rotate(subset(new int[]{6}),0,rotate[5]);
        shoulderR.rotate(subset(new int[]{5,7}),0,rotate[6]);
        elbowR.rotate(subset(new int[]{7}),0,rotate[7]);
    }
    public void doWalkingAnimationLegs(int constant,double constant2,double constant3){
        if(walkTime==0){
            //hipL.rotate(subset(new int[]{11,13}),0,constant4);
            //hipR.rotate(subset(new int[]{12,14}),0,-constant4);
            for(int i=0;i<constant;i++){
                hipL.rotate(subset(new int[]{11,13}),0,10*constant2/constant*squareWave(i+constant/2,constant));
                kneeL.rotate(subset(new int[]{13}),0,10*constant3/constant*squareWave(i,constant));
                rotate[0]+=10*constant2/constant*squareWave(i+constant/2,constant);
                rotate[1]+=10*constant3/constant*squareWave(i,constant);
            }
        }
        hipL.rotate(subset(new int[]{11,13}),0,10*constant2/constant*squareWave(walkTime+3*constant/2,constant));
        rotate[0]+=10*constant2/constant*squareWave(walkTime+3*constant/2,constant);
        kneeL.rotate(subset(new int[]{13}),0,10*constant3/constant*squareWave(walkTime+constant,constant));
        rotate[1]+=10*constant3/constant*squareWave(walkTime+constant,constant);
        hipR.rotate(subset(new int[]{12,14}),0,-10*constant2/constant*squareWave(walkTime+constant/2,constant));
        rotate[2]+=-10*constant2/constant*squareWave(walkTime+constant/2,constant);
        kneeR.rotate(subset(new int[]{14}),0,-10*constant3/constant*squareWave(walkTime,constant));
        rotate[3]+=-10*constant3/constant*squareWave(walkTime,constant);
    }
    public void doWalkingAnimationArms(int constant,double constant2,double constant3){
        //System.out.println(rotate[2]*constant2/constant22+" "+rotate[4]);
        /*if(rotate[4]==0){
            rotate[4]=rotate[2]*constant2/constant22;
            shoulderL.rotate(subset(new int[]{4,6}),0,rotate[4]);
        }
        if(rotate[5]==0){
            rotate[5]=rotate[3]*constant3/constant32;
            elbowL.rotate(subset(new int[]{6}),0,rotate[5]);
        }
        if(rotate[6]==0){
            rotate[6]=rotate[0]*constant2/constant22;
            shoulderR.rotate(subset(new int[]{5,7}),0,rotate[6]);
        }
        if(rotate[7]==0){
            rotate[7]=rotate[1]*constant3/constant32;
            elbowR.rotate(subset(new int[]{7}),0,rotate[7]);
        }*/
        shoulderL.rotate(subset(new int[]{4,6}),0,-10*constant2/constant*squareWave(walkTime+constant/2,constant));
        rotate[4]+=-10*constant2/constant*squareWave(walkTime+constant/2,constant);
        elbowL.rotate(subset(new int[]{6}),0,-10*constant3/constant*squareWave(walkTime,constant));
        rotate[5]+=-10*constant3/constant*squareWave(walkTime,constant);
        shoulderR.rotate(subset(new int[]{5,7}),0,-10*constant2/constant*squareWave(walkTime+constant/2,constant));
        rotate[6]+=-10*constant2/constant*squareWave(walkTime+constant/2,constant);
        elbowR.rotate(subset(new int[]{7}),0,10*constant3/constant*squareWave(walkTime,constant));
        rotate[7]+=10*constant3/constant*squareWave(walkTime,constant);
    }
    public void endWalk(){
        if(walkTime>=0){
            kneeL.rotate(subset(new int[]{13}),0,-rotate[1]);
            hipL.rotate(subset(new int[]{11,13}),0,-rotate[0]);
            kneeR.rotate(subset(new int[]{14}),0,-rotate[3]);
            hipR.rotate(subset(new int[]{12,14}),0,-rotate[2]);
            if(attackTime==-1){
                elbowL.rotate(subset(new int[]{6}),0,-rotate[5]);
                shoulderL.rotate(subset(new int[]{4,6}),0,-rotate[4]);
                elbowR.rotate(subset(new int[]{7}),0,-rotate[7]);
                shoulderR.rotate(subset(new int[]{5,7}),0,-rotate[6]);
            }
            rotate=new double[8];
            walkTime=-1;
        }
    }
    public void executeWalk(int walkTime,int num){
        double[]current=movePattern[walkTime%4];
        int cycle=(walkTime/4)%2;
        //System.out.println(walkTime+" "+cycle);
        if(num==-1){
            if(cycle==0){
                current=multiply(current,-1);
                kneeR.rotate(subset(new int[]{14}),0,current[3]);
                hipR.rotate(subset(new int[]{12,14}),0,current[2]);
                kneeL.rotate(subset(new int[]{13}),0,current[1]);
                hipL.rotate(subset(new int[]{11,13}),0,current[0]);
            }else{
                kneeL.rotate(subset(new int[]{14}),0,current[3]);
                hipL.rotate(subset(new int[]{12,14}),0,current[2]);
                kneeR.rotate(subset(new int[]{13}),0,current[1]);
                hipR.rotate(subset(new int[]{11,13}),0,current[0]);
            }
            
        }else{
            if(cycle==1){
                current=multiply(current,-1);
                hipR.rotate(subset(new int[]{11,13}),0,current[0]);
                kneeR.rotate(subset(new int[]{13}),0,current[1]);
                hipL.rotate(subset(new int[]{12,14}),0,current[2]);
                kneeL.rotate(subset(new int[]{14}),0,current[3]);
            }else{
                hipL.rotate(subset(new int[]{11,13}),0,current[0]);
                kneeL.rotate(subset(new int[]{13}),0,current[1]);
                hipR.rotate(subset(new int[]{12,14}),0,current[2]);
                kneeR.rotate(subset(new int[]{14}),0,current[3]);
            }
        }
    }
    public double[] multiply(double[]a,int num){
        double[]temp=new double[a.length];
        for(int i=0;i<a.length;i++){
            temp[i]=a[i]*num;
        }
        return temp;
    }
    public void restPosition(){
        if(walkTime!=-1){

        }
    }
    public void create(){
        t=new ArrayList<Triangle>();
        c=new ArrayList<Cylinder>();
        s=new ArrayList<Sphere>();
        for(int i=0;i<joints.size();i++){
            //joints.set(i,joints.get(i).multiply(scale).plus(pos));
            //s.add(new Sphere(joints.get(i).multiply(scale).plus(pos),joints.get(i).getSize()));
            if(joints.get(i).getSphere()){
                s.add(joints.get(i).getSphere(pos,scale,r,g,b));
            }
        }
        if(texture!=-1){
            for(int i=0;i<limbs.size();i++){
                c.add(limbs.get(i).getCylinder(pos,scale,texture));
            }
        }else{
            for(int i=0;i<limbs.size();i++){
                c.add(limbs.get(i).getCylinder(pos,scale,r,g,b));
            }
        }
        for(int i=0;i<c.size();i++){
            t.addAll(c.get(i).getTriangles());
        }
        sword.create();
        if(sword!=null){
            t.addAll(sword.getTriangles());
            s.addAll(sword.getSpheres());
        }
    }
    public void updatePosition(Vector v){
        pos.set(pos.plus(v));
        bounds.move(v);
    }
    public void setPosition(Vector v){
        bounds.move(v.minus(pos));
        pos.set(v);
    }
    public void setZ(double depth){
        bounds.moveZ(depth-pos.z());
        this.pos.z(depth);
    }
    public RectPrism getBounds(){
        return bounds;
    }
    public ArrayList<Sphere>getSpheres(){
        return s;
    }
    public ArrayList<Triangle>getTriangles(){
        return t;
    }
    public Vector getHead(){
        return head.multiply(scale).plus(pos);
    }
    public void move(Vector move){
        updatePosition(move);
        doWalkingAnimation();
    }
    public void move(double speed){
        updatePosition(getMoveDir().multiply(speed));
    }
    public void move(double speed,int num,ArrayList<RectPrism>walls){
        speed=speed/num;
        for(int i=0;i<num;i++){
            updatePosition(getMoveDir().multiply(speed));
            if(colliding(walls)){
                updatePosition(getMoveDir().multiply(-speed));
                return;
            }
        }
    }
    public void moveZ(double del){
        bounds.moveZ(del);
        this.pos.z(this.pos.z()+del);
    }
    public double getZ(){
        return this.pos.z();
    }
    public double getMapZ(Map m){
        return m.getZ(this.pos.x(),this.pos.y());
    }
    public void moveOnMap(double speed,Map m,int num,ArrayList<RectPrism>walls){
        speed=speed/num;
        for(int i=0;i<num;i++){
            Triangle onTriangle=m.getTriangle(pos.x(),pos.y());
            if(onTriangle!=null){
                Vector original=new Vector(pos.x(),pos.y(),pos.z());
                double zDel=getMoveDir().dot(onTriangle.getGrad());
                Vector move=new Vector(getMoveDir().x(),getMoveDir().y(),zDel).unit();
                updatePosition(move.multiply(speed));
                setZ(getMapZ(m));
                if(colliding(walls)){
                    setPosition(original);
                    return;
                }
            }else{
                Vector move=new Vector(getMoveDir().x(),getMoveDir().y(),0.0);
                updatePosition(move.multiply(speed));
                if(colliding(walls)){
                    updatePosition(move.multiply(-speed));
                    return;
                }
            }
        }
    }
    public boolean colliding(ArrayList<RectPrism>walls){
        for(int i=0;i<walls.size();i++){
            if(walls.get(i).getNear()&&walls.get(i).colliding(bounds)){
                return true;
            }
        }
        return false;
    }
    public Vector getPos(){
        return pos;
    }
    public boolean turn(double theta,ArrayList<RectPrism>walls){
        boolean value=true;
        neck.rotate(joints,0,theta);
        bounds=new RectPrism(bound.multiply(scale).plus(pos),boundX.minus(bound).multiply(scale),boundY.minus(bound).multiply(scale),boundZ.minus(bound).multiply(scale));
        if(colliding(walls)){
            value=false;
            neck.rotate(joints,0,-theta);
            bounds=new RectPrism(bound.multiply(scale).plus(pos),boundX.minus(bound).multiply(scale),boundY.minus(bound).multiply(scale),boundZ.minus(bound).multiply(scale));
        }
        create();
        return value;
    }
    public void turn(double theta){
        neck.rotate(joints,0,theta);
        bounds=new RectPrism(bound.multiply(scale).plus(pos),boundX.minus(bound).multiply(scale),boundY.minus(bound).multiply(scale),boundZ.minus(bound).multiply(scale));
        create();
    }
    public Vector getMoveDir(){
        return neck.getRotate().get(1);
    }
}