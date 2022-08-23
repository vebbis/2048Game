package tyvefortiatte;

import java.io.FileNotFoundException;
import java.util.List;

public interface ISaveHandler {
    public void save(String filename, Game game) throws FileNotFoundException;

	public Game load(String filename) throws FileNotFoundException;

    public List<String> getFileDirectory();

}
