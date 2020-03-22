/**
 * Project Looking Glass
 *
 * $RCSfile: SourceWindow.java,v $
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
 * $Date: 2006-06-07 23:59:52 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.apps.callviewer;

import javax.media.j3d.QuadArray;
import javax.vecmath.*;
import java.net.URL;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.*;
import java.awt.*;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.sg.*;
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
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 * Very very preliminary help app whcih just shows the usage guide
 * on the screen.  Intended to become more sophisticated app as we
 * evolve the system...
 */
public class SourceWindow extends Frame3D {
    
    // Scale for geometry, screen width/number of characters
    private static final float geometryScale = 0.01f/81f;
    private URL sourceURL;
    private ArrayList<CalledByData> calledBy = new ArrayList();
    private SourceTexture sourceTexture;
    private float windowWidth;
    private float windowHeight;
    private int lineCount;      // Total number of lines in source file
        
    public SourceWindow(URL sourceURL, Vector3f location) {
        if (sourceURL==null) {
            logger.warning("NULL url passed to SourceWindow");
            return;
        }
        setName(sourceURL.toExternalForm());
        this.sourceURL = sourceURL;        
        
        sourceTexture = buildTexture(sourceURL);
        windowHeight = sourceTexture.getUsedHeight()*geometryScale;
        windowWidth = sourceTexture.getUsedWidth()*geometryScale;
        
        setPreferredSize(new Vector3f(windowWidth, windowHeight, 0.001f));
        SimpleAppearance app 
            = new SimpleAppearance(1.0f, 1.0f, 1.0f,
                SimpleAppearance.ENABLE_TEXTURE 
                    | SimpleAppearance.DISABLE_CULLING);
        app.setTexture(sourceTexture);
        
        FuzzyEdgePanel body 
            = new FuzzyEdgePanel(windowWidth, windowHeight, 0f, app);
        body.setSize(windowWidth, windowHeight, 
                sourceTexture.getWidthScale(), 
                sourceTexture.getHeightScale());
        
        Component3D comp = new Component3D();
        comp.addChild(body);
        addChild(comp);
        
        setCapability(Frame3D.ALLOW_LOCAL_TO_VWORLD_READ);
        
        setThumbnail(new Thumbnail());
        changeTranslation( location, 0);
        changeVisible(true);
        
//        this.addListener(
//            new MouseEnteredEventAdapter(
//                new TooltipActionBoolean(this, sourceURL.getFile())));

    }
    
    public void addCalledBy( int localLineNo, SourceWindow caller, int callerLineNo, boolean direct ) {
        CalledByData cbd = new CalledByData(localLineNo, caller, callerLineNo, direct);
        calledBy.add( cbd );
        addChild(cbd);
    }
    
    /**
     * Return number of lines in this source file
     */
    public int getLineCount() {
        return lineCount;
    }
    
    /**
     * Returns position of this line number in vworld
     */
    public Point3f getLinePos(int lineNumber) {
        Transform3D t3d = new Transform3D();
        getLocalToVworld(t3d);
        Point3f ret = new Point3f(0f, (windowHeight/2)-lineNumber*(windowHeight/lineCount), 0f);
        t3d.transform(ret);
        
        return ret;
    }
    
    /**
     * Parse the source file and build a texture representing the file
     */
    private SourceTexture buildTexture(URL sourceFile) {
        SourceTexture sourceTexture;
        ArrayList<LineData> lineLengths = new ArrayList();
        try {            
            BufferedReader reader = new BufferedReader(new InputStreamReader(sourceFile.openStream()));
            String line;
            do {
                line = reader.readLine();
                if (line!=null)
                    lineLengths.add(new LineData(line));
            } while(line!=null);
            reader.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
        
        lineCount = lineLengths.size();
        
        int width = 81;
        int height = lineLengths.size()*2;
        BufferedImage im = new BufferedImage(getPowerOfTwoUpperBound(width),
                                             getPowerOfTwoUpperBound(height), 
                                             BufferedImage.TYPE_INT_RGB );
        Graphics g = im.getGraphics();
        
        int lineNumber = 0;
        int yOrigin = im.getHeight()-height;
        g.setColor(Color.WHITE);
        g.fillRect(0,yOrigin, width, height);
        g.setColor(Color.GRAY);
        for(LineData l : lineLengths) {
            l.drawLine(g, lineNumber++, yOrigin);
        }
        
        sourceTexture = new SourceTexture(im,width,height);
        
        return sourceTexture;
    }
    
    private static int getPowerOfTwoUpperBound(int value) {
        
        if (value < 1)
            return value;
        
        int powerValue = 1;
        
        for (;;) {
            powerValue *= 2;
            if (value < powerValue) {
                return powerValue;
            }
        }
    }   
    
    class LineData {
        private int length;
        private int firstChar=-1;
        static final float scale = 1f;
        
        public LineData(String line) {
            this.length = line.length();
            char c = ' ';
            
            for(int i=0; i<length && firstChar==-1; i++) {
                c = line.charAt(i);
                if (!Character.isSpaceChar(c))
                    firstChar = i;
            }            
        }
        
        public void drawLine(Graphics g, int lineNumber, int yOrigin) {
            if (firstChar==-1)
                return;
            int y = yOrigin+lineNumber*2;
            g.drawLine((int)(firstChar*scale),y,(int)(length*scale), y);
        }
    }
    
    class SourceTexture extends Texture2D {
        
        private float widthScale;
        private float heightScale;
        private int usedWidth;
        private int usedHeight;
        
        /**
         * @param image power of 2 image
         * @param usedWidth actual width of image used
         * @param usedHeight actual height of image used
         */
        public SourceTexture( BufferedImage image, int usedWidth, int usedHeight ) {
            super(Texture2D.BASE_LEVEL, Texture2D.RGB, image.getWidth(), image.getHeight());
            ImageComponent2D ic = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, image);
            
            setMinFilter(Texture.BASE_LEVEL_LINEAR);
            setMagFilter(Texture.BASE_LEVEL_LINEAR);
            setImage(0,ic);
            
            this.usedWidth = usedWidth;
            this.usedHeight = usedHeight;
            this.widthScale = ((float)image.getWidth())/usedWidth;
            this.heightScale = ((float)image.getHeight())/usedHeight;
        }
        
        public float getWidthScale() {
            return widthScale;
        }
        
        public float getHeightScale() {
            return heightScale;
        }
        
        public int getUsedWidth() {
            return usedWidth;
        }
        
        public int getUsedHeight() {
            return usedHeight;
        }
    }
    
    class CalledByData extends Component3D {
        private int localLineNo;
        private int callerLineNo;
        private SourceWindow caller;
        private Point3f localPoint;
        
        public CalledByData( int localLineNo, SourceWindow caller, int callerLineNo, boolean direct ) {
            this.localLineNo = localLineNo;
            this.caller = caller;
            this.callerLineNo = callerLineNo;
            
            localPoint = new Point3f(0f, (windowHeight/2)-localLineNo*(windowHeight/lineCount), 0f);
            Point3f callerPointVW = caller.getLinePos(callerLineNo);
            Transform3D t3d = new Transform3D();
            SourceWindow.this.getLocalToVworld(t3d);
            t3d.invert();
            t3d.transform(callerPointVW);
            
            Color3f color;
            
            if (direct)
                color = new Color3f(1f,0f,0f);
            else
                color = new Color3f(0f,0f,1f);
            
            addChild(new Line3D(localPoint, callerPointVW, color));
        }
    }
    
}


