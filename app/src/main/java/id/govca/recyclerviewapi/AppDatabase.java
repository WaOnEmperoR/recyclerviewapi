package id.govca.recyclerviewapi;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import id.govca.recyclerviewapi.dao.DAOAccess;
import id.govca.recyclerviewapi.entity.Favorite;

@Database(entities = {Favorite.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DAOAccess getFavoriteDAO();

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
