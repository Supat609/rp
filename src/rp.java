package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.io.*;

public class rp {
	
	static ArrayList<Pair<String,String>> init = new ArrayList<Pair<String,String>>();		// initialization information
	static ArrayList<SysCall> sTokens = new ArrayList<SysCall>();	// scribe tokens
	static ArrayList<SysCall> tokens = new ArrayList<SysCall>();

	static ArrayList<MicroOp> mOps = new ArrayList<MicroOp>();
	static Map<Integer,ArrayList<MicroOp>> envVars = new HashMap<Integer,ArrayList<MicroOp>>();
	static Map<Integer,ArrayList<SysCall>> threads = new HashMap<Integer,ArrayList<SysCall>>();

	static Integer threadNo = 0;
	static Integer threadOld;
	
	public static void main(String[] args) {
		scribeParser(args);
//		System.out.println("::"+threadNo+"::"+threads.size());
		createVTS(--threadNo);
//		constructHappenedBeforeGraph();
		HappenedBeforeGraph();
	}
	
	private static void HappenedBeforeGraph() {
		Stack<Pair<Integer,Integer>> s = new Stack<Pair<Integer,Integer>>();
		Pair<Integer,Integer> p = new Pair(new Integer(2),new Integer(0));
		s.add(p);
		while (!s.empty()) {
			p = s.pop();
			System.out.println(p.first+":"+p.second);
			System.out.println(threads.get(p.first).get(p.second).SysCallName);
			System.out.println(threads.get(p.first).get(p.second).SysCallVal);
			System.out.println(threads.get(p.first).get(p.second).SysCallParams);
		}
	}

	private static void constructHappenedBeforeGraph() {
		Stack<Pair<Integer,Integer>> s = new Stack<Pair<Integer,Integer>>();
		Pair<Integer,Integer> stackToken;
		Integer tokenNo;
		
		final Integer startThreadNo = new Integer(2);	// 0 is for Environment and 1 is for Scribe Main Thread
		final Integer endThreadNo = threads.size()+1;	// Thread Number is always +1
		Integer beginSysCall = new Integer(0);			// Position always Starting at 0
		Integer endSysCall = new Integer(0); 			// Position Finishing at threads.get(startThreadNo).size()-1
		
		ArrayList<Integer> current = new ArrayList<Integer>();		// reset the Current Vector Time Stamp(VTS)
		for (int j=startThreadNo; j<=endThreadNo; j++)				// Create an ArrayList for Current VTS
			current.add(new Integer(0));
		
		stackToken = new Pair<Integer,Integer>(startThreadNo,new Integer(0));
		s.push(stackToken);

		PrintVTC(startThreadNo, endThreadNo);
		
		if (! s.empty()) {
			stackToken = s.pop();
			tokenNo = (Integer)stackToken.first;
			tokens = threads.get(tokenNo);
			beginSysCall = (Integer)stackToken.second;
			endSysCall = tokens.size()-1;			
			for (int j=beginSysCall; j<=endSysCall; j++) {
				System.out.print(current+"::"+tokens.get(j).getVTS());
				tokens.get(j).setVTS(TakeTheGreater(current,tokens.get(j).getVTS()));
				System.out.println(">>"+tokens.get(j).getVTS());
			}
			threads.put(tokenNo, tokens);
		}
		
		PrintVTC(startThreadNo, endThreadNo);
		System.out.println(startThreadNo+":"+endThreadNo+":"+beginSysCall+":"+endSysCall);
	}

	private static void PrintVTC(int s, int e) {
		System.out.println("~~~~~~~~~~~~~~~~~~~~");
		for (int i=s; i<=e; i++) {
			tokens = threads.get(new Integer(i));
			for(SysCall t: tokens)
				System.out.println(t.getVTS());
			System.out.println("~~~~~~~~~~~~~~~~~~~~");
		}
	}
	
