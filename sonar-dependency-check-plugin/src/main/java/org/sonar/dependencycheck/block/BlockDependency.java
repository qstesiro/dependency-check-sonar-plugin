package org.sonar.dependencycheck.block;

import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
// import com.fasterxml.jackson.annotation.JsonFormat;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockDependency {

    @JsonProperty(value = "packageName")
    public String packageName;

    @JsonProperty(value = "beginVersion")
    public String beginVersion;

    @JsonProperty(value = "endVersion")
    public String endVersion;

    @JsonProperty(value = "suggestVersion")
    public String suggestVersion;

    @JsonProperty(value = "description")
    public String description;

    @JsonProperty(value = "createdAt")
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:SS")
    public long createdAt;

    @JsonProperty(value = "updatedAt")
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:SS")
    public long updatedAt;

    @JsonProperty(value = "updatedBy")
    public String updatedBy;

    public String toJson() throws Exception {
        return new ObjectMapper().writeValueAsString(this);
    }
}
