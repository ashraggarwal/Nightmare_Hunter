import java.util.ArrayList;
import java.io.Serializable;
public class Monster implements Shape,Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<Triangle> t;
    private ArrayList<Joint>joints;
    private ArrayList<Limb>limbs;
    private ArrayList<Sphere>s;
    private ArrayList<Sword>swords;
    private ArrayList<Cylinder>c;
    private Joint center;
    private double scale;
    private int bodyLength,waistLength,r,g,b,skinTexture;
    private Joint head;
    private ArrayList<Joint>spine,waist,shoulders,elbows,hips,knees,feet;
    private ArrayList<Hand>hands;
    private double[][]rotate;
    private int walkTime=-1;
    private int attackTime=-1;
    private Vector pos;
    private Trackable tracking;
    private double attackPower=50.0,health=1000,originalHealth=health;
    public Monster(Vector pos,double scale,int bodyLength,int waistLength,int r,int g,int b,int skinTexture){
        this.pos=pos;
        this.center=new Joint(0.0,0.0,0.0);
        this.center.addRotate(new Vector(0.0,0.0,1.0));
        this.scale=scale;
        this.bodyLength=bodyLength;
        this.waistLength=waistLength;
        rotate=new double[waistLength][8];
        this.r=r;
        this.g=g;
        this.b=b;
        this.skinTexture=skinTexture;
        spine=new ArrayList<Joint>();
        waist=new ArrayList<Joint>();
        shoulders=new ArrayList<Joint>();
        elbows=new ArrayList<Joint>();
        hips=new ArrayList<Joint>();
        knees=new ArrayList<Joint>();
        feet=new ArrayList<Joint>();
        hands=new ArrayList<Hand>();
        joints=new ArrayList<Joint>();
        limbs=new ArrayList<Limb>();
        swords=new ArrayList<Sword>();
        double legSize=0.3;
        double spineSize=0.4;
        double hSize=0.3;
        double waistSize=0.8;
        if(bodyLength==0){
            spineSize=0.05;
            legSize+=(spineSize-0.05)/2;
            hSize+=(spineSize-0.05)/2;
        }
        //double size=0.05*0.8;
        double size=0.05*0.8;
        double size2=0.03;
        double armSpace=0.06;
        double maxWidth=-0.12-armSpace*(bodyLength-1);
        double bellyWidth=0.95;
        double bellyDepth=1.2;
        double waistWidth=0.95;
        double waistDepth=3.0;
        double hipSize=1.5;
        head=new Joint(0.0,0.0,1.0-hSize*2/5,scale*hSize*2/5);
        for(int i=0;i<waistLength;i++){
            double push=0;
            if(waistLength!=1){
                push=i*(i-waistLength+1)*4*maxWidth/((waistLength-1)*(waistLength-1));
            }
            feet.add(new Joint(-0.06-push,waistSize/waistLength*i,legSize*0.1,scale*size));
            feet.add(new Joint(0.06+push,waistSize/waistLength*i,legSize*0.1,scale*size));
            knees.add(new Joint(-0.06-push,waistSize/waistLength*i,legSize*0.5,scale*size));
            knees.add(new Joint(0.06+push,waistSize/waistLength*i,legSize*0.5,scale*size));
            hips.add(new Joint(-0.06-push,waistSize/waistLength*i,legSize,scale*size*hipSize));
            hips.add(new Joint(0.06+push,waistSize/waistLength*i,legSize,scale*size*hipSize));
            waist.add(new Joint(0.0,waistSize/waistLength*i,legSize));
            waist.get(i).addRotate(hips.get(2*i).minus(waist.get(i)).multiply(waistWidth));
            waist.get(i).addRotate(new Vector(0.0,0.0,size2*1.2*waistDepth));
            waist.get(i).addRotate(new Vector(0.0,size2,0.0));
        }
        for(int i=0;i<bodyLength;i++){
            spine.add(new Joint(0.0,0.0,legSize+spineSize/bodyLength*(i+1)));
            shoulders.add(new Joint(-0.12-armSpace*i,0.0,legSize+spineSize/bodyLength*(i+1),scale*size));
            shoulders.add(new Joint(0.12+armSpace*i,0.0,legSize+spineSize/bodyLength*(i+1),scale*size));
            elbows.add(new Joint(-0.16-armSpace*i,0.0,legSize+spineSize*0.5/bodyLength*(i+1),scale*size));
            elbows.add(new Joint(0.16+armSpace*i,0.0,legSize+spineSize*0.5/bodyLength*(i+1),scale*size));
            hands.add(new Hand(-0.16-armSpace*i,0.0,legSize*0.8+spineSize*0.1/bodyLength*(i+1),scale*size,new Vector(-1.0,0.0,0.0),new Vector(0.0,0.0,-1.0),new Vector(0.0,-1.0,0.0)));
            hands.add(new Hand(0.16+armSpace*i,0.0,legSize*0.8+spineSize*0.1/bodyLength*(i+1),scale*size,new Vector(-1.0,0.0,0.0),new Vector(0.0,0.0,-1.0),new Vector(0.0,-1.0,0.0)));
            spine.get(i).addRotate(shoulders.get(2*i).minus(spine.get(i)).multiply(bellyWidth));
            spine.get(i).addRotate(new Vector(0.0,size2*1.2*bellyDepth,0.0));
        }
        for(int i=0;i<bodyLength;i++){
            if(i==bodyLength-1){
                limbs.add(new Limb(head,spine.get(i),size2));
            }else{
                double skew=spine.get(i+1).getRotate().get(0).mag()/spine.get(i).getRotate().get(0).mag();
                limbs.add(new Limb(spine.get(i),spine.get(i+1),spine.get(i).getRotate().get(0),spine.get(i).getRotate().get(1),skew,1.0));
            }
            limbs.add(new Limb(shoulders.get(2*i),spine.get(i),size2));
            limbs.add(new Limb(shoulders.get(2*i+1),spine.get(i),size2));
            limbs.add(new Limb(shoulders.get(2*i),elbows.get(2*i),size2));
            limbs.add(new Limb(shoulders.get(2*i+1),elbows.get(2*i+1),size2));
            limbs.add(new Limb(elbows.get(2*i),hands.get(2*i),size2));
            limbs.add(new Limb(elbows.get(2*i+1),hands.get(2*i+1),size2));
        }
        for(int i=0;i<waistLength;i++){
            if(i==0){
                if(bodyLength==0){
                    limbs.add(new Limb(head,waist.get(0),size2));
                }else{
                    double skew=waist.get(0).getRotate().get(0).mag()/spine.get(0).getRotate().get(0).mag();
                    limbs.add(new Limb(spine.get(0),waist.get(0),spine.get(0).getRotate().get(0),spine.get(0).getRotate().get(1),skew,1.0));
                }
            }else{
                double skew=waist.get(i-1).getRotate().get(0).mag()/waist.get(i).getRotate().get(0).mag();
                limbs.add(new Limb(waist.get(i),waist.get(i-1),waist.get(i).getRotate().get(1),waist.get(i).getRotate().get(0),1.0,skew));
            }
            limbs.add(new Limb(hips.get(2*i),waist.get(i),waist.get(i).getRotate().get(1),waist.get(i).getRotate().get(2)));
            limbs.add(new Limb(hips.get(2*i+1),waist.get(i),waist.get(i).getRotate().get(2),waist.get(i).getRotate().get(1)));
            limbs.add(new Limb(hips.get(2*i),knees.get(2*i),size2));
            limbs.add(new Limb(hips.get(2*i+1),knees.get(2*i+1),size2));
            limbs.add(new Limb(knees.get(2*i),feet.get(2*i),size2));
            limbs.add(new Limb(knees.get(2*i+1),feet.get(2*i+1),size2));
        }
        for(int i=0;i<hands.size();i++){
            swords.add(new Sword(hands.get(i),scale,pos,scale*0.6,1.4,13,12));
        }
        joints.add(head);
        joints.addAll(feet);
        joints.addAll(waist);
        joints.addAll(knees);
        joints.addAll(hands);
        joints.addAll(elbows);
        joints.addAll(shoulders);
        joints.addAll(hips);
        joints.addAll(spine);
        create();
    }
    public Monster(Vector pos,double scale,int bodyLength,int waistLength,double legSize,double hSize,double waistSize,int r,int g,int b,int skinTexture){
        this.pos=pos;
        this.center=new Joint(0.0,0.0,0.0);
        this.center.addRotate(new Vector(0.0,0.0,1.0));
        this.scale=scale;
        this.bodyLength=bodyLength;
        this.waistLength=waistLength;
        rotate=new double[waistLength][8];
        this.r=r;
        this.g=g;
        this.b=b;
        this.skinTexture=skinTexture;
        spine=new ArrayList<Joint>();
        waist=new ArrayList<Joint>();
        shoulders=new ArrayList<Joint>();
        elbows=new ArrayList<Joint>();
        hips=new ArrayList<Joint>();
        knees=new ArrayList<Joint>();
        feet=new ArrayList<Joint>();
        hands=new ArrayList<Hand>();
        joints=new ArrayList<Joint>();
        limbs=new ArrayList<Limb>();
        swords=new ArrayList<Sword>();
        double spineSize=1-legSize-hSize;
        if(bodyLength==0){
            legSize+=(spineSize)/2;
            hSize+=(spineSize)/2;
            spineSize=0.0;
        }
        //double size=0.05*0.8;
        double size=0.05*0.8;
        double size2=0.03;
        double armSpace=0.06;
        double maxWidth=-0.12-armSpace*(bodyLength-1);
        double bellyWidth=0.95;
        double bellyDepth=1.2;
        double waistWidth=0.95;
        double waistDepth=3.0;
        double hipSize=1.5;
        double shift=0;
        if(waistLength!=1){
            shift=waistSize/2;
        }
        head=new Joint(0.0,-shift,1.0-hSize/2,scale*hSize/2);
        //System.out.println(legSize+" "+spineSize+" "+hSize+" "+bodyLength);
        for(int i=0;i<waistLength;i++){
            double push=0;
            if(waistLength!=1){
                push=i*(i-waistLength+1)*4*maxWidth/((waistLength-1)*(waistLength-1));
            }
            feet.add(new Joint(-0.06-push,waistSize/waistLength*i-shift,legSize*0.1,scale*size));
            feet.add(new Joint(0.06+push,waistSize/waistLength*i-shift,legSize*0.1,scale*size));
            knees.add(new Joint(-0.06-push,waistSize/waistLength*i-shift,legSize*0.5,scale*size));
            knees.add(new Joint(0.06+push,waistSize/waistLength*i-shift,legSize*0.5,scale*size));
            hips.add(new Joint(-0.06-push,waistSize/waistLength*i-shift,legSize,scale*size*hipSize));
            hips.add(new Joint(0.06+push,waistSize/waistLength*i-shift,legSize,scale*size*hipSize));
            waist.add(new Joint(0.0,waistSize/waistLength*i-shift,legSize));
            waist.get(i).addRotate(hips.get(2*i).minus(waist.get(i)).multiply(waistWidth));
            waist.get(i).addRotate(new Vector(0.0,0.0,size2*1.2*waistDepth));
            waist.get(i).addRotate(new Vector(0.0,size2,0.0));
            hips.get(2*i).addRotate(new Vector(1.0,0.0,0.0));
            hips.get(2*i+1).addRotate(new Vector(-1.0,0.0,0.0));
            knees.get(2*i).addRotate(new Vector(1.0,0.0,0.0));
            knees.get(2*i+1).addRotate(new Vector(-1.0,0.0,0.0));
        }
        for(int i=0;i<bodyLength;i++){
            spine.add(new Joint(0.0,-shift,legSize+spineSize/bodyLength*(i+1)));
            shoulders.add(new Joint(-0.12-armSpace*i,-shift,legSize+spineSize/bodyLength*(i+1),scale*size));
            shoulders.get(2*i).addRotate(new Vector(1.0,0.0,0.0));
            shoulders.get(2*i).addRotate(new Vector(0.0,1.0,0.0));
            shoulders.add(new Joint(0.12+armSpace*i,-shift,legSize+spineSize/bodyLength*(i+1),scale*size));
            shoulders.get(2*i+1).addRotate(new Vector(-1.0,0.0,0.0));
            shoulders.get(2*i+1).addRotate(new Vector(0.0,1.0,0.0));
            elbows.add(new Joint(-0.16-armSpace*i,-shift,legSize+spineSize*0.5/bodyLength*(i+1),scale*size));
            elbows.add(new Joint(0.16+armSpace*i,-shift,legSize+spineSize*0.5/bodyLength*(i+1),scale*size));
            elbows.get(2*i).addRotate(new Vector(1.0,0.0,0.0));
            elbows.get(2*i+1).addRotate(new Vector(-1.0,0.0,0.0));
            hands.add(new Hand(-0.16-armSpace*i,-shift,legSize*0.5+spineSize*0.1/bodyLength*(i+1),scale*size,new Vector(-1.0,0.0,0.0),new Vector(0.0,0.0,-1.0),new Vector(0.0,-1.0,0.0)));
            hands.add(new Hand(0.16+armSpace*i,-shift,legSize*0.5+spineSize*0.1/bodyLength*(i+1),scale*size,new Vector(-1.0,0.0,0.0),new Vector(0.0,0.0,-1.0),new Vector(0.0,-1.0,0.0)));
            spine.get(i).addRotate(shoulders.get(2*i).minus(spine.get(i)).multiply(bellyWidth));
            spine.get(i).addRotate(new Vector(0.0,size2*1.2*bellyDepth,0.0));
        }
        for(int i=0;i<bodyLength;i++){
            if(i==bodyLength-1){
                limbs.add(new Limb(head,spine.get(i),size2));
            }else{
                double skew=spine.get(i+1).getRotate().get(0).mag()/spine.get(i).getRotate().get(0).mag();
                limbs.add(new Limb(spine.get(i),spine.get(i+1),spine.get(i).getRotate().get(1),spine.get(i).getRotate().get(0),1.0,skew));
            }
            limbs.add(new Limb(shoulders.get(2*i),spine.get(i),size2));
            limbs.add(new Limb(shoulders.get(2*i+1),spine.get(i),size2));
            limbs.add(new Limb(shoulders.get(2*i),elbows.get(2*i),size2));
            limbs.add(new Limb(shoulders.get(2*i+1),elbows.get(2*i+1),size2));
            limbs.add(new Limb(elbows.get(2*i),hands.get(2*i),size2));
            limbs.add(new Limb(elbows.get(2*i+1),hands.get(2*i+1),size2));
        }
        for(int i=0;i<waistLength;i++){
            if(i==0){
                if(bodyLength==0){
                    limbs.add(new Limb(head,waist.get(0),size2));
                }else{
                    double skew=waist.get(0).getRotate().get(0).mag()/spine.get(0).getRotate().get(0).mag();
                    limbs.add(new Limb(spine.get(0),waist.get(0),spine.get(0).getRotate().get(0),spine.get(0).getRotate().get(1),skew,1.0));
                }
            }else{
                double skew=waist.get(i-1).getRotate().get(0).mag()/waist.get(i).getRotate().get(0).mag();
                limbs.add(new Limb(waist.get(i),waist.get(i-1),waist.get(i).getRotate().get(1),waist.get(i).getRotate().get(0),1.0,skew));
            }
            limbs.add(new Limb(hips.get(2*i),waist.get(i),waist.get(i).getRotate().get(1),waist.get(i).getRotate().get(2)));
            limbs.add(new Limb(hips.get(2*i+1),waist.get(i),waist.get(i).getRotate().get(2),waist.get(i).getRotate().get(1)));
            limbs.add(new Limb(hips.get(2*i),knees.get(2*i),size2));
            limbs.add(new Limb(hips.get(2*i+1),knees.get(2*i+1),size2));
            limbs.add(new Limb(knees.get(2*i),feet.get(2*i),size2));
            limbs.add(new Limb(knees.get(2*i+1),feet.get(2*i+1),size2));
        }
        for(int i=0;i<hands.size();i++){
            swords.add(new Sword(hands.get(i),scale,pos,scale*1.2,1.0,13,12));
        }
        joints.add(head);
        joints.addAll(feet);
        joints.addAll(waist);
        joints.addAll(knees);
        joints.addAll(hands);
        joints.addAll(elbows);
        joints.addAll(shoulders);
        joints.addAll(hips);
        joints.addAll(spine);
        create();
    }
    public double getHealthRatio(){
        return health/originalHealth;
    }
    public boolean takeDamage(double damage){
        health-=damage;
        return health>=0;
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
        if(skinTexture!=-1){
            for(int i=0;i<limbs.size();i++){
                c.add(limbs.get(i).getCylinder(pos,scale,skinTexture));
            }
        }else{
            for(int i=0;i<limbs.size();i++){
                c.add(limbs.get(i).getCylinder(pos,scale,r,g,b));
            }
        }
        for(int i=0;i<c.size();i++){
            t.addAll(c.get(i).getTriangles());
        }
        for(int i=0;i<swords.size();i++){
            swords.get(i).create();
            t.addAll(swords.get(i).getTriangles());
            s.addAll(swords.get(i).getSpheres());
        }
    }
    public void doWalkingAnimation(){
        //System.out.println(kneeL.minus(footL).mag());
        //System.out.println(kneeR.minus(footR).mag());
        //shoulderL.rotate(subset(new int[]{4,6}),0,Math.PI/20);
        walkTime++;
        int period=4;
        for(int i=0;i<waistLength;i++){
            if(i%2==0){
                doWalkingAnimationLegs(i,hips.get(2*i),knees.get(2*i),feet.get(2*i),hips.get(2*i+1),knees.get(2*i+1),feet.get(2*i+1),period,0.15,0.11);
            }else{
                doWalkingAnimationLegs(i,hips.get(2*i+1),knees.get(2*i+1),feet.get(2*i+1),hips.get(2*i),knees.get(2*i),feet.get(2*i),period,-0.15,-0.11);
            }
        }
        //doWalkingAnimationArms(period,0.10,0.05);
    }
    public int squareWave(int x,int period){
        int time=x/period;
        return (time%2)*2-1;
    }
    public void doWalkingAnimationLegs(int j,Joint hipL,Joint kneeL,Joint footL,Joint hipR,Joint kneeR,Joint footR,int constant,double constant2,double constant3){
        if(walkTime==0){
            //hipL.rotate(subset(new int[]{11,13}),0,constant4);
            //hipR.rotate(subset(new int[]{12,14}),0,-constant4);
            for(int i=0;i<constant;i++){
                hipL.rotate(new Joint[]{kneeL,footL},0,10*constant2/constant*squareWave(i+constant/2,constant));
                kneeL.rotate(new Joint[]{footL},0,10*constant3/constant*squareWave(i,constant));
                rotate[j][0]+=10*constant2/constant*squareWave(i+constant/2,constant);
                rotate[j][1]+=10*constant3/constant*squareWave(i,constant);
            }
        }
        hipL.rotate(new Joint[]{kneeL,footL},0,10*constant2/constant*squareWave(walkTime+3*constant/2,constant));
        rotate[j][0]+=10*constant2/constant*squareWave(walkTime+3*constant/2,constant);
        kneeL.rotate(new Joint[]{footL},0,10*constant3/constant*squareWave(walkTime+constant,constant));
        rotate[j][1]+=10*constant3/constant*squareWave(walkTime+constant,constant);
        hipR.rotate(new Joint[]{kneeR,footR},0,-10*constant2/constant*squareWave(walkTime+constant/2,constant));
        rotate[j][2]+=-10*constant2/constant*squareWave(walkTime+constant/2,constant);
        kneeR.rotate(new Joint[]{footR},0,-10*constant3/constant*squareWave(walkTime,constant));
        rotate[j][3]+=-10*constant3/constant*squareWave(walkTime,constant);
    }
    public void attackAnimation(){
        int time1=4;
        int time2=4;
        int time3=4;
        int time4=4;
        int num=1;
        //endWalk();
        if(attackTime>=0){
            for(int j=0;j<shoulders.size();j++){
                Joint shoulderL=shoulders.get(j);
                Joint elbowL=elbows.get(j);
                Joint handL=hands.get(j);
                if(j%2==1){
                    num=-1;
                }
                int attackTime=this.attackTime-(j%2)*(time1+time2+time3);
                if(attackTime>=0){
                    if(attackTime<time1/*+(j%2)*time1*/){
                        shoulderL.rotate(new Joint[]{shoulderL,elbowL,handL},0,num*40*0.05/time1);
                        shoulderL.rotate(new Joint[]{elbowL,handL},1,-num*40*0.02/time1);
                        elbowL.rotate(new Joint[]{elbowL,handL},0,num*40*0.04/time1);
                        //elbowL.rotate(subset(new int[]{6}),1,0.01);
                    }else if(attackTime<(time1+time2/*+(j%2)*time1*/)){
                        elbowL.rotate(new Joint[]{elbowL,handL},0,-num*30*0.05/time2);
                        shoulderL.rotate(new Joint[]{elbowL,handL},1,num*30*0.06/time2);
                        shoulderL.rotate(new Joint[]{shoulderL,elbowL,handL},0,-num*30*0.05/time2);
                        handL.rotate(new Joint[]{handL},0,30*0.06/time2);
                    }else if(attackTime<(time1+time2+time3/*+(j%2)*time1*/)){
                        elbowL.rotate(new Joint[]{elbowL,handL},0,num*30*0.01/time3);
                        shoulderL.rotate(new Joint[]{shoulderL,elbowL,handL},0,num*30*0.04/time3);
                        elbowL.rotate(new Joint[]{handL},1,30*0.04/time3);
                        handL.rotate(new Joint[]{handL},0,-30*0.06/time2);
                        //shoulderL.rotate(subset(new int[]{4,6}),1,0.05);
                    }else if(attackTime<(time1+time2+time3+time4/*+(j%2)*time1*/)){
                        elbowL.rotate(new Joint[]{handL},1,-30*0.04/time4);
                        elbowL.rotate(new Joint[]{elbowL,handL},0,-num*30*(0.4/30)/time4);
                        shoulderL.rotate(new Joint[]{shoulderL,elbowL,handL},0,-num*30*(1.7/30)/time4);
                        shoulderL.rotate(new Joint[]{elbowL,handL},1,-num*30*(1.0/30)/time4);
                    }else{
                        if(j%2==1){
                            this.attackTime=-1;
                            return;
                        }
                    }
                }
            }
        }
        attackTime++;
    }
    public void startAttackAnimation(){
        if(attackTime==-1){
            attackTime++;
        }
    }
    public void doWalkingAnimationArms(int j,Joint shoulderL,Joint elbowL,Hand handL,Joint shoulderR,Joint elbowR,Hand handR,int constant,double constant2,double constant3){
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
        shoulderL.rotate(new Joint[]{elbowL,handL},0,-10*constant2/constant*squareWave(walkTime+constant/2,constant));
        rotate[j][4]+=-10*constant2/constant*squareWave(walkTime+constant/2,constant);
        elbowL.rotate(new Joint[]{handL},0,-10*constant3/constant*squareWave(walkTime,constant));
        rotate[j][5]+=-10*constant3/constant*squareWave(walkTime,constant);
        shoulderR.rotate(new Joint[]{elbowR,handR},0,-10*constant2/constant*squareWave(walkTime+constant/2,constant));
        rotate[j][6]+=-10*constant2/constant*squareWave(walkTime+constant/2,constant);
        elbowR.rotate(new Joint[]{handR},0,10*constant3/constant*squareWave(walkTime,constant));
        rotate[j][7]+=10*constant3/constant*squareWave(walkTime,constant);
    }
    public ArrayList<Sphere>getSpheres(){
        return s;
    }
    public ArrayList<Triangle>getTriangles(){
        return t;
    }
    public boolean colliding(ArrayList<RectPrism>walls){
        return false;
    }
    public void updatePosition(Vector v){
        //System.out.println("old "+pos);
        pos.set(pos.plus(v));
        //System.out.println("new "+pos);
        //bounds.move(v);
    }
    public void setPosition(Vector v){
        //bounds.move(v.minus(pos));
        pos.set(v);
    }
    public void setZ(double depth){
        //bounds.moveZ(depth-pos.z());
        this.pos.z(depth);
    }
    public Vector getMoveDir(){
        Vector v=waist.get(0).getRotate().get(2);
        return new Vector(v.x(),v.y(),0.0).unit().neg();
    }
    public double getMapZ2(Map m){
        double maxZ=0;
        for(int i=0;i<feet.size();i++){
            Vector temp2=feet.get(i).multiply(scale).plus(pos);
            double temp=m.getZ(temp2.x(),temp2.y());
            if(temp>maxZ){
                maxZ=temp;
            }
        }
        return maxZ;
    }
    public double getMapZ(Map m){
        double total=0;
        for(int i=0;i<feet.size();i++){
            Vector temp2=feet.get(i).multiply(scale).plus(pos);
            double temp=m.getZ(temp2.x(),temp2.y());
            total+=temp;
        }
        return total/feet.size();
    }
    public void doAttack(){
        
    }
    public void autoMove(double speed,Map m,int num,ArrayList<RectPrism>walls){
        doTracking();
        startAttackAnimation();
        attackAnimation();
        //turn(0.1);
        moveOnMap(speed,m,num,walls);
        doWalkingAnimation();
        create();
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
    public void setTracking(Trackable tracking){
        this.tracking=tracking;
    }
    public Trackable getTracking(){
        return tracking;
    }
    public void doTracking(){
        double handling=0.1;
        if(tracking!=null){
            Vector optimal=pos.minus(tracking.getPos());
            Vector current=getMoveDir();
            optimal.z(0.0);
            optimal=optimal.unit();
            double theta=Math.acos(current.dot(optimal));
            double determinant=current.x()*optimal.y()-current.y()*optimal.x();
            double factor=2.0/3;
            if(determinant>0){
                turn(Math.min(theta*factor,handling));
            }else{
                turn(-Math.min(theta*factor,handling));
            }
        }
    }
    public void hitPlayer(Player p){
        for(int i=0;i<swords.size();i++){
            if(swords.get(i).getHitBox().colliding(p.getBounds())){
                p.takeDamage(attackPower);
            }
        }
    }
    public ArrayList<RectPrism>getHitBoxes(){
        ArrayList<RectPrism>temp=new ArrayList<RectPrism>();
        for(int i=0;i<limbs.size();i++){
            temp.add(limbs.get(i).getHitBox(pos,scale));
        }
        return temp;
    }
    public Vector getPos(){
        return pos;
    }
    public void turn(double theta){
        center.rotate(joints,0,theta);
        //bounds=new RectPrism(bound.multiply(scale).plus(pos),boundX.minus(bound).multiply(scale),boundY.minus(bound).multiply(scale),boundZ.minus(bound).multiply(scale));
        //create();
    }
    public double getSquareDistance(Player p){
        Vector playerPos=p.getPos();
        return Math.pow(playerPos.x()-pos.x(),2)+Math.pow(playerPos.y()-pos.y(),2);
    }
}