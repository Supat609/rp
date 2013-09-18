package src;

import java.util.ArrayList;
import java.util.Arrays;

public class SysCall {
	String SysCallName;
	ArrayList<String> SysCallParams;
	String SysCallVal;
	ArrayList<MicroOp> mOp;
	int[] vts;
	int vtsSize;
	enum scTypes {singleoperator,clone,wait,others};
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
		setSCType();
	}
	
	private void setSCType() {
		if (SysCallName.equals("SingleOperator")) 
			type = scTypes.singleoperator;
		else if (SysCallName.contains("clone"))
			type = scTypes.clone;
		else if (SysCallName.contains("wait"))
			type = scTypes.wait;
		else
			type = scTypes.others;
	}

	SysCall() {
	}
	
	void add_microOperation(String inStr) {
		if (mOp == null) {
			mOp = new ArrayList<MicroOp>();
		}
		mOp.add(new MicroOp(inStr));
	}

	void newVTS(Integer maxThread) {
		vtsSize = maxThread+1;
		vts = new int[vtsSize];
	}

	void setVTS(int thCnt, int cnt) {
		vts[thCnt] = cnt;
	}

	public int[] findMaxVTS(int[] maxVTS) {
		for(int i=0; i<vtsSize; i++)
			vts[i] = (vts[i]>=maxVTS[i])?vts[i]:maxVTS[i];
		return vts;
	}
	
}
