package hac.services;

import hac.repo.Favourite;

import java.util.List;

public interface UserFavouritesService {
    void deleteUserFavourite(Long jokeId, Long userId) throws Exception;
    Integer getNumOfUserFavourites(Long userId);
    void saveUserFavourite(Long jokeId, Long userId) throws Exception;
    List<Favourite> getUserFavouritesData(int limit, int offset, Long userId);
    Boolean isFavourite(Long jokeId, Long userId);
}
