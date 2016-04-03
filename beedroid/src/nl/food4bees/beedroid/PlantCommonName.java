package nl.food4bees.beedroid;

public class PlantCommonName {
    private Integer mId;
    private String mCommonName;

    PlantCommonName(Integer id, String commonName) {
        mId = id;
        mCommonName = commonName;
    }

    public Integer getId() {
        return mId;
    }

    public String getCommonName() {
        return mCommonName;
    }

    public String toString() {
        return mCommonName;
    }
}
