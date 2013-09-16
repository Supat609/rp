package src;

import java.util.ArrayList;
import java.util.Arrays;

public class SysCall {
	String SysCallName;
	ArrayList<String> SysCallParams;
	String SysCallVal;
	ArrayList<MicroOp> mOp;
	int[] vts;
	enum scTypes {singleoperator,clone,others};
	scTypes type;
	
	SysCall(String t) {
		if (t.contains(": ")) {
			SysCallName = "SingleOperator";
			SysCallVal = t;
		} else {
			String[] tArray = t.split(" = ");
			if (tArray[0].contains("(")) {
				SysCallParams = new ArrayList<String>();
				SysCallName = t.substring(0, t.indexOf('('));
				String ts = t.substring(t.indexOf('(')+1,t.indexOf(')'));
				String[] tsArray = ts.split(",");
				for (String s: tsArray) 
					SysCallParams.add(s);
			} else
				SysCallName = tArray[0];
			SysCallVal = tArray[1];
		}
//		setSCType();
	}
	
	private void setSCType() {
		if (SysCallName.equals("SingleOperator")) 
			type = scTypes.singleoperator;
		else if (SysCallName.equals("clone"))
			type = scTypes.clone;
		else
			type = scTypes.others;
	}

	SysCall() {
	}
	
//	void initVTS(int vtsNo, int pos, int val) {
//		vts = new ArrayList<Integer>();
//		for (int i=1; i <= vtsNo; i++) {
//			vts.add(new Integer(0));		
//		}
//		vts.set(pos-1, new Integer(val));
//		System.out.println(vts);
//	}
//	
//	void updateVTS(int pos, int val) {
//		vts.set(pos, val);
//	}
//	
	void add_microOperation(String inStr) {
		if (mOp == null) {
			mOp = new ArrayList<MicroOp>();
		}
		mOp.add(new MicroOp(inStr));
	}

	void newVTS(Integer maxThread) {
		vts = new int[maxThread];
	}

	void setVTS(int thCnt, int cnt) {
		vts[thCnt] = cnt;
	}

//	ArrayList<Integer> getVTS() {
//		return vts;
//	}
//	
//	void setVTS(ArrayList<Integer> inVTS) {
//		vts = inVTS;
//	}
//	
//	ArrayList<MicroOp> getMOP() {
//		return mOp;
//	}
	
}
