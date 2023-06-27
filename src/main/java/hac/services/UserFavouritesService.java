package hac.services;

import hac.repo.Favourite;

import java.util.List;

public interface UserFavouritesService {
    //TODO : delete public?
    public void deleteUserFavourite(Long jokeId, Long userId) throws Exception;
    public Integer getNumOfUserFavourites(Long userId);
    public void saveUserFavourite(Long jokeId, Long userId) throws Exception;
    public List<Favourite> getUserFavouritesData(int limit, int offset, Long userId) throws Exception;
    public Boolean isFavourite(Long jokeId, Long userId);
}
