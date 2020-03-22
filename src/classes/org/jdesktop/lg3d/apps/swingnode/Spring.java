/**
 * Project Looking Glass
 *
 * $RCSfile: Spring.java,v $
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

/**
 *
 * @author paulby
 */
public class Spring {
    
    Particle p1;
    Particle p2;
    float k;    // tensile spring constant
    float d;    // damping factor
    float restLength;    // rest length of spring
    
    /** Creates a new instance of Spring */
    public Spring(Particle p1, Particle p2, float restLength, 
                  float tensileSpringConstant, float dampingFactor) {
        assert(p1!=null);
        assert(p2!=null);
        this.p1 = p1;
        this.p2 = p2;
        this.restLength = restLength;
        this.k = tensileSpringConstant;
        this.d = dampingFactor;
    }
    
}
