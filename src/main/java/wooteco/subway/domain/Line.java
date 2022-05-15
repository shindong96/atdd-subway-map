package wooteco.subway.domain;

import java.util.List;

public class Line {
    private static final String ERROR_MESSAGE_NAME_SIZE = "존재할 수 없는 이름입니다.";
    private static final String ERROR_MESSAGE_COLOR_SIZE = "존재할 수 없는 색상입니다.";
    private static final String ERROR_MESSAGE_SECTIONS_SIZE = "구간이 %d개 밖에 없으므로 삭제할 수 없습니다.";
    private static final int MINIMUM_SECTIONS_SIZE = 1;
    private final int NAME_SIZE_LIMIT = 255;
    private final int COLOR_SIZE_LIMIT = 20;

    private Long id;
    private final String name;
    private final String color;
    private Sections sections;

    public Line(String name, String color) {
        validateNameSize(name);
        validateColorSize(color);
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color) {
        validateNameSize(name);
        validateColorSize(color);
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color, Sections sections) {
        validateNameSize(name);
        validateColorSize(color);
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    private void validateNameSize(String name) {
        if (name == null || name.isBlank() || name.length() > NAME_SIZE_LIMIT) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NAME_SIZE);
        }
    }

    private void validateColorSize(String color) {
        if (color == null || color.isBlank() || color.length() > COLOR_SIZE_LIMIT) {
            throw new IllegalArgumentException(ERROR_MESSAGE_COLOR_SIZE);
        }
    }

    public void updateToAdd(Section section) {
        sections.updateToAdd(section);
    }

    public Section delete(Station station) {
        if (sections.size() <= MINIMUM_SECTIONS_SIZE) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE_SECTIONS_SIZE, MINIMUM_SECTIONS_SIZE));
        }

        return sections.removeAndUpdate(station);
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

    public Sections getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
