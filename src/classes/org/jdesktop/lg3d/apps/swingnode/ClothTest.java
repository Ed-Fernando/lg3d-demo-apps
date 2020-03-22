/**
 * Project Looking Glass
 *
 * $RCSfile: ClothTest.java,v $
 *
 * Copyright (c) 2004, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * $Revision: 1.2 $
 * $Date: 2006-08-16 16:03:52 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.apps.swingnode;

import javax.vecmath.Vector3f;
//import java.util.Random;
import java.util.Enumeration;
import java.awt.GraphicsConfiguration;
import javax.vecmath.Point3f;
import javax.vecmath.Point3d;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.sg.GeometryArray;
import org.jdesktop.lg3d.sg.GeometryUpdater;
import org.jdesktop.lg3d.sg.IndexedTriangleStripArray;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.sg.TransformGroup;
import org.jdesktop.lg3d.sg.PolygonAttributes;
import org.jdesktop.lg3d.sg.Shape3D;

/**
 *
 * A really basic Cloth simulation using the Euler integrator
 *
 * @author paulby
 */
public class ClothTest extends BranchGroup {
    
    final int NUMROWS = 8;
    final int NUMCOLUMNS = 16;
    
    float clothWidth;
    float clothHeight;
    
    final int NUMVERTICES=((NUMCOLUMNS+1) * (NUMROWS+1));
    final int NUMFACES=((NUMCOLUMNS*NUMROWS) * 2);
    final float CLOTHMASS=1.2f;
    final float MASSPERFACE=(CLOTHMASS/(float) NUMFACES);
    float CSTEP;
    float RSTEP;
    final int NUMSTRUCTURALSPRINGS=(NUMCOLUMNS*(NUMROWS+1) + NUMROWS*(NUMCOLUMNS+1) + NUMCOLUMNS*NUMROWS*2);
    
    final float GRAVITY = -32.174f;
    private float springTensionConstant=500.0f*100f;
    private float springShearConstant=600.0f*100f;
    private float springDampingConstant=2.0f;
    //final float YOFFSET=120.0f;
    final float YOFFSET=0f;
    final float DRAGCOEFFICIENT=0.01f;
    final float KRESTITUTION=0.25f;
    final float FRICTIONFACTOR=0.5f;
    
    
    Particle[][] particles = new Particle[NUMROWS+1][NUMCOLUMNS+1];
    Spring[] structuralSprings = new Spring[NUMSTRUCTURALSPRINGS];
    float[] coords = new float[(NUMROWS+1)*(NUMCOLUMNS+1)*3];
    float[] texCoords = new float[(NUMROWS+1)*(NUMCOLUMNS+1)*2];
    
    float windForceFactor = 5f;
    Vector3f windVector = new Vector3f(0f,0f,0f);
    
    private IndexedTriangleStripArray geom;
    
    private Appearance app;
    private ClothBehavior clothBehavior;
    
    public ClothTest(float width, float height, Appearance app) {
        this.app = app;
        clothWidth = width;
        clothHeight = height;
        CSTEP=((float) clothWidth / (float) NUMCOLUMNS);
        RSTEP=((float) clothHeight / (float) NUMROWS);
        
        this.setCapability(BranchGroup.ALLOW_DETACH);
        this.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        this.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        clothBehavior=new ClothBehavior(this);
    }
    
    public void setSize(float width,
            float height,
            float textureWidthScale,
            float textureHeightScale) {
        clothWidth = width;
        clothHeight = height;
        CSTEP=((float) clothWidth / (float) NUMCOLUMNS);
        RSTEP=((float) clothHeight / (float) NUMROWS);
        if (numChildren()>1)
            removeChild(1);
        init(textureWidthScale, textureHeightScale);
        addChild(createCloth());
    }
    
