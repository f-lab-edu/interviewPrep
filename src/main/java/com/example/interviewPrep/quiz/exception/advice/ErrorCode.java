package com.example.interviewPrep.quiz.exception.advice;

import lombok.Getter;

@Getter
public enum ErrorCode {


    NOT_FOUND_ANSWER("not_found_answer", "answer를 찾을 수 없습니다."),
    NOT_FOUND_QUESTION("not_found_question", "question을 찾을 수 없습니다."),
    NOT_FOUND_MEMBER("not_found_member", "member를 찾을 수 없습니다."),
    NOT_FOUND_COMMENT("not_found_comment", "comment를 찾을 수 없습니다."),
    NOT_FOUND_EXAM("not_found_exam", "해당 모의고사를 찾을 수 없습니다."),
    NOT_FOUND_ID("not_found_id", "로그인된 ID를 찾을 수 없습니다."),
    NOT_FOUND_TYPE("not_found_type", "해당 type으로 조회할 수 없습니다."),
    EXIST_HEART_HISTORY("exist_heart_history", "이미 좋아요를 눌렀습니다."),
    NOT_EXIST_HEART_HISTORY("not_exist_heart_history", "좋아요 누른 기록이 없어 삭제할 수 없습니다."),
    NOT_FOUND_REF("not_found_reference", "reference를 찾을 수 없습니다."),
    NOT_FOUND_SOCIAL_TYPE("illegal_argument", "알 수 없는 소셜 로그인 형식입니다."),

    MISSING_PARAMETER("missing_parameter", ""),

    NOT_FOUND_LOGIN("not_found_LOGIN", "id, password를 확인해 주세요."),
    NON_LOGIN("non_login", "로그인 상태가 아닙니다."),

    WRONG_ID_TOKEN("wrong_id_token", "로그아웃된 사용자의 토큰입니다."),
    WRONG_TYPE_SIGNATURE("wrong_type_signature", "잘못된 서명 키입니다."),
    WRONG_TOKEN("wrong_token", "유효하지 않은 구성의 토큰입니다"),
    EXPIRED_TOKEN("expried_token", "토큰이 만료되었습니다."),
    INVALID_TOKEN("invaild_token", "잘못된 토큰입니다."),
    FAILED_SOCIAL_LOGIN("failed_social_login", "sns 로그인을 할 수 없습니다."),

    DUPLICATE_NICKNAME("duplicate_nickname", "닉네임이 중복되었습니다."),
    DUPLICATE_EMAIL("duplicate_email", "이메일이 중복되었습니다.");

    private final String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorCode setMissingParameterMsg(String msg) {
        if (this.equals(MISSING_PARAMETER)) {
            setMessage(msg);
        }
        return this;
    }

    private void setMessage(String msg) {
        this.message = msg;
    }
}
