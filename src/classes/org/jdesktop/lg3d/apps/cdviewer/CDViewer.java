/**
 * Project Looking Glass
 *
 * $RCSfile: CDViewer.java,v $
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
 * $Revision: 1.15 $
 * $Date: 2006-08-17 18:30:16 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.apps.cdviewer;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.vecmath.Vector3f;
import com.sun.j3d.utils.shader.StringIO;

import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.GLSLShaderProgram;
import org.jdesktop.lg3d.sg.Shader;
import org.jdesktop.lg3d.sg.ShaderAppearance;
import org.jdesktop.lg3d.sg.ShaderProgram;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.sg.SourceCodeShader;
import org.jdesktop.lg3d.sg.Texture;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.action.ActionInt;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseWheelEventAdapter;
import org.jdesktop.lg3d.utils.shape.Disc;
import org.jdesktop.lg3d.utils.shape.RingShadow;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.utils.shape.SimpleShaderAppearance;
import org.jdesktop.lg3d.utils.smoother.XYPolarNaturalVector3fSmoother;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.LayoutManager3D;
import org.jdesktop.lg3d.wg.Thumbnail;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseEvent3D.ButtonId;


/**
 * A CD Viewer 3D application demo code.
 * This simple example 3D application exercises variety of the API
 * features.
 * Frame3D is the base class for all the 3D application.  You can
 * instantiate and add children to it, or you can extend it.
 * In this example, we'll extend the class. 
 */
public class CDViewer extends Frame3D {
    private static final String imageDir
        = "org/jdesktop/lg3d/apps/cdviewer/resources/images/";
    private static final int numDiscs = 10;
    private static final int numImages = 4;
    private static final float discSize = 0.05f;
    private static final float fanSizeMax = discSize * 2;
    private static final float fanSizeMin = discSize;
    private static final float frontRad = (float)(Math.PI * 3 / 2);
    private static final float stackSpacing = 0.00125f;
    private static final float bodyAngle = (float)Math.toRadians(-65);
    private static final float thumbnailDiscSize = 0.012f;
    
    private Container3D mainContainer;
    private boolean focused = false;
    private CDThumbnail thumbnail;
    private Vector3f tmpV3f = new Vector3f();
    
    public static void main(String[] args) {
        // Create the demo application.
	Frame3D app = new CDViewer();
        
        // When the Frame3D object created, it is not live -- it is not
        // a part of the scenegraph.  The setActive() call actually adds
        // the given 3D app to the scenegraph and make it viewable. 
        // More precisely, the call initiates interaction with the 
        // SceneManager and the manager deals with the details of policy
        // for presenting the application (e.g, location, size, etc).
        app.changeEnabled(true);
        
        // By default, a Frame3D object is invisible even after being
        // added to the scenegraph.  The following call makes it finally
        // visible to the user.  Note that setVisible() is fairly lightweight
        // compared to the setEnabled() call.
        app.changeVisible(true);
    }
    
