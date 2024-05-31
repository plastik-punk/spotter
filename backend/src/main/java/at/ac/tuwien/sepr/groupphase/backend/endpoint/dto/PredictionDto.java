package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public class PredictionDto {
    String prediction;

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PredictionDto that = (PredictionDto) object;
        return prediction.equals(that.prediction);
    }

    @Override
    public int hashCode() {
        return prediction.hashCode();
    }

    @Override
    public String toString() {
        return "PredictionDto{"
            + "prediction='" + prediction + '\''
            + '}';
    }

    public PredictionDto copy() {
        return PredictionBuilder.aPredictionDto()
            .withPrediction(prediction)
            .build();
    }

    public static final class PredictionBuilder {
        String prediction;

        private PredictionBuilder() {
        }

        public static PredictionBuilder aPredictionDto() {
            return new PredictionBuilder();
        }

        public PredictionBuilder withPrediction(String prediction) {
            this.prediction = prediction;
            return this;
        }

        public PredictionDto build() {
            PredictionDto predictionDto = new PredictionDto();
            predictionDto.setPrediction(prediction);
            return predictionDto;
        }
    }
}
