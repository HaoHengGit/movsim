/**
 * Copyright (C) 2010, 2011 by Arne Kesting <movsim@akesting.de>, 
 *                             Martin Treiber <treibi@mtreiber.de>,
 *                             Ralph Germ <germ@ralphgerm.de>,
 *                             Martin Budden <mjbudden@gmail.com>
 *
 * ----------------------------------------------------------------------
 * 
 *  This file is part of 
 *  
 *  MovSim - the multi-model open-source vehicular-traffic simulator 
 *
 *  MovSim is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  MovSim is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with MovSim.  If not, see <http://www.gnu.org/licenses/> or
 *  <http://www.movsim.org>.
 *  
 * ----------------------------------------------------------------------
 */
package org.movsim.simulator.vehicles.longmodel.accelerationmodels.impl;

import org.movsim.input.model.vehicle.longModel.ModelInputDataNSM;
import org.movsim.simulator.impl.MyRandom;
import org.movsim.simulator.vehicles.Vehicle;
import org.movsim.simulator.vehicles.VehicleContainer;
import org.movsim.simulator.vehicles.longmodel.accelerationmodels.AccelerationModel;
import org.movsim.simulator.vehicles.longmodel.accelerationmodels.AccelerationModelCategory;

// TODO: Auto-generated Javadoc
// Nagel-Schreckenberg or Barlovic-Model
// paper reference
/**
 * The Class NSM.
 */
public class NSM extends LongitudinalModelImpl implements AccelerationModel {

    // unit time for CA:
    private static final double dtCA = 1; // update timestep for CA !!

    private final double v0;
    private final double pTroedel;
    private final double pSlowToStart; // slow-to-start rule for Barlovic model

    /**
     * Instantiates a new nSM.
     * 
     * @param modelName
     *            the model name
     * @param parameters
     *            the parameters
     */
    public NSM(String modelName, ModelInputDataNSM parameters) {
        super(modelName, AccelerationModelCategory.CELLULAR_AUTOMATON);
        this.v0 = parameters.getV0();
        this.pTroedel = parameters.getTroedel();
        this.pSlowToStart = parameters.getSlowToStart();
    }

    // copy constructor
    /**
     * Instantiates a new nSM.
     * 
     * @param nsmToCopy
     *            the nsm to copy
     */
    public NSM(NSM nsmToCopy) {
        super(nsmToCopy.modelName(), nsmToCopy.getModelCategory());
        this.v0 = nsmToCopy.getV0();
        this.pTroedel = nsmToCopy.getTroedel();
        this.pSlowToStart = nsmToCopy.getSlowToStart();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.movsim.simulator.vehicles.longmodel.accelerationmodels.AccelerationModel
     * #acc(org.movsim.simulator.vehicles.Vehicle,
     * org.movsim.simulator.vehicles.VehicleContainer, double, double, double)
     */
    @Override
    public double acc(Vehicle me, VehicleContainer vehContainer, double alphaT, double alphaV0, double alphaA) {
        // Local dynamical variables
        final Vehicle vehFront = vehContainer.getLeader(me);
        final double s = me.netDistance(vehFront);
        final double v = me.speed();
        final double dv = me.relSpeed(vehFront);
        return accSimple(s, v, dv, alphaT, alphaV0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.movsim.simulator.vehicles.longmodel.accelerationmodels.AccelerationModel
     * #accSimple(double, double, double)
     */
    @Override
    public double accSimple(double s, double v, double dv) {
        return accSimple(s, v, dv, 1, 1);
    }

    /**
     * Acc simple.
     * 
     * @param s
     *            the s
     * @param v
     *            the v
     * @param dv
     *            the dv
     * @param alphaT
     *            the alpha t
     * @param alphaV0
     *            the alpha v0
     * @return the double
     */
    private double accSimple(double s, double v, double dv, double alphaT, double alphaV0) {
        final int v0Loc = (int) (alphaV0 * v0 + 0.5); // adapt v0 spatially
        final int vLoc = (int) (v + 0.5);
        int vNew = 0;

        final double r1 = MyRandom.nextDouble();
        final double pb = (vLoc < 1) ? pSlowToStart : pTroedel;
        final int troedel = (r1 < pb) ? 1 : 0;

        final int sLoc = (int) (s + 0.5);
        vNew = Math.min(vLoc + 1, v0Loc);
        vNew = Math.min(vNew, sLoc);
        vNew = Math.max(0, vNew - troedel);

        return ((vNew - vLoc) / dtCA);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.movsim.simulator.vehicles.longmodel.accelerationmodels.impl.
     * LongitudinalModelImpl#parameterV0()
     */
    @Override
    public double parameterV0() {
        return v0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.movsim.simulator.vehicles.longmodel.accelerationmodels.impl.
     * LongitudinalModelImpl#getRequiredUpdateTime()
     */
    @Override
    public double getRequiredUpdateTime() {
        return dtCA; // cellular automaton requires specific dt
    }

    /**
     * Gets the v0.
     * 
     * @return the v0
     */
    public double getV0() {
        return v0;
    }

    /**
     * Gets the troedel.
     * 
     * @return the troedel
     */
    public double getTroedel() {
        return pTroedel;
    }

    /**
     * Gets the slow to start.
     * 
     * @return the slow to start
     */
    public double getSlowToStart() {
        return pSlowToStart;
    }

}