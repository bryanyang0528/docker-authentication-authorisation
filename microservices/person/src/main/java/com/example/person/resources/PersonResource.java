package com.example.person.resources;

import com.example.api.model.Person;
import com.example.person.dao.PersonDao;
import com.example.person.filters.annotations.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/persons")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class PersonResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonResource.class);

    PersonDao personDao;

    public PersonResource(PersonDao personDao) {
        this.personDao = personDao;
    }

    @GET
    @Secured(permission = "personView")
    public List<Person> getAll(@HeaderParam("callerId") String callerId){
        LOGGER.info("Retrieving all persons for callerId: " + callerId);

        return personDao.getAll();
    }

    @GET
    @Path("/{id}")
    @Secured(permission = "personView")
    public Person get(@HeaderParam("callerId") String callerId, @PathParam("id") Integer id){
        LOGGER.info("Retrieving person" + id + " for callerId: " + callerId);

        return personDao.findById(id);
    }

    @POST
    @Secured(permission = "personAdd")
    public Person add(@HeaderParam("callerId") String callerId, @Valid Person person) {
        LOGGER.info("Adding a person for callerId: " + callerId);

        int newId = personDao.insert(person);
        person.setId(newId);
        return person;
    }

    @PUT
    @Path("/{id}")
    @Secured(permission = "personEdit")
    public Person edit(@HeaderParam("callerId") String callerId, @PathParam("id") Integer id, @Valid Person person) {
        LOGGER.info("Editing person " + id + " for callerId: " + callerId);

        person.setId(id);
        personDao.update(person);

        return person;
    }

    @DELETE
    @Path("/{id}")
    @Secured(permission = "personDelete")
    public void delete(@HeaderParam("callerId") String callerId, @PathParam("id") Integer id) {
        LOGGER.info("Deleting person " + id + " for callerId: " + callerId);
        personDao.deleteById(id);
    }
}