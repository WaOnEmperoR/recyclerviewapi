package id.govca.recyclerviewapi.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import id.govca.recyclerviewapi.entity.Favorite;

@Dao
public interface DAOAccess {

    @Insert
    Long insertFavorite(Favorite favorite);

    @Query("SELECT * FROM favorite WHERE type = 0")
    LiveData<List<Favorite>> fetchFavoriteMovies();

    @Query("SELECT * FROM favorite WHERE type = 1")
    LiveData<List<Favorite>> fetchFavoriteTVShows();

    @Query("SELECT count(*) FROM favorite WHERE type = 0 AND favId = :favId")
    int checkFavoriteMovie(int favId);

    @Query("SELECT count(*) FROM favorite WHERE type = 1 AND favId = :favId")
    int checkFavoriteTVShow(int favId);

    @Query("SELECT count(*) FROM favorite WHERE type = :type AND thingsId = :thingsId")
    int checkFavorite(int type, int thingsId);

    @Update
    void updateFavorite(Favorite favorite);

    @Delete
    void deleteFavorite(Favorite favorite);

    @Query("DELETE FROM favorite WHERE type = :type AND thingsId = :thingsId")
    void deleteFavorite(int type, int thingsId);
}
