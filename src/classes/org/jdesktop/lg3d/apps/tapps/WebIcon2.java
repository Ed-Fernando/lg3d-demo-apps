/*
 * Project Looking Glass
 *
 * $RCSfile: WebIcon2.java,v $
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
 * $Date: 2005-06-24 20:00:41 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.apps.tapps;

import org.jdesktop.lg3d.utils.shape.Sphere;
import org.jdesktop.lg3d.sg.*;
import org.jdesktop.lg3d.utils.action.*;
import org.jdesktop.lg3d.utils.animation.*;
import org.jdesktop.lg3d.utils.eventadapter.*;
import org.jdesktop.lg3d.utils.smoother.*;
import org.jdesktop.lg3d.wg.*;
import org.jdesktop.lg3d.wg.event.*;
import org.jdesktop.lg3d.sg.utils.image.TextureLoader;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;


/**
 * A sample 3D icon
 *
 * @author  paulby
 */
public class WebIcon2 extends Tapp {
    
    /** Creates a new instance of WebIcon */
    public WebIcon2() {
        setPreferredSize(new Vector3f(0.01f,0.01f,0.01f));
        
        Component3D j3d = new Component3D();
        j3d.setAnimation(new NaturalMotionAnimation(150));
        
        j3d.addListener(
            new MouseEnteredEventAdapter(
                new TranslateActionBoolean(j3d, new Vector3f(0.0f, 0.01f * 0.2f, 0.0f), 175)));
        j3d.addListener(
            new MouseEnteredEventAdapter(
                new ScaleActionBoolean(j3d, 1.2f, 175)));
        j3d.addListener(
            new MousePressedEventAdapter(
                new ScaleActionBoolean(j3d, 1.1f, 100)));
        j3d.setMouseEventPropagatable(true);
        
        setCursor(Cursor3D.SMALL_CURSOR);
        
        j3d.addChild(buildGraph());
        addChild(j3d);
    }
    
    private BranchGroup buildGraph() {
        BranchGroup ret = new BranchGroup();
        
        Appearance app = new Appearance();
        TextureLoader loader = new TextureLoader( this.getClass().getResource("/org/jdesktop/lg3d/apps/tutorial/resources/images/earth.jpg"),null);
        Texture tex = loader.getTexture();
        app.setTexture(tex);
        
        AnimationGroup planetRot = new AnimationGroup();

        RotationAnimation planetAnimation = new RotationAnimation(new Vector3f(0f,1f,0f),0f, (float)Math.PI*2f, 4000, RotationAnimation.LOOP_FOREVER, RotationAnimation.LoopType.REPEAT, new LinearFloatSmoother());
        planetRot.setAnimation(planetAnimation);
        planetAnimation.setRunning(true);
        
        Sphere planet = new Sphere( 0.004f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS, 9, app );
        planetRot.addChild(planet);
        
        Sphere moon = new Sphere( 0.001f, Sphere.GENERATE_NORMALS, 5, new Appearance() );
        
        TransformGroup moonPos = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(0.005f,0f,0f));
        moonPos.setTransform(t3d);
        
        AnimationGroup moonRot = new AnimationGroup();
        
        moonRot.addChild(moonPos);
        moonPos.addChild(moon);
        
        RotationAnimation moonAnimation = new RotationAnimation(new Vector3f(0f,1f,0f),(float)Math.PI*2f,0f, 1000, RotationAnimation.LOOP_FOREVER, RotationAnimation.LoopType.REPEAT, new LinearFloatSmoother());
        moonRot.setAnimation(moonAnimation);
        moonAnimation.setRunning(true);
        
        ret.addChild(planetRot);
        ret.addChild(moonRot);
        
        return ret;
    }
    
}
