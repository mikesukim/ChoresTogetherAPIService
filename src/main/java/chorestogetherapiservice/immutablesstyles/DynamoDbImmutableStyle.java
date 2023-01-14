package chorestogetherapiservice.immutablesstyles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.immutables.value.Value;

/* Immutables's style annotation for DynamoDb item types,
* which is mapped to DynamoDB's table by DynamoDBClient.
* This annotation is a must for those types. */
@SuppressWarnings("checkstyle:MissingJavadocType")
@Target({ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.CLASS)
@Value.Style(
    allParameters = true,
    strictBuilder = true,
    builderVisibility = Value.Style.BuilderVisibility.PUBLIC,
    // Setting visibility as PRIVATE creates Builder class to be not inner-class of \
    // Immutable implementation class. With the default visibility, Builder class created as \
    // inner static class of the Immutable implementation class, \
    // and referring inner static class at @DynamoDbImmutable \
    // (e.x. @DynamoDbImmutable(builder = ImmutableUserItem.Builder.class))
    // will cause error of Builder class is not detected.
    visibility = Value.Style.ImplementationVisibility.PRIVATE
)
public @interface DynamoDbImmutableStyle {}