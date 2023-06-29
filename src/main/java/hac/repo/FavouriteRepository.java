package hac.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Favourite repository saves User's favourite jokes by id
 */
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    List<Favourite> findFavouritesByUserInfo_Id(Long userInfoId, Pageable pageable);

    Favourite getFavouriteByJokeIdAndUserInfo_Id(Long jokeId, Long userId);

    Integer countFavouritesByUserInfo_Id(Long userId);
}
