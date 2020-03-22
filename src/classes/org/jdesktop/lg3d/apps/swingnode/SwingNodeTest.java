/**
 * Project Looking Glass
 *
 * $RCSfile: SwingNodeTest.java,v $
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

// org.jdesktop.lg3d.wg.* -- Project Looking Glass core components
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.SwingNode;
import org.jdesktop.lg3d.wg.Tapp;
import org.jdesktop.lg3d.wg.Component3D;
// classes related to Shape3D.
import org.jdesktop.lg3d.utils.shape.Sphere;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.sg.*;
// javax.vecmath.* -- Classes for vector math operations
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.utils.shape.FuzzyEdgePanel;
import org.jdesktop.lg3d.wg.SwingNodeRenderer;
import org.jdesktop.lg3d.wg.Toolkit3D;

/**
 * Put a simple Swing UI in the LG environment
 */
public class SwingNodeTest extends Tapp {
    public static void main(String[] args) {
        new SwingNodeTest();
    }
    
    public SwingNodeTest() {
        // First, we need to create the root container for this 3D
        // application.  The Frame3D class serves for this purpose.
        // We can extend Frame3D, or simply create one and add components
        // to it.  In this example, we'll take the later approach.
	
        Frame3D frame3d = new Frame3D();
        
        // Now create the SwingNode and add the JPanel to it.
        SwingNode swingNode = new SwingNode(new ClothSwingNodeGeometry());
        // If you are interested in a default (simple rectangular) geometry,
        // do the following instead.
//        SwingNode swingNode = new SwingNode();
        swingNode.setPanel(new TestPanel());
        
        SimpleAppearance app = new SimpleAppearance(0.6f, 0f, 0f);        
        Sphere sphere = new Sphere(0.007f, app);
        Component3D comp = new Component3D();
        Transform3D t3d = new Transform3D();
        t3d.set(new Vector3f(0f, 0.02f, 0f));
        TransformGroup tg = new TransformGroup(t3d);
        tg.addChild(sphere);
        comp.addChild(tg);
        comp.addChild(swingNode);
        frame3d.addChild(comp);
        
        
        // We need to do a few more stuff to make it visible.
        // 
        // One thing better to do is to set size hint of this application.
        // The current SceneManager implementation depends on this size hint
        // for arranging 3D applications (Frame3D objects) in the 3D space
        // it manages.
        
        frame3d.setPreferredSize(new Vector3f(0.04f, 0.03f, 0.02f));
        
        // When the Frame3D object created, it is not visible -- it is not
        // a part of the scenegraph.  The setActive() call actually adds
        // the given 3D app to the scenegraph and make it visible. 
        // More precisely, the call initiates interaction with the 3D
        // SceneManager and the manager deals with the details of policy
        // for presenting the application (e.g, location, size, etc).
        
        frame3d.setEnabled(true);
        
        // And this is the final step.
        // By default, a Frame3D object is invisible even after being
        // added to the scenegraph.  The following call makes it finally
        // visible to you.  Note that setVisible() is fairly lightweight
        // compared to the setActive() call, so you want to use setVisible()
        // for temprarily hide an object.
        
        frame3d.changeVisible(true);
        
        // Now you should see a pale green box on the screen. 
        // You saw just a green rectangle?
        // Well, we need to rotate it to make it look like a box.
        // We'll do it in the next lesson...
        
    }
    
    class ClothSwingNodeGeometry extends SwingNodeRenderer {
               
        private Appearance swingAppearance;
//        private FuzzyEdgePanel body;
        private ClothTest body;
        
        public ClothSwingNodeGeometry() {        
            width3D = 0.04f;
            height3D = 0.03f;

            swingAppearance = new Appearance();
            TextureAttributes texAttr = new TextureAttributes();
            texAttr.setTextureMode(TextureAttributes.MODULATE);
            swingAppearance.setTextureAttributes(texAttr);
            swingAppearance.setCapability(Appearance.ALLOW_TEXTURE_WRITE);

//            body = new FuzzyEdgePanel(width3D, height3D, swingAppearance);
            body = new ClothTest(width3D, height3D, swingAppearance);
            
            ControlFrame controlFrame = new ControlFrame(body);
            controlFrame.setVisible(true);
            
            addChild(body);
        }
        
        public void textureChanged(Texture2D texture) {
            int swingImageWidth = panel.getWidth();
            int swingImageHeight = panel.getHeight();
            float p2width = texture.getWidth();
            float p2height = texture.getHeight();
            Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
            float localWidth = toolkit3d.widthNativeToPhysical(panel.getWidth());
            float localHeight = toolkit3d.heightNativeToPhysical(panel.getHeight());
            
            swingAppearance.setTexture(texture);
            if (localWidth!=width3D || localHeight!=height3D) {
                width3D=localWidth;
                height3D=localHeight;
                body.setSize(width3D, height3D,
                        p2width/(float)swingImageWidth, 
                        p2height/(float)swingImageHeight);
                setPreferredSize(new Vector3f(width3D, height3D, 0f));   
            }
        } 
    }
}
