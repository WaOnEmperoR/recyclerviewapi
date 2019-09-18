package id.govca.recyclerviewapi.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import id.govca.recyclerviewapi.dao.DAOAccess;
import id.govca.recyclerviewapi.helper.DatabaseClient;

public class FavoriteProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String AUTHORITY = "id.govca.recyclerviewapi";
    public static final String TABLE_NAME = "favorite";
    private static final String SCHEME = "content";

    // Base content yang digunakan untuk akses content provider
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build();

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, 1);
    }

    private DAOAccess daoAccess;

    @Override
    public boolean onCreate() {
        daoAccess = DatabaseClient.getInstance(getContext())
                .getAppDatabase()
                .getFavoriteDAO();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case 1:
                cursor = daoAccess.fetchFavoriteMoviesCursor();
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
