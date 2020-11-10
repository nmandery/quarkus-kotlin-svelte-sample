package org.acme.commandmode

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/greeting")
class GreetingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun hello() = Greeting("hello")

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    fun hello(@PathParam("name") name: String) = Greeting("Hello $name")
}