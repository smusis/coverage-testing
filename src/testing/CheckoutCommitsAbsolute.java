package testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;

public class CheckoutCommitsAbsolute {
	public static void main(String[] args) throws IOException,
	InterruptedException {

		String project = "httpclient", version = "Older", bugId = "";
		// findPackageNames(project,version);
		// copyClasses();

		checkCommits();

		// copyJars(project);

		// instrumentCobertura(project, version);
		// copyJavaFiles(project);

		RunRandoop randoop = new RunRandoop();
		// randoop.commentNonDetTests(project, version);
		// randoop.getFailedTests(project);
		// randoop.runTestCasesOlderCobertura(project, version,bugId);

	}

	// Change all URLs to run on server

	// Read OldNewCommits file & call other functions
	public static void checkCommits() throws IOException, InterruptedException {
		String line = "";
		// final BufferedReader br = new BufferedReader(new
		// FileReader("E:\\Research Projects\\ICSM 2014 project\\Data\\OldNewCommits.csv"));
		final BufferedReader br = new BufferedReader(new FileReader(
				"E:/Research Projects/ICSM 2014 project/Top100bugIDs.txt"));
		while ((line = br.readLine()) != null) {
			String[] split = line.split(",");
			String newer = split[0];
			String older = split[1];
			String bugId = split[2];
			String project = split[3];

			System.out.println(bugId);

			//if(!bugId.equals("JCR-2298")){ continue; }

			if (!project.equals("lucene-solr")) {
				continue;
			}

			boolean filePresent=false;
			File file=new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/CoberturaData/GeneratedTestCases");
			List<File> fileList = Arrays.asList(file.listFiles());
			for (int i = 0; i < fileList.size(); i++) {
				//System.out.println(fileList.get(i).toString());
				String[] splitName=fileList.get(i).toString().split("/");
				//System.out.println(splitName[splitName.length-1].replaceAll(".txt","") + "  "+older+"_"+bugId);
				if(splitName[splitName.length-1].replaceAll(".txt","").equals(older+"_"+bugId)){
					filePresent=true;
					//System.out.println(filePresent);
				}
			}
			if(filePresent){
				continue;
			}

			//deleteOldFolders(project);
			// New Version
			/*checkoutCommit(newer, project);
			findPackageNames(project, "Newer");
			copyJars(project);*/

			RunRandoopAbsolute randoop = new RunRandoopAbsolute();
			randoop.generateTestCases(project, "Newer");
			System.exit(1);
			randoop.compileTestCases(project, "Newer");
			randoop.runTestCasesNewer(project, "Newer");

			// Old version
			checkoutCommit(older,project); 
			findPackageNames(project,"Older");

			randoop.commentNonDetTests(project, "Older",bugId);
			randoop.compileTestCases(project,"Older");
			instrumentCobertura(project, "Older");
			randoop.runTestCasesOlder(project, "Older",bugId,older);
			randoop.getFailedTests(project,bugId,older);

			copyAllData(project, bugId, older);

			//if(bugId.equals("HTTPCLIENT-853")){ System.exit(1); }

			System.exit(1);
		}
		br.close();
	}

	//Delete all previous folders
	public static void deleteOldFolders(String project) throws IOException{
		String[] folders={"CoberturaCoverage","CoberturaInstrumented","RandoopClassesNewer",
				"RandoopClassesOlder","RandoopTests","RandoopTestsCommented","RandoopOutput"};
		for(int i=0;i<folders.length;i++){
			//System.out.println("yes");
			File theDir=new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/"+folders[i]);
			FileUtils.deleteDirectory(theDir);
		}
	}

