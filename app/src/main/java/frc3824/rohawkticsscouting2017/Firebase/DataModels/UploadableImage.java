package frc3824.rohawkticsscouting2017.Firebase.DataModels;

/**
 * @author frc3824
 * Created: 3/21/17
 */

public class UploadableImage {

    public String filepath;
    public String url;

    public UploadableImage(){
        filepath = "";
        url = "";
    }

    public UploadableImage(String filepath){
        this.filepath = filepath;
    }

    public UploadableImage(String filepath, String url){
        this.filepath = filepath;
        this.url = url;
    }
}
