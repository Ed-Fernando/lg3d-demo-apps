/**
 * Project Looking Glass
 *
 * $RCSfile: Tutorial3.java,v $
 *
 * Copyright (c) 2004-2006, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * $Revision: 1.9 $
 * $Date: 2006-06-30 21:48:42 $
 * $State: Exp $
 */

/**
 * Lesson 3
 *
 * Aim: To demonstrate more complex manipulations and user interaction
 *      with a 3D application within the LG3D framework. We will create
 *      a sphere with the Earth texture and a small handle to move it.
 *
 * Requirements: 
 *      This tutorial builds upon Tutorial 1 and Tutorial 2.
 *      To use this tutorial you will need a functional installation of
 *      the of lg3d (the stable version is the best version to begin with).
 *      This can be found at the lg3d web site. Follow the "Getting started 
 *      with Project Looking Glass" link for installation instructions.
 *      Additionally, you need to have the Java JDK 6. 
 *
 * Steps:  	
 *      1. Initialize the 3D application
 *      2. Create a Textured Sphere
 *      3. Setup Some Simple Behabiours for the Earth
 *      4. Controling default scenemanager behavior
 *      5. Initialize the Container
 */
package org.jdesktop.lg3d.apps.tutorial;

// org.jdesktop.lg3d.wg.* -- Project Looking Glass core components
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Cursor3D;
// org.jdesktop.lg3d.tils.c3danimation.* -- Utility animation classes
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
// org.jdesktop.lg3d.utils.shape.* -- Utility shapes and other useful
// classes related to Shape3D.
import org.jdesktop.lg3d.utils.shape.Sphere;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
// org.jdesktop.lg3d.utils.eventadapter.* -- Utility classes that
// receives events and predigest those into action calls.
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseDraggedEventAdapter;
// org.jdesktop.lg3d.utils.actionadapter.* -- performs some processing 
// and possibly conversion among the Action interfaces.
import org.jdesktop.lg3d.utils.actionadapter.ToggleAdapter;
// org.jdesktop.lg3d.utils.action.* -- Behavior classes that accepts
// invocations via Action interface and performs predefined actions.
import org.jdesktop.lg3d.utils.action.ScaleActionBoolean;
import org.jdesktop.lg3d.utils.action.RotateActionBoolean;
// javax.vecmath.* -- Classes for vector math operations
import javax.vecmath.Vector3f;


public class Tutorial3 {
    public static void main(String[] args) {
        new Tutorial3();
    }
    
