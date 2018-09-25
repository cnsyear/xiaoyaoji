package cn.xiaoyaoji.service.biz.doc.view;

import java.util.Collections;
import java.util.List;

/**
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 * <p>
 * http文档格式
 *
 * @author: zhoujingjie
 * Date: 2018/9/25
 */
public class HttpDoc {
    /**
     * 地址
     */
    private String url;
    /**
     * 状态
     */
    private String status;
    /**
     * 请求方法,GET,POST...
     */
    private String requestMethod;
    /**
     * 请求的数据类型。X-WWW-FORM-URLENCODED,JSON,RAW,BINARY
     */
    private String dataType;
    /**
     * 响应类型
     */
    private String contentType;
    /**
     * 描述: html格式
     */
    private String description;

    /**
     * 请求参数
     */
    private List<HttpDocArgs> requestArgs = Collections.emptyList();
    /**
     * 请求头
     */
    private List<HttpDocArgs> requestHeaders = Collections.emptyList();
    /**
     * 响应头
     */
    private List<HttpDocArgs> responseHeaders = Collections.emptyList();
    /**
     * 响应参数
     */
    private List<HttpDocArgs> responseArgs = Collections.emptyList();

    /**
     * 例子
     */
    private String example;
    /**
     * 更多例子
     */
    private List<Example> egs = Collections.emptyList();

    public class Example {
        /**
         * 名称
         */
        private String name;
        /**
         * 内容
         */
        private String body;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<HttpDocArgs> getRequestArgs() {
        return requestArgs;
    }

    public void setRequestArgs(List<HttpDocArgs> requestArgs) {
        this.requestArgs = requestArgs;
    }

    public List<HttpDocArgs> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(List<HttpDocArgs> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public List<HttpDocArgs> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(List<HttpDocArgs> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public List<HttpDocArgs> getResponseArgs() {
        return responseArgs;
    }

    public void setResponseArgs(List<HttpDocArgs> responseArgs) {
        this.responseArgs = responseArgs;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public List<Example> getEgs() {
        return egs;
    }

    public void setEgs(List<Example> egs) {
        this.egs = egs;
    }
}