    /**
     * Constructs a CD Viewer demo application.
     */
    public CDViewer() {
        setName("CDViewer");
        
        // This application has the following branch graph:
        //
        //   this -> mainContainer -*-> CD
        //         
        // The mainContainer is where all the CD objects go in.
        // The mainContainer will arrange the CDs leveraging a LayoutManager3D
        // which will be explained later.
        mainContainer = new Container3D();
        addChild(mainContainer);
        
        // The mainContainer is responsible for positioning the set of CDs in 
        // a nicely angled way.
        mainContainer.setRotationAxis(1.0f, 0.0f, 0.0f);
        mainContainer.setRotationAngle(bodyAngle);
        // Sets a layoutmanager for the mainContainer.
        mainContainer.setLayout(new CDLayout());
        
        // Adds CDs to the mainContainer.
        Texture initThumbnailTex = null;
        for (int i = 0; i < numDiscs; i++) {
            CD cd;
            cd = new CD(this.getClass().getClassLoader().getResource(imageDir + "CD" + (i % numImages + 1) + ".png"));
            if (i == 0) {
                initThumbnailTex = cd.app.getTexture();
            }
            mainContainer.addChild(cd);
        }
        
        // The following implements the behavior to fan out the CDs when
        // mouse entered any part of the application.  It changes the 
        // "focused" instance variable based on mouse enter of exit,
        // then requests the mainContainer to re-layout the contents.
        addListener(
            new MouseEnteredEventAdapter(
                new ActionBoolean() {
                    public void performAction(LgEventSource source, 
                        boolean flag) 
                    {
                        focused = flag;
                        float size = (flag)?(fanSizeMax):(fanSizeMin);
                        mainContainer.revalidate();
                        tmpV3f.set(size, discSize, size);
                        setPreferredSize(tmpV3f);
                    }
                }));
        
        // The following implements the behavior to revolve the CDs
        // with the mouse wheel operation.
        addListener(
            new MouseWheelEventAdapter(
                new ActionInt() {
                    public void performAction(LgEventSource source, 
                        int clicks) 
                    {
                        rearrangeCDs(clicks);
                    }
                }));
                
        // The following implements iconification by right click on mouse
        addListener(
            new MouseClickedEventAdapter(ButtonId.BUTTON3,
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                        assert(source instanceof Frame3D);
                        ((Frame3D)source).changeVisible(false);
                    }
                }));
                
        tmpV3f.set(fanSizeMin, discSize, fanSizeMin);
        setPreferredSize(tmpV3f);
        
        thumbnail = new CDThumbnail(thumbnailDiscSize, initThumbnailTex);
        setThumbnail(thumbnail);
    }
    
    /**
     * Rearrange the order of CD objects in the mainContainer so that
     * the given CD comes to the front.  See the comments in the
     * CDLayout class.
     */
    private void rearrangeCDs(CD selectedCD) {
        mainContainer.rearrangeChildLayout(selectedCD, null);
    }
    
    /**
     * Revolve the order of CD objects in the mainContainer.
     */
    private void rearrangeCDs(int rotation) {
        mainContainer.rearrangeChildLayout(null, rotation);
    }
    
    /**
     * This class defines a CD's visual and part of the behavior.
     * It extends NaturalMotionContainer3D, which is a utility class
     * that implements smooth transition when changeTranslate/RotationAngle/
     * and Scale methods are invoked.  Together with usage of
     * NaturalMotionComponent3D for the CDInner class, it his simplifies to
     * achieve smooth motion of CDs in this application.
     */
    private class CD extends Container3D {
        private Component3D inner;
        private Appearance app;
        
        private CD(URL textureFile) {
            NaturalMotionAnimation nma = new NaturalMotionAnimation(500);
            nma.setTranslationSmoother(new XYPolarNaturalVector3fSmoother());
            setAnimation(nma);
            
            if (System.getProperty("lg.shaderdemo") != null) {
                // The following is demo of the shader support -- please skip
                // if you are new to LG3D.  SimpleShaderAppearance simplifies
                // creation of a typical ShaderAppearance object. 
                try {
                    app = new SimpleShaderAppearance(
                                getClass().getResource("resources/dimple.vert"),
                                getClass().getResource("resources/dimple.frag"));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            } else {
                // SimpleAppearance class is a utility class to simplify
                // appearance configuration.  For CD class, we need to enable
                // texture, and we disable culling so that CD won't disappear
                // when it showed the backside.  The right way to deal with such
                // a situation is to provide a proper backside (a shiny CD 
                // surface), but for our use, disabling culling serves well since
                // there is not many occasion to see the backside in normal use
                // of the demo.
                try {
                    app = new SimpleAppearance(
                                textureFile,
                                1.0f, 1.0f, 1.0f, 1.0f,
                                SimpleAppearance.ENABLE_TEXTURE
                                | SimpleAppearance.DISABLE_CULLING);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            
            // Use NaturalMotionComponent3D here too since we use the
            // change***() methods against the inner object
            // in the raise() method.
            inner = new Component3D();
            inner.setAnimation(new NaturalMotionAnimation(500));
            inner.addChild(new Disc(discSize, 36, app));
            inner.addChild(new RingShadow(
                discSize * 1.03f, discSize, 36, 0.5f));
            
            // Here we use set***() methods so that location changes
            // take action immediately.  When we need animated transition,
            // we use change***() methods.
            inner.setTranslation(0.0f, discSize, 0.0f);
            inner.setRotationAxis(-1.0f, 0.0f, 0.0f);
            
            addChild(inner);
            
            // The following lines are to identify a CD the user clicked up,
            // and to bring the CD in front of the user.
            addListener(
                new MouseClickedEventAdapter(
                    new ActionNoArg() {
                        public void performAction(LgEventSource source) {
                            rearrangeCDs((CD)source);
                        }
                    }));
            setMouseEventPropagatable(true);
        }
        
        /**
         * Moves this CD to the upright position if the argument is true.
         */
        private void raise(boolean raised) {
            if (raised) {
                inner.changeRotationAngle(bodyAngle);
                inner.changeTranslation(0.0f, discSize * 0.8f, discSize * 0.4f);
                inner.changeScale(1.2f);
                thumbnail.setCurrent(app.getTexture());
            } else {
                inner.changeRotationAngle(0.0f);
                inner.changeTranslation(0.0f, discSize, 0.0f);
                inner.changeScale(1.0f);
            }
        }
    }
    
    /**
     * A layout manager class that positions the list of CDs in
     * a circular manner starting from the front and going counter clockwise.
     * REMINDER -- this layout manager has dependency on the other class found
     * in this file.  It would be great if we could find out a way to make
     * the feature more abstract so that we can reuse it for other programs.
     * The layoutmanager framework is still in its infancy.  Lots of design
     * work have to be done.
     */
    private class CDLayout implements LayoutManager3D {
        private Container3D cont;
        private ArrayList<Component3D> compList = new ArrayList<Component3D>();
        private int frontCDIndex = 0;
        
        public void setContainer(Container3D cont) {
            this.cont = cont;
        }
        
        public void layoutContainer() {
            int numDiscs = compList.size();
            float radSeg = (float)(Math.PI / numDiscs);
            int dir = 1;
            float z = 0.0f;
            float fanSize = (focused)?(fanSizeMax):(fanSizeMin);
            
            for (int i = 0; i < numDiscs; i++) {
                int idx = (frontCDIndex + i) % numDiscs;
                Component3D comp = compList.get(idx);
                
                float r = frontRad + (float)Math.PI * 2 / numDiscs * i;
                float x = fanSize * (float)Math.cos(r);
                float y = fanSize * (float)Math.sin(r);
                comp.changeTranslation(x, y, z);
                
                float prevAngle = comp.getRotationAngle();
                float newAngle = r - frontRad;
                float diff = newAngle - prevAngle;
                if (diff > Math.PI) {
                    comp.setRotationAngle(prevAngle + (float)Math.PI * 2.0f);
                } else if (diff < -Math.PI) {
                    comp.setRotationAngle(prevAngle - (float)Math.PI * 2.0f);
                }
                
                comp.changeRotationAngle(r - frontRad);
                if (i > numDiscs / 2) {
                    z += stackSpacing;
                } else if (i == numDiscs / 2) {
                    z -= stackSpacing * 0.5f;
                } else {
                    z -= stackSpacing;
                }
                ((CD)comp).raise((i == 0) && focused);
            }
        }
        
        public void addLayoutComponent(Component3D comp, Object constraints) {
            compList.add(comp);
            comp.setRotationAxis(0.0f, 0.0f, 1.0f);
        }
        
        public void removeLayoutComponent(Component3D comp) {
            compList.remove(comp);
        }
        
        /**
         * Move the specified CD to the front position.
         */
        public boolean rearrangeLayoutComponent(Component3D selectedCd, 
            Object newConstraints) 
        {   
            int idx = 0;
            if (selectedCd != null) {
                // If a component is given, bring it to the front.
                assert(newConstraints == null);
                idx = compList.indexOf(selectedCd);
            } else {
                // Else, the rotaiton counts should have been specified
                // using the newConstraints argument.
                assert (newConstraints != null && newConstraints instanceof Integer);
                int rotation = (int)Math.signum((float)(Integer)newConstraints);
                int numDiscs = compList.size();
                idx = (frontCDIndex + rotation + numDiscs) % numDiscs;
            }
            assert(idx >= 0);
            if (frontCDIndex == idx) {
                return false;
            }
            frontCDIndex = idx;
            return true;
        }
    }
    
    private class CDThumbnail extends Thumbnail {
        private SimpleAppearance app;
        
        private CDThumbnail(float size, Texture tex) {
            app = new SimpleAppearance(
                        1.0f, 1.0f, 1.0f, 0.75f,
                        SimpleAppearance.ENABLE_TEXTURE
                        | SimpleAppearance.DISABLE_CULLING);
            app.setTexture(tex);
            Shape3D cdBody = new Disc(size, 36, app);
            cdBody.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
            addChild(cdBody);
            
            addChild(new RingShadow(size * 1.1f, size, 36, 0.3f));
            
            setPreferredSize(new Vector3f(size, size, 0));
        }
        
        private void setCurrent(Texture tex) {
            app.setTexture(tex);
        }
    }
}


