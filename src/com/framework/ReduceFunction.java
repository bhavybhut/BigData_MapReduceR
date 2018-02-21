package com.framework;

public interface ReduceFunction {
	public void reduce(Context out, String key, Context in);
}
