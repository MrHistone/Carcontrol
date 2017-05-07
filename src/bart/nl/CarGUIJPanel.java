package bart.nl;

import java.awt.Graphics;
import javax.swing.JPanel;

public class CarGUIJPanel extends JPanel {

    private int controlX;
    private int controlY;
    private int controlSize = 16;
    private int circleSize;
    private int radiusBigCircle;
    
    
    
    @Override
    protected void paintComponent(Graphics g) {
        if (circleSize == 0){
            circleSize = getWidth() - 10;
            radiusBigCircle = circleSize / 2;
        }

        super.paintComponent(g);
        g.drawOval(5, 5, circleSize, circleSize);
        g.fillOval(controlX, controlY, controlSize, controlSize);
        
    }

    protected void setXY(int x, int y){
        this.controlX = x;
        this.controlY = y;
    }
    
    protected int getControlX(){
        return controlX;
    }
    
    protected int getControlY(){
        return controlY;
    }
    
    protected void setControlX(int x){
        this.controlX = x;
        // Small oval needs to stay in the larger circle.

        // Steps:
        // Get X relative to center of big circle. (Center = 0)
        
        // Get Y relative to center of big circle. (Center = 0)
        
        // Calculate the hypotenuse with positive values of X and Y.
        
        // If hypotenuse gets larger with new X than the 
        // radius of the big circle, do not make X larger (or smaller).
        
        
        if (controlX < 5){
            controlX = 5;
        }
        
        if (controlX > 5 + circleSize){
            controlX = circleSize;
        }
        
    }
    
    protected void setControlY(int y){
        this.controlY = y;
        // Small oval needs to stay in the larger oval.
        if (controlY < 5){
            controlY = 5;
        }
        
        if (controlY > 5 + circleSize){
            controlY = circleSize;
        }
        
        
        
        
    }
    
    
    protected void centerControl(){
        controlX = getWidth() / 2;
        controlX = controlX - controlSize / 2;
        controlY = getHeight() / 2;
        controlY = controlY - controlSize / 2;
    }
    
    
    
}
