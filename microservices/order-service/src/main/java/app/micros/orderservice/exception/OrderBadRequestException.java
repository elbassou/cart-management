package app.micros.orderservice.exception;

public class OrderBadRequestException extends RuntimeException{
    public OrderBadRequestException(String s) {

        super(s);
    }
}
