package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels;

/**
 * @author Andrew Messing
 *         Created: 9/6/16
 */


public class NoteView {

    private final static String TAG = "NoteView";

    public enum NoteType
    {
        MATCH,
        SUPER,
        DRIVE_TEAM
    }

    public NoteType note_type;
    public int match_number;
    public int team_number;
    public String note;

    public static String toString(NoteType nt)
    {
       switch (nt)
       {
           case MATCH:
               return "Match";
           case SUPER:
               return "Super";
           case DRIVE_TEAM:
               return "Drive Team";
       }
        return "";
    }

}
