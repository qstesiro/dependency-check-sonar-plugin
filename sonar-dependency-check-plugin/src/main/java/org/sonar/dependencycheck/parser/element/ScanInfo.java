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
package org.sonar.dependencycheck.parser.element;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.List;
import java.util.Optional;

import org.sonar.dependencycheck.parser.deserializer.AnalysisExceptionDeserializer;

@JsonIgnoreProperties("dataSource")
public class ScanInfo {

    private final String engineVersion;
    private final List<AnalysisException> exceptions;

    @JsonCreator
    public ScanInfo(
        @JsonProperty(value = "engineVersion", required = true)
        @NonNull String engineVersion,
        @JsonProperty(value = "analysisExceptions")
        @JsonDeserialize(using = AnalysisExceptionDeserializer.class)
        @Nullable List<AnalysisException> exceptions
    ) {
        this.engineVersion = engineVersion;
        this.exceptions = exceptions;
    }

    public String getEngineVersion() {
        return engineVersion;
    }

    public Optional<List<AnalysisException>> getExceptions() {
        return Optional.ofNullable(exceptions);
    }
}
