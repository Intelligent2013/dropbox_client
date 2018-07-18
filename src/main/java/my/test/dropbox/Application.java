package my.test.dropbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.Banner;    
import org.springframework.core.env.Environment;
/**
 *
 * @author Alexander Dyuzhev
 */
@SpringBootApplication
public class Application implements CommandLineRunner {
    
    @Autowired
    private Environment env;
    
    public static void main(String[] args) throws Exception {

        SpringApplication app = new SpringApplication(Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
        	
    }

    @Override
    public void run(String... args) throws Exception {
	
        DropBoxClient dropBoxClient = new DropBoxClient(env.getProperty("dropbox.accesstoken"));
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("download")) {                
                dropBoxClient.downloadFile(args[1]);
            } else {
                dropBoxClient.dir(args[0], true);                
            }
        } else {
            dropBoxClient.dir("", false);            
        }
        System.exit(0);		
    }
}
