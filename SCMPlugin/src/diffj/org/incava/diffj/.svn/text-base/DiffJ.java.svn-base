package diffj.org.incava.diffj;

import java.io.*;
import java.util.*;

import diffj.net.sourceforge.pmd.ast.*;
import diffj.org.incava.analysis.*;
import diffj.org.incava.io.*;
import diffj.org.incava.java.*;
import diffj.org.incava.qualog.Qualog;
import diffj.org.incava.util.Arrays;
import diffj.org.incava.util.TimedEvent;
import diffj.org.incava.util.TimedEventSet;
import dubaj.tr.Relatorio;


public class DiffJ {
	private TimedEventSet totalInit = new TimedEventSet();

	private TimedEventSet totalParse = new TimedEventSet();

	private TimedEventSet totalAnalysis = new TimedEventSet();

	private Report report = null;

	private int exitValue;

	public DiffJ(String[] args)
    {
        dubaj.tr.Ace.set(true, 25, 4, 20, 25);
        dubaj.tr.Ace.setOutput(Qualog.VERBOSE, Qualog.LEVEL4);
        dubaj.tr.Ace.setOutput(Qualog.QUIET,   Qualog.LEVEL2);

        Options  opts  = Options.get();
        String[] names = opts.process(args);

        if (dubaj.tr.Ace.verbose()) {
            dubaj.tr.Ace.log("properties", System.getProperties());
        }

        if (opts.briefOutput) {
            report = new BriefReport(System.out);
        }
        else {
            report = new DetailedReport(System.out);
        }

        exitValue = 0;

        if (names.length >= 2) {
//            tr.Ace.log("names[0]: " + names[0] + "; names[" + (names.length - 1) + "]: " + names[names.length - 1]);
//            File toFile   = new File(names[names.length - 1]);
//
//            for (int ni = 0; ni < names.length - 1; ++ni) {
//                File fromFile = new File(names[ni]);
//                process(fromFile, toFile);
//            }
        	
        	Relatorio.zerarVariaveis();
        	
        	try {
        	
	        	for (int nFiles = 0; nFiles < names.length -1; nFiles++) {
	        		File toFile = new File(names[nFiles + 1]);
	        		File fromFile = new File(names[nFiles]);
	        		process(fromFile, toFile);
	        	}
        	}
        	catch(Exception ex)
        	{
        		
        	}
        }
        else {
            System.err.println("usage: diffj from-file to-file");
            exitValue = 1;
        }
    }

	protected List getJavaFiles(File fd) {
		return java.util.Arrays.asList(fd.list(new FilenameFilter() {
			public boolean accept(File dir, String pathname) {
				File f = new File(dir, pathname);
				return f.isDirectory()
						|| (f.isFile() && pathname.endsWith(".java"));
			}
		}));
	}

