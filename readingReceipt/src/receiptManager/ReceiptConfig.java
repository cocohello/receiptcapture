package receiptManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ReceiptConfig {
	// Return full path to Receipt directory
			public ArrayList<String> GetReceiptFolder(String[] receiptList) {
				
				File folder = new File(receiptList[0]);
				System.out.println(folder);
				ArrayList<String> rcptPathList = new ArrayList<>();
				
				if (receiptList.length == 1) {
					File[] listOfFiles = folder.listFiles();
					for (File file : listOfFiles) {
					    if (file.isFile()) {
					        rcptPathList.add(file.getAbsolutePath());
					    }
					}
				}else{
					System.err.println("Parameter of receiptList is null.");
					System.exit(1);
				}
				 return rcptPathList;			    	
			}

}
