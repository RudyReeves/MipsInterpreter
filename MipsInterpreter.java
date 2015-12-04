import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MipsInterpreter {

	public static final HashMap<String, MipsInstruction> MIPS_INSTRUCTIONS = new HashMap<String, MipsInstruction>();

	public static final int REGISTER_AT = 1;
	public static final int REGISTER_GP = 28;
	public static final int REGISTER_SP = 29;
	public static final int REGISTER_RA = 31;
	public static final int REGISTER_HI = 32;
	public static final int REGISTER_LO = 33;

	/* Begin huge list of MIPS instructions... */
	static {
		MIPS_INSTRUCTIONS.put("noop", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				// Do nothing.
			}
		});

		MIPS_INSTRUCTIONS.put("move", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				if (rt.label.length() > 0)
					rs.label = rt.label;
				else
					rs.setValue(rt.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("add", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() + rt.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("addu", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() + rt.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("sub", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() - rt.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("subu", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() - rt.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("slt", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() < rt.getValue() ? 1 : 0);
			}
		});

		MIPS_INSTRUCTIONS.put("sltu", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() < rt.getValue() ? 1 : 0);
			}
		});

		MIPS_INSTRUCTIONS.put("and", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() & rt.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("or", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() | rt.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("xor", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() ^ rt.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("sll", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() << immediate);
			}
		});

		MIPS_INSTRUCTIONS.put("sllv", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() << rt.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("srl", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() >>> immediate);
			}
		});

		MIPS_INSTRUCTIONS.put("sra", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() >> immediate);
			}
		});

		MIPS_INSTRUCTIONS.put("srlv", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() >>> rt.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("mfhi", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(hi.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("mflo", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(lo.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("mult", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				hi.setValue(0);
				lo.setValue(rs.getValue() * rt.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("multu", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				hi.setValue(0);
				lo.setValue(rs.getValue() * rt.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("div", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				hi.setValue(rs.getValue() / rt.getValue());
				lo.setValue(rs.getValue() % rt.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("divu", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				hi.setValue(rs.getValue() / rt.getValue());
				lo.setValue(rs.getValue() % rt.getValue());
			}
		});

		MIPS_INSTRUCTIONS.put("addi", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() + immediate);
			}
		});

		MIPS_INSTRUCTIONS.put("addiu", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() + immediate);
			}
		});

		MIPS_INSTRUCTIONS.put("slti", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() < immediate ? 1 : 0);
			}
		});

		MIPS_INSTRUCTIONS.put("sltiu", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() < immediate ? 1 : 0);
			}
		});

		MIPS_INSTRUCTIONS.put("andi", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() & immediate);
			}
		});

		MIPS_INSTRUCTIONS.put("ori", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() | immediate);
			}
		});

		MIPS_INSTRUCTIONS.put("xori", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(rs.getValue() ^ immediate);
			}
		});

		MIPS_INSTRUCTIONS.put("li", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(immediate);
			}
		});

		MIPS_INSTRUCTIONS.put("la", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.label = label;
			}
		});

		MIPS_INSTRUCTIONS.put("lui", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				rd.setValue(immediate << 16);
			}
		});

		MIPS_INSTRUCTIONS.put("j", new MipsInstruction() {
			public char getType() {return 'J';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				// Do nothing.
			}
		});

		MIPS_INSTRUCTIONS.put("beq", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				// Do nothing.
			}
		});

		MIPS_INSTRUCTIONS.put("bne", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				// Do nothing.
			}
		});

		MIPS_INSTRUCTIONS.put("beqz", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				// Do nothing.
			}
		});

		MIPS_INSTRUCTIONS.put("bgez", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				// Do nothing.
			}
		});

		MIPS_INSTRUCTIONS.put("bgtz", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				// Do nothing.
			}
		});

		MIPS_INSTRUCTIONS.put("blez", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				// Do nothing.
			}
		});

		MIPS_INSTRUCTIONS.put("bltz", new MipsInstruction() {
			public char getType() {return 'I';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				// Do nothing.
			}
		});

		MIPS_INSTRUCTIONS.put("lw", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				// Do nothing.
			}
		});

		MIPS_INSTRUCTIONS.put("lb", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				// Do nothing.
			}
		});

		MIPS_INSTRUCTIONS.put("sw", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				// Do nothing.
			}
		});

		MIPS_INSTRUCTIONS.put("sb", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				// Do nothing.
			}
		});

		MIPS_INSTRUCTIONS.put("jal", new MipsInstruction() {
			public char getType() {return 'J';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				// Do nothing.
			}
		});

		MIPS_INSTRUCTIONS.put("jr", new MipsInstruction() {
			public char getType() {return 'R';}
			public void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label) {
				// Do nothing.
			}
		});
	}
	/* End huge list of MIPS instructions... */

	public static void main(String[] args) {
		new MipsInterpreter().readFile(args[0]);
	}

	private MipsRegister[] registers;
	private HashMap<String, Integer> labels;
	private HashMap<String, ArrayList<Integer>> spaceLabels;
	private HashMap<String, String> asciizLabels;
	private Scanner scanner;
	private int pc;
	private boolean terminated;

	public MipsInterpreter() {
		registers = new MipsRegister[34];
		for (int i = 0; i < 34; i++)
			registers[i] = new MipsRegister(this);

		labels = new HashMap<String, Integer>();
		spaceLabels = new HashMap<String, ArrayList<Integer>>();
		asciizLabels = new HashMap<String, String>();

		scanner = new Scanner(System.in);
	}

	public void readFile(String fileName) {
		ArrayList<String> lines = new ArrayList<String>();
		int firstLineOfText = 0;
		boolean inText = false;
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
			String line = "";
			int lastLineNumber = -1;
			while (true) {
				try {
					lastLineNumber++;
					line = bufferedReader.readLine();
					if (line == null) break;
					line = line.trim();
					lines.add(line);
					if (line.length() == 0) continue;
					if (line.startsWith(".data")) {
						// Do nothing.
					} else if (line.startsWith(".text")) {
						inText = true;
						firstLineOfText = lastLineNumber + 1;
					} else {
						int hashIndex = line.indexOf('#');
						if (hashIndex >= 0) {
							line = line.substring(0, hashIndex);
							lines.set(lastLineNumber, line);
						}
						int colonIndex = line.indexOf(':');
						if (colonIndex >= 0) {
							String labelName = line.substring(0, colonIndex);
							String afterLabel = line.substring(colonIndex + 1, line.length()).trim();
							if (inText) {
								labels.put(labelName, lastLineNumber);
							} else if (afterLabel.startsWith(".asciiz")) {
								String afterAsciiz = afterLabel.substring(".asciiz".length() + 1, afterLabel.length()).trim();
								int quoteIndex = afterAsciiz.indexOf('\"');
								String afterQuote = afterAsciiz.substring(quoteIndex + 1, afterAsciiz.length());
								String asciizString = afterQuote.substring(0, afterQuote.indexOf('\"'));
								asciizLabels.put(labelName, asciizString);
							} else if (afterLabel.startsWith(".space")) {
								String afterSpace = afterLabel.substring(".space".length() + 1, afterLabel.length()).trim();
								ArrayList<Integer> heap = spaceLabels.get(labelName);
								if (heap == null) {
									heap = new ArrayList<Integer>();
									spaceLabels.put(labelName, heap);
								}
								heap.add(Integer.parseInt(afterSpace));
							}
							lines.set(lastLineNumber, afterLabel);
						}
					}
				} catch (IOException e) {
					System.err.println("Couldn't read line " + lastLineNumber + ".");
				}
			}
			try {
				bufferedReader.close();
			} catch (IOException e) {
				System.err.println("Couldn't close file.");
			}
			for (pc = firstLineOfText; pc < lastLineNumber && !terminated; pc++) {
				executeMipsInstruction(lines.get(pc), pc);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Invalid file: " + fileName);
		}
	}

	public void executeMipsInstruction(String mipsString, int lineNumber) {
		mipsString = mipsString.trim();
		if (mipsString.length() == 0) return;
		if (mipsString.startsWith("syscall") || mipsString.startsWith(".syscall")) {
			syscall();
			return;
		}
		int firstSpace = mipsString.indexOf(' ');
		int firstTab = mipsString.indexOf('\t');
		if (firstSpace < 0 && firstTab < 0) {
			terminated = true;
			return;
		}

		int firstWhitespace = firstSpace;
		if (firstSpace < 0) firstWhitespace = firstTab;

		String instructionString = mipsString.substring(0, firstWhitespace).trim();
		String restOfString = mipsString.substring(firstWhitespace + 1, mipsString.length()).trim();
		String[] tokens = restOfString.split(",");
		for (int i = 0; i < tokens.length; i++)
			tokens[i] = tokens[i].trim();
		if (tokens.length < 1 || tokens.length > 3) {
			terminated = true;
			return;
		}

		MipsInstruction mipsInstruction = MIPS_INSTRUCTIONS.get(instructionString.toLowerCase());
		if (mipsInstruction == null) {
			terminated = true;
			return;
		}

		MipsRegister rd = null, rs = null, rt = null;
		MipsRegister hi = registers[REGISTER_HI], lo = registers[REGISTER_LO];
		int immediate = 0;
		String label = "";

		String inst = instructionString.toLowerCase();
		int registerIndex;

		switch (mipsInstruction.getType()) {
		case 'R':
			switch (tokens.length) {
			case 1:
				if (inst.equals("jr")) {
					if (MipsRegister.parse(tokens[0]) != REGISTER_RA)
						return;
					pc = registers[REGISTER_RA].getValue();
					return;
				}
				rd = getMipsRegisterByString(tokens[0]);
				break;
			case 2:
				rs = getMipsRegisterByString(tokens[0]);
				registerIndex = MipsRegister.parse(tokens[1]);
				if (registerIndex >= 0) {
					rt = registers[registerIndex];
				} else if (isLoadInstruction(inst) || isStoreInstruction(inst)) {
					int parensIndex = tokens[1].indexOf('(');
					if (parensIndex < 0) {
						terminated = true;
						return;
					}
					String arrayIndexString = tokens[1].substring(0, parensIndex).trim();
					int arrayIndex = 0;
					if (arrayIndexString.length() != 0)
						arrayIndex = Integer.parseInt(arrayIndexString);
					String insideParens = tokens[1].substring(parensIndex + 1, tokens[1].length() - 1).trim();
					rt = getMipsRegisterByString(insideParens);
					if (rt.label.length() == 0) {
						terminated = true;
						return;
					}
					ArrayList<Integer> heap = spaceLabels.get(rt.label);
					if (isLoadInstruction(inst)) {
						rs.setValue(heap.get(arrayIndex / 4));
					} else if (isStoreInstruction(inst)) {
						if (arrayIndex / 4 >= heap.size()) {
							heap.add(rs.getValue());
						} else {
							heap.set(arrayIndex / 4, rs.getValue());
						}
					}
					return;
				}
				break;
			case 3:
				rd = getMipsRegisterByString(tokens[0]);
				registerIndex = MipsRegister.parse(tokens[2]);
				if (registerIndex > 0) {
					rs = getMipsRegisterByString(tokens[1]);
					rt = registers[registerIndex];
				} else if (labels.get(tokens[2]) != null) {
					registerIndex = MipsRegister.parse(tokens[1]);
					if (registerIndex >= 0) {
						rs = getMipsRegisterByString(tokens[1]);
						if (inst.equals("beq") && rd.getValue() == rs.getValue()) {
							pc = labels.get(tokens[2]) - 1;
						} else if (inst.equals("bne") && rd.getValue() != rs.getValue()) {
							pc = labels.get(tokens[2]) - 1;
						} else if (!isZeroRegister(tokens[2])) {
							immediate = Integer.parseInt(tokens[2]);
							return;
						}
					} else {
						int value = Integer.parseInt(tokens[1]);
						if (inst.equals("beq") && rd.getValue() == value) {
							pc = labels.get(tokens[2]) - 1;
						} else if (inst.equals("bne") && rd.getValue() != value)
							pc = labels.get(tokens[2]) - 1;
						}
					}
				}
			break;
		case 'I':
			if (tokens.length != 2 && tokens.length != 3) break;
			rd = getMipsRegisterByString(tokens[0]);
			if (labels.get(tokens[1]) != null) {
				if (inst.equals("beqz") && rd.getValue() == 0) {
					pc = labels.get(tokens[1]) - 1;
				} else if (inst.equals("bgz") && rd.getValue() > 0) {
					pc = labels.get(tokens[1]) - 1;
				} else if (inst.equals("bgtz") && rd.getValue() >= 0) {
					pc = labels.get(tokens[1]) - 1;
				} else if (inst.equals("blz") && rd.getValue() < 0) {
					pc = labels.get(tokens[1]) - 1;
				} else if (inst.equals("bltz") && rd.getValue() <= 0) {
					pc = labels.get(tokens[1]) - 1;
				}
				return;
			}
			registerIndex = MipsRegister.parse(tokens[1]);
			if (registerIndex >= 0 && tokens.length == 3) {
				rs = registers[registerIndex];
				immediate = Integer.parseInt(tokens[2]);
			} else if (!isZeroRegister(tokens[1]) && tokens.length == 2) {
				try {
					immediate = Integer.parseInt(tokens[1]);
				} catch (NumberFormatException e) {
					label = tokens[1];
				}
			}
			break;
		case 'J':
			if (tokens.length != 1) return;
			if (inst.equals("j")) {
				pc = labels.get(tokens[0]) - 1;
			} else if (inst.equals("jal")) {
				registers[REGISTER_RA].setValue(lineNumber);
				pc = labels.get(tokens[0]) - 1;
			}
			return;
		}
		mipsInstruction.call(pc, rd, rs, rt, hi, lo, immediate, label);
	}

	public void syscall() {
		switch (registers[2].getValue()) {
		case 1:
			System.out.print("" + registers[4]);
			break;
		case 4:
			System.out.print(registers[4].toString().replace("\\n", "\n"));
			break;
		case 5:
			registers[2].setValue(scanner.nextInt());
			break;
		case 8:
			String wholeLine = scanner.nextLine();
			int numCharsToRead = Math.min(registers[5].getValue(), wholeLine.length());
			String label = registers[4].label;
			spaceLabels.remove(label);
			asciizLabels.put(label, wholeLine.substring(0, numCharsToRead));
			break;
		case 10:
			terminated = true;
			break;
		case 11:
			String character = String.valueOf(Character.toChars(registers[4].getValue()));
			System.out.print(character);
			break;
		}
	}

	public String getLabel(String labelName) {
		Integer intLabelValue = labels.get(labelName);
		if (intLabelValue != null) return intLabelValue.toString();
		ArrayList<Integer> intArrayLabelValue = spaceLabels.get(labelName);
		if (intArrayLabelValue != null) return intArrayLabelValue.toString();
		return asciizLabels.get(labelName);
	}

	public MipsRegister getMipsRegisterByString(String registerString) {
		return registers[MipsRegister.parse(registerString)];
	}

	private boolean isZeroRegister(String registerString) {
		return registerString.equals("$0") || registerString.equals("$zero");
	}

	private boolean isLoadInstruction(String instructionString) {
		return instructionString.equals("lw") || instructionString.equals("lb");
	}

	private boolean isStoreInstruction(String instructionString) {
		return instructionString.equals("sw") || instructionString.equals("sb");
	}
};

