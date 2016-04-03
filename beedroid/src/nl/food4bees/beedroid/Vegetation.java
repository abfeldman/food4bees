package nl.food4bees.beedroid;

public class Vegetation {
    Integer mPlantId;
    Polygon mArea;
    Double mAmount;

    Vegetation(Integer plantId, Polygon area, Double amount) {
        mPlantId = plantId;
        mArea = area;
        mAmount = amount;
    }

    void setPlantId(Integer plantId) {
        mPlantId = plantId;
    }

    Integer getPlantId() {
        return mPlantId;
    }

    void setArea(Polygon area) {
        mArea = area;
    }

    Polygon getArea() {
        return mArea;
    }

    void setAmount(Double amount) {
        mAmount = amount;
    }

    Double getAmount() {
        return mAmount;
    }
}
