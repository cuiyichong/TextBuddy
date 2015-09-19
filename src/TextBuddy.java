import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TextBuddy {
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		waitFileInput();
		while(sc.hasNext()) {
			
			String command = sc.nextLine();
			executeFileCommand(command, sc);
			waitFileInput();
		}
	}
	
	// execute command to access file and command to modify its content
	private static void executeFileCommand(String command, Scanner sc) {
		
		String fileAction = getFirstWord(command);
		String fileName = getSecondWord(command);
		ArrayList<String> arrayList = new ArrayList<String>();
		
		if(fileAction.equals("TextBuddy")) {
			
			File newTextFile =new File(fileName);
			System.out.print("Welcome to TextBuddy. ");
			try{
		    	
		    	if(newTextFile.createNewFile()){
		    		System.out.println(fileName + " is ready for use");
		    	}
		    	else {
		    		System.out.println(fileName + " already exists");
		    		if(fileIsEmpty(newTextFile)==false) {
		    			arrayList = loadToList(newTextFile);
		    		}
		    	}			    			
		    	waitCommandInput();
		    	
		    	while(sc.hasNext())	{
		    		String feedback = "";
		    		String userCommand = sc.nextLine().trim();
		    		String commandType = getFirstWord(userCommand);
		    		String content = getSecondWord(userCommand);
		    		System.out.println(commandType);
		    		System.out.println(content);
		    		if(userCommand.equals("exit") )
		    			break;
		    		else if(commandType.equals("display")) {
	    				feedback = display(newTextFile);
	    			}
		    		else if(commandType.equals("clear")) {
	    				feedback = clearText(arrayList, newTextFile);
	    			}
		    		else if(commandType.equals("add")) {
		    			feedback = addToFile(arrayList, newTextFile, content);
		    		}
		    		else if(commandType.equals("delete")){
		    			feedback = deleteFromFile(arrayList, content ,newTextFile);
		    		}
		    		else {
		    			feedback = "Invalid command: "+userCommand;
		    		}
		    		System.out.println(feedback);
		    		waitCommandInput();
		  
		    	} 
		    	
		    	}catch(IOException e){
		    		e.printStackTrace();
		    	}
			}
		
		else { 
			displayMessageInvalid(command);
		}
		
	}

	private static void waitCommandInput() {
		System.out.print("command: ");
	}
	
	private static void waitFileInput() {
		System.out.print("c:>java ");
	}

	private static String clearText(ArrayList<String> arrayList, File file) {
		arrayList.clear();
		writeToFile(arrayList, file);
		return "all content deleted from "+ file.getName();
	}
	
	
	private static String addToFile(ArrayList<String> arrayList, File file, String content) {
		System.out.println(content);
		arrayList.add(content);
		writeToFile(arrayList, file);
		return "added to " + file.getName()+": \"" + content+'\"';
	}
	
	// display invalid message with the input message
	private static void displayMessageInvalid(String str) {
		System.out.println("Invalid command: "+str);
	}
	
	private static String display(File file) {
		if(fileIsEmpty(file)) {
			return file.getName() + " is empty";
		}
		else {
			StringBuilder displayStr = new StringBuilder();
			try{
				String str= new String();
				BufferedReader br = new BufferedReader(new FileReader(file.getName()));
				while((str=br.readLine())!=null) {
					displayStr.append(str);
					displayStr.append('\n');
				}
				br.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			return displayStr.toString().trim();
		}
	}
	
	// return first keyword of command
	private static String getFirstWord(String command) {
		String commandTypeString = command.trim().split(" ")[0];
		return commandTypeString;
	}

	// return second keyword of command
	private static String getSecondWord(String command) {
		return command.replace(getFirstWord(command), "").trim();
	}

	// If file of the same name exists under the directory, 
	// load content in every line without index to arraylist
	private static ArrayList<String> loadToList (File file) {
		ArrayList<String> content = new ArrayList<String>();
		try {
			BufferedReader bw = new BufferedReader(new FileReader(file.getName()));
			String str;
			while((str=bw.readLine())!=null) {
				content.add(getSecondWord(str).trim());
			}
			bw.close();
		}catch(IOException e){
		e.printStackTrace();
		}
		return content;
	}

	//	return true if file is empty
	private static boolean fileIsEmpty (File file) {
		String str = new String();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file.getName()));
			str = br.readLine();
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		if(str==null)
			return true;
		else
			return false;
	}
	// write every elements in arraylist to file
	public static void writeToFile(ArrayList<String> list, File file) {
		try {
			BufferedWriter bw = new BufferedWriter (new FileWriter(file.getName()));
			for(int i=1; i<=list.size(); i++) {
				bw.write(i+". "+list.get(i-1));
				bw.newLine();
			}
			bw.close();
				
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	// delete string at the content index from file only 
	// when the string content is numeric, larger than 0 and content index exists  
	public static String deleteFromFile(ArrayList<String> list, String content , File file) {
		System.out.println(content);
		if(isNumeric(content)) {
			int i = Integer.valueOf(content);
			if (i<=list.size() && i>0) {
				list.remove(i-1);
				writeToFile(list, file);
				return "deleted from " + file.getName()+": \"" + list.get(i-1)+'\"';
			
			}
		}
		return "Index " + content + " does not exist";
	}

// return true if string is numeric string including 0	
	public static boolean isNumeric(String str)
	{
		String regex = "^[0-9]";
		return str.matches(regex);
	}

} 