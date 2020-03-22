/**
 * Project Looking Glass
 *
 * $RCSfile: SampleGraph.java,v $
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
 * $Date: 2005-12-23 21:51:12 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.apps.graph;
import java.util.ArrayList;
import org.jdesktop.lg3d.scenemanager.utils.springdamper.Spring;
import org.jdesktop.lg3d.scenemanager.utils.springdamper.SprungFrame3D;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.utils.shape.Box;
import org.jdesktop.lg3d.utils.shape.Sphere;
import org.jdesktop.lg3d.wg.Component3D;

/**
 *
 * @author paulby
 */
public class SampleGraph {
    
    private ArrayList<SprungFrame3D> frames = new ArrayList();
    
    private enum ChildType { BOX, SPHERE };
    
    /** Creates a new instance of SampleGraph */
    public SampleGraph() {
        SprungFrame3D f;
        
        for(int i=0; i<4; i++) {
            f= new SprungFrame3D();
            f.addChild( createChild(ChildType.SPHERE));
            frames.add( f );
        }
        
        new Spring(frames.get(0), frames.get(1), 0.06f);
        new Spring(frames.get(1), frames.get(2), 0.06f);
        new Spring(frames.get(2), frames.get(3), 0.06f);
        new Spring(frames.get(3), frames.get(0), 0.06f);
        
        new Spring(frames.get(0), frames.get(2), 0.085f);
        new Spring(frames.get(1), frames.get(3), 0.085f);
        
        
//        comp1.setTranslation(-0.02f,0.0f, 0.0f);
//        comp2.setTranslation(0.02f, 0.0f, 0.0f);
//        comp3.setTranslation(0.02f, 0.02f, 0.0f);
        
        for(SprungFrame3D frame : frames ) {
            frame.setEnabled(true);
            frame.setVisible(true);
        }
        
    }
    
    private Component3D createChild( ChildType childType ) {
        Component3D ret = new Component3D();
        Appearance app = new Appearance();
        switch( childType ) {
            case BOX :
                ret.addChild( new Box(0.01f, 0.01f, 0.01f, app));
                break;
            case SPHERE :
                ret.addChild( new Sphere( 0.01f ));
                break;
        }
        return ret;
    }
    
    public static void main(String args[]) {
        new SampleGraph();
    }
    
    
}
