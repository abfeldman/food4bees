package nl.food4bees.beedroid;

public class Plant {
    private Integer mId;
    private String mCommonName;
    private String mScientificName;
    private String mDescription;
    private Integer mVersion;

    Plant(Integer id,
          String commonName,
          String scientificName,
          String description,
          Integer version) {
        mId = id;
        mCommonName = commonName;
        mScientificName = scientificName;
        mDescription = description;
        mVersion = version;
    }

    public Integer getId() {
        return mId;
    }

    public String getCommonName() {
        return mCommonName;
    }

    public String getScientificName() {
        return mScientificName;
    }

    public String getDescription() {
        return mDescription;
    }

    public Integer getVersion() {
        return mVersion;
    }

    public String toString() {
        return mCommonName;
    }
}
