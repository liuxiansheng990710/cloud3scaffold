package com.example.commons.web.utils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import com.example.commons.core.enums.LogEnum;
import com.example.commons.core.log.models.RequestLogger;
import com.example.commons.core.model.Errors;
import com.example.commons.core.model.Responses;
import com.example.commons.core.utils.TypeUtils;
import com.example.commons.web.servlet.cons.RequestCons;
import com.example.commons.web.servlet.response.ResponseWrapper;
import com.google.common.base.Throwables;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtils {

    /**
     * 成功返回，仅限于String类型返回
     *
     * @param object
     * @return
     */
    public static Responses<String> success(String object) {
        return success(ApplicationUtils.getResponse(), HttpStatus.OK, object);
    }

    /**
     * 成功返回，带返回结果
     *
     * @param response
     * @param object
     * @param <T>
     * @return
     */
    public static <T> Responses<T> success(HttpServletResponse response, T object) {
        return success(response, HttpStatus.OK, object);
    }

    /**
     * 成功返回，带返回状态码，带返回结果
     *
     * @param response
     * @param status
     * @param object
     * @param <T>
     * @return
     */
    public static <T> Responses<T> success(HttpServletResponse response, HttpStatus status, T object) {
        response.setStatus(status.value());
        Responses<T> responses = new Responses<>();
        responses.setStatus(status.value());
        responses.setResult(object);
        responses.setTime(new Date());
        return responses;
    }

    /**
     * 发送错误信息
     *
     * @param request
     * @param response
     * @param errors
     */
    public static void sendFail(HttpServletRequest request, HttpServletResponse response, Errors errors, Exception exception) {
        responseAndPrint(request, new ResponseWrapper(response, errors), failure(errors, exception));
    }

    /**
     * 发送错误信息，带自定义异常信息
     *
     * @param request
     * @param response
     * @param errors
     */
    public static void sendFail(HttpServletRequest request, HttpServletResponse response, Errors errors, Exception exception, String exceptionMsg) {
        responseAndPrint(request, new ResponseWrapper(response, errors), failure(errors, exception, exceptionMsg));
    }

    /**
     * 失败返回
     *
     * @param errors
     * @param throwable
     * @param <T>
     * @return
     */
    public static <T> Responses<T> failure(Errors errors, Throwable throwable) {
        Responses<T> failureResponse = new Responses<>();
        String validExceptionMsg = getValidExceptionMsg(throwable);
        return failureResponse.setError(errors.getError())
                .setMsg(StringUtils.isNotBlank(validExceptionMsg) ? validExceptionMsg : errors.getMsg())
                .setException(getValidException(errors.getStatus(), throwable))
                .setRanking(errors.getRanking())
                .setStatus(errors.getStatus())
                .setTime(new Date());
    }

    /**
     * 失败返回，自定义异常信息
     *
     * @param errors
     * @param <T>
     * @return
     */
    public static <T> Responses<T> failure(Errors errors, Throwable throwable, String exceptionMsg) {
        Responses<T> failureResponse = new Responses<>();
        return failureResponse.setError(errors.getError())
                .setMsg(errors.getMsg())
                .setException(StringUtils.isNotBlank(exceptionMsg) ? exceptionMsg : getValidException(errors.getStatus(), throwable))
                .setRanking(errors.getRanking())
                .setStatus(errors.getStatus())
                .setTime(new Date());
    }

    /**
     * 获取参数校验异常信息
     *
     * @param throwable
     * @return
     */
    private static String getValidExceptionMsg(Throwable throwable) {
        // BindException：请求参数绑定到方法参数或表单数据绑定Bean对象时，字段验证失败，类型不匹配等
        if (Objects.nonNull(throwable) && throwable instanceof BindException) {
            List<ObjectError> allErrors = ((BindException) throwable).getBindingResult().getAllErrors();
            return allErrors.stream().findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage).orElse(null);
            // ConstraintViolationException：在字段上使用注解进行验证，验证失败时抛出的异常，其中包含有关约束违例的详细信息，如哪个字段违反了哪个约束、违反约束的消息等
        } else if (Objects.nonNull(throwable) && throwable instanceof ConstraintViolationException) {
            return ((ConstraintViolationException) throwable).getConstraintViolations().stream().findFirst().map(ConstraintViolation::getMessage).orElse(null);
        }
        return null;
    }

    /**
     * 获取参数校验异常
     *
     * @param status
     * @param throwable
     * @return
     */
    private static String getValidException(int status, Throwable throwable) {
        if (Objects.isNull(throwable)) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        //MethodArgumentNotValidException：使用 @Valid 注解或者 Spring 的 @Validated 注解进行参数验证失败
        if (throwable instanceof MethodArgumentNotValidException) {
            result.append("校验失败:");
            List<ObjectError> allErrors = ((MethodArgumentNotValidException) throwable).getBindingResult().getAllErrors();
            allErrors.stream().findFirst().ifPresent(error -> result.append(((FieldError) error).getField()).append("字段规则为").append(error.getDefaultMessage()));
            return result.toString();
            //MissingServletRequestParameterException：请求中缺少必要的参数时抛出的异常
        } else if (throwable instanceof MissingServletRequestParameterException ex) {
            result.append("参数字段");
            result.append(ex.getParameterName());
            //MissingPathVariableException：请求中缺少必要的路径变量时抛出的异常
        } else if (throwable instanceof MissingPathVariableException ex) {
            result.append("路径字段");
            result.append(ex.getVariableName());
            //ConstraintViolationException：使用 Java Validation API 进行方法参数验证时抛出，表示约束验证失败
        } else if (throwable instanceof ConstraintViolationException ex) {
            result.append("方法.参数字段");
            ex.getConstraintViolations().stream().findFirst().ifPresent(constraintViolation -> result.append(constraintViolation.getPropertyPath().toString()));
        }
        if (StringUtils.isNotBlank(result)) {
            result.append("校验不通过");
            return result.toString();
        }
        if (status >= HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            return StringUtils.substring(Throwables.getStackTraceAsString(throwable), 0, 4096);
        }
        return TypeUtils.castToString(throwable);
    }

    /**
     * 响应以及打印日志
     *
     * @param request
     * @param response
     * @param result
     */
    public static void responseAndPrint(HttpServletRequest request, HttpServletResponse response, Object result) {
        RequestLogger requestLogger = getRequestLogger(request, result);
        RequestLogger.print(response.getStatus(), RequestLogger.LOG_PREFIX + requestLogger);
        //响应Json格式
        if (response instanceof ResponseWrapper) {
            ((ResponseWrapper) response).writeValueAsJson(result);
        }

    }

    /**
     * 获取请求日志参数体
     *
     * @param request
     * @param result
     * @return
     */
    public static RequestLogger getRequestLogger(HttpServletRequest request, Object result) {
        Long beiginTime = (Long) request.getAttribute(RequestCons.REQ_BEGIN_TIME);
        Long endTime = System.currentTimeMillis();

        RequestLogger logger = new RequestLogger();
        logger.setParameterMap(request.getParameterMap())
                .setStartTime(TypeUtils.castToDate(request.getAttribute(RequestCons.REQ_BEGIN_TIME)))
                .setRequestBody(request.getAttribute(RequestCons.REQ_BODY))
                .setUrl((String) request.getAttribute(RequestCons.REQ_URL))
                .setMapping((String) request.getAttribute(RequestCons.REQ_MAPPING))
                .setMethod((String) request.getAttribute(RequestCons.REQ_METHOD))
                .setData(result)
                .setIp(Optional.ofNullable(request.getHeader(RequestCons.REQ_X_REAL_IP)).orElse(IpUtils.getIpAddr(request)))
                .setType(LogEnum.REQUEST)
                .setRunTime((Objects.isNull(beiginTime) ? 0 : endTime - beiginTime) + "ms")
                .setOrigin((String) request.getAttribute(RequestCons.REQ_SERVER_NAME))
                .setEnvironment((String) request.getAttribute(RequestCons.REQ_ENVIRONMENT))
        ;
        return logger;
    }

}
