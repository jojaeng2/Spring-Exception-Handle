# 3 ways to handle exceptions in spring
## Summary
> Spring에서 제공하는 3가지 예외 처리 방법에 대해 공부한다.

## 개요
> Java에서 예외 처리를 하기 위해서는 try-catch 문법을 사용해야 한다. 하지만 모든 코드에 try-catch를 붙이는 것은 가독성을 떨어뜨리기 때문에 Spring은 이 문제를 해결하기 위해 예외 처리라는 공통 관심사를 메인 로직으로부터 분리하는 다양한 예외 처리 방식을 고안하였고, 예외 처리 전략을 추상화한 HandlerExceptionResolver interface를 만들었다. 

``` java
    public interface HandlerExceptionResolver { 
        ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex); }

```
> 위의 Object type handler는 예외가 발생한 Controller Object이다. 만약 Controller에서 Exception이 throw되면, Dispatcher Servlet까지 전달된다.  
> Dispatcher Servlet은 상황에 맞는 적합한 예외 처리 전략을 위해 HandlerExceptionResolver 구현체들을 빈으로 등록해서 관리하며, 적용 가능한 예외 처리기를 찾아 예외 처리를 한다.

## @ResponseStatus
> @ResponseStatus는 Error HTTP Status를 변경하도록 도와주는 annotation이며, 아래와 같은 경우에 적용할 수있다.  
1) Exception class에 사용
2) @ExceptionHandler와 함께 사용
3) @RestControllerAdvice와 함께 사용  
``` JAVA
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchElementFoundException extends RuntimeException{
}


```
> 예를들면 위와 같은 Exception Class에 @ResponseStatus로 response status를 지정해줄 수있다.
> 그리고 이제 ResponseStatusExceptionResolver가 지정해준 상태로 Error response이 전송된다.

``` JAVASCRIPT
{ 
    "timestamp": "2021-12-31T03:35:44.675+00:00", 
    "status": 404, 
    "error": "Not Found", 
    "path": "/product/5000" 
}

```
> 하지만 @ResponseStatus는 아래와 같은 명확한 한계점을 가지고 있다.
1) error response의 payload를 수정할 수없다.
2) 예외 상황마다 예외 클래스를 추가해야한다.
3) 예외 클래스와 강하게 결합되어 모든 예외에 대해 동일한 상태와 에러 메시지를 반환하게 된다.  

## @ExceptionHandler
> @ExceptionHandler는 매우 유연하게 Error를 처리할 수있는 방법을 제공한다. @ExceptionHandler는 아래의 경우에 annotation을 추가함으로써 Error를 손쉽게 처리할 수있다.
1) Controller Method
2) @ControllerAdvice나 @RestControllerAdvice가 있는 Method
``` JAVA
@Controller
@RequiredArgsConstructor
public class ExceptionHandlerController {

    private final ProductService productService;

    @GetMapping("/product/exceptionhandler/{id}")
    public ResponseEntity getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @ExceptionHandler(NoSuchElementFoundException.class)
    public ResponseEntity<String> handleNoSuchElementFoundException(NoSuchElementFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

}

```
> 예를들어 위와 같은 Controller Method에 @ExceptionHandler를 추가함으로써 Error를 처리할 수있다. @ExceptionHandler에 의해 발생한 예외는 ExceptionHandler Exception Resolver에 의해 처리가 된다.  
> @ExceptionHandler는 Exception class들을 속성으로 받아 처리할 예외를 지정할 수있다.  
> 또한 @ResponseStatus와도 결합이 가능한데, 만약 ResponseEntity에서도 Status를 지정하고, @ResponseStatus도 있다면 ResponseEntity가 우선순위를 가진다. 
> ExceptionHandler는 @ResponseStatus와 달리 error response payload를 자유롭게 다룰 수있다는 점에서 유연하다.    
  
## @ControllerAdvice and @RestControllerAdvice
> Spring은 @ExceptionHandler의 한계를 극복하고자 전역적으로 예외를 처리할 수있는 @ControllerAdvice와 @RestControllerAdvice를 Spring 3.2, Spring 4.3부터 제공한다.   

``` JAVA
@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
@Documented 
@ControllerAdvice 
@ResponseBody 
public @interface RestControllerAdvice { 
    ... 
}

@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME) 
@Documented 
@Component 
public @interface ControllerAdvice { 
    ... 
}
```
> ControllerAdvice는 여러 컨트롤러에 전역적으로 ExceptionHandler를 적용해준다. 또한 @Component annotation이 붙어있어 ControllerAdvice가 선언된 class는 spring bean으로 등록된다. 그러므로 전역적으로 error를 handling하는 class를 만들어 error 처리를 위임할 수있다.
``` JAVA
@RestControllerAdvice
public class AdviceExceptionHandler {

    @ExceptionHandler(NoSuchElementFoundException.class)
    protected ResponseEntity<?> handleNoSuchElementFoundException(NoSuchElementFoundException e) {
        final ErrorResponse errorResponse = new ErrorResponse("Item Not Found", e.getMessage());


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
```
>ControllerAdvice는 특정 Controller가 아니라 모든 Controller에서 동일하게 적용된다. 만약 특정 클래스에만 제한적으로 적용하고 싶으면, @RestControllerAdvice의 basePackages 등을 설정함으로 제한할 수있다.