    private BranchGroup createCloth() {
        int indexCount=(NUMCOLUMNS+1)*2*(NUMROWS);
        int[] stripIndexCount = new int[NUMROWS];
        int[] coordIndicies = new int[indexCount];
        int[] texCoordIndicies = new int[indexCount];
        int index=0;
        int ti = 0;
        
        for(int r=0; r<NUMROWS; r++) {
            for(int c=0; c<=NUMCOLUMNS; c++) {
                coordIndicies[index] = computeIndex(c,r)/3;
                texCoordIndicies[index] = computeIndex(c,r)/3;
                index++;
                coordIndicies[index] = computeIndex(c,r+1)/3;
                texCoordIndicies[index] = computeIndex(c,r+1)/3;
                index++;
            }
            stripIndexCount[r] = (NUMCOLUMNS+1)*2;
        }
        
        BranchGroup ret = new BranchGroup();
        Transform3D t3d = new Transform3D();
        //t3d.setScale(0.05f);
        TransformGroup tg = new TransformGroup(t3d);
        ret.addChild(tg);
        
        geom = new IndexedTriangleStripArray(
                coords.length/3,
                GeometryArray.COORDINATES | GeometryArray.BY_REFERENCE | GeometryArray.TEXTURE_COORDINATE_2,
//                GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2,
                1,
                new int[] { 0 },
                indexCount,
                stripIndexCount);
        geom.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
        geom.setCapability(GeometryArray.ALLOW_TEXCOORD_WRITE);
        geom.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
        
        geom.setCoordRefFloat(coords);
        geom.setTexCoordRefFloat(0, texCoords);
//        geom.setCoordinates(0,coords);
//        geom.setTextureCoordinates(0, 0, texCoords);
        geom.setCoordinateIndices(0, coordIndicies);
        geom.setTextureCoordinateIndices(0, 0, texCoordIndicies);
        
//        Appearance app = new Appearance();
        PolygonAttributes polyAttr = new PolygonAttributes();
        polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
//        polyAttr.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        app.setPolygonAttributes(polyAttr);
        
        Shape3D shape = new Shape3D(geom,app);
        tg.addChild(shape);
        
        return ret;
    }
    
    private void print() {
        for(int r=0; r<=NUMROWS; r++) {
            for(int c=0; c<=NUMCOLUMNS; c++)
                System.out.print(particles[r][c].position.x+"  ");
            System.out.println();
        }
    }
    
    private void printForces() {
        for(int r=0; r<=NUMROWS; r++) {
            for(int c=0; c<=NUMCOLUMNS; c++)
                System.out.print(particles[r][c].forces+"  ");
            System.out.println();
        }
    }
    
    private void stepSimulation(float dt) {
        Vector3f ae = new Vector3f();
        Point3f pos = new Point3f();
        calcForces();
        
        for(int r=0; r<=NUMROWS; r++) {
            for(int c=0; c<=NUMCOLUMNS; c++) {
                ae.scale(particles[r][c].mass, particles[r][c].forces);
                particles[r][c].acceleration.set(ae);
                ae.scale(dt);
                particles[r][c].velocity.add(ae);
                pos = particles[r][c].position;
                //System.out.println("Pos="+pos+"  accel"+particles[r][c].acceleration+" ae="+ae+"  force="+particles[r][c].forces);
                particles[r][c].position.scaleAdd(dt, particles[r][c].velocity, particles[r][c].position);               
            }
        }
    }
    
