/**
 * Project Looking Glass
 *
 * $RCSfile: SourceViewer.java,v $
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

import org.jdesktop.lg3d.wg.*;
import javax.vecmath.*;

/**
 *
 * @author paulby
 */
public class SourceViewer extends Frame3D {
    
    /** Creates a new instance of SourceViewer */
    public SourceViewer() {
        
        try {
            final SourceWindow[] windows = {
                new SourceWindow(getClass().getResource("SourceWindow.java"), new Vector3f(0.01f, 0f, 0f)),
                new SourceWindow(getClass().getResource("SourceViewer.java"), new Vector3f(0f, 0f, -0.01f)),
                new SourceWindow(getClass().getResource("/org/jdesktop/lg3d/apps/cdviewer/CDViewer.java"), new Vector3f(0.02f, 0f, -0.01f)),
                new SourceWindow(getClass().getResource("/org/jdesktop/lg3d/apps/tapps/WebIcon.java"), new Vector3f(0.02f, 0f, -0.02f)),
                new SourceWindow(getClass().getResource("/org/jdesktop/lg3d/apps/help/Lg3dHelp.java"), new Vector3f(0.02f, 0.05f, -0.02f)),
                new SourceWindow(getClass().getResource("/org/jdesktop/lg3d/apps/bgmanager/BgFrame.java"), new Vector3f(0.02f, 0f, -0.03f)),
                new SourceWindow(getClass().getResource("/org/jdesktop/lg3d/apps/bgmanager/BgFileReader.java"), new Vector3f(0.0f, 0f, -0.03f)),
                new SourceWindow(getClass().getResource("/org/jdesktop/lg3d/apps/bgmanager/BgLgComponent.java"), new Vector3f(-0.02f, 0f, -0.03f)),
                new SourceWindow(getClass().getResource("/org/jdesktop/lg3d/apps/bgmanager/BgManager.java"), new Vector3f(-0.02f, 0.04f, -0.03f)),
                new SourceWindow(getClass().getResource("/org/jdesktop/lg3d/apps/bgmanager/BgConfigFileReader.java"), new Vector3f(0.04f, 0.02f, -0.03f)),
                new SourceWindow(getClass().getResource("/org/jdesktop/lg3d/apps/bgmanager/BgConfigFileWriter.java"), new Vector3f(0.04f, -0.02f, -0.03f)),
                new SourceWindow(getClass().getResource("/org/jdesktop/lg3d/apps/bgmanager/BgManagerIcon.java"), new Vector3f(0.04f, -0.05f, -0.03f)),
                new SourceWindow(getClass().getResource("/org/jdesktop/lg3d/apps/tapps/WebIcon2.java"), new Vector3f(0.0f, 0f, -0.02f))
            };

            for(SourceWindow sc : windows)
                addChild(sc);

            // Hack, need to wait for window graphs to be made live so Local2VW call works
            Thread t = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch(Exception ite) {

                    }
                    windows[0].addCalledBy(10, windows[1], 10, true);
                    windows[0].addCalledBy(200, windows[1], 56, true);
                    windows[1].addCalledBy(50, windows[7], 100, false);
                    windows[1].addCalledBy(42, windows[5], 400, false);
                    windows[0].addCalledBy(120, windows[5], 230, false);
                    windows[2].addCalledBy(15, windows[9], 30, true);
                    windows[2].addCalledBy(30, windows[10], 40, false);
                    windows[9].addCalledBy(60, windows[8], 10, true);
                }
            };
            t.start();
        
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
	Frame3D app = new SourceViewer();
        app.changeEnabled(true);
        app.changeVisible(true);
    }
}
