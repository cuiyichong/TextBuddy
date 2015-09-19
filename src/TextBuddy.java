import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TextBuddy {
	
	private static Scanner sc = new Scanner(System.in);
	private static final String EXIT_STRING = "00000";
	private static final String MESSAGE_INVALID_COMMAND = "Invalid command: %s";
	private static final String MESSAGE_FILE_CLEARED = "all content deleted from %s";
	private static final String MESSAGE_ITEM_ADDED = "added to %s: \"%s\"";
	private static final String MESSAGE_FILE_EMPTY ="%s is empty";
	private static final String MESSAGE_ITEM_DELETED ="deleted from %s: \"%s\"";
	private static final String MESSAGE_INDEX_NOT_EXIST = "Index %s does not exist";
	
	public static void main(String[] args) {
		
		waitFileInput();
		while(sc.hasNext()) {
			String command = sc.nextLine();
			executeFileCommand(command);
			waitFileInput();
		}
	}
	
	// execute command to access file and command to modify its content
	private static void executeFileCommand(String command) {
		
		String fileAction = getFirstWord(command);
		String fileName = getSecondWord(command);
		ArrayList<String> arrayList = new ArrayList<String>();
		
		if(fileAction.equalsIgnoreCase("TextBuddy")) {
		
			File newTextFile =new File(fileName);
			arrayList = prepareSystem(arrayList, newTextFile);
			waitCommandInput();
		    while(sc.hasNext())	{
		    	String userCommand = sc.nextLine().trim();
		    	String feedback = executeUserCommand(arrayList, newTextFile, userCommand);
		    	if (feedback.equals(EXIT_STRING)) {
		    		break;
		    	}
		    	feedbackToUser(feedback);
		    	waitCommandInput();	
		    } 	
		    
		}
		else { 
			feedbackToUser(String.format(MESSAGE_INVALID_COMMAND, command));
		}
		
	}

	public static ArrayList<String> prepareSystem(ArrayList<String> arrayList, File newTextFile) {
		System.out.print("Welcome to TextBuddy. ");
		try{
			if(newTextFile.createNewFile()){
				System.out.println(newTextFile.getName() + " is ready for use");
			}
			else {
				System.out.println(newTextFile.getName() + " already exists");
				if(fileIsEmpty(newTextFile)==false) {
					arrayList = loadToList(newTextFile);
				}
			}			    			
		}catch(IOException e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public static String executeUserCommand(ArrayList<String> arrayList, File newTextFile, String userCommand) {
		String feedback;
		String commandType = getFirstWord(userCommand);
		String content = getSecondWord(userCommand);
		if(commandType.equalsIgnoreCase("exit") )
			feedback = EXIT_STRING;
		else if(commandType.equalsIgnoreCase("display")) {
			feedback = display(newTextFile);
		}
		else if(commandType.equalsIgnoreCase("clear")) {
			feedback = clearText(arrayList, newTextFile);
		}
		else if(commandType.equalsIgnoreCase("add")) {
			feedback = addToFile(arrayList, newTextFile, content);
		}
		else if(commandType.equalsIgnoreCase("delete")) {
			feedback = deleteFromFile(arrayList, content ,newTextFile);
		}
		else if(commandType.equalsIgnoreCase("search")) {
			feedback = searchFromFile(content, newTextFile);
		}
		else if(commandType.equalsIgnoreCase("sort")) {
			feedback = sortItemInFile(arrayList, newTextFile);
		}
		else {
			feedback = String.format(MESSAGE_INVALID_COMMAND, userCommand);
		}
		return feedback;
	}
	
	// sort item in file alphabetically and case insensitively 
	private static String sortItemInFile(ArrayList<String> arrayList, File newTextFile) {
		Collections.sort(arrayList,String.CASE_INSENSITIVE_ORDER);
		writeToFile(arrayList, newTextFile);
		return "lines are sorted alphabetically";
	}

	// support multiple existence of search item
	private static String searchFromFile(String content, File file) {
		if(content.length()==0) {
			return "Cannot search nothing. Retry";
		}
		String str= new String();
		StringBuilder sb = new StringBuilder();
		try{		
			BufferedReader br = new BufferedReader(new FileReader(file.getName()));
			while((str=br.readLine())!=null) {
				if(str.split(" ")[1].toLowerCase().contains(content.toLowerCase())) {
					sb.append(str);
					sb.append('\n');				
				}
			}			
			br.close();
			if(sb.length()!=0) {
				return sb.toString().trim();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return "No such item";
	}

	private static void feedbackToUser(String feedback) {
		System.out.println(feedback);
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
		return String.format(MESSAGE_FILE_CLEARED, file.getName());
	}
	
	private static String addToFile(ArrayList<String> arrayList, File file, String content) {
		if(content.length()==0) {
			return "Nothing Added";
		}
		arrayList.add(content);
		writeToFile(arrayList, file);
		return String.format(MESSAGE_ITEM_ADDED, file.getName(), content);
	}
		
	private static String display(File file) {
		if(fileIsEmpty(file)) {
			return String.format(MESSAGE_FILE_EMPTY, file.getName());
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
			BufferedReader br = new BufferedReader(new FileReader(file.getName()));
			String str;
			while((str=br.readLine())!=null) {
				content.add(getSecondWord(str).trim());
			}
			br.close();
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
	private static String deleteFromFile(ArrayList<String> list, String content , File file) {
		if(content.length()==0) {
			return "Nothing Deleted";
		}
		if(isNumeric(content)) {
			int i = Integer.valueOf(content);
			if (i<=list.size() && i>0) {
				String removedStr = list.get(i-1);
				list.remove(i-1);
				writeToFile(list, file);
				return String.format(MESSAGE_ITEM_DELETED, file.getName(), removedStr);
			
			}
		}
		return String.format(MESSAGE_INDEX_NOT_EXIST, content);
	}

// return true if string is numeric string including 0	
	private static boolean isNumeric(String str) {
		String regex = "^[0-9]";
		return str.matches(regex);
	}
	
} 