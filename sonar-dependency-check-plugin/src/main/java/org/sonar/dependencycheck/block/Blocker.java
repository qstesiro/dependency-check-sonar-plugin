package org.sonar.dependencycheck.block;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import org.sonar.dependencycheck.base.DependencyCheckMetrics;
import org.sonar.dependencycheck.base.DependencyCheckConstants;
import org.sonar.dependencycheck.parser.element.Analysis;
import org.sonar.dependencycheck.parser.element.Dependency;
import org.sonar.dependencycheck.parser.element.Identifier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import edu.umd.cs.findbugs.annotations.NonNull;

public class Blocker {

    private static final Logger LOGGER = Loggers.get(Blocker.class);

    private SensorContext context;

    public Blocker(SensorContext context) {
        this.context = context;
    }

    public void parse(@NonNull Analysis analysis) throws Exception {
        saveMeasure(match(analysis));
    }

    private void saveMeasure(List<BlockDependency> lst) throws Exception  {
        if (lst.size() > 0) {
            context.<Integer>newMeasure()
                .forMetric(DependencyCheckMetrics.DEPENDENCY_BLOCK_NUM)
                .on(context.project())
                .withValue(lst.size())
                .save();
            context.<String>newMeasure()
                .forMetric(DependencyCheckMetrics.DEPENDENCY_BLOCK_HITS)
                .on(context.project())
                .withValue(new ObjectMapper().writeValueAsString(lst))
                .save();
        } else {
            context.<Integer>newMeasure()
                .forMetric(DependencyCheckMetrics.DEPENDENCY_BLOCK_NUM)
                .on(context.project())
                .withValue(lst.size())
                .save();
        }
    }

    private List<BlockDependency> match(@NonNull Analysis analysis) throws Exception {
        List<BlockDependency> blocks = new ArrayList<>();
        List<BlockDependency> lst = queryBlockList();
        for (Dependency dependency : analysis.getDependencies()) {
            for (Identifier identifier : dependency.getPackages()) {
                String packageName = identifier.getPackageName().orElse("");
                for (BlockDependency block : lst) {
                    if (compare(identifier, block)) {
                        LOGGER.debug("hit block package: {}", block.toJson());
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

    private boolean compare(Identifier identifier, BlockDependency block) {
        String packageName = identifier.getPackageName().orElse("");
        String packageVersion = identifier.getPackageVersion().orElse("");
        // LOGGER.debug(
        //     "compare block package: {}, {}",
        //     String.format("%s@%s", packageName, packageVersion),
        //     String.format("%s@%s:%s", block.packageName, block.beginVersion, block.endVersion)
        // );
        if (packageName.equals(block.packageName)) {
            return compareVersion(packageVersion, block.beginVersion) >= 0
                && compareVersion(packageVersion, block.endVersion) <= 0;
        }
        return false;
    }

    private int compareVersion(String version1, String version2) {
        String[] array1 = version1.split("\\.");
        String[] array2 = version2.split("\\.");
        int idx = 0;
        int minLen = Math.min(array1.length, array2.length);
        int diff = 0;
        while (idx < minLen) {
            diff = array1[idx].length() - array2[idx].length();
            if (diff != 0) {
                break;
            }
            diff = array1[idx].compareTo(array2[idx]);
            if (diff != 0) {
                break;
            }
            ++idx;
        }
        return (diff != 0) ? diff : (array1.length - array2.length);
    }

    private List<BlockDependency> queryBlockList() throws Exception {
        LOGGER.debug("block query server: {}", getServer());
        // ??? 测试
        List<BlockDependency> lst = new ArrayList<>();
        BlockDependency block = new BlockDependency();
        block.packageName = "org.hdrhistogram/HdrHistogram";
        block.beginVersion = "2.1.0";
        block.endVersion = "2.1.9";
        block.suggestVersion = "2.1.10";
        block.description = "存在致命漏洞";
        block.createdAt = 1699585980000L;
        block.updatedAt = 1699585980000L;
        block.updatedBy = "20016572";
        lst.add(block);
        block = new BlockDependency();
        block.packageName = "com.zaxxer/HikariCP";
        block.beginVersion = "3.4.0";
        block.endVersion = "3.4.5";
        block.suggestVersion = "3.4.6";
        block.description = "存在致命漏洞";
        block.createdAt = 1699585980000L;
        block.updatedAt = 1699585980000L;
        block.updatedBy = "20016572";
        lst.add(block);
        return lst;
    }

    private String getServer() {
        return context.config()
            .get(DependencyCheckConstants.BLOCK_QUERY_SERVER)
            .orElse("");
    }
}
