/**
 * Project Looking Glass
 *
 * $RCSfile: MyLabel.java,v $
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
 * $Date: 2005-08-30 18:46:12 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.apps.swingtest;

import java.awt.Graphics;
import javax.swing.JLabel;

/**
 *
 * @author paulby
 */
public class MyLabel extends JLabel {
    
    /** Creates a new instance of MyLabel */
    public MyLabel(String label) {
        super(label);
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        //System.out.println(g);
        //Thread.dumpStack();
    }
    
}
