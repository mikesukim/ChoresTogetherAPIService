package chorestogetherapiservice.exception.mapper

import chorestogetherapiservice.exception.mapper.ConstraintViolationExceptionMapper
import spock.lang.Specification
import spock.lang.Subject

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

class ConstraintViolationExceptionMapperSpec extends Specification {

    @Subject
    def constraintViolationExceptionMapper = new ConstraintViolationExceptionMapper()

    def constraintViolationMock = Mock(ConstraintViolation)
    Set<ConstraintViolation> constraintViolations = new HashSet<ConstraintViolation>([constraintViolationMock].toSet())
    def constraintViolationException = new ConstraintViolationException(constraintViolations)

    def "test when constraintViolationException occurred"() {
        when:
        def response = constraintViolationExceptionMapper.toResponse(constraintViolationException)
        def expectedResponse = Response
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .entity("ConstraintViolationExceptionMapper raised due to constraint violated. Violations :")
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build()

        then:
        response.getEntity().toString().contains(expectedResponse.getEntity().toString())
        response.getMediaType() == response.getMediaType()
        response.getStatus() == response.getStatus()

        0 * _
    }
}
