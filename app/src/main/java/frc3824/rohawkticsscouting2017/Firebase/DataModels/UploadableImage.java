package frc3824.rohawkticsscouting2017.Firebase.DataModels;

/**
 * @author frc3824
 * Created: 3/21/17
 */

public class UploadableImage {

    public String filepath;
    public boolean remote;

    public UploadableImage(){
        filepath = "";
        remote = false;
    }

    public UploadableImage(String filepath){
        this.filepath = filepath;
    }

    public UploadableImage(String filepath, boolean remote){
        this.filepath = filepath;
        this.remote = remote;
    }
}
