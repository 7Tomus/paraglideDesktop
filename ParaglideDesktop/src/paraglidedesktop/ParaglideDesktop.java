package paraglidedesktop;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author Tomus
 */

public class ParaglideDesktop implements NativeKeyListener{

    private     long            startTime   ;
    private     boolean         start       = false;
    private     List<String>    values,collected      ;
    private     int             valueNumber = 0;
    
    public static void main(String[] args) {
        
                File responsesCSV = new File("responses.csv");
                if(!responsesCSV.exists()){
                    try {
                        responsesCSV.createNewFile();
                    } catch (IOException ex) {
                        System.err.println("Cannot create responses file.");
                    }
                }
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			System.exit(1);
		}

		GlobalScreen.addNativeKeyListener(new ParaglideDesktop());
                Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
                logger.setLevel(Level.OFF);
                logger.setUseParentHandlers(false);
	}
    
    public void nativeKeyPressed(NativeKeyEvent e) {
		System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                    try{
			GlobalScreen.unregisterNativeHook();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
		}
                
                if(e.getKeyCode() == NativeKeyEvent.VC_K){
                    valueNumber++;
                    /*
                    values.add(new String(valueNumber + "|\t" + ((System.currentTimeMillis() - startTime)/1000f)));
                    */
                    values.add(new String(((System.currentTimeMillis() - startTime)/1000f)+""));
                }
                
                if (e.getKeyCode() == NativeKeyEvent.VC_SPACE) {
                    if(!start){
                        valueNumber = 0;
                        startTime = System.currentTimeMillis();
                        values = new ArrayList<>();
                        collected = new ArrayList<>();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        /*
                        values.add("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
                        values.add(dateFormat.format(date));
                        values.add("░░░░░░░░░░░░░░░░░░░");
                        */
                        values.add(dateFormat.format(date));
                    }    
                    start = !start;
                    if(!start){
                        /*
                        values.add("");
                        for(int i = 0; i<values.size() - 1; i++){
                            System.out.print(values.get(i));
                        }
                        */
                        String collect;
                        collect = values.stream().collect(Collectors.joining(","));
                        collected.add(collect);
                        Path file = Paths.get("responses.csv");
                        try {
                            Files.write(file, collected, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
                            //Files.write(file, values, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		//System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		
	}    
}