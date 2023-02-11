package chorestogetherapiservice.handler

import chorestogetherapiservice.domain.ImmutableResponseEntity
import chorestogetherapiservice.domain.ResponseEntity
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


class ResponseHandlerSpec extends Specification {

    private final int HTTP_OK_STATUS_CODE = 200
    private final int HTTP_BAD_REQUEST_ERROR_STATUS_CODE = 400
    private final int HTTP_NO_RESOURCE_FOUND_STATUS_CODE = 404
    private final int HTTP_FAIL_STATUS_CODE = 500
    private final String SUCCESS_STATUS = "success"
    private final String ERROR_STATUS = "error"
    private final String FAIL_STATUS = "fail"

    @Subject
    def responseHandler = new ResponseHandler()

    @Shared
    def dataMock = new HashMap<String, String>()
    @Shared
    def messageMock = "message"

    def "test generating success response without data"() {
        when:
        def expectedResponseEntity = ImmutableResponseEntity.builder().status(SUCCESS_STATUS).build()
        def expectedResult = Response
                .status(HTTP_OK_STATUS_CODE)
                .entity(expectedResponseEntity)
                .type(MediaType.APPLICATION_JSON)
                .build()
        def result = responseHandler.generateSuccessResponse()

        then:
        expectedResult.status == result.status
        expectedResult.mediaType == result.mediaType
        def entityResult = result.entity as ResponseEntity
        def expectedEntity = expectedResult.entity as ResponseEntity
        entityResult.getStatus() == expectedEntity.getStatus()
        entityResult.getMessage() == expectedEntity.getMessage()
        entityResult.getData() == expectedEntity.getData()
    }

    def "test generating success response with data"() {
        given:
        dataMock.put("fake", "data")

        when:
        def expectedResponseEntity = ImmutableResponseEntity.builder().status(SUCCESS_STATUS).data(dataMock).build()
        def expectedResult = Response
                .status(HTTP_OK_STATUS_CODE)
                .entity(expectedResponseEntity)
                .type(MediaType.APPLICATION_JSON)
                .build()
        def result = responseHandler.generateSuccessResponseWith(dataMock)

        then:
        expectedResult.status == result.status
        expectedResult.mediaType == result.mediaType
        def entityResult = result.entity as ResponseEntity
        def expectedEntity = expectedResult.entity as ResponseEntity
        entityResult.getStatus() == expectedEntity.getStatus()
        entityResult.getMessage() == expectedEntity.getMessage()
        entityResult.getData() == expectedEntity.getData()
    }

    def "test success of generating bad request error response"() {
        when:
        def expectedResponseEntity = ImmutableResponseEntity.builder().status(ERROR_STATUS).message(messageMock).build()
        def expectedResult = Response
                .status(HTTP_BAD_REQUEST_ERROR_STATUS_CODE)
                .entity(expectedResponseEntity)
                .type(MediaType.APPLICATION_JSON)
                .build()
        def result = responseHandler.generateBadRequestErrorResponseWith(messageMock)

        then:
        expectedResult.status == result.status
        expectedResult.mediaType == result.mediaType
        def entityResult = result.entity as ResponseEntity
        def expectedEntity = expectedResult.entity as ResponseEntity
        entityResult.getStatus() == expectedEntity.getStatus()
        entityResult.getMessage() == expectedEntity.getMessage()
        entityResult.getData() == expectedEntity.getData()
    }

    def "test success of generating no resource found error response"() {
        when:
        def expectedResponseEntity = ImmutableResponseEntity.builder().status(ERROR_STATUS).message(messageMock).build()
        def expectedResult = Response
                .status(HTTP_NO_RESOURCE_FOUND_STATUS_CODE)
                .entity(expectedResponseEntity)
                .type(MediaType.APPLICATION_JSON)
                .build()
        def result = responseHandler.generateResourceNotFoundErrorResponseWith(messageMock)

        then:
        expectedResult.status == result.status
        expectedResult.mediaType == result.mediaType
        def entityResult = result.entity as ResponseEntity
        def expectedEntity = expectedResult.entity as ResponseEntity
        entityResult.getStatus() == expectedEntity.getStatus()
        entityResult.getMessage() == expectedEntity.getMessage()
        entityResult.getData() == expectedEntity.getData()
    }

    def "test success of generating fail response"() {
        when:
        def expectedResponseEntity = ImmutableResponseEntity.builder().status(FAIL_STATUS).message(messageMock).build()
        def expectedResult = Response
                .status(HTTP_FAIL_STATUS_CODE)
                .entity(expectedResponseEntity)
                .type(MediaType.APPLICATION_JSON)
                .build()
        def result = responseHandler.generateFailResponseWith(messageMock)

        then:
        expectedResult.status == result.status
        expectedResult.mediaType == result.mediaType
        def entityResult = result.entity as ResponseEntity
        def expectedEntity = expectedResult.entity as ResponseEntity
        entityResult.getStatus() == expectedEntity.getStatus()
        entityResult.getMessage() == expectedEntity.getMessage()
        entityResult.getData() == expectedEntity.getData()
    }

    def "test success of generating unsuccessful(error&failure) response"() {
        when:
        def expectedResponseEntity = ImmutableResponseEntity.builder().status(expectedEntityStatusMessage).message(messageMock).build()
        def expectedResult = Response
                .status(expectedStatusCode)
                .entity(expectedResponseEntity)
                .type(MediaType.APPLICATION_JSON)
                .build()
        def result = responseHandler.generateUnsuccessfulResponseWith(messageMock, statusCode)

        then:
        expectedResult.status == result.status
        expectedResult.mediaType == result.mediaType
        def entityResult = result.entity as ResponseEntity
        def expectedEntity = expectedResult.entity as ResponseEntity
        entityResult.getStatus() == expectedEntity.getStatus()
        entityResult.getMessage() == expectedEntity.getMessage()
        entityResult.getData() == expectedEntity.getData()

        where:
        statusCode | expectedStatusCode | expectedEntityStatusMessage
        400        | 400                | "error"
        404        | 404                | "error"
        500        | 500                | "fail"
    }
}
