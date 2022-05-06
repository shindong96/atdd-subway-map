package wooteco.subway.dto.info;

public class LineInfo {
    private Long id;
    private String name;
    private String color;

    public LineInfo(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public LineInfo(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
