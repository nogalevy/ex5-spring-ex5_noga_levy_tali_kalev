package hac.services;

import hac.OffsetBasedPageRequest;
import hac.repo.Favourite;
import hac.repo.FavouriteRepository;
import hac.repo.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFavouritesServiceImpl implements UserFavouritesService {
    @Autowired
    private FavouriteRepository favouriteRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public void deleteUserFavourite(Long jokeId, Long userId) throws Exception {
        Favourite favourite = favouriteRepository.getFavouriteByJokeIdAndUserInfo_Id(jokeId, userId);
        if (favourite == null) {
            throw new Exception("Cannot find favourite with the given jokeId and userId");
        }
        favouriteRepository.delete(favourite);
    }

    @Override
    public Integer getNumOfUserFavourites(Long userId) {
        //NOGA: when to throw Exception ?
        return favouriteRepository.countFavouritesByUserInfo_Id(userId);

    }

    @Override
    public void saveUserFavourite(Long jokeId, Long userId) throws Exception {
        Favourite newFavourite = new Favourite(jokeId);
        Favourite isExist = favouriteRepository.getFavouriteByJokeIdAndUserInfo_Id(jokeId, userId);

        //checks if already exist in db
        if (isExist != null) throw new Exception("favourite already exist");

        Favourite favourite = userInfoRepository.findById(userId).map(user -> {
            newFavourite.setUserInfo(user);
            return favouriteRepository.save(newFavourite);
        }).orElseThrow(() -> new Exception("sorry"));
    }

    //NOGA: move to services?
    public synchronized List<Favourite> getUserFavouritesData(int limit, int offset, Long userId) throws Exception {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset);
        return favouriteRepository.findFavouritesByUserInfo_Id(userId, pageable);
    }

    @Override
    public Boolean isFavourite(Long jokeId, Long userId) throws Exception {
        Favourite favourite = favouriteRepository.getFavouriteByJokeIdAndUserInfo_Id(jokeId, userId);
        return favourite != null;

    }
}