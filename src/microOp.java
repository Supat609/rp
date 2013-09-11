package src;

public class microOp {
	String fullLine;
	enum microOpType {REGS, DATA_INPUT, DATA_INPUTSTRING, DATA_OUTPUT, DATA_NONDET, DATA_INTERNAL, RL_MMAP, RL_FILESTRUCT, RL_FILE, RL_INODE, RL_PID, RL_FUTEX, FENCE, SIGNAL, SIGNAL_HANDLED  };
	enum accessType {READ, WRITE};
	microOpType type;
	String ptr;
	int size;
	String data;
	accessType access;
	int id;
	int serial;
	String desc;
	int fenceNo;
	
	microOp(String s) {
		fullLine = s;
		defineType();
		switch (type) {
			case REGS : 
				break;
			case DATA_INPUT : 
				break;
			case DATA_INPUTSTRING : 
				break;
			case DATA_OUTPUT : 
				break;
			case DATA_NONDET : 
				break;
			case DATA_INTERNAL : 
				break;
			case RL_MMAP : 
				break;
			case RL_FILESTRUCT : 
				break;
			case RL_FILE : 
				break;
			case RL_INODE : 
				break;
			case RL_PID	: 
				break;
			case RL_FUTEX : 
				break;
			
		}
	}
	
	microOp() {
	}
	
