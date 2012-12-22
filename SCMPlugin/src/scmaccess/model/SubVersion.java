package scmaccess.model;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.ISVNReporter;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDeltaProcessor;
import org.tmatesoft.svn.core.io.diff.SVNDiffWindow;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import diffj.org.incava.diffj.*;
import dubaj.model.Metrica;
import dubaj.tr.MudancaClasseVO;
import dubaj.tr.Relatorio;
import dubaj.tr.RelatorioFinal;
import dubaj.tr.contagem.metodo.MudancaMetodo;

import scmaccess.model.exceptions.SCMConnectionRefusedException;
import scmaccess.model.objects.Author;
import scmaccess.model.objects.ChangedPath;
import scmaccess.model.objects.Element;
import scmaccess.model.objects.ElementProperty;
import scmaccess.model.objects.RevisionItem;
import scmaccess.model.objects.TypeOfChange;

public class SubVersion extends SCM {

	private SVNRepository repository = null;
	private SVNURL svnUrl = null;
	private LinkedList<String> listDirs = new LinkedList<String>();
	private LinkedList<Node> listDirectories = new LinkedList<Node>();

	public SubVersion() {
	}

	// Bruno
	public static boolean isJavaSourcePath(String path) {
		return path.endsWith(".java");
	}

	// Bruno
	public static String getJavaFile(String path) {
		// int index = path.indexOf("src/");
		// if (index != -1) // just confirming that we are handling a src path
		// {
		StringTokenizer tokenizer = new StringTokenizer(path, "/");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token.contains(".java"))
				return token;
		}
		// }
		throw new RuntimeException("Error in getJavaFile");
		// return "";
	}

	// Bruno
	public static String getPackageFromPath(String path) {
//		String srcPackagePath = "";
		String convertedPackagePath = "";
//		int index = path.indexOf("src/");
//		if (index != -1) // just confirming that we are handling a src path
//		{
//			srcPackagePath = path.substring(index + 4);
			StringTokenizer tokenizer = new StringTokenizer(path, "/");
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				if (!token.contains(".java"))
					convertedPackagePath += token + ".";
			}
