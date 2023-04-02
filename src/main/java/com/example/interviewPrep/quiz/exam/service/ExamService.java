/*
package com.example.interviewPrep.quiz.exam.service;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.answer.dto.AnswerDTO;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.exam.domain.Exam;
import com.example.interviewPrep.quiz.exam.domain.ExamAnswer;
import com.example.interviewPrep.quiz.exam.domain.ExamKit;
import com.example.interviewPrep.quiz.exam.domain.ExamKitQuestion;
import com.example.interviewPrep.quiz.exam.dto.ExamKitListRes;
import com.example.interviewPrep.quiz.exam.dto.ExamKitReq;
import com.example.interviewPrep.quiz.exam.dto.ExamRes;
import com.example.interviewPrep.quiz.exam.dto.ExamkitRes;
import com.example.interviewPrep.quiz.exam.dto.MyExamRes;
import com.example.interviewPrep.quiz.exam.repository.ExamAnswerRepository;
import com.example.interviewPrep.quiz.exam.repository.ExamKitQuestionRepository;
import com.example.interviewPrep.quiz.exam.repository.ExamKitRepository;
import com.example.interviewPrep.quiz.exam.repository.ExamRepository;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.exception.advice.ErrorCode;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.dto.QuestionDTO;
import com.example.interviewPrep.quiz.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.interviewPrep.quiz.utils.DateFormat.customLocalDateTime;
import static com.example.interviewPrep.quiz.utils.JwtUtil.getMemberId;

@RequiredArgsConstructor
@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final ExamKitRepository examKitRepository;
    private final ExamKitQuestionRepository examKitQuestionRepository;
    private final AnswerRepository answerRepository;
    private final ExamAnswerRepository examAnswerRepository;

    public ExamRes saveExam(Long kitId, List<AnswerDTO> answers) {
        Member member = memberRepository.findById(getMemberId()).orElseThrow(
            () -> new CommonException(ErrorCode.NOT_FOUND_ID));

        Exam saveExam = examRepository.save(Exam.builder().kitId(kitId).member(member).build());

        for (AnswerDTO answer : answers) {
            Question question = questionRepository.findById(answer.getQuestionId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_QUESTION));
            Answer saveAnswer = answerRepository.save(Answer.builder()
                .member(member)
                .question(question)
                .content(answer.getContent())
                .build());
            examAnswerRepository.save(ExamAnswer.builder()
                .exam(saveExam)
                .answer(saveAnswer)
                .build());
        }

        return ExamRes.builder().title(examKitRepository.findById(saveExam.getKitId()).get().getTitle()).build();
    }

    public MyExamRes readExam(Long examId) {
        Exam exam = examRepository.findById(examId).orElseThrow(
            () -> new CommonException(ErrorCode.NOT_FOUND_EXAM)
        );

        ExamKit examKit = examKitRepository.findById(exam.getKitId()).orElseThrow();

        List<AnswerDTO> answers = examAnswerRepository.findByExamId(exam.getId()).stream().map(
            x -> AnswerDTO.builder().questionId(x.getAnswer().getId()).content(x.getAnswer().getContent()).build()
        ).collect(Collectors.toList());

        HashMap<String, String> questionAndAnswer = new HashMap<>();

        for (AnswerDTO dto : answers) {
            questionAndAnswer.put(questionRepository.findById(dto.getQuestionId()).orElseThrow().getTitle(),dto.getContent());
        }
        return MyExamRes.builder().title(examKit.getTitle()).questionAndAnswer(questionAndAnswer).build();
    }

    public List<ExamKitListRes> findExamKit() {
        List<ExamKit> kits = examKitRepository.findAll();

        return kits.stream().map(x -> ExamKitListRes.builder()
            .id(x.getId())
            .title(x.getTitle())
            .duration(x.getDuration())
            .picture(x.getPicture())
            .build()).collect(Collectors.toList());
    }

    public ExamKit saveExamKitById(ExamKitReq dto) {
        ExamKit examKit = examKitRepository.save(ExamKit.builder()
            .title(dto.getTitle())
            .duration(dto.getDuration())
            .picture(dto.getPicture())
            .build());

        List<Question> questions = dto.getQuestions().stream()
            .map(x -> questionRepository.findById(x).orElseThrow(
                () -> new CommonException(ErrorCode.NOT_FOUND_QUESTION)))
            .collect(Collectors.toList());

        for (Question question : questions) {
            examKitQuestionRepository.save(ExamKitQuestion.builder()
                .examKit(examKit)
                .question(question)
                .build());
        }

        return examKit;
    }

    public ExamkitRes loadExamQuestion(Long id) {
        ExamKit examKit = examKitRepository.findById(id).get();
        List<Question> questions = examKitQuestionRepository.findByExamKitId(id).stream()
            .map(ExamKitQuestion::getQuestion).collect(Collectors.toList());
        List<QuestionDTO> questionDTOs = questions.stream().map(x -> QuestionDTO.builder().id(x.getId()).title(x.getTitle()).type(x.getType()).build())
            .collect(Collectors.toList());
        return ExamkitRes.builder().id(id)
            .title(examKit.getTitle())
            .duration(examKit.getDuration())
            .questions(questionDTOs)
            .build();
    }

    public List<ExamRes> findMyExam() {
        Long memberId = getMemberId();
        return examRepository.findByMemberId(memberId).stream().map(x -> ExamRes.builder()
                .title(examKitRepository.findById(x.getKitId()).get().getTitle())
                .id(x.getId())
                .createTime(customLocalDateTime(x.getCreatedDate()))
                .build())
            .collect(Collectors.toList());
    }
}
*/