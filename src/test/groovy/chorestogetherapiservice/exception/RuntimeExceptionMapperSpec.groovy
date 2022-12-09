package chorestogetherapiservice.exception


import spock.lang.Specification
import spock.lang.Subject

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

class RuntimeExceptionMapperSpec extends Specification {

    // TODO: find rootcause initializing below exceptions at each when block gives NoPropertyException error for each stub
    def runtimeException = new RuntimeException(EXCEPTION_MESSAGE)
    def webApplicationException = new WebApplicationException(EXCEPTION_MESSAGE, Response.Status.BAD_REQUEST)

    @Subject
    def runtimeExceptionMapper = new RuntimeExceptionMapper()

    private static final String EXCEPTION_MESSAGE = "exceptionMessage"

    def "test when exception is RuntimeException"() {
        when:
        def response = runtimeExceptionMapper.toResponse(runtimeException)
        def expectedResponse = Response
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .entity("RuntimeExceptionMapper raised due to RuntimeException occurred. error message: " +
                        EXCEPTION_MESSAGE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build()

        then:
        response.getEntity() == expectedResponse.getEntity()
        response.getMediaType() == response.getMediaType()
        response.getStatus() == response.getStatus()

        0 * _
    }

    def "test when exception is WebApplicationException"() {
        when:
        def response = runtimeExceptionMapper.toResponse(webApplicationException)
        def expectedResponse = Response
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity("RuntimeExceptionMapper raised due to RuntimeException occurred. error message: " +
                        EXCEPTION_MESSAGE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build()

        then:
        response.getEntity() == expectedResponse.getEntity()
        response.getMediaType() == response.getMediaType()
        response.getStatus() == response.getStatus()

        0 * _
    }
}
