/*
 * Dependency-Check Plugin for SonarQube
 * Copyright (C) 2015-2020 dependency-check
 * philipp.dallig@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.dependencycheck.base;

import org.sonar.api.ce.measure.Component.Type;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * This implementation of {@link MeasureComputer} will aggregate the metrics
 * saved for the pom.xml files for directories, modules and projects. The
 * metrics being summaries will be just summarized. Metrics
 * {@link DependencyCheckMetrics#INHERITED_RISK_SCORE} and
 * {@link DependencyCheckMetrics#VULNERABLE_COMPONENT_RATIO} are re-calculated.
 */
public class DependencyCheckMeasureComputer implements MeasureComputer {

    private static final Logger LOGGER = Loggers.get(DependencyCheckMeasureComputer.class);

    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext defContext) {
        return defContext
            .newDefinitionBuilder()
            .setOutputMetrics(
                DependencyCheckMetrics.CRITICAL_SEVERITY_VULNS.getKey(),
                DependencyCheckMetrics.HIGH_SEVERITY_VULNS.getKey(),
                DependencyCheckMetrics.MEDIUM_SEVERITY_VULNS.getKey(),
                DependencyCheckMetrics.LOW_SEVERITY_VULNS.getKey(),
                DependencyCheckMetrics.TOTAL_DEPENDENCIES.getKey(),
                DependencyCheckMetrics.VULNERABLE_DEPENDENCIES.getKey(),
                DependencyCheckMetrics.TOTAL_VULNERABILITIES.getKey(),
                DependencyCheckMetrics.INHERITED_RISK_SCORE.getKey(),
                DependencyCheckMetrics.VULNERABLE_COMPONENT_RATIO.getKey(),
                DependencyCheckMetrics.REPORT.getKey(), // 可以传递但是界面无法显示,可能与数据类型有关 ???
                DependencyCheckMetrics.HMM_DEMO.getKey() // ???
            ).build();
    }

    @Override
    public void compute(MeasureComputerContext context) {
        LOGGER.info(
            "--- type: {}, key: {}",
            context.getComponent().getType(),
            context.getComponent().getKey()
        );
        // // Check if we have already measures on project
        // if (context.getComponent().getType() == Type.PROJECT &&
        //     context.getMeasure(DependencyCheckMetrics.TOTAL_DEPENDENCIES.key()) != null) {
        //     return;
        // }
        // // 实际不生效所有measure都是被绑定到project级别
        // if (context.getComponent().getType() != Type.FILE) {
        //     int blocker = sumMeasure(context, DependencyCheckMetrics.CRITICAL_SEVERITY_VULNS.key());
        //     int high = sumMeasure(context, DependencyCheckMetrics.HIGH_SEVERITY_VULNS.key());
        //     int major = sumMeasure(context, DependencyCheckMetrics.MEDIUM_SEVERITY_VULNS.key());
        //     int minor = sumMeasure(context, DependencyCheckMetrics.LOW_SEVERITY_VULNS.key());
        //     sumMeasure(context, DependencyCheckMetrics.TOTAL_DEPENDENCIES.key());
        //     int vulnerableDependencies = sumMeasure(context, DependencyCheckMetrics.VULNERABLE_DEPENDENCIES.key());
        //     int vulnerabilityCount = sumMeasure(context, DependencyCheckMetrics.TOTAL_VULNERABILITIES.key());
        //     context.addMeasure(
        //         DependencyCheckMetrics.INHERITED_RISK_SCORE.getKey(),
        //         DependencyCheckMetrics.inheritedRiskScore(blocker, high, major, minor)
        //     );
        //     context.addMeasure(
        //         DependencyCheckMetrics.VULNERABLE_COMPONENT_RATIO.getKey(),
        //         DependencyCheckMetrics.vulnerableComponentRatio(
        //             vulnerabilityCount, vulnerableDependencies
        //         )
        //     );
        //     // if (context.getComponent().getType() == Type.PROJECT) {
        //     //     LOGGER.info(
        //     //         "--- key: {}, getKey: {}",
        //     //         DependencyCheckMetrics.REPORT.key(),
        //     //         DependencyCheckMetrics.REPORT.getKey()
        //     //     );
        //     //     String val = context.getMeasure(DependencyCheckMetrics.REPORT.key()).getStringValue();
        //     //     LOGGER.info("--- report: {}", val.substring(0, 64));
        //     //     context.addMeasure(DependencyCheckMetrics.REPORT.key(), val);
        //     // }
        // }
    }

    private int sumMeasure(MeasureComputerContext context, String metricKey) {
        int sum = 0;
        for (Measure m : context.getChildrenMeasures(metricKey)) {
            sum += m.getIntValue();
        }
        LOGGER.info("--- sum: {}", sum);
        context.addMeasure(metricKey, sum);
        return sum;
    }
}
