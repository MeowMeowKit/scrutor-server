package dtos;

import java.util.UUID;

public class Tag {
    private String tagId;
    private String name;

    public Tag() {
        this.tagId = null;
        this.name = null;
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(String tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
