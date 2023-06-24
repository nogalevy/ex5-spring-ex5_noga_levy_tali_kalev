package hac.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    List<Favourite> findFavouritesByUserInfo_Id(Long userInfoId, Pageable pageable);
    Favourite getFavouriteByJokeIdAndUserInfo_Id(Long jokeId, Long userId);
    Integer countFavouritesByUserInfo_Id(Long userId);

}