	void defineType() {
		String params;
		String[] pArray;
		String tempStr;
		
		if (fullLine.contains("regs")) {	
			type = microOpType.REGS;
		} else
		if (fullLine.contains("data: input,")) {
			type = microOpType.DATA_INPUT;
			params = fullLine.substring(fullLine.indexOf("ptr"), fullLine.length());
			pArray = params.split(", ");
			ptr = pArray[0].substring(pArray[0].indexOf('=')+2, pArray[0].length());
			size = Integer.parseInt(pArray[1].substring(pArray[1].indexOf('=')+2, pArray[1].length()));
			data = pArray[2];
			
//			System.out.println(params);
//			System.out.println(ptr+":"+size+":"+data);
		} else
		if (fullLine.contains("data: input string,")) {
			type = microOpType.DATA_INPUTSTRING;
			params = fullLine.substring(fullLine.indexOf("ptr"), fullLine.length());
			pArray = params.split(", ");
			ptr = pArray[0].substring(pArray[0].indexOf('=')+2, pArray[0].length());
			size = Integer.parseInt(pArray[1].substring(pArray[1].indexOf('=')+2, pArray[1].length()));
			data = pArray[2];
			
//			System.out.println(params);
////			System.out.println(pArray[0]+":"+pArray[1]+":"+pArray[2]);
//			System.out.println(ptr+":"+size+":"+data);
		} else
		if (fullLine.contains("data: output,")) {
			type = microOpType.DATA_OUTPUT;
			params = fullLine.substring(fullLine.indexOf("ptr"), fullLine.length());
			pArray = params.split(", ");
			ptr = pArray[0].substring(pArray[0].indexOf('=')+2, pArray[0].length());
			size = Integer.parseInt(pArray[1].substring(pArray[1].indexOf('=')+2, pArray[1].length()));
			data = pArray[2];
			
//			System.out.println(params);
////			System.out.println(pArray[0]+":"+pArray[1]+":"+pArray[2]);
//			System.out.println(ptr+":"+size+":"+data);
		} else
		if (fullLine.contains("data: non-det output")) {
			type = microOpType.DATA_NONDET;
			params = fullLine.substring(fullLine.indexOf("ptr"), fullLine.length());
			pArray = params.split(", ");
			ptr = pArray[0].substring(pArray[0].indexOf('=')+2, pArray[0].length());
			size = Integer.parseInt(pArray[1].substring(pArray[1].indexOf('=')+2, pArray[1].length()));
			data = pArray[2];
			
//			System.out.println(params);
////			System.out.println(pArray[0]+":"+pArray[1]+":"+pArray[2]);
//			System.out.println(ptr+":"+size+":"+data);			
		} else
		if (fullLine.contains("data: internal")) {
			type = microOpType.DATA_INTERNAL;
			params = fullLine.substring(fullLine.indexOf("ptr"), fullLine.length());
			pArray = params.split(", ");
			ptr = pArray[0].substring(pArray[0].indexOf('=')+2, pArray[0].length());
			size = Integer.parseInt(pArray[1].substring(pArray[1].indexOf('=')+2, pArray[1].length()));
			data = pArray[2];
			
//			System.out.println(params);
////			System.out.println(pArray[0]+":"+pArray[1]+":"+pArray[2]);
//			System.out.println(ptr+":"+size+":"+data);			
		} else
		if (fullLine.contains("resource lock, type = mmap")) {
			type = microOpType.RL_MMAP;
			params = fullLine.substring(fullLine.indexOf("access"), fullLine.length());
			pArray = params.split(", ");
			if (pArray[0].contains("read"))
				access = accessType.READ;
			else
				access = accessType.WRITE;
			id = Integer.parseInt(pArray[1].substring(pArray[1].indexOf('=')+2, pArray[1].length()));
			serial = Integer.parseInt(pArray[2].substring(pArray[2].indexOf('=')+2, pArray[2].length()));
			desc = pArray[3];
			
//			System.out.println(params);
////			System.out.println(pArray[0]+":"+pArray[1]+":"+pArray[2]);
//			System.out.println(access+":"+id+":"+serial+":"+desc);			
		} else
		if (fullLine.contains("resource lock, type = files_struct")) {
			type = microOpType.RL_FILESTRUCT;
			params = fullLine.substring(fullLine.indexOf("access"), fullLine.length());
			pArray = params.split(", ");
			if (pArray[0].contains("read"))
				access = accessType.READ;
			else
				access = accessType.WRITE;
			id = Integer.parseInt(pArray[1].substring(pArray[1].indexOf('=')+2, pArray[1].length()));
			serial = Integer.parseInt(pArray[2].substring(pArray[2].indexOf('=')+2, pArray[2].length()));
			desc = pArray[3];
			
//			System.out.println(params);
////			System.out.println(pArray[0]+":"+pArray[1]+":"+pArray[2]);
//			System.out.println(access+":"+id+":"+serial+":"+desc);			
		} else
		if (fullLine.contains("resource lock, type = file")) {
			type = microOpType.RL_FILE;
			params = fullLine.substring(fullLine.indexOf("access"), fullLine.length());
			pArray = params.split(", ");
			if (pArray[0].contains("read"))
				access = accessType.READ;
			else
				access = accessType.WRITE;
			id = Integer.parseInt(pArray[1].substring(pArray[1].indexOf('=')+2, pArray[1].length()));
			serial = Integer.parseInt(pArray[2].substring(pArray[2].indexOf('=')+2, pArray[2].length()));
			desc = pArray[3];
			
//			System.out.println(params);
////			System.out.println(pArray[0]+":"+pArray[1]+":"+pArray[2]);
//			System.out.println(access+":"+id+":"+serial+":"+desc);			
		} else
		if (fullLine.contains("resource lock, type = inode")) {
			type = microOpType.RL_INODE;
			params = fullLine.substring(fullLine.indexOf("access"), fullLine.length());
			pArray = params.split(", ");
			if (pArray[0].contains("read"))
				access = accessType.READ;
			else
				access = accessType.WRITE;
			id = Integer.parseInt(pArray[1].substring(pArray[1].indexOf('=')+2, pArray[1].length()));
			serial = Integer.parseInt(pArray[2].substring(pArray[2].indexOf('=')+2, pArray[2].length()));
			desc = pArray[3];
			
//			System.out.println(params);
////			System.out.println(pArray[0]+":"+pArray[1]+":"+pArray[2]);
//			System.out.println(access+":"+id+":"+serial+":"+desc);			
		} else
		if (fullLine.contains("resource lock, type = pid"))	{
			type = microOpType.RL_PID;
			params = fullLine.substring(fullLine.indexOf("access"), fullLine.length());
			pArray = params.split(", ");
			if (pArray[0].contains("read"))
				access = accessType.READ;
			else
				access = accessType.WRITE;
			id = Integer.parseInt(pArray[1].substring(pArray[1].indexOf('=')+2, pArray[1].length()));
			serial = Integer.parseInt(pArray[2].substring(pArray[2].indexOf('=')+2, pArray[2].length()));
			desc = pArray[3];
			
//			System.out.println(params);
////			System.out.println(pArray[0]+":"+pArray[1]+":"+pArray[2]);
//			System.out.println(access+":"+id+":"+serial+":"+desc);			
		} else
		if (fullLine.contains("resource lock, type = futex")) {
			type = microOpType.RL_FUTEX;
			params = fullLine.substring(fullLine.indexOf("access"), fullLine.length());
			pArray = params.split(", ");
			if (pArray[0].contains("read"))
				access = accessType.READ;
			else
				access = accessType.WRITE;
			id = Integer.parseInt(pArray[1].substring(pArray[1].indexOf('=')+2, pArray[1].length()));
			serial = Integer.parseInt(pArray[2].substring(pArray[2].indexOf('=')+2, pArray[2].length()));
			desc = pArray[3];
			
//			System.out.println(params);
////			System.out.println(pArray[0]+":"+pArray[1]+":"+pArray[2]);
//			System.out.println(access+":"+id+":"+serial+":"+desc);			
		} else
		if (fullLine.contains("fence(")) {
			type = microOpType.FENCE;
			params = fullLine.substring(fullLine.indexOf("(")+1, fullLine.length());
			params = params.substring(0, params.indexOf(')'));
			fenceNo = Integer.parseInt(params);
//			System.out.println(params);
////			System.out.println(pArray[0]+":"+pArray[1]+":"+pArray[2]);
//			System.out.println(fenceNo);			
		} else
		if (fullLine.contains("signal handled,")) {
			type = microOpType.SIGNAL_HANDLED;
			params = fullLine.substring(fullLine.indexOf("signal ="));
			
		} else 
		if (fullLine.contains("signal:")) {
			type = microOpType.SIGNAL;
			
		}
	}
	
}
