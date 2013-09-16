package src;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import src.SysCall.scTypes;

public class RacePro {
	
	static ArrayList<SysCall> scList = null;
	static ArrayList<MicroOp> moList = null;
	static Map<Integer,ArrayList<SysCall>> threads = new HashMap<Integer,ArrayList<SysCall>>();
	static Integer maxThread = new Integer(0);
	
	public static void main(String[] args) {
		scribeParser(args);
		constructVTS();
		LamportAlgorithm();
		System.out.println("Done!");
	}

	private static void LamportAlgorithm() {
		Queue<Pair<Integer,SysCall>> q = new Queue<Pair<Integer,SysCall>>();
		int thCnt = 1;
		for (ArrayList<SysCall> scList : threads.values()) {
			for (SysCall sc : scList) {
				if (sc.SysCallName.equals("clone")) {
					System.out.println(scList.indexOf(sc)+">>"+thCnt+"::"+sc.SysCallName+":"+sc.SysCallVal);
					q.enQ(new Pair(new Integer(thCnt),sc));
				}
			}
			thCnt++;
		}
		
//		System.out.println(q.element.size());
//		for(Pair<Integer,Integer> p : q.element) {
//			System.out.println(p.first+"::"+p.second);
//		}

		while (q.notEmpty()) {
			Pair<Integer,SysCall> p = q.deQ();
//			System.out.println(threads.get(new Integer(p.first)).size()+":"+p.second);
			thCnt = p.first;
			SysCall sc = p.second;
			if (sc.type == scTypes.clone) {
				System.out.println(sc.vts.length);
				for(int j=0; j<maxThread; j++)
					System.out.print(sc.vts[j]+" ");
				System.out.println();
//				updateVTSinThread(sc.SysCallVal,0,sc.vts);
			} else {
				System.out.println("Not Clone :: "+sc.SysCallName);
			}
		}
	}

	private static void updateVTSinThread(String threadNoStr, int start, int[] vts) {
		int threadNo = Integer.parseInt(threadNoStr);
		scList = threads.get(threadNo);
		for (int i=start; i<scList.size(); i++) {
			for(int j=0; j<maxThread; j++)
				System.out.print(scList.get(i).vts[j]+" ");
			System.out.println("::"+scList.get(i).SysCallName);
		}
	}

	private static void constructVTS() {
		int thCnt = 0;
		for (ArrayList<SysCall> scList : threads.values()) {
			for (SysCall sc : scList) {
				sc.newVTS(maxThread);
			}
			int cnt = 1;
			for (SysCall sc : scList) {
				sc.setVTS(thCnt,cnt);
//				for(int i=0; i<maxThread; i++) 
//					System.out.print(sc.vts[i]+" ");
//				System.out.println();
				cnt++;
			}
			thCnt++;
		}
		
	}

	private static void scribeParser(String[] args) {
		String s = "";
		int tNo;
		Integer tNoObj;
		boolean flag;
		SysCall sc = null;			// Always hung available! At least one.

		try {
			BufferedReader in = new BufferedReader(new FileReader(args[0]));
			while (s != null) {			
				flag = true;
				while (flag) {
					s = in.readLine();
					if (s != null) {
						if ((! (s.contains("   "))) && (!(s.contains("queue EOF")))) {
							flag = false;

							tNo = getNumber(s);
							if (tNo > maxThread)
								maxThread = new Integer(tNo);
							if (sc != null) {				// Existing previous "sc"
								tNoObj = new Integer(tNo);
								scList = threads.get(tNoObj);
								threads.remove(tNoObj);
								if (scList == null) {
									scList = new ArrayList<SysCall>();
//									System.out.println("#"+tNo);
								}
								scList.add(sc);
//								System.out.println(tNoObj.toString()+"$$"+scList.size());
								threads.put(tNoObj, scList);
//								System.out.println("###"+threads.size()+"::"+tNoObj+":"+scList.size());
							}								
							sc = new SysCall(getLine(s));	// Creating a new "sc" anyway
							
							if (sc.SysCallName.equals("SingleOperator")) {
								sc.add_microOperation(sc.SysCallVal);
							}
							
//							System.out.print(threads.size()+":");
//							System.out.println(tNo+":"+maxThread+">"+s);

//							System.out.println(tNo+":"+s);

						} else if (s.contains("    ")) {
							s = s.substring(7,s.length());
							sc.add_microOperation(s);
//							System.out.println("-"+sc.mOp.size()+"-"+sc.SysCallName+"-"+s);
						}
					} else {
						flag = false;
					}
				}
			}
			
//			int k = 1;
//			for (ArrayList<SysCall> scList : threads.values()) {
//				System.out.println(threads.get(new Integer(k++)).size());
//				System.out.println(scList.size());
//			}
			
			in.close();
		} catch (Exception e) {
			System.err.println("Error:"+e.getMessage());
		}		
	}

	private static String getLine(String s) {
		return s.substring(s.indexOf("]")+2, s.length());
	}

	private static int getNumber(String s) {
		return Integer.parseInt(s.substring(1, s.indexOf("]")));
	}
}