    public Tutorial3() {
        //
        // Step 1: Initialize the 3D application
        // -------------------------------------
        // Just like in Tutorial 1 the first step is to create a Frame3D.
        
        Frame3D frame3d = new Frame3D();
        
        //
        // Step 2: Create a textured sphere
        // --------------------------------
        // The SimpleAppearance class provides a useful constructor that
        // takes the location of a texture to load.
        // 
        // The appearance parameters are initialized to reasonable values
        // which work fine for most of cases. If you are interested in
        // a more detailed configuration, you can find out more by looking
        // at the API documentation for Appearance, Material and Texture.
        
        SimpleAppearance earthApp = null;
        try {
            earthApp 
                = new SimpleAppearance(this.getClass().getClassLoader().getResource(
                    "org/jdesktop/lg3d/apps/tutorial/resources/images/earth.jpg"));
        } catch (Exception e) {
            System.err.println("Failed to load texture: " + e);
        }
        
        // Now we can create the sphere using this textured appearance.
        // Be sure to specify Primitive.GENERATE_TEXTURE_COORDS so that
        // the texture coordinates will be generated along with the position
        // coordinates.
        
        Sphere earth
            = new Sphere(0.03f, 
                Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS,
                    36, earthApp);
        
        // Now the sphere can be added to a Component3D, set it's position,
        // and set the cursor for it.
        // Note we can set a cursor for each Component3D, and in this example,
        // we use one of the predefined cursors, Cursor3D.E_RESIZE_CURSOR.
        
        Component3D earthComp = new Component3D();
        earthComp.setAnimation(new NaturalMotionAnimation(300));
        earthComp.addChild(earth);
        earthComp.setTranslation(0.0f, 0.0f, -0.01f);
        earthComp.setCursor(Cursor3D.E_RESIZE_CURSOR);
        
        // 
        // Step 3: Setup some simple behaviors for the earth
        // -------------------------------------------------
        // A number of actions are provided by the API. I suggest having
        // a look through the org.jdesktop.lg3d.utils.action,
        // org.jdesktop.lg3d.utils.actionadapter and
        // org.jdesktop.lg3d.utils.eventadapter packages.
        // For this tutorial we will be implementing a RotateActionBoolean,
        // a ScaleActionBoolean and a GenericEventPostAction.
        // The GenericEventPostAction is a generic action for posting
        // LgEvents (have a look at the org.jdesktop.lg3d.wg.event package)
        // to the LG3D event system.
        earthComp.addListener(
            new MouseEnteredEventAdapter(
                new ScaleActionBoolean(earthComp, 1.1f)));
        
        // Here is a simple usage example of a simple action adapter,
        // ToggleAdapter. It converts ActionNoArg interface to ActionBoolean
        // interface by toggling the state for the call to performAction()
        // method of the ActionBoolean interface everytime incoming
        // ActionNoArg's performAction() gets invoked. Don't forget to set
        // the rotation axis. Note that RotateActionBoolean takes the third
        // argument that specifies the duration of its action. This overrides 
        // the default duration specified in the constructor of
        // NaturalMotionAnimation.
        
        earthComp.setRotationAxis(0.0f, 1.0f, 0.0f);
        earthComp.addListener(
            new MouseClickedEventAdapter(
                new ToggleAdapter(
                    new RotateActionBoolean(earthComp, (float)Math.PI, 1000))));
        
        frame3d.addChild(earthComp);
        
        //
        // Step 4: Controling default scenemanager behavior
        // ------------------------------------------------
        // To demonstrate a more involved example on how to control which
        // component the default scenemanager behavior happens, we will add
        // a handle to the earth. 
        
        SimpleAppearance handleApp 
            = new SimpleAppearance(1.0f, 0.0f, 0.0f, 0.5f);
        Sphere handle = new Sphere(0.003f, handleApp);
        Component3D handleComp = new Component3D();
        handleComp.setAnimation(new NaturalMotionAnimation(200));
        handleComp.addChild(handle);
        handleComp.setTranslation(0.0f, 0.05f, 0.0f);
        handleComp.addListener(
            new MouseEnteredEventAdapter(
                new ScaleActionBoolean(handleComp, 1.5f)));
        
        // Now we have two parts for this application, 'earthComp' and
        // 'handleComp'. Since we have registered event adapters with the
        // both of the components, all the mouse events posted to this
        // application will be consumed and no mouse event is passed to the
        // SceneManager.
        // 
        // Let's make the handleComp component the handle for the user to
        // aid in dragging the earth about. In order to do it, we need to 
        // set the flag that allows mouse event propagation for the 
        // handleComp object. In that way, mouse events get propagated to
        // the SceneManager and it performs the default behavior agianst
        // the 'handleComp' that allows the user to grab it and move the
        // application.
        // Note that, in contrast, mouse events posted against the earthComp
        // object are still consumed and there is no default SceneManager
        // action taken against this object.
        
        handleComp.setMouseEventPropagatable(true);
        
        frame3d.addChild(handleComp);
        
        //
        // Step 5: Initialize the container
        // --------------------------------
        // Finally, we can add the frame to the container and initialize 
        // it to make it visible, as described in Tutorial 1 step 4.
        // Note, since the sphere has different dimension and has been
        // rotated and scaled, a large hint size should be given to
        // the SceneManager to avoid conflicts with other 3D applications.
        
        frame3d.setPreferredSize(new Vector3f(0.06f, 0.10f, 0.08f));
        frame3d.changeEnabled(true);
        frame3d.changeVisible(true);
        
        //
        // For compilation and execution, please refer to the Tutorials
        // web site.
        //
    }
}
