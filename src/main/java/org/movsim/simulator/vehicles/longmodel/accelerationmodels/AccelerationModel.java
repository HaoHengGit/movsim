/**
 * Copyright (C) 2010, 2011 by Arne Kesting, Martin Treiber,
 *                             Ralph Germ, Martin Budden
 *                             <info@movsim.org>
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
package org.movsim.simulator.vehicles.longmodel.accelerationmodels;

import org.movsim.simulator.vehicles.Vehicle;
import org.movsim.simulator.vehicles.VehicleContainer;

// TODO: Auto-generated Javadoc
/**
 * The Interface AccelerationModel.
 */
public interface AccelerationModel {

    /**
     * Model name.
     * 
     * @return the string
     */
    String modelName();

    /**
     * Checks if is cellular automaton.
     * 
     * @return true, if is cellular automaton
     */
    boolean isCA();

    /**
     * Checks if is iterated map.
     * 
     * @return true, if is iterated map
     */
    boolean isIteratedMap();

    /**
     * Gets the model category.
     * 
     * @return the model category
     */
    int getModelCategory();

    /**
     * Gets the required update time.
     * 
     * @return the required update time
     */
    double getRequiredUpdateTime();

    /**
     * Parameter v0. The desired velocity (m/s).
     * 
     * @return the double
     */
    double parameterV0();

    /**
     * Acc. Calculates the acceleration.
     * 
     * @param me
     *            the subject vehicle
     * @param vehContainer
     *            the vehicle container
     * @param alphaT
     *            the alpha t
     * @param alphaV0
     *            the alpha v0
     * @param alphaA
     *            the alpha a
     * @return the double. The acceleration of the subject vehicle.
     */
    double acc(Vehicle me, VehicleContainer vehContainer, double alphaT, double alphaV0, double alphaA);

    /**
     * Acc simple.
     * 
     * @param s
     *            the s
     * @param v
     *            the v
     * @param dv
     *            the dv
     * @return the double
     */
    double accSimple(double s, double v, double dv);

    /**
     * Removes the observer.
     */
    void removeObserver();

}
