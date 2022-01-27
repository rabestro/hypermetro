package metro.repository;

import java.util.Set;

public record Station(String name, int time, Set<String> next, Set<String> prev, Set<StationId> transfer) {
}