package wooteco.subway.domain.section;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.line.Line;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.line.StationsInLine;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dao.StationDao;

@SpringBootTest
@Transactional
@Sql("classpath:test-schema.sql")
class SectionDaoTest {
    private static final String stationName1 = "강남역";
    private static final String stationName2 = "잠실역";
    private static final String stationName3 = "신림역";

    @Autowired
    private LineDao lineDao;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private SectionDao sectionDao;

    @DisplayName("새로운 구간을 생성한다")
    @Test
    void save() {
        saveStations();
        long lineId = saveLine();

        Section section = new Section(lineId, 1L, 2L, 100);
        Section section2 = new Section(lineId, 2L, 3L, 200);

        long sectionId = sectionDao.save(section);
        assertEquals(sectionId + 1, sectionDao.save(section2));
    }

    @DisplayName("같은 노선의 모든 구간을 조회해 StationsInLine으로 반환한다.")
    @Test
    void findSections() {
        save();
        Map<Station, Station> expectedSections = new HashMap<>();
        expectedSections.put(stationDao.findById(1L).get(), stationDao.findById(2L).get());
        expectedSections.put(stationDao.findById(2L).get(), stationDao.findById(3L).get());
        StationsInLine stationsInLine = StationsInLine.from(expectedSections);

        assertEquals(stationsInLine, sectionDao.findOrderedStationsByLineId(1L));
    }

    @DisplayName("UpStation이 같은 구간을 조회한다.")
    @Test
    void findSectionBySameUpStation() {
        long stationId1 = stationDao.save(new Station(stationName1));
        long stationId2 = stationDao.save(new Station(stationName2));

        long lineId = saveLine();

        int distance = 100;
        Section section = new Section(1L, stationId1, stationId2, distance);
        sectionDao.save(section);

        assertEquals(section, sectionDao.findSectionBySameUpStation(lineId, 1L).get());
    }

    @DisplayName("DownStation이 같은 구간을 조회한다.")
    @Test
    void findSectionBySameDownStation() {
        saveStations();
        long lineId = saveLine();
        Section section2 = saveSection(lineId, 1L, 2L, 100, 3L, 200);

        assertEquals(section2, sectionDao.findSectionBySameDownStation(lineId, 3L).get());
    }

    @DisplayName("upStation, downStation을 수정한다.")
    @Test
    void updateUpStation() {
        saveStations();
        long lineId = saveLine();
        Section section2 = saveSection(lineId, 1L, 2L, 100, 3L, 200);

        assertEquals(1, sectionDao.updateUpStation(section2, 3L));
        assertEquals(1, sectionDao.updateDownStation(section2, 1L));
    }

    @DisplayName("구간을 삭제한다.")
    @Test
    void deleteSection() {
        saveStations();
        long lineId = saveLine();
        Section section2 = saveSection(lineId, 1L, 2L, 100, 3L, 200);

        int initSize = sectionDao.findOrderedStationsByLineId(lineId).getStations().size();

        assertEquals(1, sectionDao.deleteSection(section2));
        assertEquals(initSize - 1, sectionDao.findOrderedStationsByLineId(lineId).getStations().size());
    }

    private Section saveSection(long lineId, long upStationId, long downStationId, int distance, long downStationId2, int distance2) {
        Section section = new Section(lineId, upStationId, downStationId, distance);
        Section section2 = new Section(lineId, downStationId, downStationId2, distance2);
        sectionDao.save(section);
        sectionDao.save(section2);
        return section2;
    }

    private long saveLine() {
        Line line = new Line("2호선", "green");
        return lineDao.save(line);
    }

    private void saveStations() {
        stationDao.save(new Station(stationName1));
        stationDao.save(new Station(stationName2));
        stationDao.save(new Station(stationName3));
    }
}