	protected void process(File from, File to) {
		dubaj.tr.Ace.log("from: " + from + "; to: " + to);

		boolean fromIsStdin = from.getName().equals("-");
		boolean toIsStdin = to.getName().equals("-");

		if ((fromIsStdin || from.isFile()) && (toIsStdin || to.isFile())) {
			dubaj.tr.Ace.log("from: " + from + "; to: " + to);
			
			Relatorio.getMudancaClasse().calcularCodeChurn(from	,to);
			Relatorio.getMudancaClasse().calcularLinesOfCode(to);
			
			try {
				String fromStr = null;
				FileReader fromRdr = fromIsStdin ? new FileReader(
						FileDescriptor.in) : new FileReader(from);
				fromStr = FileExt.read(fromRdr, true);

				FileReader toRdr = null;
				String toStr = null;
				if (toIsStdin) {
					if (fromIsStdin) {
						toStr = fromStr; // need to clone this string?
					} else {
						toRdr = new FileReader(FileDescriptor.in);
					}
				} else {
					toRdr = new FileReader(to);
				}

				if (toRdr != null) {
					toStr = FileExt.read(toRdr, true);
				}

				ASTCompilationUnit fromCu = compile(from.getName(),
						new StringReader(fromStr), Options.get().fromSource);
				ASTCompilationUnit toCu = compile(to.getName(),
						new StringReader(toStr), Options.get().toSource);

				report.reset(fromIsStdin ? "-" : from.getPath(), fromStr,
						toIsStdin ? "-" : to.getPath(), toStr);

				CompilationUnitDiff cud = new CompilationUnitDiff(report, true);
				cud.compare(fromCu, toCu);
				if (report.getDifferences().size() > 0) {
					exitValue = 1;
				}

				report.flush();
			} catch (FileNotFoundException e) {
				System.out.println("Error opening file: " + e.getMessage());
				exitValue = -1;
			} catch (IOException e) {
				System.out.println("I/O error: " + e);
				exitValue = -1;
			} catch (Throwable t) {
				dubaj.tr.Ace.log("from", from);
				dubaj.tr.Ace.log("to", to);
				t.printStackTrace();
			}
		} else if (!from.exists()) {
			System.err.println(from.getPath() + " does not exist");
			// O arquivo foi adicionado
			Relatorio.getMudancaClasse().incrementarMudancasClasse();
			Relatorio.getMudancaClasse().calcularCodeChurn(from	,to);
			Relatorio.getMudancaClasse().calcularLinesOfCode(to);
			
		} else if (!to.exists()) {
			System.err.println(to.getPath() + " does not exist");
			// O arquivo foi removido
			Relatorio.getMudancaClasse().incrementarMudancasClasse();
			Relatorio.getMudancaClasse().calcularLinesOfCode(from);
		
		} else if (from.isDirectory()) {
			if (to.isDirectory()) {
				List fromFiles = getJavaFiles(from);
				List toFiles = getJavaFiles(to);

				Set merged = new TreeSet();

				merged.addAll(fromFiles);
				merged.addAll(toFiles);

				Iterator it = merged.iterator();
				while (it.hasNext()) {
					String fname = (String) it.next();
					File fromFile = new File(from, fname);
					File toFile = new File(to, fname);

					if (fromFile.isDirectory() && toFile.isDirectory()) {
						if (Options.recurse.booleanValue()) {
							process(fromFile, toFile);
						} else {
							dubaj.tr.Ace.log("not recursing");
						}
					} else {
						process(fromFile, toFile);
					}
				}
			} else {
				File fromFile = new File(from, to.getPath());
				dubaj.tr.Ace.log("fromFile: " + fromFile + " (" + fromFile.exists()
						+ ")");
				process(fromFile, to);
			}
		} else if (to.isDirectory()) {
			File toFile = new File(to, from.getPath());
			dubaj.tr.Ace.log("toFile: " + toFile + " (" + toFile.exists() + ")");
			process(from, toFile);
		}
	}

	protected ASTCompilationUnit compile(String name, Reader rdr,
			String sourceVersion) {
		TimedEvent init = new TimedEvent(totalInit);
		try {
			JavaCharStream jcs = new JavaCharStream(rdr);
			JavaParser parser = new JavaParser(jcs);

			if (sourceVersion.equals("1.3")) {
				dubaj.tr.Ace.log("creating 1.3 parser");
				parser.setJDK13();
			} else if (sourceVersion.equals("1.4")) {
				dubaj.tr.Ace.log("creating 1.4 parser");
			} else if (sourceVersion.equals("1.5")) {
				dubaj.tr.Ace.log("creating 1.5 parser");
				parser.setJDK15();
			} else {
				System.err.println("ERROR: source version '" + sourceVersion
						+ "' not recognized");
				System.exit(-1);
			}

			init.end();

			// tr.Ace.log("running parser");

			TimedEvent parse = new TimedEvent(totalParse);
			ASTCompilationUnit cu = parser.CompilationUnit();
			parse.end();

			long total = init.duration + parse.duration; // + analysis.duration;
			dubaj.tr.Ace.log("time: total: " + total + "; init: " + init.duration
					+ "; parse: " + parse.duration + "; " + name);
			// tr.Ace.log("time: total: " + total + "; init: " + init.duration +
			// "; parse: " + parse.duration + "; analysis: " + analysis.duration
			// + "; " + name);

			return cu;
		} catch (TokenMgrError tme) {
			System.out.println("Error parsing (tokenizing) " + name + ": "
					+ tme.getMessage());
			exitValue = 1;
			return null;
		} catch (ParseException e) {
			System.out
					.println("Parse error in " + name + ": " + e.getMessage());
			exitValue = -1;
			return null;
		}
	}

	public static void main(String[] args) {
		DiffJ dj = new DiffJ(args);
		// Relatorio.PreencheHashMapMetodo();
		// System.out.println(Relatorio.GerarRelatorio());
		System.exit(dj.exitValue);
	}
}
