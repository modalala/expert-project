package com.expert.common.result;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "参数错误"),
    UNAUTHORIZED(401, "未授权或token失效"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误"),

    // 用户相关 1000-1999
    USER_EXISTS(1001, "用户名已存在"),
    PASSWORD_ERROR(1002, "密码错误"),
    USER_NOT_FOUND(1003, "用户不存在"),
    USER_DISABLED(1004, "用户已被禁用"),

    // 专家相关 2000-2999
    EXPERT_NOT_FOUND(2001, "专家不存在"),
    EXPERT_STATUS_ERROR(2002, "专家状态异常"),
    EXPERT_ALREADY_EXISTS(2003, "专家已存在"),

    // 抽取相关 3000-3999
    EXTRACTION_CONDITION_ERROR(3001, "抽取条件不满足"),
    CONFIRM_EXPIRED(3002, "确认已过期"),
    CONFIRM_ALREADY_DONE(3003, "已确认或已拒绝"),

    // 审核相关 4000-4999
    REVIEW_NOT_FOUND(4001, "审核记录不存在"),
    REVIEW_STATUS_ERROR(4002, "审核状态异常");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}