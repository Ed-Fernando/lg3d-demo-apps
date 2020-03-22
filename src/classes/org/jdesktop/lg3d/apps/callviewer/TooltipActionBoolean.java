/**
 * Project Looking Glass
 *
 * $RCSfile: TooltipActionBoolean.java,v $
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

import java.lang.ref.WeakReference;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.shape.FuzzyEdgePanel;
import org.jdesktop.lg3d.wg.Component3D;

/**
 *
 * @author paulby
 */
public class TooltipActionBoolean implements ActionBoolean {
    
    private String tooltip;
    private Component3D c3d;
    private WeakReference<Component3D> tooltipShapeRef = null;
    
    /** Creates a new instance of TooltipActionBoolean */
    public TooltipActionBoolean(Component3D c3d, String tooltip) {
        this.tooltip = tooltip;
        this.c3d = c3d;
    }

    public void performAction(org.jdesktop.lg3d.wg.event.LgEventSource lgEventSource, boolean param) {
        if (tooltipShapeRef==null || tooltipShapeRef.get()==null)
            tooltipShapeRef = new WeakReference(createTooltip());
        
        c3d.addChild(tooltipShapeRef.get());
        
    }
    
    private Component3D createTooltip() {
        //FuzzyEdgePanel p = new FuzzyEdgePanel()
        return null;
    }
}
