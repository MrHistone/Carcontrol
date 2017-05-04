package bart.nl;

import java.awt.Graphics;
import javax.swing.JPanel;

public class CarGUIJPanel extends JPanel {

    private int controlX;
    private int controlY;
    private int controlSize = 16;
    private int circleSize;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        circleSize = getWidth() - 10;
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