//		}
		return convertedPackagePath;
	}

	@Override
	public void connect(String url, String user, String password)
			throws SCMConnectionRefusedException {
		this.password = password;
		this.user = user;
		this.url = url;
		setupLibrary();
		setAuthentication(url, user, password);
	}

	public String[] getDirectoryList2() {
		listEntries("");
		int size = listDirs.size();
		String list[] = new String[size];
		for (int i = 0; i < size; i++) {
			list[i] = listDirs.get(i);
		}
		return list;
	}

	@Override
	public Node getDirectoryList() {

		final Node node = new Node("", "", -1);

		ProgressMonitorDialog dialog = new ProgressMonitorDialog(new Shell(
				Display.getCurrent()));
		try {
			dialog.run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					monitor.beginTask("Loading Repository Folders ...", 100);
					// execute the task ...

					listDirectories("", node, 1);
					int size = listDirs.size();
					String list[] = new String[size];
					/*
					 * for (int i = 0; i < size; i++) { list[i] =
					 * listDirs.get(i); }
					 */

					monitor.done();

				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return node;
	}

	@Override
	public LinkedList<RevisionItem> getListOfRevisions() {
		LinkedList<RevisionItem> revisionList = new LinkedList<RevisionItem>();
		try {
			// System.out.println("SubVersion.getListOfRevisions");
			long startRevision = 0;
			long endRevision = -1; // HEAD (the latest) revision

			Collection logEntries = null;

			logEntries = repository.log(new String[] { "" }, null,
					startRevision, endRevision, true, true);

			for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
				SVNLogEntry logEntry = (SVNLogEntry) entries.next();
				Author author = new Author(logEntry.getAuthor() == null ? ""
						: logEntry.getAuthor());
				revisionList.add(new RevisionItem(logEntry.getRevision(),
						author, logEntry.getDate(), logEntry.getMessage()));
			}

		} catch (SVNException e) {
			System.out.println(e.getMessage());
		}
		return revisionList;
	}

	public SCMHistoryData proccessHistory(int startRevision, int endRevision) {

		Collection logEntries = null;
		try {
			/*
			 * Collects SVNLogEntry objects for all revisions in the range
			 * defined by its start and end points [startRevision, endRevision].
			 * For each revision commit information is represented by
			 * SVNLogEntry.
			 * 
			 * the 1st parameter (targetPaths - an array of path strings) is set
			 * when restricting the [startRevision, endRevision] range to only
			 * those revisions when the paths in targetPaths were changed.
			 * 
			 * the 2nd parameter if non-null - is a user's Collection that will
			 * be filled up with found SVNLogEntry objects; it's just another
			 * way to reach the scope.
			 * 
			 * startRevision, endRevision - to define a range of revisions you
			 * are interested in; by default in this program - startRevision=0,
			 * endRevision= the latest (HEAD) revision of the repository.
			 * 
			 * the 5th parameter - a boolean flag changedPath - if true then for
			 * each revision a corresponding SVNLogEntry will contain a map of
			 * all paths which were changed in that revision.
			 * 
			 * the 6th parameter - a boolean flag strictNode - if false and a
			 * changed path is a copy (branch) of an existing one in the
			 * repository then the history for its origin will be traversed; it
			 * means the history of changes of the target URL (and all that
			 * there's in that URL) will include the history of the origin
			 * path(s). Otherwise if strictNode is true then the origin path
			 * history won't be included.
			 * 
			 * The return value is a Collection filled up with SVNLogEntry
			 * Objects.
			 */
			logEntries = repository.log(new String[] { "" }, null,
					startRevision, endRevision, true, true);

		} catch (SVNException svne) {
			System.out.println("error while collecting log information for '"
					+ url + "': " + svne.getMessage());
			System.exit(1);
		}
		SCMHistoryData scmdata = new SCMHistoryData();
		for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
			SVNLogEntry logEntry = (SVNLogEntry) entries.next();
			String authorName = logEntry.getAuthor();
			if (authorName == null)
				authorName = "";
			Author author;
			author = scmdata.getAuthors().get(authorName);
			if (author == null) {
				author = new Author(authorName);
				scmdata.getAuthors().put(authorName, author);
			}

			RevisionItem ri = new RevisionItem(logEntry.getRevision(), author,
					logEntry.getDate(), logEntry.getMessage());

			author.addRevisionItem(ri);
			scmdata.addGeneralRevisionItem(ri);

			/*
			 * displaying all paths that were changed in that revision; changed
			 * path information is represented by SVNLogEntryPath.
			 */
			if (logEntry.getChangedPaths().size() > 0) {
				/*
				 * keys are changed paths
				 */
				Set changedPathsSet = logEntry.getChangedPaths().keySet();

				for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths
						.hasNext();) {

					SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry
							.getChangedPaths().get(changedPaths.next());

					ChangedPath cp = new ChangedPath();
					switch (entryPath.getType()) {
					case 'A':
						cp.setTypeOfChange(TypeOfChange.ADDED);
						break;
					case 'D':
						cp.setTypeOfChange(TypeOfChange.DELETED);
						break;
					case 'M':
						cp.setTypeOfChange(TypeOfChange.MODIFIED);
						break;
					default:
						cp.setTypeOfChange(TypeOfChange.NONE);
					}
					/* Verify if this element was already changed */
					Element element = null;
					String path = entryPath.getPath();
					element = scmdata.getElements().get(path);
					if (element == null) {
						element = new Element();
						element.setName(path); /*
												 * TODO after change what store
												 * in name
												 */
						element.setPath(path);
						scmdata.getElements().put(path, element);
					}
					cp.setElement(element);

					if (cp.getTypeOfChange() != TypeOfChange.DELETED)
						cp.setElementProperty(getElementProperties(path,
								ri.getRevisionNumber()));
					else
						cp.setElementProperty(new ElementProperty()); // a blank
																		// element
																		// property

					ri.addChangedPaths(cp);
					element.addRevisionItem(ri);
					// element.setSize(entryPath.) TODO get size of the element
					/*
					 * + ((entryPath.getCopyPath() != null) ? " (from " +
					 * entryPath.getCopyPath() + " revision " +
					 * entryPath.getCopyRevision() + ")" : ""));
					 */
				}
			}
		}
		return scmdata;
	}

	private ElementProperty getElementProperties(String filePath, long revision) {

		ElementProperty ep = new ElementProperty();
		try {
			SVNProperties fileProperties = new SVNProperties();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			repository.getFile(filePath, revision, fileProperties, baos);
			ep.setSize(baos.size());
			// TODO to get other properties see printFileContent(String
			// filePath)
		} catch (SVNException e) {
			e.printStackTrace();
		}
		return ep;

	}

	@Override
	public void exportRevision(long revision, String localPath)
			throws Exception {
		// revision = -1;
		// try {
		/*
		 * Prepare filesystem directory (export destination).
		 */
		File exportDir = new File(localPath + "\\" + super.selectedDirectory
				+ "_" + revision);
		if (exportDir.exists()) {
			exportDir.delete();
			/*
			 * SVNErrorMessage err =
			 * SVNErrorMessage.create(SVNErrorCode.IO_ERROR,
			 * "Path ''{0}'' already exists", exportDir); throw new
			 * SVNException(err);
			 */
		}
		exportDir.mkdirs();

		/*
		 * Create an instance of SVNRepository class. This class is the main
		 * entry point for all "low-level" Subversion operations supported by
		 * Subversion protocol.
		 * 
		 * These operations includes browsing, update and commit operations. See
		 * SVNRepository methods javadoc for more details.
		 */
		SVNURL internalSVNUrl = SVNURL.parseURIDecoded(this.url
				+ this.selectedDirectory);// url+"\\Gia_eventos");
		repository = SVNRepositoryFactory.create(internalSVNUrl);

		/*
		 * User's authentication information (name/password) is provided via an
		 * ISVNAuthenticationManager instance. SVNWCUtil creates a default
		 * authentication manager given user's name and password.
		 * 
		 * Default authentication manager first attempts to use provided user
		 * name and password and then falls back to the credentials stored in
		 * the default Subversion credentials storage that is located in
		 * Subversion configuration area. If you'd like to use provided user
		 * name and password only you may use BasicAuthenticationManager class
		 * instead of default authentication manager:
		 * 
		 * authManager = new BasicAuthenticationsManager(userName,
		 * userPassword);
		 * 
		 * You may also skip this point - anonymous access will be used.
		 */
		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager(user, password);
		repository.setAuthenticationManager(authManager);

		/*
		 * Get type of the node located at URL we used to create SVNRepository.
		 * 
		 * "" (empty string) is path relative to that URL, -1 is value that may
		 * be used to specify HEAD (latest) revision.
		 */
		SVNNodeKind nodeKind = repository.checkPath("", -1);
		if (nodeKind == SVNNodeKind.NONE) {
			SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN,
					"No entry at URL ''{0}''", url);
			throw new SVNException(err);
		} else if (nodeKind == SVNNodeKind.FILE) {
			SVNErrorMessage err = SVNErrorMessage
					.create(SVNErrorCode.UNKNOWN,
							"Entry at URL ''{0}'' is a file while directory was expected",
							url);
			throw new SVNException(err);
		}

		/*
		 * Get latest repository revision. We will export repository contents at
		 * this very revision.
		 */
		// long latestRevision = repository.getLatestRevision();

		/*
		 * Create reporterBaton. This class is responsible for reporting 'wc
		 * state' to the server.
		 * 
		 * In this example it will always report that working copy is empty to
		 * receive update instructions that are sufficient to create complete
		 * directories hierarchy and get full files contents.
		 */
		ISVNReporterBaton reporterBaton = new ExportReporterBaton(revision);

		/*
		 * Create editor. This class will process update instructions received
		 * from the server and will create directories and files accordingly.
		 * 
		 * As we've reported 'emtpy working copy', server will only send
		 * 'addDir/addFile' instructions and will never ask our editor
		 * implementation to modify a file or directory properties.
		 */
		ISVNEditor exportEditor = new ExportEditor(exportDir);

		/*
		 * Now ask SVNKit to perform generic 'update' operation using our
		 * reporter and editor.
		 * 
		 * We are passing:
		 * 
		 * - revision from which we would like to export - null as "target"
		 * name, to perform export from the URL SVNRepository was created for,
		 * not from some child directory. - reporterBaton - exportEditor.
		 */
		repository.update(revision, null, true, reporterBaton, exportEditor);

		System.out.println("Exported revision: " + revision);
		/*
		 * } catch (SVNException e) { throw e; }
		 */

	}

	@Override
	public void exportPath(Node node, String localPath, long initialRevision,
			long finalRevision) throws Exception {

		String internalDirectory = node.getPath()
				+ (node.getPath().endsWith("/") ? "" : "/") + node.getName();
		SVNURL internalSVNUrl = SVNURL.parseURIDecoded(this.url
				+ internalDirectory);
		repository = SVNRepositoryFactory.create(internalSVNUrl);

		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager(user, password);
		repository.setAuthenticationManager(authManager);

		/*
		 * Get type of the node located at URL we used to create SVNRepository.
		 * 
		 * "" (empty string) is path relative to that URL, -1 is value that may
		 * be used to specify HEAD (latest) revision.
		 */
		SVNNodeKind nodeKind = repository.checkPath(internalDirectory, -1);
		if (nodeKind == SVNNodeKind.NONE) {
			SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN,
					"No entry at URL ''{0}''", url);
			throw new SVNException(err);
		} else if (nodeKind == SVNNodeKind.FILE) {
			SVNErrorMessage err = SVNErrorMessage
					.create(SVNErrorCode.UNKNOWN,
							"Entry at URL ''{0}'' is a file while directory was expected",
							url);
			// throw new SVNException(err);
		}

		// Loop pra pegar de revisao pra revisao o que mudou, verificar onde foi
		// salvo
		// esses valores pra fazer a diferen�a com o diffj

		/*
		 * Get latest repository revision. We will export repository contents at
		 * this very revision.
		 */

		Collection logEntries = null;

		logEntries = repository.log(new String[] { "" }, null, initialRevision,
				finalRevision, true, true);

		for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
			SVNLogEntry logEntry = (SVNLogEntry) entries.next();
			System.out.println("---------------------------------------------");
			System.out.println("revision: " + logEntry.getRevision());
			System.out.println("author: " + logEntry.getAuthor());
			System.out.println("date: " + logEntry.getDate());
			System.out.println("log message: " + logEntry.getMessage());

			List<String> listaPacoteAlterados = new ArrayList<String>();
			List<String> listaPacoteRemovido = new ArrayList<String>();

			// Seta o diretorio que vai fazer o download
			String folderName = node.getName();

			if (logEntry.getChangedPaths().size() > 0) {
				System.out.println();
				System.out.println("changed paths:");
				Set changedPathsSet = logEntry.getChangedPaths().keySet();

				for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths
						.hasNext();) {
					SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry
							.getChangedPaths().get(changedPaths.next());
					System.out.println(" "
							+ entryPath.getType()
							+ " "
							+ entryPath.getPath()
							+ ((entryPath.getCopyPath() != null) ? " (from "
									+ entryPath.getCopyPath() + " revision "
									+ entryPath.getCopyRevision() + ")" : ""));
					/*
					 * Bruno TO DO: At this implementation, the code only
					 * considers the full path to identify a class. So if a
					 * class moves to another package, this code identifies the
					 * class as a different one, because the call to the method
					 * containsKey is used by passing the full path.
					 */

					if (entryPath.getPath() != null
							&& isJavaSourcePath(entryPath.getPath())) {

						if (entryPath.getType() == 'D') {
							boolean existeAlteracaoPacote = false;

							for (SVNLogEntryPath object : (Collection<SVNLogEntryPath>) logEntry
									.getChangedPaths().values()) {
								if (entryPath.getPath().equals(
										object.getCopyPath())) {
									existeAlteracaoPacote = true;
								}
							}

							if (existeAlteracaoPacote) {
								continue;
							}
						}

						getRevision(localPath, logEntry.getRevision(),
								folderName, entryPath.getPath());

						String javaFile = getJavaFile(entryPath.getPath());
						String packageFromPath = getPackageFromPath(entryPath
								.getPath());
						String nomClasseNova = packageFromPath + javaFile;

						String pathAtual = new File(localPath + "\\"
								+ logEntry.getRevision() + entryPath.getPath())
								.toString();

						String pathAntiga = "";

						if (entryPath.getCopyPath() == null) {
							getRevision(localPath, logEntry.getRevision() - 1,
									folderName, entryPath.getPath());

							pathAntiga = new File(localPath + "\\"
									+ (logEntry.getRevision() - 1)
									+ entryPath.getPath()).toString();
						} else {
							getRevision(localPath, logEntry.getRevision() - 1,
									folderName, entryPath.getCopyPath());

							pathAntiga = new File(localPath + "\\"
									+ (logEntry.getRevision() - 1)
									+ entryPath.getCopyPath()).toString();

							listaPacoteAlterados.add(entryPath.getCopyPath());

							String nomClasseAntiga = getPackageFromPath(entryPath
									.getCopyPath())
									+ getJavaFile(entryPath.getCopyPath());

							RelatorioFinal.AlterarPacote(nomClasseAntiga,
									nomClasseNova);
						}

						Metrica metricaFilter = new Metrica();

						metricaFilter
								.setUrlProjetoAntiga(getNomeVersaoNova(entryPath));
						metricaFilter
								.setUrlProjetoAtual(getNomeVersaoNova(entryPath));
						metricaFilter.setNumeroRevisaoInicial((int) (logEntry
								.getRevision() - 1));
						metricaFilter.setNumeroRevisaoFinal((int) logEntry
								.getRevision());

						if (isMetricInDatabase(metricaFilter)) {
							
							Metrica metrica = new Metrica();
							
							metrica = Metrica.SelectByFilter(metricaFilter).get(0);
							
							Relatorio.setMudancaClasse(converterMetricaParaMudancaClasse(metrica));
							
							System.out.println("Pegou m�trica do banco !");							
						} else {
							
							System.out.println("Pegou m�trica do diffj !");
							String[] args = { pathAntiga, pathAtual };

							DiffJ diffj = new DiffJ(args);

							Relatorio.getMudancaClasse().setNomClasse(
									packageFromPath + javaFile);

							// Inserir Metricas no banco para posterior consulta
							Metrica metrica = new Metrica();

							metrica.setUrlProjetoAntiga(getNomeVersaoNova(entryPath));
							metrica.setUrlProjetoAtual(getNomeVersaoNova(entryPath));
							metrica.setNumeroRevisaoInicial((int) logEntry
									.getRevision() - 1);
							metrica.setNumeroRevisaoFinal((int) logEntry
									.getRevision());
							metrica.setMudancasClasse(Relatorio
									.getMudancaClasse()
									.calcularMudancasClasse());
							metrica.setMudancasMetodos(Relatorio
									.getMudancaClasse()
									.calcularMudancasMetodo());
							metrica.setMudancasAtributos(Relatorio
									.getMudancaClasse()
									.calcularMudancasAtributo());
							metrica.setCodeChurn(Relatorio.getMudancaClasse()
									.obterCodeChurn());
							metrica.setLinhasCodigo(Relatorio.getMudancaClasse()
									.getNumeroLinesOfCode());

							Metrica.Insert(metrica);
							
						}
						
						RelatorioFinal.preencherHashMap(Relatorio
								.getMudancaClasse());

						Relatorio.zerarVariaveis();
					}
				}
			}
		}

		/*
		 * } catch (SVNException e) { throw e; }
		 */

	}

	public void exportPath(Node node, String localPath, long initialRevision,
			long finalRevision, IProgressMonitor monitor) throws Exception {

		String internalDirectory = node.getPath()
				+ (node.getPath().endsWith("/") ? "" : "/") + node.getName();
		SVNURL internalSVNUrl = SVNURL.parseURIDecoded(this.url
				+ internalDirectory);
		repository = SVNRepositoryFactory.create(internalSVNUrl);

		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager(user, password);
		repository.setAuthenticationManager(authManager);

		/*
		 * Get type of the node located at URL we used to create SVNRepository.
		 * 
		 * "" (empty string) is path relative to that URL, -1 is value that may
		 * be used to specify HEAD (latest) revision.
		 */
		SVNNodeKind nodeKind = repository.checkPath(internalDirectory, -1);
		if (nodeKind == SVNNodeKind.NONE) {
			SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN,
					"No entry at URL ''{0}''", url);
			throw new SVNException(err);
		} else if (nodeKind == SVNNodeKind.FILE) {
			SVNErrorMessage err = SVNErrorMessage
					.create(SVNErrorCode.UNKNOWN,
							"Entry at URL ''{0}'' is a file while directory was expected",
							url);
			// throw new SVNException(err);
		}

		// Loop pra pegar de revisao pra revisao o que mudou, verificar onde foi
		// salvo
		// esses valores pra fazer a diferen�a com o diffj

		/*
		 * Get latest repository revision. We will export repository contents at
		 * this very revision.
		 */

		Collection logEntries = null;

		logEntries = repository.log(new String[] { "" }, null, initialRevision,
				finalRevision, true, true);

		monitor.beginTask("Executing the diffj", logEntries.size());
		for (Iterator entries = logEntries.iterator(); entries.hasNext();) {

			SVNLogEntry logEntry = (SVNLogEntry) entries.next();
			System.out.println("---------------------------------------------");
			System.out.println("revision: " + logEntry.getRevision());
			System.out.println("author: " + logEntry.getAuthor());
			System.out.println("date: " + logEntry.getDate());
			System.out.println("log message: " + logEntry.getMessage());

			List<String> listaPacoteAlterados = new ArrayList<String>();
			List<String> listaPacoteRemovido = new ArrayList<String>();

			// Seta o diretorio que vai fazer o download
			String folderName = node.getName();

			if (logEntry.getChangedPaths().size() > 0) {
				System.out.println();
				System.out.println("changed paths:");
				Set changedPathsSet = logEntry.getChangedPaths().keySet();

				for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths
						.hasNext();) {
					SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry
							.getChangedPaths().get(changedPaths.next());
					System.out.println(" "
							+ entryPath.getType()
							+ " "
							+ entryPath.getPath()
							+ ((entryPath.getCopyPath() != null) ? " (from "
									+ entryPath.getCopyPath() + " revision "
									+ entryPath.getCopyRevision() + ")" : ""));
					/*
					 * Bruno TO DO: At this implementation, the code only
					 * considers the full path to identify a class. So if a
					 * class moves to another package, this code identifies the
					 * class as a different one, because the call to the method
					 * containsKey is used by passing the full path.
					 */

					if (entryPath.getPath() != null
							&& isJavaSourcePath(entryPath.getPath())) {

						if (entryPath.getType() == 'D') {
							boolean existeAlteracaoPacote = false;

							for (SVNLogEntryPath object : (Collection<SVNLogEntryPath>) logEntry
									.getChangedPaths().values()) {
								if (entryPath.getPath().equals(
										object.getCopyPath())) {
									existeAlteracaoPacote = true;
								}
							}

							if (existeAlteracaoPacote) {
								continue;
							}
						}

						getRevision(localPath, logEntry.getRevision(),
								folderName, entryPath.getPath());

						String pathAtual = new File(localPath + "\\"
								+ logEntry.getRevision() + entryPath.getPath())
								.toString();

						String pathAntiga = "";

						if (entryPath.getCopyPath() == null) {
							getRevision(localPath, logEntry.getRevision() - 1,
									folderName, entryPath.getPath());

							pathAntiga = new File(localPath + "\\"
									+ (logEntry.getRevision() - 1)
									+ entryPath.getPath()).toString();
						} else {
							getRevision(localPath, logEntry.getRevision() - 1,
									folderName, entryPath.getCopyPath());

							pathAntiga = new File(localPath + "\\"
									+ (logEntry.getRevision() - 1)
									+ entryPath.getCopyPath()).toString();

							listaPacoteAlterados.add(entryPath.getCopyPath());

							String nomClasseAntiga = getNomeVersaoAntiga(entryPath);

							RelatorioFinal.AlterarPacote(nomClasseAntiga,
									getNomeVersaoNova(entryPath));
						}

						Metrica metricaFilter = new Metrica();

						metricaFilter
								.setUrlProjetoAntiga(getNomeVersaoNova(entryPath));
						metricaFilter
								.setUrlProjetoAtual(getNomeVersaoNova(entryPath));
						metricaFilter.setNumeroRevisaoInicial((int) (logEntry
								.getRevision() - 1));
						metricaFilter.setNumeroRevisaoFinal((int) logEntry
								.getRevision());

						if (isMetricInDatabase(metricaFilter)) {
							
							Metrica metrica = new Metrica();
							
							metrica = Metrica.SelectByFilter(metricaFilter).get(0);
							
							Relatorio.setMudancaClasse(converterMetricaParaMudancaClasse(metrica));
							
							System.out.println("Pegou m�trica do banco !");							
						} else {
							
							System.out.println("Pegou m�trica do diffj !");
							String[] args = { pathAntiga, pathAtual };

							DiffJ diffj = new DiffJ(args);
							Relatorio.getMudancaClasse().setNomClasse(getNomeVersaoNova(entryPath));

							// Inserir Metricas no banco para posterior consulta
							Metrica metrica = new Metrica();

							metrica.setUrlProjetoAntiga(getNomeVersaoNova(entryPath));
							metrica.setUrlProjetoAtual(getNomeVersaoNova(entryPath));
							metrica.setNumeroRevisaoInicial((int) logEntry
									.getRevision() - 1);
							metrica.setNumeroRevisaoFinal((int) logEntry
									.getRevision());
							metrica.setMudancasClasse(Relatorio
									.getMudancaClasse()
									.calcularMudancasClasse());
							metrica.setMudancasMetodos(Relatorio
									.getMudancaClasse()
									.calcularMudancasMetodo());
							metrica.setMudancasAtributos(Relatorio
									.getMudancaClasse()
									.calcularMudancasAtributo());
							metrica.setCodeChurn(Relatorio.getMudancaClasse()
									.obterCodeChurn());
							metrica.setLinhasCodigo(Relatorio.getMudancaClasse()
									.getNumeroLinesOfCode());

							Metrica.Insert(metrica);
							
						}
						
						RelatorioFinal.preencherHashMap(Relatorio
								.getMudancaClasse());

						Relatorio.zerarVariaveis();

					}
				}
			}
			monitor.worked(1);
		}

		/*
		 * } catch (SVNException e) { throw e; }
		 */

	}

	private String getNomeVersaoAntiga(SVNLogEntryPath entryPath) {
		String javaFile = getJavaFile(entryPath.getCopyPath());
		String packageFromPath = getPackageFromPath(entryPath.getCopyPath());
		return packageFromPath + javaFile;
	}

	private String getNomeVersaoNova(SVNLogEntryPath entryPath) {
		String javaFile = getJavaFile(entryPath.getPath());
		String packageFromPath = getPackageFromPath(entryPath.getPath());
		return packageFromPath + javaFile;
	}

	private void getRevision(String localPath, long numRevisao,
			String folderName, String path) throws SVNException, IOException {

		SVNProperties properties = new SVNProperties();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {

			repository.getFile(path, numRevisao, properties, baos);

			// Cria Arquivo
			String fileName = localPath + "\\" + numRevisao + path;

			File file = new File(fileName);
			File parent = file.getParentFile();

			if (file.exists()) {
				deleteDir(file);
			}

			if (!parent.exists()) {
				parent.mkdirs();
			}

			FileWriter fstream = new FileWriter(fileName);

			BufferedWriter buf = new BufferedWriter(fstream);

			buf.write(baos.toString());
			buf.close();
		} catch (Exception ex) {

		}

		System.out.println("Exported revision: " + numRevisao);
	}

	private void getRevision(String localPath, long numRevisao,
			String folderName) throws SVNException {
		File exportDir = new File(localPath + "\\" + numRevisao + "\\"
				+ folderName);
		if (exportDir.exists()) {
			deleteDir(exportDir);
		}
		exportDir.mkdirs();

		/*
		 * Create reporterBaton. This class is responsible for reporting 'wc
		 * state' to the server.
		 * 
		 * In this example it will always report that working copy is empty to
		 * receive update instructions that are sufficient to create complete
		 * directories hierarchy and get full files contents.
		 */
		ISVNReporterBaton reporterBaton = new ExportReporterBaton(numRevisao);

		/*
		 * Create editor. This class will process update instructions received
		 * from the server and will create directories and files accordingly.
		 * 
		 * As we've reported 'emtpy working copy', server will only send
		 * 'addDir/addFile' instructions and will never ask our editor
		 * implementation to modify a file or directory properties.
		 */
		ISVNEditor exportEditor = new ExportEditor(exportDir);

		/*
		 * Now ask SVNKit to perform generic 'update' operation using our
		 * reporter and editor.
		 * 
		 * We are passing:
		 * 
		 * - revision from which we would like to export - null as "target"
		 * name, to perform export from the URL SVNRepository was created for,
		 * not from some child directory. - reporterBaton - exportEditor.
		 */
		repository.update(numRevisao, null, true, reporterBaton, exportEditor);

		System.out.println("Exported revision: " + numRevisao);
	}

	private void listDirectories(String path, Node root, int depth) {
		try {

			if (depth > 3)
				return;

			Collection entries = repository.getDir(path, -1, null,
					(Collection) null);

			Iterator iterator = entries.iterator();

			// Ortins Ordenar Lista de Diretorios

			Collections.sort((List<SVNDirEntry>) entries, new Comparator() {

				@Override
				public int compare(Object o1, Object o2) {
					// TODO Auto-generated method stub
					SVNDirEntry dir1 = (SVNDirEntry) o1;
					SVNDirEntry dir2 = (SVNDirEntry) o2;

					return dir1.getName().compareTo(dir2.getName());
				}
			});

			while (iterator.hasNext()) {
				SVNDirEntry entry = (SVNDirEntry) iterator.next();

				/*
				 * System.out.println("/" + (path.equals("") ? "" : path + "/")
				 * + entry.getName() + " ( author: '" + entry.getAuthor() +
				 * "'; revision: " + entry.getRevision() + "; date: " +
				 * entry.getDate() + "; URL: " + entry.getURL() + "; size: " +
				 * entry.getSize() + " bytes)");
				 */
				/*
				 * Mandar imprimir o conte�do do arquivo if ( entry.getKind()
				 * == SVNNodeKind.FILE ){ printFileContent(path.equals( "" ) ?
				 * entry.getName( ) : path + "/" + entry.getName( ) );
				 * 
				 * }
				 */
				if (entry.getKind() == SVNNodeKind.DIR
						|| entry.getKind() == SVNNodeKind.FILE) {
					// System.out.println("/" + (path.equals("") ? "" : path +
					// "/")+ entry.getName());
					String pathName = "/" + (path.equals("") ? "" : path);
					Node currentNode = new Node(entry.getName(), pathName,
							entry.getRevision());
					root.addChildren(currentNode);
					/*
					 * listDirectories.add("/" + (path.equals("") ? "" : path +
					 * "/") + entry.getName());
					 */
					if (entry.getName().contains("archiva") || entry.getName().contains("branches"))
					{
					 listDirectories((path.equals("")) ? entry.getName() :
					 path
					 + "/" + entry.getName(), currentNode, depth + 1);
					}
				}
			}

		} catch (SVNException e) {
			System.out.println(e.getMessage());
		}
	}

	private void listEntries(String path) {
		try {

			Collection entries = repository.getDir(path, -1, null,
					(Collection) null);
			Iterator iterator = entries.iterator();

			while (iterator.hasNext()) {
				SVNDirEntry entry = (SVNDirEntry) iterator.next();

				/*
				 * System.out.println("/" + (path.equals("") ? "" : path + "/")
				 * + entry.getName() + " ( author: '" + entry.getAuthor() +
				 * "'; revision: " + entry.getRevision() + "; date: " +
				 * entry.getDate() + "; URL: " + entry.getURL() + "; size: " +
				 * entry.getSize() + " bytes)");
				 * 
				 * Mandar imprimir o conte�do do arquivo if ( entry.getKind()
				 * == SVNNodeKind.FILE ){ printFileContent(path.equals( "" ) ?
				 * entry.getName( ) : path + "/" + entry.getName( ) );
				 * 
				 * }
				 */
				if (entry.getKind() == SVNNodeKind.DIR && path.equals("")) {
					// System.out.println("/" + (path.equals("") ? "" : path +
					// "/")+ entry.getName());
					listDirs.add("/" + (path.equals("") ? "" : path + "/")
							+ entry.getName());
					listEntries((path.equals("")) ? entry.getName() : path
							+ "/" + entry.getName());
				}
			}

		} catch (SVNException e) {
			System.out.println(e.getMessage());
		}

	}

	private void setupLibrary() {
		/*
		 * For using over http:// and https://
		 */
		DAVRepositoryFactory.setup();
		/*
		 * For using over svn:// and svn+xxx://
		 */
		SVNRepositoryFactoryImpl.setup();

		/*
		 * For using over file:///
		 */
		FSRepositoryFactory.setup();
	}

	private void setAuthentication(String url, String user, String password)
			throws SCMConnectionRefusedException {

		try {
			/*
			 * Creates an instance of SVNRepository to work with the repository.
			 */
			svnUrl = SVNURL.parseURIDecoded(url);
			repository = SVNRepositoryFactory.create(svnUrl, null);

		} catch (SVNException svne) {
			System.err
					.println("Error while creating an SVNRepository for the location '"
							+ svnUrl + "': " + svne.getMessage());
			throw new SCMConnectionRefusedException(svne.getMessage());
		}

		try {

			// set an auth manager which will provide user credentials
			ISVNAuthenticationManager authManager = SVNWCUtil
					.createDefaultAuthenticationManager(user, password);
			repository.setAuthenticationManager(authManager);
			repository.testConnection();
		} catch (Exception e) {
			throw new SCMConnectionRefusedException(e.getMessage());
		}

	}

	/*
	 * ReporterBaton implementation that always reports 'empty wc' state.
	 */
	private static class ExportReporterBaton implements ISVNReporterBaton {

		private long exportRevision;

		public ExportReporterBaton(long revision) {
			exportRevision = revision;
		}

		public void report(ISVNReporter reporter) throws SVNException {
			try {
				/*
				 * Here empty working copy is reported.
				 * 
				 * ISVNReporter includes methods that allows to report mixed-rev
				 * working copy and even let server know that some files or
				 * directories are locally missing or locked.
				 */
				reporter.setPath("", null, exportRevision, SVNDepth.INFINITY,
						true);

				/*
				 * Don't forget to finish the report!
				 */
				reporter.finishReport();
			} catch (SVNException svne) {
				reporter.abortReport();
				System.out.println("Report failed.");
			}
		}
	}

	/*
	 * ISVNEditor implementation that will add directories and files into the
	 * target directory accordingly to update instructions sent by the server.
	 */
	private static class ExportEditor implements ISVNEditor {

		private File myRootDirectory;
		private SVNDeltaProcessor myDeltaProcessor;

		/*
		 * root - the local directory where the node tree is to be exported
		 * into.
		 */
		public ExportEditor(File root) {
			myRootDirectory = root;
			/*
			 * Utility class that will help us to transform 'deltas' sent by the
			 * server to the new file contents.
			 */
			myDeltaProcessor = new SVNDeltaProcessor();
		}

		/*
		 * Server reports revision to which application of the further
		 * instructions will update working copy to.
		 */
		public void targetRevision(long revision) throws SVNException {
		}

		/*
		 * Called before sending other instructions.
		 */
		public void openRoot(long revision) throws SVNException {
		}

		/*
		 * Called when a new directory has to be added.
		 * 
		 * For each 'addDir' call server will call 'closeDir' method after all
		 * children of the added directory are added.
		 * 
		 * This implementation creates corresponding directory below root
		 * directory.
		 */
		public void addDir(String path, String copyFromPath,
				long copyFromRevision) throws SVNException {
			File newDir = new File(myRootDirectory, path);
			if (!newDir.exists()) {
				if (!newDir.mkdirs()) {
					SVNErrorMessage err = SVNErrorMessage.create(
							SVNErrorCode.IO_ERROR,
							"error: failed to add the directory ''{0}''.",
							newDir);
					throw new SVNException(err);
				}
			}
			System.out.println("dir added: " + path);
		}

		/*
		 * Called when there is an existing directory that has to be 'opened'
		 * either to modify this directory properties or to process other files
		 * and directories inside this directory.
		 * 
		 * In case of export this method will never be called because we
		 * reported that our 'working copy' is empty and so server knows that
		 * there are no 'existing' directories.
		 */
		public void openDir(String path, long revision) throws SVNException {
		}

		/*
		 * Instructs to change opened or added directory property.
		 * 
		 * This method is called to update properties set by the user as well as
		 * those created automatically, like "svn:committed-rev". See
		 * SVNProperty class for default property names.
		 * 
		 * When property has to be deleted value will be 'null'.
		 */

		public void changeDirProperty(String name, SVNPropertyValue property)
				throws SVNException {
		}

		/*
		 * Called when a new file has to be created.
		 * 
		 * For each 'addFile' call server will call 'closeFile' method after
		 * sending file properties and contents.
		 * 
		 * This implementation creates empty file below root directory, file
		 * contents will be updated later, and for empty files may not be sent
		 * at all.
		 */
		public void addFile(String path, String copyFromPath,
				long copyFromRevision) throws SVNException {
			File file = new File(myRootDirectory, path);
			if (file.exists()) {
				SVNErrorMessage err = SVNErrorMessage.create(
						SVNErrorCode.IO_ERROR,
						"error: exported file ''{0}'' already exists!", file);
				throw new SVNException(err);
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				SVNErrorMessage err = SVNErrorMessage.create(
						SVNErrorCode.IO_ERROR,
						"error: cannot create new  file ''{0}''", file);
				throw new SVNException(err);
			}
		}

		/*
		 * Called when there is an existing files that has to be 'opened' either
		 * to modify file contents or properties.
		 * 
		 * In case of export this method will never be called because we
		 * reported that our 'working copy' is empty and so server knows that
		 * there are no 'existing' files.
		 */
		public void openFile(String path, long revision) throws SVNException {
		}

		/*
		 * Instructs to add, modify or delete file property. In this example we
		 * skip this instruction, but 'real' export operation may inspect
		 * 'svn:eol-style' or 'svn:mime-type' property values to transfor file
		 * contents propertly after receiving.
		 */

		public void changeFileProperty(String path, String name,
				SVNPropertyValue property) throws SVNException {
		}

		/*
		 * Called before sending 'delta' for a file. Delta may include
		 * instructions on how to create a file or how to modify existing file.
		 * In this example delta will always contain instructions on how to
		 * create a new file and so we set up deltaProcessor with 'null' base
		 * file and target file to which we would like to store the result of
		 * delta application.
		 */
		public void applyTextDelta(String path, String baseChecksum)
				throws SVNException {
			myDeltaProcessor.applyTextDelta((File) null, new File(
					myRootDirectory, path), false);
		}

		/*
		 * Server sends deltas in form of 'diff windows'. Depending on the file
		 * size there may be several diff windows. Utility class
		 * SVNDeltaProcessor processes these windows for us.
		 */
		public OutputStream textDeltaChunk(String path, SVNDiffWindow diffWindow)
				throws SVNException {
			return myDeltaProcessor.textDeltaChunk(diffWindow);
		}

		/*
		 * Called when all diff windows (delta) is transferred.
		 */
		public void textDeltaEnd(String path) throws SVNException {
			myDeltaProcessor.textDeltaEnd();
		}

		/*
		 * Called when file update is completed. This call always matches
		 * addFile or openFile call.
		 */
		public void closeFile(String path, String textChecksum)
				throws SVNException {
			System.out.println("file added: " + path);
		}

		/*
		 * Called when all child files and directories are processed. This call
		 * always matches addDir, openDir or openRoot call.
		 */
		public void closeDir() throws SVNException {
		}

		/*
		 * Insturcts to delete an entry in the 'working copy'. Of course will
		 * not be called during export operation.
		 */
		public void deleteEntry(String path, long revision) throws SVNException {
		}

		/*
		 * Called when directory at 'path' should be somehow processed, but
		 * authenticated user (or anonymous user) doesn't have enough access
		 * rights to get information on this directory (properties, children).
		 */
		public void absentDir(String path) throws SVNException {
		}

		/*
		 * Called when file at 'path' should be somehow processed, but
		 * authenticated user (or anonymous user) doesn't have enough access
		 * rights to get information on this file (contents, properties).
		 */
		public void absentFile(String path) throws SVNException {
		}

		/*
		 * Called when update is completed.
		 */
		public SVNCommitInfo closeEdit() throws SVNException {
			return null;
		}

		/*
		 * Called when update is completed with an error or server requests
		 * client to abort update operation.
		 */
		public void abortEdit() throws SVNException {
		}

	}

	@Override
	public long getLastRevisionNumber() {
		// TODO Auto-generated method stub
		if (repository != null) {
			try {
				return repository.getLatestRevision();
			} catch (SVNException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
		} else {
			return 0;
		}
	}

	public void exportPath(Node node, String localPath) throws Exception {

		String internalDirectory = node.getPath()
				+ (node.getPath().endsWith("/") ? "" : "/") + node.getName();
		SVNURL internalSVNUrl = SVNURL.parseURIDecoded(this.url
				+ internalDirectory);
		repository = SVNRepositoryFactory.create(internalSVNUrl);

		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager(user, password);
		repository.setAuthenticationManager(authManager);

		/*
		 * Get type of the node located at URL we used to create SVNRepository.
		 * 
		 * "" (empty string) is path relative to that URL, -1 is value that may
		 * be used to specify HEAD (latest) revision.
		 */
		SVNNodeKind nodeKind = repository.checkPath(internalDirectory, -1);
		if (nodeKind == SVNNodeKind.NONE) {
			SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN,
					"No entry at URL ''{0}''", url);
			throw new SVNException(err);
		} else if (nodeKind == SVNNodeKind.FILE) {
			SVNErrorMessage err = SVNErrorMessage
					.create(SVNErrorCode.UNKNOWN,
							"Entry at URL ''{0}'' is a file while directory was expected",
							url);
			throw new SVNException(err);
		}

		// Loop pra pegar de revisao pra revisao o que mudou, verificar onde foi
		// salvo
		// esses valores pra fazer a diferen�a com o diffj

		/*
		 * Get latest repository revision. We will export repository contents at
		 * this very revision.
		 */

		Collection logEntries = null;

		logEntries = repository.log(new String[] { "" }, null, 0,
				repository.getLatestRevision(), true, true);

		for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
			SVNLogEntry logEntry = (SVNLogEntry) entries.next();
			System.out.println("---------------------------------------------");
			System.out.println("revision: " + logEntry.getRevision());
			System.out.println("author: " + logEntry.getAuthor());
			System.out.println("date: " + logEntry.getDate());
			System.out.println("log message: " + logEntry.getMessage());

			// Seta o diretorio que vai fazer o download
			String folderName = node.getName();

			if (logEntry.getChangedPaths().size() > 0) {
				System.out.println();
				System.out.println("changed paths:");
				Set changedPathsSet = logEntry.getChangedPaths().keySet();

				for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths
						.hasNext();) {
					SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry
							.getChangedPaths().get(changedPaths.next());
					System.out.println(" "
							+ entryPath.getType()
							+ " "
							+ entryPath.getPath()
							+ ((entryPath.getCopyPath() != null) ? " (from "
									+ entryPath.getCopyPath() + " revision "
									+ entryPath.getCopyRevision() + ")" : ""));
					/*
					 * Bruno TO DO: At this implementation, the code only
					 * considers the full path to identify a class. So if a
					 * class moves to another package, this code identifies the
					 * class as a different one, because the call to the method
					 * containsKey is used by passing the full path.
					 */

					if (entryPath.getPath() != null
							&& isJavaSourcePath(entryPath.getPath())) {

						getRevision(localPath, logEntry.getRevision(),
								folderName, entryPath.getPath());

						getRevision(localPath, logEntry.getRevision() - 1,
								folderName, entryPath.getPath());

						String javaFile = getJavaFile(entryPath.getPath());
						String packageFromPath = getPackageFromPath(entryPath
								.getPath());

						String pathAtual = new File(localPath + "\\"
								+ logEntry.getRevision() + entryPath.getPath())
								.toString();
						String pathAntiga = new File(localPath + "\\"
								+ (logEntry.getRevision() - 1)
								+ entryPath.getPath()).toString();

						String[] args = { pathAntiga, pathAtual };

						DiffJ diffj = new DiffJ(args);

					}
				}
			}
		}
	}

	public boolean isMetricInDatabase(Metrica metricaFilter) {
		// TODO Auto-generated method stub
		List<Metrica> metricList = Metrica.SelectByFilter(metricaFilter);

		if (metricList.size() > 0) {
			return true;
		}

		return false;
	}

	public MudancaClasseVO converterMetricaParaMudancaClasse(Metrica metrica) {
		// TODO Auto-generated method stub
		MudancaClasseVO mudanca = new MudancaClasseVO();

		mudanca.setNomClasse(metrica.getUrlProjetoAtual());
		mudanca.setNumeroMudancasAtributo(metrica.getMudancasAtributos());
		mudanca.setNumeroMudancasClasse(metrica.getMudancasClasse());
		mudanca.setNumeroMudancasCodeChurn(metrica.getCodeChurn());
		mudanca.setNumeroLinesOfCode(metrica.getLinhasCodigo());
		
		for (int i = 0; i < metrica.getMudancasMetodos(); i++) {
			mudanca.incrementarMudancasMetodo(new MudancaMetodo());
		}

		return mudanca;
	}
}