package com.example.interviewPrep.quiz.question.service;

import com.example.interviewPrep.quiz.dto.CreateDto;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.heart.repository.RefHeartRepository;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.domain.QuestionReference;
import com.example.interviewPrep.quiz.question.dto.ReferenceDTO;
import com.example.interviewPrep.quiz.question.repository.QuestionRepository;
import com.example.interviewPrep.quiz.question.repository.ReferenceRepository;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_MEMBER;
import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_QUESTION;
import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_REF;
import static com.example.interviewPrep.quiz.utils.DateFormat.customLocalDateTime;


@Service
@RequiredArgsConstructor
public class ReferenceService {

    private final ReferenceRepository referenceRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final RefHeartRepository refHeartRepository;


    public Page<ReferenceDTO> findAnswerReference(Long id, Pageable pageable){

        Long memberId = JwtUtil.getMemberId();

        Page<QuestionReference> references = referenceRepository.findByRef(id, pageable);
        if(references.getContent().isEmpty()) throw new CommonException(NOT_FOUND_REF);

        return references.map(ref -> ReferenceDTO.builder()
                .id(ref.getId())
                .name(ref.getMember().getName())
                .questionId(ref.getQuestion().getId())
                .link(ref.getLink())
                .createdDate(customLocalDateTime(ref.getCreatedDate()))
                .modifiedDate(customLocalDateTime(ref.getModifiedDate()))
                .modify(!ref.getCreatedDate().equals(ref.getModifiedDate()))
                .myRef(ref.getMember().getId().equals(memberId))
                .heartCnt(refHeartRepository.countRefHeartByReferenceId(ref.getId()))
                .build());
    }


    public CreateDto createReference(ReferenceDTO referenceDTO){

        Member member = findMember(JwtUtil.getMemberId());
        Question question = findQuestion(referenceDTO.getQuestionId());

        QuestionReference reference = QuestionReference.builder()
                .member(member)
                .question(question)
                .link(referenceDTO.getLink())
                .build();

        referenceRepository.save(reference);

        return CreateDto.builder()
                .id(reference.getId())
                .createDate(customLocalDateTime(reference.getCreatedDate()))
                .name(reference.getMember().getName())
                .build();
    }


    public void updateReference(ReferenceDTO referenceDTO){
        QuestionReference reference = findReference(referenceDTO.getId());
        reference.change(referenceDTO.getLink());
        referenceRepository.save(reference);
    }

    public void deleteReference(Long id){
        QuestionReference reference = findReference(id);
        referenceRepository.delete(reference);
    }

    public QuestionReference findReference(Long id){
        return referenceRepository.findById(id).orElseThrow(() -> new CommonException(NOT_FOUND_REF));
    }

    public Question findQuestion(Long id){
        return questionRepository.findById(id).orElseThrow(() -> new CommonException(NOT_FOUND_QUESTION));
    }

    public Member findMember(Long id){
        return memberRepository.findById(id).orElseThrow(()-> new CommonException(NOT_FOUND_MEMBER));
    }

}

