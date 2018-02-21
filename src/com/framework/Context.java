package com.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Context implements combineFunction {

	private ArrayList<Record> context = new ArrayList<Record>();

	public void add(Record r) {
		synchronized (this) {
			context.add(r);
		}
	}

	public int count() {
		synchronized (this) {
			return context.size();
		}
	}

	public ArrayList<Record> getContext() {
		return context;
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for(Record r : context){
			sb.append("<Key="+ r.getKey() + ",Value=" + r.getValue() + ">");
		}
		return sb.toString();
	}

	public Map<String, Context> combiner() {
		Map<String, Context> out = new HashMap<String, Context>();

		synchronized (this) {
			for (Record t : context) {
				String key = t.getKey();
				Context c = out.get(key);

				if (c == null)
					c = new Context();

				c.add(t);
				out.put(key, c);
			}
		}
		return out;
	}
}
