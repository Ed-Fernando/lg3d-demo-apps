/**
 * Project Looking Glass
 *
 * $RCSfile: Issue381.java,v $
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
 * $Revision: 1.1 $
 * $Date: 2006-02-04 00:46:36 $
 * $State: Exp $
 */

/**
 * Issue 381
 * https://lg3d-core.dev.java.net/issues/show_bug.cgi?id=381
 *
 * Test program for ensuring Frame3D's are gc'ed once they are removed
 * from the graph and go out of scope.
 *
 * To debug/check the issue you need a Reference tracking tool (such as j2bworks)
 * and search for the object TestFrame3D. When you select the Issue 381 icon
 * a box will appear and you will the TestFrame3D object in the tracking tool.
 * Close the box by right clicking on it's icon and the TestFrame3D object
 * should be gc'ed.
 */
package org.jdesktop.lg3d.tests.issue381;

import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.utils.shape.Box;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseMotionEvent3D;

public class Issue381 {
    public static void main(String[] args) {
        new Issue381();
    }
    
    public Issue381() {
	
        // In the profiler look to TestFrame3D, it should
        // be gc'ed once the box is closed
        Frame3D frame3d = new TestFrame3D();
        
        SimpleAppearance app = new SimpleAppearance(0.6f, 0.8f, 0.6f);
        Box box = new Box(0.04f, 0.03f, 0.02f, app);
        
        Component3D comp = new Component3D();
        
        comp.addChild(box);
        
        frame3d.addChild(comp);
                
        frame3d.setPreferredSize(new Vector3f(0.08f, 0.06f, 0.04f));
        
        LgEventConnector.getLgEventConnector().addListener( frame3d, new LgEventListener() {
            public void processEvent(LgEvent lgEvent) {
                System.out.println("Issue381 got MouseMotionEvent3D");
            }

            public Class[] getTargetEventClasses() {
                return new Class[] {MouseMotionEvent3D.class};
            }
            
        });
        
        frame3d.changeEnabled(true);
        
        frame3d.changeVisible(true);
        

    }
    
    class TestFrame3D extends Frame3D {
        public TestFrame3D() {
            super();
        }
    }
}
