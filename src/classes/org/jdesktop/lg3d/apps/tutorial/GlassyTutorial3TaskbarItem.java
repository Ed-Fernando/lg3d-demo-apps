/**
 * Project Looking Glass
 *
 * $RCSfile: GlassyTutorial3TaskbarItem.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2006-04-19 17:23:57 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.apps.tutorial;

import org.jdesktop.lg3d.wg.Tapp;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.shape.Sphere;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MousePressedEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.action.TranslateActionBoolean;
import org.jdesktop.lg3d.utils.action.ScaleActionBoolean;
import org.jdesktop.lg3d.utils.action.AppLaunchAction;
import javax.vecmath.Vector3f;


/**
 * @author gameldar 
 */

public class GlassyTutorial3TaskbarItem extends Tapp {

  public GlassyTutorial3TaskbarItem()  {

    SimpleAppearance earthApp = null;
    try {
      earthApp = new SimpleAppearance(this.getClass().getClassLoader().getResource("org/jdesktop/lg3d/apps/tutorial/resources/images/earth.jpg"));
    } catch (Exception ex) {
    }
    Sphere earth = new Sphere(0.004f, 
	                      Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS,
			      12, earthApp);
    Container3D top = new Container3D();
    top.setAnimation(new NaturalMotionAnimation(150));
    
    Component3D earthComp = new Component3D();
    earthComp.addChild(earth);
//    earthComp.setTranslation(0.0f, 0.0f, -0.01f);
    top.addChild(earthComp);

    SimpleAppearance handleApp = new SimpleAppearance(1.0f, 0.0f, 0.0f, 0.5f);
    Sphere handle = new Sphere(0.0005f, handleApp);
    Component3D handleComp = new Component3D();
    handleComp.addChild(handle);
    handleComp.setTranslation(0.0f, 0.0046f, 0.0f);
    top.addChild(handleComp);
    
    top.addListener(
        new MouseEnteredEventAdapter(
            new TranslateActionBoolean(top, new Vector3f(0.0f, 0.01f * 0.2f, 0.0f), 175)));
    top.addListener(
        new MouseEnteredEventAdapter(
            new ScaleActionBoolean(top, 1.2f, 175)));
    top.addListener(
        new MousePressedEventAdapter(
            new ScaleActionBoolean(top, 1.1f, 100)));
    top.addListener(
        new MouseClickedEventAdapter(
            new AppLaunchAction("java org.jdesktop.lg3d.apps.tutorial.Tutorial3", getClass().getClassLoader())));
    top.setMouseEventPropagatable(true);
    
    addChild(top);
    setPreferredSize(new Vector3f(0.01f, 0.01f, 0.01f));
    changeVisible(true);
  }
}

