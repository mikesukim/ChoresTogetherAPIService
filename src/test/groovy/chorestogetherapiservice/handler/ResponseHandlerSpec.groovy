package chorestogetherapiservice.handler

import chorestogetherapiservice.domain.JsonResponse
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import javax.ws.rs.core.Response


class ResponseHandlerSpec extends Specification {

    private final int HTTP_OK_STATUS_CODE = 200
    private final int HTTP_BAD_REQUEST_ERROR_STATUS_CODE = 400
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
        def expectedJsonResponse = new JsonResponse(SUCCESS_STATUS, Optional.empty(), Optional.empty())
        def expectedResult = Response
                .status(HTTP_OK_STATUS_CODE)
                .entity(
                        expectedJsonResponse
                ).build()
        def result = responseHandler.generateSuccessResponse()

        then:
        expectedResult.status == result.status
        def entityResult = result.entity as JsonResponse
        def expectedEntity = expectedResult.entity as JsonResponse
        entityResult.status == expectedEntity.status
        entityResult.message == expectedEntity.message
        entityResult.data == expectedEntity.data
    }

    def "test generating success response with data"() {
        given:
        dataMock.put("fake", "data")

        when:
        def expectedJsonResponse = new JsonResponse(SUCCESS_STATUS, Optional.empty(), Optional.of(dataMock))
        def expectedResult = Response
                .status(HTTP_OK_STATUS_CODE)
                .entity(
                        expectedJsonResponse
                ).build()
        def result = responseHandler.generateSuccessResponseWith(dataMock)

        then:
        expectedResult.status == result.status
        def entityResult = result.entity as JsonResponse
        def expectedEntity = expectedResult.entity as JsonResponse
        entityResult.status == expectedEntity.status
        entityResult.message == expectedEntity.message
        entityResult.data == expectedEntity.data
    }

    def "test raising exception when data should not be null"() {
        when:
        responseHandler.generateSuccessResponseWith(null)

        then:
        thrown Exception
    }

    def "test success of generating error response"() {
        when:
        def expectedJsonResponse = new JsonResponse(ERROR_STATUS, Optional.of(messageMock), Optional.empty())
        def expectedResult = Response
                .status(HTTP_BAD_REQUEST_ERROR_STATUS_CODE)
                .entity(
                        expectedJsonResponse
                ).build()
        def result = responseHandler.generateErrorResponseWith(messageMock)

        then:
        expectedResult.status == result.status
        def entityResult = result.entity as JsonResponse
        def expectedEntity = expectedResult.entity as JsonResponse
        entityResult.status == expectedEntity.status
        entityResult.message == expectedEntity.message
        entityResult.data == expectedEntity.data
    }

    def "test raising exception when error message should not be null"() {
        when:
        responseHandler.generateErrorResponseWith(null)

        then:
        thrown Exception
    }

    def "test success of generating fail response"() {
        when:
        def expectedJsonResponse = new JsonResponse(FAIL_STATUS, Optional.of(messageMock), Optional.empty())
        def expectedResult = Response
                .status(HTTP_FAIL_STATUS_CODE)
                .entity(
                        expectedJsonResponse
                ).build()
        def result = responseHandler.generateFailResponseWith(messageMock)

        then:
        expectedResult.status == result.status
        def entityResult = result.entity as JsonResponse
        def expectedEntity = expectedResult.entity as JsonResponse
        entityResult.status == expectedEntity.status
        entityResult.message == expectedEntity.message
        entityResult.data == expectedEntity.data
    }

    def "test raising exception when fail message should not be null"() {
        when:
        responseHandler.generateFailResponseWith(null)

        then:
        thrown Exception
    }
}
