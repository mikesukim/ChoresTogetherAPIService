package chorestogetherapiservice.exception.mapper

import chorestogetherapiservice.handler.ResponseHandler
import com.fasterxml.jackson.databind.JsonMappingException
import spock.lang.Specification
import spock.lang.Subject

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

class JsonMappingExceptionMapperSpec extends Specification {

    def responseHandlerMock = Mock(ResponseHandler.class)

    @Subject
    def jsonMappingExceptionMapper = new JsonMappingExceptionMapper(responseHandlerMock)

    def "test when JsonMappingException occurred"() {
        given :
        def expectedResponse = Response
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity("JsonMappingException raised")
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build()

        when:
        def response = jsonMappingExceptionMapper
                .toResponse(new JsonMappingException("JsonMappingException raised"))

        then:
        response.getEntity().toString().contains(expectedResponse.getEntity().toString())
        response.getMediaType() == response.getMediaType()
        response.getStatus() == response.getStatus()

        1 * responseHandlerMock.generateBadRequestErrorResponseWith(_) >> expectedResponse
        0 * _
    }
}
