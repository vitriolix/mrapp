package info.guardianproject.mrapp.model;

import net.sqlcipher.database.SQLiteDatabase;
import info.guardianproject.mrapp.db.ProjectsProvider;
import info.guardianproject.mrapp.db.StoryMakerDB;
import android.net.Uri;

public class SceneTable extends Table {
    private final static String TAG = "SceneTable";
    
    public SceneTable() {
        
    }
    
    public SceneTable(SQLiteDatabase db) {
        super(db);
    }
    
    @Override
    protected String getTableName() {
        return StoryMakerDB.Schema.Scenes.NAME;
    }
    
    @Override
    protected String getIDColumnName() {
        return StoryMakerDB.Schema.Scenes.ID;
    }

    @Override
    protected Uri getURI() {
        return ProjectsProvider.SCENES_CONTENT_URI;
    }

    @Override
    protected String getProviderBasePath() {
        return ProjectsProvider.SCENES_BASE_PATH;
    }
}
