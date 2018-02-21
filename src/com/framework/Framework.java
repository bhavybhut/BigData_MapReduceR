package com.framework;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Framework {

	private MapFunction mapFunction;
	private ReduceFunction reduceFunction;
	private Context context;
	private MapResourceAllocator mapManager = new MapResourceAllocator();
	private ReduceResourceAllocator reduceManager = new ReduceResourceAllocator();

	private AtomicLong mapperRecords = new AtomicLong(0);
	private AtomicLong reducerRecords = new AtomicLong(0);
	
	public void setMapFunction(MapFunction mapFunction) {
		this.mapFunction = mapFunction;
	}

	public void setReduceFunction(ReduceFunction reduceFunction) {
		this.reduceFunction = reduceFunction;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public AtomicLong getMapperTuples() {
		return mapperRecords;
	}

	public AtomicLong getReducerTuples() {
		return reducerRecords;
	}
	
	public Context process(){
	    Context temp_context = new Context();

	    long timeBeforeMap = new Date().getTime();

	    Iterator<Record> citerator = context.getContext().iterator();
	    while (citerator.hasNext())
	    {
	      Record r = citerator.next();

	      Thread MThread = mapManager.getMapThread(r, temp_context, mapFunction);
	      MThread.start();
	    }
	    mapManager.syncThreads();

	    long timeAfterMap = new Date().getTime();
	    long timePhaseMap = timeAfterMap - timeBeforeMap;
	    System.out.println("--------------------------------------------------------");
	    System.out.println("------------------Time of every Phases------------------");
	    System.out.println("--------------------------------------------------------");
	    System.out.println("                  Map : " + timePhaseMap + " Milliseconds");

	    Map<String, Context> shuffle = temp_context.combiner();
	    Set<String> keys = shuffle.keySet();
	    Context output = new Context();

	    long timeBeforeReduce = new Date().getTime();
	    for (String key : keys)
	    {
	      Context shuffle_context = shuffle.get(key);
	      int num_records = shuffle_context.count();
	      getMapperTuples().addAndGet(num_records);
	      getReducerTuples().set(Math.max(getMapperTuples().get(), num_records));

	      Thread RThread = reduceManager.getReduceThread(output, key, shuffle_context, reduceFunction);
	      RThread.start();
	    }
	    reduceManager.syncThreads();

	    long timeAfterReduce = new Date().getTime();
	    long timePhaseReduce = timeAfterReduce - timeBeforeReduce;
	    long timePhaseTotal = timePhaseMap + timePhaseReduce;
	    double timeSeconds = (double) timePhaseTotal/1000;
	    int timeMinutes = (int) (timePhaseTotal/1000)/60;
	    double timeSecMins = ( (double)timePhaseTotal/1000) - ( (double)timeMinutes * 60);

	    System.out.println("               Reduce : " + timePhaseReduce + " Milliseconds");
	    System.out.println("--------------------------------------------------------");
	    System.out.println("                Total : " + timePhaseTotal + " Milliseconds");
	    System.out.println("        Total Seconds : " + timeSeconds);
	    System.out.println("        Total Minutes : " + timeMinutes + "." + timeSecMins);
	    System.out.println("--------------------------------------------------------");
	    return output;
	  
	}
}
