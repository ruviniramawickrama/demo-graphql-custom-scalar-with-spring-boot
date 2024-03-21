/**
 * @author  Ruvini Ramawickrama
 */
package com.example.demo.scalar;

import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;

@Configuration
public class DateTimeScalarConfig {

    public static final GraphQLScalarType dateTimeScalar = GraphQLScalarType.newScalar()
            .name("DateTime")
            .description("Custom Scalar to handle DateTime")
            .coercing(new Coercing() {

                /**
                 * This method converts a value of type 'Long'(milliseconds) to a value of type 'LocalDateTime' to support the DateTime Scalar defined in the GraphQL schema.
                 **/
                @Override
                public Object serialize(@NotNull Object dataFetcherResult, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) throws CoercingSerializeException {
                    System.out.println("Executing serialize");
                    try {
                        Long milliseconds = (Long) dataFetcherResult;
                        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
                    } catch (CoercingSerializeException exception) {
                        throw new CoercingSerializeException("An error occurred while serializing the value: " + exception.getMessage());
                    }
                }

                /**
                 * This method parse a value that is provided as a variable in a GraphQL Query.
                 * It converts a value of type 'LocalDateTime' to a value of type 'Long'(milliseconds).
                 **/
                @Override
                public Object parseValue(@NotNull Object input, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) throws CoercingParseValueException {
                    System.out.println("Executing parseValue");
                    try{
                        String inputAsString = (String) input;
                        return LocalDateTime.parse(inputAsString).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    } catch (RuntimeException exception) {
                        throw new CoercingParseValueException("An error occurred while parsing the value: " + exception.getMessage());
                    }
                }

                /**
                 * This method parse a value that is directly included as a literal in the Query string.
                 * It converts a value of type 'LocalDateTime' to a value of type 'Long'(milliseconds).
                 * (Literal values are part of the query itself, such as strings, numbers, booleans, or enum values.)
                 **/
                @Override
                public Object parseLiteral(@NotNull Value input, @NotNull CoercedVariables variables, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) throws CoercingParseLiteralException {
                    System.out.println("Executing parseLiteral");
                    try {
                        StringValue inputStringValue = (StringValue) input;
                        return LocalDateTime.parse(inputStringValue.getValue()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    } catch (RuntimeException exception) {
                        throw new CoercingParseLiteralException("An error occurred while parsing the literal:"+exception.getMessage());
                    }
                }

                @Override
                public @NotNull Value<?> valueToLiteral(@NotNull Object input, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) {
                    return new StringValue(input.toString());
                }
            })
            .build();
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return builder -> builder.scalar(dateTimeScalar);
    }
}