	// git checkout the commit
	public static void checkoutCommit(String commit, String project)
			throws IOException, InterruptedException {
		String[] command = { "cmd", };
		Process p = Runtime.getRuntime().exec(command);
		new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
		new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
		PrintWriter stdin = new PrintWriter(p.getOutputStream());
		stdin.println("E:");

		// Variables on server
		stdin.println("set PATH=%PATH%;C:/Program Files/Java/jdk1.6.0_25/bin");
		stdin.println("set JAVA_HOME=C:/Program Files/Java/jdk1.6.0_25");
		//stdin.println("set PATH=%PATH%;C:/Users/kochharps/AppData/Local/Programs/Git/bin");

		// stdin.println("cd \"E:\\Research Projects\\ICSM 2014 project\\Data\\\""+project);
		stdin.println("cd \"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/" + project+"\"");
		//System.exit(1);
		if(project.equals("httpclient")||project.equals("jackrabbit")){
			stdin.println("mvn clean");
		}
		if(project.equals("lucene-solr")||project.equals("rhino")){
			stdin.println("ant clean");
		}

		stdin.println("git checkout -f " + commit);

		if(project.equals("httpclient")||project.equals("jackrabbit")){
			stdin.println("mvn -fae compile");
		}
		if(project.equals("lucene-solr")||project.equals("rhino")){
			stdin.println("ant compile");
		}

		//stdin.println("mvn dependency:copy-dependencies");
		// write any other commands you want here
		stdin.close();
		int returnCode = p.waitFor();
		System.out.println("Return code = " + returnCode);
	}

	// Find names of packages & all the class files
	public static void findPackageNames(String project, String version)
			throws IOException, InterruptedException {

		// File theDir = new
		// File("E:/Research Projects/ICSM 2014 project/Data/"+project+"/RandoopClasses"+version);
		File theDir = new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/" + project + "/RandoopClasses" + version);
		/*List<File> fileA = Arrays.asList(theDir.listFiles());
		for (int i = 0; i < fileA.size(); i++) {
		FileDeleteStrategy.FORCE.delete(fileA.get(i));
		}*/

		FileUtils.deleteDirectory(theDir);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();
		}

		
		ArrayList<String> classFiles = new ArrayList<String>();

		// File folder = new
		// File("E:/Research Projects/ICSM 2014 project/Data/"+project+"/");
		File folder = new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/" + project + "/");
		List<File> fileList = Arrays.asList(folder.listFiles());
		for (int i = 0; i < fileList.size(); i++) {
			if (!(fileList.get(i).toString().contains("RandoopClasses") || fileList
					.get(i).toString().contains("RandoopTests") ||fileList
					.get(i).toString().contains("CoberturaInstrumented") )) {
				// System.out.println(fileList.get(i).toString());
				if (new File(fileList.get(i).toString()).isDirectory()) {
					Folder(fileList.get(i).toString(), classFiles);
				}
				if (new File(fileList.get(i).toString()).isFile()
						&& fileList.get(i).toString().endsWith(".class")
						&& !classFiles.contains(fileList.get(i).toString())) {
					classFiles.add(fileList.get(i).toString());
				}
			}
		}

		ArrayList<String> packageNames = new ArrayList<String>();
		for (String str : classFiles) {
			String[] split = str.split("\\\\");
			int pos = 0;
			for (int j = 0; j < split.length; j++) {
				if (split[j].equals("classes") && split[j + 1].equals("java")) {
					pos = j + 1;
				} else if (split[j].equals("classes")
						&& (!split[j + 1].equals("java"))) {
					if(split[j + 1].equals("tools")||split[j + 1].equals("examples")){
						pos = j+1;
					}else{
						pos=j;
					}
				}
				else if (split[j].trim().equals("build")
						&& (!split[j + 1].equals("java"))) {
					pos = j+1;
				}
			}

			String packageName = "";
			for (int z = (pos + 1); z < (split.length - 1); z++) {
				packageName = packageName + (split[z] + "/");
			}
			packageNames.add(packageName);
		}

		//System.out.println(classFiles);
		//System.out.println(packageNames);

