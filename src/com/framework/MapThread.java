package com.framework;

public class MapThread extends Thread{

	  Record record = new Record();
	  Context threadContext = new Context();
	  MapFunction mapper = null;

	  MapThread(Record r, Context threadContext, MapFunction mapper) 
	  {
	    this.record = r;
	    this.threadContext = threadContext;
	    this.mapper = mapper;
	  }

	  public void run() 
	  {
	    mapper.map(threadContext, record);
	  }

}
