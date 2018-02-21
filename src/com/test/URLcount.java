package com.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import com.framework.Context;
import com.framework.Framework;
import com.framework.MapFunction;
import com.framework.Record;
import com.framework.ReduceFunction;

public class URLcount {

	public static void main(String[] args) {
		Framework f = new Framework();
		f.setMapFunction(new CountMap());
		f.setReduceFunction(new CountReduce());
		f.setContext(new Reader("data/urls_b.txt"));
		Context results = f.process();
		System.out.println("------------------------------");
		System.out.println("WordCount");
		System.out.println("------------------------------");
		System.out.println(results);
		System.out.println("------------------------------");
	}

	private static class Reader extends Context {
		Reader(String filename) {
			super();
			StringBuffer sb = new StringBuffer();
			try {
				BufferedReader input = new BufferedReader(new FileReader(filename));
				try {
					String line = null;
					while ((line = input.readLine()) != null) {
						sb.append(line.trim()).append(" ");
					}
				} finally {
					input.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			Record tmpr = new Record();
			tmpr.setKey(sb.toString());
			tmpr.setValue(0);
			super.getContext().add(tmpr);
		}
	}

	private static class CountMap implements MapFunction {
		@Override
		public void map(Context c, Record r) {
			String line = r.getKey().toString();
			StringTokenizer tokenizer = new StringTokenizer(line);
			while (tokenizer.hasMoreTokens()) {
				String key = tokenizer.nextToken();
				Record mr = new Record();
				mr.setKey(key);
				mr.setValue(1);
				c.add(mr);
			}
		}
	}

	private static class CountReduce implements ReduceFunction {
		@Override
		public void reduce(Context out, String key, Context in) {
			int count = in.count();
			Record rr = new Record();
			rr.setKey(key);
			rr.setValue(count);
			out.add(rr);
		}
	}
}
