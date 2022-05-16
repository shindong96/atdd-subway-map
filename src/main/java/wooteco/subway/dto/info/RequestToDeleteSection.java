package wooteco.subway.dto.info;

public class RequestToDeleteSection {
    private final long lineId;
    private final long stationId;

    public RequestToDeleteSection(long lineId, long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public long getLineId() {
        return lineId;
    }

    public long getStationId() {
        return stationId;
    }
}
