package info.guardianproject.mrapp.model;

import net.sqlcipher.database.SQLiteDatabase;
import info.guardianproject.mrapp.db.ProjectsProvider;
import info.guardianproject.mrapp.db.StoryMakerDB;
import android.net.Uri;

public class ProjectTable extends Table {
    private final static String TAG = "ProjectTable";
    
    public ProjectTable() {
        
    }
    
    public ProjectTable(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected String getTableName() {
        return StoryMakerDB.Schema.Projects.NAME;
    }
    
    @Override
    protected String getIDColumnName() {
        return StoryMakerDB.Schema.Projects.ID;
    }

    @Override
    protected Uri getURI() {
        return ProjectsProvider.PROJECTS_CONTENT_URI;
    }

    @Override
    protected String getProviderBasePath() {
        return ProjectsProvider.PROJECTS_BASE_PATH;
    }
}
