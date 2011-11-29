/**
 * Copyright (C) 2010, 2011 by Arne Kesting, Martin Treiber, Ralph Germ, Martin Budden <info@movsim.org>
 * ----------------------------------------------------------------------
 * 
 * This file is part of
 * 
 * MovSim - the multi-model open-source vehicular-traffic simulator
 * 
 * MovSim is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * MovSim is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with MovSim. If not, see <http://www.gnu.org/licenses/> or
 * <http://www.movsim.org>.
 * 
 * ----------------------------------------------------------------------
 */
package org.movsim.output.fileoutput;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import org.movsim.input.ProjectMetaData;
import org.movsim.output.FloatingCars;
import org.movsim.simulator.MovsimConstants;
import org.movsim.simulator.roadnetwork.LaneSegment;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.movsim.simulator.vehicles.Vehicle;
import org.movsim.utilities.ObserverInTime;
import org.movsim.utilities.impl.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class FloatingCars.
 */
public class FileFloatingCars implements ObserverInTime {

    private static final String extensionFormat = ".V%06d.csv";
    private static final String outputHeading = MovsimConstants.COMMENT_CHAR
            + "     t[s], lane,       x[m],     v[m/s],   a[m/s^2],     aModel,     gap[m],    dv[m/s],  distToTrafficlight[m],  fuelFlow[ml/s]";

    // note: number before decimal point is total width of field, not width of
    // integer part
    private static final String outputFormat = "%10.2f, %4d, %10.1f, %10.4f, %10.5f, %10.3f, %10.3f, %10.5f, %10.2f, %f%n";

    /** The Constant logger. */
    final static Logger logger = LoggerFactory.getLogger(FileFloatingCars.class);

    private final String endingFile = ".csv";
    private final HashMap<Integer, PrintWriter> hashMap;
    private final FloatingCars floatingCars;

    /**
     * Instantiates a new floating cars impl.
     * 
     * @param projectName
     *            the project name
     * @param floatingCars
     *            the floating cars
     */
    public FileFloatingCars(FloatingCars floatingCars) {
        logger.debug("Cstr. FloatingCars");
        this.floatingCars = floatingCars;
        floatingCars.registerObserver(this);

        final ProjectMetaData projectMetaData = ProjectMetaData.getInstance();
        final String regex = projectMetaData.getProjectName() + "[.]V\\d+" + endingFile;
        FileUtils.deleteFileList(projectMetaData.getOutputPath(), regex);

        hashMap = new HashMap<Integer, PrintWriter>(149, 0.75f);

        final List<Integer> fcdList = floatingCars.getFcdList();
        for (final Integer i : fcdList) {
            addFCD(i);
        }
    }

    /**
     * Adds the fcd.
     * 
     * @param vehNumber
     *            the veh number
     */
    private void addFCD(int vehNumber) {
        final String filename = createFileName(vehNumber, endingFile);
        final PrintWriter fstr = FileUtils.getWriter(filename);
        hashMap.put(vehNumber, fstr);
        fstr.println(outputHeading);
        fstr.flush();
    }

    /**
     * Write output.
     * 
     * @param updateTime
     *            the update time
     */
    public void writeOutput(double updateTime) {

        final RoadSegment roadSegment = floatingCars.getRoadSegment();
        final int laneCount = roadSegment.laneCount();
        for (int lane = 0; lane < laneCount; ++lane) {
            final LaneSegment laneSegment = roadSegment.laneSegment(lane);
            for (final Vehicle vehOnLane : laneSegment) {
                if (!vehOnLane.isFromOnramp()) {
                    // only mainroad vehicles
                    final int vehNumber = vehOnLane.getVehNumber();
                    if (hashMap.containsKey(vehNumber)) {
                        final Vehicle frontVeh = laneSegment.frontVehicle(vehOnLane);
                        writeData(updateTime, vehOnLane, frontVeh, hashMap.get(vehNumber));
                    }
                }
            }
        }
    }

    /**
     * Creates the file name.
     * 
     * @param i
     *            the i
     * @param ending
     *            the ending
     * @return the string
     */
    private String createFileName(int i, String ending) {
        final ProjectMetaData projectMetaData = ProjectMetaData.getInstance();
        final String outputPath = projectMetaData.getOutputPath();
        final String filename = outputPath + File.separator + projectMetaData.getProjectName()
                + String.format(extensionFormat, i);
        return (filename);
    }

    /**
     * Write data.
     * 
     * @param time
     *            the time
     * @param veh
     *            the veh
     * @param frontVeh
     *            the front veh
     * @param fstr
     *            the fstr
     */
    private void writeData(double time, Vehicle veh, Vehicle frontVeh, PrintWriter fstr) {
        // note: number before decimal point is total width of field, not width
        // of integer part
        fstr.printf(outputFormat, time, veh.getLane(), veh.getPosition(), veh.getSpeed(), veh.getAcc(), veh.accModel(),
                veh.getNetDistance(frontVeh), veh.getRelSpeed(frontVeh), veh.getDistanceToTrafficlight(),
                1000 * veh.getActualFuelFlowLiterPerS());
        fstr.flush();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.movsim.utilities.ObserverInTime#notifyObserver(double)
     */
    @Override
    public void notifyObserver(double time) {
        writeOutput(time);
    }
}
