package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineEntity;
import wooteco.subway.dto.info.RequestToCreateSection;
import wooteco.subway.dto.info.RequestToDeleteSection;

@Service
public class SectionService {
    private static final String ERROR_MESSAGE_NOT_EXISTS_ID = "존재하지 않는 지하철 노선 id입니다.";
    private static final String ERROR_MESSAGE_NOT_EXISTS_STATION = "존재하지 않는 역을 지나는 구간은 만들 수 없습니다.";

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineCreator lineCreator;

    public SectionService(LineDao lineDao, SectionDao sectionDao, StationDao stationDao,
        LineCreator lineCreator) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineCreator = lineCreator;
    }

    @Transactional
    public void save(RequestToCreateSection requestToCreateSection) {
        Long lineId = requestToCreateSection.getLineId();
        Long upStationId = requestToCreateSection.getUpStationId();
        Long downStationId = requestToCreateSection.getDownStationId();

        validateBeforeSave(lineId, upStationId, downStationId);

        Section section = new Section(stationDao.getStation(upStationId), stationDao.getStation(downStationId),
            requestToCreateSection.getDistance());
        LineEntity lineEntity = lineDao.find(lineId);
        Line line = lineCreator.createLine(lineEntity.getId());
        line.updateToAdd(section);

        sectionDao.save(line.getId(), section);
        saveUpdatedLine(line);
    }

    private void saveUpdatedLine(Line line) {
        Sections sections = line.getSections();
        sections.forEach(section1 -> sectionDao.update(line.getId(), section1));
    }

    private void validateBeforeSave(Long lineId, Long upStationId, Long downStationId) {
        validateNotExistsLine(lineId);
        validateNotExistStation(upStationId);
        validateNotExistStation(downStationId);
    }

    @Transactional
    public void delete(RequestToDeleteSection requestToDeleteSection) {
        Long lineId = requestToDeleteSection.getLineId();
        Long stationId = requestToDeleteSection.getStationId();

        validateNotExistsLine(lineId);
        validateNotExistStation(stationId);

        Line line = lineCreator.createLine(lineId);
        Station station = stationDao.getStation(stationId);
        Section deletedSection = line.delete(station);

        sectionDao.delete(lineId, deletedSection);
        line.getSections().forEach(section -> sectionDao.update(lineId, section));
    }

    private void validateNotExistsLine(Long id) {
        if (!lineDao.existById(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_EXISTS_ID);
        }
    }

    private void validateNotExistStation(Long id) {
        if (!stationDao.existById(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_EXISTS_STATION);
        }
    }
}
