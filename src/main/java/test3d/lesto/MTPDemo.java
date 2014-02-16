
// import all necessary classes
import java.util.Vector;

import org.ode4j.math.DMatrix3;
import org.ode4j.math.DVector3;
import org.ode4j.ode.DContactJoint;
import org.ode4j.ode.OdeHelper;
import org.ode4j.ode.DBody;
import org.ode4j.ode.DContact;
import org.ode4j.ode.DContactBuffer;
import org.ode4j.ode.DGeom;
import org.ode4j.ode.DJointGroup;
import org.ode4j.ode.DJoint;
import org.ode4j.ode.DSpace;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.DSphere;
import org.ode4j.ode.DPlane;
import org.ode4j.ode.DGeom.DNearCallback;
import org.ode4j.ode.OdeMath;
import org.ode4j.ode.OdeMath.*;
import org.ode4j.math.DMatrix3C;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.internal.joints.DxJoint;
import org.ode4j.ode.internal.DxWorld;

/*
This script is a modified version of one of the demo classes in the Ode4J package. 
I'm not sure which one I started with though.
*/

// definition_start
class MPTDemo{
    
    public Vector aoiobjects = new Vector();;
    public boolean run;
    float sim_time;
    float sim_duration; 
    
    // some constants
    public final int MAXNUM = 500;        // max number of objects
    public final double DENSITY = (2);        // density of all objects
    public final int GPB = 3;            // maximum number of geometries per body
    public final int MAX_CONTACTS = 8;          // maximum number of contact points per body
    public final int MAX_FEEDBACKNUM = 20;
    public final double GRAVITY = 9.8f;
    public final boolean USE_GEOM_OFFSET = true;
    public final float STEP_INTERVAL = 1f/25f;
    
    // dynamics and collision objects
    public MyObject(){
        
        public DBody body;            // the body
        public DGeom[] geom = new DGeom[GPB];    // geometries representing this body
        return this;
    }

    public int num = 2;        // number of objects in simulation
    public int frame_num;
    public DWorld world = null;
    public DSpace space = null;
    public DJointGroup contactgroup = null;
    
    // create an array to hold bsh objects
    Object[] obj = new Object[num];
    
    for (i=0; i<obj.length; i++){
            
        obj[i] = null;
    }
       
    // some flags
    public  int selected = -1;                      // selected object
    public  boolean random_pos = true;              // drop objects from random position?
 
    public DNearCallback nearCallback = new DNearCallback() {
            
        // override the call method of DNearCallback
        public void call(Object data, DGeom o1, DGeom o2) {
            
            nearCallback(data, o1, o2);
        }
    };

    // this is called by dSpaceCollide when two objects in space are
    // potentially colliding.
   
    public void nearCallback(Object data, DGeom o1, DGeom o2) {

        // exit without doing anything if the two bodies are connected by a joint
        DBody b1 = o1.getBody();
        DBody b2 = o2.getBody();
        
        if (b1 != null && b2 != null && OdeHelper.areConnected(b1, b2)) {
            
            return;
        }
        
        // make a list for potential contacts and initialize contact attributes
        DContactBuffer contacts = new DContactBuffer(MAX_CONTACTS);
        
        for (i = 0; i < MAX_CONTACTS; i++) {
            
            contact = contacts.get(i);
            contact.surface.mode = OdeMath.dContactBounce | OdeMath.dContactSoftCFM ;       
            contact.surface.mu = 10000000;
            contact.surface.mu2 = 100;
            contact.surface.bounce = 0.98;
            contact.surface.bounce_vel = 0.1;
            contact.surface.soft_cfm = 0.01;
        }
   
        int numc = OdeHelper.collide(o1, o2, MAX_CONTACTS, contacts.getGeomBuffer());
        
        if (numc != 0) {
            
            for (i = 0; i < numc; i++) {
                    
                DJoint c = OdeHelper.createContactJoint(world, contactgroup, contacts.get(i));
                c.attach(b1, b2);            
            }          
        }
    }
   