    private void calcForces() {
        Particle particle;
        Vector3f dragVector = new Vector3f();
        
        // Zero all forces
        for (int r=0; r<=NUMROWS; r++)
            for(int c=0; c<=NUMCOLUMNS; c++)
                particles[r][c].forces.set(0f,0f,0f);
        
        // Process Gravity and Drag
        for (int r=0; r<=NUMROWS; r++) {
            for(int c=0; c<=NUMCOLUMNS; c++) {
                particle = particles[r][c];
                if (!particle.locked) {
                    // gravity
                    particle.forces.y = GRAVITY*particle.mass;
                    
                    // viscous drag
                    dragVector.negate(particle.velocity);
                    if (!(dragVector.x==0f && dragVector.y==0f && dragVector.z==0f)) {
                        dragVector.normalize();
                        
                        // s*t1 + t2
                        particle.forces.scaleAdd(particle.velocity.lengthSquared()*DRAGCOEFFICIENT, 
                                                 dragVector, 
                                                 particle.forces);
                    }
                    
                    // wind
                    windVector.set( (float)Math.random()*10, 0f, (float)Math.random());
                    if (!(windVector.x==0 && windVector.y==0f && windVector.z==0f)) {
                        windVector.normalize();
                        particle.forces.scaleAdd( (float)Math.random()*windForceFactor, 
                                                   windVector, 
                                                   particle.forces);
                    }
                }
            }
        }
        
        //printForces();
        
        Vector3f d = new Vector3f();
        Vector3f v = new Vector3f();
        Vector3f f1 = new Vector3f();
        Vector3f f2 = new Vector3f();
        float L;
        // Process Spring forces
        for(int i=0; i<NUMSTRUCTURALSPRINGS; i++) {
            Particle p1 = structuralSprings[i].p1;
            Particle p2 = structuralSprings[i].p2;
            
            d.sub(p1.position, p2.position);
            v.sub(p1.velocity, p2.velocity);
            L = structuralSprings[i].restLength;
            float a = -(structuralSprings[i].k * (d.length() - L) +
                    structuralSprings[i].d * ( (v.dot(d) / d.length())));
            f1.set(d);
            f1.normalize();
            //System.out.println(d.length()+"  L="+L+"  a="+a);
            f1.scale(a);
            f2.negate(f1);
            //System.out.println("f1="+f1);
            
            if (!p1.locked)
                p1.forces.add(f1);
            
            if (!p2.locked)
                p2.forces.add(f2);
        }
        
        // printForces();
    }
    
    private int computeIndex(int column, int row) {
        return (row*(NUMCOLUMNS+1)*3)+column*3;
    }
    
    private void init(float textureWidthScale, float textureHeightScale) {
        float f;
        int count = 0;
        Particle p1;
        Particle p2;
        Vector3f distance = new Vector3f();
        
        float tw = 1f/textureWidthScale/NUMCOLUMNS;
        float th = 1f/textureHeightScale/NUMROWS;
        int ti = 0; // Tex Coords index
        
        // Create particles
        for(int r=0; r<=NUMROWS; r++) {
            for(int c=0; c<=NUMCOLUMNS; c++) {
                // Calc mass of this particle
                if (r==0 && c==0)
                    f = 1;
                else if (r==NUMROWS && c==0)
                    f = 2;
                else if (r==0 && c==NUMCOLUMNS)
                    f = 2;
                else if (r==NUMROWS && c==NUMCOLUMNS)
                    f = 1;
                else if ((r==0 || r==NUMROWS) && (c!=0 && c!=NUMCOLUMNS))
                    f = 3;
                else
                    f = 6;
                
                Particle p = new Particle(computeIndex(c,r),c*CSTEP-clothWidth/2, clothHeight/2 - (r*RSTEP), coords);
                p.mass = (f*MASSPERFACE)/3;
                p.invMass = 1/p.mass;
                particles[r][c] = p;
                
                p.velocity = new Vector3f();
                p.acceleration = new Vector3f();
                p.forces = new Vector3f();
                if (c==0 && (r==0 || r==NUMROWS))
                    p.locked = true;
                else
                    p.locked = false;
                
                texCoords[ti++] = tw * c;
                texCoords[ti++] = th * r;
            }
        }
        
        // Create Springs
        for(int r=0; r<=NUMROWS; r++) {
            for(int c=0; c<=NUMCOLUMNS; c++) {
                if (c<NUMCOLUMNS) {
                    p1 = particles[r][c];
                    p2 = particles[r][c+1];
                    distance.sub(p1.position,p2.position);
                    structuralSprings[count] = new Spring(p1,
                            p2,
                            distance.length(),
                            springTensionConstant,
                            springDampingConstant);
                    count++;
                }
                if (r<NUMROWS) {
                    p1 = particles[r][c];
                    p2 = particles[r+1][c];
                    distance.sub(p1.position,p2.position);
                    structuralSprings[count] = new Spring(p1,
                            p2,
                            distance.length(),
                            springTensionConstant,
                            springDampingConstant);
                    count++;
                }
                if (r<NUMROWS && c<NUMCOLUMNS) {
                    p1 = particles[r][c];
                    p2 = particles[r+1][c+1];
                    distance.sub(p1.position,p2.position);
                    structuralSprings[count] = new Spring(p1,
                            p2,
                            distance.length(),
                            springShearConstant,
                            springDampingConstant);
                    count++;
                }
                if (c>0 && r<NUMROWS) {
                    p1 = particles[r+1][c];
                    p2 = particles[r][c-1];
                    distance.sub(p1.position,p2.position);
                    structuralSprings[count] = new Spring(p1,
                            p2,
                            distance.length(),
                            springShearConstant,
                            springDampingConstant);
                    count++;
                }
            }
        }
    }
    
    
    // Methods of Control UI
    
