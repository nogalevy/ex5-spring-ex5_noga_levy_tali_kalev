package hac.services;

import hac.repo.FavouriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFavouritesServiceImpl implements UserFavouritesService {
    @Autowired
    private FavouriteRepository favouriteRepository;
}
