package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.Arrays;
import java.util.Objects;

public class PredictionDto {
    String predictionText;
    String[] areaNames;
    Long[] predictions;

    public String[] getAreaNames() {
        return areaNames;
    }

    public void setAreaNames(String[] areaNames) {
        this.areaNames = areaNames;
    }

    public Long[] getPredictions() {
        return predictions;
    }

    public void setPredictions(Long[] predictions) {
        this.predictions = predictions;
    }

    public String getPredictionText() {
        return predictionText;
    }

    public void setPredictionText(String predictionText) {
        this.predictionText = predictionText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PredictionDto that = (PredictionDto) o;
        return Objects.equals(predictionText, that.predictionText) && Objects.deepEquals(areaNames, that.areaNames) && Objects.deepEquals(predictions, that.predictions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(predictionText, Arrays.hashCode(areaNames), Arrays.hashCode(predictions));
    }

    @Override
    public String toString() {
        return "PredictionDto{"
            + "predictionText='" + predictionText + '\''
            + ", areaNames=" + Arrays.toString(areaNames)
            + ", predictions=" + Arrays.toString(predictions)
            + '}';
    }

    public PredictionDto copy() {
        return PredictionBuilder.aPredictionDto()
            .withPredictionText(predictionText)
            .withAreaNames(areaNames)
            .withPredictions(predictions)
            .build();
    }

    public static final class PredictionBuilder {
        String predictionText;
        String[] areaNames;
        Long[] predictions;

        private PredictionBuilder() {
        }

        public static PredictionBuilder aPredictionDto() {
            return new PredictionBuilder();
        }

        public PredictionBuilder withPredictionText(String predictionText) {
            this.predictionText = predictionText;
            return this;
        }

        public PredictionBuilder withAreaNames(String[] areaNames) {
            this.areaNames = areaNames;
            return this;
        }

        public PredictionBuilder withPredictions(Long[] predictions) {
            this.predictions = predictions;
            return this;
        }

        public PredictionDto build() {
            PredictionDto predictionDto = new PredictionDto();
            predictionDto.setPredictionText(predictionText);
            predictionDto.setAreaNames(areaNames);
            predictionDto.setPredictions(predictions);
            return predictionDto;
        }
    }
}
