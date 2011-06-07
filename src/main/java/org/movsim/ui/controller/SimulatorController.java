/**
 * 
 * Copyright (C) 2010 by Ralph Germ (http://www.ralphgerm.de)
 * 
 */
package org.movsim.ui.controller;

import org.movsim.simulator.Simulator;
import org.movsim.ui.desktop.SimulatorView;

/**
 * @author ralph
 * 
 */
public class SimulatorController implements ControllerInterface {

    private SimulatorView view;
    private Simulator model;
    private Thread simThread;

    public SimulatorController(Simulator model) {
        this.model = model;
        view = new SimulatorView(model, this);

        view.createControls();
        // view.createOutputViews();

        simThread = new Thread((Runnable) model);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.movsim.ui.desktop.ControllerInterface#start()
     */
    @Override
    public void start() {
        view.disableStart();
        view.enablePause();
        view.enableStop();
        // model.restart();
        simThread.start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.movsim.ui.desktop.ControllerInterface#stop()
     */
    @Override
    public void stop() {
        view.disableStop();
        view.enableStart();
        view.disablePause();
        // model.stop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.movsim.ui.desktop.ControllerInterface#pause()
     */
    @Override
    public void pause() {
        view.enableStart();
        view.enableStop();
        view.enablePause();

        // test
        // view.updateViews();
    }

}