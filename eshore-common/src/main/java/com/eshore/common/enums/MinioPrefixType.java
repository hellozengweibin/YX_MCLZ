package com.eshore.common.enums;

import lombok.Getter;

/**
 * @EnumName MinioPrefixType
 * @Description minio文件的前缀类型
 * @Author jianlin.liu
 * @Date 2023/7/20
 * @Version 1.0
 **/
@Getter
public enum MinioPrefixType {


    DEFAULT(0, "default", "默认类型", new String[] {"*"}),
    AUDIO(1, "audio", "媒体文件类型", new String[] {"*"}),
    EMPLOYEE(2, "employee", "知识库文件类型", new String[] {"*"}),
    NOTICE(3, "notice", "公告文件类型", new String[] {"*"}),
    ORDER(4, "order", "工单文件类型", new String[] {"png", "jpg", "jpeg", "doc", "docx", "xls", "xlsx", "pdf", "txt"}),
    WATER_AREA(5, "waterArea", "水域文件类型", new String[] {"png", "jpg", "jpeg", "doc", "docx", "xls", "xlsx", "pdf", "txt"}),

    ;


    private int code;

    /**
     * 类型前缀，用于minio文件目录的前缀
     */
    private String type;

    /**
     * 秒睡
     */
    private String desc;

    /**
     * 允许上传的文件类型，*表示支持所有文件类型上传
     */
    private String[] allowFileType;


    MinioPrefixType(int code, String type, String desc, String[] allowFileType) {
        this.code = code;
        this.type = type;
        this.desc = desc;
        this.allowFileType = allowFileType;
    }

    public static MinioPrefixType findByCode(int code) {
        for (MinioPrefixType value : values()) {
            if (code == value.code) {
                return value;
            }
        }
        return null;
    }

    public static MinioPrefixType findByType(String type) {
        for (MinioPrefixType value : values()) {
            if (type.equals(value.type)) {
                return value;
            }
        }
        return DEFAULT;
    }

    /**
     * 判断类型是否允许
     *
     * @param prefix
     * @return
     */
    public static boolean isPrefixAllow(String prefix) {
        for (MinioPrefixType value : values()) {
            if (prefix.equals(value.type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断文件类型是否允许上传
     *
     * @param ext
     * @return
     */
    public boolean isFileTypeAllow(String ext) {
        if (ext == null) return false;
        String[] allowFileType = this.getAllowFileType();
        if (allowFileType == null) return false;
        String fileExt = ext;
        if (ext.startsWith(".")) fileExt = ext.substring(1);
        for (int i = 0; i < allowFileType.length; i++) {
            String s = allowFileType[i];
            if (s.equals("*") || s.equals(fileExt)) return true;
        }
        return false;
    }
}
