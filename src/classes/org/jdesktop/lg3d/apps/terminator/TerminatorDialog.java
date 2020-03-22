/**
 * Project Looking Glass
 *
 * $RCSfile: TerminatorDialog.java,v $
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
 * $Date: 2006-09-07 22:36:06 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.apps.terminator;

import java.awt.Font;
import java.net.URL;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.action.ScaleActionBoolean;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MousePressedEventAdapter;
import org.jdesktop.lg3d.utils.shape.Disc;
import org.jdesktop.lg3d.utils.shape.OriginTranslation;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.utils.shape.Text2D;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;

/**
 * A confirmation dialog for quitting the Looking Glass Desktop
 * TODO: Need to make this modal.
 */
public class TerminatorDialog extends Frame3D {

    public static void main (String[] args) {
	TerminatorDialog td = new TerminatorDialog();
    }

    private Frame3D terminatorFrame;

    public enum ButtonType { YES, NO };

    public TerminatorDialog() {
	terminatorFrame = this;

	addChild(createText("Are you sure you want to exit Looking Glass ?"));
	addChild(createButton(ButtonType.YES));
	addChild(createButton(ButtonType.NO));

	changeEnabled(true);
	changeVisible(true);
    }

    private class OkActionNoArg implements ActionNoArg {
	public void performAction(LgEventSource source) {
	    System.exit(0);
	}
    }

    private class CancelActionNoArg implements ActionNoArg {
	public void performAction(LgEventSource source) {
	    terminatorFrame.changeEnabled(false);
	}
    }

    private ActionNoArg createAction(ButtonType type) {
	return (type == ButtonType.YES) ? new OkActionNoArg() : new CancelActionNoArg();
    }

    private URL getIconURL(ButtonType type) {
	String pngFile = (type == ButtonType.YES) ? "okButton.png" : "closeButton.png";

	return this.getClass().getClassLoader().getResource(
			"org/jdesktop/lg3d/apps/terminator/resources/" + pngFile);
    }

    private Component3D createButton(ButtonType type) {
        Component3D button = new Component3D();
        
        Appearance app = new SimpleAppearance(
                1.0f, 1.0f, 1.0f, 1.0f,
                SimpleAppearance.ENABLE_TEXTURE
                | SimpleAppearance.DISABLE_CULLING);

        try {
            ((SimpleAppearance)app).setTexture(getIconURL(type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        button.addChild(new Disc(0.015f, 36, app));
        button.setTranslation((type == ButtonType.YES) ? -0.02f : 0.02f, -0.01f, 0.048f);

        button.addListener(new MouseClickedEventAdapter(createAction(type)));
        button.addListener(
                new MouseEnteredEventAdapter(
		    new ScaleActionBoolean(button,1.2f, 100)));
        button.addListener(
                new MousePressedEventAdapter(
		    new ScaleActionBoolean(button,1.15f, 100)));
        button.setMouseEventPropagatable(true);
        return button;
    }

    private Component3D createText(String label) {
        Component3D textComp = new Component3D();

        textComp.setPreferredSize(new Vector3f(0.0f, 0.0f, 0.0f));

        Text2D text = new Text2D(label, new Color3f(1.0f, 1.0f, 1.0f), "SansSerif", 24, Font.BOLD);
        text.setRectangleScaleFactor(1.0f / 4096.0f);
        textComp.addChild(text);

        Text2D textShadow = new Text2D(label, new Color3f(0.3f, 0.3f, 0.3f), "SansSerif", 24, Font.BOLD);
        textShadow.setRectangleScaleFactor(1.0f / 4096.0f);
        textComp.addChild(new OriginTranslation(textShadow, new Vector3f(0.0003f, -0.0003f, -0.0001f)));

	textComp.setTranslation(-0.07f, 0.01f, 0.048f);
	return textComp;
    }
}
