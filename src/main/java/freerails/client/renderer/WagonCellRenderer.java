package freerails.client.renderer;

import freerails.model.cargo.CargoType;

import javax.swing.*;
import java.awt.*;

public class WagonCellRenderer implements ListCellRenderer {

    private final Component[] labels;

    public WagonCellRenderer(ListModel w2lma, RendererRoot rendererRoot) {

        labels = new Component[w2lma.getSize()];
        for (int i = 0; i < w2lma.getSize(); i++) {
            JLabel label = new JLabel();
            label.setFont(new Font("Dialog", 0, 12));
            Image image = rendererRoot.getWagonImages(i).getSideOnImage();
            int height = image.getHeight(null);
            int width = image.getWidth(null);
            int scale = height / 10;

            Icon icon = new ImageIcon(image.getScaledInstance(width / scale, height / scale, Image.SCALE_FAST));
            label.setIcon(icon);
            labels[i] = label;
        }
    }

    /**
     *
     * @param list
     * @param value value to display
     * @param index cell index
     * @param isSelected is the cell selected
     * @param cellHasFocus the list and the cell have the focus
     * @return
     */
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus)  {
        if (index >= 0 && index < labels.length) {
            CargoType cargoType = (CargoType) value;
            String text = "<html><body>" + (isSelected ? "<strong>" : "") + cargoType.getDisplayName() + (isSelected ? "</strong>" : "&nbsp;&nbsp;&nbsp;&nbsp;"/*
             * padding to stop
             * word wrap due to
             * greater width of
             * strong font
             */) + "</body></html>";
            ((JLabel) labels[index]).setText(text);
            return labels[index];
        }
        return null;
    }
}
