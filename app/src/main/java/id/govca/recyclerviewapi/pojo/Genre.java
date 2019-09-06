package id.govca.recyclerviewapi.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Genre {
    @JsonProperty("id")
    private int ID;
    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    public int getID() {
        return ID;
    }

    @JsonProperty("id")
    public void setID(int ID) {
        this.ID = ID;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }
}
