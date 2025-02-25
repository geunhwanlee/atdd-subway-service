package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static nextstep.subway.path.domain.Charge.BASE_CHARGE;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private Charge charge;

    public Path(List<Station> stations, int distance, List<Section> sections) {
        this.stations = stations;
        this.distance = distance;
        this.charge = findCharge(distance, lineSet(sections));
    }

    private Charge findCharge(int distance, Set<Line> lines) {
        return BASE_CHARGE.addAll(DistanceSurcharge.from(distance), LineSurcharge.from(lines));
    }

    private Set<Line> lineSet(List<Section> sections) {
        return sections.stream()
                .map(Section::getLine)
                .collect(toSet());
    }

    public void discountBy(LoginMember loginMember) {
        charge = charge.discountBy(loginMember);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public Charge getCharge() {
        return charge;
    }
}
