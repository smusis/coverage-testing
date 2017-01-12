package testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import testing.*;

public class RunRandoopAbsolute {
	public static void main(String[] args) throws IOException, InterruptedException {
		//generateTestCases();
		//compileTestCases();
		//runTestCasesNewer();
		//runTestCasesOlder();
		//getFailedTests();
	}

	public static void generateTestCases(String project,String version) throws IOException, InterruptedException
	{	
		System.out.println("Generate");
		//File theDir = new File("E:/Research Projects/ICSM 2014 project/Data/"+project+"/RandoopTests");
		File theDir = new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopTests");
		FileUtils.deleteDirectory(theDir);
		/*List<File> fileList = Arrays.asList(theDir.listFiles());
		for(int i=0;i<fileList.size();i++)
		{
			FileUtils.forceDelete(fileList.get(i));
		}*/
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();  
		}

		String[] command =
			{
				"cmd",
			};
		Process p = Runtime.getRuntime().exec(command);
		new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
		new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
		PrintWriter stdin = new PrintWriter(p.getOutputStream());
		//stdin.println("E:");
		//stdin.println("set PATH=%PATH%;C:/Program Files/Java/jdk1.7.0_15/bin");
		//stdin.println("cd \"E:/Research Projects/ICSM 2014 project/Data/httpclient\"");

		/*stdin.println("java -cp \"E:/Research Projects/ICSM 2014 project/Data/jars/randoop.1.3.4.jar\";" +
				"\"E:/Research Projects/ICSM 2014 project/Data/"+project+"/RandoopClasses"+version+"\";" +
				"\"E:/Research Projects/ICSM 2014 project/Data/"+project+"/RandoopJars/*\" " +
				"randoop.main.Main gentests " +
				"--classlist=\"E:/Research Projects/ICSM 2014 project/Data/"+project+"/classList.txt\" " +
				"--junit-output-dir=\"E:/Research Projects/ICSM 2014 project/Data/"+project+"/RandoopTests\"");*/

		//stdin.println("set PATH=%PATH%;C:/Program Files/Java/jdk1.6.0_25/bin");
		//stdin.println("set JAVA_HOME=C:/Program Files/Java/jdk1.6.0_25");
		
