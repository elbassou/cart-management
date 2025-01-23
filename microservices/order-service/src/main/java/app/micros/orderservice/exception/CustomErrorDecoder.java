package app.micros.orderservice.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {

     return   switch (response.status()) {
            case 400 -> new OrderBadRequestException(s);

            case 404 -> new OrderNotFoundException(s);

            case 500 -> new OrderIntenatlException(s);

            default -> new Exception(s);

        };

    }
}
