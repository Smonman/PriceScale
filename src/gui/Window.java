package gui;

import gui.util.ResultTableFactory;
import shopping.cart.ShoppingCart;
import shopping.list.ShoppingList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;

public class Window {

    private final JPanel contentPanel;
    private final JLabel statusLabel;
    private final JTable resultTable;

    public Window() {
        this.contentPanel = new JPanel(new BorderLayout());
        this.statusLabel = new JLabel();
        this.resultTable = new JTable(ResultTableFactory.get());
        init();
        statusLabel.setText("");
    }

    private void init() {
        final JFrame frame = new JFrame("Simple Raytracer - Render Output");
        final JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(new LineBorder(Color.GRAY));
        statusBar.add(statusLabel);
        contentPanel.add(new JScrollPane(resultTable), BorderLayout.CENTER);
        resultTable.setFillsViewportHeight(true);
        contentPanel.add(statusBar, BorderLayout.SOUTH);
        frame.add(contentPanel);
        frame.setSize(960, 540);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void setTableData(final ShoppingList shoppingList,
                             final List<ShoppingCart> shoppingCarts
    ) {
        ResultTableFactory.addShoppingCarts(shoppingCarts);
        ResultTableFactory.setShoppingList(shoppingList);
        ResultTableFactory.compileTable();
        statusLabel.setText("Finished");
    }
}
