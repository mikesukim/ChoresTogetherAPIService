package chorestogetherapiservice.exception.mapper

import chorestogetherapiservice.handler.ResponseHandler
import spock.lang.Specification
import spock.lang.Subject

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

class RuntimeExceptionMapperSpec extends Specification {

    // TODO: find rootcause initializing below exceptions at each when block gives NoPropertyException error for each stub
    def runtimeException = new RuntimeException(EXCEPTION_MESSAGE)
    def webApplicationException = new WebApplicationException(EXCEPTION_MESSAGE, Response.Status.BAD_REQUEST)
    def responseHandlerMock = Mock(ResponseHandler.class)

    @Subject
    def runtimeExceptionMapper = new RuntimeExceptionMapper(responseHandlerMock)

    private static final String EXCEPTION_MESSAGE = "exceptionMessage"

    def "test when exception is RuntimeException"() {
        given:
        def expectedResponse = Response
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .entity("RuntimeExceptionMapper raised due to RuntimeException occurred. error message: " +
                        EXCEPTION_MESSAGE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build()

        when:
        def response = runtimeExceptionMapper.toResponse(runtimeException)

        then:
        response.getEntity() == expectedResponse.getEntity()
        response.getMediaType() == response.getMediaType()
        response.getStatus() == response.getStatus()

        1 * responseHandlerMock.generateUnsuccessfulResponseWith( _, Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) >> expectedResponse
        0 * _
    }

    def "test when exception is WebApplicationException"() {
        given:
        def expectedResponse = Response
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity("RuntimeExceptionMapper raised due to RuntimeException occurred. error message: " +
                        EXCEPTION_MESSAGE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build()

        when:
        def response = runtimeExceptionMapper.toResponse(webApplicationException)

        then:
        response.getEntity() == expectedResponse.getEntity()
        response.getMediaType() == response.getMediaType()
        response.getStatus() == response.getStatus()

        1 * responseHandlerMock.generateUnsuccessfulResponseWith(_, Response.Status.BAD_REQUEST.getStatusCode()) >> expectedResponse
        0 * _
    }
}
