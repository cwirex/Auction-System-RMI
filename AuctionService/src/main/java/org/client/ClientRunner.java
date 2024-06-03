package org.client;

import org.aslib.Service;

public class ClientRunner implements GUIHolder {
    private GUI gui;
    private final Service service;

    public ClientRunner(Service service) {
        this.service = service;
    }

    void start(){
        gui = new MainGUI(this, service);
        gui.showMenuWithSelection();
    }

    @Override
    public GUI getGUI() {
        return gui;
    }

    @Override
    public void swapGUI(GUI gui) {
        this.gui = gui;
        gui.showMenuWithSelection();
    }
}
