package com.cecilylove.blueocean.web.handler;

import com.cecilylove.blueocean.core.api.Result;
import com.cecilylove.blueocean.core.enums.CommonRespCode;
import com.cecilylove.blueocean.core.exception.BusinessException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器 (企业级完整版)
 * 统一拦截 Controller 抛出的异常，返回标准 JSON 格式
 *
 * @author Wang Li Hong
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // =================================================================================
    // 1. 业务与系统基础异常
    // =================================================================================

    /**
     * 捕获自定义业务异常
     * 一般由业务代码手动抛出
     *
     * @param e 业务异常
     * @return 标准结果 Result
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: code={}, msg={}", e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 捕获系统未知异常 (兜底策略)
     * 防止空指针等未预料的异常直接将堆栈信息暴露给前端
     *
     * @param e 未知异常
     * @return 500 系统错误
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统未知异常", e);
        return Result.error(CommonRespCode.SYSTEM_ERROR);
    }

    // =================================================================================
    // 2. 参数校验异常 (分开处理以提供更精准的错误提示)
    // =================================================================================

    /**
     * 处理 @RequestBody JSON 参数校验异常
     * 场景：前端 POST JSON 数据，DTO 对象上的 @NotNull, @Size 等校验失败
     *
     * @param e MethodArgumentNotValidException
     * @return 错误提示 (拼接所有失败字段)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("JSON参数校验失败: {}", msg);
        return Result.error(CommonRespCode.PARAM_ERROR.getCode(), msg);
    }

    /**
     * 处理 Form 表单或 GET 对象参数绑定异常
     * 场景：前端提交 Form Data 或 GET 请求参数绑定到对象时校验失败
     *
     * @param e BindException
     * @return 错误提示
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        String msg = e.getAllErrors().stream()
                .map(org.springframework.validation.ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("表单参数绑定失败: {}", msg);
        return Result.error(CommonRespCode.PARAM_ERROR.getCode(), msg);
    }

    /**
     * 处理单参数校验异常 (@Validated + @RequestParam)
     * 场景：在 Controller 类上加了 @Validated，并在方法参数上直接使用 @Min(1) 等注解
     *
     * @param e ConstraintViolationException
     * @return 错误提示
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.warn("接口参数校验失败: {}", msg);
        return Result.error(CommonRespCode.PARAM_ERROR.getCode(), msg);
    }

    /**
     * 捕获 Jakarta Validation 基础异常 (校验框架兜底)
     *
     * @param e ValidationException
     * @return 错误提示
     */
    @ExceptionHandler(ValidationException.class)
    public Result<Void> handleValidationException(ValidationException e) {
        log.warn("Validation校验异常: {}", e.getMessage());
        return Result.error(CommonRespCode.PARAM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 处理非法参数异常
     *
     * @param e IllegalArgumentException
     * @return 错误提示
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("非法参数异常: {}", e.getMessage());
        return Result.error(CommonRespCode.PARAM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 可能会抛出此异常，而非传统的 ConstraintViolationException
     * 可能会抛出此异常，而非传统的 ConstraintViolationException
     *
     * @param e HandlerMethodValidationException
     * @return 错误提示
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public Result<Void> handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        // 提取所有验证结果
        String msg = e.getParameterValidationResults().stream()
                // 获取每个参数的错误
                .flatMap(result -> result.getResolvableErrors().stream())
                // 获取错误消息 (e.g. "必须大于等于1")
                .map(org.springframework.context.MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("方法参数校验失败: {}", msg);
        return Result.error(CommonRespCode.PARAM_ERROR.getCode(), msg);
    }

    // =================================================================================
    // 3. 请求解析与 IO 异常
    // =================================================================================

    /**
     * 处理 HTTP 消息不可读异常
     * 场景：前端传的 JSON 格式错误（如少括号、类型不匹配）
     *
     * @param e HttpMessageNotReadableException
     * @return 友好的 JSON 格式错误提示
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("HTTP消息不可读(JSON格式错误): {}", e.getMessage());
        return Result.error(CommonRespCode.PARAM_ERROR.getCode(), "请求Body格式错误，请检查JSON语法");
    }

    /**
     * 处理缺少必填参数异常
     * 场景：接口定义 @RequestParam(required=true) 但前端未传
     *
     * @param e MissingServletRequestParameterException
     * @return 提示缺少的参数名
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String msg = "缺少必填参数: " + e.getParameterName();
        log.warn("请求参数缺失: {}", msg);
        return Result.error(CommonRespCode.PARAM_ERROR.getCode(), msg);
    }

    /**
     * 处理其他 Servlet 请求绑定异常 (参数处理兜底)
     *
     * @param e ServletRequestBindingException
     * @return 错误提示
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    public Result<Void> handleServletRequestBindingException(ServletRequestBindingException e) {
        log.warn("Servlet参数绑定异常: {}", e.getMessage());
        return Result.error(CommonRespCode.PARAM_ERROR.getCode(), "请求参数绑定失败");
    }

    /**
     * 处理文件上传大小超限异常
     * 场景：上传的文件超过了 application.yml 中配置的 max-file-size
     *
     * @param e MaxUploadSizeExceededException
     * @return 友好的超限提示
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.warn("文件上传超限: {}", e.getMessage());
        return Result.error(CommonRespCode.PARAM_ERROR.getCode(), "文件大小超过限制，请压缩后上传");
    }

    // =================================================================================
    // 4. 资源异常
    // =================================================================================


    /**
     * 处理 404 资源未找到异常
     * 场景：请求了不存在的接口路径
     *
     * @param e NoResourceFoundException
     * @return 404 错误
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Void> handleNoResourceFoundException(NoResourceFoundException e) {
        return Result.error(404, "请求的接口路径不存在");
    }
}