/**
 * Project Looking Glass
 *
 * $RCSfile: Line3D.java,v $
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
 * $Date: 2005-06-26 01:07:08 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.apps.callviewer;

import org.jdesktop.lg3d.sg.*;
import org.jdesktop.lg3d.utils.shape.*;
import javax.vecmath.*;

/**
 *
 * @author paulby
 */
public class Line3D extends Shape3D {
    
    /** Creates a new instance of Line3D */
    public Line3D(Point3f start, Point3f end, Color3f color) {
        LineArray line = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        line.setCoordinate(0, start);
        line.setCoordinate(1, end);
        
        line.setColors( 0, new Color3f[] { color,
                                         color });
        
        Appearance app = new Appearance();
        LineAttributes la = new LineAttributes();
        app.setLineAttributes(la);
        la.setLineWidth(2f);
        setAppearance(app);
        addGeometry(line);
    }
    
}
