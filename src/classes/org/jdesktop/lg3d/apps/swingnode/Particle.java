/**
 * Project Looking Glass
 *
 * $RCSfile: Particle.java,v $
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
 * $Date: 2006-04-04 00:47:11 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.apps.swingnode;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 *
 * @author paulby
 */
public class Particle {
    
    float mass;
    float invMass;
    Point3f position;
    Vector3f acceleration;
    Vector3f velocity;
    Vector3f forces;
    boolean locked;
    
    private int row;
    private int column;
    private float[] coords;
    private int index;
    
    /** Creates a new instance of Particle */
    public Particle(int index, float x, float y, float[] coords) {
        this.coords = coords;
        this.index = index;
        coords[index] = x;
        coords[index+1] = y;
        coords[index+2] = 0f;
        position = new Point3f(x,y,0f);
    }
    
    public void updateCoords(float[] coords) {
        coords[index] = position.x;
        coords[index+1] = position.y;
        coords[index+2] = position.z;        
        //System.out.println("updateCoords "+index+" : "+coords[index]+" "+coords[index+1]+" "+coords[index+2]);
    }
    
}
