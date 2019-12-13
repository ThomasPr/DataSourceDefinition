package demo.message.boundary;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import demo.message.entity.Message;


@Path("/messages")
@Stateless
public class MessagesResource {

  @PersistenceContext()
  private EntityManager em;

  @Path("/")
  @GET
  public List<Message> all() {
    return em.createQuery("SELECT m FROM Message m", Message.class).getResultList();
  }

  @Path("/id}")
  @GET
  public Message find(Long id) {
    return em.find(Message.class, id);
  }

  @POST
  public Response save(Message message, @Context UriInfo uriInfo) {
    em.persist(message);
    return Response.ok().build();
  }
}
