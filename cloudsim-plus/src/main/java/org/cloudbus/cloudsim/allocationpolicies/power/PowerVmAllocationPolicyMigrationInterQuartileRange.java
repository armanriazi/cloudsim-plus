/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.allocationpolicies.power;

import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.hosts.power.PowerHost;
import org.cloudbus.cloudsim.util.Log;
import org.cloudbus.cloudsim.hosts.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.selectionpolicies.power.PowerVmSelectionPolicy;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.util.MathUtil;

/**
 * A VM allocation policy that uses Inter Quartile Range (IQR)  to compute
 * a dynamic threshold in order to detect host over utilization.
 * <p>
 * <p>If you are using any algorithms, policies or workload included in the power package please cite
 * the following paper:</p>
 * <p>
 * <ul>
 * <li><a href="http://dx.doi.org/10.1002/cpe.1867">Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012</a>
 * </ul>
 *
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 3.0
 */
public class PowerVmAllocationPolicyMigrationInterQuartileRange extends PowerVmAllocationPolicyMigrationDynamicUpperThresholdAbstract {
    /**
     * Creates a PowerVmAllocationPolicyMigrationInterQuartileRange.
     *
     * @param vmSelectionPolicy          the policy that defines how VMs are selected for migration
     * @param safetyParameter            the safety parameter
     * @param fallbackVmAllocationPolicy
     * @param utilizationThreshold       the utilization threshold
     */
    public PowerVmAllocationPolicyMigrationInterQuartileRange(
        PowerVmSelectionPolicy vmSelectionPolicy,
        double safetyParameter,
        PowerVmAllocationPolicyMigrationAbstract fallbackVmAllocationPolicy,
        double utilizationThreshold)
    {
        super(vmSelectionPolicy);
        setSafetyParameter(safetyParameter);
        setFallbackVmAllocationPolicy(fallbackVmAllocationPolicy);
    }

    /**
     * Creates a PowerVmAllocationPolicyMigrationInterQuartileRange.
     *
     * @param vmSelectionPolicy          the vm selection policy
     * @param safetyParameter            the safety parameter
     * @param fallbackVmAllocationPolicy
     */
    public PowerVmAllocationPolicyMigrationInterQuartileRange(
        PowerVmSelectionPolicy vmSelectionPolicy,
        double safetyParameter,
        PowerVmAllocationPolicyMigration fallbackVmAllocationPolicy)
    {
        super(vmSelectionPolicy);
        setSafetyParameter(safetyParameter);
        setFallbackVmAllocationPolicy(fallbackVmAllocationPolicy);
    }

    /**
     * Computes the host utilization IRQ used for generating the host over utilization threshold.
     *
     * @param host the host
     * @return the host CPU utilization percentage IQR
     */
    @Override
    public double computeHostUtilizationMeasure(PowerHostUtilizationHistory host) throws IllegalArgumentException {
        double[] data = host.getUtilizationHistory();
        if (MathUtil.countNonZeroBeginning(data) >= 12) { // 12 has been suggested as a safe value
            return MathUtil.iqr(data);
        }

        throw new IllegalArgumentException("There is not enough Host history to compute Host utilization IRQ");
    }

}
