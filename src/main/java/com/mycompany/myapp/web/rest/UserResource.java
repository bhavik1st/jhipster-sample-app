package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.web.rest.dto.ExternalAccountDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;
    
    @Inject
    private UsersConnectionRepository usersConnectionRepository;

    /**
     * GET  /users -> get all users.
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<User> getAll() {
        log.debug("REST request to get all Users");
        return userRepository.findAll();
    }

    /**
     * GET  /users/:login -> get the "login" user.
     */
    @RequestMapping(value = "/users/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<User> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return userRepository.findOneByLogin(login)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * GET  /users/:login/connections -> get the "login" user's social connections.
     */
    @RequestMapping(value = "/users/{login}/connections",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    List<String> getUserConnections(@PathVariable String login) {
    	log.debug("REST request to get User's social connections : {}", login);
    	
    	List<String> connections = new ArrayList<>();
    	MultiValueMap<String, Connection<?>> allConnections = usersConnectionRepository.createConnectionRepository(login).findAllConnections();
    	for (Entry<String, List<Connection<?>>> entry : allConnections.entrySet()) {
    		for (Connection<?> connect : entry.getValue()) {
    			connections.add(connect.getDisplayName() + "@" + entry.getKey());
    		}
		}
    	
    	return connections;
    }
    
    /**
     * DELETE  /users/:login/connections/{providerId} -> delete the "login" user's social connections.
     */
    @RequestMapping(value = "/users/{login}/connections/{providerId}",
    		method = RequestMethod.DELETE)
    @Timed
    void deleteUserConnections(@PathVariable String login, @PathVariable String providerId) {
    	log.debug("REST request to delete User ({})'s social connection : {}", login, providerId);
    	
    	usersConnectionRepository.createConnectionRepository(login).removeConnections(providerId);;
    }
}
