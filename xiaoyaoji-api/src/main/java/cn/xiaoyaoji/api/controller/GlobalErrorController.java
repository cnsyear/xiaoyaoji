package cn.xiaoyaoji.api.controller;

import cn.xiaoyaoji.api.base.SessionTimeoutException;
import cn.xiaoyaoji.service.common.ResultModel;
import cn.xiaoyaoji.service.exception.ServiceException;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　　　　　┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 * 　　┃　　　┃神兽保佑
 * 　　┃　　　┃代码无BUG！
 * 　　┃　　　┗━━━┓
 * 　　┃　　　　　　　┣┓
 * 　　┃　　　　　　　┏┛
 * 　　┗┓┓┏━┳┓┏┛
 * 　　　┃┫┫　┃┫┫
 * 　　　┗┻┛　┗┻┛
 * <p>
 * 全局错误
 *
 * @author zhoujingjie
 *         Date 2018-06-21
 */
@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
@ControllerAdvice
public class GlobalErrorController extends AbstractErrorController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public GlobalErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }


    @ExceptionHandler(value = Exception.class)
    public ResultModel defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        StringBuffer error = new StringBuffer();
        error.append("\n--------------------------代码调试请求信息begin-----------------------------------------------");
        error.append("\n请求路径：" + req.getServletPath());
        error.append("\n请求方式：" + req.getMethod());
        error.append("\n错误信息：" + e.getMessage());
        error.append("\n--------------------------代码调试请求信息end-------------------------------------------------");
        if (e instanceof MaxUploadSizeExceededException) {
            return ResultModel.error("文件大小超出限制");
        } else {
            logger.error("\n请求参数:");
            for (Map.Entry<String, String[]> entry : req.getParameterMap().entrySet()) {
                String[] value = entry.getValue();
                String v = value != null ? Joiner.on(',').join(entry.getValue()) : null;
                logger.error("\n\t\t{}:{}", entry.getKey(), v);
            }
            logger.error(error.toString(), e);
        }
        return ResultModel.error("系统繁忙请重试");
    }

    @ExceptionHandler(value = {ServiceException.class})
    public ResultModel businessExceptionHandler(HttpServletRequest req, ServiceException e) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.error(e.getMessage(), e);
        }
        return new ResultModel(e.getErrorCode(), null, e.getMessage());
    }

    /**
     * 未登录
     */
    @ExceptionHandler(value = SessionTimeoutException.class)
    public ResultModel sessionTimeout() {
        return new ResultModel(ResultModel.NOT_LOGIN, null, "请重新授权");
    }


    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request,
                                  HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(
                request, true));
        response.setStatus(200);
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return (modelAndView != null ? modelAndView : new ModelAndView("error", model));
    }


    @RequestMapping
    @ResponseBody
    public Object error(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> body = getErrorAttributes(request,
                false);
        HttpStatus status = getStatus(request);
        response.setStatus(200);
        logger.error("code=" + status.value() + "\n{}", body.toString());
        return ResultModel.error("系统繁忙请重试");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }


}