    public void start() {
            
        frame_num = 0;
        run = true;
        sim_duration = 20f; // 20 seconds
        sim_time = 0f; // start time
        OdeHelper.initODE2(0);
        makeWorld();
        makeBodies();
    }

    /********************/
    public void demo() {       
            
        // setup world, bodies and geoms
        start();
        // run the simulation
        simLoop();
        // cleanup the world, bodies and geoms
        stop();      
    }
    /*******************/

    public void stop() {
            
        contactgroup.destroy();
        space.destroy();
        world.destroy();
        OdeHelper.closeODE();
    }

    public void simLoop() {
            
        // need some objects in the aoi scene so that we can add animation track keyframes to them
        makeAoIBodyCollection();
        
        while (run) {
            
            if (sim_time > sim_duration){
                    
                run = false;
            }
            
            space.collide(null, nearCallback);
            world.step(STEP_INTERVAL);
            updateAoIBodies();
            contactgroup.empty();
            sim_time += STEP_INTERVAL;
        }
        putBodiesIntoAoIScene();
    }

    public void makeBodies() {
            
        for (int i =0; i < num; i++){
            
            if (i > MAXNUM){
                    
                System.out.println("error, you have made more objects than NUMC states is the max");
                return;
            }
            
            double[] sides = new double[3];
            m = OdeHelper.createMass();
            boolean setBody;
            // we are creating (dropping) another object
            setBody = false;
            
            // create a body
            obj[i].body = OdeHelper.createBody(world);
            
            for (int k = 0; k < 3; k++) {
                    
                sides[k] = OdeMath.dRandReal() * 0.5 + 0.1;
            }

            DMatrix3 R = new DMatrix3();
            
            if (random_pos) {
                    
                obj[i].body.setPosition(
                        OdeMath.dRandReal() * 2 - 1, 
                        OdeMath.dRandReal() + 10, 
                        OdeMath.dRandReal() + 5
                );
                OdeMath.dRFromAxisAndAngle(
                        R, 
                        OdeMath.dRandReal() * 2.0 - 1.0, 
                        OdeMath.dRandReal() * 2.0 - 1.0,
                        OdeMath.dRandReal() * 2.0 - 1.0, 
                        OdeMath.dRandReal() * 10.0 - 5.0
                );
            } else {
                    
                double maxheight = 0;
                for (int k = 0; k < num; k++) {
                    
                    final DVector3C pos = obj[k].body.getPosition();
                    
                    if (pos.get2() > maxheight) {
                            
                        maxheight = pos.get2();
                    }
                }
                
                obj[i].body.setPosition(0, 0, maxheight + 1);
                R.setIdentity();
            }
            obj[i].body.setRotation(R);
            obj[i].body.setData(i);//(void*) i);
            sides[0] *= 1.1;
            m.setSphere(DENSITY, sides[0]);
            obj[i].geom[0] = OdeHelper.createSphere(space, sides[0]);
            obj[i].body.setMass(m);

            if (!setBody) {
                    
                for (int k = 0; k < GPB; k++) {
                    
                    if (obj[i].geom[k] != null) {
                            
                        obj[i].geom[k].setBody(obj[i].body);
                    }
                }
            }
            
            obj[i].body.setMass(m);
        }   
    }

    public void makeWorld(){
            
        OdeHelper.initODE2(0);
        world = OdeHelper.createWorld();
        space = OdeHelper.createHashSpace(null);
        contactgroup = OdeHelper.createJointGroup();
        world.setGravity(0, 0, -GRAVITY);
        world.setCFM(0.01);
        world.setAutoDisableFlag(true);
        world.setLinearDamping(0);
        world.setAngularDamping(0.05);
        world.setMaxAngularSpeed(200);
        world.setContactMaxCorrectingVel(0.1);
        world.setContactSurfaceLayer(0.01);
        OdeHelper.createPlane(space, 0, 0, 1, 0);     
        System.out.println("number of bodies: " + obj.length);
        
        for (int i = 0; i < obj.length; i++) {
            
            obj[i] = MyObject();          
        }     
    }

