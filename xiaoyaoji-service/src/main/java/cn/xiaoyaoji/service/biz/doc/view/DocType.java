package cn.xiaoyaoji.service.biz.doc.view;

/**
 * 内容类型
 * @author: zhoujingjie
 * @Date: 17/4/1
 */
public enum  DocType {

    //http 接口
    SYS_HTTP("sys.http"),

    //websocket
    SYS_WEBSOCKET("sys.websocket"),

    //文档
    SYS_DOC_MD("sys.doc.md"),

    //文档 wangEditor
    SYS_DOC_RICH_TEXT("sys.doc.richtext"),

    //第三方
    SYS_THIRDPARTY("sys.thirdparty"),

    //文件夹
    SYS_FOLDER("sys.folder"),

    //快捷方式
    SYS_SHORT_CUT("sys.shortcut"),

    //模块 类似于文件夹,在前端显示上有区分
    SYS_MODULE("sys.module");

    private String typeName;
    DocType(String typeName) {
        this.typeName=typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public static DocType parse(String typeName) {

        for (DocType docType : DocType.values()) {
            if (docType.getTypeName().equals(typeName)) {
                return docType;
            }
        }
        return null;
    }
}
