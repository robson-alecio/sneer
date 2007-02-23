package spikes.gandhi.dirsync;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryUtil {
    
    private DirectoryUtil() {
    }
    
    public static List<FileInfo> list(String path){
        List<FileInfo> result=new ArrayList<FileInfo>();
        fillList(result,path,path);
        return result;
    }
    
    private static void fillList(List<FileInfo> result, String currentPath, String originalPath){
        File file=new File(currentPath);
        if (!file.exists())
            throw new RuntimeException("Directory does not exist: " + file.getAbsolutePath());
        for(File temp:file.listFiles()){
            if (temp.isDirectory()){
                fillList(result,temp.getPath(),originalPath);
            }else{
                String path=temp.getPath().substring(originalPath.length());
                result.add(new FileInfo(path,temp.length(),temp.lastModified()));
            }
        }
        
    }
    
    public static List<FileInfo> diff(List<FileInfo> list1,List<FileInfo> list2){
        List<FileInfo> result=new ArrayList<FileInfo>();
        for(FileInfo item:list1){ 
            if (!list2.contains(item)){
                System.out.println("different: "+item.getPath());
                result.add(item);
            }
        }
        return result;
    }
    
}
