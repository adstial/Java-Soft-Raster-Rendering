package app.ui;

import dependence.json.builder.JsonElement;
import dependence.json.builder.JsonFile;

@SuppressWarnings("unused")


@JsonFile("assert/UIConfig.json")
public class UIConfig {
    @JsonElement("title")
    private String title;

    @JsonElement("backgroundColor")
    private String backgroundColor;

    @JsonElement("size/width")
    private int width;

    @JsonElement("size/height")
    private int height;
}
