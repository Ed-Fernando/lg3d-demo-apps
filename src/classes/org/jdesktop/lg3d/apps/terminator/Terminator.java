/**
 * Project Looking Glass
 *
 * $RCSfile: Terminator.java,v $
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
 * $Revision: 1.6 $
 * $Date: 2006-09-07 22:36:06 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.apps.terminator;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.scenemanager.utils.SceneControl;
import org.jdesktop.lg3d.scenemanager.utils.plugin.SceneManagerPlugin;
import org.jdesktop.lg3d.scenemanager.utils.taskbar.TaskbarItemConfig;
import org.jdesktop.lg3d.utils.action.AppLaunchAction;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.component.Pseudo3DIcon;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Tapp;
import org.jdesktop.lg3d.wg.event.LgEventConnector;

/**
 * Terminates LG3D.
 * At this moment, simply terminates LG3D by calling System.exit(). 
 * This won't work in future once client-server mechanism is put in place. 
 * In future, we'll add a method for termination to SceneControl, and let
 * this class call the method (it is the reason why this feature is 
 * implemented as a SceneManagerPlugin).
 */
public class Terminator implements SceneManagerPlugin {
    private Component3D root;
    
    public Terminator() {
    }
    
    public void initialize(SceneControl sceneControl) {
        final Pseudo3DIcon icon = new Pseudo3DIcon(getClass().getClassLoader().getResource("resources/images/icon/JollyRoger.png"));
        icon.addListener(
            new MouseClickedEventAdapter(
                new AppLaunchAction(
		    "java org.jdesktop.lg3d.apps.terminator.TerminatorDialog",
		    getClass().getClassLoader())));
                
        // Post the taskbar item event
        LgEventConnector.getLgEventConnector().postEvent(
            new TaskbarItemConfig() {
                @Override
                public Tapp createItem() {
                    Tapp tapp = new Tapp();
                    tapp.addChild(icon);
                    tapp.setPreferredSize(icon.getPreferredSize(new Vector3f()));
                    return tapp;
                }
                @Override
                public int getItemIndex() {
                    return -1;
                }
            }, null);
    }
    
    public void destroy() {
        // TODO -- remove the taskbar item
    }
    
    public boolean isRemovable() {
        return true;        
    }
    
    public Component3D getPluginRoot() {
        return null;
    }
}


