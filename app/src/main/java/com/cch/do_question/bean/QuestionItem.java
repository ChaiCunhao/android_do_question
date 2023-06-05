package com.cch.do_question.bean;

//使用了一个自定义的数据类 QuestionItem 来表示每个题目的状态和题号
public class QuestionItem {
    private int questionNumber;
    private int selected_Index;
    private boolean answered;
    private boolean correct;

    public QuestionItem(int questionNumber, int selected_Index ,boolean answered, boolean correct) {
        this.questionNumber = questionNumber;
        this.selected_Index = selected_Index;
        this.answered = answered;
        this.correct = correct;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public int getSelected_Index() {
        return selected_Index;
    }

    public void setSelected_Index(int selected_Index) {
        this.selected_Index = selected_Index;
    }

    public boolean isAnswered() {
        return answered;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}

