package nl.food4bees.beedroid;

public class PlantVersion {
    private Integer mId;
    private Integer mVersion;

    PlantVersion(Integer id, Integer version) {
        mId = id;
        mVersion = version;
    }

    public Integer getId() {
        return mId;
    }

    public Integer getVersion() {
        return mVersion;
    }
}
