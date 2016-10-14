package fr.glouglouwine.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.glouglouwine.domain.Bottle;

public class BottleRepository {
	public final static BottleRepository INSTANCE = new BottleRepository();
	private Map<Long, Bottle> dataStore = new HashMap<>();

	public void add(Bottle bottle) {
		dataStore.put(bottle.getBottleId(), bottle);
	}

	public Bottle getById(Long id) {
		return dataStore.get(id);
	}

	public List<Bottle> findAll() {
		return dataStore.values().stream().collect(Collectors.toList());
	}
}
