package com.example.interviewPrep.quiz.dto;

import lombok.Builder;
import lombok.Getter;

// @Builder를 사용했기 때문에 @Setter를 따로 선언하지 않았습니다.
@Getter
public class ProblemDTO {

    // 인스턴스 변수를 불변으로 선언해서 오류 가능성을 낮췄습니다.
    private final Long id;
    private final String title;

    // @Builder를 생성자 레벨에서 사용했습니다.
    // @Builder를 생성자 레벨에서 사용하면, 내가 원하는 생성자를 선언하고 그에 맞는 빌더 패턴을 만듭니다.
    // 반면, @Builder를 클래스 레벨에서 사용하면, @AllArgsConstructor를 적용하는 것과 동일한
    // 효과가 있습니다.
    @Builder
    public ProblemDTO(Long id, String title){
       this.id = id;
       this.title = title;
    }

}
