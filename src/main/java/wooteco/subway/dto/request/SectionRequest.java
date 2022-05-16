package wooteco.subway.dto.request;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public SectionRequest() {
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
