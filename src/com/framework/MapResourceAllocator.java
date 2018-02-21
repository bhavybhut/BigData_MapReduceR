package com.framework;

import java.util.ArrayList;
import java.util.List;

public class MapResourceAllocator {

	private List<Thread> mapThreadList = new ArrayList<Thread>();
	private int mapThreadDefault = 100;
	private int mapThreadMax = 0;

	private void setThreadMax(int max) {
		this.mapThreadMax = max;
	}

	public int getThreadMax() {
		return this.mapThreadMax;
	}

	public MapResourceAllocator() {
		setThreadMax(mapThreadDefault);
	}

	public MapResourceAllocator(int maxThread) {
		setThreadMax(maxThread);
	}

	public Thread getMapThread(Record r, Context threadContext, MapFunction mapper) {
		int i = 0;
		boolean isCreated = false;
		MapThread MThread = null;
		Thread ThreadTemp = null;

		if (mapThreadList.size() < mapThreadMax) {
			MThread = new MapThread(r, threadContext, mapper);
			mapThreadList.add(MThread);
		} else {
			while (isCreated != true) {
				while (i < mapThreadList.size()) {
					ThreadTemp = mapThreadList.get(i);

					if (!ThreadTemp.isAlive()) {
						mapThreadList.remove(i);
						MThread = new MapThread(r, threadContext, mapper);
						mapThreadList.add(MThread);
						isCreated = true;
						i = mapThreadList.size();
					} else {
						i++;
					}
				}
				i = 0;
			}
		}
		return MThread;
	}

	public void syncThreads() {
		int i = 0;
		Thread ThreadTemp = null;
		while (i < mapThreadList.size()) {
			ThreadTemp = mapThreadList.get(i);

			if (ThreadTemp.isAlive())
				i = 0;
			else
				i++;
		}
		mapThreadList.clear();

	}
}
