package com.framework;

public class ReduceThread extends Thread{

	  Context threadOutput = new Context();
	  String threadKey = null;
	  Context threadInput = new Context();
	  ReduceFunction reducer = null;

	  ReduceThread(Context threadOutput, String threadKey, Context threadInput, ReduceFunction reducer) 
	  { 
	    this.threadOutput = threadOutput;
	    this.threadKey = threadKey;
	    this.threadInput = threadInput;
	    this.reducer = reducer;
	  }

	  public void run() 
	  {
	    reducer.reduce(threadOutput, threadKey, threadInput);
	  }

}
