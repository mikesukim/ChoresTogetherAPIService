package chorestogetherapiservice.exception.mapper

import chorestogetherapiservice.handler.ResponseHandler
import spock.lang.Specification
import spock.lang.Subject

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

class ConstraintViolationExceptionMapperSpec extends Specification {

    def responseHandlerMock = Mock(ResponseHandler.class)

    @Subject
    def constraintViolationExceptionMapper = new ConstraintViolationExceptionMapper(responseHandlerMock)

    def constraintViolationMock = Mock(ConstraintViolation)
    Set<ConstraintViolation> constraintViolations = new HashSet<ConstraintViolation>([constraintViolationMock].toSet())
    def constraintViolationException = new ConstraintViolationException(constraintViolations)

    def "test when constraintViolationException occurred"() {
        given :
        def expectedResponse = Response
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .entity("ConstraintViolationExceptionMapper raised due to constraint violated. Violations :")
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build()

        when:
        def response = constraintViolationExceptionMapper.toResponse(constraintViolationException)

        then:
        response.getEntity().toString().contains(expectedResponse.getEntity().toString())
        response.getMediaType() == response.getMediaType()
        response.getStatus() == response.getStatus()

        1 * responseHandlerMock.generateBadRequestErrorResponseWith(_) >> expectedResponse
        0 * _
    }
}
