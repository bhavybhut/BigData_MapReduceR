package com.framework;

import java.util.ArrayList;
import java.util.List;

public class ReduceResourceAllocator {

	private List<Thread> reduceThreadList = new ArrayList<Thread>();
	private int reduceThreadDefault = 100;
	private int reduceThreadMax = 0;

	private void setThreadMax(int max) {
		this.reduceThreadMax = max;
	}

	public int getThreadMax() {
		return this.reduceThreadMax;
	}

	public ReduceResourceAllocator() {
		setThreadMax(reduceThreadDefault);
	}

	public ReduceResourceAllocator(int maxThread) {
		setThreadMax(maxThread);
	}

	public Thread getReduceThread(Context threadOutput, String threadKey, Context threadInput, ReduceFunction reducer) {
		int i = 0;
		boolean isCreated = false;
		ReduceThread RThread = null;
		Thread ThreadTemp = null;

		if (reduceThreadList.size() < reduceThreadMax) {
			RThread = new ReduceThread(threadOutput, threadKey, threadInput, reducer);
			reduceThreadList.add(RThread);
		} else {
			while (isCreated != true) {
				while (i < reduceThreadList.size()) {
					ThreadTemp = reduceThreadList.get(i);

					if (!ThreadTemp.isAlive()) {
						reduceThreadList.remove(i);
						RThread = new ReduceThread(threadOutput, threadKey, threadInput, reducer);
						reduceThreadList.add(RThread);
						isCreated = true;
						i = reduceThreadList.size();
					} else
						i++;
				}
				i = 0;
			}
		}
		return RThread;
	}

	public void syncThreads() {
		int i = 0;
		Thread ThreadTemp = null;
		while (i < reduceThreadList.size()) {
			ThreadTemp = reduceThreadList.get(i);

			if (ThreadTemp.isAlive())
				i = 0;
			else
				i++;
		}
		reduceThreadList.clear();
	}
}