    void setSingleStep(boolean singleStep) {
        clothBehavior.setSingleStep(singleStep);
    }
    
    void step() {
        clothBehavior.postId(0);
    }
    
    void resetGeom() {
        Particle p;
        
        for(int r=0; r<=NUMROWS; r++) {
            for(int c=0; c<=NUMCOLUMNS; c++) {
                p = particles[r][c];
                p.position.set(c*CSTEP-clothWidth/2, clothHeight/2 - (r*RSTEP), 0f);
                p.velocity.set(0f,0f,0f);
                p.forces.set(0f,0f,0f);
                p.acceleration.set(0f,0f,0f);
            }
        }
        step();
    }
    
    float getSpringTensionConstant() {
        return springTensionConstant;
    }

    void setSpringTensionConstant(float springTensionConstant) {
        this.springTensionConstant = springTensionConstant;
    }

    float getSpringShearConstant() {
        return springShearConstant;
    }

    void setSpringShearConstant(float springShearConstant) {
        this.springShearConstant = springShearConstant;
    }

    float getSpringDampingConstant() {
        return springDampingConstant;
    }

    void setSpringDampingConstant(float springDampingConstant) {
        this.springDampingConstant = springDampingConstant;
    }
    
    float getWindFactor() {
        return windForceFactor;
    }
    
    void setWindFactor(float windFactor) {
        windForceFactor = windFactor;
    }
    
    Vector3f getWindVector() {
        return windVector;
    }

    void updateSpringConstants(float springTensionConstant, float springShearConstant, float springDampingConstant) {
        this.springTensionConstant = springTensionConstant;
        this.springShearConstant = springShearConstant;
        this.springDampingConstant = springDampingConstant;
        
        int count = 0;
        Particle p1;
        Particle p2;
        
        for(int r=0; r<=NUMROWS; r++) {
            for(int c=0; c<=NUMCOLUMNS; c++) {
                if (c<NUMCOLUMNS) {
                    p1 = particles[r][c];
                    p2 = particles[r][c+1];
                    structuralSprings[count].k = springTensionConstant;
                    structuralSprings[count].d = springDampingConstant;
                    count++;
                }
                if (r<NUMROWS) {
                    p1 = particles[r][c];
                    p2 = particles[r+1][c];
                    structuralSprings[count].k = springTensionConstant;
                    structuralSprings[count].d = springDampingConstant;
                    count++;
                }
                if (r<NUMROWS && c<NUMCOLUMNS) {
                    p1 = particles[r][c];
                    p2 = particles[r+1][c+1];
                    structuralSprings[count].k = springShearConstant;
                    structuralSprings[count].d = springDampingConstant;
                    count++;
                }
                if (c>0 && r<NUMROWS) {
                    p1 = particles[r+1][c];
                    p2 = particles[r][c-1];
                    structuralSprings[count].k = springShearConstant;
                    structuralSprings[count].d = springDampingConstant;
                    count++;
                }
            }
        }
    }
    
    // End Control UI methods
    
    // HUGE HACK
    // We need a centralised physics system
    class ClothBehavior extends javax.media.j3d.Behavior {
        private javax.media.j3d.WakeupCriterion wakeupContinuous = new javax.media.j3d.WakeupOnElapsedFrames(0);
        private javax.media.j3d.WakeupCriterion wakeupStep = new javax.media.j3d.WakeupOnBehaviorPost(this,0);
        
        private javax.media.j3d.WakeupCondition wakeup = wakeupContinuous;
        
        private int skipCount = 0;
        
        private boolean singleStep = false;
        
        private int frameCount = 0;
                