		stdin.println("java -cp \"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/jars/randoop.1.3.4.jar\";" +
				"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopClasses"+version+"\";" +
				"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopJars/*\";" +
						"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopJarsCopy/*\" " +
				"randoop.main.Main gentests " +
				"--classlist=\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/classList.txt\" " +
				"--timelimit 100 "+
				"--junit-output-dir=\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopTests\"");
		// write any other commands you want here
		stdin.close();
		int returnCode = p.waitFor();
		System.out.println("Return code = " + returnCode);
	}

	public static void compileTestCases(String project,String version) throws IOException, InterruptedException
	{
		String[] command =
			{
				"cmd",
			};
		Process p = Runtime.getRuntime().exec(command);
		new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
		new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
		PrintWriter stdin = new PrintWriter(p.getOutputStream());
		//stdin.println("E:");
		//stdin.println("set PATH=%PATH%;C:/Program Files/Java/jdk1.7.0_15/bin");

		//stdin.println("cd \"E:/Research Projects/ICSM 2014 project/Data/"+project+"\"");
		//stdin.println("cd ./Data/"+project);

		/*stdin.println("\"C:/Program Files/Java/jdk1.7.0_15/bin/javac.exe\" -cp \"E:/Research Projects/ICSM 2014 project/Data/jars/randoop.1.3.4.jar\";" +
				"\"E:/Research Projects/ICSM 2014 project/Data/"+project+"/RandoopJars/*\";" +
				"\"E:/Research Projects/ICSM 2014 project/Data/"+project+"/RandoopClasses"+version+"\"; " +
				"RandoopTests/RandoopTest*.java");*/
		//stdin.println("set PATH=%PATH%;C:/Program Files/Java/jdk1.6.0_25/bin");
		//stdin.println("set JAVA_HOME=C:/Program Files/Java/jdk1.6.0_25");
		
		if(version.equals("Newer")){
			stdin.println("javac -cp \"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/jars/randoop.1.3.4.jar\";" +
					"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopClasses"+version+"\";" +
					"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopJars/*\" " +
					"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopTests/RandoopTest\"*.java");
		}
		if(version.equals("Older")){
			stdin.println("javac -cp \"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/jars/randoop.1.3.4.jar\";" +
					"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopClasses"+version+"\";" +
					"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopJars/*\" " +
					"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopTestsCommented/RandoopTest\"*.java");
		}
		// write any other commands you want here
		stdin.close();
		int returnCode = p.waitFor();
		System.out.println("Return code = " + returnCode);
	}

	//Run test cases 100 times on the newer version(bug fix) to see the non deterministic test cases (which keep changing)
	public static void runTestCasesNewer(String project,String version) throws IOException, InterruptedException
	{

		//File theDir = new File("E:/Research Projects/ICSM 2014 project/Data/"+project+"/RandoopOutput");
		File theDir = new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopOutput");

		FileUtils.deleteDirectory(theDir);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();  
		}

		String[] command =
			{
				"cmd",
			};
		Process p = Runtime.getRuntime().exec(command);
		new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
		new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
		PrintWriter stdin = new PrintWriter(p.getOutputStream());
		//stdin.println("E:");

		//stdin.println("set PATH=%PATH%;C:\\Program Files\\Java\\jdk1.7.0_15\\bin");
		//stdin.println("cd \"E:\\Research Projects\\ICSM 2014 project\\Data\\httpclient\"");

		/*stdin.println("for /l %x in (1,1,100) do java -classpath " +
				"\"E:\\Research Projects\\ICSM 2014 project\\Data\\jars\\randoop.1.3.4.jar\";" +
				"\"E:\\Research Projects\\ICSM 2014 project\\Data\\"+project+"\\RandoopClasses"+version+"\";" +
				"\"E:\\Research Projects\\ICSM 2014 project\\Data\\"+project+"\\RandoopJars\\*\";" +
				"\"E:\\Research Projects\\ICSM 2014 project\\Data\\"+project+"\\RandoopTests\" " +
				"junit.textui.TestRunner --output-nonexec=true --capture-output RandoopTest > " +
				"\"E:\\Research Projects\\ICSM 2014 project\\Data\\"+project+"\\RandoopOutput\\randoop%x\"");*/

		stdin.println("set PATH=%PATH%;C:/Program Files/Java/jdk1.6.0_25/bin");
		stdin.println("set JAVA_HOME=C:/Program Files/Java/jdk1.6.0_25");
		
		stdin.println("for /l %x in (1,1,50) do java -classpath " +
				"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data\\jars\\randoop.1.3.4.jar\";" +
				"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data\\"+project+"\\RandoopClasses"+version+"\";" +
				"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data\\"+project+"\\RandoopJars\\*\";" +
				"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data\\"+project+"\\RandoopTests\" " +
				"junit.textui.TestRunner --output-nonexec=true --capture-output RandoopTest > " +
				"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data\\"+project+"\\RandoopOutput\\randoop%x\"");
		// write any other commands you want here
		stdin.close();
		int returnCode = p.waitFor();
		System.out.println("Return code = " + returnCode);
	}

	//Run test cases once on the older version(buggy)
	public static void runTestCasesOlder(String project,String version,String bugId,String older) throws IOException, InterruptedException
	{
		/*File file=new File("Data\\"+project+"\\cobertura.ser");


		if (file.exists()) {
			file.delete();
		}*/
		
		String[] command =
			{
				"cmd",
			};
		Process p = Runtime.getRuntime().exec(command);
		new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
		new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
		PrintWriter stdin = new PrintWriter(p.getOutputStream());
		//stdin.println("E:");
		//stdin.println("set PATH=%PATH%;C:\\Program Files\\Java\\jdk1.7.0_15\\bin");
		//stdin.println("cd \"E:\\Research Projects\\ICSM 2014 project\\Data\\httpclient\"");

		stdin.println("set PATH=%PATH%;C:/Program Files/Java/jdk1.6.0_25/bin");
		stdin.println("set JAVA_HOME=C:/Program Files/Java/jdk1.6.0_25");
		
		/*stdin.println("java -classpath " +
				"\"E:\\Research Projects\\ICSM 2014 project\\Data\\jars\\randoop.1.3.4.jar\";" +
				"\"E:\\Research Projects\\ICSM 2014 project\\Data\\"+project+"\\RandoopClasses"+version+"\";" +
				"\"E:\\Research Projects\\ICSM 2014 project\\Data\\"+project+"\\RandoopJars\\*\";" +
				"\"E:\\Research Projects\\ICSM 2014 project\\Data\\"+project+"\\RandoopTests\" " +
				"junit.textui.TestRunner --output-nonexec=true --capture-output RandoopTest > " +
				"\"E:\\Research Projects\\ICSM 2014 project\\Data\\"+project+"\\randoopNewer\"");*/
		stdin.println("java -classpath \"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/jars/randoop.1.3.4.jar\";" +
				"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/cobertura-1.9.4.1/lib/*\";" +
				"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/CoberturaInstrumented\";" +
						"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopClassesOlder\";" +
				"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopJars/*\";" +
						"\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopTestsCommented\" " +
				"-Dnet.sourceforge.cobertura.datafile=\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/cobertura.ser\" " +
				"junit.textui.TestRunner --capture-output RandoopTest > \"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/randoopNewer\"");
		/*stdin.println("java -classpath " +
				"\"Data\\jars\\randoop.1.3.4.jar\";" +
				"\"Data/cobertura-1.9.4.1/lib/*\";"+
				"\"Data/"+project+"/CoberturaInstrumented\";"+
				"\"Data\\"+project+"\\RandoopClasses"+version+"\";" +
				"\"Data\\"+project+"\\RandoopJars\\*\";" +
				"\"Data\\"+project+"\\RandoopTests\" " +
				"-Dnet.sourceforge.cobertura.datafile=\"Data/httpclient/cobertura.ser\"" +
				"junit.textui.TestRunner --output-nonexec=true --capture-output RandoopTest > " +
				"\"Data\\"+project+"\\randoopNewer\"");*/
		// write any other commands you want here
		stdin.close();
		int returnCode = p.waitFor();
		System.out.println("Return code = " + returnCode);
		generateCobReport(project, version, bugId, older);
	}

	//Delete Non-Deterministic tests & copy rest of test file to RandoopTestsCommented
	public static void commentNonDetTests(String project,String version,String bugId) throws IOException, InterruptedException
	{
		System.out.println(bugId);
		CheckFolderOrFileAbsolute orFile=new CheckFolderOrFileAbsolute();
		ArrayList<String> finalNonDetTests=orFile.checkNonDeterministicTests(project);
		ArrayList<String> testSuite=new ArrayList<String>();
		ArrayList<String> testCase=new ArrayList<String>();
		for(String str:finalNonDetTests){
			String[] split=str.split("\\(");
			testSuite.add(split[1].replaceAll("\\)", "").replaceAll("RandoopTest", ""));
			testCase.add(split[0].replaceAll("test", ""));
		}

		File theDir = new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopTestsCommented");
		FileUtils.deleteDirectory(theDir);
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();  
		}

		for(int i=0;i<testSuite.size();i++){

			String line="";
			StringBuilder content=new StringBuilder();
			BufferedReader br = null;

			File fileExists=new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopTestsCommented/RandoopTest"+testSuite.get(i)+".java");
			if(fileExists.exists()){
				System.out.println("yes");
				br = new BufferedReader(new FileReader(fileExists));
			}
			else{

				br = new BufferedReader(new FileReader("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopTests/RandoopTest"+testSuite.get(i)+".java")); 
			}

			while((line=br.readLine())!=null)
			{
				content.append(line+"\n");
			}
			br.close();
			System.out.println(testSuite.get(i)+"  "+testCase.get(i).trim());
			//System.out.println("public void test"+(Integer.parseInt(testCase.get(i).trim())+1));
			int start=content.indexOf("public void test"+Integer.parseInt(testCase.get(i).trim()));

			int method=Integer.parseInt(testCase.get(i).trim())+1;
			int end=content.indexOf("public void test"+method,start);

			System.out.println(start);
			System.out.println(end);
			if(end>start){
				content.delete(start, end);
			}

			File file=new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopTestsCommented/RandoopTest"+testSuite.get(i)+".java");
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(content.toString());
			bw.close();
			content.delete(0, content.length());
			//System.exit(1);

		}

		File folder = new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopTests/");

		List<File> fileList = Arrays.asList(folder.listFiles());
		for(int i=0;i<fileList.size();i++)
		{
			if(fileList.get(i).toString().contains(".java")){
				String[] splitName=fileList.get(i).toString().split("/");
				File source=new File(fileList.get(i).toString());
				File destination=new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/RandoopTestsCommented/"+splitName[splitName.length-1]);

				if(!destination.exists()){
					copyFileUsingApacheCommonsIO(source,destination);
				}
			}
		}
		//File source=new File("Data/"+project+"/RandoopTests/RandoopTest.java");
		//File destination=new File("Data/"+project+"/RandoopTestsCommented/RandoopTest.java");
		//copyFileUsingApacheCommonsIO(source,destination);
	}


	//Run test cases once on the older version(buggy) with Cobertura
	public static void runTestCasesOlderCobertura(String project,String version,String bugId) throws IOException, InterruptedException
	{
		CheckFolderOrFile orFile=new CheckFolderOrFile();
		ArrayList<String> finalNonDetTests=orFile.checkNonDeterministicTests(project);

		//File file=new File("E:\\Research Projects\\ICSM 2014 project\\Data\\"+project+"\\cobertura.ser");
		File file=new File("Data\\"+project+"\\cobertura.ser");


		if (file.exists()) {
			file.delete();
		}

		ArrayList<String> testSuite=new ArrayList<String>();
		ArrayList<String> testCase=new ArrayList<String>();

		for(String str:finalNonDetTests){
			String[] split=str.split("\\(");
			testSuite.add(split[1].replaceAll("\\)", "").replaceAll("RandoopTest", ""));
			testCase.add(split[0].replaceAll("test", ""));
		}

		//System.out.println(testSuite);
		//System.out.println(testCase);
		//File folder = new File("E:/Research Projects/ICSM 2014 project/Data/"+project+"/RandoopTests/");
		File folder = new File("Data/"+project+"/RandoopTests/");

		List<File> fileList = Arrays.asList(folder.listFiles());
		for(int i=0;i<((fileList.size()/2)-1);i++)
		{
			for(int j=1;j<=500;j++){
				boolean failed=false;
				for(int z=0;z<testSuite.size();z++){
					if(testSuite.get(z).equals(Integer.toString(i))){
						if(testCase.get(z).equals(Integer.toString(j))){
							failed=true;
						}
					}
				}

				if(failed){
					//System.out.println(i+" "+j);
					//System.exit(1);
					continue;
				}
				String[] command =
					{
						"cmd",
					};
				Process p = Runtime.getRuntime().exec(command);
				new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
				new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
				PrintWriter stdin = new PrintWriter(p.getOutputStream());

				//stdin.println("E:");
				//stdin.println("set PATH=%PATH%;C:\\Program Files\\Java\\jdk1.7.0_15\\bin");
				//stdin.println("cd \"E:\\Research Projects\\ICSM 2014 project\\Data\\httpclient\"");

				/*stdin.println("java -cp " +
						"\"E:\\Research Projects\\ICSM 2014 project\\Data\\jars\\randoop.1.3.4.jar\";" +
						"\"E:/Research Projects/ICSM 2014 project/Data/cobertura-1.9.4.1/lib/*\";"+
						"\"E:/Research Projects/ICSM 2014 project/Data/"+project+"/CoberturaInstrumented\";"+
						"\"E:\\Research Projects\\ICSM 2014 project\\Data\\"+project+"\\RandoopClasses"+version+"\";" +
						"\"C:\\Users\\kochharps.2012\\Documents\\Applications\\CoverageTesting\\bin\";"+
						"\"E:\\Research Projects\\ICSM 2014 project\\Data\\"+project+"\\RandoopJars\\*\";" +
						"\"E:\\Research Projects\\ICSM 2014 project\\Data\\"+project+"\\RandoopTests\" " +
						"-Dnet.sourceforge.cobertura.datafile=\"E:/Research Projects/ICSM 2014 project/Data/httpclient/cobertura.ser\"" +
						" testing.SingleJUnitTestRunner RandoopTest"+i+"#test"+j);*/

				stdin.println("java -cp " +
						"\"Data\\jars\\randoop.1.3.4.jar\";" +
						"\"Data/cobertura-1.9.4.1/lib/*\";"+
						"\"Data/"+project+"/CoberturaInstrumented\";"+
						"\"Data\\"+project+"\\RandoopClasses"+version+"\";" +
						"\"CoverageTesting\\bin\";"+
						"\"Data\\"+project+"\\RandoopJars\\*\";" +
						"\"Data\\"+project+"\\RandoopTests\" " +
						"-Dnet.sourceforge.cobertura.datafile=\"Data/httpclient/cobertura.ser\"" +
						" testing.SingleJUnitTestRunner RandoopTest"+i+"#test"+j);

				stdin.close();
				int returnCode = p.waitFor();
				System.out.println("Return code = " + returnCode);
			}
		}

		//generateCobReport(project, version,bugId,o);
	}

	//Generate report for cobertura
		public static void generateCobReport(String project,String version,String bugId,String older) throws IOException, InterruptedException
		{
			String[] command =
				{
					"cmd",
				};
			Process p = Runtime.getRuntime().exec(command);
			new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
			new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
			PrintWriter stdin = new PrintWriter(p.getOutputStream());
			//stdin.println("E:");
			//stdin.println("set PATH=%PATH%;C:\\Program Files\\Java\\jdk1.7.0_15\\bin");
			//stdin.println("cd \"E:\\Research Projects\\ICSM 2014 project\\Data\\httpclient\"");

			stdin.println("set PATH=%PATH%;C:/Program Files/Java/jdk1.6.0_25/bin");
			stdin.println("set JAVA_HOME=C:/Program Files/Java/jdk1.6.0_25");
			
			/*File theDir = new File("E:/Research Projects/ICSM 2014 project/Data/"+project+"/CoberturaCoverage");
				FileUtils.deleteDirectory(theDir);
				// if the directory does not exist, create it
				if (!theDir.exists()) {
					boolean result = theDir.mkdir();  
				}*/

			/*stdin.println("\"E:\\Research Projects\\ICSM 2014 project\\Data\\cobertura-1.9.4.1\\cobertura-report.bat\" " +
					"--format xml --datafile=\"E:/Research Projects/ICSM 2014 project/Data/httpclient/cobertura.ser\" " +
					"--destination \"E:/Research Projects/ICSM 2014 project/Data/"+project+"/CoberturaCoverage\"");*/

			stdin.println("\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data\\cobertura-1.9.4.1\\cobertura-report.bat\" " +
					"--format xml --datafile=\"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/cobertura.ser\" " +
					"--destination \"E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/CoberturaCoverage\"");

			// write any other commands you want here
			stdin.close();
			int returnCode = p.waitFor();
			System.out.println("Return code = " + returnCode);

			/*String covSource="E:/Research Projects/ICSM 2014 project/Data/"+project+"/CoberturaCoverage/coverage.xml";
			String covDest="E:/Research Projects/ICSM 2014 project/Data/CoberturaData/CoberturaCoverage/"+bugId+".xml";*/

			String covSource="E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/CoberturaCoverage/coverage.xml";
			String covDest="E:/Research Projects/ICSM 2014 project/RunTestCases/Data/CoberturaData/CoberturaCoverage/"+older+"_"+bugId+".xml";

			File source=new File(covSource);
			File destination=new File(covDest);
			//if(!destination.exists()){
				copyFileUsingApacheCommonsIO(source,destination);
			//}

		}

	//Get the non-deterministic test cases
	public static void getFailedTests(String project,String bugId,String older) throws IOException, InterruptedException
	{
		//File file=new File("E:/Research Projects/ICSM 2014 project/Data/CoberturaData/PassedTests"+bugId+".txt");
		File file=new File("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/CoberturaData/PassedTests/"+older+"_"+bugId+".txt");

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

		CheckFolderOrFile orFile=new CheckFolderOrFile();
		ArrayList<String> finalNonDetTests=orFile.checkNonDeterministicTests(project);

		System.out.println(finalNonDetTests.size());
		ArrayList<String> nonDetTests=new ArrayList<String>();
		boolean failures=false;
		String line="";
		//final BufferedReader br = new BufferedReader(new FileReader("E:/Research Projects/ICSM 2014 project/Data/"+project+"/randoopNewer")); 
		final BufferedReader br = new BufferedReader(new FileReader("E:/Research Projects/ICSM 2014 project/RunTestCases/Data/"+project+"/randoopNewer")); 

		while((line=br.readLine())!=null)
		{
			if((line.contains("There were") && line.contains("failures:"))){
				failures=true;
			}

			if(failures && line!=null && !line.isEmpty()){
				if(Character.isDigit(line.charAt(0))){
					String[] split=line.split("\\)");
					nonDetTests.add(split[1]+")");
				}
			}
		}

		System.out.println(finalNonDetTests);
		for(String str: nonDetTests){
			//System.out.println(str);

			if(!finalNonDetTests.contains(str)){		
				out.write(str+"\n");
			}
		}
		br.close();
		out.close();
	}

	private static void copyFileUsingApacheCommonsIO(File source, File dest)
			throws IOException {
		FileUtils.copyFile(source, dest);
	}
}