	private static ArrayList<Integer> TakeTheGreater(ArrayList<Integer> current, ArrayList<Integer> vts) {
		ArrayList<Integer> r = current;
		for (int i=0; i<r.size(); i++) {
			if (r.get(i) < vts.get(i)) 
				r.set(i, vts.get(i));
		}
		return r;
	}

	private static void createVTS(Integer threadNo) {
		int cnt = 0;
		for(int i = 1; i<=threadNo.intValue(); i++) {
/**/			System.out.println(i);
			ArrayList<SysCall> tokens = threads.get(new Integer(i+1));
/**/			System.out.println(tokens);
			for (SysCall t: tokens) {
				t.initVTS(threadNo,i,++cnt);
			}
			SysCall tt = tokens.get(0);
			tt.updateVTS(i-1,1);
			tokens.set(0, tt);
			threads.put(i+1, tokens);
			cnt = 0;
		}
	}

	private static void scribeParser(String[] inArgs) {
		Integer maxThread = new Integer(0);
		try {
			BufferedReader in = new BufferedReader(new FileReader(inArgs[0]));
			while (in.ready()) {
				  String s = in.readLine();
				  threadOld = threadNo; 
				  threadNo = Integer.parseInt(s.substring(1, s.indexOf("]")));
				  if (threadOld.intValue() != threadNo.intValue()) {
					  if (threadOld.intValue() > 1) {
						  threads.put(threadOld, tokens);
						  envVars.put(threadOld, mOps);
					  }
					  System.out.println("===================================================");
					  tokens = new ArrayList<SysCall>();
					  mOps = new ArrayList<MicroOp>();
					  if (threadNo > maxThread)
						  maxThread = threadNo;
				  }
				  s = s.substring(s.indexOf("]")+2, s.length());
				  if (threadNo.intValue() == 0) {
					  extract_init(s);							// isolating initialization information
				  } else if (threadNo.intValue() == 1) {
					  
					  sTokens = extract_rest(threadNo,s);		// isolating scribe thread
				  } else {
					  tokens = extract_rest(threadNo, s);
				  }
			}
			threads.put(threadOld, tokens);
			envVars.put(threadOld, mOps);
			threadNo = maxThread;
			
//			System.out.println("::"+threads);
//			System.out.println("::"+envVars);
			
			in.close();
		} catch (Exception e) {
			System.err.println("Error:"+e.getMessage());
		}		
	}

	private static ArrayList<SysCall> extract_rest(Integer threadNo, String s) {
		SysCall t;
		MicroOp m;
		if (! s.contains("queue EOF")) {								// *** SKIPPING the end of each QUEUE
			if (s.contains("   ")) {										// All Micro Operator in each System Call
//				System.out.println(">>>>"+s+"::"+tokens.size());
				if (tokens.size() > 0) {
					t = tokens.get(tokens.size()-1);
//					System.out.println("<<<<");
					tokens.remove(tokens.size()-1);
				} else {
					t = new SysCall();
				}
				t.add_microOperation(s);
				tokens.add(t);
			} else {
//				System.out.println("["+threadNo+"] >> "+s);
				if (s.contains(":")) {
					System.out.println("["+threadNo+"] "+"SingleOperator"+"=="+s);
					t = new SysCall("SingelOperator",s);
					t.add_microOperation(s);
					tokens.add(t);
				} else {												// Main System Calls
					String[] tokenPair = s.split(" = ");
					System.out.println("["+threadNo+"] "+tokenPair[0]+"=="+tokenPair[1]);
					tokens.add(new SysCall(tokenPair[0],tokenPair[1]));
				}
			}
		}
		return tokens;
	}

	private static void extract_init(String s) {
		Pair<String,String> p;
		String params = s.substring(6, s.length());
		System.out.println(">>"+params);
		String[] subInitString = params.split(", ");
		for(String initString : subInitString) {
			String[] initPair = initString.split(" = ");
			p = new Pair<String,String>(initPair[0],initPair[1]);
			init.add(p);
		}
	}

}
