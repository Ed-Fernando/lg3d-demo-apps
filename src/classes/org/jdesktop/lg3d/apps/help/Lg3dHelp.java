/**
 * Project Looking Glass
 *
 * $RCSfile: Lg3dHelp.java,v $
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
 * $Revision: 1.13 $
 * $Date: 2006-08-23 22:31:09 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.apps.help;

import java.net.MalformedURLException;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.sg.utils.transparency.TransparencyOrderedGroup;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.sg.Texture;
import org.jdesktop.lg3d.sg.TransparencyAttributes;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.action.AppearanceChangeAction;
import org.jdesktop.lg3d.utils.action.ScaleActionBoolean;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.shape.FuzzyEdgePanel;
import org.jdesktop.lg3d.utils.shape.GlassyPanel;
import org.jdesktop.lg3d.utils.shape.ImagePanel;
import org.jdesktop.lg3d.utils.shape.RectShadow;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.Thumbnail;
import org.jdesktop.lg3d.wg.Toolkit3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;

import java.net.URL;


/**
 * Very very preliminary help app whcih just shows the usage guide
 * on the screen.  Intended to become more sophisticated app as we
 * evolve the system...
 */
public class Lg3dHelp extends Frame3D {
    private URL imageFilename 
        = getClass().getResource("resources/images/lg3d-wm-usage.png");
    private static final float bodyDepth = 0.005f;
    private static final float decoWidth = 0.005f;
    // REMINDER -- make the Appearance non-static so that multiple
    // instances of this class won't share it.  Since the current LG3D
    // runs apps on the same VM, the bodyApp will be shared. 
    // This causes a visual issue when translucency effect is applied.
    private /*static*/ final Appearance bodyApp
	= new SimpleAppearance(
	    0.6f, 1.0f, 0.6f, 1.0f, SimpleAppearance.DISABLE_CULLING);
    private static final float buttonSize = 0.005f;
    private static final float buttonOnSize = buttonSize * 1.15f;
    private static final float shadowN = 0.001f;
    private static final float shadowE = 0.0015f;
    private static final float shadowS = 0.002f;
    private static final float shadowW = 0.001f;
    private static final float shadowI = 0.001f;
    private static final float thumbnailScale = 0.13f;
    
    // the followings cannot be static anymore because 
    // the translucency effect touches the appearance objects.
    private SimpleAppearance closeButtonOffAppearance;
    private SimpleAppearance closeButtonOnAppearance;
    private SimpleAppearance minimizeButtonOffAppearance;
    private SimpleAppearance minimizeButtonOnAppearance;
    
    public static void main(String[] args) {
	Frame3D app = new Lg3dHelp();
        app.changeEnabled(true);
        app.changeVisible(true);
    }
    
