package com.example.interviewPrep.quiz.question.service;

import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.company.domain.Company;
import com.example.interviewPrep.quiz.company.repository.CompanyRepository;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.exception.advice.ErrorCode;
import com.example.interviewPrep.quiz.question.domain.Question;
import com.example.interviewPrep.quiz.question.dto.FilterDTO;
import com.example.interviewPrep.quiz.question.dto.QuestionRequest;
import com.example.interviewPrep.quiz.question.dto.QuestionResponse;
import com.example.interviewPrep.quiz.question.repository.QuestionRepository;
import com.example.interviewPrep.quiz.questionCompany.domain.QuestionCompany;
import com.example.interviewPrep.quiz.questionCompany.repository.QuestionCompanyRepository;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_COMPANY;
import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_FOUND_QUESTION;
import static com.example.interviewPrep.quiz.question.dto.QuestionResponse.createQuestionResponse;


@Slf4j
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final CompanyRepository companyRepository;
    private final QuestionCompanyRepository questionCompanyRepository;
    private final AnswerRepository answerRepository;

    public QuestionService(QuestionRepository questionRepository, CompanyRepository companyRepository, QuestionCompanyRepository questionCompanyRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.companyRepository = companyRepository;
        this.questionCompanyRepository = questionCompanyRepository;
        this.answerRepository = answerRepository;
    }


    public Question createQuestion(QuestionRequest questionRequest) {
        String title = questionRequest.getTitle();
        String type = questionRequest.getType();
        String difficulty = questionRequest.getDifficulty();
        boolean freeOfCharge = false;

        Question question = new Question(title, type, difficulty, freeOfCharge);
        questionRepository.save(question);
        return question;
    }

    //@Cacheable(value = "question", key="#id")
    public QuestionResponse getQuestion(Long id) {
        Question question = findQuestion(id);
        return createQuestionResponse(question);
    }

    public Question updateQuestion(Long id, QuestionRequest questionRequest) {
        Question question = findQuestion(id);
        question.changeTitleOrType(questionRequest.getTitle(), questionRequest.getType());
        return question;
    }

    public Question deleteQuestion(Long id) {
        Question question = findQuestion(id);
        questionRepository.delete(question);
        return question;
    }

    public Question findQuestion(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new CommonException(NOT_FOUND_QUESTION, ErrorCode.NOT_FOUND_QUESTION.getMessage(id)));
    }

    public int getTotalQuestionsCount() {
        List<Question> questions = questionRepository.findAll();
        return questions.size();
    }


    //@Cacheable(value = "questionDTO", key="#pageable.pageSize.toString().concat('-').concat(#pageable.pageNumber)")
    // @Timer
    public Page<QuestionResponse> findByType(String type, Pageable pageable) {
        Long memberId = JwtUtil.getMemberId();
        Page<Question> questions = findQuestionsByTypeAndPageable(type, pageable);

        if (questions.getContent().isEmpty()) {
            throw new CommonException(NOT_FOUND_QUESTION);
        }

        return makeQuestionResponses(memberId, questions);
    }

    public List<QuestionResponse> findByCompany(String companyName) {

        Company company = companyRepository.findByName(companyName).orElseThrow(() -> new CommonException(NOT_FOUND_COMPANY));
        Long companyId = company.getId();

        List<Question> questions = findQuestionsByCompany(companyId);
        if (questions.isEmpty()) {
            throw new CommonException(NOT_FOUND_QUESTION);
        }

        return makeQuestionResponses(questions);

    }

    public Page<Question> findQuestionsByTypeAndPageable(String type, Pageable pageable) {
        if (type == null) {
            return questionRepository.findAllBy(pageable);
        }
        return questionRepository.findByType(type, pageable);
    }

    public List<Question> findQuestionsByCompany(Long companyId) {
        List<QuestionCompany> questionCompanies = questionCompanyRepository.findByCompanyId(companyId);

        List<Question> questions = new ArrayList<>();
        for (QuestionCompany questionCompany : questionCompanies) {
            Long questionId = questionCompany.getQuestion().getId();
            Question question = questionRepository.findById(questionId).orElseThrow(() -> new CommonException(NOT_FOUND_QUESTION));
            questions.add(question);
        }

        return questions;
    }

    public List<QuestionResponse> makeQuestionResponses(List<Question> questions) {
        return questions.stream().map(
                        q -> QuestionResponse.builder()
                                .id(q.getId())
                                .title(q.getTitle())
                                .type(q.getType())
                                .difficulty((q.getDifficulty()))
                                .freeOfCharge(q.isFreeOfCharge())
                                .build())
                .collect(Collectors.toList());
    }

    public Page<QuestionResponse> makeQuestionResponses(Long memberId, Page<Question> questions) {

        Set<Long> answers;

        if (memberId == 0L) {
            answers = new HashSet<>();
        } else {
            List<Long> questionIds = questions.getContent().stream().map(Question::getId).collect(Collectors.toList());
            answers = answerRepository.findMyAnswer(questionIds, memberId);
        }

        return questions.map(q -> QuestionResponse.builder()
                .type(q.getType())
                .title(q.getTitle())
                .difficulty(q.getDifficulty())
                .freeOfCharge(q.isFreeOfCharge())
                .build());
    }


    public List<FilterDTO> findFilterLanguage() {
        List<FilterDTO> filterDTOs = new ArrayList<>();

        List<String> languages = questionRepository.findAllByLanguage();

        for (String language : languages) {
            FilterDTO filterDTO = FilterDTO.builder()
                    .language(language)
                    .build();
            filterDTOs.add(filterDTO);
        }

        return filterDTOs;
    }


}