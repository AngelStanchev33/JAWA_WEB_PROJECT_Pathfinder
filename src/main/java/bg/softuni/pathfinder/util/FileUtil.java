package bg.softuni.pathfinder.util;

import org.springframework.stereotype.Component;

@Component
public class FileUtil {
    
    public String getUniqueName(String originalFilename) {
        String uniqueName = java.util.UUID.randomUUID().toString() + "_" + originalFilename;
        return uniqueName;
    }

    

  
} 