		copyClasses(packageNames, classFiles, project, version);
	}

	// copy all the class files to Randoopclasses folder
	public static void copyClasses(ArrayList<String> packageNames,
			ArrayList<String> classFiles, String project, String version)
					throws IOException, InterruptedException {
		// if(version.equals("Newer")){
		// File file=new
		// File("E:/Research Projects/ICSM 2014 project/Data/"+project+"/classList.txt");
		File file = new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/" + project + "/classList.txt");

		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		// }

		for (String str : packageNames) {
			String folder = "";
			String[] split = str.split("/");
			for (int i = 0; i < split.length; i++) {
				folder = folder + "/" + split[i];

				// File theDir = new
				// File("E:/Research Projects/ICSM 2014 project/Data/"+project+"/RandoopClasses"+version+"/"+folder);
				File theDir = new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/" + project + "/RandoopClasses"
						+ version + "/" + folder);

				// if the directory does not exist, create it
				if (!theDir.exists()) {
					boolean result = theDir.mkdir();
				}
			}
		}

		//System.out.println(classFiles);
		
		for (String str : classFiles) {
			if (!(str.contains("\\test\\")||(str.contains("\\test-framework\\"))||(str.contains("\\test-classes\\")))) {
				String[] split = str.split("\\\\");
				int pos = 0;
				for (int j = 0; j < split.length; j++) {
					if (split[j].equals("classes")
							&& split[j + 1].equals("java")) {
						pos = j + 1;
					} else if (split[j].equals("classes")
							&& (!split[j + 1].equals("java"))) {
						if(split[j + 1].equals("tools")||split[j + 1].equals("examples")){
							pos = j+1;
						}else{
							pos=j;
						}
					}else if (split[j].equals("build")
							&& (!split[j + 1].equals("java"))) {
						pos = j+1;
					}
				}

				// System.out.println(split[split.length-1]);
				String packageName = "";
				for (int z = (pos + 1); z < (split.length); z++) {
					if (!((split[split.length - 1].contains("test")) || (split[split.length - 1]
							.contains("Test")))) {
						// System.out.println(split[split.length-1]);
						packageName = packageName + (split[z] + "/");
					}
				}

				//System.out.println(packageName);
				if (!packageName.isEmpty()) {
					if (version.equals("Newer")) {
						bw.write(packageName
								.substring(0, packageName.length() - 1)
								.replaceAll("\\.class", "").replaceAll("/", ".")
								+ "\n");
					}
					// String
					// fileName="E:/Research Projects/ICSM 2014 project/Data/"+project+"/RandoopClasses"+version+"/"+packageName;
					String fileName = "E:/Research Projects/ICSM 2014 project/RunTestCases/Data/" + project + "/RandoopClasses"
							+ version + "/" + packageName;

					File source = new File(str);
					File destination = new File(fileName);
					copyFileUsingApacheCommonsIO(source, destination);
				}
			}
		}

		bw.close();
	}

	// copy jar files into RandoopJars
	public static void copyJars(String project) throws IOException,
	InterruptedException {
		// File theDir = new
		// File("E:/Research Projects/ICSM 2014 project/Data/"+project+"/RandoopJars");
		File theDir = new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/" + project + "/RandoopJars");

		//FileUtils.deleteDirectory(theDir);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();
		}

		ArrayList<String> classFiles = new ArrayList<String>();

		// File folder = new
		// File("E:/Research Projects/ICSM 2014 project/Data/"+project+"/");
		File folder = new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/" + project + "/");

		List<File> fileList = Arrays.asList(folder.listFiles());
		for (int i = 0; i < fileList.size(); i++) {
			// System.out.println(fileList.get(i).toString());
			if (new File(fileList.get(i).toString()).isDirectory()) {
				FolderJars(fileList.get(i).toString(), classFiles);
			}
			if (new File(fileList.get(i).toString()).isFile()
					&& fileList.get(i).toString().endsWith(".jar")
					&& !classFiles.contains(fileList.get(i).toString())) {
				classFiles.add(fileList.get(i).toString());
			}
		}
		// System.out.println("Jars   "+classFiles);
		// System.exit(1);

		for (String str : classFiles) {
			String[] split = str.split("\\\\");
			String jarName = split[split.length - 1];

			// String
			// fileName="E:/Research Projects/ICSM 2014 project/Data/"+project+"/RandoopJars/"+jarName;
			String fileName = "E:/Research Projects/ICSM 2014 project/RunTestCases/Data/" + project + "/RandoopJars/" + jarName;

			File source = new File(str);
			File destination = new File(fileName);
			if (!destination.exists()) {
				copyFileUsingApacheCommonsIO(source, destination);
			}
		}
	}

	// copy all java files for Cobertura
	/*
	 * public static void copyJavaFiles(String project) throws IOException,
	 * InterruptedException { File file=new
	 * File("E:/Research Projects/ICSM 2014 project/Data/"
	 * +project+"/classList.txt"); BufferedWriter bw = new BufferedWriter(new
	 * FileWriter(file)); //}
	 * 
	 * ArrayList<String> classFiles=new ArrayList<String>();
	 * 
	 * File folder = new
	 * File("E:/Research Projects/ICSM 2014 project/Data/"+project+"/");
	 * List<File> fileList = Arrays.asList(folder.listFiles()); for(int
	 * i=0;i<fileList.size();i++) {
	 * if(!fileList.get(i).toString().contains("Randoop")){
	 * //System.out.println(fileList.get(i).toString()); if(new
	 * File(fileList.get(i).toString()).isDirectory()){
	 * Folder(fileList.get(i).toString(),classFiles); } if(new
	 * File(fileList.get(i).toString()).isFile() &&
	 * fileList.get(i).toString().endsWith(".java") &&
	 * !classFiles.contains(fileList.get(i).toString())){
	 * classFiles.add(fileList.get(i).toString()); } } }
	 * 
	 * 
	 * ArrayList<String> packageNames=new ArrayList<String>(); for(String
	 * str:classFiles){ String[] split=str.split("\\\\"); int pos=0; for(int
	 * j=0;j<split.length;j++){
	 * if(split[j].equals("classes")&&split[j+1].equals("java")){ pos=j+1; }
	 * else if(split[j].equals("classes")&& (!split[j+1].equals("java"))){
	 * pos=j; } }
	 * 
	 * String packageName=""; for(int z=(pos+1);z<(split.length-1);z++){
	 * packageName=packageName+(split[z]+"/"); } packageNames.add(packageName);
	 * }
	 * 
	 * for(String str:packageNames){ String folder=""; String[]
	 * split=str.split("/"); for(int i=0;i<split.length;i++){
	 * folder=folder+"/"+split[i];
	 * 
	 * File theDir = new
	 * File("E:/Research Projects/ICSM 2014 project/Data/"+project
	 * +"/RandoopClasses"+version+"/"+folder);
	 * 
	 * // if the directory does not exist, create it if (!theDir.exists()) {
	 * boolean result = theDir.mkdir(); } } }
	 * 
	 * for(String str:classFiles){ if(!str.contains("\\test\\")){ String[]
	 * split=str.split("\\\\"); int pos=0; for(int j=0;j<split.length;j++){
	 * if(split[j].equals("classes")&&split[j+1].equals("java")){ pos=j+1; }
	 * else if(split[j].equals("classes")&& (!split[j+1].equals("java"))){
	 * pos=j; } }
	 * 
	 * //System.out.println(split[split.length-1]); String packageName="";
	 * for(int z=(pos+1);z<(split.length);z++){
	 * if(!((split[split.length-1].contains
	 * ("test"))||(split[split.length-1].contains("Test")))){
	 * //System.out.println(split[split.length-1]);
	 * packageName=packageName+(split[z]+"/"); } }
	 * 
	 * 
	 * 
	 * //System.out.println(packageName); if(!packageName.isEmpty()){
	 * if(version.equals("Newer")){
	 * bw.write(packageName.substring(0,packageName.
	 * length()-1).replaceAll(".class", "").replaceAll("/", ".")+"\n"); } String
	 * fileName
	 * ="E:/Research Projects/ICSM 2014 project/Data/"+project+"/RandoopClasses"
	 * +version+"/"+packageName;
	 * 
	 * File source=new File(str); File destination=new File(fileName);
	 * copyFileUsingApacheCommonsIO(source,destination); } } }
	 * 
	 * bw.close(); }
	 */

	public static void instrumentCobertura(String project, String version)
			throws IOException, InterruptedException {

		File file=new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/cobertura.ser");

		if (file.exists()) {
			file.delete();
		}

		// File theDir = new
		// File("E:/Research Projects/ICSM 2014 project/Data/"+project+"/CoberturaInstrumented");
		File theDir = new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/" + project + "/CoberturaInstrumented");

		FileUtils.deleteDirectory(theDir);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();
		}

		String[] command = { "cmd", };
		Process p = Runtime.getRuntime().exec(command);
		new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
		new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
		PrintWriter stdin = new PrintWriter(p.getOutputStream());
		// stdin.println("E:");
		// stdin.println("set PATH=%PATH%;C:\\Program Files/Java/jdk1.6.0_25/bin");

		// stdin.println("cd \"E:/Research Projects/ICSM 2014 project/Data/"+project+"\"");
		//stdin.println("cd \"Data/" + project + "\"");

		/*
		 * stdin.println(
		 * "\"E:/Research Projects/ICSM 2014 project/Data/cobertura-1.9.4.1/cobertura-instrument.bat\" "
		 * + "\"E:/Research Projects/ICSM 2014 project/Data/"+project+
		 * "/RandoopClasses"+version+"\""+
		 * " --destination \"E:/Research Projects/ICSM 2014 project/Data/"
		 * +project+"/CoberturaInstrumented\"");
		 */

		stdin.println("\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/cobertura-1.9.4.1/cobertura-instrument.bat\" "
				+ "\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/" + project + "/RandoopClasses" + version + "\""
				+ " --datafile \"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/cobertura.ser\""
				+ " --destination \"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/" + project
				+ "/CoberturaInstrumented\"");
		// write any other commands you want here
		stdin.close();
		int returnCode = p.waitFor();
		System.out.println("Return code = " + returnCode);
	}

	//Copy generated test cases, execution traces of Newer & Older version
	public static void copyAllData(String project, String bugId,String older)
			throws IOException, InterruptedException {

		//copy generated test cases
		File theDir = new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/CoberturaData/GeneratedTestCases/"+older+"_"+bugId);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();
		}

		File folder=new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopTests/");
		List<File> fileList = Arrays.asList(folder.listFiles()); 
		for(int i=0;i<fileList.size();i++){
			if(fileList.get(i).toString().contains(".java")){
				String[] splitName=fileList.get(i).toString().split("/");
				File source=new File(fileList.get(i).toString());
				File destination=new File(theDir+"/"+splitName[splitName.length-1]);
				copyFileUsingApacheCommonsIO(source, destination);
			}
		}

		//copy test executions new
		File testExe = new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/CoberturaData/TestExecutionsNewer/"+older+"_"+bugId);
		if (!testExe.exists()) {
			boolean result = testExe.mkdir();
		}

		folder=new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopOutput/");
		fileList = Arrays.asList(folder.listFiles()); 
		for(int i=0;i<fileList.size();i++){
			String[] splitName=fileList.get(i).toString().split("/");
			File source=new File(fileList.get(i).toString());
			File destination=new File(testExe+"/"+splitName[splitName.length-1]);
			copyFileUsingApacheCommonsIO(source, destination);
		}

		//copy test executions old
		File testExeNew = new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/CoberturaData/TestExecutionsOlder");
		if (!testExeNew.exists()) {
			boolean result = testExeNew.mkdir();
		}
		File source=new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/randoopNewer");
		File destination=new File(testExeNew+"/"+older+"_"+bugId);
		copyFileUsingApacheCommonsIO(source, destination);
	}
	private static void copyFileUsingApacheCommonsIO(File source, File dest)
			throws IOException {
		FileUtils.copyFile(source, dest);
	}

	public static void Folder(String file, ArrayList<String> classFiles)
			throws FileNotFoundException {
		File folder = new File(file);
		List<File> fileList = Arrays.asList(folder.listFiles());

		for (int i = 0; i < fileList.size(); i++) {
			if (new File(fileList.get(i).toString()).isDirectory()) {
				Folder(fileList.get(i).toString(), classFiles);
			}
			if (new File(fileList.get(i).toString()).isFile()
					&& fileList.get(i).toString().endsWith(".class")
					&& !classFiles.contains(fileList.get(i).toString())) {
				classFiles.add(fileList.get(i).toString());
			}
		}
	}

	public static void FolderJars(String file, ArrayList<String> classFiles)
			throws FileNotFoundException {
		File folder = new File(file);
		List<File> fileList = Arrays.asList(folder.listFiles());

		for (int i = 0; i < fileList.size(); i++) {
			if (new File(fileList.get(i).toString()).isDirectory()) {
				FolderJars(fileList.get(i).toString(), classFiles);
			}
			if (new File(fileList.get(i).toString()).isFile()
					&& fileList.get(i).toString().endsWith(".jar")
					&& !classFiles.contains(fileList.get(i).toString())) {
				classFiles.add(fileList.get(i).toString());
			}
		}
	}

	public static void FolderJava(String file, ArrayList<String> classFiles)
			throws FileNotFoundException {
		File folder = new File(file);
		List<File> fileList = Arrays.asList(folder.listFiles());

		for (int i = 0; i < fileList.size(); i++) {
			if (new File(fileList.get(i).toString()).isDirectory()) {
				FolderJava(fileList.get(i).toString(), classFiles);
			}
			if (new File(fileList.get(i).toString()).isFile()
					&& fileList.get(i).toString().endsWith(".java")
					&& !classFiles.contains(fileList.get(i).toString())) {
				classFiles.add(fileList.get(i).toString());
			}
		}
	}
}
