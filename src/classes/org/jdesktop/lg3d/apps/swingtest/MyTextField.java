/**
 * Project Looking Glass
 *
 * $RCSfile: MyTextField.java,v $
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
 * $Date: 2006-03-10 22:21:46 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.apps.swingtest;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;



/**
 *
 * @author paulby
 */
public class MyTextField extends JTextField {
    
    /**
     * Creates a new instance of MyTextField 
     */
    public MyTextField() {
        super();
//        addKeyListener(new KeyListener() {
//           public void keyPressed(KeyEvent e) {
//               System.out.println("TF "+e);
//           } 
//           
//           public void keyReleased(KeyEvent e) {
//               System.out.println("TF "+e);
//           } 
//           
//           public void keyTyped(KeyEvent e) {
//               System.out.println("TF "+e);
//           } 
//        });
    }
    
    public void paint(Graphics g) {
        //System.out.println("Paint");
        super.paint(g);
    }
}
