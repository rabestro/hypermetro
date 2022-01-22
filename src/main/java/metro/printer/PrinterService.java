package metro.printer;

import metro.model.MetroLine;
import metro.model.MetroStation;
import metro.model.StationId;

import java.util.List;

public interface PrinterService {
    String printLine(MetroLine metroLine);

    String printStation(MetroStation metroStation);

    String printRoute(List<StationId> route);
}
