package src;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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

//		System.out.println(maxThread);		
//		int thCnt = 0;
//		for (ArrayList<SysCall> scList : threads.values()) {
//			System.out.println(thCnt+":"+scList.size());
//			thCnt++;
//		}
		
//		int i = 1;
//		scList = threads.get(new Integer(2));
//		for (SysCall sc : scList) {
//			System.out.println(i+" : "+sc.SysCallName+"="+sc.SysCallVal);
//			i++;
//		}
		
		System.out.println("Done!");
	}

	private static void LamportAlgorithm() {
		Queue<Pair<Integer,Integer>> q = new Queue<Pair<Integer,Integer>>();
		int thCnt = 0;
		int scCnt;
		int[] maxVTS;
		
		for (ArrayList<SysCall> scList : threads.values()) {
			scCnt = 0;
			for (SysCall sc : scList) {
				if (sc.SysCallName.equals("clone")) {
//					System.out.println(scList.indexOf(sc)+">>"+thCnt+"::"+sc.SysCallName+":"+sc.SysCallVal);
					q.enQ(new Pair(new Integer(thCnt),new Integer(scCnt)));
				}
				scCnt++;
			}
			thCnt++;
		}
		
//		System.out.println(q.element.size());
//		for(Pair<Integer,Integer> p : q.element) {
//			ArrayList<SysCall> scList = threads.get(p.first);
//			System.out.println(p.first+"::"+scList.get(p.second).SysCallName+"="+scList.get(p.second).SysCallVal);
//		}

//		int i = 1;
//		scList = threads.get(new Integer(0));
//		for (SysCall sc : scList) {
//			System.out.println(i+" : "+sc.SysCallName+"="+sc.SysCallVal);
//			i++;
//		}
		
		while (q.notEmpty()) {
			Pair<Integer,Integer> p = q.deQ();
			thCnt = p.first;
			scCnt = p.second;
			SysCall sc = threads.get(thCnt).get(scCnt);
			if (sc.type == scTypes.clone) {
//				System.out.print("~~~ ");
//				for(int j=0; j<=maxThread; j++)
//					System.out.format(" %4d",sc.vts[j]);
//				System.out.println(" >> l = "+sc.vts.length+" >> ");
				maxVTS = updateVTSinAThread(sc.SysCallVal,0,sc.vts);
				maxVTS = updateVTSAfterClone(p,maxVTS,sc.SysCallVal);
				LoopBackToFirst(p);
			} else {
				System.out.println("Error: Impossible Alert: Not Clone ::"+sc.SysCallName);
			}
//			System.out.println("---------------------------------------------------");			
		}

		int j = 0;
		for (ArrayList<SysCall> scList : threads.values()) {
			int i = 1;
//			scList = threads.get(new Integer(1));
			for (SysCall sc : scList) {
//				System.out.format("%d: %4d: ",j,i);
				printArray(sc.vts);
				System.out.println();
//				System.out.println(" : "+sc.SysCallName+"="+sc.SysCallVal);
				i++;
			}
			j++;
		}
		
	}

	private static void LoopBackToFirst(Pair<Integer, Integer> p) {
		int current = p.first;
		int previous = current - 1;
		boolean flag = false;
		int[] maxVTS;
		while (current >= 2) {
			ArrayList<SysCall> scList = threads.get(previous);
			ArrayList<SysCall> scPrev = threads.get(current);
			maxVTS = scPrev.get(scPrev.size()-1).vts;
			for (SysCall sc : scList) {
				if (sc.type == scTypes.wait) {
					if (isInteger(sc.SysCallVal)) {
						if (Integer.parseInt(sc.SysCallVal)==current) {
							flag = true;
						}
					}
				}
				if (flag) {
					maxVTS = sc.findMaxVTS(maxVTS);					
				}
			}
//			System.out.println(previous+","+current);
			previous = current--;
		}
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}

	private static int[] updateVTSAfterClone(Pair<Integer,Integer> p, int[] maxVTS, String caller) {
		int thNo = p.first;
		int scNo = p.second;
		
		ArrayList<SysCall> scList = threads.get(thNo);
		boolean flag = false;
		for (int i=scNo; i<scList.size(); i++) {
			SysCall sc = scList.get(i);
			if ((sc.type == scTypes.wait) && (sc.SysCallVal.equals(caller))) 
				flag = true;
			if (flag) {
				maxVTS = threads.get(thNo).get(i).findMaxVTS(maxVTS);
			}
		}
		return maxVTS;
	}

	private static int[] updateVTSinAThread(String threadNoStr, int start, int[] inVTS) {
		SysCall sc;
		int threadNo = Integer.parseInt(threadNoStr);
		int[] maxVTS = inVTS;
		for (int i=start; i<threads.get(threadNo).size(); i++) {
//			System.out.print("#");
//			printArray(threads.get(threadNo).get(i).vts);
			maxVTS = threads.get(threadNo).get(i).findMaxVTS(maxVTS);
//			System.out.print(" || ");
//			printArray(threads.get(threadNo).get(i).vts);
//			System.out.println();
		}
		return maxVTS;
	}

	private static void printArray(int[] vts) {
		for(int j=0; j<=maxThread; j++) 
			System.out.format(" %4d",vts[j]);
//		System.out.println();
	}

	private static void constructVTS() {
		int thCnt = 0;
		for (ArrayList<SysCall> scList : threads.values()) {
			for (SysCall sc : scList)
				sc.newVTS(maxThread);
			int cnt = 1;
//			System.out.println("*** "+thCnt+"/"+maxThread+" ***");
			for (SysCall sc : scList) {
				sc.setVTS(thCnt, cnt);
				cnt++;
//				for(int k=0; k<sc.vtsSize; k++)
//					System.out.format(" %4d", sc.vts[k]);
//				System.out.println(sc.SysCallName+"=="+sc.SysCallVal);
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
						if ((!(s.contains("   "))) && (!(s.contains("queue EOF")))) {
							flag = false;

							sc = new SysCall(getLine(s));	// Creating a new "sc" anyway
								
							if (sc.SysCallName.equals("SingleOperator")) {
								sc.add_microOperation(sc.SysCallVal);
							}

							tNo = getNumber(s);					// Creating tNoObj for sc
							if (tNo > maxThread)
								maxThread = new Integer(tNo);
							tNoObj = new Integer(tNo);

							scList = threads.get(tNoObj);		// Getting scList from threads
							threads.remove(tNoObj);
							if (scList == null) {
								scList = new ArrayList<SysCall>();
							}
							scList.add(sc);
							threads.put(tNoObj, scList);

						} else if (s.contains("    ")) {
							s = s.substring(7,s.length());
							sc.add_microOperation(s);
						}
					} else {
						flag = false;
					}
				}
			}
			
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