    public Lg3dHelp() {
        setName("LG3D Helper");
        
        float height = Toolkit3D.getToolkit3D().getScreenHeight() * 0.5f;
        float width = height;
        setPreferredSize(new Vector3f(width + decoWidth * 2, height + decoWidth * 2, bodyDepth));
        
        GlassyPanel bodyDeco
            = new GlassyPanel(
		width + decoWidth * 2,
                height + decoWidth * 2, 
                bodyDepth, 
                bodyApp);
                    
        Shape3D bodyShadow
            = new RectShadow(
                width + decoWidth * 2,
                height + decoWidth * 2,
                shadowN, shadowE, shadowS, shadowW, shadowI,
                -bodyDepth,
                0.2f);
        
        SimpleAppearance app 
            = new SimpleAppearance(1.0f, 1.0f, 1.0f,
                SimpleAppearance.ENABLE_TEXTURE 
                    | SimpleAppearance.DISABLE_CULLING);
        try {
            app.setTexture(imageFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        FuzzyEdgePanel body 
            = new FuzzyEdgePanel(width, height, decoWidth * 0.5f, app);
            
        TransparencyOrderedGroup tog = new TransparencyOrderedGroup();        
        tog.addChild(bodyShadow);
        tog.addChild(bodyDeco);
        tog.addChild(body);
        
        Component3D comp = new Component3D();
        comp.addChild(tog);
        addChild(comp);
        
        initButtonAppearances();
        
        Component3D closeButton 
	    = new Button(buttonSize, closeButtonOffAppearance,
		buttonOnSize, closeButtonOnAppearance);
	closeButton.setCursor(Cursor3D.SMALL_CURSOR);
        closeButton.setTranslation(width * 0.5f, height * 0.5f, 0.001f);
        closeButton.addListener(
            new MouseClickedEventAdapter(
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                        changeEnabled(false);
                    }
                }));
        tog.addChild(closeButton);
        
        Component3D minimizeButton 
	    = new Button(buttonSize, minimizeButtonOffAppearance,
		buttonOnSize, minimizeButtonOnAppearance);
	minimizeButton.setCursor(Cursor3D.SMALL_CURSOR);
        minimizeButton.setTranslation(
            width * 0.5f - buttonSize * 1.4f, 
            height * 0.5f + buttonSize * 0.2f, 0.001f);
        minimizeButton.addListener(
            new MouseClickedEventAdapter(
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                        changeVisible(false);
                    }
                }));
        tog.addChild(minimizeButton);
        
        setThumbnail(new HelpThumbnail(width, height, thumbnailScale, app.getTexture()));
    }
    
    private void initButtonAppearances() {
	closeButtonOffAppearance 
	    = new ButtonAppearance(
                "resources/images/button/window-close.png", false);
	closeButtonOnAppearance
	    = new ButtonAppearance(
                "resources/images/button/window-close.png", true);
        minimizeButtonOffAppearance
	    = new ButtonAppearance(
                "resources/images/button/window-minimize.png", false);
	minimizeButtonOnAppearance
	    = new ButtonAppearance(
                "resources/images/button/window-minimize.png", true);
    }
    
    private static class ButtonAppearance extends SimpleAppearance {
        
	private ButtonAppearance(String filename, boolean on)
	{
	    super(0.0f, 0.0f, 0.0f, 0.0f,
		SimpleAppearance.DISABLE_CULLING
		| SimpleAppearance.ENABLE_TEXTURE);

	    if (on) {
		setColor(1.0f, 0.6f, 0.6f, 0.8f);
	    } else {
		setColor(0.6f, 1.0f, 0.6f, 0.6f);
	    }
            try {
                setTexture(this.getClass().getClassLoader().getResource(filename));
            } catch (Exception e) {
                throw new RuntimeException(
                    "failed to initilaze window button: " + e);
            }
	}
    }

    private class Button extends Component3D {
	private Button(float size, Appearance app) {
	    this(size, app, size, app);
	}

	private Button(float sizeOff, Appearance appOff,
	    float sizeOn, Appearance appOn)
	{
	    Shape3D shape = new ImagePanel(sizeOff, sizeOff);
	    shape.setAppearance(appOff);
	    addChild(shape);
	    if (appOff != appOn) {
                addListener(
                    new MouseEnteredEventAdapter(
                        new AppearanceChangeAction(shape, appOn)));
	    }
	    if (sizeOff != sizeOn) {
                addListener(
                    new MouseEnteredEventAdapter(
                        new ScaleActionBoolean(this, sizeOn/sizeOff, 100)));
	    }
	}
    }
    
    private class HelpThumbnail extends Thumbnail {
        private HelpThumbnail(float width, float height, float scale, Texture tex) {   
            GlassyPanel thumbnailDeco
                = new GlassyPanel(
                    width + decoWidth * 2,
                    height + decoWidth * 2, 
                    bodyDepth * 2, 
                    bodyApp);
            
            Shape3D bodyShadow
                = new RectShadow(
                    width + decoWidth * 2,
                    height + decoWidth * 2,
                    shadowN * 2, shadowE * 2, shadowS * 2, shadowW * 2, 
                    shadowI,
                    -bodyDepth * 2,
                    0.3f);
            
            SimpleAppearance tnApp 
                = new SimpleAppearance(1.0f, 1.0f, 1.0f, 0.75f,
                    SimpleAppearance.ENABLE_TEXTURE 
                        | SimpleAppearance.DISABLE_CULLING);
            tnApp.setTexture(tex);
            
            FuzzyEdgePanel body 
                = new FuzzyEdgePanel(width, height, decoWidth * 0.5f, tnApp);
            
            Component3D scalingCont = new Component3D();
            scalingCont.addChild(thumbnailDeco);
            scalingCont.addChild(bodyShadow);
            scalingCont.addChild(body);
            scalingCont.setScale(scale);
            addChild(scalingCont);
            
            setPreferredSize(
                    new Vector3f(width * scale, height * scale, bodyDepth * scale));
        }
    }
}