        /**
         * Creates the behavior and attaches it to the parent
         */
        public ClothBehavior(BranchGroup parent) {
            super();
            setSchedulingBounds(new javax.media.j3d.BoundingSphere(new Point3d(), Double.POSITIVE_INFINITY));
            getImplementationNode(parent).addChild(this);
        }
        
        public void setSingleStep(boolean singleStep) {
            if (this.singleStep==singleStep)
                return;
            
            if (singleStep)
                wakeup = wakeupStep;
            else {
                wakeup = wakeupContinuous;
                postId(0);
            }
            this.singleStep = singleStep;
        }
        
        public void initialize() {
            wakeupOn(wakeup);
        }
        
        public void processStimulus(Enumeration e) {
            //System.out.println("Proc Stim entered");
//            skipCount++;
//            if (!singleStep && skipCount<2) {
//                wakeupOn(wakeup);
//                return;
//            }
//            skipCount=0;
            
            long start = System.nanoTime();
            final int SIM_STEPS = 3;
            for(int i=0; i<SIM_STEPS; i++)
                stepSimulation(1f/(30f*SIM_STEPS));
            //System.out.println("Step "+((System.nanoTime()-start)/1000)+" us.  ----------------------------------------");
            
            javax.media.j3d.GeometryArray j3dGeom = getImplementationNode(geom);
            
            for(int r=0; r<=NUMROWS; r++) {
                for(int c=0; c<=NUMCOLUMNS; c++) {
                    particles[r][c].updateCoords(coords);
                }
            }
//            j3dGeom.setCoordinates(0,coords);
            j3dGeom.updateData( new javax.media.j3d.GeometryUpdater() {
                public void updateData(javax.media.j3d.Geometry geometry) {
                    for(int r=0; r<=NUMROWS; r++) {
                        for(int c=0; c<=NUMCOLUMNS; c++) {
                            particles[r][c].updateCoords(coords);
                        }
                    }
                }
            });
                        
            wakeupOn(wakeup);
            //System.out.println("Proc Stim returning "+(frameCount++));
        }
        
        /**
         * get the  underlying Java 3D node. This is an implementation
         * specific call and should only be made by the display server
         * @param lgBg the LGBranchGroup to get the Java3D version of
         * @return the Java3D version of the LGBranchGroup
         */
        private javax.media.j3d.GeometryArray getImplementationNode(GeometryArray lgBg) {
            Object obj;
            org.jdesktop.lg3d.sg.internal.wrapper.SceneGraphObjectWrapper w = lgBg.getWrapped();
            javax.media.j3d.GeometryArray ret = null;
            
            while( ret==null ) {
                obj = w.getWrapped();
                if (obj==null)
                    throw new RuntimeException("Can't resolve implementation node");
                else if (obj instanceof org.jdesktop.lg3d.sg.internal.wrapper.SceneGraphObjectWrapper)
                    w = (org.jdesktop.lg3d.sg.internal.wrapper.SceneGraphObjectWrapper)obj;
                else if (obj instanceof javax.media.j3d.GeometryArray)
                    ret = (javax.media.j3d.GeometryArray)obj;
            }
            
            return ret;
        }
        /**
         * get the  underlying Java 3D node. This is an implementation
         * specific call and should only be made by the display server
         * @param lgBg the LGBranchGroup to get the Java3D version of
         * @return the Java3D version of the LGBranchGroup
         */
        private javax.media.j3d.BranchGroup getImplementationNode(BranchGroup lgBg) {
            Object obj;
            org.jdesktop.lg3d.sg.internal.wrapper.SceneGraphObjectWrapper w = lgBg.getWrapped();
            javax.media.j3d.BranchGroup ret = null;
            
            while( ret==null ) {
                obj = w.getWrapped();
                if (obj==null)
                    throw new RuntimeException("Can't resolve implementation node");
                else if (obj instanceof org.jdesktop.lg3d.sg.internal.wrapper.SceneGraphObjectWrapper)
                    w = (org.jdesktop.lg3d.sg.internal.wrapper.SceneGraphObjectWrapper)obj;
                else if (obj instanceof javax.media.j3d.BranchGroup)
                    ret = (javax.media.j3d.BranchGroup)obj;
            }
            
            return ret;
        }
    }


}
