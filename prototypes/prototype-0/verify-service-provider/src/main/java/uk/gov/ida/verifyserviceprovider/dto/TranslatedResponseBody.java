package uk.gov.ida.verifyserviceprovider.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class TranslatedResponseBody {

    private final String scenario;
    private final String pid;
    private final LevelOfAssurance levelOfAssurance;
    private final Optional<Attributes> attributes;

    @JsonCreator
    public TranslatedResponseBody(
        @JsonProperty("scenario") String scenario,
        @JsonProperty("pid") String pid,
        @JsonProperty("levelOfAssurance") LevelOfAssurance levelOfAssurance,
        @JsonProperty("attributes") Optional<Attributes> attributes
    ) {
        this.scenario = scenario;
        this.pid = pid;
        this.levelOfAssurance = levelOfAssurance;
        this.attributes = attributes;
    }

    public String getScenario() {
        return scenario;
    }

    public String getPid() {
        return pid;
    }

    public LevelOfAssurance getLevelOfAssurance() {
        return levelOfAssurance;
    }

    public Optional<Attributes> getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TranslatedResponseBody that = (TranslatedResponseBody) o;

        if (scenario != null ? !scenario.equals(that.scenario) : that.scenario != null) return false;
        if (pid != null ? !pid.equals(that.pid) : that.pid != null) return false;
        if (levelOfAssurance != that.levelOfAssurance) return false;
        return attributes != null ? attributes.equals(that.attributes) : that.attributes == null;
    }

    @Override
    public int hashCode() {
        int result = scenario != null ? scenario.hashCode() : 0;
        result = 31 * result + (pid != null ? pid.hashCode() : 0);
        result = 31 * result + (levelOfAssurance != null ? levelOfAssurance.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TranslatedResponseBody{" +
            "scenario='" + scenario + '\'' +
            ", pid='" + pid + '\'' +
            ", levelOfAssurance=" + levelOfAssurance +
            ", attributes=" + attributes +
            '}';
    }

}
