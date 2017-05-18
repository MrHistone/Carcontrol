package bart.nl.client;

import bart.nl.Defaults;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JToggleButton;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class ClientGUI extends javax.swing.JFrame {

    private boolean forward = false;
    private boolean backward = false;
    private boolean right = false;
    private boolean left = false;
    private boolean forwardSignal = false;
    private boolean backwardSignal = false;
    private boolean rightSignal = false;
    private boolean leftSignal = false;
    private Defaults.Momentum momentum;
    private boolean momentumDetermined = false;
    private Client client;
    private StyledDocument styledDocument;
    private KeyboardFocusManager keyboardFocusManager;
    private LocalKeyEventDispatcher localKeyEventDispatcher;
    private boolean isConnected = false;
    private ArrayList<Controller> foundControllers;

    private CarGUI carGUI;
    public ClientGUI() {

        carGUI = new CarGUI();
        initComponents();
        btBackward.setBackground(Color.red);
        btForward.setBackground(Color.red);
        btRight.setBackground(Color.red);
        btLeft.setBackground(Color.red);
        server.setEnabled(false);
        port.setEnabled(false);
        // Button Connect
        connect.setEnabled(true);

        requestFocusInWindow();
        client = new Client(server.getText(), Integer.valueOf(port.getText()), this);
//        client.start();

        styledDocument = txtTextPane.getStyledDocument();

        // Global Keylistener so the entire Frame will respond.
        localKeyEventDispatcher = new LocalKeyEventDispatcher();
        keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        // Add my own KeyEventDispatcher. Now not every component needs to have a listener.
        keyboardFocusManager.addKeyEventDispatcher(localKeyEventDispatcher);

        // Controller
        foundControllers = new ArrayList<>();
        searchForControllers();
        // If at least one controller was found we start showing controller data on window.
        if (!foundControllers.isEmpty()) {
            startShowingControllerData();
        } else {
            showServerMessage("No controller found.");
        }

    }

    private void determineChangeInMomentum() {
        momentumDetermined = false;
        if (forward == true && forwardSignal == false) {
            // Move forward.
            forwardSignal = true;
            momentumDetermined = true;
            sendSignal(Defaults.Momentum.FORWARD);
        }

        if (forward == false && forwardSignal == true) {
            // Stop moving
            forwardSignal = false;
            momentumDetermined = true;
            sendSignal(Defaults.Momentum.STOP);
        }

        if (backward == true && backwardSignal == false) {
            // Move back.
            backwardSignal = true;
            momentumDetermined = true;
            sendSignal(Defaults.Momentum.BACKWARD);
        }

        if (backward == false && backwardSignal == true) {
            // Stop moving
            backwardSignal = false;
            momentumDetermined = true;
            sendSignal(Defaults.Momentum.STOP);
        }

        if (right == true && rightSignal == false) {
            // Move right.
            rightSignal = true;
            momentumDetermined = true;
            sendSignal(Defaults.Momentum.RIGHT);
        }

        if (right == false && rightSignal == true) {
            // Stop moving
            rightSignal = false;
            momentumDetermined = true;
            sendSignal(Defaults.Momentum.STOP);
        }

        if (left == true && leftSignal == false) {
            // Move left.
            leftSignal = true;
            momentumDetermined = true;
            sendSignal(Defaults.Momentum.LEFT);
        }

        if (left == false && leftSignal == true) {
            // Stop moving
            leftSignal = false;
            momentumDetermined = true;
            sendSignal(Defaults.Momentum.STOP);
        }
    }

    private void sendSignal(Defaults.Momentum momentum) {
        switch (momentum) {
            case BACKWARD:
                btBackward.setBackground(Color.green);
                break;
            case FORWARD:
                btForward.setBackground(Color.green);
                break;
            case LEFT:
                btLeft.setBackground(Color.green);
                break;
            case RIGHT:
                btRight.setBackground(Color.green);
                break;
            case STOP:
                btBackward.setBackground(Color.red);
                btForward.setBackground(Color.red);
                btRight.setBackground(Color.red);
                btLeft.setBackground(Color.red);
                break;
        }

        System.out.println("Momentum: " + momentum);
        if (isConnected) {
            client.sendMessage(momentum.toString());
        }

    }

    protected void showServerMessage(String serverMessage) {
        try {
            if (styledDocument.getLength() > 0) {
                serverMessage = "\n" + serverMessage;
            }
            Style s = styledDocument.getStyle("bold");
            styledDocument.insertString(styledDocument.getLength(), serverMessage, s);

            txtTextPane.setCaretPosition(styledDocument.getLength());
        } catch (BadLocationException e) {

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        server = new javax.swing.JTextField();
        port = new javax.swing.JTextField();
        connect = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btForward = new javax.swing.JButton();
        btLeft = new javax.swing.JButton();
        btRight = new javax.swing.JButton();
        btBackward = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtTextPane = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        server.setText("192.168.2.12");

        port.setText("1500");

        connect.setText("Connect");
        connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(server, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(connect)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(server, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(connect))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btForward.setText("Forward");
        btForward.setMaximumSize(new java.awt.Dimension(100, 50));
        btForward.setMinimumSize(new java.awt.Dimension(100, 50));
        btForward.setPreferredSize(new java.awt.Dimension(100, 50));

        btLeft.setText("Left");
        btLeft.setMaximumSize(new java.awt.Dimension(100, 50));
        btLeft.setMinimumSize(new java.awt.Dimension(100, 50));
        btLeft.setPreferredSize(new java.awt.Dimension(100, 50));

        btRight.setText("Right");
        btRight.setMaximumSize(new java.awt.Dimension(100, 50));
        btRight.setMinimumSize(new java.awt.Dimension(100, 50));
        btRight.setPreferredSize(new java.awt.Dimension(100, 50));

        btBackward.setText("Backward");
        btBackward.setMaximumSize(new java.awt.Dimension(100, 50));
        btBackward.setMinimumSize(new java.awt.Dimension(100, 50));
        btBackward.setPreferredSize(new java.awt.Dimension(100, 50));

        jScrollPane1.setViewportView(txtTextPane);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btBackward, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btForward, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(106, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btForward, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btBackward, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectActionPerformed
        client.start();
        isConnected = true;

    }//GEN-LAST:event_connectActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientGUI().setVisible(true);
            }
        });
    }

    private void searchForControllers() {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        for (int i = 0; i < controllers.length; i++) {
            Controller controller = controllers[i];

            if (controller.getType() == Controller.Type.STICK
                    || controller.getType() == Controller.Type.GAMEPAD
                    || controller.getType() == Controller.Type.WHEEL
                    || controller.getType() == Controller.Type.FINGERSTICK) {
                // Add new controller to the list of all controllers.
                foundControllers.add(controller);

                // Add new controller to the list on the window.
                showServerMessage(controller.getName() + " - " + controller.getType().toString() + " type");
            }
        }
    }

    private void startShowingControllerData() {
        showServerMessage("Start showing Controller Data.");
        // This needs to be done in a different thread.
        ShowControllerData showControllerData = new ShowControllerData();
        Thread threadObj = new Thread(showControllerData);
        threadObj.start();

    }

    private class ShowControllerData implements Runnable {

        @Override
        public void run() {
            while (true) {
                // Currently selected controller.
                // Assume that there is only one controller or that the first one is used.
                int selectedControllerIndex = 0;
                Controller controller = foundControllers.get(selectedControllerIndex);

                // Pull controller for current data, and break while loop if controller is disconnected.
                if (!controller.poll()) {
                    showServerMessage("Controller disconnected!");
                    break;
                }

                // X axis and Y axis
                int xAxisPercentage = 0;
                int yAxisPercentage = 0;

                // Go trough all components of the controller.
                Component[] components = controller.getComponents();
                for (int i = 0; i < components.length; i++) {
                    Component component = components[i];
                    Component.Identifier componentIdentifier = component.getIdentifier();
                    
                // Buttons
                //if(component.getName().contains("Button")){ // If the language is not english, this won't work.
                if(componentIdentifier.getName().matches("^[0-9]*$")){ // If the component identifier name contains only numbers, then this is a button.
                    // Is button pressed?
                    boolean isItPressed = true;
                    if(component.getPollData() == 0.0f)
                        isItPressed = false;
                    
                    // Button index
                    String buttonIndex;
                    buttonIndex = component.getIdentifier().toString();
                    
                    
                    
                    // We know that this component was button so we can skip to next component.
                    continue;
                }
                    
                    
                    
                }

            }

        }

    }

    private class LocalKeyEventDispatcher implements KeyEventDispatcher {

        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            System.out.println("LocalKeyEventDispatcher");
            switch (e.getID()) {
                case KeyEvent.KEY_PRESSED:
                    switch (e.getKeyCode()) {
                        case 87:
                            forward = true;
                            break;
                        case 38:
                            forward = true;
                            break;
                        case 83:
                            backward = true;
                            break;
                        case 40:
                            backward = true;
                            break;
                        case 39:
                            right = true;
                            break;
                        case 68:
                            right = true;
                            break;
                        case 37:
                            left = true;
                            break;
                        case 65:
                            left = true;
                            break;
                        default:
                            forward = false;
                            backward = false;
                            right = false;
                            left = false;

                    }
                    determineChangeInMomentum();
                    break;
                case KeyEvent.KEY_RELEASED:
                    switch (e.getKeyCode()) {
                        case 87:
                            forward = false;
                            break;
                        case 38:
                            forward = false;
                            break;
                        case 83:
                            backward = false;
                            break;
                        case 40:
                            backward = false;
                            break;
                        case 39:
                            right = false;
                            break;
                        case 68:
                            right = false;
                            break;
                        case 37:
                            left = false;
                            break;
                        case 65:
                            left = false;
                            break;
                        default:
                            forward = false;
                            backward = false;
                            right = false;
                            left = false;

                    }
                    determineChangeInMomentum();
                    break;
                case KeyEvent.KEY_TYPED:
                    break;
                default:
                    break;
            }
            return false;
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBackward;
    private javax.swing.JButton btForward;
    private javax.swing.JButton btLeft;
    private javax.swing.JButton btRight;
    private javax.swing.JButton connect;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField port;
    private javax.swing.JTextField server;
    private javax.swing.JTextPane txtTextPane;
    // End of variables declaration//GEN-END:variables
}
