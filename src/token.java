package src;

import java.util.ArrayList;
import java.util.Arrays;

public class token {
	String SysCallName;
	ArrayList<String> SysCallParams;
	String SysCallVal;
	ArrayList<Integer> vts;		// Vector Time Stamp
	ArrayList<microOp> mOp;
	
	token(String t1, String t2) {
		if (t1.contains("(")) {
			SysCallParams = new ArrayList<String>();
			SysCallName = t1.substring(0, t1.indexOf('('));
			String ts = t1.substring(t1.indexOf('(')+1,t1.indexOf(')'));
			String[] tsArray = ts.split(",");
			for (String s: tsArray) {
				SysCallParams.add(s);
			}	
		} else {
			SysCallName = t1;
		}
		SysCallVal = t2;
	}
	
	token() {
	}
	
	void initVTS(int vtsNo, int pos, int val) {
		vts = new ArrayList<Integer>();
		for (int i=1; i <= vtsNo; i++) {
			vts.add(new Integer(0));		
		}
		vts.set(pos-1, new Integer(val));
		System.out.println(vts);
	}
	
	void updateVTS(int pos, int val) {
		vts.set(pos, val);
	}
	
	void add_microOperation(String inStr) {
		if (mOp == null) {
			mOp = new ArrayList<microOp>();
		}
		mOp.add(new microOp(inStr));
	}

	ArrayList<Integer> getVTS() {
		return vts;
	}
	
	void setVTS(ArrayList<Integer> inVTS) {
		vts = inVTS;
	}
	
	ArrayList<microOp> getMOP() {
		return mOp;
	}
	
}
