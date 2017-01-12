package testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CheckFolderOrFile {
	public static void main(String[] args) throws IOException {
		//checkFileFolder();
		//checkNonDeterministicTests();
	}

	public static void checkFileFolder(String project) throws FileNotFoundException
	{
		File folder = new File("E:/Research Projects/ICSM 2014 project/Data/httpclient/classes/");
		List<File> fileList = Arrays.asList(folder.listFiles());

		ArrayList<String> classFiles=new ArrayList<String>();
		
		for(int i=0;i<fileList.size();i++)
		{
			if(new File(fileList.get(i).toString()).isDirectory()){
				Folder(fileList.get(i).toString());
			}
			if(new File(fileList.get(i).toString()).isFile()){
				String[] split=fileList.get(i).toString().split("\\\\");
				int pos=0;
				for(int j=0;j<split.length;j++){
					if(split[j].equals("classes")){
						pos=j;
					}
				}

				String fileName="";
				for(int z=(pos+1);z<split.length;z++){
					fileName=fileName+"."+split[z];
				}
				if(fileName.contains(".class")){
					System.out.println(fileName.substring(1, fileName.length()).replaceAll(".class", ""));
				}
			}
		}
	}

	public static void Folder(String file) throws FileNotFoundException{
		File folder = new File(file);
		List<File> fileList = Arrays.asList(folder.listFiles());

		for(int i=0;i<fileList.size();i++)
		{
			if(new File(fileList.get(i).toString()).isDirectory()){
				Folder(fileList.get(i).toString());
			}
			if(new File(fileList.get(i).toString()).isFile()){
				File(fileList.get(i).toString());
			}
		}
	}
	public static void File(String file) throws FileNotFoundException{
		String[] split=file.split("\\\\");
		int pos=0;
		for(int j=0;j<split.length;j++){
			if(split[j].equals("classes")){
				pos=j;
			}
		}

		String fileName="";
		for(int z=(pos+1);z<split.length;z++){
			fileName=fileName+"."+split[z];
		}
		if(fileName.contains(".class")){
			System.out.println(fileName.substring(1, fileName.length()).replaceAll(".class", ""));
		}
	}
	
	
	public static ArrayList<String> checkNonDeterministicTests(String project) throws IOException
	{
		File folder = new File("Data/"+project+"/RandoopOutput/");
		//File folder = new File("Data/"+project+"/RandoopOutput/");
		
		
		List<File> fileList = Arrays.asList(folder.listFiles());
		
		TreeMap<String,Integer> nonDetTests=new TreeMap<String, Integer>();
		ArrayList<String> finalNonDetTests=new ArrayList<String>();

		for(int i=0;i<fileList.size();i++)
		{
			String line="";
			final BufferedReader br = new BufferedReader(new FileReader(fileList.get(i))); 
			//System.out.println(fileList.get(i));
			boolean failures=false;
			int count=0;
			while((line=br.readLine())!=null)
			{
				if((line.contains("There were") && line.contains("failures:"))){
					failures=true;
				}
				
				if(failures && line!=null && !line.isEmpty()){
					if(Character.isDigit(line.charAt(0))){
						String[] split=line.split("\\)");
						if(!nonDetTests.containsKey(split[1]+")")){
							nonDetTests.put(split[1]+")",1);
						}
						else{
							nonDetTests.put(split[1]+")",nonDetTests.get(split[1]+")")+1);
						}
					}
				}
			}
		}
		
		System.out.println(nonDetTests);
		for (Map.Entry<String, Integer> entry : nonDetTests.entrySet()) {
			if(entry.getValue()!=50){
				finalNonDetTests.add(entry.getKey());
			}
		}
		return finalNonDetTests;
	}
}