class MipsRegister {
	private MipsInterpreter interpreter;
	private int value;
	public String label;

	MipsRegister(MipsInterpreter interpreter) {
		this.interpreter = interpreter;
		value = 0;
	}

	public static int parse(String registerString) {
		if (registerString.charAt(0) != '$') return -1;

		if (registerString.equals("$at"))
			return MipsInterpreter.REGISTER_AT;
		else if (registerString.equals("$gp"))
			return MipsInterpreter.REGISTER_GP;
		else if (registerString.equals("$sp"))
			return MipsInterpreter.REGISTER_SP;
		else if (registerString.equals("$ra"))
			return MipsInterpreter.REGISTER_RA;

		try {
			String registerNumber = registerString.substring(1, registerString.length());
			int registerIndex = Integer.parseInt(registerNumber);
			if (registerIndex < 0 || registerIndex > 31) return -1;
			return registerIndex;
		} catch (NumberFormatException e) {
			int offset = 0;
			switch (registerString.charAt(1)) {
			case 'v':
				offset = 2;
				break;
			case 'a':
				offset = 4;
				break;
			case 't':
				offset = 8;
				break;
			case 's':
				offset = 16;
				break;
			}
			int registerIndex = Integer.parseInt("" + registerString.charAt(2));
			return Integer.parseInt("" + (registerIndex + offset));
		}
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		label = "";
	}

	public String toString() {
		if (label.length() > 0) return interpreter.getLabel(label);
		return "" + value;
	}
}

abstract class MipsInstruction {
	public abstract char getType();
	public abstract void call(int pc, MipsRegister rd, MipsRegister rs, MipsRegister rt, MipsRegister hi, MipsRegister lo, int immediate, String label);
}