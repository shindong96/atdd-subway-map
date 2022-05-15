package wooteco.subway.dto.info;

public class StationDto {
    private Long id;
    private String name;

    public StationDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StationDto(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
