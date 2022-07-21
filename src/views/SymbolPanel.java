package views;

import javax.swing.*;
import java.awt.*;

public class SymbolPanel extends JPanel {

    private final boolean blocked;
    private final boolean isReport;

    public SymbolPanel(boolean blocked, boolean isReport){
        setBackground(Color.decode("#FDFEFE"));
        this.isReport = isReport;
        this.blocked = blocked;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3));
        if(blocked){
            paintBlocked(g2, width, height);
        }else{
            paintNoBlocked(g2, width, height);
        }
    }

    private void paintBlocked(Graphics2D g2 ,int width, int height){
        g2.setColor(Color.GREEN);
        if(isReport){
            g2.drawLine((int)(width * 0.45), (int)(height * 0.55), (int)(width * 0.5), (int)(height * 0.8));
            g2.drawLine((int)(width * 0.5), (int)(height * 0.8), (int)(width * 0.55), (int)(height * 0.2));
        }else{
            g2.drawLine((int)(width * 0.15), (int)(height * 0.57), (int)(width * 0.35), (int)(height * 0.7));
            g2.drawLine((int)(width * 0.35), (int)(height * 0.7), (int)(width * 0.5), (int)(height * 0.3));
        }
    }

    private void paintNoBlocked(Graphics2D g2 ,int width, int height) {
        g2.setColor(Color.RED);
        if (isReport) {
            g2.drawLine((int)(width * 0.45), (int)(height * 0.2), (int)(width * 0.55), (int)(height * 0.8));
            g2.drawLine((int)(width * 0.55), (int)(height * 0.2), (int)(width * 0.45), (int)(height * 0.8));
        }else{
            g2.drawLine((int)(width * 0.15), (int)(height * 0.3), (int)(width * 0.45), (int)(height * 0.7));
            g2.drawLine((int)(width * 0.45), (int)(height * 0.3), (int)(width * 0.15), (int)(height * 0.7));
        }
    }
}
