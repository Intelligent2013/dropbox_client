package my.test.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.GetMetadataErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Alexander Dyuzhev
 */
public class DropBoxClient {
   
    DbxClientV2 client;
    
    final String downloadPathLocation = "download" + File.separator;
    
    public DropBoxClient (String ACCESS_TOKEN)  throws DbxException, IOException {        
        // Create Dropbox client        
        DbxRequestConfig dbconfig = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        client = new DbxClientV2(dbconfig, ACCESS_TOKEN);        
    }
    
    public void downloadFile(String filepath) throws DbxException
    {   
        try
        {   
            if (this.isFileExist(filepath)) {            
                File file = new File(downloadPathLocation + filepath);
                file.getParentFile().mkdirs();                
                try
                (                       
                    OutputStream downloadFile = new FileOutputStream(file)) {
                    System.out.println("Downloading file " + filepath +  "...");
                    FileMetadata metadata = client.files().downloadBuilder(filepath).download(downloadFile);
                }     
            }
        }
        //exception handled
        catch (DbxException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();        
        }         
    }
    
    public boolean isFileExist(String dropboxPath) throws DbxException {        
        try {            
            client.files().getMetadata(dropboxPath);
            return true;
        } catch (GetMetadataErrorException e){            
        }
        System.out.println("File/folder " + dropboxPath + " doesn't exist");
        return false;
    }
    
    public void dir(String path, boolean recurse) throws DbxException {
     
        // Get files and folder metadata from Dropbox directory 'path'
        ListFolderResult result = null;
        try {
            result = client.files().listFolder(path);
            while (true) {
                for (Metadata metadata : result.getEntries()) {                                    
                    System.out.println(metadata.getPathLower());
                    if (recurse) {
                        dir(metadata.getPathLower(), recurse);
                    }
                }

                if (!result.getHasMore()) {
                    break;
                }

                result = client.files().listFolderContinue(result.getCursor());
            }
        } catch (Exception ex) {
            //is not a folder
        }
    }
}