    public void putBodiesIntoAoIScene(){
        // no checking done, but scene must not contain any existing spheres!
        
        for (int i = 0; i < obj.length; i++){
            
            aoiobjinfo = aoiobjects.elementAt(i);
            window.addObject(aoiobjinfo, new UndoRecord(window, false));
       }
    }

    public void makeAoIBodyCollection(){
            
        for (int i = 0; i < obj.length; i++){
            
            ob = obj[i];
            cs = new CoordinateSystem(
                new Vec3(
                    ob.body.getPosition().get0(), 
                    ob.body.getPosition().get1(),
                    ob.body.getPosition().get2()
                ),
                0,
                0,
                0
            );
            
            g = (DSphere)ob.geom[0];
            r = g.getRadius();
            aoiobj3d = new Sphere(r,r,r);
            String name = "ode_object" + i;
            aoiobjinfo = new ObjectInfo(aoiobj3d, cs, name);
            aoiobjinfo.addTrack(new RotationTrack(aoiobjinfo),0);
            aoiobjinfo.addTrack(new PositionTrack(aoiobjinfo),0);
            aoiobjects.add(aoiobjinfo);
       }
    }

    public void updateAoIBodies(){
            
        frame_num += 1;
        
        if (frame_num%4 == 0){
            
            for (int i = 0; i < obj.length; i++){
                    
                // get the simulation time
                double time = sim_time;

                // get reference to the ode body
                ob = obj[i];

                // get the position of the body
                double d0 = ob.body.getPosition().get0(); 
                double d1 = ob.body.getPosition().get1(); 
                double d2 = ob.body.getPosition().get2();
                // get the rotation of the body
                rotmatrix = ob.body.getRotation();

                // get the aoi body
                aoibody = (ObjectInfo)aoiobjects.get(i);
               
                // get the position track of aoi body
                postrack = aoibody.getTracks()[0]; 
                
                // make an aoi position keyframe for the current time
                kp = new VectorKeyframe(d0,d2,d1);// swap x,y
                
                // add a keyframe to the position track
                postrack.setKeyframe(time,kp,new Smoothness(1));

                m00 = rotmatrix.get00();
                m01 = rotmatrix.get01();
                m02 = rotmatrix.get02();

                m10 = rotmatrix.get10();
                m11 = rotmatrix.get11();
                m12 = rotmatrix.get12();

                m20 = rotmatrix.get20();
                m21 = rotmatrix.get21();
                m22 = rotmatrix.get22();
                
                // make some AoI Mat4 objects from the rotation matrix, need to play around 
                // with transforms between the aoi and ode4j coordinate systems

                m4 = new Mat4(
                                m00,  m10,  m20,  0, 
                                m01,  m11,  m21,  0,
                                m02,  m12,  m22,  0,
                                  0,    0,    0,  1
                );


                m4a = new Mat4(
                                m00,  m01,  m02,  0, 
                                m10,  m11,  m12,  0,
                                m20,  m21,  m22,  0,
                                  0,    0,    0,  1
                );

                rotx90 = new Mat4(
                                1,  0,  0,  0, 
                                0,  0, -1,  0,
                                0,  1,  0,  0,
                                0,  0,  0,  1
                );

                irotx90 = new Mat4(
                                1,  0,   0,  0, 
                                0,  0,  1,  0,
                                0, -1,   0,  0,
                                0,  0,   0,  1
                );


                // make a new AoI coordinate system
                cs = new CoordinateSystem();

                // apply ode rotation transform  to this cs
                cs.transformAxes(m4);

                // get the rotation track of aoi body
                rottrack = aoibody.getTracks()[1];

                // get the rotations from the transformed cs
                rotangles = cs.getRotationAngles();

                // make a keyframe with the rotation angles
                kr = new RotationKeyframe(rotangles[0],rotangles[2],rotangles[1]);

                // apply rotation feyframe to rotation track
                rottrack.setKeyframe(time,kr,new Smoothness(1));
                rottrack.setUseQuaternion(true);
            }
        }
    }
    return this;
}
// definition_end

// execution_start
doit = MPTDemo();
doit.demo();
print("DONE");
// execution_end