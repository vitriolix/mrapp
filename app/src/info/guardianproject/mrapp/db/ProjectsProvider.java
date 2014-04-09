package info.guardianproject.mrapp.db;

import info.guardianproject.mrapp.model.Auth;
import info.guardianproject.mrapp.model.AuthTable;
import info.guardianproject.mrapp.model.LessonTable;
import info.guardianproject.mrapp.model.MediaTable;
import info.guardianproject.mrapp.model.Project;
import info.guardianproject.mrapp.model.ProjectTable;
import info.guardianproject.mrapp.model.Scene;
import info.guardianproject.mrapp.model.Media;
import info.guardianproject.mrapp.model.Lesson;
import info.guardianproject.mrapp.model.SceneTable;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteQueryBuilder;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

// FIXME rename this to SMProvier and get rid of LessonsProvider
public class ProjectsProvider extends ContentProvider {  
	private StoryMakerDB mDBHelper;
	private SQLiteDatabase mDB = null;
    private String mPassphrase = "foo"; //how and when do we set this??
    
    private static final String AUTHORITY = "info.guardianproject.mrapp.db.ProjectsProvider";
    public static final int PROJECTS = 101;
    public static final int PROJECT_ID = 111;
    public static final int LESSONS = 102;
    public static final int LESSON_ID = 112;
    public static final int MEDIA = 103;
    public static final int MEDIA_ID = 113;
    public static final int SCENES = 104;
    public static final int SCENE_ID = 114;
    public static final int AUTH = 105;
    public static final int AUTH_ID = 115;
    public static final String PROJECTS_BASE_PATH = "projects";
    public static final String SCENES_BASE_PATH = "scenes";
    public static final String LESSONS_BASE_PATH = "lessons";
    public static final String MEDIA_BASE_PATH = "media";
    public static final String AUTH_BASE_PATH = "auth";
    public static final Uri PROJECTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PROJECTS_BASE_PATH);
    public static final Uri SCENES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + SCENES_BASE_PATH);
    public static final Uri LESSONS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + LESSONS_BASE_PATH);
    public static final Uri MEDIA_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + MEDIA_BASE_PATH);
    public static final Uri AUTH_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + AUTH_BASE_PATH);

    public static final String PROJECTS_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/projects";
    public static final String PROJECTS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/projects";
    public static final String SCENES_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/scenes";
    public static final String SCENES_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/scenes";
    public static final String LESSONS_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/lessons";
    public static final String LESSONS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/lessons";
    public static final String MEDIA_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/media";
    public static final String MEDIA_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/media";
    public static final String AUTH_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/auth";
    public static final String AUTH_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/auth";
    
    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    
    static {
        sURIMatcher.addURI(AUTHORITY, PROJECTS_BASE_PATH, PROJECTS);
        sURIMatcher.addURI(AUTHORITY, PROJECTS_BASE_PATH + "/#", PROJECT_ID);
        sURIMatcher.addURI(AUTHORITY, SCENES_BASE_PATH, SCENES);
        sURIMatcher.addURI(AUTHORITY, SCENES_BASE_PATH + "/#", SCENE_ID);
        sURIMatcher.addURI(AUTHORITY, LESSONS_BASE_PATH, LESSONS);
        sURIMatcher.addURI(AUTHORITY, LESSONS_BASE_PATH + "/#", LESSON_ID);
        sURIMatcher.addURI(AUTHORITY, MEDIA_BASE_PATH, MEDIA);
        sURIMatcher.addURI(AUTHORITY, MEDIA_BASE_PATH + "/#", MEDIA_ID);
        sURIMatcher.addURI(AUTHORITY, AUTH_BASE_PATH, AUTH);
        sURIMatcher.addURI(AUTHORITY, AUTH_BASE_PATH + "/#", AUTH_ID);
    }
    
    @Override
    public boolean onCreate() {
        mDBHelper = new StoryMakerDB(getContext()); 
        return true;
    }
    
    private SQLiteDatabase getDB() {
        if (mDB == null) {
            mDB = mDBHelper.getWritableDatabase(mPassphrase);
        }
        return mDB;
    }

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}
    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case PROJECT_ID:
            return (new ProjectTable(getDB())).queryOne(getContext(), uri, projection, selection, selectionArgs, sortOrder);
        case PROJECTS:
            return (new ProjectTable(getDB())).queryAll(getContext(), uri, projection, selection, selectionArgs, sortOrder);
        case SCENE_ID:
            return (new SceneTable(getDB())).queryOne(getContext(), uri, projection, selection, selectionArgs, sortOrder);
        case SCENES:
            return (new SceneTable(getDB())).queryAll(getContext(), uri, projection, selection, selectionArgs, sortOrder);
        case LESSON_ID:
            return (new LessonTable(getDB())).queryOne(getContext(), uri, projection, selection, selectionArgs, sortOrder);
        case LESSONS:
            return (new LessonTable(getDB())).queryAll(getContext(), uri, projection, selection, selectionArgs, sortOrder);
        case MEDIA_ID:
            return (new MediaTable(getDB())).queryOne(getContext(), uri, projection, selection, selectionArgs, sortOrder);
        case MEDIA:
            return (new MediaTable(getDB())).queryAll(getContext(), uri, projection, selection, selectionArgs, sortOrder);
        case AUTH_ID:
            return (new AuthTable(getDB())).queryOne(getContext(), uri, projection, selection, selectionArgs, sortOrder);
        case AUTH:
            return (new AuthTable(getDB())).queryAll(getContext(), uri, projection, selection, selectionArgs, sortOrder);
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long newId;
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case PROJECTS:
            return (new ProjectTable(getDB())).insert(getContext(), uri, values);
        case SCENES:
            return (new SceneTable(getDB())).insert(getContext(), uri, values);
        case LESSONS:
            return (new LessonTable(getDB())).insert(getContext(), uri, values);
        case MEDIA:
            return (new MediaTable(getDB())).insert(getContext(), uri, values);
        case AUTH:
            return (new AuthTable(getDB())).insert(getContext(), uri, values);
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
    }
    
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case PROJECTS:
        case PROJECT_ID:
            return (new ProjectTable(getDB())).delete(getContext(), uri, selection, selectionArgs);
        case SCENES:
        case SCENE_ID:
            return (new SceneTable(getDB())).delete(getContext(), uri, selection, selectionArgs);
        case LESSONS:
        case LESSON_ID:
            return (new LessonTable(getDB())).delete(getContext(), uri, selection, selectionArgs);
        case MEDIA:
        case MEDIA_ID:
            return (new MediaTable(getDB())).delete(getContext(), uri, selection, selectionArgs);
        case AUTH:
        case AUTH_ID:
            return (new AuthTable(getDB())).delete(getContext(), uri, selection, selectionArgs);
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case PROJECTS:
        case PROJECT_ID:
            return (new ProjectTable(getDB())).update(getContext(), uri, values, selection, selectionArgs);
        case SCENES:
        case SCENE_ID:
            return (new SceneTable(getDB())).update(getContext(), uri, values, selection, selectionArgs);
        case LESSONS:
        case LESSON_ID:
            return (new LessonTable(getDB())).update(getContext(), uri, values, selection, selectionArgs);
        case MEDIA:
        case MEDIA_ID:
            return (new MediaTable(getDB())).update(getContext(), uri, values, selection, selectionArgs);
        case AUTH:
        case AUTH_ID:
            return (new AuthTable(getDB())).update(getContext(), uri, values, selection, selectionArgs);
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
    }
    
    /*

    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case PROJECT_ID:
            queryBuilder.setTables(StoryMakerDB.Schema.Projects.NAME);
            queryBuilder.appendWhere(StoryMakerDB.Schema.Projects.ID + "="
                    + uri.getLastPathSegment());
            break;
        case PROJECTS:
            queryBuilder.setTables(StoryMakerDB.Schema.Projects.NAME);
            break;
        case SCENE_ID:
            queryBuilder.setTables(StoryMakerDB.Schema.Scenes.NAME);
            queryBuilder.appendWhere(StoryMakerDB.Schema.Scenes.ID + "="
                    + uri.getLastPathSegment());
            break;
        case SCENES:
            queryBuilder.setTables(StoryMakerDB.Schema.Scenes.NAME);
            break;
        case LESSON_ID:
            queryBuilder.setTables(StoryMakerDB.Schema.Lessons.NAME);
            queryBuilder.appendWhere(StoryMakerDB.Schema.Lessons.ID + "="
                    + uri.getLastPathSegment());
            break;
        case LESSONS:
            queryBuilder.setTables(StoryMakerDB.Schema.Lessons.NAME);
            break;
        case MEDIA_ID:
            queryBuilder.setTables(StoryMakerDB.Schema.Media.NAME);
            queryBuilder.appendWhere(StoryMakerDB.Schema.Media.ID + "="
                    + uri.getLastPathSegment());
            break;
        case MEDIA:
            queryBuilder.setTables(StoryMakerDB.Schema.Media.NAME);
            break;
        case AUTH_ID:
            queryBuilder.setTables(StoryMakerDB.Schema.Auth.NAME);
            queryBuilder.appendWhere(StoryMakerDB.Schema.Auth.ID + "="
                    + uri.getLastPathSegment());
            break;
        case AUTH:
            queryBuilder.setTables(StoryMakerDB.Schema.Auth.NAME);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
        
        Cursor cursor = queryBuilder.query(getDB(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long newId;
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case PROJECTS:
            newId = getDB().insertOrThrow(StoryMakerDB.Schema.Projects.NAME, null, values);
            getContext().getContentResolver().notifyChange(uri, null);
            return PROJECTS_CONTENT_URI.buildUpon().appendPath(PROJECTS_BASE_PATH).appendPath("" + newId).build();
        case SCENES:
            newId = getDB().insertOrThrow(StoryMakerDB.Schema.Scenes.NAME, null, values);
            getContext().getContentResolver().notifyChange(uri, null);
            return SCENES_CONTENT_URI.buildUpon().appendPath(SCENES_BASE_PATH).appendPath("" + newId).build();
        case LESSONS:
            newId = getDB().insertOrThrow(StoryMakerDB.Schema.Lessons.NAME, null, values);
            getContext().getContentResolver().notifyChange(uri, null);
            return LESSONS_CONTENT_URI.buildUpon().appendPath(LESSONS_BASE_PATH).appendPath("" + newId).build();
        case MEDIA:
            newId = getDB().insertOrThrow(StoryMakerDB.Schema.Media.NAME, null, values);
            getContext().getContentResolver().notifyChange(uri, null);
            return MEDIA_CONTENT_URI.buildUpon().appendPath(MEDIA_BASE_PATH).appendPath("" + newId).build();
        case AUTH:
            newId = getDB().insertOrThrow(StoryMakerDB.Schema.Auth.NAME, null, values);
            getContext().getContentResolver().notifyChange(uri, null);
            return AUTH_CONTENT_URI.buildUpon().appendPath(AUTH_BASE_PATH).appendPath("" + newId).build();
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
    }
    
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int count;
        String table;
        switch (uriType) {
        case PROJECTS:
        case PROJECT_ID:
            table = StoryMakerDB.Schema.Projects.NAME;
            break;
        case SCENES:
        case SCENE_ID:
            table = StoryMakerDB.Schema.Scenes.NAME;
            break;
        case LESSONS:
        case LESSON_ID:
            table = StoryMakerDB.Schema.Lessons.NAME;
            break;
        case MEDIA:
        case MEDIA_ID:
            table = StoryMakerDB.Schema.Media.NAME;
            break;
        case AUTH:
        case AUTH_ID:
            table = StoryMakerDB.Schema.Auth.NAME;
            break;
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
        count = getDB().delete(table, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int count;
        String table;
        switch (uriType) {
        case PROJECTS:
        case PROJECT_ID:
            table = StoryMakerDB.Schema.Projects.NAME;
            break;
        case SCENES:
        case SCENE_ID:
            table = StoryMakerDB.Schema.Scenes.NAME;
            break;
        case LESSONS:
        case LESSON_ID:
            table = StoryMakerDB.Schema.Lessons.NAME;
            break;
        case MEDIA:
        case MEDIA_ID:
            table = StoryMakerDB.Schema.Media.NAME;
            break;
        case AUTH:
        case AUTH_ID:
            table = StoryMakerDB.Schema.Auth.NAME;
            break;
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
        count = getDB().update(table, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }     
     */
}
