package com.klachkova.locationsystem.services;

import com.klachkova.locationsystem.modeles.*;
import com.klachkova.locationsystem.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@Transactional(readOnly = true)
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationAccessService locationAccessService;
    private final UserService userService;


    @Autowired
    public LocationService(LocationRepository locationRepository,
                           LocationAccessService locationAccessService,
                           UserService userService) {
        this.locationRepository = locationRepository;
        this.locationAccessService = locationAccessService;
        this.userService = userService;
    }

    @Transactional
    public void registerLocation(Location location) {
        checkRelevanceLocation(location);
        locationRepository.save(location);
    }

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Location findById(int id) {

        return locationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Location with ID " + id + " not found"));
    }

    public Location findByAddress(String address) {

        return locationRepository.findByAddress(address)
                .orElseThrow(() -> new NoSuchElementException("Location with address " + address + " not found"));
    }

    public boolean existsByAddress(String address) {

        return locationRepository.existsByAddress(address);
    }

    public void checkRelevanceLocation(Location locationDetails) {

        String ownerEmail = locationDetails.getOwner().getEmail();
        locationDetails.setOwner(userService.findByEmail(ownerEmail));

    }

    public List<Location> getAllSharedLocations(User user) {

        return locationAccessService.getAllSharedLocations(user);
    }

    public List<User> getFriendsWithAccessToLocation(int locationId) {
        return locationAccessService.getFriendsWithAccess(locationId);
    }


}
