/*
 * Dependency-Check Plugin for SonarQube
 * Copyright (C) 2015-2023 dependency-check
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
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.umd.cs.findbugs.annotations.NonNull;

public class Evidence {

    private final String source;
    private final String name;
    private final String value;
    private final String type;
    private final Confidence confidence;

    @JsonCreator
    public Evidence(@JsonProperty(value = "source", required = true) @NonNull String source,
                    @JsonProperty(value = "name", required = true) @NonNull String name,
                    @JsonProperty(value = "value", required = true) @NonNull String value,
                    @JsonProperty(value = "type", required = true) @NonNull String type,
                    @JsonProperty(value = "confidence", required = true) @NonNull Confidence confidence) {
        this.source = source;
        this.name = name;
        this.value = value;
        this.type = type;
        this.confidence = confidence;
    }

    public String getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public Confidence getConfidence() {
        return confidence;
    }

}
