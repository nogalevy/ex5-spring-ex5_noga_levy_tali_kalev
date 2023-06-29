package hac.services;

import hac.OffsetBasedPageRequest;
import hac.exceptions.UserNotFound;
import hac.repo.Favourite;
import hac.repo.FavouriteRepository;
import hac.repo.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static hac.utils.Constants.FAVOURITE_DATA_ERROR_MSG;
import static hac.utils.Constants.FAVOURITE_EXIST_ERROR_MSG;

/**
 * User favourites service implementation
 */
@Service
public class UserFavouritesServiceImpl implements UserFavouritesService {
    @Autowired
    private FavouriteRepository favouriteRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    /**
     * delete row in table by joke id and user id
     * @param jokeId long
     * @param userId long
     * @throws Exception if favourite not found
     */
    @Override
    public void deleteUserFavourite(Long jokeId, Long userId) throws Exception {
        Favourite favourite = favouriteRepository.getFavouriteByJokeIdAndUserInfo_Id(jokeId, userId);
        if (favourite == null) {
            throw new Exception(FAVOURITE_DATA_ERROR_MSG);
        }
        favouriteRepository.delete(favourite);
    }

    /**
     * get number of user favourites
     * @param userId long
     * @return Integer
     */
    @Override
    public Integer getNumOfUserFavourites(Long userId) {
        return favouriteRepository.countFavouritesByUserInfo_Id(userId);
    }

    /**
     * save user favourite
     * @param jokeId long
     * @param userId long
     * @throws Exception if favourite already exist or if user not found
     */
    @Override
    public void saveUserFavourite(Long jokeId, Long userId) throws Exception {
        Favourite newFavourite = new Favourite(jokeId);
        Favourite isExist = favouriteRepository.getFavouriteByJokeIdAndUserInfo_Id(jokeId, userId);

        //checks if already exist in db
        if (isExist != null) throw new Exception(FAVOURITE_EXIST_ERROR_MSG);

        Favourite favourite = userInfoRepository.findById(userId).map(user -> {
            newFavourite.setUserInfo(user);
            return favouriteRepository.save(newFavourite);
        }).orElseThrow(() -> new UserNotFound());
    }

    /**
     * get user favourites data
     * @param limit int
     * @param offset int
     * @param userId long
     * @return List of favourites
     */
    public synchronized List<Favourite> getUserFavouritesData(int limit, int offset, Long userId) {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset);
        return favouriteRepository.findFavouritesByUserInfo_Id(userId, pageable);
    }

    /**
     * check if joke is favourite
     * @param jokeId Long
     * @param userId Long
     * @return Boolean
     */
    @Override
    public Boolean isFavourite(Long jokeId, Long userId) {
        Favourite favourite = favouriteRepository.getFavouriteByJokeIdAndUserInfo_Id(jokeId, userId);
        return favourite != null;
    }
}
